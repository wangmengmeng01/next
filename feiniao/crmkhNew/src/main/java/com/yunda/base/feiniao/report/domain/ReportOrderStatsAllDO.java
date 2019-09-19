package com.yunda.base.feiniao.report.domain;

import com.github.crab2died.annotation.ExcelField;

import java.io.Serializable;
import java.util.Date;


/**
 * 客户报表订单统计-总表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-18 16:23:26
 */
public class ReportOrderStatsAllDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//customer_id
	@ExcelField(title = "customer_id", order = 1)
	private String customerId;
	//客户名称
	@ExcelField(title = "客户名称", order = 2)
	private String customerName;
	//所属1级网点编码
	@ExcelField(title = "所属1级网点编码", order = 3)
	private Integer branchCode;
	//客户来源
	@ExcelField(title = "客户来源", order = 4)
	private String customerSourceType;
	//日订单量
	@ExcelField(title = "日订单量", order = 5)
	private String dailyOrderNum;
	//创建时间
	@ExcelField(title = "创建时间", order = 6)
	private Date creDate;
	//查询日期
	@ExcelField(title = "查询日期", order = 7)
	private Date quDate;
	//客户新增日期
	@ExcelField(title = "客户新增日期", order = 8)
	private Date customerAddtime;
	//gs
	@ExcelField(title = "gs", order = 9)
	private Integer gs;

 

	/**
	 * 设置：customer_id
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	/**
	 * 获取：customer_id
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
	 * 设置：所属1级网点编码
	 */
	public void setBranchCode(Integer branchCode) {
		this.branchCode = branchCode;
	}
	/**
	 * 获取：所属1级网点编码
	 */
	public Integer getBranchCode() {
		return branchCode;
	}
	/**
	 * 设置：客户来源
	 */
	public void setCustomerSourceType(String customerSourceType) {
		this.customerSourceType = customerSourceType;
	}
	/**
	 * 获取：客户来源
	 */
	public String getCustomerSourceType() {
		return customerSourceType;
	}
	/**
	 * 设置：日订单量
	 */
	public void setDailyOrderNum(String dailyOrderNum) {
		this.dailyOrderNum = dailyOrderNum;
	}
	/**
	 * 获取：日订单量
	 */
	public String getDailyOrderNum() {
		return dailyOrderNum;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreDate(Date creDate) {
		this.creDate = creDate;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreDate() {
		return creDate;
	}
	/**
	 * 设置：查询日期
	 */
	public void setQuDate(Date quDate) {
		this.quDate = quDate;
	}
	/**
	 * 获取：查询日期
	 */
	public Date getQuDate() {
		return quDate;
	}
	/**
	 * 设置：客户新增日期
	 */
	public void setCustomerAddtime(Date customerAddtime) {
		this.customerAddtime = customerAddtime;
	}
	/**
	 * 获取：客户新增日期
	 */
	public Date getCustomerAddtime() {
		return customerAddtime;
	}
	/**
	 * 设置：gs
	 */
	public void setGs(Integer gs) {
		this.gs = gs;
	}
	/**
	 * 获取：gs
	 */
	public Integer getGs() {
		return gs;
	}
}
