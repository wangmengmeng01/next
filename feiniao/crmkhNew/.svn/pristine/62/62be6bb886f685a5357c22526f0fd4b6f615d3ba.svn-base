package com.yunda.base.feiniao.schedule.suckdata.task.impls.khbd;

import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.utils.TaskBeanUtils;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
/**
 * 2018-08-01之前跑的task
 * @author admin
 *
 */
public class KhbdByCustomerBefore20180801 extends KhbdSuckDataTaskAbs {

	@Override
	public int index() {
		// TODO Auto-generated method stub
		return 1;
	}

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
        //表示targetDay小于dateNew 返回true，执行此task
//2018-08-01开始，返回false，不执行此task
        return targetDay.before(dateNew);
		
	}

	@Override
	public String realProcess(LogSuckdataService logSuckdataService,
			Date targetDay) {
		String searchMonth = null;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
	    String targetday = sdf.format(targetDay);
		if(org.apache.commons.lang3.StringUtils.isNotEmpty(targetday)){
			searchMonth = DateUtils.format(targetDay, "yyyyMM");
        }
		//String searchMonth=targetDay.toString().substring(0,6);
		HashMap<String, Object> taskParamMap = new HashMap<String, Object>();
		taskParamMap.put("targetDay", targetday);
		taskParamMap.put("searchMonth", searchMonth);
		Object result=TaskBeanUtils.getReportFluctuateDao().saveBdByCustomerBefore20180801(taskParamMap);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public int whichLogType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String whoareyou() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String cacheKeyPerfix() {
		// TODO Auto-generated method stub
		return null;
	}

}
