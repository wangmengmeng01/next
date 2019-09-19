package com.yunda.base.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.crab2died.annotation.ExcelField;

public class LoginLogDO {
	@ExcelField(title = "序号", order = 1)
	private Long id;

	/**
	 * 用户登录账号
	 */
	@ExcelField(title = "登录人账号", order = 2)
	private String userId;

	/**
	 * 用户真实姓名
	 */
	@ExcelField(title = "登录人姓名", order = 3)
	private String name;

	/**
	 * 角色
	 */
	@ExcelField(title = "角色", order = 4)
	private String roleName;

	/**
	 * 所属机构
	 */
	@ExcelField(title = "所属机构", order = 5)
	private String orgName;

	/**
	 * 身份证号
	 */
	@ExcelField(title = "身份证号", order = 6)
	private String idcdNo;

	/**
	 * 手机号
	 */
	@ExcelField(title = "手机号", order = 7)
	private String mobile;

	/**
	 * 登陆时间
	 */
	@ExcelField(title = "登陆时间", order = 8)
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private String createTime;

	/**
	 * 退出时间
	 */
	@ExcelField(title = "退出时间", order = 9)
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private String outTime;

	/**
	 * 登陆时长
	 */
	@ExcelField(title = "登陆时长", order = 10)
	private String time;

	/**
	 * 是否报警
	 */
	@ExcelField(title = "是否报警", order = 11)
	private String police;

	/**
	 * 报警类型
	 */
	@ExcelField(title = "报警类型", order = 12)
	private String policeTyp;

	/**
	 * sessionId
	 */
	private String sessionId;

	/**
	 * 用户登录操作
	 */
	private String operation;

	/**
	 * 用户访问ip
	 */
	@ExcelField(title = "IP地址", order = 13)
	private String userIp;

	/**
	 * 有线mac地址
	 */
	@ExcelField(title = "mac有线地址", order = 14)
	private String macAdress;

	/**
	 * 有线mac无线地址
	 */
	@ExcelField(title = "mac无线地址", order = 15)
	private String wMacAdress;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation == null ? null : operation.trim();
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp == null ? null : userIp.trim();
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getIdcdNo() {
		return idcdNo;
	}

	public void setIdcdNo(String idcdNo) {
		this.idcdNo = idcdNo;
	}

	public String getMacAdress() {
		return macAdress;
	}

	public void setMacAdress(String macAdress) {
		this.macAdress = macAdress;
	}

	public String getwMacAdress() {
		return wMacAdress;
	}

	public void setwMacAdress(String wMacAdress) {
		this.wMacAdress = wMacAdress;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPolice() {
		return police;
	}

	public void setPolice(String police) {
		this.police = police;
	}

	public String getPoliceTyp() {
		return policeTyp;
	}

	public void setPoliceTyp(String policeTyp) {
		this.policeTyp = policeTyp;
	}

	@Override
	public String toString() {
		return "LogDO{" +
				"id=" + id + '\'' +
				", userId=" + userId + '\'' +
				", roleName=" + roleName + '\'' +
				", orgName=" + orgName + '\'' +
				", idcdNo=" + idcdNo + '\'' +
				", name=" + name + '\'' +
				", mobile=" + mobile + '\'' +
				", operation='" + operation + '\'' +
				", userIp='" + userIp + '\'' +
				", macAdress='" + macAdress + '\'' +
				", wMacAdress='" + wMacAdress + '\'' +
				", outTime='" + outTime + '\'' +
				", time='" + time + '\'' +
				", sessionId='" + sessionId + '\'' +
				", createTime=" + createTime +'\'' +
				", police=" + police +'\'' +
				", policeTyp=" + policeTyp +
				'}';
	}

}