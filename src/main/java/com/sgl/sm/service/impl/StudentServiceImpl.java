package com.sgl.sm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sgl.sm.mapper.StudentMapper;
import com.sgl.sm.pojo.LoginForm;
import com.sgl.sm.pojo.Student;
import com.sgl.sm.service.StudentService;
import com.sgl.sm.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("studentServiceImpl")
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Override
    public Student login(LoginForm loginForm) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper();
        queryWrapper.eq("name",loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));

        Student student = baseMapper.selectOne(queryWrapper);
        return student;
    }

    @Override
    public Student getStudentById(Long userId) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        Student student = baseMapper.selectOne(queryWrapper);
        return student;
    }

    @Override
    public IPage<Student> getStudentByOpr(Page<Student> studentPage, Student student) {

        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();

        //班级名称查询条件
        String clazzName = student.getClazzName();
        if (!StringUtils.isEmpty(clazzName)){
            queryWrapper.like("clazz_name",clazzName);
        }
        //学生姓名条件
        String StudentName = student.getName();
        if (!StringUtils.isEmpty(clazzName)){
            queryWrapper.like("name",StudentName);
        }

        //设置排序规则
        queryWrapper.orderByDesc("id");
        queryWrapper.orderByDesc("name");

        //分页查询的数据
        Page<Student> page = baseMapper.selectPage(studentPage, queryWrapper);

        return page;
    }

}
