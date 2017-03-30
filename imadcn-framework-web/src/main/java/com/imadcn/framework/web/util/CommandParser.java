package com.imadcn.framework.web.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.imadcn.framework.util.RegexUtil;

/**
 * 指令动态数据解析
 * @author imadcn
 */
public class CommandParser {
	
	protected static final Logger logger = LoggerFactory.getLogger(CommandParser.class);

	/**
	 * 解析数据
	 * @param command 源指令数据
	 * @param regex 指令正则表达式
	 * @param commandKeys 指令关键字
	 * @return
	 */
	public static String[] parse(String command, String regex, String[] commandKeys) {
		if (RegexUtil.isEmpty(command)) {
			throw new IllegalArgumentException("command mustn't be null or empty");
		}
		if (RegexUtil.isEmpty(regex)) {
			throw new IllegalArgumentException("regex mustn't be null or empty");
		}
		if (RegexUtil.isEmpty(commandKeys)) {
			throw new IllegalArgumentException("command keys mustn't be null or empty");
		}
		List<String> values = new ArrayList<>();
		Pattern p = Pattern.compile(regex);
		if (p.matcher(command).matches()) { // 满足正则
			String tmpCommand = command;
			for (int i = 0; i < commandKeys.length; i++) {
				String key = commandKeys[i];
				String[] tmpValue = tmpCommand.split(key, 2); // 前后key
				if (!RegexUtil.isEmpty(tmpValue[0])) {
					values.add(tmpValue[0].trim());
				}
				tmpCommand = tmpValue[1];
				if (i == commandKeys.length - 1) {
					if (!RegexUtil.isEmpty(tmpCommand)) {
						values.add(tmpCommand.trim());
					}
				}
			} 
			
		} else {
			logger.info("command [{}] didn't match regex [{}]", command, regex);
		}
		return values.toArray(new String[values.size()]);
	}
	
	public static void main(String[] args) {
		String command = "/tt somke @iMad 100";
		String regex = "/tt somke (.)+ \\d+";
		String[] commandKeys={"/tt somke ", " "};
		String[] tmp = command.split(regex);
		System.out.println(JSON.toJSONString(tmp));
/*		String command = "给 @ i M a d 来一发!!!";
		String regex = "给(.)+来一发(.)+";
		String[] commandKeys={"给", "来一发"};
*/		
		System.err.println(JSON.toJSONString(parse(command, regex, commandKeys)));
	}
}
