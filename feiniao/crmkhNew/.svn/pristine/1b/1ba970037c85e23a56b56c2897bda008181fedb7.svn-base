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
public class ExportWarningBenchmarkDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//序号
	@ExcelField(title = "序号", order = 1)
	private Long id;
	//机构名称
	@ExcelField(title = "机构名称", order = 2)
	private String jgmc;
	//预警客户类型基准
	@ExcelField(title = "预警客户类型基准", order = 3)
	private String benchmarkSetting;
	//修改时间
	@ExcelField(title = "修改时间", order = 4)
	private String updateDate;
	//修改人
	@ExcelField(title = "修改人", order = 5)
	private String updateName;

	
	
	

/*	public String getShowBenchmarkSetting() {
	 if("1".equals(benchmarkSetting)){
		showBenchmarkSetting =  "B(50-200票)";
   	 }else if("2".equals(benchmarkSetting)){
   		showBenchmarkSetting =  "C(200-1000票)";
   	 }else if("3".equals(benchmarkSetting)){
   		showBenchmarkSetting =  "D(1000-2000票)";
   	 }else if("4".equals(benchmarkSetting)){
   		showBenchmarkSetting =  "E(2000-3000票)";
   	 }else if("5".equals(benchmarkSetting)){
   		showBenchmarkSetting =  "F(3000-5000票)";
   	 }else if("6".equals(benchmarkSetting)){
   		showBenchmarkSetting =  "G(5000票以上)";
   	 }
		return showBenchmarkSetting;
	}
	public void setShowBenchmarkSetting(String showBenchmarkSetting) {
		this.showBenchmarkSetting = showBenchmarkSetting;
	}*/
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
	 * 设置：修改时间
	 */
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * 获取：修改时间
	 */
	public String getUpdateDate() {
		return updateDate;
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
