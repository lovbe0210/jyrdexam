package com.jyrd.exam.base.vo;

import lombok.Data;

@Data
public class QuestionTypeVo {
    int paperId; // 试卷ID
    int questionType;  // 试题类型
    int questionCount; // 试题数量
    double questionScore; // 单项试题分数

}
