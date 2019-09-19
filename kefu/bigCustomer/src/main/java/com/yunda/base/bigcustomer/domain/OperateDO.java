package com.yunda.base.bigcustomer.domain;
import java.io.Serializable;
import java.util.List;

import com.github.crab2died.annotation.ExcelField;


/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-15140330
 */
public class OperateDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//序号
	@ExcelField(title = "序号", order = 1)
	private Integer id;
	//咨询订单号
	private String orderId;
	//操作人
	@ExcelField(title = "操作人", order = 2)
	private String operateName;
	//操作人账号
	private String operateCode;
	//操作机构
	@ExcelField(title = "操作机构", order = 3)
	private String operateOrganization;
	//操作时间
	@ExcelField(title = "操作时间", order = 4)
	private String time;
	//操作类型
	@ExcelField(title = "操作类型", order = 5)
	private String type;
	//处理内容
	@ExcelField(title = "处理内容", order = 6)
	private String dealContent;
	//结单结果
	private String jieDanResult;
	//附件地址
	@ExcelField(title = "附件地址", order = 7)
	private String uploadPath;
	//附件名称
	private String fileName;
	//责任方
	@ExcelField(title = "责任方", order = 8)
	private String zeRenFang;

	private List<ConsultFileDO> consultFileDOList;

	public List<ConsultFileDO> getConsultFileDOList() {
		return consultFileDOList;
	}

	public void setConsultFileDOList(List<ConsultFileDO> consultFileDOList) {
		this.consultFileDOList = consultFileDOList;
	}

	/**
	 * 设置：序号
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：序号
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：操作人
	 */
	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}
	/**
	 * 获取：操作人
	 */
	public String getOperateName() {
		return operateName;
	}
	/**
	 * 设置：操作机构
	 */
	public void setOperateOrganization(String operateOrganization) {
		this.operateOrganization = operateOrganization;
	}
	/**
	 * 获取：操作机构
	 */
	public String getOperateOrganization() {
		return operateOrganization;
	}
	/**
	 * 设置：操作时间
	 */
	public void setTime(String time) {
		this.time = time;
	}
	/**
	 * 获取：操作时间
	 */
	public String getTime() {
		return time;
	}
	/**
	 * 设置：操作类型
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：操作类型
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：处理内容
	 */
	public void setDealContent(String dealContent) {
		this.dealContent = dealContent;
	}
	/**
	 * 获取：处理内容
	 */
	public String getDealContent() {
		return dealContent;
	}
	/**
	 * 设置：附件地址
	 */
	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}
	/**
	 * 获取：附件地址
	 */
	public String getUploadPath() {
		return uploadPath;
	}
	/**
	 * 设置：责任方
	 */
	public void setZeRenFang(String zeRenFang) {
		this.zeRenFang = zeRenFang;
	}
	/**
	 * 获取：责任方
	 */
	public String getZeRenFang() {
		return zeRenFang;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOperateCode() {
		return operateCode;
	}

	public void setOperateCode(String operateCode) {
		this.operateCode = operateCode;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getJieDanResult() {
		return jieDanResult;
	}

	public void setJieDanResult(String jieDanResult) {
		this.jieDanResult = jieDanResult;
	}
}
