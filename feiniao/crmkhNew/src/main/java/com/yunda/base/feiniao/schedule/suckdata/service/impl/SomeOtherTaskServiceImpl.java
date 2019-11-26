package com.yunda.base.feiniao.schedule.suckdata.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.feiniao.report.dao.RebateAmountInterfaceDao;
import com.yunda.base.feiniao.report.domain.RebateAmountInterfaceDO;
import com.yunda.base.feiniao.schedule.suckdata.service.SomeOtherTaskService;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;

@Service
public class SomeOtherTaskServiceImpl implements SomeOtherTaskService{
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private static final int TYPE_PRODUCE = 1; // 生成目标天数据落表
	private static final int TYPE_CLEAR_TABLE = 2;// 清理数据库表目标天数据
	
	@Autowired
	RebateAmountInterfaceDao rebateAmountInterfaceDao;
	
	/**
	 * 针对指定日期生成数据，如果当日已经存在，那么需要先清理然后重建数据
	 * 
	 * @param targetDay抽数的目标日期，
	 *            0为当天，-1为昨天，-2为前天，以此类推
	 */
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
		/**
		 * 执行删除老逻辑目标天数据动作
		 */
		realProcessOld(targetDay, TYPE_CLEAR_TABLE);
		/**
		 * 执行生成老逻辑目标天数据落表动作
		 */
		realProcessOld(targetDay, TYPE_PRODUCE);
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
		log.warn("财务大客户返利新逻辑开始跑数："+targetDay);
		switch (type) {
		//生成数据落表
		case TYPE_PRODUCE:
			int dataCount = rebateAmountInterfaceDao.reportOrderCount(targetDay);
			if(dataCount <= 0){
				log.warn("财务大客户返利新逻辑开始跑数,crmkhv2_report_order_stats_all 表 "+targetDay+" 没有数据");
				break;
			}
			rebateAmountInterfaceDao.saveFinanceCustPriceSum(targetDay);
			rebateAmountInterfaceDao.saveFinanceGsPriceSum(targetDay);
			List<RebateAmountInterfaceDO> dataList = rebateAmountInterfaceDao.getFinanceGsPriceSumList(targetDay);
			Integer zzz = 0;
			for(RebateAmountInterfaceDO data : dataList){
				//获取一级网点分拨中心
				RebateAmountInterfaceDO dataFB = rebateAmountInterfaceDao.getBranchFB(data.getBranchCode());
				//log.warn("============================分拨中心："+dataFB.getFbCenter());
				if(null == dataFB){
					data.setFbCenter(zzz);
				}else{
					if(null == dataFB.getFbCenter()){
						data.setFbCenter(zzz);
					}else{
						data.setFbCenter(dataFB.getFbCenter());
					}
					
				}
			}
			if(dataList.size() > 0 && dataList != null){
				rebateAmountInterfaceDao.saveRebateAmount(dataList);
			}
			break;
		//清理表目标天数据
		case TYPE_CLEAR_TABLE:
			rebateAmountInterfaceDao.removeRebateAmount(targetDay);
			rebateAmountInterfaceDao.removeFinanceCustPriceSum(targetDay);
			rebateAmountInterfaceDao.removeFinanceGsPriceSum(targetDay);
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
	 * @param target
	 *            抽数的目标日期， 0为当天，-1为昨天，-2为前天，以此类推
	 * @param type
	 *            1生成数据 2清理
	 */
	private void realProcessOld(int target, int type) {
		String targetDay = getTargetDay(target);
		log.warn("财务大客户返利老逻辑开始跑数："+targetDay);
		switch (type) {
/*		//生成数据落表
		case TYPE_PRODUCE:
			int dataCount = rebateAmountInterfaceDao.reportGsPriceSumCount(targetDay);
			if(dataCount <= 0){
				log.warn("财务大客户返利老逻辑开始跑数,tmpv2_gs_price_sum 表  "+targetDay+" 没有数据，请重新从GP源头表跑数");
				break;
			}

			List<RebateAmountInterfaceDO> dataList = rebateAmountInterfaceDao.getGsPriceSumList(targetDay);
			Integer zzz = 0;
			for(RebateAmountInterfaceDO data : dataList){
				//获取一级网点分拨中心
				RebateAmountInterfaceDO dataFB = rebateAmountInterfaceDao.getBranchFB(data.getBranchCode());
				if(null == dataFB){
					data.setFbCenter(zzz);
				}else{
					if(null == dataFB.getFbCenter()){
						data.setFbCenter(zzz);
					}else{
						data.setFbCenter(dataFB.getFbCenter());
					}
				}
			}
			if(dataList.size() > 0 && dataList != null){
				rebateAmountInterfaceDao.saveOldRebateAmount(dataList);
			}
			break;
		//清理表目标天数据
		case TYPE_CLEAR_TABLE:
			rebateAmountInterfaceDao.removeOldRebateAmount(targetDay);
			break;*/

		//生成数据落表
		case TYPE_PRODUCE:
			int dataCount = rebateAmountInterfaceDao.reportOrderCount(targetDay);
			if(dataCount <= 0){
				log.warn("财务大客户返利新逻辑开始跑数,crmkhv2_report_order_stats_all 表 "+targetDay+" 没有数据");
				break;
			}

			rebateAmountInterfaceDao.saveFinanceCustPriceSumOld(readyMap(targetDay));
			rebateAmountInterfaceDao.saveFinanceGsPriceSumOld(targetDay);
			List<RebateAmountInterfaceDO> dataList = rebateAmountInterfaceDao.getFinanceGsPriceSumOldList(targetDay);
			Integer zzz = 0;
			for(RebateAmountInterfaceDO data : dataList){
				//获取一级网点分拨中心
				RebateAmountInterfaceDO dataFB = rebateAmountInterfaceDao.getBranchFB(data.getBranchCode());
				if(null == dataFB){
					data.setFbCenter(zzz);
				}else{
					if(null == dataFB.getFbCenter()){
						data.setFbCenter(zzz);
					}else{
						data.setFbCenter(dataFB.getFbCenter());
					}
				}
			}
			if(dataList.size() > 0 && dataList != null){
				rebateAmountInterfaceDao.saveOldRebateAmount(dataList);
			}
			break;
		//清理表目标天数据
		case TYPE_CLEAR_TABLE:
			rebateAmountInterfaceDao.removeOldRebateAmount(targetDay);
			rebateAmountInterfaceDao.removeFinanceCustPriceSumOld(targetDay);
			rebateAmountInterfaceDao.removeFinanceGsPriceSumOld(targetDay);
			break;
		}
		
	}
	
	
	public HashMap<String,Object> readyMap(String targetDay) {
		HashMap<String,Object> param =new HashMap<String,Object>();
		String priceType = "y";
		if("02,03,04,05,06,07,08,".indexOf(targetDay.split("-")[1]) != -1) {
			priceType = "x";
		}
		param.put("targetDay", targetDay);
		param.put("priceType", priceType);
		return param;
	}
}