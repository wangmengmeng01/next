package com.yunda.base.feiniao.schedule.suckdata.task.impls.khlj;

import com.yunda.base.feiniao.log.enums.LogSuckTypeEnum;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.utils.TaskBeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

//抽数 - 客户揽件报表 - 按日生成大区汇总表

public class KhljByArea extends KhljSuckDataTaskAbs {
	@Override
	public int index() {
		// 排序，越大优先度越高. 根据依赖关系调整优先顺序
		return 2;
	}

	@Override
	public boolean preCheck(LogSuckdataService logSuckdataService,
			Date targetDay) {
		return true;
	}
	
	@Override
	public String realProcess(LogSuckdataService logSuckdataService,Date targetDay) {
		Object result=TaskBeanUtils.getReportTotaldataDao().saveLjByBigArea(targetDay);
		return "大区揽件数据生成"+result!=null?result+"":0+"条";
	}

	@Override
	public void realClearTable(LogSuckdataService logSuckdataService,Date targetDay) {
		TaskBeanUtils.getReportTotaldataDao().removeLjByBigArea(targetDay);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void realClearRedis(LogSuckdataService logSuckdataService,Date targetDay) {
		Collection<Object> persons = new ArrayList<Object>();
		persons.addAll(TaskBeanUtils.getRedisTemplate().keys("queryBranchTotalInfo"+"*"));
		TaskBeanUtils.getRedisTemplate().delete(persons);
	}

	@Override
	public int whichLogType() {
		return LogSuckTypeEnum.khlj_area.getCode();
	}

	@Override
	public String whoareyou() {
		return "客户揽件报表 - 按日生成大区汇总表";
	}

	@Override
	public String cacheKeyPerfix() {
		// TODO Auto-generated method stub
		return null;
	}

}