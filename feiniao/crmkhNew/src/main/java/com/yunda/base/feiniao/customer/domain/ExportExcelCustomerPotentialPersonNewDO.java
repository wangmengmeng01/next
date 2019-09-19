package com.yunda.base.feiniao.customer.domain;

import com.github.crab2died.annotation.ExcelField;

import java.io.Serializable;


/**
 * 潜在客户新表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-12-14173416
 */
public class ExportExcelCustomerPotentialPersonNewDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	//客户名称
	@ExcelField(title = "客户名称", order = 1)
	private String customerName;
	//负责人联系方式
	@ExcelField(title = "联系方式", order = 2)
	private String sendNumber;
	//店铺名称
	@ExcelField(title = "店铺名称", order = 3)
	private String shopName;
	//产品结构
	@ExcelField(title = "产品结构", order = 4)
	private String product;
	//是否泡货
	@ExcelField(title = "是否泡货", order = 5)
	private String bulkyCargo;
	//三公斤内均重（kg）
	@ExcelField(title = "三公斤内均重（kg）", order = 6)
	private String weight;
	//日均件量(票)
	@ExcelField(title = "日均件量(票)", order = 7)
	private String dailyOrderAvg;
	//合作快递公司
	@ExcelField(title = "合作快递公司", order = 8)
	private String expressCompany;
	//价格
	@ExcelField(title = "价格", order = 9)
	private String unitPrice;
	//客户具体发货地址
	@ExcelField(title = "客户具体发货地址", order = 10)
	private String sendAddress;
	//网点编码
	@ExcelField(title = "网点编码", order = 11)
	private String branchCode;
	//网点名称
	@ExcelField(title = "网点名称", order = 12)
	private String branchName;
	//城市
	@ExcelField(title = "城市", order = 13)
	private String cityName;
	//省份名称
	@ExcelField(title = "省份", order = 14)
	private String provinceName;
	//大区
	@ExcelField(title = "大区", order = 15)
	private String bigarea;
	@ExcelField(title = "上传人员姓名", order = 16)
	private String handlerName;
	//修改时间
	@ExcelField(title = "维护时间", order = 17)
	private String updateTime;
 


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
	 * 设置：负责人联系方式
	 */
	public void setSendNumber(String sendNumber) {
		this.sendNumber = sendNumber;
	}
	/**
	 * 获取：负责人联系方式
	 */
	public String getSendNumber() {
		return sendNumber;
	}
	/**
	 * 设置：店铺名称
	 */
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	/**
	 * 获取：店铺名称
	 */
	public String getShopName() {
		return shopName;
	}
	/**
	 * 设置：产品结构
	 */
	public void setProduct(String product) {
		this.product = product;
	}
	/**
	 * 获取：产品结构
	 */
	public String getProduct() {
		return product;
	}
	/**
	 * 设置：是否泡货
	 */
	public void setBulkyCargo(String bulkyCargo) {
		this.bulkyCargo = bulkyCargo;
	}
	/**
	 * 获取：是否泡货
	 */
	public String getBulkyCargo() {
		return bulkyCargo;
	}
	/**
	 * 设置：三公斤内均重（kg）
	 */
	public void setWeight(String weight) {
		this.weight = weight;
	}
	/**
	 * 获取：三公斤内均重（kg）
	 */
	public String getWeight() {
		return weight;
	}
	/**
	 * 设置：日均件量(票)
	 */
	public void setDailyOrderAvg(String dailyOrderAvg) {
		this.dailyOrderAvg = dailyOrderAvg;
	}
	/**
	 * 获取：日均件量(票)
	 */
	public String getDailyOrderAvg() {
		return dailyOrderAvg;
	}
	/**
	 * 设置：合作快递公司
	 */
	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}
	/**
	 * 获取：合作快递公司
	 */
	public String getExpressCompany() {
		return expressCompany;
	}
	/**
	 * 设置：价格
	 */
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}
	/**
	 * 获取：价格
	 */
	public String getUnitPrice() {
		return unitPrice;
	}
	/**
	 * 设置：客户具体发货地址
	 */
	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}
	/**
	 * 获取：客户具体发货地址
	 */
	public String getSendAddress() {
		return sendAddress;
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
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
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
	public String getHandlerName() {
		return handlerName;
	}
	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	

}
