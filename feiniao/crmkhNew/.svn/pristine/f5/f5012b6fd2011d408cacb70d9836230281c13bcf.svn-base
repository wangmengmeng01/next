package com.yunda.base.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.ValidateUser;
import com.yunda.base.system.service.ValidateUserInfo;
import okhttp3.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class ValidateUserInfoImpl implements ValidateUserInfo {
	Logger log = Logger.getLogger(getClass());
	/*@Value("${validate.url.urlNameStringLogin}")
	private String urlNameStringLogin;
	@Value("${validate.url.urlNameStringRole}")
	private String urlNameStringRole;
	@Value("${validate.url.authId}")
	private String authId;*/
	@Override
	public ValidateUser userLogin(String userId, String password) {
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		RequestBody formBody = new FormBody.Builder().add("userId", userId).add("authId", SysConfig.authId).add("password", password).build();
		Request request = new Request.Builder().url(SysConfig.urlNameStringLogin).post(formBody).addHeader("cache-control", "no-cache").addHeader("content-type", "application/x-www-form-urlencoded").build();
		Response response = null;
		String resultStr = "";
		try {
			response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				resultStr = response.body().string();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
        ValidateUser validateUser = JSON.parseObject(resultStr, new TypeReference<ValidateUser>(){});
		return validateUser;
	}

	@Override
	public Map<String, String> queryUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getRole() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValidateUser userRole(String userId) {
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		RequestBody formBody = new FormBody.Builder().add("userId", userId).add("authId", SysConfig.authId).build();
		Request request = new Request.Builder().url(SysConfig.urlNameStringRole).post(formBody).addHeader("cache-control", "no-cache").addHeader("content-type", "application/x-www-form-urlencoded").build();
		Response response = null;
		String resultStr = "";
		try {
			response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				resultStr = response.body().string();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		ValidateUser validateUser = JSON.parseObject(resultStr, new TypeReference<ValidateUser>(){});
		return validateUser;
	}

}
