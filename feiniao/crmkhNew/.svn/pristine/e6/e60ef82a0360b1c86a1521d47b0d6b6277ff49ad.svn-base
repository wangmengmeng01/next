package com.yunda.base.feiniao.schedule.suckdata.task.impls.khje;

import com.yunda.base.feiniao.log.enums.LogSuckTypeEnum;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.utils.TaskBeanUtils;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;

import java.util.Date;
import java.util.HashMap;

//抽数 - 客户金额报表 - 按日大区金额合计

public class KhjeByArea extends KhjeSuckDataTaskAbs {
	
	@Override
	public boolean preCheck(LogSuckdataService logSuckdataService,
			Date targetDay) {
		return true;
	}
	
	@Override
	public String realProcess(LogSuckdataService logSuckdataService,Date targetDay) {
		TaskBeanUtils.getReportTotaldataDao().saveJeByBigArea(targetDay);
//		TaskBeanUtils.getReportTotaldataDao().saveJeByBigAreaPriceSum(targetDay);
		return "抽取了大区汇总金额";
	}

	@Override
	// 排序，越大优先度越高. 根据依赖关系调整优先顺序
	public int index() {
		// TODO 优先度
		return 2;
	}

	@Override
	public void realClearTable(LogSuckdataService logSuckdataService,Date targetDay) {
		TaskBeanUtils.getReportTotaldataDao().removeJeByBigArea(targetDay);
		/*HashMap<String,Object> map = new HashMap<String,Object>();
		String date = DateUtils.format(targetDay);
		map.put("yone", DateUtils.getMonthBegin(date));
		map.put("ytwo", DateUtils.getMonthEnd(date));

		TaskBeanUtils.getReportTotaldataDao().removeJeByCustomerStatsSJD2(map);
		TaskBeanUtils.getReportTotaldataDao().removeJeByGsPriceSum(map);
		TaskBeanUtils.getReportTotaldataDao().removeJeByCityPriceSum(map);
		TaskBeanUtils.getReportTotaldataDao().removeJeByProvincePriceSum(map);
		TaskBeanUtils.getReportTotaldataDao().removeJeByBigAreaPriceSum(map);
		TaskBeanUtils.getReportTotaldataDao().removeJeByTimePriceSum(map);*/
		
		//TaskBeanUtils.getReportTotaldataDao().removeJeByCustomerSJD(map);
		/*TaskBeanUtils.getReportTotaldataDao().removeJeByWangdianSJD(map);
		TaskBeanUtils.getReportTotaldataDao().removeJeByCitySJD(map);
		TaskBeanUtils.getReportTotaldataDao().removeJeByProvinceSJD(map);
		TaskBeanUtils.getReportTotaldataDao().removeJeByBigAreaSJD(map);
		TaskBeanUtils.getReportTotaldataDao().removeJeByTimeSJD(map);
		
		TaskBeanUtils.getReportTotaldataDao().removeTotalPriceConfig(map);*/
	}

	@Override
	public void realClearRedis(LogSuckdataService logSuckdataService,Date targetDay) {
	}

	@Override
	public String whoareyou() {
		return "客户金额报表 - 按日大区金额合计";
	}

	@Override
	public int whichLogType() {
		return LogSuckTypeEnum.khje_area.getCode();
	}

	@Override
	public String cacheKeyPerfix() {
		// TODO Auto-generated method stub
		return null;
	}

}
