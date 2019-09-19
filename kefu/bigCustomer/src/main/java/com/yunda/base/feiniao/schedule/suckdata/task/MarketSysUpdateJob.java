package com.yunda.base.feiniao.schedule.suckdata.task;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunda.base.common.task.TaskAbs;
import com.yunda.base.feiniao.market.service.MarketSysUpdateService;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
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
		marketSysUpdateService.synUpdateData(DateUtils.addDate(new Date(), +1));
    }

	@Override
	public String whoareyou() {
		return "占有率基础数据生成";
	}
}