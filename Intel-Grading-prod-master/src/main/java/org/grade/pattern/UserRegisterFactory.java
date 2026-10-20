package org.grade.pattern;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 工厂类，创建模板方法的实例
 */
@Component
public class UserRegisterFactory {
    @Resource
    private AdminRegister adminRegister;
    @Resource
    private TeacherRegister teacherRegister;
    @Resource
    private StudentRegister studentRegister;
    public UserRegisterTemplate newInstance(int roleId) {
        switch (roleId) {
            case 1 :
                return adminRegister;
            case 2 :
                return teacherRegister;
            default :
                return studentRegister;
        }
    }
}
