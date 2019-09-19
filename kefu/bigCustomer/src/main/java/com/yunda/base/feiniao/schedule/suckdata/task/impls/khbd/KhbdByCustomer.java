package com.yunda.base.feiniao.schedule.suckdata.task.impls.khbd;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.yunda.base.feiniao.log.enums.LogSuckTypeEnum;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.utils.TaskBeanUtils;
import com.yunda.base.feiniao.schedule.suckdata.enums.SuckCacheKeyPerfixEnum;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
/**
 * 2018-08-01开始跑的task
 * @author admin
 *
 */
public class KhbdByCustomer extends KhbdSuckDataTaskAbs {
	
	@Override
	public boolean preCheck(LogSuckdataService logSuckdataService,
			Date targetDay) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟 
		//String dateEnd = sdf.format(targetDay);
		  java.util.Date dateNew = null ;
		try {
			dateNew = sdf.parse("2018-08-01");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
        return !targetDay.before(dateNew);
	}
	
	@Override
	/**
	 * 每日和主task一起执行的定时任务
	 */
	public String realProcess(LogSuckdataService logSuckdataService,Date targetDay) {
		String searchMonth = null;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
	    String targetday = sdf.format(targetDay);
		if(org.apache.commons.lang3.StringUtils.isNotEmpty(targetday)){
			searchMonth = DateUtils.format(targetDay, "yyyyMM");
        }
		//String searchMouth=targetDay.toString().substring(0,6);
		HashMap<String, Object> taskParamMap = new HashMap<String, Object>();
		taskParamMap.put("targetDay", targetday);
		taskParamMap.put("searchMonth", searchMonth);
		Object result=TaskBeanUtils.getReportFluctuateDao().saveBdByCustomer(taskParamMap);
		return "按日客户波动基础数据生成"+result!=null?result+"":0+"条";
	}

	
	@Override
	public void realClearTable(LogSuckdataService logSuckdataService,
			Date targetDay) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
	    String dqday = sdf.format(targetDay);
		if(org.apache.commons.lang3.StringUtils.isNotEmpty(dqday)){
			TaskBeanUtils.getReportFluctuateDao().removeBdByCustomer(targetDay);
		}
		
	}
	
	
	

	

	@Override
	public void realClearRedis(LogSuckdataService logSuckdataService,
			Date targetDay) {
		// 清理查询产生的缓存
		
		
	}

	@Override
	public int whichLogType() {
		return LogSuckTypeEnum.khbd_cutomer.getCode();
	}

	@Override
	public String whoareyou() {
		return "客户波动情况 — 按日生成客户波动基础数据";
	}
	@Override
	public int index() {
		// 排序，越大优先度越高. 根据依赖关系调整优先顺序
		return 1;
	}

	@Override
	public String cacheKeyPerfix() {
		return SuckCacheKeyPerfixEnum.bodong.getCode();
	}


}
