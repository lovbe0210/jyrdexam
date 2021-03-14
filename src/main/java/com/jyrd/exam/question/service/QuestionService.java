package com.jyrd.exam.question.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jyrd.exam.base.entity.Question;
import com.jyrd.exam.base.entity.QuestionType;
import com.jyrd.exam.base.entity.SysCatalog;
import com.jyrd.exam.base.vo.ParamVo;
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
public interface QuestionService extends IService<Question> {

    List<Map<String, Object>> getQuestionTypeTreeList();

    QuestionType getQuestionType(Integer questionTypeId);

    void addAndUpdate(Question question, String[] answer);

    ResponsePageVo getListpage(ParamVo paramVo);

    List<QuestionType> getQuestionTypeList();

    List<SysCatalog> getCatalogList();
}
