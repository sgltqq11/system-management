package com.sgl.sm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sgl.sm.pojo.Teacher;
import com.sgl.sm.service.TeacherService;
import com.sgl.sm.util.MD5;
import com.sgl.sm.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Api(tags = "教师管理")
@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {


    @Autowired
    TeacherService teacherService;

    @ApiOperation("删除学生信息")
    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(@ApiParam("要删除的所有的teacher的id的JSON集合") @RequestBody List<Integer> ids){

        teacherService.removeByIds(ids);

        return Result.ok();
    }

    @ApiOperation("添加或修改老师信息,带id为添加,不带id为修改")
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(@ApiParam("JSON转换后端Teacher数据模型") @RequestBody Teacher teacher){

        //对老师的密码进行加密(添加操作)
        //如果添加的是学生,需要对老师的密码进行加密
        if (!StringUtils.isEmpty(teacher.getPassword())){
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }
        //保存老师信息进入数据库
        teacherService.saveOrUpdate(teacher);

        return Result.ok();
    }

    @ApiOperation("分页带条件查询教师信息")
    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeacher(
            @ApiParam("分页查询页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询页码大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("查询条件") Teacher teacher
    ){
        //设置分页信息
        Page<Teacher> page = new Page<Teacher>(pageNo,pageSize);

        //调用服务层,传入分页信息和查询条件
        IPage<Teacher> pageRs = teacherService.getTeacherByOpr(page,teacher);


        return Result.ok(pageRs);
    }
}
