package com.yunda.base.system.domain;

import java.io.Serializable;
import java.util.List;

import com.github.crab2died.annotation.ExcelField;


/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-08-21 10:35:37
 */
public class ProvinceDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//省ID
	@ExcelField(title = "省ID", order = 1)
	private String provinceid;
	//省名称
	@ExcelField(title = "省名称", order = 2)
	private String provincename;
	//简称
	@ExcelField(title = "简称", order = 3)
	private String mininame;
	//bigarea
	@ExcelField(title = "bigarea", order = 4)
	private String bigarea;
	//新大区编号
	@ExcelField(title = "新大区编号", order = 5)
	private Integer rgId;
	//order_id
	@ExcelField(title = "order_id", order = 6)
	private Integer orderId;
	//省可维护状态，1不可维护，2可维护
	@ExcelField(title = "省可维护状态，1不可维护，2可维护", order = 7)
	private Integer maintainState;
	
	private List<Long> cityIds;

 

	public List<Long> getCityIds() {
		return cityIds;
	}
	public void setCityIds(List<Long> cityIds) {
		this.cityIds = cityIds;
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
	 * 设置：简称
	 */
	public void setMininame(String mininame) {
		this.mininame = mininame;
	}
	/**
	 * 获取：简称
	 */
	public String getMininame() {
		return mininame;
	}
	/**
	 * 设置：bigarea
	 */
	public void setBigarea(String bigarea) {
		this.bigarea = bigarea;
	}
	/**
	 * 获取：bigarea
	 */
	public String getBigarea() {
		return bigarea;
	}
	/**
	 * 设置：新大区编号
	 */
	public void setRgId(Integer rgId) {
		this.rgId = rgId;
	}
	/**
	 * 获取：新大区编号
	 */
	public Integer getRgId() {
		return rgId;
	}
	/**
	 * 设置：order_id
	 */
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	/**
	 * 获取：order_id
	 */
	public Integer getOrderId() {
		return orderId;
	}
	/**
	 * 设置：省可维护状态，1不可维护，2可维护
	 */
	public void setMaintainState(Integer maintainState) {
		this.maintainState = maintainState;
	}
	/**
	 * 获取：省可维护状态，1不可维护，2可维护
	 */
	public Integer getMaintainState() {
		return maintainState;
	}
}
