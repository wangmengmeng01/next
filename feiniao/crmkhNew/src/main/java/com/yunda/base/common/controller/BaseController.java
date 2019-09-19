package com.yunda.base.common.controller;

import com.yunda.base.common.enums.RespEnum;
import com.yunda.base.feiniao.report.core.CRM_constants;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.vo.RspBean;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

@Controller
public class BaseController {
	@Autowired
	SimpMessagingTemplate template;
	Logger log = Logger.getLogger(getClass());

	// 通过websocket通知信息到前端
	public void notifyPage(String info) {
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();

		UserDO userDo = (UserDO) session.getAttribute(CRM_constants.AUTH_USER);
		if (StringUtils.isNotBlank(userDo.getUsername())) {
			template.convertAndSendToUser(userDo.getUsername(), "/queue/notifications", info);
		}
	}

	// 封装返回结果
	protected <T> RspBean<T> success(T data) {
		return new RspBean<T>(data).success();
	}

	// 封装返回结果
	protected <T> RspBean<T> failure(int code) {
		return new RspBean<T>().failure(code);
	}

	// 封装返回结果
	protected <T> RspBean<T> failure(T data) {
		return new RspBean<T>(data).failure(RespEnum.ERROR_BUSINESS_OPERATE);
	}

	// 封装返回结果
	protected <T> RspBean<T> failure(String message) {
		return new RspBean<T>(RespEnum.ERROR_BUSINESS_OPERATE.getCode(), message);
	}

	// 封装返回结果
	protected <T> RspBean<T> failure(RespEnum result) {
		return new RspBean<T>().failure(RespEnum.ERROR_BUSINESS_OPERATE);
	}

	public UserDO getUser(HttpServletRequest request) {
		UserDO user = (UserDO) request.getSession().getAttribute(CRM_constants.AUTH_USER);

		return user;
	}

	public Long getUserId(HttpServletRequest request) {
		return getUser(request).getUserId();
	}

	public String getUsername(HttpServletRequest request) {
		return getUser(request).getUsername();
	}

	public String getName(HttpServletRequest request) {
		return getUser(request).getName();
	}
}
