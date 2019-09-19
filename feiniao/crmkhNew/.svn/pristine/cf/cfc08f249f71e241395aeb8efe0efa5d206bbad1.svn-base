package com.yunda.base.feiniao.schedule.suckdata.task.impls.khje;

import java.util.Date;
import java.util.HashMap;

import com.yunda.base.common.config.Constant;
import com.yunda.base.feiniao.log.enums.LogSuckTypeEnum;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.dao.ReportTotaldataDao;
import com.yunda.base.feiniao.report.utils.TaskBeanUtils;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;

//抽数 - 客户金额报表 - 按日客户金额合计

public class KhjeByCustomer extends KhjeSuckDataTaskAbs {
	
	@Override
	public boolean preCheck(LogSuckdataService logSuckdataService,
			Date targetDay) {
		return true;
	}
	
	@Override
	public void realClearTable(LogSuckdataService logSuckdataService,Date targetDay) {

		TaskBeanUtils.getReportTotaldataDao().removeJeByCustomer(targetDay);
		//TaskBeanUtils.getReportTotaldataDao().removeJeByCustomerStats(targetDay);
	}

	@Override
	public void realClearRedis(LogSuckdataService logSuckdataService,Date targetDay) {
		// TODO 清理查询产生的redis缓存
	}

	@Override
	public String realProcess(LogSuckdataService logSuckdataService,Date targetDay) {
		Object result = null;
		//查询日期为2019.1.1日前的，按照老逻辑，2019.1.1日及以后的（if(true)），按照新逻辑
		if(!targetDay.before(DateUtils.parseDate(Constant.NEWBEGINRUNTIME))){
			HashMap<String,Object> map = readyMap(targetDay);
			result=TaskBeanUtils.getReportTotaldataDao().saveJeByCustomerNew(map);
		}else{
			result=TaskBeanUtils.getReportTotaldataDao().saveJeByCustomer(readyMap(targetDay));
		}
		//TaskBeanUtils.getReportTotaldataDao().saveJeByCustomerStats(targetDay);
		return "抽取了客户汇总数据"+result!=null?result+"":0+"条";
	}

	@Override
	// 排序，越大优先度越高. 根据依赖关系调整优先顺序
	public int index() {
		// TODO 优先度
		return 6;
	}

	@Override
	public String whoareyou() {
		return "客户金额报表 - 按日客户金额合计";
	}

	@Override
	public int whichLogType() {
		return LogSuckTypeEnum.khje_customer.getCode();
	}
	
	public HashMap<String,Object> readyMap(Date targetDay) {
		HashMap<String,Object> param =new HashMap<String,Object>();
		String yone =DateUtils.format(targetDay);
		String priceType = "y";
		if("02,03,04,05,06,07,08,".indexOf(yone.split("-")[1]) != -1) {
			priceType = "x";
		}
		String startDay = DateUtils.getMonthBegin(DateUtils.formatDate(targetDay));
		param.put("startDay", startDay);
		param.put("targetDay", targetDay);
		param.put("priceType", priceType);
		//一类省份id
		if(targetDay.before(DateUtils.parseDate(Constant.FIRST_PROVINCEIDSBEFORE2019_WORK))){
			param.put("firstProvinceids", Constant.FIRST_PROVINCEIDSBEFORE2019);
		}else{
			param.put("firstProvinceids", Constant.FIRST_PROVINCEIDS);
		}
		return param;
	}

	@Override
	public String cacheKeyPerfix() {
		// TODO Auto-generated method stub
		return null;
	}

}
