package com.yunda.base.feiniao.report.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		long y = System.currentTimeMillis();
		executeTotalPriceSJD(startDate,endDate);
		executeTotalLJSJD(startDate,endDate);
		logger.warn("is_finish:汇总金额"+prefix+"--"+ (System.currentTimeMillis() - y));
		return false;
			/*Logger logger = Logger.getLogger(CRMKHReportTotalSqltableServinceImpl.class);
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
			//遍历月份，确定税率
			for (String tb : tbnms) {
				String[] zy = tb.split("_");
				String yone = zy[0];//起始月份(格式：yyyy-mm-dd)
				String ytwo = zy[1];//结束月份(格式：yyyy-mm-dd)
			    HashMap<String,Object> param = new HashMap<>();
			    param.put("yone", stDate);
			    param.put("ytwo", enDate);
				searchDate.add(param);
			
				String priceType = "y";
				//根据月份确定税率
				if(Constant.TOTALPRICETYPEXMONTH.indexOf(yone.split("-")[1]) != -1) {
					priceType = "x";
				}
				//判断tmpv2_total_price_config 表中是否有查询时间段（查询了全表数据，有必要？）
			    List<HashMap<String,Object>> result= totalDateDao.searchTotalPriceConfig(param);
			    if(result.size()>0){
			    	//循环全表数据 性能？？？
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
			    	//tmpv2_total_price_config中没有时间段数据
			    	doesExecute =true;
	    			executeTotalSJD(yone,ytwo,priceType);
			    }
			}
			if(doesExecute)
		    executeTotalSJD2(searchDate);
		logger.warn("is_finish:汇总金额"+prefix+"--"+ (System.currentTimeMillis() - y));
		return false;*/

	}
	
	/**
	 * 查询月份不同，奖励金额基数不一样
	 * 
	 * @param startDate
	 * @param readyMap
	 * @return
	 */
	public Map<String, Object> readyMap(String startDate, Map<String, Object> readyMap) {
		String priceType = "y";
		if ("02,03,04,05,06,07,08,".indexOf(startDate.split("-")[1]) != -1) {
			priceType = "x";
		}
		readyMap.put("priceType", priceType);
		return readyMap;
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
	 * 不能跨月，因为每个月的返利税率可能不一样
	 * 生成返利金额具体时间段汇总（不能跨月）
	 * 
	 * @param stDate
	 * @param enDate
	 */
	public void executeTotalPriceSJD(String stDate,String enDate){
		Map<String, Object> zbjeMap = new HashMap<String, Object>();
	    zbjeMap.put("yone", stDate);
	    zbjeMap.put("ytwo", enDate);
	    String startDay = DateUtils.getMonthBegin(enDate);
	    zbjeMap.put("startDay", startDay);
		//一类省份id
		if(DateUtils.parseDate(enDate).before(DateUtils.parseDate(Constant.FIRST_PROVINCEIDSBEFORE2019_WORK))){
			zbjeMap.put("firstProvinceids", Constant.FIRST_PROVINCEIDSBEFORE2019);
		}else{
			zbjeMap.put("firstProvinceids", Constant.FIRST_PROVINCEIDS);
		}
		zbjeMap = readyMap(stDate, zbjeMap);
		//查询日期为2019.1.1日前的，按照老逻辑，2019.1.1日后的（if(true)），按照新逻辑
		if(!DateUtils.parseDate(enDate).before(DateUtils.parseDate(Constant.NEWBEGINRUNTIME))){
			//单个月份 各类客户金额 tmpv2_cust_price_sum_sjd
			//2019-01-01开始执行新的逻辑	
			
			totalDateDao.saveJeByCustomerSJDNew(zbjeMap);
			
		}else{
			//单个月份 各类客户金额	 tmpv2_cust_price_sum_sjd
			totalDateDao.saveJeByCustomerSJD(zbjeMap);
		}
		//金额-2-网点金额合计 tmpv2_gs_price_sum_sjd

		totalDateDao.saveJeByWangdianSJD(zbjeMap);
		//金额-3-城市金额合计  tmpv2_city_price_sum_sjd
		
		totalDateDao.saveJeByCitySJD(zbjeMap);
		//金额-4-省份金额合计  tmpv2_province_price_sum_sjd
		
		totalDateDao.saveJeByProvinceSJD(zbjeMap);
		//金额-5-大区金额合计  tmpv2_bigarea_price_sum_sjd
		
		totalDateDao.saveJeByBigAreaSJD(zbjeMap);
		//金额-6-全国金额合计  tmpv2_all_price_sum_sjd
		
		totalDateDao.saveJeByTimeSJD(zbjeMap);
	}
	/**
	 * 生成揽件具体时间段汇总（不能跨月）
	 * @param stDate
	 * @param enDate
	 */
	public void executeTotalLJSJD(String stDate,String enDate){
		Map<String, Object> zbljMap = new HashMap<String, Object>();
		zbljMap.put("startDate", stDate);
		zbljMap.put("endDate", enDate);
	    String startDay = DateUtils.getMonthBegin(enDate);
	    zbljMap.put("startDay", startDay);
		//一类省份id
		if(DateUtils.parseDate(enDate).before(DateUtils.parseDate(Constant.FIRST_PROVINCEIDSBEFORE2019_WORK))){
			zbljMap.put("firstProvinceids", Constant.FIRST_PROVINCEIDSBEFORE2019);
		}else{
			zbljMap.put("firstProvinceids", Constant.FIRST_PROVINCEIDS);
		}
		//zbljMap = readyMap(stDate, zbljMap);
		//查询日期为2019.1.1日前的，按照老逻辑，2019.1.1日后的（if(true)）按照新逻辑
		if(!DateUtils.parseDate(enDate).before(DateUtils.parseDate(Constant.NEWBEGINRUNTIME))){
			//揽件-客户 tmpv2_cust_od_sum_sjd
			totalDateDao.saveLjByCustomerSJDNew(zbljMap);
			//揽件-网点 
			totalDateDao.saveLjByWangdianSJDNew(zbljMap);

		}else{
			//揽件-客户
			totalDateDao.saveLjByCustomerSJD(zbljMap);
			//揽件-网点
			totalDateDao.saveLjByWangdianSJD(zbljMap);
		}

		//揽件-城市 
		totalDateDao.saveLjByCitySJD(zbljMap);
		//揽件-省份 
		totalDateDao.saveLjByProvinceSJD(zbljMap);
		//揽件-大区 
		totalDateDao.saveLjByBigAreaSJD(zbljMap);
		//揽件-全国 
		totalDateDao.saveLjByAllSJD(zbljMap);
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
/*	public void executeTotalSJD(String stDate,String enDate,String priceType){
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
	    //向tmpv2_total_price_config表中插入当前时间段
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
	}*/
	
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
