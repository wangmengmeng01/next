package com.yunda.base.feiniao.report.domain;

import java.io.Serializable;
import java.util.Date;

import com.github.crab2died.annotation.ExcelField;


/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-31 10:34:48
 */
public class GpBasSCustPickTmpDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//揽件网点编码
	@ExcelField(title = "揽件网点编码", order = 1)
	private Integer pickBrchCd;
	//揽件日期
	@ExcelField(title = "揽件日期", order = 2)
	private Date pickDt;
	//客户编码
	@ExcelField(title = "客户编码", order = 3)
	private String custCd;
	//客户来源;1菜鸟2二维码3普通
	@ExcelField(title = "客户来源;1菜鸟2二维码3普通", order = 4)
	private String custSrc;
	//揽件量
	@ExcelField(title = "揽件量", order = 5)
	private Long pickCnt;

 

	/**
	 * 设置：揽件网点编码
	 */
	public void setPickBrchCd(Integer pickBrchCd) {
		this.pickBrchCd = pickBrchCd;
	}
	/**
	 * 获取：揽件网点编码
	 */
	public Integer getPickBrchCd() {
		return pickBrchCd;
	}
	/**
	 * 设置：揽件日期
	 */
	public void setPickDt(Date pickDt) {
		this.pickDt = pickDt;
	}
	/**
	 * 获取：揽件日期
	 */
	public Date getPickDt() {
		return pickDt;
	}
	/**
	 * 设置：客户编码
	 */
	public void setCustCd(String custCd) {
		this.custCd = custCd;
	}
	/**
	 * 获取：客户编码
	 */
	public String getCustCd() {
		return custCd;
	}
	/**
	 * 设置：客户来源;1菜鸟2二维码3普通
	 */
	public void setCustSrc(String custSrc) {
		this.custSrc = custSrc;
	}
	/**
	 * 获取：客户来源;1菜鸟2二维码3普通
	 */
	public String getCustSrc() {
		return custSrc;
	}
	/**
	 * 设置：揽件量
	 */
	public void setPickCnt(Long pickCnt) {
		this.pickCnt = pickCnt;
	}
	/**
	 * 获取：揽件量
	 */
	public Long getPickCnt() {
		return pickCnt;
	}
}
