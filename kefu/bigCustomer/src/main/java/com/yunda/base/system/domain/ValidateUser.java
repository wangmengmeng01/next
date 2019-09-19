package com.yunda.base.system.domain;

public class ValidateUser {
	private String retmsg;
	private String retcode;
	private boolean result;
	private String userRole;
	private UserDO userData;

	public String getRetmsg() {
		return retmsg;
	}

	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}

	public String getRetcode() {
		return retcode;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public UserDO getUserData() {
		return userData;
	}

	public void setUserData(UserDO userData) {
		this.userData = userData;
	}

}
