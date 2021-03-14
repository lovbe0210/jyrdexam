package com.jyrd.exam.paper.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jyrd.exam.base.entity.Paper;
import com.jyrd.exam.base.entity.PaperConfig;
import com.jyrd.exam.base.entity.SysCatalog;
import com.jyrd.exam.base.vo.PaperQuestionVo;
import com.jyrd.exam.base.vo.ParamVo;
import com.jyrd.exam.base.vo.QuestionTypeVo;
import com.jyrd.exam.base.vo.ResponsePageVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lovbe
 * @since 2021-03-19
 */
public interface PaperService extends IService<Paper> {

    List<Map<String, Object>> getPaperTypeTreeList();

    ResponsePageVo getListpage(ParamVo paramVo);

    PaperQuestionVo getPaperQuestion(Integer paperTypeId);

    boolean addQuestionType(QuestionTypeVo questionTypeVo);

    PaperConfig getPaperConfig(int paperId);

    List<SysCatalog> getCatalogList();
}
