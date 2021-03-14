package com.jyrd.exam.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
@TableName("sys_quartz_task")
public class QuartzTask implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
      @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 实现类
     */
    @TableField("JOB_CLASS")
    private String jobClass;

    /**
     * cron表达式
     */
    @TableField("CRON")
    private String cron;


    @TableField("jobDetail_name")
    private String jobDetailName;

    @TableField("cronTrigger_name")
    private String cronTriggerName;

    /**
     * 1：启动；2：停止；
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
