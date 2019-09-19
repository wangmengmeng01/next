package com.yunda.base.feiniao.warning.domain;

import java.io.Serializable;
import java.util.Date;
import com.github.crab2died.annotation.ExcelField;


/**
 * 大客户预警短信--网点手机号信息表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-17132313
 */
public class WarningBranchMobileDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	@ExcelField(title = "主键", order = 1)
	private Integer orgid;
	//用户姓名
	@ExcelField(title = "用户姓名", order = 2)
	private String userName;
	//网点编码
	@ExcelField(title = "网点编码", order = 3)
	private String branchCode;
	//网点名称
	@ExcelField(title = "网点名称", order = 4)
	private String branchName;
	//用户电话
	@ExcelField(title = "用户电话", order = 5)
	private String mobile;
	//更新时间
	@ExcelField(title = "更新时间", order = 6)
	private Date updateTime;
	//使用状态
	//@ExcelField(title = "使用状态")
	private String status;
	@ExcelField(title = "使用状态", order = 7)
	private String showStatus;
 

	/**
	 * 设置：主键
	 */
	public void setOrgid(Integer orgid) {
		this.orgid = orgid;
	}
	/**
	 * 获取：主键
	 */
	public Integer getOrgid() {
		return orgid;
	}
	/**
	 * 设置：用户姓名
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 获取：用户姓名
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * 设置：网点编码
	 */
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	/**
	 * 获取：网点编码
	 */
	public String getBranchCode() {
		return branchCode;
	}
	/**
	 * 设置：网点名称
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	/**
	 * 获取：网点名称
	 */
	public String getBranchName() {
		return branchName;
	}
	/**
	 * 设置：用户电话
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 获取：用户电话
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * 设置：更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：使用状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 获取：使用状态
	 */
	public String getStatus() {
		return status;
	}
	public String getShowStatus() {
		if("running".equals(status)){
			showStatus = "使用中";
   	 	}else if("stop".equals(status)){
   	 		showStatus = "已停用";
   	 	}else{
   	 		showStatus = "";
   	 	}
		return showStatus;
	}
	public void setShowStatus(String showStatus) {
		this.showStatus = showStatus;
	}
	
}
