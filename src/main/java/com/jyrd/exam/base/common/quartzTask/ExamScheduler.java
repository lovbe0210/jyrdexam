package com.jyrd.exam.base.common.quartzTask;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
public class ExamScheduler {

  public static Scheduler scheduler = null;

  static {
    // 1. 创建调度器Scheduler
    StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
    try {
      Scheduler sc = stdSchedulerFactory.getScheduler();
      scheduler = sc;
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  public static void startQuartzTask(Map<String, Object> params) throws SchedulerException, InterruptedException, ClassNotFoundException {

    // 2. 在参数中获取需要绑定的Job实例类名以及创建的jobName和触发器cronTriggerName
    String quartTaskClass = (String) params.get("quartTaskClass");
    String jobDetailName = (String) params.get("jobDetailName");
    String cronTriggerName = (String) params.get("cronTriggerName");
    JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(quartTaskClass))
                                    .withIdentity(jobDetailName, "examGroup")
                                    .build();
    // 3. 构建Trigger实例，设置执行频率
    CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                                            .withIdentity(cronTriggerName, "examCron")
                                            .withSchedule( CronScheduleBuilder.cronSchedule((String) params.get("cron")) )
                                            .build();
    // * 传递参数
    jobDetail.getJobDataMap().put("params",params);
    // 4. 执行任务
    scheduler.scheduleJob(jobDetail,cronTrigger);
    String time = LocalDateTime.now().toString();
    log.info(time + " 开始执行任务："+ (String) params.get("quartzName") +" **************");
    scheduler.start();
  }


  /**
   * 移除定时任务
   * @param jobName
   * @param triggerName
   */
  public static void removeJob(Scheduler scheduler,String jobName,String triggerName) {
    try {
      scheduler.pauseTrigger(TriggerKey.triggerKey(triggerName));// 停止触发器
      scheduler.unscheduleJob(TriggerKey.triggerKey(triggerName));// 移除触发器
      scheduler.deleteJob(JobKey.jobKey(jobName));// 删除任务
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
