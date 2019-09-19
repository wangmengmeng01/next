package com.yunda.base.feiniao.schedule.suckdata.task;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunda.base.common.task.TaskAbs;
import com.yunda.base.feiniao.schedule.suckdata.service.SuckDataService;
import com.yunda.base.feiniao.schedule.suckdata.vo.TaskParams;

@Component
/**
 * 抽数，从gp推来的客户揽件总表生成飞鸟需要的各种报表
 * 
 * @author Administrator
 *
 */
public class SuckDataJob extends TaskAbs {
	private static final Logger log = LoggerFactory
			.getLogger(SuckDataJob.class);

	@Autowired
	SuckDataService suckDataService;

	@Override
	public void run(JobExecutionContext arg0) {
		TaskParams tp = TaskParams.newInstance(arg0);

		// { "fromDay": 0 ,"toDay": 0 ,"targetClass":"com.bootdo.portrait.task.suck.SuckDataJob" }
		for (int i = tp.getFromDay(); i <= tp.getToDay(); i++) {
			// 执行指定日的抽数
			suckDataService.processSuck(i);
		}

		// 执行指定日的抽数
		// suckDataService.processSuck(tp.getTargetDay());
	}

	@Override
	public String whoareyou() {
		return "从客户揽件总表抽数";
	}
}