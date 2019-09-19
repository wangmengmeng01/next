package com.yunda.base.feiniao.schedule.suckdata.task.impls.khlj;

import com.yunda.base.feiniao.log.enums.LogSuckTypeEnum;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.utils.TaskBeanUtils;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

//抽数 - 客户揽件报表 - 按日城市汇总

public class KhljByCity extends KhljSuckDataTaskAbs {

	@Override
	public int index() {
		// 排序，越大优先度越高. 根据依赖关系调整优先顺序
		return 4;
	}
	
	@Override
	public boolean preCheck(LogSuckdataService logSuckdataService,
			Date targetDay) {
		return true;
	}

	@Override
	public String realProcess(LogSuckdataService logSuckdataService,Date targetDay) {
		String startDay = DateUtils.getMonthBegin(DateUtils.formatDate(targetDay));
		HashMap<String, Object> targetDate = new HashMap<String, Object>();
		targetDate.put("targetDay", targetDay);
		targetDate.put("startDay", startDay);
		Object result=TaskBeanUtils.getReportTotaldataDao().saveLjByCity(targetDate);
		return "城市揽件数据生成"+result!=null?result+"":0+"条";
	}

	@Override
	public void realClearTable(LogSuckdataService logSuckdataService,Date targetDay) {
		TaskBeanUtils.getReportTotaldataDao().removeLjByCity(targetDay);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void realClearRedis(LogSuckdataService logSuckdataService,Date targetDay) {
		Collection<Object> persons = new ArrayList<Object>();
		persons.addAll(TaskBeanUtils.getRedisTemplate().keys("queryMultiProvinceTotalInfo"+"*"));
		TaskBeanUtils.getRedisTemplate().delete(persons);
	}

	@Override
	public int whichLogType() {
		return LogSuckTypeEnum.khlj_city.getCode();
	}

	@Override
	public String whoareyou() {
		return "客户揽件报表 - 按日城市汇总";
	}

	@Override
	public String cacheKeyPerfix() {
		// TODO Auto-generated method stub
		return null;
	}

}
