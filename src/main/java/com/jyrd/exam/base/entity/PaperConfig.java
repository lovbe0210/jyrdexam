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
@TableName("exm_paper_config")
public class PaperConfig implements Serializable {
    private static final long serialVersionUID=1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 试卷ID
     */
    @TableField("PAPER_ID")
    private int paperId;


    /**
     * 总分数
     */
    @TableField("TOTLE_SCORE")
    private double totleScore;

    /**
     * 单选题数
     */
    @TableField("RADIO_COUNT")
    private int radioCount;

    /**
     * 单选每项得分
     */
    @TableField("RADIO_SCORE")
    private double radioScore;

    /**
     * 多选题数
     */
    @TableField("CHECKBOX_COUNT")
    private int checkedCount;

    /**
     * 多选每项得分
     */
    @TableField("CHECKBOX_SCORE")
    private double checkedScore;

    /**
     * 判断题数
     */
    @TableField("JUDGE_COUNT")
    private int judgeCount;

    /**
     * 判断每项得分
     */
    @TableField("JUDGE_SCORE")
    private double judgeScore;

    /**
     * 填空题数
     */
    @TableField("SCANNER_COUNT")
    private int scannerCount;

    /**
     * 填空每项得分
     */
    @TableField("SCANNER_SCORE")
    private double scannerScore;

    /**
     * 问答题数
     */
    @TableField("QANSWER_COUNT")
    private int qAnswerCount;

    /**
     * 问答每项得分
     */
    @TableField("QANSWER_SCORE")
    private double qAnswerScore;


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

    public void submitTotalScore(){
        this.totleScore = this.radioCount * this.radioScore + this.checkedCount * this.checkedScore +
                          this.judgeCount * this.judgeScore + this.scannerCount * this.scannerScore +
                          this.qAnswerCount * this.qAnswerScore;
    }

}
