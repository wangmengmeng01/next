package com.yunda.base.feiniao.schedule.suckdata.domain;

import java.io.Serializable;
import java.util.Date;

import com.github.crab2died.annotation.ExcelField;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-21 23:55:47
 */
public class RecordSuckDO implements Serializable {
	private static final long serialVersionUID = 1L;

	// id
	@ExcelField(title = "id", order = 1)
	private Integer id;

	// 抽数目标日，int型yyyyMMdd
	@ExcelField(title = "抽数目标日，int型yyyyMMdd", order = 2)
	private Integer suckDate;

	// creat_time
	@ExcelField(title = "creat_time", order = 3)
	private Date creatTime = new Date();

	// 抽数结束数据的主键id
	@ExcelField(title = "抽数开始时候GP的数据条数", order = 5)
	private Long gpNums;

	// 删除，0正常-1删除
	@ExcelField(title = "删除，0正常-1删除", order = 6)
	private Integer delFlag;

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
	 * 设置：抽数目标日，int型yyyyMMdd
	 */
	public void setSuckDate(Integer suckDate) {
		this.suckDate = suckDate;
	}

	/**
	 * 获取：抽数目标日，int型yyyyMMdd
	 */
	public Integer getSuckDate() {
		return suckDate;
	}

	/**
	 * 设置：creat_time
	 */
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	/**
	 * 获取：creat_time
	 */
	public Date getCreatTime() {
		return creatTime;
	}

	public Long getGpNums() {
		return gpNums;
	}

	public void setGpNums(Long gpNums) {
		this.gpNums = gpNums;
	}

	/**
	 * 设置：删除，0正常-1删除
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	/**
	 * 获取：删除，0正常-1删除
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
}
