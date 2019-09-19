package com.yunda.base.common.utils;

import org.springframework.messaging.simp.SimpMessagingTemplate;

//websocket推消息给页面
public class PushWebsocketMsgUtils {
	private SimpMessagingTemplate template;
	private static String defaultDestination = "/topic/getResponse";

	public boolean pushMsg(String msg) {
		return pushMsg(defaultDestination, msg);
	}

	public boolean pushMsg(String destination, String msg) {
		getSimpMessagingTemplate().convertAndSend("/topic/getResponse", R.ok(msg));

		return true;
	}

	private SimpMessagingTemplate getSimpMessagingTemplate() {
		if (template == null) {
			template = SpringUtil.getBean(SimpMessagingTemplate.class);
		}
		return template;
	}
}
