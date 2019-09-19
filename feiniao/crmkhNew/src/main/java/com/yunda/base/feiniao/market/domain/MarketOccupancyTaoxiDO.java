package com.yunda.base.feiniao.market.domain;

import java.io.Serializable;
import java.util.Date;
import com.github.crab2died.annotation.ExcelField;


/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-07150734
 */
public class MarketOccupancyTaoxiDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//报表类型
	private String timerType;
	//日期
	private String showQuDate;
	//序号
	@ExcelField(title = "序号", order = 1)
	private Long id;
	//日期
	@ExcelField(title = "日期", order = 2)
	private Date quDate;
	//总计(单位:万)
	@ExcelField(title = "总计(单位:万)", order = 3)
	private Double totalSum;
	//韵达-揽件量(万)
	@ExcelField(title = "韵达-揽件量(万)", order = 4)
	private Double ydSum;
	//韵达占比(%)
	@ExcelField(title = "韵达占比(%)", order = 5)
	private Double ydRatio;
	//中通-揽件量(万)
	@ExcelField(title = "中通-揽件量(万)", order = 6)
	private Double ztoSum;
	//中通占比(%)
	@ExcelField(title = "中通占比(%)", order = 7)
	private Double ztoRatio;
	//圆通-揽件量(万)
	@ExcelField(title = "圆通-揽件量(万)", order = 8)
	private Double ytoSum;
	//圆通占比(%)
	@ExcelField(title = "圆通占比(%)", order = 9)
	private Double ytoRatio;
	//申通-揽件量(万)
	@ExcelField(title = "申通-揽件量(万)", order = 10)
	private Double stoSum;
	//申通占比(%)
	@ExcelField(title = "申通占比(%)", order = 11)
	private Double stoRatio;
	//百世-揽件量(万)
	@ExcelField(title = "百世-揽件量(万)", order = 12)
	private Double bestexSum;
	//百世占比(%)
	@ExcelField(title = "百世占比(%)", order = 13)
	private Double bestexRatio;

 

	public String getTimerType() {
		return timerType;
	}
	public void setTimerType(String timerType) {
		this.timerType = timerType;
	}
	public String getShowQuDate() {
		return showQuDate;
	}
	public void setShowQuDate(String showQuDate) {
		this.showQuDate = showQuDate;
	}
	/**
	 * 设置：序号
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：序号
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：日期
	 */
	public void setQuDate(Date quDate) {
		this.quDate = quDate;
	}
	/**
	 * 获取：日期
	 */
	public Date getQuDate() {
		return quDate;
	}
	/**
	 * 设置：总计(单位:万)
	 */
	public void setTotalSum(Double totalSum) {
		this.totalSum = totalSum;
	}
	/**
	 * 获取：总计(单位:万)
	 */
	public Double getTotalSum() {
		return totalSum;
	}
	/**
	 * 设置：韵达-揽件量(万)
	 */
	public void setYdSum(Double ydSum) {
		this.ydSum = ydSum;
	}
	/**
	 * 获取：韵达-揽件量(万)
	 */
	public Double getYdSum() {
		return ydSum;
	}
	/**
	 * 设置：韵达占比(%)
	 */
	public void setYdRatio(Double ydRatio) {
		this.ydRatio = ydRatio;
	}
	/**
	 * 获取：韵达占比(%)
	 */
	public Double getYdRatio() {
		return ydRatio;
	}
	/**
	 * 设置：中通-揽件量(万)
	 */
	public void setZtoSum(Double ztoSum) {
		this.ztoSum = ztoSum;
	}
	/**
	 * 获取：中通-揽件量(万)
	 */
	public Double getZtoSum() {
		return ztoSum;
	}
	/**
	 * 设置：中通占比(%)
	 */
	public void setZtoRatio(Double ztoRatio) {
		this.ztoRatio = ztoRatio;
	}
	/**
	 * 获取：中通占比(%)
	 */
	public Double getZtoRatio() {
		return ztoRatio;
	}
	/**
	 * 设置：圆通-揽件量(万)
	 */
	public void setYtoSum(Double ytoSum) {
		this.ytoSum = ytoSum;
	}
	/**
	 * 获取：圆通-揽件量(万)
	 */
	public Double getYtoSum() {
		return ytoSum;
	}
	/**
	 * 设置：圆通占比(%)
	 */
	public void setYtoRatio(Double ytoRatio) {
		this.ytoRatio = ytoRatio;
	}
	/**
	 * 获取：圆通占比(%)
	 */
	public Double getYtoRatio() {
		return ytoRatio;
	}
	/**
	 * 设置：申通-揽件量(万)
	 */
	public void setStoSum(Double stoSum) {
		this.stoSum = stoSum;
	}
	/**
	 * 获取：申通-揽件量(万)
	 */
	public Double getStoSum() {
		return stoSum;
	}
	/**
	 * 设置：申通占比(%)
	 */
	public void setStoRatio(Double stoRatio) {
		this.stoRatio = stoRatio;
	}
	/**
	 * 获取：申通占比(%)
	 */
	public Double getStoRatio() {
		return stoRatio;
	}
	/**
	 * 设置：百世-揽件量(万)
	 */
	public void setBestexSum(Double bestexSum) {
		this.bestexSum = bestexSum;
	}
	/**
	 * 获取：百世-揽件量(万)
	 */
	public Double getBestexSum() {
		return bestexSum;
	}
	/**
	 * 设置：百世占比(%)
	 */
	public void setBestexRatio(Double bestexRatio) {
		this.bestexRatio = bestexRatio;
	}
	/**
	 * 获取：百世占比(%)
	 */
	public Double getBestexRatio() {
		return bestexRatio;
	}
}
