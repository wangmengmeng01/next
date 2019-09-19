package com.yunda.base.feiniao.schedule.suckdata.task.impls.khyj;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.base.feiniao.log.enums.LogSuckTypeEnum;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.utils.TaskBeanUtils;
import com.yunda.base.feiniao.schedule.suckdata.enums.SuckCacheKeyPerfixEnum;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khbd.KhbdSuckDataTaskAbs;
/**
 * 该任务为生成每天的预警数据  作为预警反馈表的基础数据  因需要用到预警周基础表数据   
 * 故不继承KhyjSuckDataTaskAbs  而继承KhbdSuckDataTaskAbs跟随波动表的job在预警周基础表后面的job执行
 * @author admin
 *
 */
public class KhyjByWarningDay extends KhbdSuckDataTaskAbs {
	Logger log = Logger.getLogger(getClass());
	//private WarningHandleDao warningHandleDao;
	@Override
	public boolean preCheck(LogSuckdataService logSuckdataService,
			Date targetDay) {
		
        return true;
	}
	
	@Override
	/**
	 * 每日执行的定时任务
	 */
	public String realProcess(LogSuckdataService logSuckdataService,Date targetDay) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
	    String dqday = sdf.format(targetDay);
	   // System.out.println("targetDay是!!!!!!!!!!!!!!!!!!!!!"+targetDay);
	    java.util.Date date;
		try {
			date = sdf.parse(dqday);//获取目标跑数日期
			cal.setTime(date);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}  
		cal.add(Calendar.DATE, 1);  
        String deadline = sdf.format(cal.getTime());  //获取目标跑数日期的第二天  作为预警反馈截止时间
        //System.out.println("deadline是!!!!!!!!!!!!!!!!!!"+deadline);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
        cal.add(Calendar.DATE, -1);
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
        //System.out.println("startDatenum是!!!!!!!!!!!!!!!!!!!!!!!!!!!"+startDatenum);
        
        //searchWeek是标识字段  记录是哪周的数据
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("searchWeek",startDatenum);//标识字段
		param.put("qu_date", targetDay);
		param.put("deadline", deadline);
		param.put("feedbackStatus", "A");//反馈状态默认值为A 即"网点待处理"
		
		Object result=TaskBeanUtils.getWarningHandleDao().saveYjByDay(param);//warningHandleDao.saveYjByDay(param);
		System.out.println(startDatenum+"!!!!!!!"+targetDay);
		return "按日客户预警反馈表基础数据生成"+result!=null?result+"":0+"条";
	}

	
	@Override
	public void realClearTable(LogSuckdataService logSuckdataService,
			Date targetDay) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
	    String dqday = sdf.format(targetDay);
		if(org.apache.commons.lang3.StringUtils.isNotEmpty(dqday)){
			TaskBeanUtils.getWarningHandleDao().removeYjByDay(targetDay);
		}
		
	}
	
	
	

	

	@Override
	public void realClearRedis(LogSuckdataService logSuckdataService,
			Date targetDay) {
		// 清理查询产生的缓存
		
		
	}

	@Override
	public int whichLogType() {
		return LogSuckTypeEnum.khyj_warningday.getCode();
	}

	@Override
	public String whoareyou() {
		return "预警反馈表 — 按日生成预警反馈表每日基础预警数据";
	}
	@Override
	public int index() {
		// 排序，越大优先度越高. 根据依赖关系调整优先顺序
		return 1;
	}

	@Override
	public String cacheKeyPerfix() {
		return SuckCacheKeyPerfixEnum.yujinday.getCode();
	}


}
