package com.yunda.base.bigcustomer.domain;

import java.io.Serializable;

import com.github.crab2died.annotation.ExcelField;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-07-24153026
 */
public class StatementTypeDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//序号
	@ExcelField(title = "序号", order = 1)
	private Long id;
	//咨询类型
	@ExcelField(title = "咨询类型", order = 2)
	private String consultType;
	//结单结果
	@ExcelField(title = "结单结果", order = 3)
	private String statementResult;
	//状态
	//@ExcelField(title = "状态", order = 4)
	private String status;

	//页面显示状态
	@ExcelField(title = "状态", order = 4)
	private String showStatus;
 

	/**
	 * 设置：序号
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：序号
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：咨询类型
	 */
	public void setConsultType(String consultType) {
		this.consultType = consultType;
	}
	/**
	 * 获取：咨询类型
	 */
	public String getConsultType() {
		return consultType;
	}
	/**
	 * 设置：结单结果
	 */
	public void setStatementResult(String statementResult) {
		this.statementResult = statementResult;
	}
	/**
	 * 获取：结单结果
	 */
	public String getStatementResult() {
		return statementResult;
	}
	/**
	 * 设置：状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 获取：状态
	 */
	public String getStatus() {
		return status;
	}

	public String getShowStatus() {
		if(status.equals("1")){
			showStatus = "启用";
		}else if(status.equals("0")){
			showStatus = "禁用";
		}else{
			showStatus ="--";
		}
		return showStatus;
	}

	public void setShowStatus(String showStatus) {
		this.showStatus = showStatus;
	}
}
