package com.yunda.base.feiniao.report.domain;

import java.io.Serializable;

import com.github.crab2died.annotation.ExcelField;


/**
 * 权限表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-18 17:02:50
 */
public class ReportJurisdictionTableDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//工号
	@ExcelField(title = "工号", order = 1)
	private Integer jobNumber;
	//申请人姓名
	@ExcelField(title = "申请人姓名", order = 2)
	private String applicantName;
	//电话
	@ExcelField(title = "电话", order = 3)
	private String phoneNumber;
	//BQQ
	@ExcelField(title = "BQQ", order = 4)
	private String bqq;
	//权限类型
	@ExcelField(title = "权限类型", order = 5)
	private String permissionType;
	//申请理由
	@ExcelField(title = "申请理由", order = 6)
	private String applicationReasons;
	//审批状态
	@ExcelField(title = "审批状态", order = 7)
	private String approvalState;
	//所属大区(权限)
	@ExcelField(title = "所属大区(权限)", order = 8)
	private String bigarea;
	//所属省份(权限)
	@ExcelField(title = "所属省份(权限)", order = 9)
	private String province;
	//申请时间
	@ExcelField(title = "申请时间", order = 10)
	private String applicantTime;
	//is_asles
	@ExcelField(title = "is_asles", order = 11)
	private String isAsles;

 

	/**
	 * 设置：工号
	 */
	public void setJobNumber(Integer jobNumber) {
		this.jobNumber = jobNumber;
	}
	/**
	 * 获取：工号
	 */
	public Integer getJobNumber() {
		return jobNumber;
	}
	/**
	 * 设置：申请人姓名
	 */
	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}
	/**
	 * 获取：申请人姓名
	 */
	public String getApplicantName() {
		return applicantName;
	}
	/**
	 * 设置：电话
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * 获取：电话
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * 设置：BQQ
	 */
	public void setBqq(String bqq) {
		this.bqq = bqq;
	}
	/**
	 * 获取：BQQ
	 */
	public String getBqq() {
		return bqq;
	}
	/**
	 * 设置：权限类型
	 */
	public void setPermissionType(String permissionType) {
		this.permissionType = permissionType;
	}
	/**
	 * 获取：权限类型
	 */
	public String getPermissionType() {
		return permissionType;
	}
	/**
	 * 设置：申请理由
	 */
	public void setApplicationReasons(String applicationReasons) {
		this.applicationReasons = applicationReasons;
	}
	/**
	 * 获取：申请理由
	 */
	public String getApplicationReasons() {
		return applicationReasons;
	}
	/**
	 * 设置：审批状态
	 */
	public void setApprovalState(String approvalState) {
		this.approvalState = approvalState;
	}
	/**
	 * 获取：审批状态
	 */
	public String getApprovalState() {
		return approvalState;
	}
	/**
	 * 设置：所属大区(权限)
	 */
	public void setBigarea(String bigarea) {
		this.bigarea = bigarea;
	}
	/**
	 * 获取：所属大区(权限)
	 */
	public String getBigarea() {
		return bigarea;
	}
	/**
	 * 设置：所属省份(权限)
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * 获取：所属省份(权限)
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * 设置：申请时间
	 */
	public void setApplicantTime(String applicantTime) {
		this.applicantTime = applicantTime;
	}
	/**
	 * 获取：申请时间
	 */
	public String getApplicantTime() {
		return applicantTime;
	}
	/**
	 * 设置：is_asles
	 */
	public void setIsAsles(String isAsles) {
		this.isAsles = isAsles;
	}
	/**
	 * 获取：is_asles
	 */
	public String getIsAsles() {
		return isAsles;
	}
}
