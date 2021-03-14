package com.jyrd.exam.base.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 错题Vo，这里需要实现Serializable
 */
@Data
public class ErrorQuestionVo implements Serializable {
    private Integer id;
    private String title;
    private String trueAnswer;
    private String yourAnswer;
    private String analysis;
    private Integer errorCount;
}
