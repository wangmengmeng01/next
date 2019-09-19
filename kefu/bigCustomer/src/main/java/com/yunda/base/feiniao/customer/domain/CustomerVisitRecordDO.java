package com.yunda.base.feiniao.customer.domain;

import java.io.Serializable;
import java.util.Date;

import com.github.crab2died.annotation.ExcelField;


/**
 * 客户拜访记录表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-10-31144058
 */
public class CustomerVisitRecordDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//数据记录主键ID
	@ExcelField(title = "数据记录主键ID", order = 1)
	private Integer recordId;
	//拜访id
	@ExcelField(title = "拜访id", order = 2)
	private Integer visitId;
	//业务员姓名
	@ExcelField(title = "业务员姓名", order = 3)
	private String createrName;
	//创建人所属网点
	@ExcelField(title = "创建人所属网点", order = 4)
	private String belongBranch;
	//拜访时间
	@ExcelField(title = "拜访时间", order = 5)
	private Date visitTime;
	//客户编码
	@ExcelField(title = "客户编码", order = 6)
	private String customerId;
	//客户名称
	@ExcelField(title = "客户名称", order = 7)
	private String customerName;
	//客户联系人
	@ExcelField(title = "客户联系人", order = 8)
	private String customerLinkman;
	//客户联系方式
	@ExcelField(title = "客户联系方式", order = 9)
	private String customerPhonenum;
	//客户所在地区
	@ExcelField(title = "客户所在地区", order = 10)
	private String customerArea;
	//客户所在省
	@ExcelField(title = "客户所在省", order = 11)
	private String customerProvince;
	//客户所在城市
	@ExcelField(title = "客户所在城市", order = 12)
	private String customerCity;
	//客户详细地址
	@ExcelField(title = "客户详细地址", order = 13)
	private String customerDetailaddress;
	//拜访目的
	@ExcelField(title = "拜访目的", order = 14)
	private String visitPerpose;
	//客户反馈
	@ExcelField(title = "客户反馈", order = 15)
	private String customerFeedback;
	//洽谈结果
	@ExcelField(title = "洽谈结果", order = 16)
	private String visitResult;
	//创建时间
	@ExcelField(title = "创建时间", order = 17)
	private Date creatTime;

 

	/**
	 * 设置：数据记录主键ID
	 */
	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}
	/**
	 * 获取：数据记录主键ID
	 */
	public Integer getRecordId() {
		return recordId;
	}
	/**
	 * 设置：拜访id
	 */
	public void setVisitId(Integer visitId) {
		this.visitId = visitId;
	}
	/**
	 * 获取：拜访id
	 */
	public Integer getVisitId() {
		return visitId;
	}
	/**
	 * 设置：业务员姓名
	 */
	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}
	/**
	 * 获取：业务员姓名
	 */
	public String getCreaterName() {
		return createrName;
	}
	/**
	 * 设置：创建人所属网点
	 */
	public void setBelongBranch(String belongBranch) {
		this.belongBranch = belongBranch;
	}
	/**
	 * 获取：创建人所属网点
	 */
	public String getBelongBranch() {
		return belongBranch;
	}
	/**
	 * 设置：拜访时间
	 */
	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}
	/**
	 * 获取：拜访时间
	 */
	public Date getVisitTime() {
		return visitTime;
	}
	/**
	 * 设置：客户编码
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	/**
	 * 获取：客户编码
	 */
	public String getCustomerId() {
		return customerId;
	}
	/**
	 * 设置：客户名称
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * 获取：客户名称
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * 设置：客户联系人
	 */
	public void setCustomerLinkman(String customerLinkman) {
		this.customerLinkman = customerLinkman;
	}
	/**
	 * 获取：客户联系人
	 */
	public String getCustomerLinkman() {
		return customerLinkman;
	}
	/**
	 * 设置：客户联系方式
	 */
	public void setCustomerPhonenum(String customerPhonenum) {
		this.customerPhonenum = customerPhonenum;
	}
	/**
	 * 获取：客户联系方式
	 */
	public String getCustomerPhonenum() {
		return customerPhonenum;
	}
	/**
	 * 设置：客户所在地区
	 */
	public void setCustomerArea(String customerArea) {
		this.customerArea = customerArea;
	}
	/**
	 * 获取：客户所在地区
	 */
	public String getCustomerArea() {
		return customerArea;
	}
	/**
	 * 设置：客户所在省
	 */
	public void setCustomerProvince(String customerProvince) {
		this.customerProvince = customerProvince;
	}
	/**
	 * 获取：客户所在省
	 */
	public String getCustomerProvince() {
		return customerProvince;
	}
	/**
	 * 设置：客户所在城市
	 */
	public void setCustomerCity(String customerCity) {
		this.customerCity = customerCity;
	}
	/**
	 * 获取：客户所在城市
	 */
	public String getCustomerCity() {
		return customerCity;
	}
	/**
	 * 设置：客户详细地址
	 */
	public void setCustomerDetailaddress(String customerDetailaddress) {
		this.customerDetailaddress = customerDetailaddress;
	}
	/**
	 * 获取：客户详细地址
	 */
	public String getCustomerDetailaddress() {
		return customerDetailaddress;
	}
	/**
	 * 设置：拜访目的
	 */
	public void setVisitPerpose(String visitPerpose) {
		this.visitPerpose = visitPerpose;
	}
	/**
	 * 获取：拜访目的
	 */
	public String getVisitPerpose() {
		return visitPerpose;
	}
	/**
	 * 设置：客户反馈
	 */
	public void setCustomerFeedback(String customerFeedback) {
		this.customerFeedback = customerFeedback;
	}
	/**
	 * 获取：客户反馈
	 */
	public String getCustomerFeedback() {
		return customerFeedback;
	}
	/**
	 * 设置：洽谈结果
	 */
	public void setVisitResult(String visitResult) {
		this.visitResult = visitResult;
	}
	/**
	 * 获取：洽谈结果
	 */
	public String getVisitResult() {
		return visitResult;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreatTime() {
		return creatTime;
	}
}
