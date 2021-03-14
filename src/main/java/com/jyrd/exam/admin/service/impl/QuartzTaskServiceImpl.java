package com.jyrd.exam.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jyrd.exam.admin.service.QuartzTaskService;
import com.jyrd.exam.base.entity.QuartzTask;
import com.jyrd.exam.base.mapper.QuartzTaskMapper;
import org.springframework.stereotype.Service;

@Service
public class QuartzTaskServiceImpl extends ServiceImpl<QuartzTaskMapper, QuartzTask> implements QuartzTaskService {
}
