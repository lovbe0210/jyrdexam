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
@TableName("exm_question")
public class Question implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 2：单选；3：多选；4：判断；5：填空；6：问答
     */
    @TableField("question_type_id")
    private Integer questionTypeId;

    /**
     * 1:安规；2：运规；3：三种人
     */
    @TableField("catalog_id")
    private Integer catalogId;

    /**
     * 1：极易；2：简单；3：适中；4：困难；5：极难
     */
    @TableField("DIFFICULTY")
    private Integer difficulty;

    /**
     * 题干
     */
    @TableField("TITLE")
    private String title;

    /**
     * 选项A
     */
    @TableField("OPTION_A")
    private String optionA;

    /**
     * 选项B
     */
    @TableField("OPTION_B")
    private String optionB;

    /**
     * 选项C
     */
    @TableField("OPTION_C")
    private String optionC;

    /**
     * 选项D
     */
    @TableField("OPTION_D")
    private String optionD;

    /**
     * 选项E
     */
    @TableField("OPTION_E")
    private String optionE;

    /**
     * 选项F
     */
    @TableField("OPTION_F")
    private String optionF;

    /**
     * 选项G
     */
    @TableField("OPTION_G")
    private String optionG;

    /**
     * 答案
     */
    @TableField("ANSWER")
    private String answer;

    /**
     * 解析
     */
    @TableField("ANALYSIS")
    private String analysis;

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
     * 排序
     */
    @TableField("NO")
    private Integer no;
}
