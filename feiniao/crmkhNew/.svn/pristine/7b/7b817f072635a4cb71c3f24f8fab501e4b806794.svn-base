package com.yunda.base.feiniao.report.utils;

import com.yunda.base.common.utils.SpringUtil;
import com.yunda.base.feiniao.customer.dao.NotCooperateCustomerDao;
import com.yunda.base.feiniao.market.dao.MarketOccupancyTaoxiDao;
import com.yunda.base.feiniao.report.dao.*;
import com.yunda.base.feiniao.report.service.ReportTotaldataService;
import com.yunda.base.feiniao.report.service.impl.CRMKHReportTotalManageServiceImpl;
import com.yunda.base.feiniao.warning.dao.WarningHandleDao;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;


public class TaskBeanUtils {
	private static RedisTemplate template;
	private static ReportTotaldataDao reportTotaldataDao;
	private static GpBasSCustPickTmpDao gpBasSCustPickTmpDao;
	private static ReportWarningDao reportWarningDao;
	private static ReportTotaldataService reportTotaldataServiceImpl;
	private static ReportFluctuateDao reportFluctuateDao;
	private static ReportCustRewardDetailsDao reportCustRewardDetailsDao;
	private static WarningHandleDao warningHandleDao;
	private static NotCooperateCustomerDao notCooperateCustomerDao;
	private static MarketOccupancyTaoxiDao marketOccupancyTaoxiDao;
	private static CRMKHReportTotalManageServiceImpl cRMKHReportTotalManageServiceImpl;
	private static StringRedisTemplate stringRedisTemplate;
	

//	private static String defaultDestination = "/topic/getResponse";

//	public boolean pushMsg(String msg) {
//		return pushMsg(defaultDestination, msg);
//	}
//
//	public boolean pushMsg(String destination, String msg) {
//		getSimpMessagingTemplate().convertAndSend("/topic/getResponse", R.ok(msg));
//
//		return true;
//	}
	
	public static RedisTemplate getRedisTemplate() {
		if (template == null) {
			template = SpringUtil.getBean("stringRedisTemplate",StringRedisTemplate.class);
		}
		return template;
	}
	public static ReportTotaldataDao getReportTotaldataDao() {
		if (reportTotaldataDao == null) {
		reportTotaldataDao = SpringUtil.getBean(ReportTotaldataDao.class);
		}
		return reportTotaldataDao;
	}
	public static GpBasSCustPickTmpDao getGpBasSCustPickTmpDao() {
		if (gpBasSCustPickTmpDao == null) {
			gpBasSCustPickTmpDao = SpringUtil.getBean(GpBasSCustPickTmpDao.class);
		}
		return gpBasSCustPickTmpDao;
	}
	

	public static ReportWarningDao getReportWarningDao() {
		if (reportWarningDao == null) {
			reportWarningDao = SpringUtil.getBean(ReportWarningDao.class);
		}
		return reportWarningDao;
	}
	
	public static ReportTotaldataService getReportTotaldataServiceImpl() {
		if (reportTotaldataServiceImpl == null) {
			reportTotaldataServiceImpl = SpringUtil.getBean(ReportTotaldataService.class);
		}
		return reportTotaldataServiceImpl;
	}
	
	public static ReportFluctuateDao getReportFluctuateDao() {
		if (reportFluctuateDao == null) {
			reportFluctuateDao = SpringUtil.getBean(ReportFluctuateDao.class);
		}
		return reportFluctuateDao;
	}
	
	public static ReportCustRewardDetailsDao getReportCustRewardDetailsDao() {
		if (reportCustRewardDetailsDao == null) {
			reportCustRewardDetailsDao = SpringUtil.getBean(ReportCustRewardDetailsDao.class);
		}
		return reportCustRewardDetailsDao;
	}
	
	public static WarningHandleDao getWarningHandleDao() {
		if(warningHandleDao == null){
			warningHandleDao= SpringUtil.getBean(WarningHandleDao.class);
		}
		return warningHandleDao;
	}
	public static NotCooperateCustomerDao getNotCooperateCustomerDao() {
		if (notCooperateCustomerDao == null) {
			notCooperateCustomerDao = SpringUtil.getBean(NotCooperateCustomerDao.class);
		}
		return notCooperateCustomerDao;
	}
	public static MarketOccupancyTaoxiDao getMarketOccupancyTaoxiDao() {
		if (marketOccupancyTaoxiDao == null) {
			marketOccupancyTaoxiDao = SpringUtil.getBean(MarketOccupancyTaoxiDao.class);
		}
		return marketOccupancyTaoxiDao;
	}
	
	public static CRMKHReportTotalManageServiceImpl getCRMKHReportTotalManageServiceImpl() {
		if (cRMKHReportTotalManageServiceImpl == null) {
			cRMKHReportTotalManageServiceImpl = SpringUtil.getBean(CRMKHReportTotalManageServiceImpl.class);
		}
		return cRMKHReportTotalManageServiceImpl;
	}
	
	public static StringRedisTemplate getStringRedisTemplate() {
		if (stringRedisTemplate == null) {
			stringRedisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
		}
		return stringRedisTemplate;
	}
}
