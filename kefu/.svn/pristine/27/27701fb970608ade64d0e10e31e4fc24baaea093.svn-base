package com.yunda.base.feiniao.schedule.suckdata.task.impls.khyj;

import com.yunda.base.common.config.Constant;
import com.yunda.base.feiniao.log.enums.LogSuckTypeEnum;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.utils.TaskBeanUtils;
import com.yunda.base.feiniao.schedule.suckdata.enums.SuckCacheKeyPerfixEnum;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
//抽数 - 客户波动/预警表上个月的月份基础数据

public class KhyjByWarningSum extends KhyjSuckDataTaskAbs {
	Logger log = Logger.getLogger(getClass());
	@Override
	public int index() {
		// 排序，越大优先度越高. 根据依赖关系调整优先顺序
		return 1;
	}
	//不检测  单独起一个job  通过cron表达式控制月初上成上个月的波动表月基础数据
	@Override
	public boolean preCheck(LogSuckdataService logSuckdataService,
			Date targetDay) {
		return true;
		/*SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
	    String dqday = sdf.format(targetDay);
		String search_month = null;
		  if(org.apache.commons.lang3.StringUtils.isNotEmpty(dqday)) {
		   Date _startDate = DateUtils.parseDate(dqday, "yyyy-MM-dd");
		   //201808
		    search_month = DateUtils.format(_startDate, "yyyyMM");
		  }
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("search_month",search_month);
		
		int count = TaskBeanUtils.getReportWarningDao().countTask(param);
		return count <1;*/
	}



	@Override
	public String realProcess(LogSuckdataService logSuckdataService,Date targetDay) {
		//获取所选时间的上一个月
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
	    String dqday = sdf.format(targetDay);
		java.util.Date date;
		try {
			date = sdf.parse(dqday);
			cal.setTime(date);
	        cal.add(Calendar.MONTH, -1);
		} catch (ParseException e) {
			//e.printStackTrace();
			log.error(e.getMessage(), e);
		}
	    String lastDate=sdf.format(cal.getTime());  
        String endDatenum = DateUtils.getMonthEnd(lastDate);
        String startDatenum = DateUtils.getMonthBegin(lastDate);
        
        //targetDay的年月作为   跑的数据是哪个月的  标识字段
        String search_month = null;
		  if(org.apache.commons.lang3.StringUtils.isNotEmpty(dqday)) {
		   //2018-08-08 
		   Date _startDate = DateUtils.parseDate(dqday, "yyyy-MM-dd");
		   search_month = DateUtils.format(_startDate, "yyyyMM");
		  }
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("search_month",search_month);
		param.put("startDatenum", startDatenum);
		param.put("endDatenum", endDatenum);
		Object result = null;
		//查询日期为2019.1.1日前的，按照老逻辑，2019.1.1日后的（if(true)），按照新逻辑
		if(!DateUtils.parseDate(endDatenum).before(DateUtils.parseDate(Constant.NEWBEGINRUNTIME))){
			result=TaskBeanUtils.getReportWarningDao().saveYjByAllNew(param);//saveYjByAll(targetDay);
		}else{
			result=TaskBeanUtils.getReportWarningDao().saveYjByAll(param);//saveYjByAll(targetDay);
		}
		
		return targetDay+"预警数据生成"+result!=null?result+"":0+"条";
	}

	@Override
	public void realClearTable(LogSuckdataService logSuckdataService,Date targetDay) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
	    String dqday = sdf.format(targetDay);
	  //targetDay的年月作为   跑的数据是哪个月的标识字段
	    String search_month = null;
		  if(org.apache.commons.lang3.StringUtils.isNotEmpty(dqday)) {
		   Date _startDate = DateUtils.parseDate(dqday, "yyyy-MM-dd");
		   search_month = DateUtils.format(_startDate, "yyyyMM");
		  }
		TaskBeanUtils.getReportWarningDao().removeYjByAll(search_month);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void realClearRedis(LogSuckdataService logSuckdataService,Date targetDay) {
		Collection<Object> persons = new ArrayList<Object>();
		persons.addAll(TaskBeanUtils.getRedisTemplate().keys("queryallcount"+"*"));
		TaskBeanUtils.getRedisTemplate().delete(persons);
	}

	@Override
	public int whichLogType() {
		return LogSuckTypeEnum.khyj_warningsum.getCode();
	}

	@Override
	public String whoareyou() {
		return "客户预警报表 - 上个月的波动/预警月份基础表";
	}
	@Override
	public String cacheKeyPerfix() {
		return SuckCacheKeyPerfixEnum.yujin.getCode();
	}

}