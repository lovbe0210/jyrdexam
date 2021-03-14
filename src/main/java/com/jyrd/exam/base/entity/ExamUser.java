package com.jyrd.exam.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@TableName("exm_exam_user")
public class ExamUser implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

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
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 考试总共用时：单位秒
     */
    @TableField("Exam_Duration")
    private Integer examDuration;

    /**
     * 考试开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("BEGIN_TIME")
    private Date beginTime;

    /**
     * 最高分
     */
    @TableField("MAX_SCORE")
    private double maxScore;

    /**
     * 已考次数
     */
    @TableField("EXAMED_COUNT")
    private Integer examedCount;

    /**
     * 1：未考试；2：考试中；3：已交卷；4：强制交卷；5：判卷中；6：未通过；7：已通过
     */
    @TableField("STATE")
    private Integer state;


}
