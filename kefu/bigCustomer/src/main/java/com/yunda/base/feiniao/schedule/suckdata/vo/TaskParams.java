package com.yunda.base.feiniao.schedule.suckdata.vo;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import com.alibaba.fastjson.JSON;

//task控制参数
public class TaskParams {
	// 目标类的全路径
	private String targetClass;
	// 0为当天，-1为昨天，-2为前天，以此类推
	private int targetDay = 0;
	// 运行方式，force强制，true强制false非强制
	private boolean force = false;
	
	//抽数从哪天,0为当天，-1为昨天，-2为前天，以此类推
	private int fromDay=0;
	//抽数到哪天,0为当天，-1为昨天，-2为前天，以此类推
	private int toDay=0;
	

	public TaskParams() {

	}

	public static TaskParams newInstance(JobExecutionContext arg0) {
		JobDataMap dataMap = arg0.getJobDetail().getJobDataMap();
		// 提供形如 的扩展字符串
		if (dataMap != null && dataMap.containsKey("extData")) {
			String extData = dataMap.getString("extData");
			if (StringUtils.isNotBlank(extData)) {
				return JSON.parseObject(extData, TaskParams.class);
			}
		}
		return new TaskParams();
	}

	public String getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}

	public int getFromDay() {
		return fromDay;
	}

	public void setFromDay(int fromDay) {
		this.fromDay = fromDay;
	}

	public int getToDay() {
		return toDay;
	}

	public void setToDay(int toDay) {
		this.toDay = toDay;
	}

	public int getTargetDay() {
		return targetDay;
	}

	public void setTargetDay(int targetDay) {
		this.targetDay = targetDay;
	}

	public boolean isForce() {
		return force;
	}

	public void setForce(boolean force) {
		this.force = force;
	}

}
