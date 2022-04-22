package com.sgl.sm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sgl.sm.pojo.Admin;
import com.sgl.sm.service.AdminService;
import com.sgl.sm.util.MD5;
import com.sgl.sm.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Api(tags = "系统管理员")
@RestController
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    AdminService adminService;

    @ApiOperation("删除管理员信息")
    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(@ApiParam("要删除的所有的admin的id的JSON集合") @RequestBody List<Integer> ids){

        adminService.removeByIds(ids);

        return Result.ok();
    }


    @ApiOperation("添加或修改管理员信息,带id是修改,不带id是添加")
    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(@ApiParam("JSON转换后端Admin数据模型") @RequestBody Admin admin){

        //对管理员的密码进行加密(添加操作)
        //如果是添加学生,需要对管理员的密码加密
        if (!StringUtils.isEmpty(admin.getPassword())){
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        //保存管理员的信息进入数据库
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }

    @ApiOperation("带分页查询管理员信息")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(
            @ApiParam("分页查询页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("查询条件(管理员名字)") String adminName

            ){
        //设置分页信息
        Page<Admin> adminPage = new Page<>(pageNo, pageSize);
        //调用服务层，传入分页信息和查询条件
        IPage<Admin> pageRs = adminService.getAdminByOpr(adminPage,adminName);
        //封装Result对象并返回
        return Result.ok(pageRs);
    }

}
