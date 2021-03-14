package com.jyrd.exam.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("exm_exam_catalog")
public class ExamCatalog implements Serializable {

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
     * 父ID
     */
    @TableField("PARENT_ID")
    private Integer parentId;

    /**
     * 父子关系（格式：_父ID_子ID_子子ID_... ...）
     */
    @TableField("PARENT_SUB")
    private String parentSub;

    /**
     * 修改人
     */
    @TableField("UPDATE_USER_ID")
    private Integer updateUserId;

    /**
     * 修改时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 0：删除；1：正常
     */
    @TableField("STATE")
    private Integer state;

    /**
     * 排序
     */
    @TableField("NO")
    private Integer no;

    /**
     * 用户权限。多用户用逗号分隔，如：,2,45,66,57,
     */
    @TableField("USER_IDS")
    private String userIds;

    /**
     * 机构权限
     */
    @TableField("ORG_IDS")
    private String orgIds;

    /**
     * 岗位权限
     */
    @TableField("POST_IDS")
    private String postIds;


}
