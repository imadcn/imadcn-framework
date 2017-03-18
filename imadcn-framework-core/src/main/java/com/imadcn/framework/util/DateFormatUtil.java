package com.imadcn.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间转换工具类
 * @author YangChao
 * @since 20130717
 * @version 1.0
 */
public class DateFormatUtil {
	/**
	 * 格式化通配符: yyyy-MM-dd HH:mm:ss
	 */
	public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 格式化通配符: yyyyMMddHHmmss
	 */
	public static final String SHORT_DATE_TIME = "yyyyMMddHHmmss";
	/**
	 * 格式化通配符: yyyy-MM-dd
	 */
	public static final String DATE = "yyyy-MM-dd";
	/**
	 * 格式化通配符: yyyyMMdd
	 */
	public static final String SHORT_DATE = "yyyyMMdd";
	/**
	 * 格式化通配符: yyyy-MM
	 */
	public static final String YEAR_MONTH = "yyyy-MM";
	/**
	 * 格式化通配符: yyyyMM
	 */
	public static final String SHORT_YEAR_MONTH = "yyyyMM";
	/**
	 * 格式化通配符: MM-dd
	 */
	public static final String MONTH_DAY = "MM-dd";
	/**
	 * 格式化通配符: MMdd
	 */
	public static final String SHORT_MONTH_DAY = "MMdd";
	/**
	 * 格式化通配符: HH:mm:ss
	 */
	public static final String TIME = "HH:mm:ss";
	/**
	 * 格式化通配符: HHmmss
	 */
	public static final String SHORT_TIME = "HHmmss";
	/**
	 * 格式化通配符: HH:mm
	 */
	public static final String HOUR_MINUTE = "HH:mm";
	/**
	 * 格式化通配符: HHmm
	 */
	public static final String SHORT_HOUR_MINUTE = "HHmm";
	/**
	 * 格式化通配符: mm:ss
	 */
	public static final String MINUTE_SECOND = "mm:ss";
	/**
	 * 格式化通配符: mmss
	 */
	public static final String SHORT_MINUTE_SECOND = "mmss";
	
	/**
	 * 按照给定的通配模式，格式化成相应的时间字符串
	 * @param srcDate 原始时间字符串
	 * @param srcPattern 原始时间通配符
	 * @param destPattern 格式化成的时间通配符
	 * @return 格式化成功返回成功后的字符串，失败返回<b>""</b>
	 * @see DateFormatUtil.DATE_TIME
	 * @see DateFormatUtil.DATE
	 */
	public static String format(String srcDate, String srcPattern, String destPattern) {
		try {
			SimpleDateFormat srcSdf = new SimpleDateFormat(srcPattern);
			SimpleDateFormat dstSdf = new SimpleDateFormat(destPattern);
			return dstSdf.format(srcSdf.parse(srcDate));
		} catch (ParseException e) {
			return ""; // 格式化失败，返回""
		}
	}
	
	/**
	 * 按照给定的通配模式 YYYY-MM-DD HH:MM:SS ，将时间格式化成相应的字符串
	 * @param srcDate 待格式化的时间
	 * @return 格式化成功返回成功后的字符串，失败返回<b>null</b>
	 */
	public static String format(Date srcDate) {
		if (srcDate != null) {
			SimpleDateFormat dstSdf = new SimpleDateFormat(DATE_TIME);
			return dstSdf.format(srcDate);
		}
		return "";
	}
	
	/**
	 * 按照给定的通配模式，将时间格式化成相应的字符串
	 * @param srcDate 待格式化的时间
	 * @param destPattern 格式化成的时间通配符
	 * @return 格式化成功返回成功后的字符串，失败返回<b>null</b>
	 */
	public static String format(Date srcDate, String destPattern) {
		if (srcDate != null && destPattern != null) {
			SimpleDateFormat dstSdf = new SimpleDateFormat(destPattern);
			return dstSdf.format(srcDate);
		}
		return "";
	}
	
	/**
	 * 按照默认"yyyy-MM-dd"通配模式，将字符串转换成java.util.Date对象
	 * @param srcDate 原始时间字符串
	 * @param srcPattern 原始时间字符串通配符
	 * @return 转换成功返回java.util.Date对象，失败返回<b>null</b>
	 * @see java.util.Date
	 */
	public static Date parse(String srcDate) {
		return parse(srcDate, DATE);
	}
	
	/**
	 * 按照给定的通配模式，将字符串转换成java.util.Date对象
	 * @param srcDate 原始时间字符串
	 * @param srcPattern 原始时间字符串通配符
	 * @return 转换成功返回java.util.Date对象，失败返回<b>null</b>
	 * @see java.util.Date
	 */
	public static Date parse(String srcDate, String srcPattern) {
		try {
			SimpleDateFormat srcSdf = new SimpleDateFormat(srcPattern);
			return srcSdf.parse(srcDate);
		} catch (ParseException e) {
			return null; // 转换失败，返回null
		}
	}
	
	/**
	 * 判断两个时间点相差多少时间，返回Date类型
	 * @param date1 开始时间
	 * @param date2 结束时间
	 * @return 相差时间
	 */
	public static Date sub(Date date1, Date date2) {
		if (date1.after(date2))
			throw new IllegalArgumentException("date1 must be before date2, pls check your args.");
		else {
			return new Date(date2.getTime() - date1.getTime());
		}
	}
	
	/**
	 * 在时间基础上，加上N年，如果要作减法，请输入负数
	 * @param date 时间
	 * @param amount 数量
	 * @return 计算后的时间
	 */
	public static Date addYear(Date date, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, amount);
		return calendar.getTime();
	}
	
	/**
	 * 在时间基础上，加上N月，如果要作减法，请输入负数
	 * @param date 时间
	 * @param amount 数量
	 * @return 计算后的时间
	 */
	public static Date addMonth(Date date, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, amount);
		return calendar.getTime();
	}
	
	/**
	 * 在时间基础上，加上N天，如果要作减法，请输入负数
	 * @param date 时间
	 * @param amount 数量
	 * @return 计算后的时间
	 */
	public static Date addDay(Date date, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, amount);
		return calendar.getTime();
	}
	
	/**
	 * 在时间基础上，加上N周，如果要作减法，请输入负数
	 * @param date 时间
	 * @param amount 数量
	 * @return 计算后的时间
	 */
	public static Date addWeek(Date date, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.WEEK_OF_YEAR, amount);
		return calendar.getTime();
	}
	
	/**
	 * 在时间基础上，加上N小时，如果要作减法，请输入负数
	 * @param date 时间
	 * @param amount 数量
	 * @return 计算后的时间
	 */
	public static Date addHour(Date date, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, amount);
		return calendar.getTime();
	}
	
	/**
	 * 在时间基础上，加上N分钟，如果要作减法，请输入负数
	 * @param date 时间
	 * @param amount 数量
	 * @return 计算后的时间
	 */
	public static Date addMinute(Date date, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, amount);
		return calendar.getTime();
	}
	
	/**
	 * 在时间基础上，加上N秒，如果要作减法，请输入负数
	 * @param date 时间
	 * @param amount 数量
	 * @return 计算后的时间
	 */
	public static Date addSecond(Date date, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, amount);
		return calendar.getTime();
	}
	
	public static String toWeekDay(String date, String srcPattern) {
		try {
			SimpleDateFormat srcSdf = new SimpleDateFormat(srcPattern);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(srcSdf.parse(date));
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			return (dayOfWeek == 1) ? "7" : String.valueOf(dayOfWeek - 1);
		} catch (ParseException e) {
			return null; // 格式化失败，返回null
		}
	}
	
	/**
	 * 获取本月第一天时间，如YYYY-MM-01
	 * @param day 日期
	 * @return
	 */
	public static Date getMonth(Date day) {
		String yyyyMM = DateFormatUtil.format(day, DateFormatUtil.YEAR_MONTH);
		Date month = DateFormatUtil.parse(yyyyMM, DateFormatUtil.YEAR_MONTH);
		return month;
	}
	
	/**
	 * 获取本月第一天时间，如YYYY-MM-01
	 * @return
	 */
	public static Date getMonth() {
		return getMonth(new Date());
	}
	
	/**
	 * 获取本月第一天
	 * @param day YYYY-MM-DD
	 * @return
	 */
	public static Date getFirstDayOfMonth(Date day) {
		return getMonth(day);
	}
	
	/**
	 * 获取本月第一天
	 * @return
	 */
	public static Date getFirstDayOfMonth() {
		return getFirstDayOfMonth(new Date());
	}
	
	/**
	 * 获取[下月]第一天
	 * @param day YYYY-MM-DD
	 * @return
	 */
	public static Date getFirstDayOfNextMonth(Date day) {
		return addMonth(getMonth(day), 1);
	}
	
	/**
	 * 获取[下月]第一天
	 * @return
	 */
	public static Date getFirstDayOfNextMonth() {
		return getFirstDayOfNextMonth(new Date());
	}
	
	/**
	 * 获取本月最后一天
	 * @param day
	 * @return
	 */
	public static Date getLastDayOfMonth(Date day) {
		Date firstDay = getFirstDayOfMonth(day);
		Date nextMonthFirstDay = addMonth(firstDay, 1);
		return addDay(nextMonthFirstDay, -1);
	}
	
	/**
	 * 获取本月最后一天
	 * @param day
	 * @return
	 */
	public static Date getLastDayOfMonth() {
		return getLastDayOfMonth(new Date());
	}
	
	/**
	 * 将日期转成制定格式
	 * @param day 源日期
	 * @param destPattern 转换模式
	 * @return
	 */
	public static Date parse(Date srcDate, String destPattern) {
		String date = format(srcDate, destPattern);
		return parse(date, destPattern);
	}
	
	public static void main(String[] args) {
		System.err.println(format(getFirstDayOfMonth()));
		System.err.println(format(getLastDayOfMonth()));
		System.err.println(format(getFirstDayOfNextMonth()));
		System.err.print(format(parse(new Date(), DATE)));
	}
}
