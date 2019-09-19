package com.yunda.base.feiniao.schedule.suckdata.task;

import com.yunda.base.common.task.TaskAbs;
import com.yunda.base.feiniao.schedule.suckdata.service.SuckDataKHBDService;
import com.yunda.base.feiniao.schedule.suckdata.vo.TaskParams;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/**
 * 1 波动表task
 * 2 为预警反馈表提供基础数据的task
 * @author Administrator
 *
 */
public class SuckDataKHBDJob extends TaskAbs {
	private static final Logger log = LoggerFactory
			.getLogger(SuckDataKHBDJob.class);

	@Autowired
	SuckDataKHBDService suckDataKHBDService;

	@Override
	public void run(JobExecutionContext arg0) {
		TaskParams tp = TaskParams.newInstance(arg0);

		// { "fromDay": 0 ,"toDay": 0 ,"targetClass":"com.bootdo.portrait.task.suck.SuckDataJob" }
		for (int i = tp.getFromDay(); i <= tp.getToDay(); i++) {
			// 执行指定日的抽数
			suckDataKHBDService.processSuck(i);
		}

		// 执行指定日的抽数
		// suckDataService.processSuck(tp.getTargetDay());
	}

	@Override
	public String whoareyou() {
		return "波动表和预警反馈表基础数据";
	}
}