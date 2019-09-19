package com.yunda.base.feiniao.schedule.suckdata.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.feiniao.log.domain.LogSuckdataDO;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.schedule.suckdata.domain.RecordSuckDO;
import com.yunda.base.feiniao.schedule.suckdata.service.MarketTaoxiTaskService;
import com.yunda.base.feiniao.schedule.suckdata.service.RecordSuckService;
import com.yunda.base.feiniao.schedule.suckdata.task.MarketTaoxiAddJob;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.SuckDataTaskAbs;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.market.MarketTaoxiSum;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.market.MarketTaoxiTaskAbs;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;

@Service
public class MarketTaoxiTaskServiceImpl implements MarketTaoxiTaskService{
	private static final int TYPE_SUCK =1;
	private static final int TYPE_CLEAR_TABLE =2;
	private static final int TYPE_CLEAR_REDIS =3;
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	RecordSuckService recordSuckService;
	@Autowired
	LogSuckdataService logSuckdataService;
	//淘系
	@Override
	public void taoxiAdd(int targetDay) {
		int day = DateUtils.convertDate2Int4Day(DateUtils.getDate(targetDay));
		
		realProcessTaoxi(targetDay, TYPE_CLEAR_TABLE);
		realProcessTaoxi(targetDay, TYPE_CLEAR_REDIS);
		logSuckdataService.save(new LogSuckdataDO("日期targetDay="+targetDay + "淘系市场占有率上报--重建数据"));
		realProcessTaoxi(targetDay, TYPE_SUCK);
		
	}
	public void realProcessTaoxi(int targetDay,int type){
		logSuckdataService.save(new LogSuckdataDO(Thread.currentThread().getName() + "淘系市场占有率上报" + showType(type)
				+ "任务,targetDay=" + showTargetDay(targetDay)));
		try {
			processAllSubClass(MarketTaoxiTaskAbs.class, targetDay, type);
		} catch (Exception e) {
			logSuckdataService.save(new LogSuckdataDO("淘系市场占有率上报--异常"+ e.getMessage()));
		}
	}
		
	private String showTargetDay(int targetDay){
		Date date = DateUtils.getDate(targetDay);
		return DateUtils.format(date,"yyyy-MM-dd");
	}
	private String showType(int type){
		switch(type){
		case TYPE_SUCK:
			return "抽数";
		case TYPE_CLEAR_TABLE:
			return "清理数据库表";
		case TYPE_CLEAR_REDIS:
			return "清理缓存";
		default:
			return "";
		}
	}
	
	public void processAllSubClass(Class csabs,int targetDay, int type){
		List<Class> list =new ArrayList<Class>();
		if(csabs ==MarketTaoxiTaskAbs.class){
			list.add(MarketTaoxiSum.class);
		}
		List<SuckDataTaskAbs> objs = new ArrayList<SuckDataTaskAbs>();
		for(Class cs: list){
			try {
				SuckDataTaskAbs task = (SuckDataTaskAbs) cs.newInstance();
				objs.add(task);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
		Collections.sort(objs, new Comparator<SuckDataTaskAbs>() {
			@Override
			public int compare(SuckDataTaskAbs o1,SuckDataTaskAbs o2){
				return Integer.valueOf(o2.index()).compareTo(o1.index());
			}
		});
		for(SuckDataTaskAbs task :objs){
			switch(type){
			case TYPE_SUCK:
				task.process(logSuckdataService, targetDay);
				break;
			case TYPE_CLEAR_TABLE:
				task.clearTable(logSuckdataService, targetDay);
				break;
			case TYPE_CLEAR_REDIS:
				task.clearRedis(logSuckdataService, targetDay);
				break;
			}
		}
	}
}
