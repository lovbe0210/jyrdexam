package com.jyrd.exam.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

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
@TableName("exm_exam")
public class Exam implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
      @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    @TableField("NAME")
    private String name;


    /**
     * 考试时长
     */
    @TableField("Exam_Duration")
    private String examDuration;


    /**
     * 考试次数
     */
    @TableField("max_exam_count")
    private Integer maxExamCount;

    /**
     * 及格分数
     */
    @TableField("PASS_SCORE")
    private BigDecimal passScore;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 开始时间
     */
    @TableField("START_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("END_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 0：未发布；1：进行中；2：已结束
     */
    @TableField("STATE")
    private Integer state;

    /**
     * 试卷ID
     */
    @TableField("PAPER_ID")
    private Integer paperId;

    /**
     * 考试分类
     */
    @TableField("catalog_id")
    private Integer catalogId;


}
