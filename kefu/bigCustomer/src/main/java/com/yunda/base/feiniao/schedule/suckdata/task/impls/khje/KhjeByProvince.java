package com.yunda.base.feiniao.schedule.suckdata.task.impls.khje;

import java.util.Date;

import com.yunda.base.feiniao.log.enums.LogSuckTypeEnum;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.utils.TaskBeanUtils;

//抽数 - 客户金额报表 - 按日省份金额合计

public class KhjeByProvince extends KhjeSuckDataTaskAbs {
	
	@Override
	public boolean preCheck(LogSuckdataService logSuckdataService,
			Date targetDay) {
		return true;
	}
	
	@Override
	public void realClearTable(LogSuckdataService logSuckdataService,Date targetDay) {
		TaskBeanUtils.getReportTotaldataDao().removeJeByProvince(targetDay);
//		TaskBeanUtils.getReportTotaldataDao().removeJeByProvincePriceSum(targetDay);
	}

	@Override
	public void realClearRedis(LogSuckdataService logSuckdataService,Date targetDay) {
		// TODO 清理查询产生的redis缓存
	}

	@Override
	public String realProcess(LogSuckdataService logSuckdataService,Date targetDay) {
		TaskBeanUtils.getReportTotaldataDao().saveJeByProvince(targetDay);
//		TaskBeanUtils.getReportTotaldataDao().saveJeByProvincePriceSum(targetDay);
		return "抽取了省份汇总金额";
	}

	@Override
	// 排序，越大优先度越高. 根据依赖关系调整优先顺序
	public int index() {
		return 3;
	}

	@Override
	public String whoareyou() {
		return "客户金额报表 - 按日省份金额合计";
	}

	@Override
	public int whichLogType() {
		return LogSuckTypeEnum.khje_province.getCode();
	}

	@Override
	public String cacheKeyPerfix() {
		// TODO Auto-generated method stub
		return null;
	}

}
