package com.yunda.base.system.domain;

import com.github.crab2died.annotation.ExcelField;

import java.io.Serializable;


/**
 * 敏感操作日志
 * 
 * @author xh
 * @email zhanghan813@163.com
 * @date 2018-12-24 16:49:46
 */
public class SensitiveOperateLogDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	@ExcelField(title = "序号", order = 1)
	private Long id;
	//用户id
	private Long userId;
	//用户账号
	@ExcelField(title = "登录人账号", order = 2)
	private String userName;
	//姓名
	@ExcelField(title = "登录人姓名", order = 3)
	private String name;
	//用户操作
	@ExcelField(title = "访问页面", order = 4)
	private String operation;
	//用户操作
	@ExcelField(title = "操作类型", order = 5)
	private String operationTyp;
	//请求方法
	private String method;
    //操作时间
    @ExcelField(title = "操作时间", order = 6)
    private String createTime;
	//请求参数
	@ExcelField(title = "查询条件", order = 7)
	private String params;
	//IP地址
	private String ip;
	//sessionId
	private String sessionId;


	/**
	 * 设置：id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：用户id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户id
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：用户名
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 获取：用户名
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * 设置：用户操作
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}
	/**
	 * 获取：用户操作
	 */
	public String getOperation() {
		return operation;
	}
	/**
	 * 设置：请求方法
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * 获取：请求方法
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * 设置：请求参数
	 */
	public void setParams(String params) {
		this.params = params;
	}
	/**
	 * 获取：请求参数
	 */
	public String getParams() {
		return params;
	}
	/**
	 * 设置：IP地址
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * 获取：IP地址
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * 设置sessionId
	 * @param sessionId
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	/**
	 * 获取sessionId
	 * @return
	 */
	public String getSessionId() {
		return sessionId;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public String getCreateTime() {
		return createTime;
	}

	public String getOperationTyp() {
		return operationTyp;
	}

	public void setOperationTyp(String operationTyp) {
		this.operationTyp = operationTyp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
