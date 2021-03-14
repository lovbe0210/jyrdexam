package com.jyrd.exam.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jyrd.exam.admin.service.SysDepartmentService;
import com.jyrd.exam.base.entity.SysDepartment;
import com.jyrd.exam.base.mapper.SysDepartmentMapper;
import org.springframework.stereotype.Service;

@Service
public class SysDepartmentServiceImpl extends ServiceImpl<SysDepartmentMapper, SysDepartment> implements SysDepartmentService {
}
