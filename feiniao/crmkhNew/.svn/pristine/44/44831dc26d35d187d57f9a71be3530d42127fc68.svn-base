package com.yunda.base.feiniao.schedule.suckdata.task;

import com.yunda.base.common.task.TaskAbs;
import com.yunda.base.feiniao.market.service.MarketSysUpdateService;
import com.yunda.base.feiniao.schedule.suckdata.vo.TaskParams;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
/**
 * 抽数，market表
 * 
 * @author Administrator
 *
 */
public class MarketSysUpdateJob extends TaskAbs {
	@Autowired
	MarketSysUpdateService marketSysUpdateService;

	@Override
	public void run(JobExecutionContext arg0) {
		TaskParams tp = TaskParams.newInstance(arg0);

		// { "fromDay": 0 ,"toDay": 0 ,"targetClass":"com.bootdo.portrait.task.suck.SuckDataJob" }
		for (int i = tp.getFromDay(); i <= tp.getToDay(); i++) {
			// 执行指定日的抽数
			marketSysUpdateService.synUpdateData(i);
		}
		
    }

	@Override
	public String whoareyou() {
		return "占有率基础数据生成";
	}
}