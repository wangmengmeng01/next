package com.yunda.base.system.domain;

import com.github.crab2died.annotation.ExcelField;

import java.io.Serializable;


/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-02-21151731
 */
public class AlarmDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//sessionId
	@ExcelField(title = "sessionId", order = 1)
	private String sessionId;
	//账号
	@ExcelField(title = "账号", order = 2)
	private String userName;
	//报警类型
	@ExcelField(title = "报警类型", order = 3)
	private String alarmType;
	//报警时间
	@ExcelField(title = "报警时间", order = 4)
	private String time;

	private String imgAddress;
 

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
	 * 设置：账号
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 获取：账号
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * 设置：报警类型
	 */
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	/**
	 * 获取：报警类型
	 */
	public String getAlarmType() {
		return alarmType;
	}
	/**
	 * 设置：报警时间
	 */
	public void setTime(String time) {
		this.time = time;
	}
	/**
	 * 获取：报警时间
	 */
	public String getTime() {
		return time;
	}

	public String getImgAddress() {
		return imgAddress;
	}

	public void setImgAddress(String imgAddress) {
		this.imgAddress = imgAddress;
	}

	@Override
	public String toString() {
		return "AlarmDO{" +
				"sessionId='" + sessionId + '\'' +
				", userName='" + userName + '\'' +
				", alarmType='" + alarmType + '\'' +
				", time='" + time + '\'' +
				'}';
	}
}
