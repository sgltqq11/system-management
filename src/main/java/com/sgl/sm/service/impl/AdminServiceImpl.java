package com.sgl.sm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sgl.sm.mapper.AdminMapper;
import com.sgl.sm.pojo.Admin;
import com.sgl.sm.pojo.LoginForm;
import com.sgl.sm.service.AdminService;
import com.sgl.sm.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//ServiceImpl继承了BaseMapper并实现了IService接口
/*
ServiceImpl<AdminMapper,Admin>泛型 规定对哪个mapper进行注入
    ===》 private AdminMapper adminMapper ,对哪个数据库表格进行增删改查、实体类进行封装
*/
@Service("adminServiceImpl")
@Transactional  //事务
public class AdminServiceImpl extends ServiceImpl<AdminMapper,Admin> implements AdminService {
    @Override
    public Admin login(LoginForm loginForm) {
        //创建QueryWrapper对象
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        //拼接查询条件
        queryWrapper.eq("name",loginForm.getUsername());
        // 转换成密文进行查询
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));

        //public class ServiceImpl<M extends BaseMapper<T>, T> implements IService<T> {...}
        Admin admin = baseMapper.selectOne(queryWrapper);
        return admin;
    }

    @Override
    public Admin getAdminById(Long userId) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        Admin admin = baseMapper.selectOne(queryWrapper);
        return admin;
    }

    @Override
    public IPage<Admin> getAdminByOpr(Page<Admin> adminPage, String adminName) {

        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(adminName)){
            queryWrapper.like("name",adminName);
        }

        queryWrapper.orderByDesc("id");
        queryWrapper.orderByAsc("name");

        Page<Admin> page = baseMapper.selectPage(adminPage, queryWrapper);

        return page;
    }


}
