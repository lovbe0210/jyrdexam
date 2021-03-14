package com.jyrd.exam.base.common.quartzTask;

import com.jyrd.exam.exam.service.ExamService;
import org.quartz.*;

import java.util.Map;

/**
 * 用户考试试卷强制交卷任务
 */
public class SubmitExamJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        // 获取运行时自定义的参数
        Map<String,Object> params = (Map<String, Object>) jobDataMap.get("params");
        ExamService examService = (ExamService) params.get("examService");
        Scheduler scheduler = jobExecutionContext.getScheduler();
      try {
        // 调用考试service然后改变ExamUser状态为强制交卷
        examService.forceSubmitExam(scheduler,params);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
}
