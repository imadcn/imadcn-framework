package com.imadcn.framework.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 正则表达式，数据验证工具
 * @author iMad
 * @since 20130811.1158
 * @version 1.0
 */
public class RegexUtil {
	
	/**
	 * 判断邮件地址是否正确
	 * @param address 邮件地址
	 * @return 正确返回true，否则返回false
	 */
	public static boolean isEmail(String address) {
		if (address == null || "".equals(address) || address.indexOf("..") != -1) //地址不为null, 不为"",不能有两个连续的.
			return false;
		String regex = "[a-zA-Z0-9][\\w-.]+[a-zA-Z0-9]@[a-zA-Z0-9][\\w-.]+[a-zA-Z]"; // 匹配正则表达式
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(address);
		return matcher.matches();
	}
	
	/**
	 * 判断手机号码(11位)是否正确
	 * @param cellphone 手机号
	 * @return 正确返回true，否则返回false
	 */
	public static boolean isCellphone(String cellphone) {
		/*
		 * 移动号段：134~139，147，150~152，157~159，181~183， 187~188，178
		 * 联通号段：130~132，155~156，185~186，176
		 * 电信号段：133，153，180，181，189，177，173
		 * 虚拟运营：170，171
		 * 13[0,1,2,3,4,5,6,7,8,9]
		 * 147
		 * 15[0,1,2,3,5,6,7,8,9]
		 * 18[0,1,2,3,5,6,7,8,9]
		 */
		if (cellphone == null || "".equals(cellphone))
			return false;
		String regex = "^1([34578][0-9])[0-9]{8}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(cellphone);
		return matcher.matches();
	}
	
	/**
	 * 判断是否为金额数字类型，区间[0,+oo)，除小于1的小数外，首位不能为0，请小数点最多保留两位
	 * @param cellphone 手机号
	 * @return 正确返回true，否则返回false
	 */
	public static boolean isMoney(String moeny) {
		if (moeny == null || "".equals(moeny))
			return false;
		String regex = "^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){1,2})?$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(moeny);
		return matcher.matches();
	}
	
	/**
	 * 判断IP地址是否正确
	 * @param cellphone 手机号
	 * @return 正确返回true，否则返回false
	 */
	public static boolean isIpAddrress(String ip) {
		if (ip == null || "".equals(ip))
			return false;
		// String regex = "^1[3458][0-9]{9}$"; // 手机号匹配正则表达式
		String regex = "((25[0-5]|2[0-4]\\d|1?\\d?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1?\\d?\\d)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}
	
	/**
	 * 校验短日期格式[yyyyMMdd]
	 * @param date 短日期
	 * @return 正确返回true，错误返回false
	 */
	public static boolean isShortDate(String date) {
		if (date == null || "".equals(date))
			return false;
		String regex = "^([\\d]{4}(((0[13578]|1[02])((0[1-9])|([12][0-9])|(3[01])))|(((0[469])|11)((0[1-9])|([12][1-9])|30))|(02((0[1-9])|(1[0-9])|(2[1-8])))))|((((([02468][048])|([13579][26]))00)|([0-9]{2}(([02468][048])|([13579][26]))))(((0[13578]|1[02])((0[1-9])|([12][0-9])|(3[01])))|(((0[469])|11)((0[1-9])|([12][1-9])|30))|(02((0[1-9])|(1[0-9])|(2[1-9])))))$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(date);
		return matcher.matches();
	}
	
	/**
	 * 判断传入的日期是否>=今天
	 * @param date 待判断的日期
	 * @return 是返回true，否则返回false
	 */
	public static boolean isNotLessThanToday(String date) {
		return isNotLessThanToday(date, DateFormatUtil.DATE);
	}
	
	/**
	 * 判断传入的日期是否>=今天
	 * @param date 待判断的日期
	 * @return 是返回true，否则返回false
	 */
	public static boolean isNotLessThanToday(String date, String datePattern) {
		if (date == null || date.isEmpty())
			return false;
		Date cmpDate = DateFormatUtil.addDay(new Date(), -1); // 以昨天为基准
		Date srcDate = DateFormatUtil.parse(date, datePattern);
		return srcDate.after(cmpDate);
	}
	
	/**
	 * 批量判断是否<b> 全部不为 </b> null 或者 ""
	 * @param values 待判断的字符串数组
	 * @return 是返回true，否则返回false
	 */
	public static boolean isNotEmpty(String... values) {
		if (values != null && values.length > 0) {
			for (String val : (String[]) values) {
				if (val == null || val.isEmpty()) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 批量判断是否<b> 全部不为 </b> null 或者 ""
	 * @param values 待判断的字符串数组
	 * @return 是返回true，否则返回false
	 */
	public static boolean isNotNull(String... values) {
		if (values != null && values.length > 0) {
			for (String val : (String[]) values) {
				if (val == null) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
		
	/** 
	 * 判断字符串是否<b> 包含 </b> null 或者 ""
	 * @param values 待判断的字符串
	 * @return 是返回true，否则返回false
	 */
	public static boolean isEmpty(String... values) {
		if (values != null && values.length > 0) {
			for (String val : values) {
				if (val == null || val.isEmpty()) {
					return true;
				}
			}
			return false;
		}
		return true;
	}
	
	/** 
	 * 判断字符串是否<b> 全部为 </b> null 或者 ""
	 * @param values 待判断的字符串
	 * @return 是返回true，否则返回false
	 */
	public static boolean isAllEmpty(String... values) {
		if (values != null && values.length > 0) {
			int count = 0;
			for (String val : values) {
				if (val == null || val.isEmpty()) {
					count++;
				}
			}
			return count == values.length;
		}
		return true;
	}
	
	/** 
	 * 判断字符串是否<b> 包含 </b> null 或者 ""
	 * @param values 待判断的字符串
	 * @return 是返回true，否则返回false
	 */
	public static boolean isEmpty(List<String> values) {
		if (values != null && !values.isEmpty()) {
			for (String val : values) {
				if (val == null || val.isEmpty()) {
					return true;
				}
			}
			return false;
		}
		return true;
	}
	
	public static boolean isInteger(String... values) {
		if (values != null && values.length > 0) {
			for (String v : values) {
				try {
					Integer.parseInt(v);
				} catch (Exception e) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 验证是否为int
	 * @param value 待判断的字符串
	 * @return 是返回true，否则返回false
	 */
	public static boolean isPositiveInteger(String... values) {
		if (isInteger(values)) {
			for (String v : values) {
				if (Integer.parseInt(v) <= 0) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否为非负数
	 * @param value
	 * @return
	 */
	public static boolean isNotNegativeInteger(String... values) {
		if (isInteger(values)) {
			for (String v : values) {
				if (Integer.parseInt(v) < 0) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否为非负数
	 * @param value
	 * @return
	 */
	public static boolean isNotNegativeInteger(int... value) {
		if (value != null && value.length > 0) {
			for (int v : value) {
				if (v < 0) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 验证是否为int
	 * @param value 待判断的数据
	 * @return 是返回true，否则返回false
	 */
	public static boolean isPositiveInteger(int... value) {
		if (value != null && value.length > 0) {
			for (int v : value) {
				if (v <= 0) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 验证是否为long
	 * @param value 待判断的字符串
	 * @return 是返回true，否则返回false
	 */
	public static boolean isPositiveLong(String value) {
		try { 
			return Long.parseLong(value) > 0;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isDouble(String... values) {
		if (values != null && values.length > 0) {
			for (String v : values) {
				try {
					Double.parseDouble(v);
				} catch (Exception e) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public static boolean isNotNegativeDouble(double... values) {
		if (values != null && values.length > 0) {
			for (double v : values) {
				if (v < 0) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public static boolean isNotNegativeDouble(String... values) {
		if (isDouble(values)) {
			for (String v : values) {
				if (Double.parseDouble(v) < 0) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 验证是否为double
	 * @param value 待判断的字符串
	 * @return 是返回true，否则返回false
	 */
	public static boolean isPositiveDouble(double... values) {
		if (values != null && values.length > 0) {
			for (double v : values) {
				if (v <= 0) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 验证是否为double
	 * @param value 待判断的字符串
	 * @return 是返回true，否则返回false
	 */
	public static boolean isPositiveDouble(String... values) {
		if (isDouble(values)) {
			for (String v : values) {
				if (Double.parseDouble(v) <= 0) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 验证是否为float
	 * @param value 待判断的字符串
	 * @return 是返回true，否则返回false
	 */
	public static boolean isPositiveFloat(String value) {
		try { 
			return Float.parseFloat(value) > 0;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 校验日期格式,日期不能早于当前天
	 * @param dptDate 日期，仅需包含年月日
	 * @param pattern 日期转移格式
	 * @return 通过返回true，失败返回false
	 */
	public static boolean isDate(String dptDate, String pattern) {
		if (dptDate == null || dptDate.isEmpty())
			return false;
		String formatDate = DateFormatUtil.format(dptDate, pattern, pattern);
		// 时间校验，如果"标准化"转换后，与原数据一致，则相同，像20130229这种不正确的日期将转换为20130301，则时间错误
		if (formatDate != null && formatDate.equals(dptDate)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 校验日期格式,日期不能早于当前天， 默认日期转义格式：yyyy-MM-dd
	 * @param dptDate 日期，仅需包含年月日
	 * @return 通过返回true，失败返回false
	 */
	public static boolean isDate(String dptDate) {
		return isDate(dptDate, DateFormatUtil.DATE);
	}
	
	/**
	 * 校验前面的日期go，是否早于或者等于后面的日期back
	 * @param go 日期1
	 * @param back 日期2
	 * @param pattern 日期正则表达式
	 * @return go<=back返回true，否则返回false
	 */
	public static boolean isBefore(String go, String back, String pattern) {
		if (go == null || back == null || go.isEmpty() || back.isEmpty())
			return false;
		// 提起往前挪一天，不然相同日期没有办法做判断
		Date goDate = DateFormatUtil.addDay(DateFormatUtil.parse(go, pattern), -1); 
		Date backDate = DateFormatUtil.parse(back, pattern);
		if (goDate != null && backDate != null) {
			return goDate.before(backDate);
		}
		return false;
	}
	
	/**
	 * 校验前面的日期go，是否早于或者等于后面的日期back
	 * @param go 日期1
	 * @param back 日期2
	 * @return go<=back返回true，否则返回false
	 */
	public static boolean isBefore(String go, String back) {
		return isBefore(go, back, DateFormatUtil.DATE);
	}
	
	/**
	 * 验证长日期格式yyyy-MM-dd HH:mm:ss
	 * @param datetime 日期
	 * @return 成功返回true，失败返回false
	 */
	public static boolean isDatetime(String datetime) {
		return isDate(datetime, DateFormatUtil.DATE_TIME);
	}
	
	/**
	 * 根据出发日期，判断身份证是否是成人 >=12周岁
	 * @param ni 身份证号码
	 * @param dptDate 出发日期，默认格式 yyyy-MM-dd
	 * @return 成功返回true，失败返回false
	 */
	public static boolean isAdultIDCard(String ni, String dptDate) {
		return isAdultIDCard(ni, dptDate, DateFormatUtil.DATE);
	}
	
	/**
	 * 根据出发日期，判断身份证是否是成人 >=12周岁
	 * @param ni 身份证号码
	 * @param dptDate 出发日期
	 * @param pattern 日期格式
	 * @return 成功返回true，失败返回false
	 */
	public static boolean isAdultIDCard(String ni, String dptDate, String pattern) {
		if (ni == null || ni.length() != 18) {
			return false;
		}
		try {
			if ("".equals(IDCardUtil.IDCardValidate(ni.toUpperCase()))) { // 校验身份证正确性
				String birthday = DateFormatUtil.format(ni.substring(6, 14), DateFormatUtil.SHORT_DATE, pattern);
				if (isAdultBirthday(birthday, dptDate, pattern)) { // 截取身份证，校验出身日期
					return true;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * 根据出发日期，判断身份证是否是儿童 >=2 <12周岁
	 * @param ni 身份证号码
	 * @param dptDate 出发日期，默认格式 yyyy-MM-dd
	 * @return 成功返回true，失败返回false
	 */
	public static boolean isChildIDCard(String ni, String dptDate) {
		return isChildIDCard(ni, dptDate, DateFormatUtil.DATE);
	}
	
	/**
	 * 根据出发日期，判断身份证是否是儿童 >=2 <12周岁
	 * @param ni 身份证号码
	 * @param dptDate 出发日期，默认格式 yyyy-MM-dd
	 * @param pattern 日期格式
	 * @return 成功返回true，失败返回false
	 */
	public static boolean isChildIDCard(String ni, String dptDate, String pattern) {
		if (ni == null || ni.length() != 18) {
			return false;
		}
		try {
			if ("".equals(IDCardUtil.IDCardValidate(ni))) { // 校验身份证正确性
				String birthday = DateFormatUtil.format(ni.substring(6, 14), DateFormatUtil.SHORT_DATE, pattern);
				if (isChildBirthday(birthday, dptDate, pattern)) { // 截取身份证，校验出身日期
					return true;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * 判断生日是否是成人 >=12周岁
	 * @param birthday 出生日期，默认格式yyyy-MM-dd
	 * @param dptDate 出发日期
	 * @return 成功返回true，失败返回false
	 */
	public static boolean isAdultBirthday(String birthday, String dptDate) {
		return isAdultBirthday(birthday, dptDate, DateFormatUtil.DATE);
	}
	
	/**
	 * 判断生日是否是成人 >=12周岁
	 * @param birthday 出生日期
	 * @param dptDate 出发日期
	 * @param pattern 出生日期格式
	 * @return 成功返回true，失败返回false
	 */
	public static boolean isAdultBirthday(String birthday, String dptDate, String pattern) {
		if (birthday != null && dptDate != null && pattern != null) {
			if (isDate(birthday, pattern) && isDate(dptDate, pattern)) {
				Date srcDate = DateFormatUtil.parse(birthday, pattern); // 2001-12-18
				Date cmpDate = DateFormatUtil.addYear(DateFormatUtil.parse(dptDate, pattern), -12); // 判断标准 12岁以上 // 2013-12-17
				if (!cmpDate.before(srcDate)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 判断生日是否为儿童 >=2周岁  <12周岁
	 * @param birthday 出生日期，默认格式yyyy-MM-dd
	 * @param dptDate 出发日期
	 * @return 成功返回true，失败返回false
	 */
	public static boolean isChildBirthday(String birthday, String dptDate) {
		return isChildBirthday(birthday, dptDate, DateFormatUtil.DATE);
	}
	
	/**
	 * 判断生日是否为儿童 >=2周岁  <12周岁
	 * @param birthday 出生日期
	 * @param dptDate 出发日期
	 * @param pattern 出生日期格式
	 * @return 成功返回true，失败返回false
	 */
	public static boolean isChildBirthday(String birthday, String dptDate, String pattern) {
		if (birthday != null && dptDate != null && pattern != null) {
			if (isDate(birthday, pattern) && isDate(dptDate, pattern)) {
				Date srcDate = DateFormatUtil.parse(birthday, pattern); // 2001-12-18
				Date bgnDate = DateFormatUtil.addYear(DateFormatUtil.parse(dptDate, pattern), -2);   // 2周岁
				Date endDate = DateFormatUtil.addYear(DateFormatUtil.parse(dptDate, pattern), -12);  // 12周岁
				if (!bgnDate.before(srcDate) && endDate.before(srcDate)) { // >=2 <12
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 校验身份证号码是否正确
	 * @param value 身份证号
	 * @return 成功返回true，失败返回false
	 */
	public static boolean isIDCard(String value) {
		if (value == null || value.length() != 18) {
			return false;
		}
		try {
			if ("".equals(IDCardUtil.IDCardValidate(value))) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * 验证格式为XXXX,XXXX,XXXX的数据（XXXX的取值范围a-zA-Z0-9）
	 * @param data 
	 * @return
	 */
	public static boolean isSplitData(String data) {
		if (data == null || "".equals(data.trim()))
			return false;
		String regex = "^([a-zA-Z0-9]+,?)+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(data);
		return matcher.matches();
	}
	
	/**
	 * 判断是否为中文
	 * @param value
	 * @return
	 */
	public static boolean isChinese(String value) {
		if (value == null || "".equals(value))
			return false;
		String regex = "^[\u4e00-\u9fa5]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
	
	/**
	 * 判断元素是否<b>在</b>给定集合里面
	 * @param value 指定元素
	 * @param set 集合
	 * @return
	 */
	public static boolean isIn(String value, String[] set) {
		if (RegexUtil.isEmpty(value) || RegexUtil.isEmpty(set)) {
			throw new IllegalArgumentException("参数错误");
		}
		for (String cmp : set) {
			if (value.equals(cmp)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断元素是否<b>在</b>给定集合里面
	 * @param value 指定元素
	 * @param set 集合
	 * @return
	 */
	public static boolean isIn(int value, int[] set) {
		if (set == null || set.length == 0) {
			throw new IllegalArgumentException("参数错误");
		}
		for (int cmp : set) {
			if (value == cmp) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断元素是否<b>不在</b>给定集合里面
	 * @param value 指定元素
	 * @param set 集合
	 * @return
	 */
	public static boolean isNotIn(String value, String[] set) {
		if (RegexUtil.isEmpty(value) || RegexUtil.isEmpty(set)) {
			throw new IllegalArgumentException("参数错误");
		}
		for (String cmp : set) {
			if (value.equals(cmp)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断元素是否<b>不在</b>给定集合里面
	 * @param value 指定元素
	 * @param set 集合
	 * @return
	 */
	public static boolean isNotIn(int value, int[] set) {
		if (set == null || set.length == 0) {
			throw new IllegalArgumentException("参数错误");
		}
		for (int cmp : set) {
			if (value == cmp) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 分，转换成两位小数的元
	 * @param fen
	 * @return
	 */
	public static String fenToYuan(int fen) {
		return new BigDecimal(fen).divide(new BigDecimal(100)).toString();
	}
	
	/**
	 * 拼接字符串，null作<b> "" </b>处理
	 * @param values
	 * @return
	 */
	public static String concat(String... values) {
		StringBuilder sb = new StringBuilder();
		if (values != null && values.length > 0) {
			for (String v : values) {
				if (v != null) {
					sb.append(v);
				}
			}
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(isChinese("中文"));
	}
}
