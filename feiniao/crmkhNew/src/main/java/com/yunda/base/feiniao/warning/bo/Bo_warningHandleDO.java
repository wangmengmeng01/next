package com.yunda.base.feiniao.warning.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.github.crab2died.annotation.ExcelField;


/**
 * 预警反馈表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-03-13095948
 */
@ApiModel(value = "预警反馈表验参")
public class Bo_warningHandleDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	//客户编码
	@ApiModelProperty(value="客户编码")
	private String customerId;
	//客户名称
	@ApiModelProperty(value="客户名称")
	private String customerName;
	//所属站点编码(公司)
	@ApiModelProperty(value="所属网点编码")
	private String branchCode;
	
	/*//该条数据的预警日期
	@ApiModelProperty(value="该条数据的预警日期")
	private Date warnDate;*/
	
	//客户类型
	@ApiModelProperty(value="客户类型")
	private String priceLevel;
	
	//反馈状态
	@ApiModelProperty(value="反馈状态")
	private String feedbackStatus;
	
	//网点反馈类型
	@ApiModelProperty(value="网点反馈类型")
	private String branchDealType;
	
	@ApiModelProperty(value="每页查询数")
	public int limit;
	
	@ApiModelProperty(value="每页开始位置")
	public int offset;
	
	/*预警日期 开始时间*/
	@ApiModelProperty(value = "开始时间")
	private String startDate;
	/*预警日期 结束时间*/
	@ApiModelProperty(value = "结束时间")
	private String endDate;
//权限管理
	@ApiModelProperty(value = "权限类型标识  S省 	D大区	 W网点")
	private String tmp_field;
	//管辖省份的id   省权限
	private List<Long> provinceIdsqx;
	@ApiModelProperty(value = "网点权限")
	private Integer branchCodeQX;
	
	@ApiModelProperty(value = "省份")
	private String provinceName;
	@ApiModelProperty(value = "大区")
	private String bigarea;
	
	
	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getBigarea() {
		return bigarea;
	}

	public void setBigarea(String bigarea) {
		this.bigarea = bigarea;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getTmp_field() {
		return tmp_field;
	}

	public void setTmp_field(String tmp_field) {
		this.tmp_field = tmp_field;
	}

	public List<Long> getProvinceIdsqx() {
		return provinceIdsqx;
	}

	public void setProvinceIdsqx(List<Long> provinceIdsqx) {
		this.provinceIdsqx = provinceIdsqx;
	}

	public Integer getBranchCodeQX() {
		return branchCodeQX;
	}

	public void setBranchCodeQX(Integer branchCodeQX) {
		this.branchCodeQX = branchCodeQX;
	}

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

	public String getPriceLevel() {
		return priceLevel;
	}

	public void setPriceLevel(String priceLevel) {
		this.priceLevel = priceLevel;
	}

	public String getFeedbackStatus() {
		return feedbackStatus;
	}

	public void setFeedbackStatus(String feedbackStatus) {
		this.feedbackStatus = feedbackStatus;
	}

	public String getBranchDealType() {
		return branchDealType;
	}

	public void setBranchDealType(String branchDealType) {
		this.branchDealType = branchDealType;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	

}
