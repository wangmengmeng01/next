package com.yunda.base.feiniao.report.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Objects;
import com.yunda.base.common.config.Constant;
import com.yunda.base.feiniao.report.dao.ReportTotaldataDao;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
@Service
public class CRMKHReportTotalSqltableServinceImpl {
	
	@Autowired
	ReportTotaldataDao totalDateDao;
	
	public synchronized boolean totalSql(String startDate, String endDate) throws Exception {
			Logger logger = Logger.getLogger(CRMKHReportTotalSqltableServinceImpl.class);
			String prefix = startDate.replaceAll("-", "") + "_" +endDate.replaceAll("-", "");
			SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyyMMdd-HH-mm-ss");
			//金额 (每个月的金额不同--取出开始时间到结束时间的月份)
			List<String> tbnms = new ArrayList<String>();
			List<HashMap<String,Object>> searchDate = new ArrayList<HashMap<String,Object>>();
            boolean doesExecute =false;
            boolean doesHave =false;

			String stDate = startDate;
			String enDate = endDate;
			int reckon = reckon(stDate, enDate) + 1;	
			getTotalDate(tbnms,reckon,stDate,enDate);
			logger.warn("汇总金额:" + prefix+"--"+simpleFormat.format(new Date()));
			long y = System.currentTimeMillis();
			for (String tb : tbnms) {
				String[] zy = tb.split("_");
				String yone = zy[0];//起始月份(格式：yyyy-mm-dd)
				String ytwo = zy[1];//结束月份(格式：yyyy-mm-dd)
			    HashMap<String,Object> param = new HashMap<>();
			    param.put("yone", stDate);
			    param.put("ytwo", enDate);
				searchDate.add(param);
			
				String priceType = "y";
				if(Constant.TOTALPRICETYPEXMONTH.indexOf(yone.split("-")[1]) != -1) {
					priceType = "x";
				}
	
			    List<HashMap<String,Object>> result= totalDateDao.searchTotalPriceConfig(param);
			    if(result.size()>0){
		            for(HashMap<String,Object> totalDate:result){
		            	if(Objects.equal(stDate, totalDate.get("start_date").toString())&&
		            			Objects.equal(enDate, totalDate.get("end_date").toString()))
		            	{
		            		doesHave =true;
		            	}
		            }
		        	if(!doesHave){
		        		doesExecute =true;
		    			executeTotalSJD(yone,ytwo,priceType);
		        	}
			    }else{
			    	doesExecute =true;
	    			executeTotalSJD(yone,ytwo,priceType);
			    }
			}
			if(doesExecute)
		    executeTotalSJD2(searchDate);
			logger.warn("is_finish:汇总金额"+prefix+"--"+ (System.currentTimeMillis() - y));
			return false;

	}
	
	/**
	 * 
	 * 生成具体数据起始日期和结束日期。
	 * 
	 * @param tbnms
	 * @param reckon
	 * @param stDate
	 * @param enDate
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年10月9日
	 */	
	public List<String> getTotalDate(List<String> tbnms,int reckon,String stDate,String enDate){
		if (reckon == 1) {
			tbnms.add(stDate + "_" + enDate);
		} else if (reckon == 2) {
			String a = DateUtils.getMonthEnd(stDate);
			tbnms.add(stDate + "_" + a);
			
			String b = DateUtils.getMonthBegin(enDate);
			tbnms.add(b + "_" + enDate);
		} else {
			String a = DateUtils.getMonthEnd(stDate);
			tbnms.add(stDate + "_" + a);
			
			Calendar c = Calendar.getInstance();  
			for (int i = 1; i < reckon - 1; i++) {
				 c.setTime(DateUtils.parseDate(stDate));//设置当前日期  
		         c.add(Calendar.MONTH, i); //日期加1月
		         String cc = DateUtils.format(c.getTime());
		         tbnms.add(DateUtils.getMonthBegin(cc) + "_" + DateUtils.getMonthEnd(cc));
			}
			String b = DateUtils.getMonthBegin(enDate);
			tbnms.add(b + "_" + enDate);
		}
		return tbnms;
		
	}
	
	/**
	 * 
	 * 生成具体时间段数据。
	 * 
	 * @param stDate
	 * @param enDate
	 * @param priceType
	 * @author bianxiaolong
	 * @since 1.0.0_2018年10月9日
	 */
	public void executeTotalSJD(String stDate,String enDate,String priceType){
	    HashMap<String,Object> param = new HashMap<>();
	    param.put("yone", stDate);
	    param.put("ytwo", enDate);
	    param.put("priceType", priceType);
	    String startDay = DateUtils.getMonthBegin(enDate);
	    param.put("startDay", startDay);
		//一类省份id
		if(DateUtils.parseDate(enDate).before(DateUtils.parseDate(Constant.FIRST_PROVINCEIDSBEFORE2019_WORK))){
			param.put("firstProvinceids", Constant.FIRST_PROVINCEIDSBEFORE2019);
		}else{
			param.put("firstProvinceids", Constant.FIRST_PROVINCEIDS);
		}
	    
	    totalDateDao.insertTotalPriceConfig(param);
		//查询日期为2019.1.1日前的，按照老逻辑，2019.1.1日后的（if(true)），按照新逻辑
		if(!DateUtils.parseDate(enDate).before(DateUtils.parseDate(Constant.NEWBEGINRUNTIME))){
			//金额-1-单个月份 各类客户金额	2019-01-01开始执行新的逻辑	
			totalDateDao.saveJeByCustomerSJDNew(param);
		}else{
			//金额-1-单个月份 各类客户金额		
			totalDateDao.saveJeByCustomerSJD(param);
		}
		//金额-2-网点金额合计 
		totalDateDao.saveJeByWangdianSJD(param);
		//金额-3-城市金额合计 
		totalDateDao.saveJeByCitySJD(param);
		//金额-4-省份金额合计 
		totalDateDao.saveJeByProvinceSJD(param);
		//金额-5-大区金额合计 
		totalDateDao.saveJeByBigAreaSJD(param);
		//金额-6-全国金额合计 
		totalDateDao.saveJeByTimeSJD(param);	
	}
	
	/**
	 * 
	 * 生成具体时间段汇总数据。
	 * 
	 * @param stDate
	 * @param enDate
	 * @author bianxiaolong
	 * @since 1.0.0_2018年10月9日
	 */
	public void executeTotalSJD2(List<HashMap<String,Object>> tbnms){
		totalDateDao.saveJeByCustomerStatsSJD2(tbnms);
		totalDateDao.saveJeByGsPriceSum(tbnms);
		totalDateDao.saveJeByCityPriceSum(tbnms);
		totalDateDao.saveJeByProvincePriceSum(tbnms);
		totalDateDao.saveJeByBigAreaPriceSum(tbnms);
		totalDateDao.saveJeByTimePriceSum(tbnms);	
	}
	
	/**
	 * 
	 * 生成月份间隔数。
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 * @author bianxiaolong
	 * @since 1.0.0_2018年10月9日
	 */
	public int reckon(String startDate, String endDate) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(sdf.parse(startDate));
		c2.setTime(sdf.parse(endDate));
		int year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
		// 开始日期若小月结束日期
		if (year < 0) {
			year = -year;
			return year * 12 + c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
		}
		return year * 12 + c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
	}
}
