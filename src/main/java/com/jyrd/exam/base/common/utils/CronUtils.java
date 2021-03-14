package com.jyrd.exam.base.common.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * cron工具类
 *
 * @author lovbe
 */
public class CronUtils {
   private static final SimpleDateFormat sdf = new SimpleDateFormat("ss mm HH dd MM ? yyyy");

  /***
   * 功能描述：日期转换cron表达式
   * convert Date to cron, eg "0 07 10 15 1 ? 2016"
   * @param date  : 时间点
   * @return
   */
  public static String getCron(Date date) {
    String cronStr = null;
    if (Objects.nonNull(date)) {
      cronStr = sdf.format(date);
    }
    return cronStr;
  }
}
