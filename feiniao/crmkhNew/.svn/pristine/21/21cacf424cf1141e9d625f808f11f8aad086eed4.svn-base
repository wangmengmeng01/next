package com.yunda.base.system.shiro;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.JsonPath;
import com.yunda.base.common.config.ApplicationContextRegister;
import com.yunda.base.common.utils.ShiroUtils;
import com.yunda.base.system.dao.UserDao;
import com.yunda.base.system.domain.OutLogDO;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.service.MenuService;
import com.yunda.base.system.service.OutLogService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.yunda.ydmbspringbootstarter.common.utils.*;
import java.util.*;

public class UserRealm extends AuthorizingRealm {
	@Autowired
	OutLogService outLogService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		Long userId = ShiroUtils.getUserId();
		MenuService menuService = ApplicationContextRegister.getBean(MenuService.class);
		Set<String> perms = menuService.listPerms(userId);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(perms);
		return info;
	}

	/*@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
		Map<String, Object> map = new HashMap<>(16);
		map.put("username", username);
		String password = new String((char[]) token.getCredentials());

		UserDao userMapper = ApplicationContextRegister.getBean(UserDao.class);
		// 查询用户信息
		List<UserDO> list = userMapper.list(map);

		// 账号不存在
		if (list.isEmpty()) {
			throw new UnknownAccountException("账号不存在");
		}

		// 密码错误
		if (!password.equals(list.get(0).getPassword())) {
			throw new IncorrectCredentialsException("账号或密码不正确");
		}

		// 账号锁定
		if (list.get(0).getStatus() == 0) {
			throw new LockedAccountException("账号已被锁定,请联系管理员");
		}
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(list.get(0), password, getName());
		return info;
	}*/
	@Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String userName = (String)authenticationToken.getPrincipal();
 
        //处理session
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager)securityManager.getSessionManager();
        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();//获取当前已登录的用户session列表
        for(Session session:sessions){
        String jsonString = JSON.toJSONString(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY));
        
        if(!StringUtils.isEmpty(jsonString)&&!StringUtils.equals("null", jsonString)){
        	String username = JsonPath.read(jsonString, "$.primaryPrincipal.username");
        	//清除该用户以前登录时保存的session
        	if(userName.equals(String.valueOf(username))) {
				saveLog();
				sessionManager.getSessionDAO().delete(session);
        	}
        }
        }
 
        String username = (String) authenticationToken.getPrincipal();
		Map<String, Object> map = new HashMap<>(16);
		map.put("username", username);
		String password = new String((char[]) authenticationToken.getCredentials());

		UserDao userMapper = ApplicationContextRegister.getBean(UserDao.class);
		// 查询用户信息
		List<UserDO> list = userMapper.list(map);

		// 账号不存在
		if (list.isEmpty()) {
			throw new UnknownAccountException("账号不存在");
		}

		// 密码错误
		if (!password.equals(list.get(0).getPassword())) {
			throw new IncorrectCredentialsException("账号或密码不正确");
		}

		// 账号锁定
		if (list.get(0).getStatus() == 0) {
			throw new LockedAccountException("账号已被锁定,请联系管理员");
		}
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(list.get(0), password, getName());
		return info;
    }
   //日志记录
   void saveLog() {
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
