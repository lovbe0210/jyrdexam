package com.jyrd.exam.base.vo;

import lombok.Data;

/**
 * 我的考试信息Vo
 */
@Data
public class MyExamVo {
    // 考试ID
    private Integer id;

    private String examName;

    // 排名
    private Integer sort;


    private double maxScore;

    // 考试用时
    private String totalDuration;

}
