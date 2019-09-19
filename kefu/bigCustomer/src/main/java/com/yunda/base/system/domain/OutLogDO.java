package com.yunda.base.system.domain;

import java.io.Serializable;

import com.github.crab2died.annotation.ExcelField;


/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-01-24170431
 */
public class OutLogDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//sessionId
	@ExcelField(title = "sessionId", order = 1)
	private String sessionId;
	//退出登录的时间
	@ExcelField(title = "退出登录的时间", order = 2)
	private String outTime;

 

	/**
	 * 设置：sessionId
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	/**
	 * 获取：sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}
	/**
	 * 设置：退出登录的时间
	 */
	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}
	/**
	 * 获取：退出登录的时间
	 */
	public String getOutTime() {
		return outTime;
	}

	@Override
	public String toString() {
		return "OutLogDO{" +
				"sessionId='" + sessionId + '\'' +
				", outTime='" + outTime + '\'' +
				'}';
	}
}
