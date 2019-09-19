package com.yunda.base.feiniao.report.domain;

import java.io.Serializable;

import com.github.crab2died.annotation.ExcelField;



/**
 * 客户报表订单统计-总表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-11 14:54:40
 */
public class ExportReportWarningdataDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//客户名称
	@ExcelField(title = "客户名称", order = 1)
	private String customerName;
	//a_customer_sum
	/*@ExcelField(title = "A类0-50", order = 2)
	private double aCustomerSum;*/
	@ExcelField(title = "B类50-200", order = 2)
	private double bCustomerSum;
	//c_customer_sum
	@ExcelField(title = "C类200-1000", order = 3)
	private double cCustomerSum;
	//d_customer_sum
	@ExcelField(title = "D类1000-2000", order = 4)
	private double dCustomerSum;
	//e_customer_sum
	@ExcelField(title = "E类2000-3000", order = 5)
	private double eCustomerSum;
	//f_customer_sum
	@ExcelField(title = "F类3000-5000", order = 6)
	private double fCustomerSum;
	//g_customer_sum
	@ExcelField(title = "G类5000+", order = 7)
	private double gCustomerSum;
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/*public double getaCustomerSum() {
		return aCustomerSum;
	}
	public void setaCustomerSum(double aCustomerSum) {
		this.aCustomerSum = aCustomerSum;
	}*/
	public double getbCustomerSum() {
		return bCustomerSum;
	}
	public void setbCustomerSum(double bCustomerSum) {
		this.bCustomerSum = bCustomerSum;
	}
	public double getcCustomerSum() {
		return cCustomerSum;
	}
	public void setcCustomerSum(double cCustomerSum) {
		this.cCustomerSum = cCustomerSum;
	}
	public double getdCustomerSum() {
		return dCustomerSum;
	}
	public void setdCustomerSum(double dCustomerSum) {
		this.dCustomerSum = dCustomerSum;
	}
	public double geteCustomerSum() {
		return eCustomerSum;
	}
	public void seteCustomerSum(double eCustomerSum) {
		this.eCustomerSum = eCustomerSum;
	}
	public double getfCustomerSum() {
		return fCustomerSum;
	}
	public void setfCustomerSum(double fCustomerSum) {
		this.fCustomerSum = fCustomerSum;
	}
	public double getgCustomerSum() {
		return gCustomerSum;
	}
	public void setgCustomerSum(double gCustomerSum) {
		this.gCustomerSum = gCustomerSum;
	}
	
	
	
	
	
	
}