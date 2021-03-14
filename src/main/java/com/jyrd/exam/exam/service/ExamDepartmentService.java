package com.jyrd.exam.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jyrd.exam.base.entity.SysDepartment;
import com.jyrd.exam.base.entity.ExamDepartment;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lovbe
 * @since 2021-03-19
 */
public interface ExamDepartmentService extends IService<ExamDepartment> {
    List<SysDepartment> getAllDept();
}
