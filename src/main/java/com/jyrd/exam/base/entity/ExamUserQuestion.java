package com.jyrd.exam.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *  Entry实体类
 * </p>
 *
 * @author lovbe
 * @since 2020-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("exm_exam_user_question")
public class ExamUserQuestion implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
      @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 考试用户ID
     */
    @TableField("EXAM_USER_ID")
    private Integer examUserId;

    /**
     * 考试ID
     */
    @TableField("EXAM_ID")
    private Integer examId;

    /**
     * 用户ID
     */
    @TableField("USER_ID")
    private Integer userId;

    /**
     * 试题ID
     */
    @TableField("QUESTION_ID")
    private Integer questionId;

    /**
     * 试题类型type 2单选3多选4判断5填空6简答
     */
    @TableField("QUESTION_TYPE")
    private Integer questionType;

    @TableField("UPDATE_USER_ID")
    private Integer updateUserId;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 答案
     */
    @TableField("ANSWER")
    private String answer;

    /**
     * 得分
     */
    @TableField("SCORE")
    private double score;

    /**
     * 单项得分
     */
    @TableField("single_Score")
    private Double singleScore;

}
