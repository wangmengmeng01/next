package com.yunda.base.common.utils;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
	public static final String EXCHANGE_DRIVER = "driverExchange";
	public static final String ROUTINGKEY_DRIVER = "driverKey";
	public static final String RABBITMQ_QUEUE_DRIVER_LOCATION = "new_driver_location";
	
	/**消费者数量，默认10*/
	public static final int DEFAULT_CONCURRENT = 5;
	/**每个消费者获取最大投递数量 默认50 */
	public static final int DEFAULT_PREFETCH_COUNT = 100;
	
	@Value("${spring.rabbitmq.host}")
	private String address;

	@Value("${spring.rabbitmq.username}")
	private String username;

	@Value("${spring.rabbitmq.password}")
	private String password;
	
	/**
	 * 配置链接信息
	 * 
	 * @return connectionFactory
	 */
	@Bean(name = "connectionFactory")
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setAddresses(address);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		connectionFactory.setVirtualHost("/");
		connectionFactory.setPublisherConfirms(true); // 必须要设置
		connectionFactory.setRequestedHeartBeat(5);
		return connectionFactory;
	}
	

	/**
	 * driverLocationTemplete 用来发送消息
	 * 
	 * @param connectionFactory
	 * @return template
	 */
	@Bean(name = "driverRabbitmqTemplate")
	public RabbitTemplate driverRabbitmqTemplate(
			@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		return template;
	}

	/**
	 * SimpleMessageListenerContainer，消费者
	 * 
	 * @param configurer
	 * @param connectionFactory
	 * @return
	 */
	@Bean(name = "driverLocationFactory")
	public SimpleRabbitListenerContainerFactory wxPushFactoryDriver(SimpleRabbitListenerContainerFactoryConfigurer configurer,
			@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setPrefetchCount(DEFAULT_PREFETCH_COUNT);
		factory.setConcurrentConsumers(DEFAULT_CONCURRENT);
		configurer.configure(factory, connectionFactory);
		return factory;
	}

	/**
	 * 构建队列，名称，是否持久化之类
	 * 
	 * @return
	 */
	@Bean(RABBITMQ_QUEUE_DRIVER_LOCATION)
	public Queue queueDriver() {
		return new Queue(RABBITMQ_QUEUE_DRIVER_LOCATION, true); // 队列持久
	}
	
	

}
