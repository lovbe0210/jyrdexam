package com.jyrd.exam.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class SysFile implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 前缀
     */
    @TableField("NAME")
    private String name;

    /**
     * 后缀
     */
    @TableField("EXT_NAME")
    private String extName;

    /**
     * 类型
     */
    @TableField("FILE_TYPE")
    private String fileType;

    /**
     * 路径
     */
    @TableField("PATH")
    private String path;

    /**
     * 上传ip
     */
    @TableField("IP")
    private String ip;

    /**
     * 0：删除；1：正常
     */
    @TableField("STATE")
    private Integer state;

    /**
     * 更新人
     */
    @TableField("UPDATE_USER_ID")
    private Integer updateUserId;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;


}
