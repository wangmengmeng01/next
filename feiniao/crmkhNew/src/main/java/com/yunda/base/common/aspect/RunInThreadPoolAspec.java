package com.yunda.base.common.aspect;

import com.alibaba.fastjson.JSON;
import com.yunda.base.common.bo.Bo_Interface;
import com.yunda.base.common.enums.RespEnum;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.feiniao.report.core.CRM_constants;
import com.yunda.base.feiniao.report.service.ReportThreadPoolManager;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.annotation.RunInThreadPool;
import com.yunda.ydmbspringbootstarter.common.utils.MD5Utils;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class RunInThreadPoolAspec {
	private Logger log = Logger.getLogger(getClass());
	@Autowired
	private RedisTemplate redisTemplate;

	@Around("@annotation(com.yunda.ydmbspringbootstarter.common.annotation.RunInThreadPool)")
	public Object doAccessCheck(final ProceedingJoinPoint pjp) throws Throwable {
		if (SysConfig.report_thread_flag == null || !SysConfig.report_thread_flag.equalsIgnoreCase("open")) {
			log.debug("停用报表线程池机制");
			return pjp.proceed();
		}

		String className = pjp.getTarget().getClass().getSimpleName();
		String methodName = pjp.getSignature().getName();
		Class<?> classTarget = pjp.getTarget().getClass();
		Class<?>[] par = ((MethodSignature) pjp.getSignature()).getParameterTypes();
		Method objMethod = classTarget.getMethod(methodName, par);
		final RunInThreadPool ml = objMethod.getAnnotation(RunInThreadPool.class);
		if (ml == null) {
			log.debug("没顶注释，直接返回");
			return pjp.proceed();
		}

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		UserDO loginUser = (UserDO) request.getSession().getAttribute(CRM_constants.AUTH_USER);

		// 形成请求的数据的基因
		String tag = null;
		Object[] args = pjp.getArgs();
		for (Object obj : args) {
			if (obj instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) obj;
				if (bindingResult.hasErrors()) {
					log.debug("验参有错误");
					return new RspBean(new PageUtils()).failure(RespEnum.ERROR_BUSINESS_OPERATE)
							.setMessage(bindingResult.getFieldError().getDefaultMessage());
				}
			}

			if (obj instanceof Bo_Interface) {
				// 对请求参数做基因tag
				tag = MD5Utils.encrypt(className + "_list_" + JSON.toJSONString(obj) + JSON.toJSONString(loginUser));
			}
		}
		if (tag == null) {
			log.debug("无法形成基因字符串");
			return pjp.proceed();
		}

		final String valueKey = tag + "_value";
		PageUtils pageUtils = (PageUtils) redisTemplate.opsForValue().get(valueKey);
		if (pageUtils != null) {
			log.debug("报表结果直接取用缓存");
			return new RspBean(pageUtils).success();
		}

		// 若没有缓存的结果，查看该tag是否在处理队列中
		if (!redisTemplate.hasKey(tag)) {
			final String _tag = tag;
			log.debug("报表处理丢入线程池,报表处理动作加锁");
			redisTemplate.opsForValue().set(_tag, true, 3, TimeUnit.DAYS);

			// 若该tag没有在队列中，那么丢入队列并直接返回等待信息
			ReportThreadPoolManager.getPool(ml.poolName()).execute(new Runnable() {
				@Override
				public void run() {
					Thread t = Thread.currentThread();
					log.info(t.getName() + "线程开始工作");

					// 查询列表数据
					try {
						RspBean result = (RspBean) pjp.proceed();
						if (result.getCode() == RespEnum.SUCCESS.getCode()) {
							// 放置结果缓存
							redisTemplate.opsForValue().set(valueKey, result.getData(), ml.cacheDays(), TimeUnit.DAYS);
						}

						log.debug("报表结果丢入缓存供后续使用");
					} catch (Throwable e) {
						log.error(e.getMessage(), e);
					} finally {
						redisTemplate.delete(_tag);
						log.debug("报表处理解锁");
					}
				}
			});
		} else {
			log.debug("报表处理队列排队/处理中");
		}

		return new RspBean(new PageUtils()).setRespEnum(RespEnum.REPORT_ING);
	}
}