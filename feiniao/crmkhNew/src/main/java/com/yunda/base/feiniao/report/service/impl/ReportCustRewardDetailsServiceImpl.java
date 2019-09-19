package com.yunda.base.feiniao.report.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.MD5Utils;
import com.alibaba.fastjson.JSON;
import com.yunda.base.common.config.Constant;
import com.yunda.base.feiniao.report.bo.Bo_reportCustRewardDetails_list;
import com.yunda.base.feiniao.report.dao.ReportCustRewardDetailsDao;
import com.yunda.base.feiniao.report.dao.ReportOrderStatsAllDao;
import com.yunda.base.feiniao.report.domain.ExportBranchCRDDataDO;
import com.yunda.base.feiniao.report.domain.ExportCRDDataDO;
import com.yunda.base.feiniao.report.domain.ReportCustRewardDetailsDO;
import com.yunda.base.feiniao.report.service.ReportCustRewardDetailsService;
import com.yunda.base.feiniao.report.utils.CachePrefixConformity;
import com.yunda.base.feiniao.schedule.suckdata.enums.SuckCacheKeyPerfixEnum;
import com.yunda.base.system.domain.UserDO;

@Service
public class ReportCustRewardDetailsServiceImpl implements ReportCustRewardDetailsService {
	Logger log = Logger.getLogger(getClass());

	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private ReportCustRewardDetailsDao reportCustRewardDetailsDao;

	@Autowired
	private ReportOrderStatsAllDao reportOrderStatsAllDao;

	@Autowired
	private CqkhCustomerCacheService cqkhCustomerCacheService;

	@Override
	public List<ReportCustRewardDetailsDO> getCustRewardDetailsList(Bo_reportCustRewardDetails_list boReportCustRewardDetailsList, UserDO loginUser) throws Exception {

		List<ReportCustRewardDetailsDO> totalData = new ArrayList<ReportCustRewardDetailsDO>();
		// ReportCustRewardDetailsDO proState =new ReportCustRewardDetailsDO();
		String startDate = "";
		String endDate = "";
		// 时间段
		if (boReportCustRewardDetailsList.getStartDate() != null && !boReportCustRewardDetailsList.getStartDate().isEmpty() && boReportCustRewardDetailsList.getEndDate() != null && !boReportCustRewardDetailsList.getEndDate().isEmpty()) {
			startDate = boReportCustRewardDetailsList.getStartDate();
			endDate = boReportCustRewardDetailsList.getEndDate();
		}
		// 不能选今天和今天之后
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("quDate", endDate);
		int TableNum = reportOrderStatsAllDao.count(map1);
		if (TableNum == 0) {
			// 没有当前日期数据，前台做一个判断
			// proState.setCustomerName("rqcw");
			// totalData.add(0, proState);
			return totalData;
		}

		// 权限控制----------------------------------------------------------------------------------------------------------------
		if (loginUser.hasPerms(Constant.REPORT_ADMIN_PERMS)) {
			// 超级用户权限 无限制
			// 系统菜单配置了report:admin:allperms权限标识 角色管理中当该用户的角色勾选该权限标识就说明该用户是超级用户
			// 能查看所有报表的集团大区省市等所有数据
		} else {
			if (loginUser.isBigareaqx()) {// 是否有大区权限
				boReportCustRewardDetailsList.setBigareaNames(loginUser.getBigareaNames());
				boReportCustRewardDetailsList.setTmpField("D");

			} else if (loginUser.isProvinceqx()) {// 是否有省权限
				boReportCustRewardDetailsList.setProvinceids(loginUser.getProvinceIds());
				boReportCustRewardDetailsList.setTmpField("S");
			} else if (loginUser.getOrgCode() != null & !loginUser.getOrgCode().isEmpty()) {// 只有某网点权限
				boReportCustRewardDetailsList.setBranchCode(loginUser.getOrgCode());
				boReportCustRewardDetailsList.setTmpField("wd");
			} else {
				// return"wsq";//无授权
				// proState.setCustomerName("wsq");
				// totalData.add(0, proState);
				return totalData;
			}
		}

		return getCRDList(boReportCustRewardDetailsList, startDate, endDate);
	}

	private List<ReportCustRewardDetailsDO> getCRDList(Bo_reportCustRewardDetails_list boReportCustRewardDetailsList, String startDate, String endDate) {
		CachePrefixConformity cache = new CachePrefixConformity();
		ValueOperations<String, List<ReportCustRewardDetailsDO>> operations = redisTemplate.opsForValue();
		// 客户名称
		ValueOperations<String, Map<String, Object>> operationsCqkh = redisTemplate.opsForValue();

		Map<String, Object> khJLMap = new HashMap<String, Object>();
		khJLMap.put("startDate", startDate);
		khJLMap.put("endDate", endDate);
		khJLMap.put("gs", boReportCustRewardDetailsList.getGs());
		khJLMap.put("customerId", boReportCustRewardDetailsList.getCustomerId());
		khJLMap.put("customerSourceType", boReportCustRewardDetailsList.getCustomerSourceType());
		khJLMap.put("startOrderSum", boReportCustRewardDetailsList.getStartOrderSum());
		khJLMap.put("endOrderSum", boReportCustRewardDetailsList.getEndOrderSum());
		khJLMap.put("TRegionId", boReportCustRewardDetailsList.getBigareaNames());
		khJLMap.put("TProvinceID", boReportCustRewardDetailsList.getProvinceids());
		khJLMap.put("TBranchCode", boReportCustRewardDetailsList.getBranchCode());
		khJLMap.put("TTmpField", boReportCustRewardDetailsList.getTmpField());
    	if(boReportCustRewardDetailsList.getLimit() != 0){
    		khJLMap.put("offset", boReportCustRewardDetailsList.getOffset());
    		khJLMap.put("limit", boReportCustRewardDetailsList.getLimit());
    	}
		//一类省份id
		if(DateUtils.parseDate(endDate).before(DateUtils.parseDate(Constant.FIRST_PROVINCEIDSBEFORE2019_WORK))){
			khJLMap.put("firstProvinceids", Constant.FIRST_PROVINCEIDSBEFORE2019);
		}else{
			khJLMap.put("firstProvinceids", Constant.FIRST_PROVINCEIDS);
		}

		khJLMap = readyMap(startDate, khJLMap);
		String khJLMapStrin = JSON.toJSONString(khJLMap);
		String khJLMapprefix = MD5Utils.encrypt(khJLMapStrin);

		List<ReportCustRewardDetailsDO> allDatas = operations.get(cache.getSeed(Constant.QUERYKHJLMXINFO + khJLMapprefix, SuckCacheKeyPerfixEnum.jianglimingxi.getCode()));
		if (allDatas == null || allDatas.size() < 1) {
			//查询日期为2019.1.1日前的，按照老逻辑，2019.1.1日后的（if(true)），按照新逻辑
			if(!DateUtils.parseDate(endDate).before(DateUtils.parseDate(Constant.NEWBEGINRUNTIME))){
				//新逻辑账号是网点权限，不显示返利金额
				if("wd".equals(boReportCustRewardDetailsList.getTmpField())){
					allDatas = reportCustRewardDetailsDao.getBranchCustRewardDetailsListNew(khJLMap);
				}else{
					allDatas = reportCustRewardDetailsDao.getCustRewardDetailsListNew(khJLMap);
				}
			}else{
				//老逻辑账号是网点权限，不显示返利金额
				if("wd".equals(boReportCustRewardDetailsList.getTmpField())){
					allDatas = reportCustRewardDetailsDao.getBranchCustRewardDetailsList(khJLMap);
				}else{
					allDatas = reportCustRewardDetailsDao.getCustRewardDetailsList(khJLMap);
				}

			}
			operations.set(cache.getSeed(Constant.QUERYKHJLMXINFO + khJLMapprefix, SuckCacheKeyPerfixEnum.jianglimingxi.getCode()), allDatas, 86400, TimeUnit.SECONDS);
		}

		for (ReportCustRewardDetailsDO aa : allDatas) {
			if (aa.getCustomerName() == null || aa.getCustomerName().isEmpty() || "-".equals(aa.getCustomerName())) {
				Map<String, Object> customerInfo = operationsCqkh.get(aa.getCustomerId());
				if (customerInfo != null && customerInfo.get("khmc") != null) {
					aa.setCustomerName(customerInfo.get("khmc").toString());
				}
			}
			//菜鸟和京东库取商家id和店铺名称
			if("1".equals(aa.getCustomerSourceType())){
				String customerId = aa.getCustomerId()+"cn";
				Map<String, Object> sellerCNInfo = operationsCqkh.get(customerId);
				if(sellerCNInfo != null && sellerCNInfo.get("seller_id") != null) {
					aa.setSellerId(sellerCNInfo.get("seller_id").toString());
					aa.setSellerName(sellerCNInfo.get("seller_name").toString());
				}
			}
			if("4".equals(aa.getCustomerSourceType())){
				String customerId = aa.getCustomerId()+"jd";
				Map<String, Object> sellerJDInfo = operationsCqkh.get(customerId);
				if(sellerJDInfo != null && sellerJDInfo.get("vendor_code") != null) {
					aa.setSellerId(sellerJDInfo.get("vendor_code").toString());
					aa.setSellerName(sellerJDInfo.get("vendor_name").toString());
				}
			}
			if("5".equals(aa.getCustomerSourceType())){
				String customerId = aa.getCustomerId()+"pdd";
				Map<String, Object> sellerPDDInfo = operationsCqkh.get(customerId);
				if(sellerPDDInfo != null && sellerPDDInfo.get("vendor_code") != null) {
					aa.setSellerId(sellerPDDInfo.get("vendor_code").toString());
					aa.setSellerName(sellerPDDInfo.get("vendor_name").toString());
				}
			}
		}
		return allDatas;
	}

	@Override
	public int getCustRewardDetailsCount(Bo_reportCustRewardDetails_list boReportCustRewardDetailsList, UserDO loginUser) throws Exception {

		// 权限控制----------------------------------------------------------------------------------------------------------------
		if (loginUser.hasPerms(Constant.REPORT_ADMIN_PERMS)) {
			// 超级用户权限 无限制
			// 系统菜单配置了report:admin:allperms权限标识 角色管理中当该用户的角色勾选该权限标识就说明该用户是超级用户
			// 能查看所有报表的集团大区省市等所有数据
		} else {
			if (loginUser.isBigareaqx()) {// 是否有大区权限
				boReportCustRewardDetailsList.setBigareaNames(loginUser.getBigareaNames());
				boReportCustRewardDetailsList.setTmpField("D");

			} else if (loginUser.isProvinceqx()) {// 是否有省权限
				boReportCustRewardDetailsList.setProvinceids(loginUser.getProvinceIds());
				boReportCustRewardDetailsList.setTmpField("S");
			} else if (loginUser.getOrgCode() != null & !loginUser.getOrgCode().isEmpty()) {// 只有某网点权限
				boReportCustRewardDetailsList.setBranchCode(loginUser.getOrgCode());
				boReportCustRewardDetailsList.setTmpField("wd");
			} else {
				// return"wsq";//无授权
				// proState.setCustomerName("wsq");
				// totalData.add(0, proState);
				return 0;
			}
		}

		Map<String, Object> khJLMap = new HashMap<String, Object>();
		khJLMap.put("startDate", boReportCustRewardDetailsList.getStartDate());
		khJLMap.put("endDate", boReportCustRewardDetailsList.getEndDate());
		khJLMap.put("gs", boReportCustRewardDetailsList.getGs());
		khJLMap.put("customerId", boReportCustRewardDetailsList.getCustomerId());
		khJLMap.put("customerSourceType", boReportCustRewardDetailsList.getCustomerSourceType());
		khJLMap.put("startOrderSum", boReportCustRewardDetailsList.getStartOrderSum());
		khJLMap.put("endOrderSum", boReportCustRewardDetailsList.getEndOrderSum());
		khJLMap.put("TRegionId", boReportCustRewardDetailsList.getBigareaNames());
		khJLMap.put("TProvinceID", boReportCustRewardDetailsList.getProvinceids());
		khJLMap.put("TBranchCode", boReportCustRewardDetailsList.getBranchCode());
		khJLMap.put("TTmpField", boReportCustRewardDetailsList.getTmpField());
		//一类省份id
		if(DateUtils.parseDate(boReportCustRewardDetailsList.getEndDate()).before(DateUtils.parseDate(Constant.FIRST_PROVINCEIDSBEFORE2019_WORK))){
			khJLMap.put("firstProvinceids", Constant.FIRST_PROVINCEIDSBEFORE2019);
		}else{
			khJLMap.put("firstProvinceids", Constant.FIRST_PROVINCEIDS);
		}
		khJLMap = readyMap( boReportCustRewardDetailsList.getStartDate(), khJLMap);
		String khJLMapStrin = JSON.toJSONString(khJLMap);
		String khJLMapprefix = MD5Utils.encrypt(khJLMapStrin);
		ValueOperations<String, Integer> operations = redisTemplate.opsForValue();
		CachePrefixConformity cache = new CachePrefixConformity();
		Integer result = operations.get(cache.getSeed(Constant.QUERYKHJLMXINFOCOUNT + khJLMapprefix, SuckCacheKeyPerfixEnum.jianglimingxi.getCode()));
		if (result == null || result  < 1) {
			//查询日期为2019.1.1日前的，按照老逻辑，2019.1.1日后的（if(true)），按照新逻辑
			if(!DateUtils.parseDate(boReportCustRewardDetailsList.getEndDate()).before(DateUtils.parseDate(Constant.NEWBEGINRUNTIME))){
				result = reportCustRewardDetailsDao.getCustRewardDetailsCountNew(readyMap(boReportCustRewardDetailsList.getStartDate(), khJLMap));
			}else{
				result = reportCustRewardDetailsDao.getCustRewardDetailsCount(readyMap(boReportCustRewardDetailsList.getStartDate(), khJLMap));
			}
			operations.set(cache.getSeed(Constant.QUERYKHJLMXINFOCOUNT + khJLMapprefix, SuckCacheKeyPerfixEnum.jianglimingxi.getCode()), result, 86400, TimeUnit.SECONDS);
		}

		
		return result;
	}

	@Override
	public List<ReportCustRewardDetailsDO> getCustBraData(String queryInfo) throws Exception {

		return reportCustRewardDetailsDao.getCustBraData(queryInfo);
	}

	@Override
	public List<ExportCRDDataDO> filterData(List<ReportCustRewardDetailsDO> CRData) {
		List<ExportCRDDataDO> crdData = new ArrayList<ExportCRDDataDO>();
		ExportCRDDataDO newCRD = new ExportCRDDataDO();

		for (ReportCustRewardDetailsDO data : CRData) {
			if (data.getCustomerName() != null && !"".equals(data.getCustomerName())) {

				newCRD = new ExportCRDDataDO();
				newCRD.setRecordId(data.getRecordId());
				newCRD.setBigarea(data.getBigarea());
				newCRD.setProvinceName(data.getProvinceName());
				newCRD.setCityName(data.getCityName());
				newCRD.setMc(data.getMc());
				newCRD.setGs(data.getGs());
				newCRD.setBranchCode(data.getBranchCode());
				newCRD.setCustomerId(data.getCustomerId());
				newCRD.setCustomerName(data.getCustomerName());
				newCRD.setSellerId(data.getSellerId());
				newCRD.setSellerName(data.getSellerName());
				newCRD.setCustomerSourceType(data.getCustomerSourceType());
				newCRD.setOrderSum(data.getOrderSum());
				newCRD.setOrderAvg(data.getOrderAvg());
				if(null != data.getAllPriceSum()){
					newCRD.setAllPriceSum(data.getAllPriceSum());
				}

				newCRD.setCustLevel(data.getCustLevel());
				crdData.add(newCRD);

			}
		}
		return crdData;
	}
	
	@Override
	public List<ExportBranchCRDDataDO> filterBranchData(List<ReportCustRewardDetailsDO> CRData) {
		List<ExportBranchCRDDataDO> crdData = new ArrayList<ExportBranchCRDDataDO>();
		ExportBranchCRDDataDO newCRD = new ExportBranchCRDDataDO();

		for (ReportCustRewardDetailsDO data : CRData) {
			if (data.getCustomerName() != null && !"".equals(data.getCustomerName())) {
				if ("1".equals(data.getCustomerSourceType())) {
					data.setCustomerSourceType("菜鸟");
				} else if ("2".equals(data.getCustomerSourceType())) {
					data.setCustomerSourceType("二维码");
				} else if ("4".equals(data.getCustomerSourceType())) {
					data.setCustomerSourceType("京东");
				} else if ("5".equals(data.getCustomerSourceType())) {
					data.setCustomerSourceType("拼多多");
				}
				
				if ("a".equals(data.getCustLevel())) {
					data.setCustLevel("A类");
				} else if ("b".equals(data.getCustLevel())) {
					data.setCustLevel("B类");
				} else if ("c".equals(data.getCustLevel())) {
					data.setCustLevel("C类");
				} else if ("d".equals(data.getCustLevel())) {
					data.setCustLevel("D类");
				} else if ("e".equals(data.getCustLevel())) {
					data.setCustLevel("E类");
				} else if ("f".equals(data.getCustLevel())) {
					data.setCustLevel("F类");
				} else if ("g".equals(data.getCustLevel())) {
					data.setCustLevel("G类");
				}
				
				newCRD = new ExportBranchCRDDataDO();
				newCRD.setRecordId(data.getRecordId());
				newCRD.setBigarea(data.getBigarea());
				newCRD.setProvinceName(data.getProvinceName());
				newCRD.setCityName(data.getCityName());
				newCRD.setMc(data.getMc());
				newCRD.setGs(data.getGs());
				newCRD.setBranchCode(data.getBranchCode());
				newCRD.setCustomerId(data.getCustomerId());
				newCRD.setCustomerName(data.getCustomerName());
				newCRD.setSellerId(data.getSellerId());
				newCRD.setSellerName(data.getSellerName());
				newCRD.setCustomerSourceType(data.getCustomerSourceType());
				newCRD.setOrderSum(data.getOrderSum());
				newCRD.setOrderAvg(data.getOrderAvg());
				newCRD.setCustLevel(data.getCustLevel());
				crdData.add(newCRD);
			}
		}
		return crdData;
	}

	@Override
	public List<Map<String, Object>> searchCustomerBraData(Map<String, Object> map, UserDO user) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		String orgType = "";
		if (!StringUtils.isBlank(user.getOrgCode())) {
			/**
			 * 查询公司类别
			 */
			result = reportCustRewardDetailsDao.searchLbByGsInfo(user.getOrgCode());
		}
		if (result.size() > 0) {
			orgType = result.get(0).get("lb").toString();
		}
		if (StringUtils.isEmpty(orgType)) {
			orgType = user.getOrgType();
		}
		map.put("orgType", orgType);
		map.put("userId", user.getUserId());

		List<Map<String, Object>> customerList = reportCustRewardDetailsDao.searchCustBraData(map);
		return customerList;
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
