package com.sgl.sm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sgl.sm.mapper.ClazzMapper;
import com.sgl.sm.pojo.Clazz;
import com.sgl.sm.service.ClazzService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("clazzServiceImpl")
@Transactional
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {
    @Override
    public IPage<Clazz> getGradeByOpr(Page<Clazz> pageParam, Clazz clazz) {

        QueryWrapper<Clazz> queryWrapper = new QueryWrapper<>();

        //年级名称条件
        String gradeName = clazz.getGradeName();
        if (!StringUtils.isEmpty(gradeName)){
            queryWrapper.like("grade_name",gradeName);
        }

        //班级名称条件
        String clazzName = clazz.getName();
        if (!StringUtils.isEmpty(clazzName)){
            queryWrapper.like("name",clazzName);
        }

        //设置排序规则
        queryWrapper.orderByDesc("id");
        queryWrapper.orderByAsc("name");

        //分页查询的数据
        Page<Clazz> page = baseMapper.selectPage(pageParam, queryWrapper);
        return page;
    }

    @Override
    public List<Clazz> getClazz() {

        List<Clazz> clazzes = baseMapper.selectList(null);

        return clazzes;
    }
}
