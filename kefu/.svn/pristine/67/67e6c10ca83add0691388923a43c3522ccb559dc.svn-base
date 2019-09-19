package com.yunda.base.feiniao.schedule.suckdata.task.impls.khlj;

import com.yunda.base.feiniao.log.enums.LogSuckTypeEnum;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.utils.TaskBeanUtils;
import com.yunda.base.feiniao.schedule.suckdata.enums.SuckCacheKeyPerfixEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

//抽数 - 客户揽件报表 - 按日生成省份汇总表

public class KhljByProvince extends KhljSuckDataTaskAbs {
	@Override
	public int index() {
		// 排序，越大优先度越高. 根据依赖关系调整优先顺序
		return 3;
	}

	@Override
	public boolean preCheck(LogSuckdataService logSuckdataService,
			Date targetDay) {
		return true;
	}
	
	@Override
	public String realProcess(LogSuckdataService logSuckdataService,Date targetDay) {
		Object result=TaskBeanUtils.getReportTotaldataDao().saveLjByProvince(targetDay);
		return "省份揽件数据生成"+result!=null?result+"":0+"条";
	}

	@Override
	public void realClearTable(LogSuckdataService logSuckdataService,Date targetDay) {		
		TaskBeanUtils.getReportTotaldataDao().removeLjByProvince(targetDay);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void realClearRedis(LogSuckdataService logSuckdataService,Date targetDay) {
		Collection<Object> persons = new ArrayList<Object>();
		persons.addAll(TaskBeanUtils.getRedisTemplate().keys("queryCityTotalInfo"+"*"));
		TaskBeanUtils.getRedisTemplate().delete(persons);
	}

	@Override
	public int whichLogType() {
		return LogSuckTypeEnum.khlj_province.getCode();
	}

	@Override
	public String whoareyou() {
		return "客户揽件报表 - 按日生成省份汇总表";
	}

	@Override
	public String cacheKeyPerfix() {
		return SuckCacheKeyPerfixEnum.zongbiao.getCode();
	}

}