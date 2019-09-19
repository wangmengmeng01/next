package com.yunda.base.feiniao.warning.domain;

import java.io.Serializable;

import com.github.crab2died.annotation.ExcelField;


/**
 * 预警反馈表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-03-13095948
 */
public class ExportWarningHandleDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	/*@ExcelField(title = "id", order = 1)
	private Long id;*/
	//客户编码
	@ExcelField(title = "客户编码", order = 1)
	private String customerId;
	//客户名称
	@ExcelField(title = "客户名称", order = 2)
	private String customerName;
	//客户来源
	@ExcelField(title = "客户来源", order = 3)
	private String showCustomerSourceType;
	//客户类型
	@ExcelField(title = "客户类别", order = 4)
	private String showPriceLevel;
	//该条数据的预警日期
	@ExcelField(title = "预警日期", order = 5)
	private String showWarnDate;
	//日均单量
	@ExcelField(title = "当前日均量", order = 6)
	private Double orderAvg;
	//上周日均量
	@ExcelField(title = "上周日均量", order = 7)
	private Double lastOrderAvg;
	//降幅比率
	@ExcelField(title = "降幅比率", order = 8)
	private String reducedRatio;
	//降幅单量
	@ExcelField(title = "降幅单量", order = 9)
	private Double reducedOrder;
	//所属站点编码(公司)
	@ExcelField(title = "所属站点编码(公司)", order = 10)
	private Integer gs;
	//所属站点名称(公司)
	@ExcelField(title = "所属站点名称(公司)", order = 11)
	private String gsmc;
	//上级网点编码
	@ExcelField(title = "上级网点编码", order = 12)
	private Integer branchCode;
	//上级网点名称
	@ExcelField(title = "上级网点名称", order = 13)
	private String branchName;
	//市id
	@ExcelField(title = "市id", order = 14)
	private String cityid;
	//市名称
	@ExcelField(title = "市名称", order = 15)
	private String cityname;
	//省id
	@ExcelField(title = "省id", order = 16)
	private String provinceid;
	//省名称
	@ExcelField(title = "省名称", order = 17)
	private String provincename;
	//大区
	@ExcelField(title = "大区", order = 18)
	private String bigarea;
	
	@ExcelField(title = "反馈截止时间", order = 19)
	private String showFeedbackDeadline;
	@ExcelField(title = "反馈状态", order = 20)
	private String showFeedbackStatus;
	//备注
	@ExcelField(title = "备注", order = 21)
	private String remark;
	//网点反馈人
	@ExcelField(title = "网点反馈人", order = 22)
	private String barnchDealUser;
	//网点反馈类型
	@ExcelField(title = "网点反馈类型", order = 23)
	private String showBranchDealType;
	//网点反馈内容
	@ExcelField(title = "网点反馈内容", order = 24)
	private String branchDealDesc;
	//网点反馈时间--用来罚款的
	@ExcelField(title = "网点反馈时间", order = 25)
	private String showBranchFeedbackDate;
	//省总反馈人
	@ExcelField(title = "省总反馈人", order = 26)
	private String provinceDealUser;	
	//省总反馈内容
	@ExcelField(title = "省总反馈内容", order = 27)
	private String provinceDealDesc;
	//省总反馈时间
	@ExcelField(title = "省总反馈时间", order = 28)
	private String showProvinceFeedbackDate;
	//总部反馈人
	@ExcelField(title = "总部反馈人", order = 29)
	private String zbDealUser;
	//总部反馈内容
	@ExcelField(title = "总部反馈内容", order = 30)
	private String zbDealDesc;
	//总部反馈时间
	@ExcelField(title = "总部反馈时间", order = 31)
	private String showZbFeedbackDate;
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getShowCustomerSourceType() {
		return showCustomerSourceType;
	}
	public void setShowCustomerSourceType(String showCustomerSourceType) {
		this.showCustomerSourceType = showCustomerSourceType;
	}
	public String getShowPriceLevel() {
		return showPriceLevel;
	}
	public void setShowPriceLevel(String showPriceLevel) {
		this.showPriceLevel = showPriceLevel;
	}
	public String getShowWarnDate() {
		return showWarnDate;
	}
	public void setShowWarnDate(String showWarnDate) {
		this.showWarnDate = showWarnDate;
	}
	public Double getOrderAvg() {
		return orderAvg;
	}
	public void setOrderAvg(Double orderAvg) {
		this.orderAvg = orderAvg;
	}
	public Double getLastOrderAvg() {
		return lastOrderAvg;
	}
	public void setLastOrderAvg(Double lastOrderAvg) {
		this.lastOrderAvg = lastOrderAvg;
	}
	public String getReducedRatio() {
		return reducedRatio;
	}
	public void setReducedRatio(String reducedRatio) {
		this.reducedRatio = reducedRatio;
	}
	public Double getReducedOrder() {
		return reducedOrder;
	}
	public void setReducedOrder(Double reducedOrder) {
		this.reducedOrder = reducedOrder;
	}
	public Integer getGs() {
		return gs;
	}
	public void setGs(Integer gs) {
		this.gs = gs;
	}
	public String getGsmc() {
		return gsmc;
	}
	public void setGsmc(String gsmc) {
		this.gsmc = gsmc;
	}
	public Integer getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(Integer branchCode) {
		this.branchCode = branchCode;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getCityid() {
		return cityid;
	}
	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	public String getCityname() {
		return cityname;
	}
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	public String getProvinceid() {
		return provinceid;
	}
	public void setProvinceid(String provinceid) {
		this.provinceid = provinceid;
	}
	public String getProvincename() {
		return provincename;
	}
	public void setProvincename(String provincename) {
		this.provincename = provincename;
	}
	public String getBigarea() {
		return bigarea;
	}
	public void setBigarea(String bigarea) {
		this.bigarea = bigarea;
	}
	public String getShowFeedbackDeadline() {
		return showFeedbackDeadline;
	}
	public void setShowFeedbackDeadline(String showFeedbackDeadline) {
		this.showFeedbackDeadline = showFeedbackDeadline;
	}
	public String getShowFeedbackStatus() {
		return showFeedbackStatus;
	}
	public void setShowFeedbackStatus(String showFeedbackStatus) {
		this.showFeedbackStatus = showFeedbackStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBarnchDealUser() {
		return barnchDealUser;
	}
	public void setBarnchDealUser(String barnchDealUser) {
		this.barnchDealUser = barnchDealUser;
	}
	public String getShowBranchDealType() {
		return showBranchDealType;
	}
	public void setShowBranchDealType(String showBranchDealType) {
		this.showBranchDealType = showBranchDealType;
	}
	public String getBranchDealDesc() {
		return branchDealDesc;
	}
	public void setBranchDealDesc(String branchDealDesc) {
		this.branchDealDesc = branchDealDesc;
	}
	public String getShowBranchFeedbackDate() {
		return showBranchFeedbackDate;
	}
	public void setShowBranchFeedbackDate(String showBranchFeedbackDate) {
		this.showBranchFeedbackDate = showBranchFeedbackDate;
	}
	public String getProvinceDealUser() {
		return provinceDealUser;
	}
	public void setProvinceDealUser(String provinceDealUser) {
		this.provinceDealUser = provinceDealUser;
	}
	public String getProvinceDealDesc() {
		return provinceDealDesc;
	}
	public void setProvinceDealDesc(String provinceDealDesc) {
		this.provinceDealDesc = provinceDealDesc;
	}
	public String getShowProvinceFeedbackDate() {
		return showProvinceFeedbackDate;
	}
	public void setShowProvinceFeedbackDate(String showProvinceFeedbackDate) {
		this.showProvinceFeedbackDate = showProvinceFeedbackDate;
	}
	public String getZbDealUser() {
		return zbDealUser;
	}
	public void setZbDealUser(String zbDealUser) {
		this.zbDealUser = zbDealUser;
	}
	public String getZbDealDesc() {
		return zbDealDesc;
	}
	public void setZbDealDesc(String zbDealDesc) {
		this.zbDealDesc = zbDealDesc;
	}
	public String getShowZbFeedbackDate() {
		return showZbFeedbackDate;
	}
	public void setShowZbFeedbackDate(String showZbFeedbackDate) {
		this.showZbFeedbackDate = showZbFeedbackDate;
	}
	
}
