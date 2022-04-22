package com.sgl.sm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.StringUtils;
import com.sgl.sm.mapper.GradeMapper;
import com.sgl.sm.pojo.Grade;
import com.sgl.sm.service.GradeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("gradeServiceImpl")
@Transactional
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {
    @Override
    public IPage<Grade> getGradeByOpr(Page<Grade> gradePage, String gradeName) {

        //设置查询条件
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(gradeName)){
            queryWrapper.like("name",gradeName);
        }
        //设置排序规则
        queryWrapper.orderByDesc("id");
        queryWrapper.orderByAsc("name");

        //分页查询数据
        Page<Grade> page = baseMapper.selectPage(gradePage, queryWrapper);

        return page;
    }

    @Override
    public List<Grade> getGrades() {
        List<Grade> grades = baseMapper.selectList(null);
        return grades;
    }

}
