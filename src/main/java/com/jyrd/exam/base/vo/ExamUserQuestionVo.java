package com.jyrd.exam.base.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 前端试题显示相关参数
 */
@Data
public class ExamUserQuestionVo {
    /**
     * 考试用户的试题ID，与ExamUserQuestion的id相同
     */
    private Integer id;

    /**
     * 考试用户ID
     */
    private Integer examUserId;

    /**
     * 考试ID
     */
    private Integer examId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 试题ID
     */
    private Integer questionId;

    /**
     * 试题类型type 2单选3多选4判断5填空6简答
     */
    private Integer questionType;

    /**
     * 答案
     */
    private String answer;


    /**
     * 题干
     */
    private String title;

    /**
     * 选项A
     */
    private String optionA;

    /**
     * 选项B
     */
    private String optionB;

    /**
     * 选项C
     */
    private String optionC;

    /**
     * 选项D
     */
    private String optionD;

    /**
     * 选项E
     */
    private String optionE;

    /**
     * 选项F
     */
    private String optionF;

    /**
     * 选项G
     */
    private String optionG;


    /**
     * 解析
     */
    private String analysis;

    /**
     * 单项得分
     */
    private double singleScore;

    /**
     * 是否标记
     */
    private boolean marked = false;
}
