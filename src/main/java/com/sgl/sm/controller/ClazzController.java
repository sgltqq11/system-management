package com.sgl.sm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sgl.sm.pojo.Clazz;
import com.sgl.sm.service.ClazzService;
import com.sgl.sm.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "班级管理")
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {

    @Autowired
    ClazzService clazzService;

    @ApiOperation("获取全部的班级信息")
    @GetMapping("/getClazzs")
    public Result getClazzs(){

        List<Clazz> clazzs = clazzService.getClazz();

        return Result.ok(clazzs);
    }


    @ApiOperation("删除clazz信息")
    @DeleteMapping("deleteClazz")
    public Result deleteClazz(@ApiParam("要删除的所有的clazz的id的JSON集合") @RequestBody List<Integer> ids){

        clazzService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("保存或修改班级信息,有id属性是修改，否则增加")
    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(@ApiParam("JSON转换后端Clazz数据模型") @RequestBody Clazz clazz){

        clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }

    @ApiOperation("查询班级信息，分页带条件")
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzByOpr(
            @ApiParam("分页查询页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("查询条件") Clazz clazz
    ){
        //设置分页信息
        Page<Clazz> page = new Page<>(pageNo,pageSize);
        //调用服务层，传入分页信息和查询条件
        IPage<Clazz> pageRs = clazzService.getGradeByOpr(page,clazz);

        //封装Result对象并返回
        return Result.ok(pageRs);
    }
}
