package com.yunda.base.feiniao.customer.domain;

import java.io.Serializable;
import java.util.List;

import com.github.crab2died.annotation.ExcelField;


/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-03-01105527
 */
public class NotCooperateCustomerDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	private Integer id;
	//业务省
	private String province;
	//业务省编码
	private String provinceId;
	//市
	private String city;
	//一级网点编码
	private String mcCode;
	//一级网点名称
	private String mc;
	//网点名称
	@ExcelField(title = "所属网点", order = 1)
	private String branchName;
	//网点编码
	@ExcelField(title = "网点编码", order = 2)
	private String branchCode;
	//客户名称
	@ExcelField(title = "客户名称", order = 3)
	private String customerName;
	//联系人
	@ExcelField(title = "联系人", order = 4)
	private String contactName;
	//客户联系电话
	@ExcelField(title = "联系电话", order = 5)
	private String customerPhone;
	//日均单量
	@ExcelField(title = "日均票件量", order = 6)
	private String dayAverageAmount;
	//产品类目
	@ExcelField(title = "产品类目", order = 7)
	private String productType;
	//产品均重
	@ExcelField(title = "产品均重", order = 8)
	private String productAverageHeavy;
	//是否泡货
	@ExcelField(title = "是否泡货", order = 9)
	private String soak;
	//上传人姓名及联系方式
	@ExcelField(title = "上传人姓名及联系方式", order = 10)
	private String uploadNamePhone;
	//客户所属平台
	@ExcelField(title = "客户所属平台", order = 11)
	private String customerPlatform;
	//合作同行及情况
	@ExcelField(title = "合作同行及情况", order = 12)
	private String cooperatePeerCase;
    //合作同行填写信息对象
    private List<CooperatePeerDO> listCooperatePeer;
	//客户发货地址
	@ExcelField(title = "客户发货地址", order = 13)
	private String customerDeliverAddress;
	//备注
	@ExcelField(title = "备注", order = 14)
	private String remark;
	//为合作原因
	@ExcelField(title = "未合作原因", order = 15)
	private String notCooperateCause;
	//客户意向
	@ExcelField(title = "客户意向", order = 16)
	private String customerIntention;
	//网点需求
	@ExcelField(title = "网点需求", order = 17)
	private String branchNeed;
	//上传日期
	@ExcelField(title = "上传时间", order = 18)
	private String time;
	//省公司网点反馈审核
	@ExcelField(title = "省公司网点反馈审核", order = 19)
	private String feedBackCheck;
	//省公司处理状态
	@ExcelField(title = "省公司处理状态", order = 20)
	private String state;
	//合作网点名称
	@ExcelField(title = "合作网点编码", order = 21)
	private String cooperateBranch;
	//绑定客户VIP账号
	@ExcelField(title = "绑定vip账号", order = 22)
	private String boundVipAccount;
	//省公司处理意见
	private String provinceDealOpinion;
	//省公司处理人
	@ExcelField(title = "省公司处理人", order = 23)
	private String provinceDealName;
	//省公司处理时间
	@ExcelField(title = "省公司处理时间", order = 24)
	private String provinceDealTime;
	//总部处理意见
	@ExcelField(title = "总部处理意见", order = 25)
	private String zongBuDealOpinion;
	//总部处理人
	@ExcelField(title = "总部处理人", order = 26)
	private String zongBuDealName;
	//总部处理时间
	@ExcelField(title = "总部处理时间", order = 27)
	private String zongBuDealTime;
	//是否删除
    private String delete;
	//绑定vip账号时间
	private String boundTime;

	/**
	 * 设置：id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：省
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * 获取：省
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * 设置：市
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * 获取：市
	 */
	public String getCity() {
		return city;
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

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}
	/**
	 * 设置：联系人
	 */
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	/**
	 * 获取：联系人
	 */
	public String getContactName() {
		return contactName;
	}
	/**
	 * 设置：产品类目
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}
	/**
	 * 获取：产品类目
	 */
	public String getProductType() {
		return productType;
	}
	/**
	 * 设置：日均单量
	 */
	public void setDayAverageAmount(String dayAverageAmount) {
		this.dayAverageAmount = dayAverageAmount;
	}
	/**
	 * 获取：日均单量
	 */
	public String getDayAverageAmount() {
		return dayAverageAmount;
	}
	/**
	 * 设置：网点编码
	 */
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	/**
	 * 获取：网点编码
	 */
	public String getBranchCode() {
		return branchCode;
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
	 * 设置：上传日期
	 */
	public void setTime(String time) {
		this.time = time;
	}
	/**
	 * 获取：上传日期
	 */
	public String getTime() {
		return time;
	}
	/**
	 * 设置：状态
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * 获取：状态
	 */
	public String getState() {
		return state;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：客户所属平台
	 */
	public void setCustomerPlatform(String customerPlatform) {
		this.customerPlatform = customerPlatform;
	}
	/**
	 * 获取：客户所属平台
	 */
	public String getCustomerPlatform() {
		return customerPlatform;
	}
	/**
	 * 设置：客户发货地址
	 */
	public void setCustomerDeliverAddress(String customerDeliverAddress) {
		this.customerDeliverAddress = customerDeliverAddress;
	}
	/**
	 * 获取：客户发货地址
	 */
	public String getCustomerDeliverAddress() {
		return customerDeliverAddress;
	}
	/**
	 * 设置：为合作原因
	 */
	public void setNotCooperateCause(String notCooperateCause) {
		this.notCooperateCause = notCooperateCause;
	}
	/**
	 * 获取：为合作原因
	 */
	public String getNotCooperateCause() {
		return notCooperateCause;
	}
	/**
	 * 设置：客户意向
	 */
	public void setCustomerIntention(String customerIntention) {
		this.customerIntention = customerIntention;
	}
	/**
	 * 获取：客户意向
	 */
	public String getCustomerIntention() {
		return customerIntention;
	}
	/**
	 * 设置：网点需求
	 */
	public void setBranchNeed(String branchNeed) {
		this.branchNeed = branchNeed;
	}
	/**
	 * 获取：网点需求
	 */
	public String getBranchNeed() {
		return branchNeed;
	}

	public String getProductAverageHeavy() {
		return productAverageHeavy;
	}

	public void setProductAverageHeavy(String productAverageHeavy) {
		this.productAverageHeavy = productAverageHeavy;
	}

	public String getSoak() {
		return soak;
	}

	public void setSoak(String soak) {
		this.soak = soak;
	}

	public String getUploadNamePhone() {
		return uploadNamePhone;
	}

	public void setUploadNamePhone(String uploadNamePhone) {
		this.uploadNamePhone = uploadNamePhone;
	}

	public String getCooperatePeerCase() {
		return cooperatePeerCase;
	}

	public void setCooperatePeerCase(String cooperatePeerCase) {
		this.cooperatePeerCase = cooperatePeerCase;
	}

	public String getFeedBackCheck() {
		return feedBackCheck;
	}

	public void setFeedBackCheck(String feedBackCheck) {
		this.feedBackCheck = feedBackCheck;
	}

	public String getCooperateBranch() {
		return cooperateBranch;
	}

	public void setCooperateBranch(String cooperateBranch) {
		this.cooperateBranch = cooperateBranch;
	}

	public String getBoundVipAccount() {
		return boundVipAccount;
	}

	public void setBoundVipAccount(String boundVipAccount) {
		this.boundVipAccount = boundVipAccount;
	}

	public String getProvinceDealName() {
		return provinceDealName;
	}

	public void setProvinceDealName(String provinceDealName) {
		this.provinceDealName = provinceDealName;
	}

	public String getProvinceDealTime() {
		return provinceDealTime;
	}

	public void setProvinceDealTime(String provinceDealTime) {
		this.provinceDealTime = provinceDealTime;
	}

	public String getZongBuDealOpinion() {
		return zongBuDealOpinion;
	}

	public void setZongBuDealOpinion(String zongBuDealOpinion) {
		this.zongBuDealOpinion = zongBuDealOpinion;
	}

	public String getZongBuDealName() {
		return zongBuDealName;
	}

	public void setZongBuDealName(String zongBuDealName) {
		this.zongBuDealName = zongBuDealName;
	}

	public String getZongBuDealTime() {
		return zongBuDealTime;
	}

	public void setZongBuDealTime(String zongBuDealTime) {
		this.zongBuDealTime = zongBuDealTime;
	}

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public List<CooperatePeerDO> getListCooperatePeer() {
        return listCooperatePeer;
    }

    public void setListCooperatePeer(List<CooperatePeerDO> listCooperatePeer) {
        this.listCooperatePeer = listCooperatePeer;
    }

	public String getProvinceDealOpinion() {
		return provinceDealOpinion;
	}

	public void setProvinceDealOpinion(String provinceDealOpinion) {
		this.provinceDealOpinion = provinceDealOpinion;
	}

	public String getMcCode() {
		return mcCode;
	}

	public void setMcCode(String mcCode) {
		this.mcCode = mcCode;
	}

	public String getMc() {
		return mc;
	}

	public void setMc(String mc) {
		this.mc = mc;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getBoundTime() {
		return boundTime;
	}

	public void setBoundTime(String boundTime) {
		this.boundTime = boundTime;
	}

	@Override
	public String toString() {
		return "NotCooperateCustomerDO{" +
				"id=" + id +
				", province='" + province + '\'' +
				", provinceId='" + provinceId + '\'' +
				", city='" + city + '\'' +
				", mcCode='" + mcCode + '\'' +
				", mc='" + mc + '\'' +
				", branchName='" + branchName + '\'' +
				", branchCode='" + branchCode + '\'' +
				", customerName='" + customerName + '\'' +
				", contactName='" + contactName + '\'' +
				", customerPhone='" + customerPhone + '\'' +
				", dayAverageAmount='" + dayAverageAmount + '\'' +
				", productType='" + productType + '\'' +
				", productAverageHeavy='" + productAverageHeavy + '\'' +
				", soak='" + soak + '\'' +
				", uploadNamePhone='" + uploadNamePhone + '\'' +
				", customerPlatform='" + customerPlatform + '\'' +
				", cooperatePeerCase='" + cooperatePeerCase + '\'' +
				", listCooperatePeer=" + listCooperatePeer +
				", customerDeliverAddress='" + customerDeliverAddress + '\'' +
				", remark='" + remark + '\'' +
				", notCooperateCause='" + notCooperateCause + '\'' +
				", customerIntention='" + customerIntention + '\'' +
				", branchNeed='" + branchNeed + '\'' +
				", time='" + time + '\'' +
				", feedBackCheck='" + feedBackCheck + '\'' +
				", state='" + state + '\'' +
				", cooperateBranch='" + cooperateBranch + '\'' +
				", boundVipAccount='" + boundVipAccount + '\'' +
				", provinceDealOpinion='" + provinceDealOpinion + '\'' +
				", provinceDealName='" + provinceDealName + '\'' +
				", provinceDealTime='" + provinceDealTime + '\'' +
				", zongBuDealOpinion='" + zongBuDealOpinion + '\'' +
				", zongBuDealName='" + zongBuDealName + '\'' +
				", zongBuDealTime='" + zongBuDealTime + '\'' +
				", delete='" + delete + '\'' +
				", boundTime='" + boundTime + '\'' +
				'}';
	}
}
