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
import com.yunda.base.feiniao.schedule.suckdata.service.ReportZongBiaoMonthLJDataService;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
@Service
public class ReportZongBiaoMonthLJDataServiceImpl implements ReportZongBiaoMonthLJDataService{
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
		log.warn("按月汇总揽件数据："+targetDay);
		switch (type) {
		//生成数据落表
		case TYPE_PRODUCE:
			executeTotalLJSJD(startDay,endDay);
			logSuckdataService.save(new LogSuckdataDO(startDay+"-"+ endDay+ "按月汇总揽件数据完成"));
			break;
		//清理表目标天数据
		case TYPE_CLEAR_TABLE:
			//删除揽件时间段数据
			reportTotaldataDao.deleteLJByCustomerSJDNew(startDay);
			reportTotaldataDao.deleteLJByWangdianSJD(startDay);
			reportTotaldataDao.deleteLJByCitySJD(startDay);
			reportTotaldataDao.deleteLJByProvinceSJD(startDay);
			reportTotaldataDao.deleteLJByBigAreaSJD(startDay);
			reportTotaldataDao.deleteLJByTimeSJD(startDay);
			break;

		}
	}
	
	private String getTargetDay(int target) {
		Date targetDay1 = DateUtils.getDate(new Date(), target);
		String targetDay = DateUtils.format(targetDay1);
		return targetDay;
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
			reportTotaldataDao.saveLjByCustomerSJDNew(zbljMap);
			//揽件-网点 
			reportTotaldataDao.saveLjByWangdianSJDNew(zbljMap);
			
		}else{
			//揽件-客户
			reportTotaldataDao.saveLjByCustomerSJD(zbljMap);
			//揽件-网点
			reportTotaldataDao.saveLjByWangdianSJD(zbljMap);
		}

		//揽件-城市 
		reportTotaldataDao.saveLjByCitySJD(zbljMap);
		//揽件-省份 
		reportTotaldataDao.saveLjByProvinceSJD(zbljMap);
		//揽件-大区 
		reportTotaldataDao.saveLjByBigAreaSJD(zbljMap);
		//揽件-全国 
		reportTotaldataDao.saveLjByAllSJD(zbljMap);
	}
}
