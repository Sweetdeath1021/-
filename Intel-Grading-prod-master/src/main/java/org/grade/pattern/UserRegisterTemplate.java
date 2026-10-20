package org.grade.pattern;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.grade.bean.request.User.UserRegisterRequest;
import org.grade.common.Result;
import org.grade.mapper.UserMapper;
import org.grade.model.User;
import org.grade.utils.OSSUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static org.grade.constant.OSSConstant.URL_STORAGE_AVATAR;

@Component
public abstract class UserRegisterTemplate {
    @Resource
    private OSSUtil ossUtil;
    @Resource
    private UserMapper userMapper;
    @Resource
    private PasswordEncoder passwordEncoder;

    public Result register(MultipartFile file, UserRegisterRequest request) {
        // 1.判断手机号是否重复注册
        if (isExistAccount(request)) {
            return Result.fail("该手机号或学号已存在用户");
        }

        // 2.上传头像
        String avatarUrl = uploadAvatar(file);

        // 3.设置用户基本信息，将用户及对应角色存入数据库
        saveUser(request, avatarUrl);

        return Result.ok("注册成功");
    }

    public String uploadAvatar(MultipartFile file) {
        // 获取上传的文件的输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            // 2.2.获取源文件名
            String originalFilename = file.getOriginalFilename();
            return ossUtil.upload(originalFilename, inputStream, URL_STORAGE_AVATAR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断是否存在相同账号
     * @param request
     * @return
     */
    public boolean isExistAccount(UserRegisterRequest request) {
        // 判断账号是否存在
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_account", request.getUser().getUserAccount());
        User dataUser = userMapper.selectOne(wrapper);
        if (Objects.isNull(dataUser)) {
            return false;
        }
        return true;
    }

    /**
     * 设置用户基本信息
     * @param request
     * @param avatarUrl
     */
    public void saveUser(UserRegisterRequest request, String avatarUrl) {
        User user = request.getUser();
        // 密码加密
        user.setUserPassword(passwordEncoder
                .encode(user.getUserPassword()));
        // 设置头像路径
        user.setUserImage(avatarUrl);

        userMapper.insert(user);
        userMapper.register(user.getUserId(), request.getRoleId());
    }
}
