package com.sgl.sm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sgl.sm.pojo.Clazz;

import java.util.List;

public interface ClazzService extends IService<Clazz> {
    IPage<Clazz> getGradeByOpr(Page<Clazz> clazzPage, Clazz clazz);

    List<Clazz> getClazz();
}
