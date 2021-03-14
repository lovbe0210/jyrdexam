package com.jyrd.exam.base.common.utils;

import java.util.Collection;

/**
 * 校验工具类
 *
 * @author lovbe
 */
public class ValidateUtil {

	/**
	 * 字符串校验
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isValid(String str) {
		if (str == null || str.trim().isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * 集合校验
	 *
	 * @param collection
	 * @return boolean
	 */
	public static <T> boolean isValid(Collection<T> collection) {
		if (collection == null || collection.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * 数组校验
	 *
	 * @param arr
	 * @return boolean
	 */
	public static boolean isValid(Object[] arr) {
		if (arr == null || arr.length == 0) {
			return false;
		}
		return true;
	}
}
