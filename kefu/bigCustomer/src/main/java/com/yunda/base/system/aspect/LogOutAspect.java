package com.yunda.base.system.aspect;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.yunda.base.system.domain.OutLogDO;
import com.yunda.base.system.service.OutLogService;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
/**
 * 退出登陆日志(主要记录退出时间和sessionId)
 */
@Aspect
@Component
public class LogOutAspect {
	Logger log = Logger.getLogger(getClass());
	
	@Autowired
	OutLogService outLogService;

	@Pointcut("@annotation(com.yunda.base.system.annotation.LogOut)")
	public void logPointCut() {
	}

	@Before("logPointCut()")
	public void before(JoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		// 执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;
		//异步保存日志
		try {
			saveLog(point, time);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}

	@Async
	void saveLog(JoinPoint joinPoint, long time) {
		OutLogDO sysLog = new OutLogDO();
		//设置sessionId

        Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String id = session.getId().toString();
		sysLog.setSessionId(id);
		// 系统当前时间(退出登陆时间)
		String date = DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		sysLog.setOutTime(date);
		// 保存系统日志
		outLogService.save(sysLog);
	}
}
