package com.yunda.base.feiniao.schedule.suckdata.task;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunda.base.common.task.TaskAbs;
import com.yunda.base.feiniao.schedule.suckdata.service.SuckDataYJMonthService;
import com.yunda.base.feiniao.schedule.suckdata.vo.TaskParams;

@Component
/**
 * 抽数，波动表月基础数据
 * @author Administrator
 *
 */
public class SuckDataYJMonthJob extends TaskAbs {
	private static final Logger log = LoggerFactory
			.getLogger(SuckDataYJMonthJob.class);

	@Autowired
	SuckDataYJMonthService suckDataYJMonthService;

	@Override
	public void run(JobExecutionContext arg0) {
		TaskParams tp = TaskParams.newInstance(arg0);

		for (int i = tp.getFromDay(); i <= tp.getToDay(); i++) {
			// 执行指定日的抽数
			suckDataYJMonthService.processSuck(i);
		}

		// 执行指定日的抽数
		// suckDataService.processSuck(tp.getTargetDay());
	}

	@Override
	public String whoareyou() {
		return "波动表按月生成基础数据";
	}
}