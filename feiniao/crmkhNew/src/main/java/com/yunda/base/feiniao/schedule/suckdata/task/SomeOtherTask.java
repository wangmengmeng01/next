package com.yunda.base.feiniao.schedule.suckdata.task;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunda.base.common.task.TaskAbs;
import com.yunda.base.feiniao.schedule.suckdata.service.SomeOtherTaskService;
import com.yunda.base.feiniao.schedule.suckdata.vo.TaskParams;

@Component
/**
 * 大客户返利（财务用）定时任务
 * 
 */
public class SomeOtherTask extends TaskAbs{
	
	@Autowired
	SomeOtherTaskService someOtherTaskService;

	@Override
	public void run(JobExecutionContext arg0) {
		TaskParams tp = TaskParams.newInstance(arg0);

		// { "fromDay": 0 ,"toDay": 0 ,"targetClass":"com.bootdo.portrait.task.suck.SuckDataJob" }
		for (int i = tp.getFromDay(); i <= tp.getToDay(); i++) {
			// 执行指定日的抽数
			someOtherTaskService.processSuck(i);
		}

		
	}

	@Override
	public String whoareyou() {
		return "重新生成大客户返利（财务用）";
	}

}
