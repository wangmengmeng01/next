package com.yunda.base.system.aspect;

import com.yunda.base.common.utils.HttpContextUtils;
import com.yunda.base.common.utils.ShiroUtils;
import com.yunda.base.system.annotation.SensitiveOperateLog;
import com.yunda.base.system.domain.SensitiveOperateLogDO;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.service.SensitiveOperateLogService;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.NetUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
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
import java.util.*;
@Aspect
@Component
public class SensitiveOperateLogAspect {

    @Autowired
    private SensitiveOperateLogService sensitiveOperateLogService;

    @Pointcut("@annotation(com.yunda.base.system.annotation.SensitiveOperateLog)")
    public void SensitiveOperatePointCut() {
    }

    @Around("SensitiveOperatePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 执行方法
        Object result = point.proceed();
        saveLog(point);
        return result;
    }

    @Async
    void saveLog(ProceedingJoinPoint joinPoint) throws InterruptedException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SensitiveOperateLogDO sensitiveLog = new SensitiveOperateLogDO();
        SensitiveOperateLog sensitiveOperateLog = method.getAnnotation(SensitiveOperateLog.class);
        if (sensitiveOperateLog != null) {
            // 注解上的描述(访问页面)
            sensitiveLog.setOperation(sensitiveOperateLog.value());
            // 注解上的描述(操作类型)
            sensitiveLog.setOperationTyp(sensitiveOperateLog.type());
        }
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sensitiveLog.setMethod(className + "." + methodName + "()");
        // 请求的参数
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> map = new HashMap<String,String>();
        String params;
        Set<Map.Entry<String, String[]>> entries = parameterMap.entrySet();
        Iterator<Map.Entry<String, String[]>> iterator = entries.iterator();
        while(iterator.hasNext()){
            Map.Entry<String, String[]> next = iterator.next();
            String k = next.getKey();
            String[] v = next.getValue();
            if(null!=v[0]&&!v[0].equals("")){
                map.put(k,v[0]);
            }

        }
            params = map.toString();
        if (params.length() > 5000) {
            params = params.substring(0, 4999);
        }
        if (map.isEmpty()){
            params=null;
        }
        sensitiveLog.setParams(params);
        //设置sessionId
        Session session = SecurityUtils.getSubject().getSession();
        String sessionId = session.getId().toString();
        sensitiveLog.setSessionId(sessionId);
        // 设置IP地址
        sensitiveLog.setIp(NetUtils.getIpAddr(request));
        // 用户名
        UserDO currUser = ShiroUtils.getUser();
        if (null == currUser) {
            if (null != sensitiveLog.getParams()) {
                sensitiveLog.setUserId(-1L);
                sensitiveLog.setUserName(sensitiveLog.getParams());
            } else {
                sensitiveLog.setUserId(-1L);
                sensitiveLog.setUserName("获取用户信息为空");
            }
        } else {
            sensitiveLog.setUserId(ShiroUtils.getUserId());
            sensitiveLog.setUserName(ShiroUtils.getUser().getUsername());
        }
        // 系统当前时间
        String date = DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
        sensitiveLog.setCreateTime(date);
        // 保存系统日志
        sensitiveOperateLogService.save(sensitiveLog);
    }
}
