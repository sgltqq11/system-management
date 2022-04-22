package com.sgl.sm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sgl.sm.pojo.Admin;
import com.sgl.sm.pojo.LoginForm;
import com.sgl.sm.pojo.Student;

//IService 定义一系列增删改查的业务方法
public interface AdminService extends IService<Admin> {
    Admin login(LoginForm loginForm);

    Admin getAdminById(Long userId);

    IPage<Admin> getAdminByOpr(Page<Admin> adminPage, String adminName);

}
