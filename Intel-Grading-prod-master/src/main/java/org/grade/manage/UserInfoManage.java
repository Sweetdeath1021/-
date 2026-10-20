package org.grade.manage;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.grade.bean.request.User.PasswordForgetRequest;
import org.grade.bean.request.User.UserLoginRequest;
import org.grade.bean.request.User.UserRegisterRequest;
import org.grade.bean.request.User.UserUpdateRequest;
import org.grade.bean.response.ProfileResponse;
import org.grade.bean.response.UserInfoResponse;
import org.grade.common.Result;
import org.grade.common.ServiceException;
import org.grade.model.LoginUser;
import org.grade.model.User;
import org.grade.pattern.UserRegisterFactory;
import org.grade.pattern.UserRegisterTemplate;
import org.grade.service.IUserService;
import org.grade.utils.ConverterUtil;
import org.grade.utils.JwtUtil;
import org.grade.utils.OSSUtil;
import org.grade.utils.SecurityUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.grade.constant.OSSConstant.URL_STORAGE_AVATAR;
import static org.grade.constant.RedisConstant.LOGIN_CODE_KEY;

/**
 * @author lixin
 * @date 2024/5/28
 */
@Service
public class UserInfoManage {
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private OSSUtil ossUtil;
    @Resource
    private UserRegisterFactory factory;
    @Resource
    private IUserService userService;
    @Resource
    private ConverterUtil converterUtil;

    public Result login(UserLoginRequest user) {
        //AuthenticationManager 进行用户认证，校验手机号和密码是否正确
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserAccount(), user.getUserPassword());
        Authentication authenticate;
        try {
            authenticate = authenticationManager.authenticate(authenticationToken);
        } catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }
        //认证失败给出提示
        //认证通过，生成jwt并返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getUserId();
        String jwtToken = JwtUtil.createToken(userId);
        Map<String, String> map = new HashMap<>();

        stringRedisTemplate.opsForValue()
                .set(LOGIN_CODE_KEY + userId, JSONUtil.toJsonStr(loginUser));
        // 返回给前端
        User newUser = loginUser.getUser();
        Integer roleId = userService.getRoleId(newUser.getUserId());
        map.put("token", jwtToken);
        map.put("school", newUser.getUserSchool());
        map.put("roleId", String.valueOf(roleId));
        map.put("userId", newUser.getUserId());
        return Result.ok(map);
    }

    public Result logout() {
        // 获取SecurityContextHolder中的用户id
        String userId = SecurityUtil
                .getUserFromHolder().getUserId();

        // 删除redis中的值
        stringRedisTemplate
                .delete(LOGIN_CODE_KEY + userId);
        return Result.ok("注销成功");
    }

    public Result register(MultipartFile file, UserRegisterRequest userRegisterRequest) throws IOException {
        int roleId = Integer.parseInt(userRegisterRequest.getRoleId());

        // 调用工厂方法创建实例
        UserRegisterTemplate template = factory.newInstance(roleId);

        return template.register(file, userRegisterRequest);
    }

    public Result info() {
        // 获取SecurityContextHolder中的用户
        User user = SecurityUtil.getUserFromHolder();
        UserInfoResponse response = UserInfoResponse.builder()
                .userName(user.getUserName())
                .userSchool(user.getUserSchool())
                .userEmail(user.getUserEmail())
                .userSex(user.getUserSex())
                .userPhone(user.getUserAccount())
                .userImage(user.getUserImage()).build();

        return Result.ok(response);
    }

    public Result update(UserUpdateRequest userUpdateRequest) {
        // 获取SecurityContextHolder中的用户id
        String userId = SecurityUtil
                .getUserFromHolder().getUserId();

        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getUserId, userId);

        // 获取dto类的所有字段
        User user = converterUtil.req2User(userUpdateRequest);

        if (!userService.update(user, wrapper)) {
            throw new ServiceException("update.error");
        }

        // 更新缓存
        SecurityUtil.UpdateCache(user, stringRedisTemplate);

        return Result.ok();
    }

    public Result change(String rawPassword, String newPassword) {
        // 获取SecurityContextHolder中的用户原始密码
        User user = SecurityUtil.getUserFromHolder();
        String password = user.getUserPassword();
        // 设置查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserId, user.getUserId());

        //如果原始密码与数据库不同
        if (!passwordEncoder.matches(rawPassword, password)) {
            throw new ServiceException("raw.pwd.error");
        }
        // 新密码加密后存入数据库
        user.setUserPassword(passwordEncoder.encode(newPassword));

        if (!userService.update(user, wrapper)) {
            throw new ServiceException("update.error");
        }
        // 更新缓存
        SecurityUtil.UpdateCache(user, stringRedisTemplate);

        return Result.ok();
    }

    public Result forget(PasswordForgetRequest request) {
        if (StrUtil.isEmpty(request.getUserAccount())
                || StrUtil.isEmpty(request.getUserEmail())
                || StrUtil.isEmpty(request.getUserPassword())) {
            throw new ServiceException("请正确填写数据");
        }
        // 设置查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserAccount, request.getUserAccount());
        wrapper.eq(User::getUserEmail, request.getUserEmail());

        // 获取数据库中的用户
        User userRaw = userService.getOne(wrapper);
        if (Objects.isNull(userRaw)) {
            return Result.fail("手机号或邮箱错误");
        }

        // 新密码加密后存入数据库
        String newPassword = request.getUserPassword();
        userRaw.setUserPassword(passwordEncoder.encode(newPassword));
        if (!userService.update(userRaw, wrapper)) {
            return Result.fail("更新失败");
        }

        return Result.ok("更新成功");
    }

    public Result avatar(MultipartFile file) {
        if (Objects.isNull(file)) {
            return Result.fail("文件为空");
        }
        // 获取SecurityContextHolder中的用户id
        String userId = SecurityUtil
                .getUserFromHolder().getUserId();
        try {
            // 获取上传的文件的输入流
            InputStream inputStream = file.getInputStream();
            // 获取源文件名
            String originalFilename = file.getOriginalFilename();
            // 上传图片并获取图像存储路径
            String avatarUrl = ossUtil.upload(originalFilename, inputStream, URL_STORAGE_AVATAR);
            // 更新
            boolean flag = userService.lambdaUpdate()
                    .set(User::getUserImage, avatarUrl)
                    .eq(User::getUserId, userId)
                    .update();
            if (!flag) {
                return Result.fail("更新失败");
            }

            // 更新缓存
            User user = new User();
            user.setUserImage(avatarUrl);
            SecurityUtil.UpdateCache(user, stringRedisTemplate);

            return Result.ok();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Result profile() {
        // 获取SecurityContextHolder中的用户
        User user = SecurityUtil.getUserFromHolder();
        Integer roleId = userService.getRoleId(user.getUserId());
        if (Objects.isNull(roleId)) {
            return Result.fail("该用户未绑定角色");
        }
        ProfileResponse build = ProfileResponse.builder()
                .roleId(roleId)
                .userName(user.getUserName()).build();
        return Result.ok(build);
    }
}
