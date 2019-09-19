package com.yunda.base.system.config;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import com.yunda.base.common.config.InjectServiceUtil;
import com.yunda.base.system.domain.OutLogDO;
import com.yunda.base.system.service.OutLogService;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
public class BDSessionListener implements ServletContextListener, SessionListener {

	private final AtomicInteger sessionCount = new AtomicInteger(0);


	@Override
	public void onStart(Session session) {
		sessionCount.incrementAndGet();
	}

	@Override
	public void onStop(Session session) {
		System.out.println(session.getId());
		sessionCount.decrementAndGet();

	}

	/**
	 * session会话超时
	 * @param session
	 */
	@Override
	public void onExpiration(Session session) {
		InjectServiceUtil instance = InjectServiceUtil.getInstance();
		OutLogService outLogService = instance.getOutLogService();
		OutLogDO sysLog = new OutLogDO();
		String sessionId =  session.getId().toString();
		sysLog.setSessionId(sessionId);
		// 系统当前时间(退出登陆时间)
		String outTime = DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		sysLog.setOutTime(outTime);
		// 保存系统日志
		outLogService.save(sysLog);
	}

	public int getSessionCount() {
		return sessionCount.get();
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		//this.outLogService = SpringContextHolder.getBean("OutLogService");
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

	}
}
