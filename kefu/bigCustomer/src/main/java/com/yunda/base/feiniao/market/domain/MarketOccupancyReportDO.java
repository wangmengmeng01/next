package com.yunda.base.feiniao.market.domain;

import java.io.Serializable;
import java.util.Date;

import com.github.crab2died.annotation.ExcelField;


/**
 * 市场占有率数据上报
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-10-12103231
 */
public class MarketOccupancyReportDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//数据记录主键ID
	@ExcelField(title = "数据记录主键ID", order = 1)
	private Integer recordId;
	//大区
	@ExcelField(title = "大区", order = 2)
	private String bigarea;
	//省ID
	@ExcelField(title = "省ID", order = 3)
	private String provinceid;
	//省名称
	@ExcelField(title = "省名称", order = 4)
	private String provincename;
	//城市ID
	@ExcelField(title = "城市ID", order = 5)
	private String cityid;
	//城市名称
	@ExcelField(title = "城市名称", order = 6)
	private String cityname;
	//负责人
	@ExcelField(title = "负责人", order = 7)
	private String responsiblePeople;
	//所含重点城市
	@ExcelField(title = "所含重点城市", order = 8)
	private String containCity;
	//上报月份
	@ExcelField(title = "上报月份", order = 9)
	private String reportDate;
	//上报状态
	@ExcelField(title = "上报状态", order = 10)
	private String reportStatus;
	//审核结果
	@ExcelField(title = "审核结果", order = 11)
	private String auditResult;
	//审核备注
	@ExcelField(title = "审核备注", order = 12)
	private String auditRemarks;
	//地区/公司
	@ExcelField(title = "地区/公司", order = 13)
	private String regionGs;
	//韵达占有率
	@ExcelField(title = "韵达占有率", order = 14)
	private Double proportionYd;
	//圆通占有率
	@ExcelField(title = "圆通占有率", order = 15)
	private Double proportionYt;
	//中通占有率
	@ExcelField(title = "中通占有率", order = 16)
	private Double proportionZt;
	//申通占有率
	@ExcelField(title = "申通占有率", order = 17)
	private Double proportionSt;
	//百世占有率
	@ExcelField(title = "百世占有率", order = 18)
	private Double proportionBs;
	//审核人
	@ExcelField(title = "审核人", order = 19)
	private String auditUser;
	//更新时间
	@ExcelField(title = "更新时间", order = 20)
	private Date updateTime;
	//本月得分
	@ExcelField(title = "本月得分", order = 21)
	private Integer monthScore;
	//上报年份
	@ExcelField(title = "上报年份", order = 22)
	private String reportNian;
	//上报月份
	@ExcelField(title = "上报月份", order = 23)
	private String reportYue;
	//省--韵达量
	@ExcelField(title = "省--韵达量", order = 24)
	private Double orderProvinceYdSum;
	//集团总量
	@ExcelField(title = "集团总量", order = 25)
	private Double orderJtzlYdSum;
	//登录人类型
	@ExcelField(title = "登录人类型", order = 26)
	private String type;
 
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
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
	 * 设置：大区
	 */
	public void setBigarea(String bigarea) {
		this.bigarea = bigarea;
	}
	/**
	 * 获取：大区
	 */
	public String getBigarea() {
		return bigarea;
	}
	/**
	 * 设置：省ID
	 */
	public void setProvinceid(String provinceid) {
		this.provinceid = provinceid;
	}
	/**
	 * 获取：省ID
	 */
	public String getProvinceid() {
		return provinceid;
	}
	/**
	 * 设置：省名称
	 */
	public void setProvincename(String provincename) {
		this.provincename = provincename;
	}
	/**
	 * 获取：省名称
	 */
	public String getProvincename() {
		return provincename;
	}
	/**
	 * 设置：城市ID
	 */
	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	/**
	 * 获取：城市ID
	 */
	public String getCityid() {
		return cityid;
	}
	/**
	 * 设置：城市名称
	 */
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	/**
	 * 获取：城市名称
	 */
	public String getCityname() {
		return cityname;
	}
	/**
	 * 设置：负责人
	 */
	public void setResponsiblePeople(String responsiblePeople) {
		this.responsiblePeople = responsiblePeople;
	}
	/**
	 * 获取：负责人
	 */
	public String getResponsiblePeople() {
		return responsiblePeople;
	}
	/**
	 * 设置：所含重点城市
	 */
	public void setContainCity(String containCity) {
		this.containCity = containCity;
	}
	/**
	 * 获取：所含重点城市
	 */
	public String getContainCity() {
		return containCity;
	}
	/**
	 * 设置：上报月份
	 */
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	/**
	 * 获取：上报月份
	 */
	public String getReportDate() {
		return reportDate;
	}
	/**
	 * 设置：上报状态
	 */
	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}
	/**
	 * 获取：上报状态
	 */
	public String getReportStatus() {
		return reportStatus;
	}
	/**
	 * 设置：审核结果
	 */
	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}
	/**
	 * 获取：审核结果
	 */
	public String getAuditResult() {
		return auditResult;
	}
	/**
	 * 设置：审核备注
	 */
	public void setAuditRemarks(String auditRemarks) {
		this.auditRemarks = auditRemarks;
	}
	/**
	 * 获取：审核备注
	 */
	public String getAuditRemarks() {
		return auditRemarks;
	}
	/**
	 * 设置：地区/公司
	 */
	public void setRegionGs(String regionGs) {
		this.regionGs = regionGs;
	}
	/**
	 * 获取：地区/公司
	 */
	public String getRegionGs() {
		return regionGs;
	}
	/**
	 * 设置：韵达占有率
	 */
	public void setProportionYd(Double proportionYd) {
		this.proportionYd = proportionYd;
	}
	/**
	 * 获取：韵达占有率
	 */
	public Double getProportionYd() {
		return proportionYd;
	}
	/**
	 * 设置：圆通占有率
	 */
	public void setProportionYt(Double proportionYt) {
		this.proportionYt = proportionYt;
	}
	/**
	 * 获取：圆通占有率
	 */
	public Double getProportionYt() {
		return proportionYt;
	}
	/**
	 * 设置：中通占有率
	 */
	public void setProportionZt(Double proportionZt) {
		this.proportionZt = proportionZt;
	}
	/**
	 * 获取：中通占有率
	 */
	public Double getProportionZt() {
		return proportionZt;
	}
	/**
	 * 设置：申通占有率
	 */
	public void setProportionSt(Double proportionSt) {
		this.proportionSt = proportionSt;
	}
	/**
	 * 获取：申通占有率
	 */
	public Double getProportionSt() {
		return proportionSt;
	}
	/**
	 * 设置：百世占有率
	 */
	public void setProportionBs(Double proportionBs) {
		this.proportionBs = proportionBs;
	}
	/**
	 * 获取：百世占有率
	 */
	public Double getProportionBs() {
		return proportionBs;
	}
	/**
	 * 设置：审核人
	 */
	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}
	/**
	 * 获取：审核人
	 */
	public String getAuditUser() {
		return auditUser;
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
	 * 设置：本月得分
	 */
	public void setMonthScore(Integer monthScore) {
		this.monthScore = monthScore;
	}
	/**
	 * 获取：本月得分
	 */
	public Integer getMonthScore() {
		return monthScore;
	}
	/**
	 * 设置：上报年份
	 */
	public void setReportNian(String reportNian) {
		this.reportNian = reportNian;
	}
	/**
	 * 获取：上报年份
	 */
	public String getReportNian() {
		return reportNian;
	}
	/**
	 * 设置：上报月份
	 */
	public void setReportYue(String reportYue) {
		this.reportYue = reportYue;
	}
	/**
	 * 获取：上报月份
	 */
	public String getReportYue() {
		return reportYue;
	}
	/**
	 * 设置：省--韵达量
	 */
	public void setOrderProvinceYdSum(Double orderProvinceYdSum) {
		this.orderProvinceYdSum = orderProvinceYdSum;
	}
	/**
	 * 获取：省--韵达量
	 */
	public Double getOrderProvinceYdSum() {
		return orderProvinceYdSum;
	}
	/**
	 * 设置：集团总量
	 */
	public void setOrderJtzlYdSum(Double orderJtzlYdSum) {
		this.orderJtzlYdSum = orderJtzlYdSum;
	}
	/**
	 * 获取：集团总量
	 */
	public Double getOrderJtzlYdSum() {
		return orderJtzlYdSum;
	}
}
