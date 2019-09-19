package com.yunda.base.common.aspect;

import com.alibaba.fastjson.JSON;
import com.yunda.base.common.enums.RespEnum;
import com.yunda.base.feiniao.report.core.CRM_constants;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.MD5Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;
/**
 * 用于保护目标controller方法在执行过程中重复提交不被重复执行
 * 场景：如果一段逻辑正在处理订单的某流程，而用户等不及又重复提交请求，基于redis加锁可防止其他机器响应处理
 * 
 * @author Grimm
 *
 */
@Aspect
@Component
public class MethodLockAspect {
	private final Logger log = LoggerFactory.getLogger(MethodLockAspect.class);
	private static String suffix = "mlock";
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Around("@annotation(com.yunda.ydmbspringbootstarter.common.annotation.MethodLock)")
	public Object doAccessCheck(ProceedingJoinPoint pjp) throws Throwable {
		String className = pjp.getTarget().getClass().getSimpleName();
		String methodName = pjp.getSignature().getName();
		Object[] args = pjp.getArgs();

		Class<?> classTarget = pjp.getTarget().getClass();
		Class<?>[] par = ((MethodSignature) pjp.getSignature()).getParameterTypes();
		Method objMethod = classTarget.getMethod(methodName, par);

		MethodLock ml = objMethod.getAnnotation(MethodLock.class);
		if (ml == null) {
			return pjp.proceed();
		}

		int lockSeconds = ml.lockSeconds();
		// 按annonation提供的key隔离锁
		String key = ml.key();
		if (StringUtils.isBlank(key)) {
			return pjp.proceed();
		}

		log.debug("doAccessCheck 开始----" + key);

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		if (key.equals("request")) {
			// 按所有的request参数隔离锁
			Map map = request.getParameterMap();
			key = MD5Utils.encrypt(JSON.toJSONString(map));
		} else if (key.startsWith("request.")) {
			// 按指定的request参数隔离锁
			String param = key.substring(key.indexOf("request.") + "request.".length());
			key = request.getParameter(param);
		}

		// 按类，方法，隔离锁
		key = suffix + "_" + className + "_" + methodName + "_" + key;

		// 按操作人隔离锁
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser != null) {
			Session session = currentUser.getSession();
			if (session != null) {
				UserDO userDo=(UserDO) session.getAttribute(CRM_constants.AUTH_USER);
				if (StringUtils.isNotBlank(userDo.getUsername())) {
					key += userDo.getUsername();
				}
			}
		}

		String value = stringRedisTemplate.opsForValue().get(key);
		if (value != null) {
			//复查
			long v = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
			if (v == -1) {
				log.info("清理 ttl -1 的锁标记");
				stringRedisTemplate.delete(key);
			} else {
				log.info("重复请求----" + key + "超时时间：" + v);
				// 重复请求
				//return null;
				//return R.error("重复请求");
				return new RspBean(RespEnum.ERROR_REQ_AGAIN.getCode(),"重复请求");
			}
		}

		log.debug("加锁----" + key);
		// 加锁
		stringRedisTemplate.opsForValue().set(key, "1", lockSeconds, TimeUnit.SECONDS);
		// 执行
		Object result = pjp.proceed();
		// 释放锁
		stringRedisTemplate.delete(key);
		log.debug("放锁----" + key);

		return result;
	}
}
