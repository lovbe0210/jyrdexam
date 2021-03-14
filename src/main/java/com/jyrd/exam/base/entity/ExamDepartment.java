package com.jyrd.exam.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@TableName("exm_exam_dept")
public class ExamDepartment implements Serializable {
    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 考试Id
     */
    @TableField("exam_id")
    private Integer examId;

    /**
     * 部门Id
     */
    @TableField("dept_id")
    private Integer deptId;

    /**
     * 部门名称
     */
    @TableField("dept_name")
    private String deptName;
}
