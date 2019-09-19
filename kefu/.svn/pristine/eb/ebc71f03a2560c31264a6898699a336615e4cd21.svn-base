package com.yunda.base.feiniao.schedule.suckdata.task;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunda.base.common.task.TaskAbs;
import com.yunda.base.feiniao.schedule.suckdata.service.RunBasicalInfoByMonthService;
import com.yunda.base.feiniao.schedule.suckdata.vo.TaskParams;
@Component
/**
 * 每月末从基础信息表获取最新的区县市省大区信息，放入基础信息表（crmkhv2_regional_basic_information）
 *  
 * 
 */
public class RunBasicalInfoByMonth extends TaskAbs {
	@Autowired
	RunBasicalInfoByMonthService runBasicalInfoByMonthService;

	@Override
	public void run(JobExecutionContext arg0) {
		TaskParams tp = TaskParams.newInstance(arg0);

		// { "fromDay": 0 ,"toDay": 0 ,"targetClass":"com.bootdo.portrait.task.suck.SuckDataJob" }
		for (int i = tp.getFromDay(); i <= tp.getToDay(); i++) {
			// 执行指定日的抽数
			runBasicalInfoByMonthService.processSuck(i);
		}

		
	}

	@Override
	public String whoareyou() {
		return "生成每月基础信息数据（区县市省大区）";
	}

}
