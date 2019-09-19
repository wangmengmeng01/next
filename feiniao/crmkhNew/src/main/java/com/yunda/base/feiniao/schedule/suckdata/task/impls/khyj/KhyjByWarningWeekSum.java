package com.yunda.base.feiniao.schedule.suckdata.task.impls.khyj;

import com.yunda.base.feiniao.log.enums.LogSuckTypeEnum;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.utils.TaskBeanUtils;
import com.yunda.base.feiniao.schedule.suckdata.enums.SuckCacheKeyPerfixEnum;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//抽数 - 客户波动/预警表上个月的月份基础数据

public class KhyjByWarningWeekSum extends KhyjWeekSuckDataTaskAbs {
	Logger log = Logger.getLogger(getClass());
	@Override
	public int index() {
		// 排序，越大优先度越高. 根据依赖关系调整优先顺序
		return 1;
	}
	//不检测   通过cron表达式控制每周一跑数   如果某段时间的数据需要重新跑   直接删数据并重建
	@Override
	public boolean preCheck(LogSuckdataService logSuckdataService,
			Date targetDay) {
		//SuckDataYJWEEKServiceImpl类注释了该检测方法   不检测
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
	    String dqday = sdf.format(targetDay);
	    java.util.Date date;
		try {
			date = sdf.parse(dqday);
			cal.setTime(date);
	        //cal.add(Calendar.MONTH, -1);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}  
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天  
        if (1 == dayWeek) {  
           cal.add(Calendar.DAY_OF_MONTH, -1);  
        }  
         System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期  
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
        cal.setFirstDayOfWeek(Calendar.MONDAY);  
        // 获得当前日期是一个星期的第几天  
        int day = cal.get(Calendar.DAY_OF_WEEK);  
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day-7);  
        String searchWeek = sdf.format(cal.getTime());  
        
        int count = TaskBeanUtils.getReportWarningDao().countYJWeekTask(searchWeek);
		return count <1;
        
	}



	@Override
	public String realProcess(LogSuckdataService logSuckdataService,Date targetDay) {
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
	    String dqday = sdf.format(targetDay);
	    java.util.Date date;
		try {
			date = sdf.parse(dqday);//获取目标跑数日期
			cal.setTime(date);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}  
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得目标跑数日期是一个星期的第几天  
        if (1 == dayWeek) {  
           cal.add(Calendar.DAY_OF_MONTH, -1);  
        }  
         // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
        cal.setFirstDayOfWeek(Calendar.MONDAY);  
        // 获得当前日期是一个星期的第几天  
        int day = cal.get(Calendar.DAY_OF_WEEK);  
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day-7);  
        String startDatenum = sdf.format(cal.getTime());  //获取目标跑数日期的  上周周一
        cal.add(Calendar.DATE, 6);  
        String endDatenum = sdf.format(cal.getTime());//上周周日
        
        //searchWeek是标识字段  记录是哪周的数据
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("searchWeek",startDatenum);//标识字段
		param.put("startDatenum", startDatenum);
		param.put("endDatenum", endDatenum);
		//System.out.println("!!!!!!!!!!"+startDatenum+"!!!!!!!!"+endDatenum);
		Object result = null;
		result=TaskBeanUtils.getReportWarningDao().saveYjByWeek(param);//saveYjByAll(targetDay);
		return targetDay+"预警数据生成"+result!=null?result+"":0+"条";
	}

	@Override
	public void realClearTable(LogSuckdataService logSuckdataService,Date targetDay) {
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
	    String dqday = sdf.format(targetDay);
	    java.util.Date date;
		try {
			date = sdf.parse(dqday);
			cal.setTime(date);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}  
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天  
        if (1 == dayWeek) {  
           cal.add(Calendar.DAY_OF_MONTH, -1);  
        }  
        // System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期  
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
        cal.setFirstDayOfWeek(Calendar.MONDAY);  
        // 获得当前日期是一个星期的第几天  
        int day = cal.get(Calendar.DAY_OF_WEEK);  
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day-7);  
        String searchWeek = sdf.format(cal.getTime());  
        
		TaskBeanUtils.getReportWarningDao().removeYjByWeek(searchWeek);
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
		return "抽数 - 预警周基础表";
	}
	@Override
	public String cacheKeyPerfix() {
		return SuckCacheKeyPerfixEnum.yujinweek.getCode();
	}

}