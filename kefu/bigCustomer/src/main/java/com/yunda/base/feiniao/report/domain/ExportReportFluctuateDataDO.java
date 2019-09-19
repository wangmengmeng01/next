package com.yunda.base.feiniao.report.domain;

import java.io.Serializable;

import com.github.crab2died.annotation.ExcelField;

/**
 * 波动表导出实体类
 * @author admin
 *
 */
public class ExportReportFluctuateDataDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ExcelField(title = "基础信息", order = 1)
	private String customerName;
	
	@ExcelField(title = "差值", order = 2)
	private double czCustomerSum;
	
	@ExcelField(title = "流失数", order = 3)
	private double lostCustomerSum;
	
	@ExcelField(title = "新增数", order = 4)
	private double addCustomerSum;
	
	@ExcelField(title = "差值", order = 5)
	private double czBCustomerSum;
	
	@ExcelField(title = "流失数", order = 6)
	private double blostCustomerSum;
	
	@ExcelField(title = "新增数", order = 7)
	private double baddCustomerSum;
	
	@ExcelField(title = "差值", order = 8)
	private double czCCustomerSum;
	
	@ExcelField(title = "流失数", order = 9)
	private double clostCustomerSum;
	
	@ExcelField(title = "新增数", order = 10)
	private double caddCustomerSum;
	
	@ExcelField(title = "差值", order = 11)
	private double czDCustomerSum;
	
	@ExcelField(title = "流失数", order = 12)
	private double dlostCustomerSum;
	
	@ExcelField(title = "新增数", order = 13)
	private double daddCustomerSum;
	
	@ExcelField(title = "差值", order = 14)
	private double czECustomerSum;
	
	@ExcelField(title = "流失数", order = 15)
	private double elostCustomerSum;
	
	@ExcelField(title = "新增数", order = 16)
	private double eaddCustomerSum;
	
	@ExcelField(title = "差值", order = 17)
	private double czFCustomerSum;
	
	@ExcelField(title = "流失数", order = 18)
	private double flostCustomerSum;
	
	@ExcelField(title = "新增数", order = 19)
	private double faddCustomerSum;

	@ExcelField(title = "差值", order = 20)
	private double czGCustomerSum;
	
	@ExcelField(title = "流失数", order = 21)
	private double glostCustomerSum;
	
	@ExcelField(title = "新增数", order = 22)
	private double gaddCustomerSum;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public double getCzCustomerSum() {
		return czCustomerSum;
	}

	public void setCzCustomerSum(double czCustomerSum) {
		this.czCustomerSum = czCustomerSum;
	}

	public double getLostCustomerSum() {
		return lostCustomerSum;
	}

	public void setLostCustomerSum(double lostCustomerSum) {
		this.lostCustomerSum = lostCustomerSum;
	}

	public double getAddCustomerSum() {
		return addCustomerSum;
	}

	public void setAddCustomerSum(double addCustomerSum) {
		this.addCustomerSum = addCustomerSum;
	}

	public double getCzBCustomerSum() {
		return czBCustomerSum;
	}

	public void setCzBCustomerSum(double czBCustomerSum) {
		this.czBCustomerSum = czBCustomerSum;
	}

	public double getBlostCustomerSum() {
		return blostCustomerSum;
	}

	public void setBlostCustomerSum(double blostCustomerSum) {
		this.blostCustomerSum = blostCustomerSum;
	}

	public double getBaddCustomerSum() {
		return baddCustomerSum;
	}

	public void setBaddCustomerSum(double baddCustomerSum) {
		this.baddCustomerSum = baddCustomerSum;
	}

	public double getCzCCustomerSum() {
		return czCCustomerSum;
	}

	public void setCzCCustomerSum(double czCCustomerSum) {
		this.czCCustomerSum = czCCustomerSum;
	}

	public double getClostCustomerSum() {
		return clostCustomerSum;
	}

	public void setClostCustomerSum(double clostCustomerSum) {
		this.clostCustomerSum = clostCustomerSum;
	}

	public double getCaddCustomerSum() {
		return caddCustomerSum;
	}

	public void setCaddCustomerSum(double caddCustomerSum) {
		this.caddCustomerSum = caddCustomerSum;
	}

	public double getCzDCustomerSum() {
		return czDCustomerSum;
	}

	public void setCzDCustomerSum(double czDCustomerSum) {
		this.czDCustomerSum = czDCustomerSum;
	}

	public double getDlostCustomerSum() {
		return dlostCustomerSum;
	}

	public void setDlostCustomerSum(double dlostCustomerSum) {
		this.dlostCustomerSum = dlostCustomerSum;
	}

	public double getDaddCustomerSum() {
		return daddCustomerSum;
	}

	public void setDaddCustomerSum(double daddCustomerSum) {
		this.daddCustomerSum = daddCustomerSum;
	}

	public double getCzECustomerSum() {
		return czECustomerSum;
	}

	public void setCzECustomerSum(double czECustomerSum) {
		this.czECustomerSum = czECustomerSum;
	}

	public double getElostCustomerSum() {
		return elostCustomerSum;
	}

	public void setElostCustomerSum(double elostCustomerSum) {
		this.elostCustomerSum = elostCustomerSum;
	}

	public double getEaddCustomerSum() {
		return eaddCustomerSum;
	}

	public void setEaddCustomerSum(double eaddCustomerSum) {
		this.eaddCustomerSum = eaddCustomerSum;
	}

	public double getCzFCustomerSum() {
		return czFCustomerSum;
	}

	public void setCzFCustomerSum(double czFCustomerSum) {
		this.czFCustomerSum = czFCustomerSum;
	}

	public double getFlostCustomerSum() {
		return flostCustomerSum;
	}

	public void setFlostCustomerSum(double flostCustomerSum) {
		this.flostCustomerSum = flostCustomerSum;
	}

	public double getFaddCustomerSum() {
		return faddCustomerSum;
	}

	public void setFaddCustomerSum(double faddCustomerSum) {
		this.faddCustomerSum = faddCustomerSum;
	}

	public double getCzGCustomerSum() {
		return czGCustomerSum;
	}

	public void setCzGCustomerSum(double czGCustomerSum) {
		this.czGCustomerSum = czGCustomerSum;
	}

	public double getGlostCustomerSum() {
		return glostCustomerSum;
	}

	public void setGlostCustomerSum(double glostCustomerSum) {
		this.glostCustomerSum = glostCustomerSum;
	}

	public double getGaddCustomerSum() {
		return gaddCustomerSum;
	}

	public void setGaddCustomerSum(double gaddCustomerSum) {
		this.gaddCustomerSum = gaddCustomerSum;
	}

		
	

	
}
