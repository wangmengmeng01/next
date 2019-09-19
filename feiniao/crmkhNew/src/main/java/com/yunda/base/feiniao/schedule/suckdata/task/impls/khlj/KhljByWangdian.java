package com.yunda.base.feiniao.schedule.suckdata.task.impls.khlj;

import com.yunda.base.common.config.Constant;
import com.yunda.base.feiniao.log.enums.LogSuckTypeEnum;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.utils.TaskBeanUtils;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
//抽数 - 客户揽件报表 - 按日生成网点汇总表

public class KhljByWangdian extends KhljSuckDataTaskAbs {
	@Override
	public int index() {
		// 排序，越大优先度越高. 根据依赖关系调整优先顺序
		return 5;
	}

	@Override
	public boolean preCheck(LogSuckdataService logSuckdataService,
			Date targetDay) {
		return true;
	}
	
	@Override
	public String realProcess(LogSuckdataService logSuckdataService,Date targetDay) {
		Object result = null;
		//查询日期为2019.1.1日前的，按照老逻辑，2019.1.1日后的（if(true)），按照新逻辑
		if(!targetDay.before(DateUtils.parseDate(Constant.NEWBEGINRUNTIME))){
 			result=TaskBeanUtils.getReportTotaldataDao().saveLjByWangdianNew(targetDay);
		}else{
			result=TaskBeanUtils.getReportTotaldataDao().saveLjByWangdian(targetDay);
		}
		
		return "公司明细揽件数据公司汇总生成"+result!=null?result+"":0+"条";
	}

	@Override
	public void realClearTable(LogSuckdataService logSuckdataService,Date targetDay) {
		TaskBeanUtils.getReportTotaldataDao().removeLjByWangdian(targetDay);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void realClearRedis(LogSuckdataService logSuckdataService,Date targetDay) {
		Collection<Object> persons = new ArrayList<Object>();
		persons.addAll(TaskBeanUtils.getRedisTemplate().keys("queryProvinceTotalInfo"+"*"));
		TaskBeanUtils.getRedisTemplate().delete(persons);
		}

	@Override
	public int whichLogType() {
		return LogSuckTypeEnum.khlj_wd.getCode();
	}

	@Override
	public String whoareyou() {
		return "客户揽件报表 - 按日生成网点汇总表";
	}

	@Override
	public String cacheKeyPerfix() {
		// TODO Auto-generated method stub
		return null;
	}

}