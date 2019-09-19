package com.yunda.base.system.domain;

import com.github.crab2died.annotation.ExcelField;

import java.io.Serializable;


/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-08-21 16:04:31
 */
public class CityDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//市ID
	@ExcelField(title = "市ID", order = 1)
	private String cityid;
	//市名称
	@ExcelField(title = "市名称", order = 2)
	private String cityname;
	//所属省ID
	@ExcelField(title = "所属省ID", order = 3)
	private String provinceid;
	//区号
	@ExcelField(title = "区号", order = 4)
	private String areacode;

 

	/**
	 * 设置：市ID
	 */
	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	/**
	 * 获取：市ID
	 */
	public String getCityid() {
		return cityid;
	}
	/**
	 * 设置：市名称
	 */
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	/**
	 * 获取：市名称
	 */
	public String getCityname() {
		return cityname;
	}
	/**
	 * 设置：所属省ID
	 */
	public void setProvinceid(String provinceid) {
		this.provinceid = provinceid;
	}
	/**
	 * 获取：所属省ID
	 */
	public String getProvinceid() {
		return provinceid;
	}
	/**
	 * 设置：区号
	 */
	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}
	/**
	 * 获取：区号
	 */
	public String getAreacode() {
		return areacode;
	}
}
