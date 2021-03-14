package com.jyrd.exam.paper.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jyrd.exam.base.entity.PaperConfig;
import com.jyrd.exam.base.mapper.PaperConfigMapper;
import com.jyrd.exam.paper.service.PaperConfigService;
import org.springframework.stereotype.Service;

@Service
public class PaperConfigServiceImpl extends ServiceImpl<PaperConfigMapper, PaperConfig> implements PaperConfigService {
}
