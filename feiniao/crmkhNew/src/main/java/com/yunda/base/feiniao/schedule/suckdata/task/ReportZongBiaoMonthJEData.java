package com.yunda.base.feiniao.schedule.suckdata.task;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunda.base.common.task.TaskAbs;
import com.yunda.base.feiniao.schedule.suckdata.service.ReportZongBiaoMonthJEDataService;
import com.yunda.base.feiniao.schedule.suckdata.vo.TaskParams;

@Component
public class ReportZongBiaoMonthJEData extends TaskAbs {
	
	@Autowired
	ReportZongBiaoMonthJEDataService reportZongBiaoMonthJEDataService;

	@Override
	public void run(JobExecutionContext arg0) {
		TaskParams tp = TaskParams.newInstance(arg0);

		// { "fromDay": 0 ,"toDay": 0 ,"targetClass":"com.bootdo.portrait.task.suck.SuckDataJob" }
		for (int i = tp.getFromDay(); i <= tp.getToDay(); i++) {
			// 执行指定日的抽数
			reportZongBiaoMonthJEDataService.processSuck(i);
		}

		
	}

	@Override
	public String whoareyou() {
		return "生成每月总表时间段数据（返利）";
	}


}
