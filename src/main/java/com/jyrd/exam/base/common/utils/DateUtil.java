package com.jyrd.exam.base.common.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author lovbe
 */
public class DateUtil {

	/**
	 * 获取n秒之后的时间
	 *
	 * @param latterSec
	 * @return Date
	 */
	public static Date addSeconds(Date currentData,int latterSec) {
		LocalDateTime localDateTime = currentData.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
		LocalDateTime plusSeconds = localDateTime.plusSeconds(latterSec);
		Date latterDate = Date.from(plusSeconds.atZone( ZoneId.systemDefault()).toInstant());
		return latterDate;
	}

	/**
	 * 计算两个时间差
	 *
	 * @param latterSec
	 * @return String
	 */
	public static String getMulTime(Date beginData,Date endData) {
		LocalDateTime beginDateTime = beginData.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();

		LocalDateTime endDateTime = endData.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();

		long between = ChronoUnit.SECONDS.between(beginDateTime, endDateTime);

		if(between <= 60){
			return between+"秒";
		}else if(between >= 3600){
			long hour = between / 3600;
			long sec = between % 3600;
			if(sec <= 60){
				return hour+"小时"+sec+"秒";
			}else {
				long minuter = sec / 60;
				long second = between % 60;
				return hour+"小时"+minuter+"分"+second+"秒";
			}
		}else {
			long minuter = between / 60;
			long second = between % 60;
			return minuter+"分"+second+"秒";
		}
	}
}
