/*
package com.yunda.base.common.aspect;

import com.yunda.base.common.enums.ResourceSafeEnum;
import com.yunda.base.common.enums.RespEnum;
import com.yunda.base.feiniao.report.core.CRM_constants;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.LoginUserDO;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.service.LoginUserService;
import com.yunda.base.system.service.ResourceSafeConfigService;
import com.yunda.base.system.service.SessionService;
import com.yunda.base.system.service.UserService;
import com.yunda.base.system.vo.RspBean;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class ResourceSafeMonitor {
    Logger log = LoggerFactory.getLogger(ResourceSafeMonitor.class);
    @Autowired
    private ResourceSafeConfigService resourceSafeConfigService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    LoginUserService loginUserService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserService userService;

    @Pointcut("execution(public * com.yunda.base.feiniao.*.controller.*.*(..))")
    public void valid() {
    }

    @Around("valid()")
    public Object latencyService(ProceedingJoinPoint pjp) throws Throwable {
        Integer resourcelevel = null;
        Integer userlevel = null;
        List<Object> info = new ArrayList<>();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String url = request.getRequestURI();
        if (StringUtils.isNotBlank(url)) {
            try {
                String username = sessionService.findSession(request.getSession().getId()).getUsername();
                UserDO userdo = (UserDO) request.getSession().getAttribute(CRM_constants.AUTH_USER);
                String safeLevel = userdo.getSafeLevel();// 当前登录的用户的安全等级
                Long userNumber = userService.getUserId(username);// 获取到该用户在用户表中的序号
                if (userNumber != null) {
                    List<String> userRole_number = userService.getUserRole(userNumber);// 通过用户编号查询出用户的角色
                    List<String> user_role = Arrays.asList(SysConfig.USER_ROLE.split(","));
                    boolean flag = false;
                    // 只要角色中有一个需要检测的,就需要检测
                    for (String userRole : userRole_number) {
                        if (user_role.contains(userRole)) {
                            flag = true;
                        }
                    }
                    if (flag) {
                        if (StringUtils.isNotBlank(username)) {
                            // 去匹配安全配置表,和用户持有的安全等级做对比
                            String level = resourceSafeConfigService.getSafeLevel(url);
                            if (StringUtils.isBlank(level)) {
                                level = ResourceSafeEnum.TOGE_AUTH.getNum();// 默认是0
                            }
                            if (StringUtils.isBlank(safeLevel)) {
                                safeLevel = ResourceSafeEnum.TOGE_AUTH.getNum();
                            }
                            log.info("用户名:[{}],用户等级是:[{}],资源等级:[{}],角色编号:[{}]", username, safeLevel, level, userNumber);

                            if (!StringUtils.equals(level, safeLevel) && Integer.valueOf(level) > Integer.valueOf(safeLevel)) {
                                // 需要进行安全校验
                                resourcelevel = Integer.valueOf(level);
                                userlevel = Integer.valueOf(safeLevel);
                                // 需要进行多少级校验
                                while (resourcelevel > userlevel) {
                                    userlevel++;
                                    info.add(userlevel);
                                }
                                info.add(username);
                                // 如果查找基础表有就这个,如果没有就返回基础表没有
                                LoginUserDO loginUserDO = loginUserService.getUserByName(username);
                                if (loginUserDO == null || loginUserDO.getStatus() == 0) {
                                    log.info("用户名:[{}],缺少基础数据", username);
                                    return new RspBean<>().failureWithData(RespEnum.ERROR_NOT_EXITS_LOGIN_USER.getCode(), "缺少基础数据");
                                }

                                // 记忆用户希望提升到的目标等级
                                stringRedisTemplate.opsForValue().set(request.getSession().getId() + "_targetlevel", String.valueOf(resourcelevel));
                                return new RspBean<>().failureWithData(RespEnum.NOTSAFE.getCode(), info);
                            }
                        }
                    }
                }
            } catch (Throwable throwable) {
                log.error("安全资源切面出现异常;[{}]", throwable.toString());
            }
        }
        return pjp.proceed();
    }
}
*/
