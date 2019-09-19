package com.yunda.base.system.service;

import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.domain.UserOnline;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public interface SessionService {
	List<UserOnline> list();

	List<UserDO> listOnlineUser();

	Collection<Session> sessionList();
	
	boolean forceLogout(String sessionId);
	
	UserOnline findSession(String sessionId);
}
