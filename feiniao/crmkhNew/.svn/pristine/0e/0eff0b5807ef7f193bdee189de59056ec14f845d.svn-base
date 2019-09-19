package com.yunda.base.system.aspect;

import com.yunda.base.common.utils.HttpContextUtils;
import com.yunda.base.common.utils.ShiroUtils;
import com.yunda.base.system.annotation.LoginLog;
import com.yunda.base.system.domain.LoginLogDO;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.service.LoginLogService;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.JSONUtils;
import com.yunda.ydmbspringbootstarter.common.utils.NetUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
public class LoginLogAspect {

	@Autowired
	LoginLogService loginLogService;

	@Pointcut("@annotation(com.yunda.base.system.annotation.LoginLog)")
	public void logPointCut() {
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		// 执行方法
		Object result = point.proceed();
		// 执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;
		//异步保存日志
		saveLog(point, time);
		return result;
	}

	@Async
	void saveLog(ProceedingJoinPoint joinPoint, long time) throws InterruptedException {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		LoginLogDO sysLog = new LoginLogDO();
		LoginLog syslog = method.getAnnotation(LoginLog.class);
		if (syslog != null) {
			// 注解上的描述
			sysLog.setOperation(syslog.value());
		}
		// 请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
//		sysLog.setMethod(className + "." + methodName + "()");
		// 请求的参数
		Object[] args = joinPoint.getArgs();
		try {
			String params = JSONUtils.beanToJson(args[0]).substring(0, 4999);
//			sysLog.setParams(params);
		} catch (Exception e) {

		}
		// 获取request
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		//设置sessionId
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String id = session.getId().toString();
		sysLog.setSessionId(id);
		// 设置IP地址
		sysLog.setUserIp(NetUtils.getIpAddr(request));
		// 用户名
		UserDO currUser = ShiroUtils.getUser();
		if (null != currUser) {
//			sysLog.setUserId(ShiroUtils.getUserId());
//			sysLog.setUsername(ShiroUtils.getUser().getUsername());
			sysLog.setUserId(ShiroUtils.getUser().getUsername());
//			sysLog.setTime((int) time);
			// 系统当前时间
            String date = DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
			sysLog.setCreateTime(date);
			// 保存系统日志
			loginLogService.save(sysLog);
		}
	}
}
