package com.yunda.base.feiniao.schedule.suckdata.task.impls.khlj;

import java.util.Date;

import com.yunda.base.feiniao.log.enums.LogSuckTypeEnum;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.utils.TaskBeanUtils;

//抽数 - 客户揽件报表 - 按日生成全国时间段汇总表

public class KhljByCountry extends KhljSuckDataTaskAbs {
	@Override
	public int index() {
		// 排序，越大优先度越高. 根据依赖关系调整优先顺序
		return 1;
	}

	@Override
	public boolean preCheck(LogSuckdataService logSuckdataService,
			Date targetDay) {
		return true;
	}
	
	@Override
	public String realProcess(LogSuckdataService logSuckdataService,Date targetDay) {
		Object result=TaskBeanUtils.getReportTotaldataDao().saveLjByAll(targetDay);
		return "时间段揽件数据生成"+result!=null?result+"":0+"条";
	}

	@Override
	public void realClearTable(LogSuckdataService logSuckdataService,Date targetDay) {
		TaskBeanUtils.getReportTotaldataDao().removeLjByAll(targetDay);
	}

	@Override
	public void realClearRedis(LogSuckdataService logSuckdataService,Date targetDay) {
		// TODO 清理查询产生的redis缓存
	}

	@Override
	public int whichLogType() {
		return LogSuckTypeEnum.khlj_country.getCode();
	}

	@Override
	public String whoareyou() {
		return "客户揽件报表 - 按日生成全国时间段汇总表";
	}

	@Override
	public String cacheKeyPerfix() {
		// TODO Auto-generated method stub
		return null;
	}

}