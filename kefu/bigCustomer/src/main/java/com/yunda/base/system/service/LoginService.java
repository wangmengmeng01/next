package com.yunda.base.system.service;

import java.util.List;

import org.dom4j.DocumentException;

import com.yunda.base.system.vo.RspBean;

public interface LoginService {
	//安全登录校验短信
	RspBean smsLogin(String phone) throws DocumentException;

	//查询当前用户的角色Id
	List<String> getRoleIds(String userId);

	String sendMsm(String message, Object phone_no);
}
