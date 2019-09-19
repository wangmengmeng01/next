package com.yunda.base.feiniao.schedule.suckdata.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.common.config.Constant;
import com.yunda.base.feiniao.log.domain.LogSuckdataDO;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.dao.ReportTotaldataDao;
import com.yunda.base.feiniao.schedule.suckdata.service.ReportZongBiaoMonthJEDataService;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
@Service
public class ReportZongBiaoMonthJEDataServiceImpl implements ReportZongBiaoMonthJEDataService{
private final Logger log = LoggerFactory.getLogger(getClass());
	
	private static final int TYPE_PRODUCE = 1; // 生成目标天数据落表
	private static final int TYPE_CLEAR_TABLE = 2;// 清理数据库表目标天数据
	
	@Autowired
	LogSuckdataService logSuckdataService;
	
	@Autowired
	private ReportTotaldataDao reportTotaldataDao;

	@Override
	public void processSuck(int targetDay) {
		/** 
		 * 新逻辑数据落表1，老逻辑数据落表2
		 * 重新跑就先删除目标天数据，再落数据
		 * 这里流程简单化  不检测  直接删表数据/重建数据
		 * */
		// 清理新逻辑所有过程表
		realProcess(targetDay, TYPE_CLEAR_TABLE);
		// 执行生成新逻辑目标天数据落表动作
		realProcess(targetDay, TYPE_PRODUCE);
		
	}
	
	/**
	 * 
	 * @param target
	 *            抽数的目标日期， 0为当天，-1为昨天，-2为前天，以此类推
	 * @param type
	 *            1生成数据 2清理
	 */
	private void realProcess(int target, int type) {
		String targetDay = getTargetDay(target);
		String startDay = DateUtils.getMonthBegin(targetDay);
		String endDay = DateUtils.getMonthEnd(targetDay);
		//月末最后一天没有跑数
		int TableNum = reportTotaldataDao.findIfHasDate(endDay);		
		if(TableNum == 0){
			log.warn(targetDay+"	数据没有跑数，无法生成整月数据");
			return ;
		}
		log.warn("按月汇总返利数据："+targetDay);
		switch (type) {
		//生成数据落表
		case TYPE_PRODUCE:
			executeTotalJESJD(startDay,endDay);
			logSuckdataService.save(new LogSuckdataDO(startDay+"-"+ endDay+ "按月汇总返利数据完成"));
			break;
		//清理表目标天数据
		case TYPE_CLEAR_TABLE:
			//删除金额时间段数据
			reportTotaldataDao.deleteJeByCustomerSJDNew(startDay);
			reportTotaldataDao.deleteJeByWangdianSJD(startDay);
			reportTotaldataDao.deleteJeByCitySJD(startDay);
			reportTotaldataDao.deleteJeByProvinceSJD(startDay);
			reportTotaldataDao.deleteJeByBigAreaSJD(startDay);
			reportTotaldataDao.deleteJeByTimeSJD(startDay);
			break;

		}
	}
	
	private String getTargetDay(int target) {
		Date targetDay1 = DateUtils.getDate(new Date(), target);
		String targetDay = DateUtils.format(targetDay1);
		return targetDay;
	}
	
	/**
	 * 
	 * 不能跨月，因为每个月的返利税率可能不一样
	 * 生成返利金额具体时间段汇总（不能跨月）
	 * 
	 * @param stDate
	 * @param enDate
	 */
	public void executeTotalJESJD(String stDate,String enDate){
		Map<String, Object> zbjeMap = new HashMap<String, Object>();
	    zbjeMap.put("yone", stDate);
	    zbjeMap.put("ytwo", enDate);
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
			
			reportTotaldataDao.saveJeByCustomerSJDNew(zbjeMap);
			
		}else{
			//单个月份 各类客户金额	 tmpv2_cust_price_sum_sjd
			reportTotaldataDao.saveJeByCustomerSJD(zbjeMap);
		}
		//金额-2-网点金额合计 tmpv2_gs_price_sum_sjd

		reportTotaldataDao.saveJeByWangdianSJD(zbjeMap);
		//金额-3-城市金额合计  tmpv2_city_price_sum_sjd
		
		reportTotaldataDao.saveJeByCitySJD(zbjeMap);
		//金额-4-省份金额合计  tmpv2_province_price_sum_sjd
		
		reportTotaldataDao.saveJeByProvinceSJD(zbjeMap);
		//金额-5-大区金额合计  tmpv2_bigarea_price_sum_sjd
		
		reportTotaldataDao.saveJeByBigAreaSJD(zbjeMap);
		//金额-6-全国金额合计  tmpv2_all_price_sum_sjd
		
		reportTotaldataDao.saveJeByTimeSJD(zbjeMap);
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
}
