package com.jyrd.exam.base.vo;

import lombok.Data;

/**
 * 试卷问题相关参数
 */
@Data
public class PaperQuestionVo {
    // 当前试卷分类
    private int paperTypeId;

    // 单选题数量
    private int radioCount;
    // 多选题数量
    private int checkboxCount;
    // 判断题数量
    private int judgeCount;
    // 填空题数量
    private int scannerCount;
    // 问答题数量
    private int qAnswerCount;

}
