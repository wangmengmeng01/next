package com.yunda.base.feiniao.market.domain;

import java.io.Serializable;

import com.github.crab2died.annotation.ExcelField;


/**
 * 客户报表订单统计-总表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-11 14:54:40
 */
public class ExportMarketOccupancyReportDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ExcelField(title = "序号", order = 1)
	private Integer recordId;
	//大区
	@ExcelField(title = "大区", order = 2)
	private String bigarea;
	//省名称
	@ExcelField(title = "省名称", order = 3)
	private String provincename;
	//负责人
	@ExcelField(title = "负责人", order = 4)
	private String responsiblePeople;
	//上报年份
	@ExcelField(title = "上报年份", order = 5)
	private String reportNian;
	//上报月份
	@ExcelField(title = "上报月份", order = 6)
	private String reportYue;
	//上报状态
	@ExcelField(title = "上报状态", order = 7)
	private String reportStatus;
	//审核结果
	@ExcelField(title = "审核结果", order = 8)
	private String auditResult;
	//审核备注
	@ExcelField(title = "审核备注", order = 9)
	private String auditRemarks;
	//本月得分
	@ExcelField(title = "本月得分", order = 10)
	private Integer monthScore;
	
	
	public Integer getRecordId() {
		return recordId;
	}
	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}
	public String getBigarea() {
		return bigarea;
	}
	public void setBigarea(String bigarea) {
		this.bigarea = bigarea;
	}
	public String getProvincename() {
		return provincename;
	}
	public void setProvincename(String provincename) {
		this.provincename = provincename;
	}
	public String getResponsiblePeople() {
		return responsiblePeople;
	}
	public void setResponsiblePeople(String responsiblePeople) {
		this.responsiblePeople = responsiblePeople;
	}
	public String getReportNian() {
		return reportNian;
	}
	public void setReportNian(String reportNian) {
		this.reportNian = reportNian;
	}
	public String getReportYue() {
		return reportYue;
	}
	public void setReportYue(String reportYue) {
		this.reportYue = reportYue;
	}
	public String getReportStatus() {
		return reportStatus;
	}
	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}
	public String getAuditResult() {
		return auditResult;
	}
	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}
	public String getAuditRemarks() {
		return auditRemarks;
	}
	public void setAuditRemarks(String auditRemarks) {
		this.auditRemarks = auditRemarks;
	}
	public Integer getMonthScore() {
		return monthScore;
	}
	public void setMonthScore(Integer monthScore) {
		this.monthScore = monthScore;
	}
}
