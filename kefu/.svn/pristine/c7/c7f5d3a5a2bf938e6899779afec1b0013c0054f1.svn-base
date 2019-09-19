package com.yunda.base.feiniao.schedule.suckdata.task;

import com.yunda.base.common.task.TaskAbs;
import com.yunda.base.feiniao.schedule.suckdata.service.SuckDataYJWEEKService;
import com.yunda.base.feiniao.schedule.suckdata.vo.TaskParams;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/**
 * 抽数，预警表改成按周
 * (因业务需求  预警表数据由 
 * 本月平均/上月平均<0.5
 * 改成 本周平均/上周平均<0.5)
 * 该job是按周生成上周的基础数据表  为预警表提供服务
 * @author Administrator
 *
 */
public class SuckDataYJWEEKJob extends TaskAbs {
	private static final Logger log = LoggerFactory
			.getLogger(SuckDataYJWEEKJob.class);

	@Autowired
	SuckDataYJWEEKService suckDataYJWEEKService;

	@Override
	public void run(JobExecutionContext arg0) {
		TaskParams tp = TaskParams.newInstance(arg0);

		for (int i = tp.getFromDay(); i <= tp.getToDay(); i++) {
			// 执行指定日的抽数
			suckDataYJWEEKService.processSuck(i);
		}

		// 执行指定日的抽数
		// suckDataService.processSuck(tp.getTargetDay());
	}

	@Override
	public String whoareyou() {
		return "预警表按周生成基础数据";
	}
}