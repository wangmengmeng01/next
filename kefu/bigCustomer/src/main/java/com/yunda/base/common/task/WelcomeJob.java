package com.yunda.base.common.task;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WelcomeJob extends TaskAbs {
	@Autowired
	SimpMessagingTemplate messagingTemplate;

	@Override
	public void run(JobExecutionContext arg0) {
		// 针对单用户发送
		messagingTemplate.convertAndSendToUser("guest", "/queue/notifications", "新消息：guest");
		messagingTemplate.convertAndSendToUser("test", "/queue/notifications", "新消息：test");
		messagingTemplate.convertAndSendToUser("admin", "/queue/notifications", "新消息：admin");

		// messagingTemplate.convertAndSend("/topic/getResponse",
		// new
		// Response("欢迎体验bootbase,这是一个任务计划，使用了websocket和quzrtz技术，可以在计划列表中取消，欢迎您加入qq群交流学习!"));
	}

	@Override
	public String whoareyou() {
		return "测试job";
	}
}