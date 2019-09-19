package com.yunda.base.feiniao.schedule.suckdata.task;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunda.base.common.task.TaskAbs;
import com.yunda.base.feiniao.schedule.suckdata.service.MarketTaoxiTaskService;
import com.yunda.base.feiniao.schedule.suckdata.service.SuckDataYJWEEKService;
import com.yunda.base.feiniao.schedule.suckdata.vo.TaskParams;
@Component
/**
 * this is a gaoxiao task
 * @author admin
 *
 */
public class MarketTaoxiAddJob extends TaskAbs{

	private static final Logger log = LoggerFactory.getLogger(MarketTaoxiAddJob.class);
	@Autowired
	MarketTaoxiTaskService marketTaoxiTaskService;
	//@Autowired
	//SuckDataYJWEEKService marketTaoxiAddService;
	@Override
	public void run(JobExecutionContext arg0) {
		TaskParams tb = TaskParams.newInstance(arg0);
		
		for(int i=tb.getFromDay(); i<=tb.getToDay();i++){
			marketTaoxiTaskService.taoxiAdd(i);
		}
	}

	@Override
	public String whoareyou() {
		return "淘系市场占有率日报--每日生成一条";
	}

}
