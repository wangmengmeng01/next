package com.yunda.base.feiniao.report.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.yunda.base.common.config.Constant;
import com.yunda.base.feiniao.report.bo.Bo_ReportFluctuate;
import com.yunda.base.feiniao.report.dao.ProvinceOdSumDao;
import com.yunda.base.feiniao.report.dao.ReportFluctuateDao;
import com.yunda.base.feiniao.report.dao.ReportJurisdictionTableDao;
import com.yunda.base.feiniao.report.dao.ReportOrderStatsAllDao;
import com.yunda.base.feiniao.report.domain.ExportReportFluctuateCustDO;
import com.yunda.base.feiniao.report.domain.ExportReportFluctuateDataDO;
import com.yunda.base.feiniao.report.domain.ReportFluctuateDO;
import com.yunda.base.feiniao.report.service.ReportFluctuateService;
import com.yunda.base.feiniao.report.utils.CachePrefixConformity;
import com.yunda.base.feiniao.schedule.suckdata.enums.SuckCacheKeyPerfixEnum;
import com.yunda.base.system.domain.UserDO;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.MD5Utils;



@Service
public class ReportFluctuateServiceImpl implements ReportFluctuateService {
	Logger logger = Logger.getLogger(ReportFluctuateServiceImpl.class);
	@Autowired
	private ProvinceOdSumDao provinceOdSumDao;
	@Autowired
	private ReportJurisdictionTableDao reportJurisdictionTableDao;
	@Autowired
	private ReportOrderStatsAllDao reportOrderStatsAllDao;
	
	@Autowired
	private ReportFluctuateDao reportFluctuateDao;
	
	@Autowired
	CqkhCustomerCacheService cqkhCustomerCacheService;
	
	@Autowired
	private RedisTemplate redisTemplate;
	


	@Override
	public List<ReportFluctuateDO> custNumReport(
			Bo_ReportFluctuate boReportFluctuate, UserDO loginUser)
			throws ParseException {
		
		List<ReportFluctuateDO> totalData =new ArrayList<ReportFluctuateDO>();
		ReportFluctuateDO proState =new ReportFluctuateDO();
		String startDate = "";
		String endDate = "";
		//时间段
		if(boReportFluctuate.getStartDate() !=null && ! boReportFluctuate.getStartDate().isEmpty() && boReportFluctuate.getEndDate() !=null && ! boReportFluctuate.getEndDate().isEmpty()){
			startDate = boReportFluctuate.getStartDate();
			endDate = boReportFluctuate.getEndDate();
		}
		//不能选今天和今天之后
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("quDate", endDate);
		int TableNum = reportOrderStatsAllDao.count(map1);
		if(TableNum == 0){
			proState.setCustomerName("rqcw");
			totalData.add(0, proState);
			return totalData;
		}
		
		
		//权限控制----------------------------------------------------------------------------------------------------------------
		if(loginUser.hasPerms(Constant.REPORT_ADMIN_PERMS)) {
			   //超级用户权限   无限制
			//系统菜单配置了report:admin:allperms权限标识   角色管理中当该用户的角色勾选该权限标识就说明该用户是超级用户  能查看所有报表的集团大区省市等所有数据	
		}else{
			if(loginUser.isBigareaqx()){//是否有大区权限
				if(!"city".equals(boReportFluctuate.getShowType()) & !"branch".equals(boReportFluctuate.getShowType()) & !"openAllCustWindow".equals(boReportFluctuate.getShowType()) & !"openBigareaCustWindow".equals(boReportFluctuate.getShowType()) & !"openProvinceCustWindow".equals(boReportFluctuate.getShowType()) & !"openCityCustWindow".equals(boReportFluctuate.getShowType()) & !"openBranchCustWindow".equals(boReportFluctuate.getShowType())){
					boReportFluctuate.setBigareaNames(loginUser.getBigareaNames());
				}
	            boReportFluctuate.setTmpField("D");
	            
			}else if(loginUser.isProvinceqx()){//是否有省权限
				if(!"city".equals(boReportFluctuate.getShowType()) & !"branch".equals(boReportFluctuate.getShowType()) & !"openAllCustWindow".equals(boReportFluctuate.getShowType()) & !"openBigareaCustWindow".equals(boReportFluctuate.getShowType()) & !"openProvinceCustWindow".equals(boReportFluctuate.getShowType()) & !"openCityCustWindow".equals(boReportFluctuate.getShowType()) & !"openBranchCustWindow".equals(boReportFluctuate.getShowType())){
					boReportFluctuate.setProvinceids(loginUser.getProvinceIds());
				}
				boReportFluctuate.setTmpField("S");
			}else {
				if(loginUser.getOrgCode() != null & ! loginUser.getOrgCode().isEmpty()){
				boReportFluctuate.setBranchCode(loginUser.getOrgCode());
				/*if("openBranchCustWindow".equals(boReportFluctuate.getShowType())){
					
				}else{*/
					//boReportFluctuate.setShowType("branch");
					boReportFluctuate.setTmpField("wd");
					proState.setTmpWddl("wddl");
				}
			}	
			/*}else{
				//return"wsq";//无授权	 
				proState.setCustomerName("wsq");
				totalData.add(0, proState);
				return totalData;	 
			}*/
		}
		
		
		// --------------------------------------------------------------------------------------------------------------
		logger.debug("boReportFluctuate:" + boReportFluctuate);
		if("city".equals(boReportFluctuate.getShowType())) {
			totalData=doWithCity(boReportFluctuate,startDate, endDate);
		} else if ("openAllCustWindow".equals(boReportFluctuate.getShowType())) {
			totalData = doWithCustnumber( boReportFluctuate,startDate, endDate,"all");
		} else if ("openBigareaCustWindow".equals(boReportFluctuate.getShowType())) {
			totalData = doWithCustnumber( boReportFluctuate,startDate, endDate,"bigarea");
		}  else if ("openProvinceCustWindow".equals(boReportFluctuate.getShowType())) {
			totalData = doWithCustnumber( boReportFluctuate,startDate, endDate,"province");
		} else if ("openCityCustWindow".equals(boReportFluctuate.getShowType())) {
			totalData = doWithCustnumber( boReportFluctuate,startDate, endDate,"city");
		} else if ("openBranchCustWindow".equals(boReportFluctuate.getShowType())) {
			totalData = doWithCustnumber( boReportFluctuate,startDate, endDate,"branch");
		}  else if ("branch".equals(boReportFluctuate.getShowType())) {
			totalData = doWithBranch( boReportFluctuate,startDate, endDate);
		}else {
			totalData=doWithProvince(boReportFluctuate,startDate, endDate,loginUser);
			
		}
		
		for(ReportFluctuateDO allData:totalData){
			allData.setStartDate(startDate);
			allData.setEndDate(endDate);
		}
		return totalData;
	}
	
	private  List<ReportFluctuateDO> doWithCity(Bo_ReportFluctuate boReportFluctuate,String startDate,String endDate) throws ParseException {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = sdf.parse(startDate);
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		String lastDate = sdf.format(cal.getTime());
		//上月第一天
		final String sssdate = DateUtils.getMonthBegin(lastDate);
		//上月最后一天
		final String eeedate = DateUtils.getMonthEnd(lastDate);
		List<ReportFluctuateDO> allDatas = new ArrayList<ReportFluctuateDO>();
		CachePrefixConformity cache = new CachePrefixConformity();
		ValueOperations<String, List<ReportFluctuateDO>> operations = redisTemplate.opsForValue();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("sssdate", sssdate);
		paramMap.put("eeedate", eeedate);
		paramMap.put("TProvinceId", boReportFluctuate.getProvinceid());
		String paramMapStrin  = JSON.toJSONString(paramMap);
	    String paramMapprefix = MD5Utils.encrypt(paramMapStrin);
		List<ReportFluctuateDO> shengData = operations.get(cache.getSeed(Constant.QUERYBDSINGLEPROVINCEINFO+paramMapprefix,SuckCacheKeyPerfixEnum.bodong.getCode()));
	
		if(shengData == null || shengData.size() < 1){
			//查询日期为2019.1.1日前的，按照老逻辑，2019.1.1日后的（if(true)），按照新逻辑
			if(!DateUtils.parseDate(endDate).before(DateUtils.parseDate(Constant.NEWBEGINRUNTIME))){
				shengData = reportFluctuateDao.getDoWithCityShengNew(paramMap);
			}else{
				shengData = reportFluctuateDao.getDoWithCitySheng(paramMap);
			}
			operations.set(cache.getSeed(Constant.QUERYBDSINGLEPROVINCEINFO+paramMapprefix,SuckCacheKeyPerfixEnum.bodong.getCode()), shengData,86400, TimeUnit.SECONDS);
		}
		// 省份合计
		shengData.get(0).setCustomerName(shengData.get(0).getProvincename()+"合计");
		shengData.get(0).setTmpXuanze("选择省份");
		allDatas.add(shengData.get(0).toReckonList());
		// 循环城市
		//波动某个省的所有市 缓存 key = queryBDCityInfo+改省id+查询时间
		List<ReportFluctuateDO> shiDatas = operations.get(cache.getSeed(Constant.QUERYBDCITYINFO+paramMapprefix,SuckCacheKeyPerfixEnum.bodong.getCode()));
		if(shiDatas == null || shiDatas.size() < 1){
			//查询日期为2019.1.1日前的，按照老逻辑，2019.1.1日后的（if(true)），按照新逻辑
			if(!DateUtils.parseDate(endDate).before(DateUtils.parseDate(Constant.NEWBEGINRUNTIME))){
				shiDatas = reportFluctuateDao.getDoWithCityShiNew(paramMap);
			}else{
				shiDatas = reportFluctuateDao.getDoWithCityShi(paramMap);
			}
			
			operations.set(cache.getSeed(Constant.QUERYBDCITYINFO+paramMapprefix,SuckCacheKeyPerfixEnum.bodong.getCode()), shiDatas,86400, TimeUnit.SECONDS);
		}
		
		for (ReportFluctuateDO cityData : shiDatas) {
			cityData.setBigarea(cityData.getBigarea());
			cityData.setProvinceid(cityData.getProvinceid());
			cityData.setCityid(cityData.getCityid());
			cityData.setCustomerName(cityData.getCityname());
			cityData.setTmpXuanze("选择城市");
			allDatas.add(cityData.toReckonList());
		}
		return allDatas;
	}
	
	private List<ReportFluctuateDO> doWithCustnumber( Bo_ReportFluctuate boReportFluctuate,String startDate,String endDate, String type) throws ParseException {
		String prefix = startDate.replaceAll("-", "") + "_" +endDate.replaceAll("-", "");
		CachePrefixConformity cache = new CachePrefixConformity();
		ValueOperations<String, List<ReportFluctuateDO>> operations = redisTemplate.opsForValue();
		List<ReportFluctuateDO> allDatas = new ArrayList<ReportFluctuateDO>();
		Map<String,Object> custMap = new HashMap<String, Object>();
		custMap.put("startDate", startDate);
		custMap.put("endDate", endDate);
		custMap.put("TBdType",boReportFluctuate.getBdType());
		custMap.put("type", type);
		custMap.put("TProvinceId",boReportFluctuate.getProvinceid());
		custMap.put("TCityId",boReportFluctuate.getCityid());
		custMap.put("TBranchCode",boReportFluctuate.getBranchCode());
		custMap.put("TRegionId",boReportFluctuate.getRegionId());
		custMap.put("offset",boReportFluctuate.getOffset());
		custMap.put("limit", boReportFluctuate.getLimit());
		String custMapStrin  = JSON.toJSONString(custMap);
	    String custMapprefix = MD5Utils.encrypt(custMapStrin);
	    //客户名称 
	  	ValueOperations<String, Map<String, Object>> operationsCqkh = redisTemplate.opsForValue();
		List<ReportFluctuateDO> custDatas = operations.get(cache.getSeed(Constant.QUERYCUSTFLUCTUATEINFO+custMapprefix,SuckCacheKeyPerfixEnum.bodong.getCode()));
		if(custDatas == null || custDatas.size() < 1){
			//查询日期为2019.1.1日前的，按照老逻辑，2019.1.1日后的（if(true)），按照新逻辑
			if(!DateUtils.parseDate(endDate).before(DateUtils.parseDate(Constant.NEWBEGINRUNTIME))){
				custDatas = reportFluctuateDao.getDoWithCustnumberNew(custMap);
			}else{
				custDatas = reportFluctuateDao.getDoWithCustnumber(custMap);
			}
			operations.set(cache.getSeed(Constant.QUERYCUSTFLUCTUATEINFO+custMapprefix,SuckCacheKeyPerfixEnum.bodong.getCode()), custDatas,86400, TimeUnit.SECONDS);
		}
		
		for (ReportFluctuateDO custData : custDatas) {
			Long getOrderAvg = new Double(custData.getOrderAvg()).longValue();
			double getOrderAvgDouble = (double)getOrderAvg;
			custData.setOrderAvg(getOrderAvgDouble);
			//客户名称
			if(custData.getCustomerId() != null && !custData.getCustomerId().isEmpty()){
				Map<String, Object> customerInfo = operationsCqkh.get(custData.getCustomerId());
				if(customerInfo != null && customerInfo.get("khmc") != null) {
					custData.setCustomerName(customerInfo.get("khmc").toString());
				}
			}
			//菜鸟和京东库取商家id和店铺名称
			if("菜鸟".equals(custData.getCustomerSourceType())){
				String customerId = custData.getCustomerId()+"cn";
				Map<String, Object> sellerCNInfo = operationsCqkh.get(customerId);
				if(sellerCNInfo != null && sellerCNInfo.get("seller_id") != null) {
					custData.setSellerId(sellerCNInfo.get("seller_id").toString());
					custData.setSellerName(sellerCNInfo.get("seller_name").toString());
				}
			}
			if("京东".equals(custData.getCustomerSourceType())){
				String customerId = custData.getCustomerId()+"jd";
				Map<String, Object> sellerJDInfo = operationsCqkh.get(customerId);
				if(sellerJDInfo != null && sellerJDInfo.get("vendor_code") != null) {
					custData.setSellerId(sellerJDInfo.get("vendor_code").toString());
					custData.setSellerName(sellerJDInfo.get("vendor_name").toString());
				}
			}
			
			allDatas.add(custData);
		}
		return allDatas;
	}
	
	private List<ReportFluctuateDO> doWithBranch(Bo_ReportFluctuate boReportFluctuate,String startDate,String endDate) throws ParseException {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
		java.util.Date date = sdf.parse(startDate);
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		String lastDate = sdf.format(cal.getTime());
		final String sssdate = DateUtils.getMonthBegin(lastDate);
		final String eeedate = DateUtils.getMonthEnd(lastDate);
		List<ReportFluctuateDO> allDatas = new ArrayList<ReportFluctuateDO>();
		CachePrefixConformity cache = new CachePrefixConformity();
		ValueOperations<String, List<ReportFluctuateDO>> operations = redisTemplate.opsForValue();
		Map<String, Object> BShiMap = new HashMap<String, Object>();
		BShiMap.put("startDate", startDate);
		BShiMap.put("endDate", endDate);
		BShiMap.put("sssdate", sssdate);
		BShiMap.put("eeedate", eeedate);
		BShiMap.put("TCityId", boReportFluctuate.getCityid());
		String BShiMapString = JSON.toJSONString(BShiMap);
	    String BShiMapPrefix = MD5Utils.encrypt(BShiMapString);
		List<ReportFluctuateDO> shiDatas = operations.get(cache.getSeed(Constant.QUERYBDSINGLECITYINFO+BShiMapPrefix,SuckCacheKeyPerfixEnum.bodong.getCode()));
		if(shiDatas==null||shiDatas.size()<1){
			//查询日期为2019.1.1日前的，按照老逻辑，2019.1.1日后的（if(true)），按照新逻辑
			if(!DateUtils.parseDate(endDate).before(DateUtils.parseDate(Constant.NEWBEGINRUNTIME))){
				//市合计
				shiDatas = reportFluctuateDao.getDoWithBranchShiNew(BShiMap);
			}else{
				//市合计
				shiDatas = reportFluctuateDao.getDoWithBranchShi(BShiMap);
			}

			 //查询后的值再放入缓存
			operations.set(cache.getSeed(Constant.QUERYBDSINGLECITYINFO+BShiMapPrefix,SuckCacheKeyPerfixEnum.bodong.getCode()), shiDatas,86400, TimeUnit.SECONDS);
		}
		shiDatas.get(0).setCustomerName(shiDatas.get(0).getCityname()+"合计");
		shiDatas.get(0).setTmpXuanze("选择城市");
		allDatas.add(shiDatas.get(0).toReckonList());
		
		List<ReportFluctuateDO> gsDatas = null;
		Map<String,Object> gsMap = new HashMap<String, Object>();
		gsMap.put("startDate", startDate);
		gsMap.put("endDate", endDate);
		gsMap.put("sssdate", sssdate);
		gsMap.put("eeedate", eeedate);
		if("wd".equals(boReportFluctuate.getTmpField())){
			
				gsMap.put("TbranchCode", boReportFluctuate.getBranchCode());
				String gsMapString = JSON.toJSONString(gsMap);
			    String gsMapPrefix = MD5Utils.encrypt(gsMapString);
			    gsDatas = operations.get(cache.getSeed(Constant.QUERYBDGS1INFO+gsMapPrefix,SuckCacheKeyPerfixEnum.bodong.getCode()));
				// 查询网点
			    if(gsDatas == null || gsDatas.size() < 1){
					//查询日期为2019.1.1日前的，按照老逻辑，2019.1.1日后的（if(true)），按照新逻辑
					if(!DateUtils.parseDate(endDate).before(DateUtils.parseDate(Constant.NEWBEGINRUNTIME))){
						gsDatas = reportFluctuateDao.getDoWithBranchGs1New(gsMap);
					}else{
						gsDatas = reportFluctuateDao.getDoWithBranchGs1(gsMap);
					}
					
					operations.set(cache.getSeed(Constant.QUERYBDGS1INFO+gsMapPrefix,SuckCacheKeyPerfixEnum.bodong.getCode()), gsDatas,86400, TimeUnit.SECONDS);
			    }
			
		}else{
			
			
				gsMap.put("TCityId", boReportFluctuate.getCityid());
				String gsMapString = JSON.toJSONString(gsMap);
			    String gsMapPrefix = MD5Utils.encrypt(gsMapString);
				gsDatas = operations.get(cache.getSeed(Constant.QUERYBDGS2INFO+gsMapPrefix,SuckCacheKeyPerfixEnum.bodong.getCode()));
				// 查询网点
				if(gsDatas == null || gsDatas.size() < 1){
					//查询日期为2019.1.1日前的，按照老逻辑，2019.1.1日后的（if(true)），按照新逻辑
					if(!DateUtils.parseDate(endDate).before(DateUtils.parseDate(Constant.NEWBEGINRUNTIME))){
						gsDatas = reportFluctuateDao.getDoWithBranchGs2New(gsMap);
					}else{
						gsDatas = reportFluctuateDao.getDoWithBranchGs2(gsMap);
					}
					
					operations.set(cache.getSeed(Constant.QUERYBDGS2INFO+gsMapPrefix,SuckCacheKeyPerfixEnum.bodong.getCode()), gsDatas,86400, TimeUnit.SECONDS);
				}
			
		}
		for (ReportFluctuateDO branchData : gsDatas) {
			branchData.setCustomerName(branchData.getBranchName()+"("+branchData.getBranchCode()+")");
			branchData.setTmpXuanze("选择网点");
			allDatas.add(branchData.toReckonList());
		}
		return allDatas;
	}
	
	private List<ReportFluctuateDO> doWithProvince(Bo_ReportFluctuate boReportFluctuate,String startDate,String endDate,UserDO loginUser) throws ParseException {
		String prefix = startDate.replaceAll("-", "") + "_" +endDate.replaceAll("-", "");
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
		java.util.Date date = sdf.parse(startDate);
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		String lastDate = sdf.format(cal.getTime());
		final String sssdate = DateUtils.getMonthBegin(lastDate);
		final String eeedate = DateUtils.getMonthEnd(lastDate);
		List<ReportFluctuateDO> allDatas = new ArrayList<ReportFluctuateDO>();
		CachePrefixConformity cache = new CachePrefixConformity();
		
		ValueOperations<String, List<ReportFluctuateDO>> operations = redisTemplate.opsForValue();
		//如果是网点权限 
		if("wd".equals(boReportFluctuate.getTmpField())){
			
		    	List<ReportFluctuateDO> gsDatas = null;
		    	Map<String,Object> gsMap = new HashMap<String, Object>();
				gsMap.put("startDate", startDate);
				gsMap.put("endDate", endDate);
				gsMap.put("sssdate", sssdate);
				gsMap.put("eeedate", eeedate);
				gsMap.put("TbranchCode", boReportFluctuate.getBranchCode());
				
				String gsMapString = JSON.toJSONString(gsMap);
			    String gsMapPrefix = MD5Utils.encrypt(gsMapString);
			    gsDatas = operations.get(cache.getSeed(Constant.QUERYBDGS1INFO+gsMapPrefix,SuckCacheKeyPerfixEnum.bodong.getCode()));
				// 查询网点
			    if(gsDatas == null || gsDatas.size() < 1){
					//查询日期为2019.1.1日前的，按照老逻辑，2019.1.1日后的（if(true)），按照新逻辑
					if(!DateUtils.parseDate(endDate).before(DateUtils.parseDate(Constant.NEWBEGINRUNTIME))){
						gsDatas = reportFluctuateDao.getDoWithBranchGs1New(gsMap);
					}else{
						gsDatas = reportFluctuateDao.getDoWithBranchGs1(gsMap);
					}

					operations.set(cache.getSeed(Constant.QUERYBDGS1INFO+gsMapPrefix,SuckCacheKeyPerfixEnum.bodong.getCode()), gsDatas,86400, TimeUnit.SECONDS);
			    }
			    
			    for (ReportFluctuateDO branchData : gsDatas) {
					branchData.setCustomerName(branchData.getBranchName()+"("+branchData.getBranchCode()+")网点");
					branchData.setTmpXuanze("选择网点");
					allDatas.add(branchData);
				}
		}else {
	    
		// 集团合计
		//从缓存取值  
		//波动集团合计 缓存  key  = queryBDCompanyInfo+查询时间
		Map<String,Object> allCountMap = new HashMap<String, Object>();
		allCountMap.put("startDate", startDate);
		allCountMap.put("endDate", endDate);
		allCountMap.put("sssdate", sssdate);
		allCountMap.put("eeedate", eeedate);
		String allCountMapString = JSON.toJSONString(allCountMap);
	    String allCountMapPrefix = MD5Utils.encrypt(allCountMapString);
		List<ReportFluctuateDO> allcountDatas = operations.get(cache.getSeed(Constant.QUERYBDCOMPANYINFO+allCountMapPrefix,SuckCacheKeyPerfixEnum.bodong.getCode()));
		if(allcountDatas==null||allcountDatas.size()<1){
			//查询日期为2019.1.1日前的，按照老逻辑，2019.1.1日后的（if(true)），按照新逻辑
			if(!DateUtils.parseDate(endDate).before(DateUtils.parseDate(Constant.NEWBEGINRUNTIME))){
				allcountDatas = reportFluctuateDao.getDoWithProvinceAllCountDataNew(allCountMap);
			}else{
				allcountDatas = reportFluctuateDao.getDoWithProvinceAllCountData(allCountMap);
			}
			
			 //查询后的值再放入缓存
			operations.set(cache.getSeed(Constant.QUERYBDCOMPANYINFO+allCountMapPrefix,SuckCacheKeyPerfixEnum.bodong.getCode()), allcountDatas,86400, TimeUnit.SECONDS);
		}
		
		logger.debug("集团合计:" + allcountDatas);
		
		//没有超级权限  不显示集团合计
		if("D".equals(boReportFluctuate.getTmpField())){
		}else if("S".equals(boReportFluctuate.getTmpField())){
		}else{
		allcountDatas.get(0).setCustomerName("集团合计");
		allcountDatas.get(0).setTmpXuanze("集团合计");
		allDatas.add(allcountDatas.get(0).toReckonList());
		}
		//用户大区数据
		Map<String,Object>  areaMap = new HashMap<String, Object>();
		areaMap.put("startDate", startDate);
		areaMap.put("endDate", endDate);
		areaMap.put("sssdate", sssdate);
		areaMap.put("eeedate", eeedate);
		areaMap.put("TRegionId", boReportFluctuate.getBigareaNames());
		areaMap.put("TTmpField", boReportFluctuate.getTmpField());
		String areaMapString = JSON.toJSONString(areaMap);
	    String areaMapPrefix = MD5Utils.encrypt(areaMapString);
		//从缓存取大区数据  
		List<ReportFluctuateDO> allBigareaDatas = operations.get(cache.getSeed(Constant.QUERYBDBIGAREAINFO+areaMapPrefix,SuckCacheKeyPerfixEnum.bodong.getCode()));
		//若没有大区数据，则从数据库查
		if(allBigareaDatas==null||allBigareaDatas.size()<1){
			//查询日期为2019.1.1日前的，按照老逻辑，2019.1.1日后的（if(true)），按照新逻辑
			if(!DateUtils.parseDate(endDate).before(DateUtils.parseDate(Constant.NEWBEGINRUNTIME))){
				allBigareaDatas = reportFluctuateDao.getDoWithProvinceBigAreaNew(areaMap);
			}else{
				allBigareaDatas = reportFluctuateDao.getDoWithProvinceBigArea(areaMap);
			}
			
			//再放入缓存
			operations.set(cache.getSeed(Constant.QUERYBDBIGAREAINFO+areaMapPrefix,SuckCacheKeyPerfixEnum.bodong.getCode()), allBigareaDatas,86400, TimeUnit.SECONDS);
		}
		logger.debug("大区合计:" + allBigareaDatas);
		Map<String,Object>  shengMap = new HashMap<String, Object>();
		//循环大区
		for (ReportFluctuateDO bigareaData : allBigareaDatas) {
			if(StringUtils.isEmpty(bigareaData.getBigarea())) {
				continue;
			}
			// 大区合计 
			//如果只有省权限，则不显示大区数据
			//如果只有某个省的权限，则没有该省对应的大区权限
			if("S".equals(boReportFluctuate.getTmpField())){
				
			}else{
			bigareaData.setCustomerName(bigareaData.getBigarea()+"合计");
			bigareaData.setTmpXuanze("大区合计");
			allDatas.add(bigareaData.toReckonList());
			
			}
			//List<ReportFluctuateDO> shengDatas = null;
			//用户每个大区下面的省数据，缓存 key = queryBDProvinceInfo+用户id+大区名+查询时间
			
			
			shengMap.put("startDate", startDate);
			shengMap.put("endDate", endDate);
			shengMap.put("sssdate", sssdate);
			shengMap.put("eeedate", eeedate);
			shengMap.put("DBigArea", bigareaData.getBigarea());
			shengMap.put("TProvinceID",boReportFluctuate.getProvinceids());
			shengMap.put("tProvinceID",boReportFluctuate.getProvinceids());
			shengMap.put("TTmpField", boReportFluctuate.getTmpField());
			String shengMapString = JSON.toJSONString(shengMap);
		    String shengMapPrefix = MD5Utils.encrypt(shengMapString);
		    List<ReportFluctuateDO> shengDatas = operations.get(cache.getSeed(Constant.QUERYBDPROVINCEINFO+shengMapPrefix,SuckCacheKeyPerfixEnum.bodong.getCode()));
		    if(shengDatas==null||shengDatas.size()<1){
				//查询日期为2019.1.1日前的，按照老逻辑，2019.1.1日后的（if(true)），按照新逻辑
				if(!DateUtils.parseDate(endDate).before(DateUtils.parseDate(Constant.NEWBEGINRUNTIME))){
					shengDatas = reportFluctuateDao.getDoWithProvinceShengNew(shengMap);
				}else{
					shengDatas = reportFluctuateDao.getDoWithProvinceSheng(shengMap);
				}
				//再放入缓存
				operations.set(cache.getSeed(Constant.QUERYBDPROVINCEINFO+shengMapPrefix,SuckCacheKeyPerfixEnum.bodong.getCode()), shengDatas,86400, TimeUnit.SECONDS);
		    }
			shengMap.clear();
			// 循环省份
			for (ReportFluctuateDO shengData : shengDatas) {
				// 省份合计
				shengData.setCustomerName(shengData.getProvincename());
				shengData.setProvinceid(shengData.getProvinceid());
				shengData.setBigarea(shengData.getBigarea());
				shengData.setTmpXuanze("选择省份");
				allDatas.add(shengData.toReckonList());
			}
		}
		}
		return allDatas;
	}

	@Override
	public List<ExportReportFluctuateDataDO> filterData(List<ReportFluctuateDO> reportFluctuateData) {
		
		List<ExportReportFluctuateDataDO> fluctuateData = new ArrayList<ExportReportFluctuateDataDO>();
		ExportReportFluctuateDataDO newFluctuate = new ExportReportFluctuateDataDO();
		
		for(ReportFluctuateDO data : reportFluctuateData){		
		     if( data.getCustomerName() !=null&& !"".equals(data.getCustomerName())){
		    	 if("no".equals(data.getCustomerName())||"wait".equals(data.getCustomerName())||"rqcw".equals(data.getCustomerName())||"bt".equals(data.getCustomerName())){    		  
		    	 }else{
		    		 newFluctuate = new ExportReportFluctuateDataDO();
		    		 
		    		 newFluctuate.setCustomerName(data.getCustomerName());
		    		 newFluctuate.setCzCustomerSum(data.getCzCustomerSum());
		    		 newFluctuate.setLostCustomerSum(data.getLostCustomerSum());
		    		 newFluctuate.setAddCustomerSum(data.getAddCustomerSum());
		    		 newFluctuate.setCzBCustomerSum(data.getCzBCustomerSum());
		    		 newFluctuate.setBlostCustomerSum(data.getBLostCustomerSum());
		    		 newFluctuate.setBaddCustomerSum(data.getBAddCustomerSum());
		    		 newFluctuate.setCzCCustomerSum(data.getCzCCustomerSum());
		    		 newFluctuate.setClostCustomerSum(data.getCLostCustomerSum());
		    		 newFluctuate.setCaddCustomerSum(data.getCAddCustomerSum());
		    		 newFluctuate.setCzDCustomerSum(data.getCzDCustomerSum());
		    		 newFluctuate.setDlostCustomerSum(data.getDLostCustomerSum());
		    		 newFluctuate.setDaddCustomerSum(data.getDAddCustomerSum());
		    		 newFluctuate.setCzECustomerSum(data.getCzECustomerSum());
		    		 newFluctuate.setElostCustomerSum(data.getELostCustomerSum());
		    		 newFluctuate.setEaddCustomerSum(data.getEAddCustomerSum());
		    		 newFluctuate.setCzFCustomerSum(data.getCzFCustomerSum());
		    		 newFluctuate.setFlostCustomerSum(data.getFLostCustomerSum());
		    		 newFluctuate.setFaddCustomerSum(data.getFAddCustomerSum());
		    		 newFluctuate.setCzGCustomerSum(data.getCzGCustomerSum());
		    		 newFluctuate.setGlostCustomerSum(data.getGLostCustomerSum());
		    		 newFluctuate.setGaddCustomerSum(data.getGAddCustomerSum());
		    		 
		    		 fluctuateData.add(newFluctuate);		    		 
		    	 }
		     }	 
		}      
		return fluctuateData;
	}
	
	
	
	@Override
	public List<ExportReportFluctuateCustDO> filterDataCust(List<ReportFluctuateDO> reportFluctuateData) {
		
		List<ExportReportFluctuateCustDO> fluctuateData = new ArrayList<ExportReportFluctuateCustDO>();
		ExportReportFluctuateCustDO newFluctuate = new ExportReportFluctuateCustDO();
		
		for(ReportFluctuateDO data : reportFluctuateData){		
		     if(data.getBigarea() !=null&& !"".equals(data.getBigarea())){
		    		 newFluctuate = new ExportReportFluctuateCustDO();
		    		 
		    		 newFluctuate.setBigarea(data.getBigarea());
		    		 newFluctuate.setProvincename(data.getProvincename());
		    		 newFluctuate.setCityname(data.getCityname());
		    		 newFluctuate.setBranchName(data.getBranchName());
		    		 newFluctuate.setCustomerId(data.getCustomerId());
		    		 newFluctuate.setCustomerName(data.getCustomerName());
		    		 newFluctuate.setSellerId(data.getSellerId());
		    		 newFluctuate.setSellerName(data.getSellerName());
		    		 newFluctuate.setCustomerSourceType(data.getCustomerSourceType());
		    		 newFluctuate.setOrderSum(data.getOrderSum());
		    		 newFluctuate.setOrderAvg(data.getOrderAvg());
		    		 newFluctuate.setShowPriceLevel(data.getShowPriceLevel());
		    		 newFluctuate.setBorderSum(data.getBOrderSum());
		    		 newFluctuate.setBorderAvg(data.getBOrderAvg());
		    		 newFluctuate.setShowBPriceLevel(data.getShowBPriceLevel());
		    		 
		    		 fluctuateData.add(newFluctuate);		    		 
		    	 
		     }	 
		}      
		return fluctuateData;
	}

	@Override
	public int custBDcount(Bo_ReportFluctuate boReportFluctuate,
			UserDO loginUser) throws ParseException {
		String startDate = "";
		String endDate = "";
		//时间段
		if(boReportFluctuate.getStartDate() !=null && ! boReportFluctuate.getStartDate().isEmpty() && boReportFluctuate.getEndDate() !=null && ! boReportFluctuate.getEndDate().isEmpty()){
			startDate = boReportFluctuate.getStartDate();
			endDate = boReportFluctuate.getEndDate();
		}
		int resultNumber = 0;
		if("openAllCustWindow".equals(boReportFluctuate.getShowType())) {
			resultNumber = doWithCustnumberCount( boReportFluctuate,startDate, endDate,"all");
		} else if ("openBigareaCustWindow".equals(boReportFluctuate.getShowType())) {
			resultNumber = doWithCustnumberCount( boReportFluctuate,startDate, endDate,"bigarea");
		}  else if ("openProvinceCustWindow".equals(boReportFluctuate.getShowType())) {
			resultNumber = doWithCustnumberCount( boReportFluctuate,startDate, endDate,"province");
		} else if ("openCityCustWindow".equals(boReportFluctuate.getShowType())) {
			resultNumber = doWithCustnumberCount( boReportFluctuate,startDate, endDate,"city");
		} else if ("openBranchCustWindow".equals(boReportFluctuate.getShowType())) {
			resultNumber = doWithCustnumberCount( boReportFluctuate,startDate, endDate,"branch");
		}
		
		return resultNumber;
	}
	
	int doWithCityCount(Bo_ReportFluctuate boReportFluctuate,String startDate,String endDate) throws ParseException {
		
		
		return 3;
		
	}
		
		
	int doWithCustnumberCount(Bo_ReportFluctuate boReportFluctuate,String startDate,String endDate, String type) throws ParseException {
			
			Map<String,Object> custMap = new HashMap<String, Object>();
			custMap.put("startDate", startDate);
			custMap.put("endDate", endDate);
			custMap.put("TBdType",boReportFluctuate.getBdType());
			custMap.put("type", type);
			custMap.put("TProvinceId",boReportFluctuate.getProvinceid());
			custMap.put("TCityId",boReportFluctuate.getCityid());
			custMap.put("TBranchCode",boReportFluctuate.getBranchCode());
			custMap.put("TRegionId",boReportFluctuate.getRegionId());
			custMap.put("offset",boReportFluctuate.getOffset());
			custMap.put("limit", boReportFluctuate.getLimit());
			
			//查询日期为2019.1.1日前的，按照老逻辑，2019.1.1日后的（if(true)），按照新逻辑
			if(!DateUtils.parseDate(endDate).before(DateUtils.parseDate(Constant.NEWBEGINRUNTIME))){
				return reportFluctuateDao.custBDcountNew(custMap);
			}else{
				return reportFluctuateDao.custBDcount(custMap);
			}
			
			
		}
	
	
	int doWithProvinceCount(Bo_ReportFluctuate boReportFluctuate,String startDate,String endDate, UserDO loginUser) throws ParseException {
		List<ReportFluctuateDO> totalData=doWithProvince(boReportFluctuate,startDate, endDate,loginUser);
		if(totalData != null){
			return totalData.size();
		}else{
			return 0;
		}
		
	}
		
}

	
	
	

