package com.yunda.base.feiniao.schedule.suckdata.task.impls.khje;

import java.util.Date;
import java.util.HashMap;

import com.yunda.base.feiniao.log.enums.LogSuckTypeEnum;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.utils.TaskBeanUtils;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;

//抽数 - 客户金额报表 - 按日城市金额合计

public class KhjeByCity extends KhjeSuckDataTaskAbs {
	
	@Override
	public boolean preCheck(LogSuckdataService logSuckdataService,
			Date targetDay) {
		return true;
	}
	
	@Override
	public void realClearTable(LogSuckdataService logSuckdataService,Date targetDay) {
		TaskBeanUtils.getReportTotaldataDao().removeJeByCity(targetDay);
//		TaskBeanUtils.getReportTotaldataDao().removeJeByCityPriceSum(targetDay);
	}

	@Override
	public void realClearRedis(LogSuckdataService logSuckdataService,Date targetDay) {
		// TODO 清理查询产生的redis缓存
	}

	@Override
	public String realProcess(LogSuckdataService logSuckdataServic,Date targetDay) {
		String startDay = DateUtils.getMonthBegin(DateUtils.formatDate(targetDay));

		HashMap<String, Object> targetDate = new HashMap<String, Object>();
		targetDate.put("startDay", startDay);
		targetDate.put("targetDay", targetDay);
		TaskBeanUtils.getReportTotaldataDao().saveJeByCity(targetDate);
//		TaskBeanUtils.getReportTotaldataDao().saveJeByCityPriceSum(targetDay);
		return "抽取了城市汇总金额";
	}

	@Override
	// 排序，越大优先度越高. 根据依赖关系调整优先顺序
	public int index() {
		// TODO 优先度
		return 4;
	}

	@Override
	public String whoareyou() {
		return "客户金额报表 - 按日城市金额合计";
	}

	@Override
	public int whichLogType() {
		return LogSuckTypeEnum.khje_city.getCode();
	}

	@Override
	public String cacheKeyPerfix() {
		// TODO Auto-generated method stub
		return null;
	}

}
