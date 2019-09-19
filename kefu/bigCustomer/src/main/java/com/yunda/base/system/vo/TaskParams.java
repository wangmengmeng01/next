package com.yunda.base.system.vo;

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
	// 0为本月，-1为上月，-2为上上月，以此类推
	private int targetMonth = 0;
	// 运行方式，force强制，true强制false非强制
	private boolean force = false;
	// 任务策略（集群中的活跃个数，小于1标识所有节点都活跃）
	private int jobPolicy;

	public TaskParams() {

	}

	public static TaskParams newInstance(JobExecutionContext arg0) {
		TaskParams obj = null;

		JobDataMap dataMap = arg0.getJobDetail().getJobDataMap();
		// 提供形如 的扩展字符串
		if (dataMap != null && dataMap.containsKey("extData")) {
			String extData = dataMap.getString("extData");
			if (StringUtils.isNotBlank(extData)) {
				obj = JSON.parseObject(extData, TaskParams.class);
			}
		}
		if (obj == null) {
			obj = new TaskParams();
		}

		if (dataMap != null && dataMap.containsKey("jobPolicy")) {
			Integer jobPolicy = dataMap.getInt("jobPolicy");
			if (jobPolicy != null) {
				obj.setJobPolicy(jobPolicy);
			}
		}

		return obj;
	}

	public int getJobPolicy() {
		return jobPolicy;
	}

	public void setJobPolicy(int jobPolicy) {
		this.jobPolicy = jobPolicy;
	}

	public int getTargetMonth() {
		return targetMonth;
	}

	public void setTargetMonth(int targetMonth) {
		this.targetMonth = targetMonth;
	}

	public String getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
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
