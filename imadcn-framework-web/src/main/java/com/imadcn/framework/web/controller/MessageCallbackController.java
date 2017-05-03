package com.imadcn.framework.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.imadcn.framework.web.core.Message;
import com.imadcn.framework.web.core.PostType;
import com.imadcn.framework.web.filter.MessageContainer;

@Controller
@RequestMapping("/message/callback")
public class MessageCallbackController {
	
	protected static final Logger logger = LoggerFactory.getLogger(MessageCallbackController.class);
	
	@Autowired
	private MessageContainer rcvMsgExceutor;
	
	@RequestMapping("/receive")
	public void receive(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, String> data) {
		try {
			String postType = data.get("post_type");
			if (PostType.equals(postType, PostType.RECEIVE_MESSAGE)) {
				Message message = JSON.parseObject(JSON.toJSONString(data), Message.class);
				rcvMsgExceutor.doFilter(message);
			} else {
				logger.info("ignore post type [{}]", postType);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}
}
