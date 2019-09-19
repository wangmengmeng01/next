package com.yunda.base.feiniao.customer.bo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class NotCooperateCustomerBO implements Serializable {
	private static final long serialVersionUID = 1L;

	//id
	private Integer id;
	//网点编码
	private String branchCode;
	//一级网点编码
	private String mcCode;
	//客户名称
	private String customerName;
	//客户联系电话
	private String customerPhone;
	/*开始时间*/
	private String startDate;
	/* 结束时间*/
	private String endDate;
	//状态
	private String state;
	/* 每页查询数*/
	@ApiModelProperty(value = "每页查询数")
	private int limit;

	/* 查询开始位置*/
	@ApiModelProperty(value = "查询开始位置")
	private int offset;
	/* 大区权限*/
	private List<String> bigareaNames;

	/* 权限标识*/
	private String tmpField;

	/* 省份权限*/
	private List<Long> provinceids;
	//省名称
	private String provinceName;

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

	public List<String> getBigareaNames() {
		return bigareaNames;
	}

	public void setBigareaNames(List<String> bigareaNames) {
		this.bigareaNames = bigareaNames;
	}

	public String getTmpField() {
		return tmpField;
	}

	public void setTmpField(String tmpField) {
		this.tmpField = tmpField;
	}

	public List<Long> getProvinceids() {
		return provinceids;
	}

	public void setProvinceids(List<Long> provinceids) {
		this.provinceids = provinceids;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getMcCode() {
		return mcCode;
	}

	public void setMcCode(String mcCode) {
		this.mcCode = mcCode;
	}

	@Override
	public String toString() {
		return "NotCooperateCustomerBO{" +
				"id=" + id +
				", branchCode='" + branchCode + '\'' +
				", mcCode='" + mcCode + '\'' +
				", customerName='" + customerName + '\'' +
				", customerPhone='" + customerPhone + '\'' +
				", startDate='" + startDate + '\'' +
				", endDate='" + endDate + '\'' +
				", state='" + state + '\'' +
				", limit=" + limit +
				", offset=" + offset +
				", bigareaNames=" + bigareaNames +
				", tmpField='" + tmpField + '\'' +
				", provinceids=" + provinceids +
				", provinceName='" + provinceName + '\'' +
				'}';
	}
}