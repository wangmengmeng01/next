package com.yunda.base.feiniao.schedule.suckdata.task.impls.khlj;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.yunda.base.feiniao.log.enums.LogSuckTypeEnum;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.utils.TaskBeanUtils;

//抽数 - 客户揽件报表 - 按日生成公司汇总表

public class KhljByGs extends KhljSuckDataTaskAbs {
	@Override
	public int index() {
		// 排序，越大优先度越高. 根据依赖关系调整优先顺序
		return 6;
	}

	@Override
	public boolean preCheck(LogSuckdataService logSuckdataService,
			Date targetDay) {
		return true;
	}
	
	@Override
	public String realProcess(LogSuckdataService logSuckdataService,Date targetDay) {
		Object result=TaskBeanUtils.getReportTotaldataDao().saveLjByGs(targetDay);
		return "公司揽件数据公司汇总生成"+result!=null?result+"":0+"条";
	}

	@Override
	public void realClearTable(LogSuckdataService logSuckdataService,Date targetDay) {		
		TaskBeanUtils.getReportTotaldataDao().removeLjByGs(targetDay);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void realClearRedis(LogSuckdataService logSuckdataService,Date targetDay) {
		Collection<Object> persons = new ArrayList<Object>();
		persons.addAll(TaskBeanUtils.getRedisTemplate().keys("queryBigareaTotalInfo"+"*"));
		TaskBeanUtils.getRedisTemplate().delete(persons);
		}

	@Override
	public int whichLogType() {
		return LogSuckTypeEnum.khlj_wd.getCode();
	}

	@Override
	public String whoareyou() {
		return "客户揽件报表 - 按日生成公司汇总表";
	}

	@Override
	public String cacheKeyPerfix() {
		// TODO Auto-generated method stub
		return null;
	}

}