package com.yunda.base.feiniao.schedule.suckdata.service.impl;

import com.yunda.base.feiniao.log.domain.LogSuckdataDO;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.dao.RegionalBasicInformationDao;
import com.yunda.base.feiniao.report.domain.RegionalBasicInformationDO;
import com.yunda.base.feiniao.schedule.suckdata.service.RunBasicalInfoByMonthService;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class RunBasicalInfoByMonthImpl implements RunBasicalInfoByMonthService {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private static final int TYPE_PRODUCE = 1; // 生成目标天数据落表
	private static final int TYPE_CLEAR_TABLE = 2;// 清理数据库表目标天数据
	
	@Autowired
	LogSuckdataService logSuckdataService;
	
	@Autowired
	RegionalBasicInformationDao regionalBasicInformationDao;

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
		log.warn("每月更新区县市省大区信息："+targetDay);
		switch (type) {
		//生成数据落表
		case TYPE_PRODUCE:
			List<RegionalBasicInformationDO> infoList = regionalBasicInformationDao.getBasicInformationList();
			if(infoList.size() <= 0|| infoList == null){
				log.warn("ydserver基础信息表 没有数据");
				break;
			}
			for(RegionalBasicInformationDO date :infoList){
				//业务省名字为null,则sys_province与ydserver.province_business数据不一致，手动更新sys_province数据
				if(null == date.getProvincename()||"".equals(date.getProvincename())){
					logSuckdataService.save(new LogSuckdataDO(targetDay + ":sys_province与产线数据(province_business)不一致，请手动更新"));
					return;
				}
				date.setStartDate(startDay);
				date.setEndDate(endDay);
				
			}
			int result = 0;
			result = regionalBasicInformationDao.saveBasicalInfo(infoList);
			logSuckdataService.save(new LogSuckdataDO(targetDay + "更新基础信息表(crmkhv2_regional_basic_information) "+result+"  条数据"));
			break;
		//清理表目标天数据
		case TYPE_CLEAR_TABLE:
			regionalBasicInformationDao.deleteDateByDate(startDay);
			break;

		}
	}
	
	private String getTargetDay(int target) {
		Date targetDay1 = DateUtils.getDate(new Date(), target);
		String targetDay = DateUtils.format(targetDay1);
		return targetDay;
	}

}
