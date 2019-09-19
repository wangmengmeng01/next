package com.yunda.base.feiniao.workorder.bo;

import com.yunda.base.common.bo.Bo_Interface;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

//需要分页参数的时候可以extends PageBo
@ApiModel(value = "客户咨询单参数")
public class Bo_ServiceWorkOrder implements Bo_Interface {
private static final long serialVersionUID = 1L;
	
	//工单编号
	@ApiModelProperty(value = "工单编号")
	private Integer workOrderId;
	
	//客户名称
	@NotNull()
	@Size(min = 1, max = 30, message = "客户名称长度必须在1和30之间")
	@ApiModelProperty(value = "客户名称")
	private String customerName;
	
	//客户编码
	@ApiModelProperty(value = "客户编码")	
	private String customerId;
	//运单号
	@ApiModelProperty(value = "运单号")
	private String shipId;
	//订单号
	@ApiModelProperty(value = "订单号")
	private String orderId;
	//发件人
	@ApiModelProperty(value = "发件人")
	private String sender;
	//发件人电话
	@ApiModelProperty(value = "发件人电话")
	private String sendNumber;
	//发件人地址
	@ApiModelProperty(value = "发件人地址")
	private String sendAddress;
	//收件人
	@ApiModelProperty(value = "收件人")
	private String receiver;
	//收件人电话
	@ApiModelProperty(value = "收件人电话")
	private String receiverNumber;
	//收件人地址
	@ApiModelProperty(value = "收件人地址")
	private String receiverAddress;
	//咨询类型
	@ApiModelProperty(value = "咨询类型")
	private String consultType;
	//咨询描述
	@ApiModelProperty(value = "咨询描述")
	private String consultDscr;
	//联系人
	@ApiModelProperty(value = "联系人")
	private String affiliation;
	//联系方式
	@ApiModelProperty(value = "联系方式")
	private String contactNumber;
	//咨询单状态
	@ApiModelProperty(value = "咨询单状态")
	private String consultSheetStatus;
	//网点名称
	@ApiModelProperty(value = "网点名称")
	private String branchName;
	//网点编码
	@ApiModelProperty(value = "网点编码")
	private Integer branchCode;
	//是否催处理
	@ApiModelProperty(value = "是否催处理")
	private String reminderProcessing;
	//当前处理人
	@ApiModelProperty(value = "当前处理人")
	private String handler;
	//当前处理人姓名
	@ApiModelProperty(value = "当前处理人姓名")
	private String handlerName;
	//当前处理机构
	@ApiModelProperty(value = "当前处理机构")
	private Integer handleOrganize;
	//当前处理机构名称
	@ApiModelProperty(value = "当前处理机构名称")
	private String handleOrganizeName;
	//结单类型
	@ApiModelProperty(value = "结单类型")
	private String statementType;
	//结单时间
	@ApiModelProperty(value = "结单时间")
	private Date statementTime;
	//版本号
	@ApiModelProperty(value = "版本号")
	private Integer version;
	//创建时间
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	//更新时间
	@ApiModelProperty(value = "更新时间")
	private Date updateTime;
	//是否有运单号
	@ApiModelProperty(value = "是否有运单号")
	private String workOrderType;
	//领取时间
	@ApiModelProperty(value = "领取时间")
	private Date receiveTime;
	//最新处理时间
	@ApiModelProperty(value = "最新处理时间")
	private Date newestTime;
	//处理时间
	@ApiModelProperty(value = "处理时间")
	private Date handlerTime;
	
	/**
	 * 设置：工单编号
	 */
	public void setWorkOrderId(Integer workOrderId) {
		this.workOrderId = workOrderId;
	}
	/**
	 * 获取：工单编号
	 */
	public Integer getWorkOrderId() {
		return workOrderId;
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
	 * 设置：运单号
	 */
	public void setShipId(String shipId) {
		this.shipId = shipId;
	}
	/**
	 * 获取：运单号
	 */
	public String getShipId() {
		return shipId;
	}
	/**
	 * 设置：订单号
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	/**
	 * 获取：订单号
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * 设置：发件人
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	/**
	 * 获取：发件人
	 */
	public String getSender() {
		return sender;
	}
	/**
	 * 设置：发件人电话
	 */
	public void setSendNumber(String sendNumber) {
		this.sendNumber = sendNumber;
	}
	/**
	 * 获取：发件人电话
	 */
	public String getSendNumber() {
		return sendNumber;
	}
	/**
	 * 设置：发件人地址
	 */
	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}
	/**
	 * 获取：发件人地址
	 */
	public String getSendAddress() {
		return sendAddress;
	}
	/**
	 * 设置：收件人
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	/**
	 * 获取：收件人
	 */
	public String getReceiver() {
		return receiver;
	}
	/**
	 * 设置：收件人电话
	 */
	public void setReceiverNumber(String receiverNumber) {
		this.receiverNumber = receiverNumber;
	}
	/**
	 * 获取：收件人电话
	 */
	public String getReceiverNumber() {
		return receiverNumber;
	}
	/**
	 * 设置：收件人地址
	 */
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	/**
	 * 获取：收件人地址
	 */
	public String getReceiverAddress() {
		return receiverAddress;
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
	 * 设置：咨询描述
	 */
	public void setConsultDscr(String consultDscr) {
		this.consultDscr = consultDscr;
	}
	/**
	 * 获取：咨询描述
	 */
	public String getConsultDscr() {
		return consultDscr;
	}
	/**
	 * 设置：联系人
	 */
	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}
	/**
	 * 获取：联系人
	 */
	public String getAffiliation() {
		return affiliation;
	}
	/**
	 * 设置：联系方式
	 */
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	/**
	 * 获取：联系方式
	 */
	public String getContactNumber() {
		return contactNumber;
	}
	/**
	 * 设置：咨询单状态
	 */
	public void setConsultSheetStatus(String consultSheetStatus) {
		this.consultSheetStatus = consultSheetStatus;
	}
	/**
	 * 获取：咨询单状态
	 */
	public String getConsultSheetStatus() {
		return consultSheetStatus;
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
	 * 设置：网点编码
	 */
	public void setBranchCode(Integer branchCode) {
		this.branchCode = branchCode;
	}
	/**
	 * 获取：网点编码
	 */
	public Integer getBranchCode() {
		return branchCode;
	}
	/**
	 * 设置：是否催处理
	 */
	public void setReminderProcessing(String reminderProcessing) {
		this.reminderProcessing = reminderProcessing;
	}
	/**
	 * 获取：是否催处理
	 */
	public String getReminderProcessing() {
		return reminderProcessing;
	}
	/**
	 * 设置：当前处理人
	 */
	public void setHandler(String handler) {
		this.handler = handler;
	}
	/**
	 * 获取：当前处理人
	 */
	public String getHandler() {
		return handler;
	}
	/**
	 * 设置：当前处理人姓名
	 */
	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}
	/**
	 * 获取：当前处理人姓名
	 */
	public String getHandlerName() {
		return handlerName;
	}
	/**
	 * 设置：当前处理机构
	 */
	public void setHandleOrganize(Integer handleOrganize) {
		this.handleOrganize = handleOrganize;
	}
	/**
	 * 获取：当前处理机构
	 */
	public Integer getHandleOrganize() {
		return handleOrganize;
	}
	/**
	 * 设置：当前处理机构名称
	 */
	public void setHandleOrganizeName(String handleOrganizeName) {
		this.handleOrganizeName = handleOrganizeName;
	}
	/**
	 * 获取：当前处理机构名称
	 */
	public String getHandleOrganizeName() {
		return handleOrganizeName;
	}
	/**
	 * 设置：结单类型
	 */
	public void setStatementType(String statementType) {
		this.statementType = statementType;
	}
	/**
	 * 获取：结单类型
	 */
	public String getStatementType() {
		return statementType;
	}
	/**
	 * 设置：结单时间
	 */
	public void setStatementTime(Date statementTime) {
		this.statementTime = statementTime;
	}
	/**
	 * 获取：结单时间
	 */
	public Date getStatementTime() {
		return statementTime;
	}
	/**
	 * 设置：版本号
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}
	/**
	 * 获取：版本号
	 */
	public Integer getVersion() {
		return version;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
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
	 * 设置：是否有运单号
	 */
	public void setWorkOrderType(String workOrderType) {
		this.workOrderType = workOrderType;
	}
	/**
	 * 获取：是否有运单号
	 */
	public String getWorkOrderType() {
		return workOrderType;
	}
	/**
	 * 设置：领取时间
	 */
	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}
	/**
	 * 获取：领取时间
	 */
	public Date getReceiveTime() {
		return receiveTime;
	}
	/**
	 * 设置：最新处理时间
	 */
	public void setNewestTime(Date newestTime) {
		this.newestTime = newestTime;
	}
	/**
	 * 获取：最新处理时间
	 */
	public Date getNewestTime() {
		return newestTime;
	}
	/**
	 * 设置：处理时间
	 */
	public void setHandlerTime(Date handlerTime) {
		this.handlerTime = handlerTime;
	}
	/**
	 * 获取：处理时间
	 */
	public Date getHandlerTime() {
		return handlerTime;
	}

}
