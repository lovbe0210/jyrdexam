package com.jyrd.exam.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jyrd.exam.base.entity.ExamUserQuestion;
import com.jyrd.exam.base.vo.ExamUserQuestionVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lovbe
 * @since 2021-03-19
 */
@Repository
public interface ExamUserQuestionMapper extends BaseMapper<ExamUserQuestion> {

    List<ExamUserQuestionVo> getExamQuestionList(@Param("id")Integer id);

    ExamUserQuestion getOne(@Param("id")Integer id);
}
