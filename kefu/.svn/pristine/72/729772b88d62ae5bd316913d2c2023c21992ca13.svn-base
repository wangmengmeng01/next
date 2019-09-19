package com.yunda.base.system.service;

import com.yunda.base.system.domain.ValidateUser;

import java.util.Map;

public interface ValidateUserInfo {
	// 用户登录   本系统的username即统一授权的userId
    ValidateUser userLogin(String userId, String password);

	// 查询用户并获取角色
    Map<String, String> queryUser();

	// 查询用户并获取角色
    Map<String, String> getRole();

	// 查询用户并获取角色
    ValidateUser userRole(String userId);
}
