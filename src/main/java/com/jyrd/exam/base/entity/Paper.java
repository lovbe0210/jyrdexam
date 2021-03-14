package com.jyrd.exam.base.entity;

import com.baomidou.mybatisplus.annotation.*;
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
@TableName("exm_paper")
public class Paper implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 总分数
     */
    @TableField("TOTLE_SCORE")
    private BigDecimal totleScore;

    /**
     * 试卷分类
     */
    @TableField("catalog_id")
    private Integer catalogId;

    /**
     * 0：未配置不能发布；1：已配置可发布
     */
    @TableField("STATE")
    private Integer state;

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
}
