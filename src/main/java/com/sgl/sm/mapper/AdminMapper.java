package com.sgl.sm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sgl.sm.pojo.Admin;
import org.springframework.stereotype.Repository;

/*
BaseMapper 封装了一系列增删改查的方法（不需要手动写增删改查，直接调用即可）
 */
@Repository
public interface AdminMapper extends BaseMapper<Admin> {
}
