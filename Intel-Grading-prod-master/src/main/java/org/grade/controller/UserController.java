package org.grade.controller;

import org.grade.bean.request.User.*;
import org.grade.common.Result;
import org.grade.manage.UserInfoManage;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;


@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserInfoManage userInfoManage;

    @PostMapping("/login")
    public Result login(@RequestBody UserLoginRequest userLoginRequest) {
        return userInfoManage.login(userLoginRequest);
    }

    @GetMapping("/profile")
    public Result profile() {
        return userInfoManage.profile();
    }

    @GetMapping("/logout")
    public Result logout() {
        return userInfoManage.logout();
    }

    @PostMapping("/register")
    public Result register(@RequestPart("file") MultipartFile file,
                           @RequestPart("entity") UserRegisterRequest userRegisterRequest) throws IOException {
        return userInfoManage.register(file, userRegisterRequest);
    }
    @PutMapping("/avatar")
    public Result avatar(@RequestPart("file") MultipartFile file){
        return userInfoManage.avatar(file);
    }
    @GetMapping("info")
    public Result info() {
        return userInfoManage.info();
    }

    @PutMapping("/update")
    public Result update(@RequestBody UserUpdateRequest userUpdateRequest) {
        return userInfoManage.update(userUpdateRequest);
    }

    @PostMapping("/forget")
    public Result forget(@RequestBody PasswordForgetRequest passwordForgetRequest) {
        return userInfoManage.forget(passwordForgetRequest);
    }

    @PutMapping("/change")
    public Result change(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        return userInfoManage
                .change(passwordChangeRequest.getRawPassword(), passwordChangeRequest.getNewPassword());
    }
}
