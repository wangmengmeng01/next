package com.yunda.base.feiniao.market.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunda.base.feiniao.market.bo.Bo_marketOccupancyTaoxi;
import com.yunda.base.feiniao.market.dao.MarketOccupancyTaoxiDao;
import com.yunda.base.feiniao.market.domain.MarketOccupancyTaoxiDO;
import com.yunda.base.feiniao.market.service.MarketOccupancyTaoxiService;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;



@Service
public class MarketOccupancyTaoxiServiceImpl implements MarketOccupancyTaoxiService {
	@Autowired
	private MarketOccupancyTaoxiDao marketOccupancyTaoxiDao;
	
	@Override
	public MarketOccupancyTaoxiDO get(Long id){
		return marketOccupancyTaoxiDao.get(id);
	}
	
	@Override
	public List<MarketOccupancyTaoxiDO> list(Bo_marketOccupancyTaoxi boInterface){
		Map<String, Object> map = new HashMap<String, Object>();
		//map.put("timeType", boInterface.getTimeType());
		map.put("startDate", boInterface.getStartDate());
		map.put("endDate", boInterface.getEndDate());
		map.put("month", boInterface.getMonth());
		map.put("limit", boInterface.getLimit());
		map.put("offset", boInterface.getOffset());
		
		List<MarketOccupancyTaoxiDO> dataList = new ArrayList<MarketOccupancyTaoxiDO>(); 
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//日报
		if("1".equals(boInterface.getTimeType())){
			dataList= marketOccupancyTaoxiDao.list(map);
			for(MarketOccupancyTaoxiDO data : dataList){
				data.setShowQuDate(df.format(data.getQuDate()));
			}
		}
		//周报
		if("2".equals(boInterface.getTimeType())){
			
			Calendar cal = Calendar.getInstance();
		    String dqday = boInterface.getStartDate();
		    java.util.Date date;
			try {
				date = df.parse(dqday);//获取目标跑数日期
				cal.setTime(date);
			} catch (ParseException e) {
			}  
	        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
	        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得目标跑数日期是一个星期的第几天  
	        if (1 == dayWeek) {  
	           cal.add(Calendar.DAY_OF_MONTH, -1);  
	        }  
	         // 业务需求  设置周五为一个星期的第一天
	        cal.setFirstDayOfWeek(Calendar.FRIDAY);  
	        // 获得当前日期是一个星期的第几天  
	        int day = cal.get(Calendar.DAY_OF_WEEK);  
	        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
	        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day -7); 
	        if(day>=6){
	        	cal.add(Calendar.DATE, 7);
	        }
	        String startDatenum = df.format(cal.getTime());  //startDate所在周的起始日期
	        //System.out.println("!!!!!!!!!!!!!!!"+startDatenum);
	        cal.add(Calendar.DATE, 6);  
	        String endDatenum = df.format(cal.getTime());//startDate所在周的结束日期
	        //所选日期是不是跨周
	        Date sd1 = null;
	        Date sd2 = null;
	        Date sd3 = null;
	        Date sd4 = null;
			String startDate = startDatenum;//boInterface.getStartDate();
			String endDate = endDatenum;
			String now = df.format(new Date());//获取系统当前时间
			try {
				sd1 = df.parse(startDate);
				sd2 = df.parse(boInterface.getEndDate());
				sd3 = df.parse(endDate);
				sd4 = df.parse(now);
			} catch (ParseException e) {
			}
			while(sd1.getTime() <= sd2.getTime()){
				if(sd3.getTime() > sd2.getTime()&& sd3.getTime()>sd4.getTime()){
					endDate= boInterface.getEndDate();//endDate在周末内
				}
				
				map.put("startDate", startDate);
				map.put("endDate", endDate);
				MarketOccupancyTaoxiDO listWeek= marketOccupancyTaoxiDao.listRangeSum(map);//周汇总
				if(listWeek!=null){
				listWeek.setShowQuDate(startDate+"-"+endDate);
				
				double yd_ratio = listWeek.getYdSum()/listWeek.getTotalSum();
				double zto_ratio = listWeek.getZtoSum()/listWeek.getTotalSum();
				double yto_ratio = listWeek.getStoSum()/listWeek.getTotalSum();
				double sto_ratio = listWeek.getYtoSum()/listWeek.getTotalSum();
				
				listWeek.setYdRatio((double)Math.round(yd_ratio*100)/100);
				listWeek.setZtoRatio((double)Math.round(zto_ratio*100)/100);
				listWeek.setYtoRatio((double)Math.round(yto_ratio*100)/100);
				listWeek.setStoRatio((double)Math.round(sto_ratio*100)/100);
				//百世=100-所有   保持总数是100%
				double bestexRatio = 100-listWeek.getYdRatio()-listWeek.getZtoRatio()-listWeek.getStoRatio()-listWeek.getYtoRatio();
				double bestexSum = listWeek.getTotalSum()- listWeek.getYdSum()-listWeek.getZtoSum()-listWeek.getStoSum()-listWeek.getYtoSum();
				
				listWeek.setBestexRatio((double)Math.round(bestexRatio*100)/100);
				listWeek.setBestexSum((double)Math.round(bestexSum*100)/100);
				
				dataList.add(listWeek);
				}
				//循环计算下一周
				cal.setTime(sd3);
				cal.add(Calendar.DATE, 1);
				sd1 = cal.getTime();
				startDate = df.format(sd1);//下周开始日期  周五
				cal.add(Calendar.DATE, 6);
				sd3 = cal.getTime();//下周结束日期  周四
				endDate = df.format(sd3);
			}
		}
		//月报
		if("3".equals(boInterface.getTimeType())){
			 String startDate = boInterface.getMonth()+"-01";
			 String endDatenum = DateUtils.getMonthEnd(startDate);
			 String[] b = endDatenum.split("-");
			 String daynum = b[2];
			 String endDateMonth = boInterface.getMonth()+"-"+daynum;
			
			map.put("startDate", startDate);
			//map.put("endDate", endDateMonth);
			Date sd1 = null;
			try {
				sd1 = df.parse(endDateMonth);
			} catch (ParseException e) {
			}
			 Date sd2 = new Date();
			 if(sd1.before(sd2)){
				 map.put("endDate", endDateMonth);//非本月
			 }else{
				 map.put("endDate", df.format(sd2));//本月
			 }
			
			MarketOccupancyTaoxiDO listMonth= marketOccupancyTaoxiDao.listRangeSum(map);//月汇总
			listMonth.setShowQuDate(boInterface.getMonth());
			
			double yd_ratio = listMonth.getYdSum()/listMonth.getTotalSum();
			double zto_ratio = listMonth.getZtoSum()/listMonth.getTotalSum();
			double yto_ratio = listMonth.getStoSum()/listMonth.getTotalSum();
			double sto_ratio = listMonth.getYtoSum()/listMonth.getTotalSum();
			
			listMonth.setYdRatio((double)Math.round(yd_ratio*100)/100);
			listMonth.setZtoRatio((double)Math.round(zto_ratio*100)/100);
			listMonth.setYtoRatio((double)Math.round(yto_ratio*100)/100);
			listMonth.setStoRatio((double)Math.round(sto_ratio*100)/100);
			
			//百世=100-所有   保持总数是100%
			double bestexRatio = 100-listMonth.getYdRatio()-listMonth.getZtoRatio()-listMonth.getStoRatio()-listMonth.getYtoRatio();
			double bestexSum = listMonth.getTotalSum()- listMonth.getYdSum()-listMonth.getZtoSum()-listMonth.getStoSum()-listMonth.getYtoSum();
			listMonth.setBestexRatio((double)Math.round(bestexRatio*100)/100);
			listMonth.setBestexSum((double)Math.round(bestexSum*100)/100);
			
			dataList.add(listMonth);
		}
		
		return dataList;
	}
	
	@Override
	public int count(Bo_marketOccupancyTaoxi boInterface){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startDate", boInterface.getStartDate());
		map.put("endDate", boInterface.getEndDate());
		return marketOccupancyTaoxiDao.count(map);
	}
	
	@Override
	public int save(MarketOccupancyTaoxiDO marketOccupancyTaoxi){
		return marketOccupancyTaoxiDao.save(marketOccupancyTaoxi);
	}
	
	@Override
	public int update(MarketOccupancyTaoxiDO marketOccupancyTaoxi){
		double a = marketOccupancyTaoxi.getTotalSum(); 
		double b = marketOccupancyTaoxi.getYdRatio();
		double c = marketOccupancyTaoxi.getZtoRatio();
		double d = marketOccupancyTaoxi.getYtoRatio();
		double e = marketOccupancyTaoxi.getStoRatio();
		double f = 100 - b - c - d - e ;
		if(f<0){
			return 0;
		}
		marketOccupancyTaoxi.setYdSum((double)Math.round(a * b)/100);
		marketOccupancyTaoxi.setZtoSum((double)Math.round(a * c)/100);
		marketOccupancyTaoxi.setYtoSum((double)Math.round(a * d)/100);
		marketOccupancyTaoxi.setStoSum((double)Math.round(a * e)/100);
		marketOccupancyTaoxi.setBestexRatio((double)Math.round(f * 100)/100);
		marketOccupancyTaoxi.setBestexSum((double)Math.round(a * f)/100);
		return marketOccupancyTaoxiDao.update(marketOccupancyTaoxi);
	}
	
	@Override
	public int remove(Long id){
		return marketOccupancyTaoxiDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return marketOccupancyTaoxiDao.batchRemove(ids);
	}
	
}
