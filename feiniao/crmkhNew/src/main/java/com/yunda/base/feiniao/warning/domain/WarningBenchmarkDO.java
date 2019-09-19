package com.yunda.base.feiniao.warning.domain;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import com.github.crab2died.annotation.ExcelField;


/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-03-06143800
 */
public class WarningBenchmarkDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//序号
	@ExcelField(title = "序号", order = 1)
	private Long id;
	//省ID
	@ExcelField(title = "省ID", order = 2)
	private String provinceid;
	//省名称
	@ExcelField(title = "省名称", order = 3)
	private String provincename;
	//机构名称
	@NotBlank(message="机构名称不能为空")
	@ExcelField(title = "机构名称", order = 4)
	private String jgmc;
	//预警客户类型基准
	@NotBlank(message="预警基准不能为空")
	@ExcelField(title = "预警客户类型基准", order = 5)
	private String benchmarkSetting;
	//大区
	@ExcelField(title = "大区", order = 6)
	private String bigarea;
	//修改时间
	@ExcelField(title = "修改时间", order = 7)
	private Date updateDate;
	//修改人id
	@ExcelField(title = "修改人id", order = 8)
	private String updateId;
	//修改人
	@ExcelField(title = "修改人", order = 9)
	private String updateName;

	@ExcelField(title = "页面显示预警基准", order = 10)
	private String showBenchmarkSetting;
	
	

	public String getShowBenchmarkSetting() {
	 if("b".equals(benchmarkSetting)){
		showBenchmarkSetting =  "B类";
   	 }else if("c".equals(benchmarkSetting)){
   		showBenchmarkSetting =  "C类";
   	 }else if("d".equals(benchmarkSetting)){
   		showBenchmarkSetting =  "D类";
   	 }else if("e".equals(benchmarkSetting)){
   		showBenchmarkSetting =  "E类";
   	 }else if("f".equals(benchmarkSetting)){
   		showBenchmarkSetting =  "F类";
   	 }else if("g".equals(benchmarkSetting)){
   		showBenchmarkSetting =  "G类";
   	 }
		return showBenchmarkSetting;
	}
	public void setShowBenchmarkSetting(String showBenchmarkSetting) {
		this.showBenchmarkSetting = showBenchmarkSetting;
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
	 * 设置：机构名称
	 */
	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}
	/**
	 * 获取：机构名称
	 */
	public String getJgmc() {
		return jgmc;
	}
	/**
	 * 设置：预警客户类型基准
	 */
	public void setBenchmarkSetting(String benchmarkSetting) {
		this.benchmarkSetting = benchmarkSetting;
	}
	/**
	 * 获取：预警客户类型基准
	 */
	public String getBenchmarkSetting() {
		return benchmarkSetting;
	}
	/**
	 * 设置：大区
	 */
	public void setBigarea(String bigarea) {
		this.bigarea = bigarea;
	}
	/**
	 * 获取：大区
	 */
	public String getBigarea() {
		return bigarea;
	}
	/**
	 * 设置：修改时间
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getUpdateDate() {
		return updateDate;
	}
	/**
	 * 设置：修改人id
	 */
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}
	/**
	 * 获取：修改人id
	 */
	public String getUpdateId() {
		return updateId;
	}
	/**
	 * 设置：修改人
	 */
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}
	/**
	 * 获取：修改人
	 */
	public String getUpdateName() {
		return updateName;
	}
}
