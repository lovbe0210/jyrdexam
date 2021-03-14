package com.jyrd.exam.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@EqualsAndHashCode()
@Accessors(chain = true)
@TableName("sys_user")
public class User {
    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField("login_name")
    private String loginName;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 工号
     */
    @TableField("work_num")
    private Integer workNum;

    /**
     * 部门id
     */
    @TableField("department_id")
    private Integer departmentId;

    /**
     * 部门名称
     */
    @TableField("department_name")
    private String departmentName;

    /**
     * 修改时间
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 最后一次登录
     */
    @TableField("last_login_time")
    private Date lastLoginTime;

    @TableField("avatar_url")
    private String avatarUrl;

    @TableField("phone_num")
    private String phoneNum;
}
