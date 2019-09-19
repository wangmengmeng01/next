package com.yunda.base.system.websocket;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.LinkedList;
import java.util.Map;

/**
 * https://blog.csdn.net/qq_28988969/article/details/78134114?locationNum=9&fps=1
 * @ClassName: UserInterceptor
 * @Description: 客户端渠道拦截适配器
 * @author cheng
 * @date 2017年9月29日 下午2:40:12
 */
public class UserInterceptor extends ChannelInterceptorAdapter {

	/**
	 * 获取包含在stomp中的用户信息
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
			if (raw instanceof Map) {
				Object name = ((Map) raw).get("login");
				if (name instanceof LinkedList) {
					// 设置当前访问器的认证用户
					accessor.setUser(new User(((LinkedList) name).get(0).toString()));
				}
			}
		}
		return message;
	}
}