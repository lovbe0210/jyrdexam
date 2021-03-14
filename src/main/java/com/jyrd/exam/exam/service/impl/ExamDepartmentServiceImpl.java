package com.jyrd.exam.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jyrd.exam.base.entity.SysDepartment;
import com.jyrd.exam.base.entity.ExamDepartment;
import com.jyrd.exam.base.mapper.SysDepartmentMapper;
import com.jyrd.exam.base.mapper.ExamDepartmentMapper;
import com.jyrd.exam.exam.service.ExamDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamDepartmentServiceImpl extends ServiceImpl<ExamDepartmentMapper, ExamDepartment> implements ExamDepartmentService {
    @Autowired
    private SysDepartmentMapper sysDepartmentMapper;

    @Override
    public List<SysDepartment> getAllDept() {
        return sysDepartmentMapper.selectList(new QueryWrapper<SysDepartment>());
    }
}
