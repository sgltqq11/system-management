package com.sgl.sm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sgl.sm.pojo.Grade;
import com.sgl.sm.service.GradeService;
import com.sgl.sm.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "年级管理")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    GradeService gradeService;


    @ApiOperation("获取全部年级")
    @GetMapping("/getGrades")
    public Result getGrades(){
        List<Grade> grades =gradeService.getGrades();
        return Result.ok(grades);
    }


    @ApiOperation("删除grade信息")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(@ApiParam("要删除的所有的grade的id的JSON集合") @RequestBody List<Integer> ids){

        gradeService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("新增或修改grade，有id属性是修改，否则增加")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(@ApiParam("JSON的Grade对象") @RequestBody Grade grade){

        //接收参数
        //调用服务层完成增加或修改
        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }

    @ApiOperation("查询年级信息,分页带条件")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(
            @ApiParam("分页查询页码数") @PathVariable("pageNo") Integer pageNo,  // 页码数
            @ApiParam("分页查询页大小") @PathVariable("pageSize") Integer pageSize, // 页大小
            @ApiParam("分页查询模糊匹配班级名") String gradeName // 模糊查询条件
    ){
        // 设置分页信息
        Page<Grade> gradePage = new Page<>(pageNo, pageSize);
        // 调用服务层方法,传入分页信息,和查询的条件
        IPage<Grade> PageRs = gradeService.getGradeByOpr(gradePage,gradeName);
        //封装Result对象并返回
        return Result.ok(PageRs);
    }

}
