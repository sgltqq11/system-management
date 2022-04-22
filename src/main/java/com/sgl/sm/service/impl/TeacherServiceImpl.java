package com.sgl.sm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sgl.sm.mapper.TeacherMapper;
import com.sgl.sm.pojo.LoginForm;
import com.sgl.sm.pojo.Student;
import com.sgl.sm.pojo.Teacher;
import com.sgl.sm.service.TeacherService;
import com.sgl.sm.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

@Service("teacherServiceImpl")
@Transactional
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
    @Override
    public Teacher login(LoginForm loginForm) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));

        Teacher teacher = baseMapper.selectOne(queryWrapper);
        return teacher;
    }

    @Override
    public Teacher getTeacherById(Long userId) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        Teacher teacher = baseMapper.selectOne(queryWrapper);
        return teacher;
    }

    @Override
    public IPage<Teacher> getTeacherByOpr(Page<Teacher> page, Teacher teacher) {

        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();

        //班级名称查询条件
        String clazzName = teacher.getClazzName();
        if (!StringUtils.isEmpty(clazzName)){
            queryWrapper.like("clazz_name",clazzName);
        }
        //老师姓名查询条件
        String teacherName = teacher.getName();
        if (!StringUtils.isEmpty(clazzName)){
            queryWrapper.like("name",teacherName);
        }

        Page<Teacher> teacherPage = baseMapper.selectPage(page, queryWrapper);

        return teacherPage;
    }
}
