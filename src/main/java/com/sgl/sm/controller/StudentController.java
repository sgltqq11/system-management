package com.sgl.sm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sgl.sm.pojo.Student;
import com.sgl.sm.service.StudentService;
import com.sgl.sm.util.MD5;
import com.sgl.sm.util.Result;
import com.sgl.sm.util.ResultCodeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "学生管理")
@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    StudentService studentService;


    @ApiOperation("删除学生信息")
    @DeleteMapping("/delStudentById")
    public Result deleteStudentById(@ApiParam("要删除的所有的student的id的JSON集合") @RequestBody List<Integer> ids){

        studentService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("添加或修改学生信息,带id是修改,不带id是添加")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(@ApiParam("JSON转换后端Student数据模型") @RequestBody Student student){

        //对学生的密码进行加密(添加操作)
        //如果是添加学生,需要学生的密码进行加密
        if (!StringUtils.isEmpty(student.getPassword())){
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        //保存学生信息进入数据库
        studentService.saveOrUpdate(student);
        return Result.ok();
    }




    @ApiOperation("分页带条件查询学生信息")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(
            @ApiParam("分页查询页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询页码大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("查询条件")Student student
            ){

        //设置分页信息
        Page<Student> page = new Page<>(pageNo,pageSize);
        //调用服务层,传入分页信息和查询条件
        IPage<Student> pageRs = studentService.getStudentByOpr(page,student);

        //封装对象并返回
        return Result.ok(pageRs);
    }
}
