package com.yunda.base.system.config;

import java.security.Principal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.yunda.base.feiniao.report.core.CRM_constants;
import com.yunda.base.system.domain.UserDO;

/**
 * 通过EnableWebSocketMessageBroker 开启使用STOMP协议来传输基于代理(message
 * broker)的消息,此时浏览器支持使用@MessageMapping 就像支持@RequestMapping一样。
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
	private static final Logger LOG = Logger.getLogger(WebSocketConfig.class);

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) { // endPoint
		// 注册websocket，客户端用ws://host:port/项目名/webSocket 访问
		// registry.addEndpoint("/endpointChat").withSockJS();
		// registry.addEndpoint("/websocket").withSockJS();

		registry.addEndpoint("/endpointChat").setHandshakeHandler(new DefaultHandshakeHandler() {
			@Override
			protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
					Map<String, Object> attributes) {

				Subject currentUser = SecurityUtils.getSubject();
				Session session = currentUser.getSession();

				UserDO userDo=(UserDO) session.getAttribute(CRM_constants.AUTH_USER);

				if (StringUtils.isNotBlank(userDo.getUsername())) {
					// key就是服务器和客户端保持一致的标记，一般可以用账户名称，或者是用户ID。
					return new MyPrincipal(userDo.getUsername());
				}

				return null;
			}
		}).withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {// 配置消息代理(message
		registry.enableSimpleBroker("/topic", "/queue");// 这句话表示在topic和user这两个域上服务端可以向客户端发消息
		registry.setApplicationDestinationPrefixes("/ws");// 这句话表示客户端向服务器端发送时的主题上面需要加"/ws"作为前缀
		registry.setUserDestinationPrefix("/user");// 这句话表示服务端给客户端指定用户发送一对一的主题，前缀是"/user"

	}

	/**
	 * 自定义的Principal
	 */
	class MyPrincipal implements Principal {

		private String key;

		public MyPrincipal(String key) {
			this.key = key;
		}

		@Override
		public String getName() {
			return key;
		}

	}
}
