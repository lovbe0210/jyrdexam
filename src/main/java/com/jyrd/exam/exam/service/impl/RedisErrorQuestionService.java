package com.jyrd.exam.exam.service.impl;

import com.jyrd.exam.base.entity.ExamUserQuestion;
import com.jyrd.exam.base.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class RedisErrorQuestionService {
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 保存到redis中
     * @param examUserQuestion
     * @param questionId
     * @param question
     */
    @Async("redisErrorQuestion")
    public void saveErrorQ(ExamUserQuestion examUserQuestion, Integer questionId, Question question) {
        String key = "userId-" + examUserQuestion.getUserId() + ':' + questionId;
        Set keys = redisTemplate.opsForHash().keys(key);
        if(keys.isEmpty()){
            // 第一次添加
            Map errorQuestion = new HashMap<String,Object>();
            errorQuestion.put("title",question.getTitle());
            errorQuestion.put("trueAnswer",question.getAnswer());
            errorQuestion.put("yourAnswer",examUserQuestion.getAnswer());
            errorQuestion.put("errorCount",1);
            errorQuestion.put("analysis",question.getAnalysis());
            redisTemplate.opsForHash().putAll(key,errorQuestion);
        }else {
            redisTemplate.opsForHash().put(key,"yourAnswer",examUserQuestion.getAnswer());
            redisTemplate.opsForHash().increment(key,"errorCount",1);
        }
    }
}
