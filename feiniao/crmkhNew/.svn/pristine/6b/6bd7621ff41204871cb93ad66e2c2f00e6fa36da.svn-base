package com.yunda.base.feiniao.report.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.yunda.base.common.config.Constant;
import com.yunda.base.common.multi.annotation.DataSourceAnnotation;
import com.yunda.base.feiniao.report.bo.Bo_ReportTotaldata;
import com.yunda.base.feiniao.report.dao.GpBasSCustPickTmpDao;
import com.yunda.base.feiniao.report.dao.ReportJurisdictionTableDao;
import com.yunda.base.feiniao.report.dao.ReportTotaldataDao;
import com.yunda.base.feiniao.report.domain.ExportCustBranchReportTotaldataDO;
import com.yunda.base.feiniao.report.domain.ExportCustReportTotaldataDO;
import com.yunda.base.feiniao.report.domain.ExportReportTotaldataDO;
import com.yunda.base.feiniao.report.domain.ReportTotaldataDO;
import com.yunda.base.feiniao.report.service.ReportTotaldataService;
import com.yunda.base.feiniao.report.utils.CachePrefixConformity;
import com.yunda.base.feiniao.schedule.suckdata.enums.SuckCacheKeyPerfixEnum;
import com.yunda.base.system.domain.UserDO;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.MD5Utils;


@Service
@Transactional
public class ReportTotaldataServiceImpl implements ReportTotaldataService {
	Logger logger = Logger.getLogger(ReportTotaldataServiceImpl.class);
	@Autowired
	private ReportTotaldataDao reportTotaldataDao;
	@Autowired
	private CqkhCustomerCacheService cqkhCustomerCacheService;
	@Autowired
	private ReportJurisdictionTableDao reportJurisdictionTableDao;
	@Autowired
	private GpBasSCustPickTmpDao gpBasSCustPickTmpDao;

	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
/*	@Autowired
	private CRMKHReportTotalSqltableServinceImpl reportTotalSqltableServince;*/
	
	@Autowired
	private CRMKHReportTotalManageServiceImpl totalManageServiceImpl;
	
	@Override
	public ReportTotaldataDO get(String bigarea){
		return reportTotaldataDao.get(bigarea);
	}
	
	@Override
	public List<ReportTotaldataDO> listNew(Bo_ReportTotaldata boReportTotaldata,UserDO loginUser){
		List<ReportTotaldataDO> totalData =new ArrayList<ReportTotaldataDO>();
		ReportTotaldataDO proState =new ReportTotaldataDO();
		String startDate = "";
		String endDate = "";
		//加日期条件---日
		if(boReportTotaldata.getQu_date() !=null && ! boReportTotaldata.getQu_date().isEmpty()){
			 startDate = boReportTotaldata.getQu_date();
			 endDate = boReportTotaldata.getQu_date();
			 boReportTotaldata.setStartDate(startDate);
			 boReportTotaldata.setEndDate(endDate);
		}
		//时间段
		if(boReportTotaldata.getStart_date() !=null && ! boReportTotaldata.getStart_date().isEmpty() && boReportTotaldata.getEnd_date() !=null && ! boReportTotaldata.getEnd_date().isEmpty()){
			startDate = boReportTotaldata.getStart_date();
			endDate = boReportTotaldata.getEnd_date();
			 boReportTotaldata.setStartDate(startDate);
			 boReportTotaldata.setEndDate(endDate);
		}		
		//不能选今天和今天之后
		int TableNum = reportTotaldataDao.findIfHasDate(endDate);		
		if(TableNum == 0){
			proState.setCustomerName("rqcw");
			totalData.add(proState);
			return totalData;
		}
		HashMap<String,String> paramMap = new HashMap<>();
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		
		if(boReportTotaldata.getStart_date() !=null && ! boReportTotaldata.getStart_date().isEmpty() && boReportTotaldata.getEnd_date() !=null && ! boReportTotaldata.getEnd_date().isEmpty()){
		    //判断总表查询时间段数据是否存在
			int count = reportTotaldataDao.countJeByCustomerSJD(paramMap);
			if(count == 0){
				return totalData;
			}
		}
		//判断是否是时间段
/*		if(boReportTotaldata.getStart_date() !=null && ! boReportTotaldata.getStart_date().isEmpty() && boReportTotaldata.getEnd_date() !=null && ! boReportTotaldata.getEnd_date().isEmpty()){
		    //判断总表查询时间段数据是否存在
			int count = reportTotaldataDao.countJeByCustomerSJD(paramMap);
			int differ =StringUtils.isEmpty(endDate)||StringUtils.isEmpty(startDate)? 0:Integer.parseInt(endDate.substring(endDate.length()-2))-Integer.parseInt(startDate.substring(startDate.length()-2));
			//查询时间段数据不存在且查询的是时间段，执行sql,按时间段生成数据并落表
			//只要有时间段查询，就生成全网数据，所以生成数据缓存key = startDate + endDate + SuckCacheKeyPerfixEnum.zongbiao.getCode()
			if(count == 0 && differ>0){
				try{
					ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
					String cacheTotal = operations.get(startDate+endDate+SuckCacheKeyPerfixEnum.zongbiao.getCode());
					//cacheTotal = null;
					logger.warn("准备执行任务:queryReport" + startDate + endDate + "_" + cacheTotal);
					if(CRMKHReportTotalManageServiceImpl.REPORT_STATUS_WAIT.equals(cacheTotal)) {
						proState.setCustomerName("wait");
						totalData.add(0, proState);
						return totalData;
					} else if(!CRMKHReportTotalManageServiceImpl.REPORT_STATUS_OK.equals(cacheTotal)) {
						logger.warn("提交任务:queryReport" + startDate + endDate);
						muitiPriceDate(startDate,endDate);
						totalManageServiceImpl.setTotalReportStatus(startDate, endDate, CRMKHReportTotalManageServiceImpl.REPORT_STATUS_WAIT);
						proState.setCustomerName("no");
						totalData.add(0, proState);
						return totalData;
					}	
				}catch (Exception e) {
						totalManageServiceImpl.setTotalReportStatus(startDate, endDate, "fail");
						logger.error("总部查询异常", e);
					}
			}
		}*/
 
		// 权限控制----------------------------------------------------------------------------------------------------------------
				if (loginUser.hasPerms(Constant.REPORT_ADMIN_PERMS)) {
					// 超级用户权限 无限制
					// 系统菜单配置了report:admin:allperms权限标识 角色管理中当该用户的角色勾选该权限标识就说明该用户是超级用户
					// 能查看所有报表的集团大区省市等所有数据
					boReportTotaldata.setTmpField("zongbu");
				} else {
					if (loginUser.isBigareaqx()) {// 是否有大区权限
						boReportTotaldata.setBigareaNames(loginUser.getBigareaNames());
						boReportTotaldata.setTmpField("D");

					} else if (loginUser.isProvinceqx()) {// 是否有省权限
						boReportTotaldata.setProvinceids(loginUser.getProvinceIds());
						boReportTotaldata.setTmpField("S");
					} else if (loginUser.getOrgCode() != null & !loginUser.getOrgCode().isEmpty()) {// 只有某网点权限
						boReportTotaldata.setBranchCode(loginUser.getOrgCode());
						boReportTotaldata.setTmpField("wd");
					} else {
						// return"wsq";//无授权
						// proState.setCustomerName("wsq");
						// totalData.add(0, proState);
						return totalData;
					}
				}
				
				// --------------------------------------------------------------------------------------------------------------
				logger.debug("boReportFluctuate:" + boReportTotaldata);
				if("city".equals(boReportTotaldata.getShowType())) {
					totalData=doWithCity(boReportTotaldata);
				} else if ("branch".equals(boReportTotaldata.getShowType())) {
					totalData=doWithBranch(boReportTotaldata);
				} else if ("cust".equals(boReportTotaldata.getShowType())) {
					totalData=doWithCust(boReportTotaldata,loginUser);
				}else {
					totalData=doWithProvince(boReportTotaldata,loginUser);
					
				}
				
				
				
					
				
		return totalData;	
	}
	
	
	private List<ReportTotaldataDO> doWithCust(Bo_ReportTotaldata boReportTotaldata,UserDO loginUser) {
		List<ReportTotaldataDO> allDatas = new ArrayList<ReportTotaldataDO>();
		ValueOperations<String, Map<String, Object>> operationsCqkh = redisTemplate.opsForValue();
		ValueOperations<String, List<ReportTotaldataDO>> operations = redisTemplate.opsForValue();
		CachePrefixConformity cache = new CachePrefixConformity();
	   	Map<String,Object> custMap = new HashMap<String, Object>();
    	if("D".equals(boReportTotaldata.getTmpField())){
    		//大区权限  
    		//根据大区获取对应省份
    		if(boReportTotaldata.getBigareaNames() != null && boReportTotaldata.getBigareaNames().size() > 0){
            	String[] bigareas = new String[boReportTotaldata.getBigareaNames().size()];
            	boReportTotaldata.getBigareaNames().toArray(bigareas);
            	String[] TProvinceIDs = searchBigAreaCode(boReportTotaldata.getEndDate(),bigareas);
            	custMap.put("provinceIds", TProvinceIDs);
            	custMap.put("TTmpField", boReportTotaldata.getTmpField());
    		}
    	}else if("S".equals(boReportTotaldata.getTmpField())){
    		//省权限
    		custMap.put("provinceIds", boReportTotaldata.getProvinceIds());
    		custMap.put("TTmpField", boReportTotaldata.getTmpField());
    	}else if("wd".equals(boReportTotaldata.getTmpField())){
    		//网点权限，只能查询本网点数据
    		custMap.put("branchCode", boReportTotaldata.getBranchCode());
    		custMap.put("TTmpField", boReportTotaldata.getTmpField());
    	}else{
    		//总部权限没有限制
    	}
	   	custMap.put("startDate", boReportTotaldata.getStartDate());
	   	custMap.put("endDate", boReportTotaldata.getEndDate());
	   	custMap.put("customerId", boReportTotaldata.getCustomerId());
    	if(boReportTotaldata.getLimit() == 0){
    		
    	}else{
    		custMap.put("offset", boReportTotaldata.getOffset());
    		custMap.put("limit", boReportTotaldata.getLimit());
    	}

		//一类省份id
		if(DateUtils.parseDate(boReportTotaldata.getEndDate()).before(DateUtils.parseDate(Constant.FIRST_PROVINCEIDSBEFORE2019_WORK))){
			custMap.put("firstProvinceids", Constant.FIRST_PROVINCEIDSBEFORE2019);
		}else{
			custMap.put("firstProvinceids", Constant.FIRST_PROVINCEIDS);
		}
		custMap = readyMap(boReportTotaldata.getStartDate(), custMap);
		String custMapString = JSON.toJSONString(custMap);
	    String custMapPrefix = MD5Utils.encrypt(custMapString);
	    //datas数据量太大放缓存，读取也会耗时
		List<ReportTotaldataDO> datas = operations.get(cache.getSeed(Constant.QUERYCUSTOMERTOTALINFO+custMapPrefix+"2",SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		if(datas==null||datas.size()<1){
			int differ =StringUtils.isEmpty(boReportTotaldata.getEndDate())||StringUtils.isEmpty(boReportTotaldata.getStartDate())? 0:Integer.parseInt(boReportTotaldata.getEndDate().substring(boReportTotaldata.getEndDate().length()-2))-Integer.parseInt(boReportTotaldata.getStartDate().substring(boReportTotaldata.getStartDate().length()-2));

			if(differ>0){
    			//查询日期为2019.1.1日前的，按照老逻辑，2019.1.1日后的（if(true)），按照新逻辑
    			if(!DateUtils.parseDate(boReportTotaldata.getEndDate()).before(DateUtils.parseDate(Constant.NEWBEGINRUNTIME))){
    				//新逻辑账号是网点权限，不显示返利金额
    				if("wd".equals(boReportTotaldata.getTmpField())){
    					datas= reportTotaldataDao.queryBranchCustTotalInfoSJDNew(custMap);
    				}else{
    					datas= reportTotaldataDao.queryCustTotalInfoSJDNew(custMap);
    				}
    			}else{
    				//老逻辑账号是网点权限，不显示返利金额
    				if("wd".equals(boReportTotaldata.getTmpField())){
    					datas= reportTotaldataDao.queryBranchCustTotalInfoSJD(custMap);
    				}else{
    					datas= reportTotaldataDao.queryCustTotalInfoSJD(custMap);;
    				}

    			}
            }else{
            	//单日的数据已经区分好了新老逻辑
				//新逻辑账号是网点权限，不显示返利金额
				if("wd".equals(boReportTotaldata.getTmpField())){
					datas= reportTotaldataDao.queryBranchCustTotalInfo(custMap);
				}else{
					datas= reportTotaldataDao.queryCustTotalInfo(custMap);
				}
            	
            }
            if(null != datas && datas.size() >0){
    			operations.set(cache.getSeed(Constant.QUERYCUSTOMERTOTALINFO+custMapPrefix+"2",SuckCacheKeyPerfixEnum.zongbiao.getCode()), datas,86400, TimeUnit.SECONDS);
            }
		}
		for (ReportTotaldataDO aa : datas) {
			if(aa.getCustomerId() != null && !aa.getCustomerId().isEmpty()){
				Map<String, Object> customerInfo = operationsCqkh.get(aa.getCustomerId());
				if(customerInfo != null && customerInfo.get("khmc") != null) {
					aa.setCustomerName(customerInfo.get("khmc").toString());
				}
			}	
			//菜鸟和京东库取商家id和店铺名称
			if("菜鸟".equals(aa.getCustomerSourceType())){
				String customerId = aa.getCustomerId()+"cn";
				Map<String, Object> sellerCNInfo = operationsCqkh.get(customerId);
				if(sellerCNInfo != null && sellerCNInfo.get("seller_id") != null) {
					aa.setSellerId(sellerCNInfo.get("seller_id").toString());
					aa.setSellerName(sellerCNInfo.get("seller_name").toString());
				}
			}
			if("京东".equals(aa.getCustomerSourceType())){
				String customerId = aa.getCustomerId()+"jd";
				Map<String, Object> sellerJDInfo = operationsCqkh.get(customerId);
				if(sellerJDInfo != null && sellerJDInfo.get("vendor_code") != null) {
					aa.setSellerId(sellerJDInfo.get("vendor_code").toString());
					aa.setSellerName(sellerJDInfo.get("vendor_name").toString());
				}
			}
			if("拼多多".equals(aa.getCustomerSourceType())){
				String customerId = aa.getCustomerId()+"pdd";
				Map<String, Object> sellerPDDInfo = operationsCqkh.get(customerId);
				if(sellerPDDInfo != null && sellerPDDInfo.get("vendor_code") != null) {
					aa.setSellerId(sellerPDDInfo.get("vendor_code").toString());
					aa.setSellerName(sellerPDDInfo.get("vendor_name").toString());
				}
			}
			
		}
		return datas;
	}
	
	private  List<ReportTotaldataDO> doWithBranch(Bo_ReportTotaldata boReportTotaldata) {
		List<ReportTotaldataDO> allDatas = new ArrayList<ReportTotaldataDO>();
		ReportTotaldataDO proState =new ReportTotaldataDO();
		CachePrefixConformity cache = new CachePrefixConformity();
		int differ =StringUtils.isEmpty(boReportTotaldata.getEndDate())||StringUtils.isEmpty(boReportTotaldata.getStartDate())? 0:Integer.parseInt(boReportTotaldata.getEndDate().substring(boReportTotaldata.getEndDate().length()-2))-Integer.parseInt(boReportTotaldata.getStartDate().substring(boReportTotaldata.getStartDate().length()-2));
		ValueOperations<String, List<ReportTotaldataDO>> operations = redisTemplate.opsForValue();
    	Map<String,Object> branchMap = new HashMap<String, Object>();
    	
    	if("D".equals(boReportTotaldata.getTmpField())){	//大区权限 
    		//根据大区获取对应省份
    		if(boReportTotaldata.getBigareaNames() != null && boReportTotaldata.getBigareaNames().size() > 0){
            	String[] bigareas = new String[boReportTotaldata.getBigareaNames().size()];
            	boReportTotaldata.getBigareaNames().toArray(bigareas);
            	String[] TProvinceIDs = searchBigAreaCode(boReportTotaldata.getEndDate(),bigareas);
            	branchMap.put("provinceIds", TProvinceIDs);
            	branchMap.put("provinceIDs", TProvinceIDs);
            	branchMap.put("TTmpField", boReportTotaldata.getTmpField());
            	
            	//搜索框查询省份id是否在账号所属省份权限内
            	if(null != boReportTotaldata.getProvinceId() && !"".equals(boReportTotaldata.getProvinceId())){
    				if(Arrays.asList(TProvinceIDs).contains(boReportTotaldata.getProvinceId())){
    					branchMap.put("provinceId", boReportTotaldata.getProvinceId());
    				}else{
    					//搜索框插询的省份不在改账号权限内
    					//myqx 没有权限
    					proState.setCustomerName("myqx");
    					allDatas.add(proState);
    					return allDatas;
    				}
            	}
            	
            	//根据大区获取对应的城市id
            	String[] cityIds = getCityIdsByBigarea(boReportTotaldata.getEndDate(),bigareas);
            	//查询城市在所属账号权限内，放入参数map
            	if(null != boReportTotaldata.getCityId() && !"".equals(boReportTotaldata.getCityId())){
                	if(Arrays.asList(cityIds).contains(boReportTotaldata.getCityId())){
                		branchMap.put("cityId", boReportTotaldata.getCityId());
                	}else{
                		//搜索框插询的省份不在改账号权限内
                		//myqx 没有权限
            			proState.setCustomerName("myqx");
            			allDatas.add(proState);
            			return allDatas;
                	}
            	}

            	//查询公司编码在所属账号权限内，放入参数map
            	if(null != boReportTotaldata.getGsbm() && !"".equals(boReportTotaldata.getGsbm())){
                	//根据大区获取对应的网点公司编码
                	String[] gsbm = getGSByBigarea(boReportTotaldata.getEndDate(),TProvinceIDs);
                	if(Arrays.asList(gsbm).contains(boReportTotaldata.getGsbm())){
                		branchMap.put("branchCode", boReportTotaldata.getGsbm());
                	}else{
                		//搜索框插询的公司编码不在改账号权限内
                		//myqx 没有权限
            			proState.setCustomerName("myqx");
            			allDatas.add(proState);
            			return allDatas;
                	}
            	}
    		}
    	}else if("S".equals(boReportTotaldata.getTmpField())){	//省总权限
    		Long[] provinces = new Long[boReportTotaldata.getProvinceids().size()];
    		boReportTotaldata.getProvinceids().toArray(provinces);
    		branchMap.put("provinceIds", provinces);
    		branchMap.put("provinceIDs", provinces);
    		branchMap.put("TTmpField", boReportTotaldata.getTmpField());
        	//搜索框查询省份id是否在账号所属省份权限内
    		if(null != boReportTotaldata.getProvinceId() && !"".equals(boReportTotaldata.getProvinceId())){
    			if(boReportTotaldata.getProvinceids().contains(Long.parseLong(boReportTotaldata.getProvinceId()))){
    				branchMap.put("provinceId", boReportTotaldata.getProvinceId());
    			}else{
    				//搜索框插询的省份不在改账号权限内
    				//myqx 没有权限
    				proState.setCustomerName("myqx");
    				allDatas.add(proState);
    				return allDatas;
    			}
    		}
			
        	//根据大区获取对应的城市id
        	String[] cityIds = getCityIdsByProvinces(boReportTotaldata.getEndDate(),boReportTotaldata.getProvinceids());
        	//查询城市在所属账号权限内，放入参数map
        	if(null != boReportTotaldata.getCityId() && !"".equals(boReportTotaldata.getCityId())){
            	if(Arrays.asList(cityIds).contains(boReportTotaldata.getCityId())){
            		branchMap.put("cityId", boReportTotaldata.getCityId());
            	}else{
            		//搜索框插询的省份不在改账号权限内
            		//myqx 没有权限
        			proState.setCustomerName("myqx");
        			allDatas.add(proState);
        			return allDatas;
            	}
        	}

        	//查询公司编码在所属账号权限内，放入参数map
        	if(null != boReportTotaldata.getGsbm() && !"".equals(boReportTotaldata.getGsbm())){
        		String[] provinceids = new String[boReportTotaldata.getProvinceids().size()];
        		for(int i=0;i<boReportTotaldata.getProvinceids().size();i++){
        			provinceids[i] = boReportTotaldata.getProvinceids().get(i).toString();
        		}
            	//根据省份获取对应的网点公司编码
            	String[] gsbm = getGSByBigarea(boReportTotaldata.getEndDate(),provinceids);
            	if(Arrays.asList(gsbm).contains(boReportTotaldata.getGsbm())){
            		branchMap.put("branchCode", boReportTotaldata.getGsbm());
            	}else{
            		//搜索框插询的公司编码不在改账号权限内
            		//myqx 没有权限
        			proState.setCustomerName("myqx");
        			allDatas.add(proState);
        			return allDatas;
            	}
        	}

    	}else if("wd".equals(boReportTotaldata.getTmpField())){
    		//网点权限，直接返回，无数据
    		branchMap.put("branchCode", boReportTotaldata.getBranchCode());
    		branchMap.put("TTmpField", boReportTotaldata.getTmpField());
    		if(null != boReportTotaldata.getGsbm() && !"".equals(boReportTotaldata.getGsbm())){
    			if(boReportTotaldata.getGsbm().equals(boReportTotaldata.getBranchCode())){
    				
    			}else{
            		//搜索框插询的公司编码不在改账号权限内
            		//myqx 没有权限
        			proState.setCustomerName("myqx");
        			allDatas.add(proState);
        			return allDatas;
    			}
    		}
    	}else{
    		//总部权限没有限制
    		branchMap.put("provinceId", boReportTotaldata.getProvinceId());
    		branchMap.put("cityId", boReportTotaldata.getCityId());
    		branchMap.put("branchCode", boReportTotaldata.getGsbm());
    	}
    	branchMap.put("startDate", boReportTotaldata.getStartDate());
    	branchMap.put("endDate", boReportTotaldata.getEndDate());
    	if(boReportTotaldata.getLimit() == 0){
    		
    	}else{
    		branchMap.put("offset", boReportTotaldata.getOffset());
    		branchMap.put("limit",boReportTotaldata.getLimit() );
    	}
		String branchMapString = JSON.toJSONString(branchMap);
	    String branchMapPrefix = MD5Utils.encrypt(branchMapString);
		List<ReportTotaldataDO> gsDatas = operations.get(cache.getSeed(Constant.QUERYCITYTOTALINFO+"branch"+branchMapPrefix,SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		if(gsDatas==null||gsDatas.size()<1){
			if(differ>0){
				gsDatas = reportTotaldataDao.queryBranchTotalInfoSJD(branchMap);
			}else{
				gsDatas= reportTotaldataDao.queryBranchTotalInfo(branchMap);
			}
			if(gsDatas.size() > 0 && gsDatas.get(0) != null){
				operations.set(cache.getSeed(Constant.QUERYCITYTOTALINFO+"branch"+branchMapPrefix,SuckCacheKeyPerfixEnum.zongbiao.getCode()), gsDatas,86400, TimeUnit.SECONDS);
			}
		}		
		for (ReportTotaldataDO gsData : gsDatas) {
			gsData.setBigarea(gsData.getBigarea());
			gsData.setProvinceid(gsData.getProvinceid());
			gsData.setCityid(gsData.getCityid());
			gsData.setCustomerName(gsData.getBranchName()+"("+gsData.getBranchCode()+")");
			gsData.setBranchCode(gsData.getBranchCode());
			//gsData.setTmpField(boReportTotaldata.getTmp_field());

			allDatas.add(gsData.toReckonList());
		}
		return allDatas;
	}
	
	
	private List<ReportTotaldataDO> doWithCity(Bo_ReportTotaldata boReportTotaldata) {
		List<ReportTotaldataDO> allDatas = new ArrayList<ReportTotaldataDO>();
		ReportTotaldataDO proState =new ReportTotaldataDO();
		CachePrefixConformity cache = new CachePrefixConformity();
		ValueOperations<String, List<ReportTotaldataDO>> operations = redisTemplate.opsForValue();
    	Map<String,Object> cityMap = new HashMap<String, Object>();
    	
    	if("D".equals(boReportTotaldata.getTmpField())){
    		//大区权限  
    		//根据大区获取对应省份
    		if(boReportTotaldata.getBigareaNames() != null && boReportTotaldata.getBigareaNames().size() > 0){
            	String[] bigareas = new String[boReportTotaldata.getBigareaNames().size()];
            	boReportTotaldata.getBigareaNames().toArray(bigareas);
            	String[] TProvinceIDs = searchBigAreaCode(boReportTotaldata.getEndDate(),bigareas);
            	cityMap.put("provinceIds", TProvinceIDs);
            	cityMap.put("provinceIDs", TProvinceIDs);
            	cityMap.put("TTmpField", boReportTotaldata.getTmpField());
            	
            	//搜索框查询省份id是否在账号所属省份权限内
            	if(null != boReportTotaldata.getProvinceId() && !"".equals(boReportTotaldata.getProvinceId())){
                	if(Arrays.asList(TProvinceIDs).contains(boReportTotaldata.getProvinceId())){
                        cityMap.put("provinceId", boReportTotaldata.getProvinceId());
                	}else{
                		//搜索框插询的省份不在改账号权限内
                		//myqx 没有权限
            			proState.setCustomerName("myqx");
            			allDatas.add(proState);
            			return allDatas;
                		
                	}
            	}
            	//根据大区获取对应的城市id
            	String[] cityIds = getCityIdsByBigarea(boReportTotaldata.getEndDate(),bigareas);
            	//查询城市在所属账号权限内，放入参数map
            	if(null != boReportTotaldata.getCityId() && !"".equals(boReportTotaldata.getCityId())){
                	if(Arrays.asList(cityIds).contains(boReportTotaldata.getCityId())){
                        cityMap.put("cityId", boReportTotaldata.getCityId());
                	}else{
                		//搜索框插询的省份不在改账号权限内
                		//myqx 没有权限
            			proState.setCustomerName("myqx");
            			allDatas.add(proState);
            			return allDatas;
                	}
            	}
    		}
    	}else if("S".equals(boReportTotaldata.getTmpField())){
        	Long[] provinces = new Long[boReportTotaldata.getProvinceids().size()];
        	boReportTotaldata.getProvinceids().toArray(provinces);
    		//省权限
    		cityMap.put("provinceIds", provinces);
    		cityMap.put("TTmpField", boReportTotaldata.getTmpField());
        	//搜索框查询省份id是否在账号所属省份权限内
        	//搜索框查询省份id是否在账号所属省份权限内65
        	if(null != boReportTotaldata.getProvinceId() && !"".equals(boReportTotaldata.getProvinceId())){
            	if(boReportTotaldata.getProvinceids().contains(Long.parseLong(boReportTotaldata.getProvinceId()))){
                    cityMap.put("provinceId", boReportTotaldata.getProvinceId());
            	}else{
            		//搜索框插询的省份不在该账号权限内
            		//myqx 没有权限
        			proState.setCustomerName("myqx");
        			allDatas.add(proState);
        			return allDatas;
            	}
        	}
        	//根据省份获取对应的城市id

        	String[] cityIds = getCityIdsByProvinces(boReportTotaldata.getEndDate(),boReportTotaldata.getProvinceids());
        	//查询城市在所属账号权限内，放入参数map
        	if(null != boReportTotaldata.getCityId() && !"".equals(boReportTotaldata.getCityId())){
            	//查询城市在所属账号权限内，放入参数map
            	if(Arrays.asList(cityIds).contains(boReportTotaldata.getCityId())){
                    cityMap.put("cityId", boReportTotaldata.getCityId());
            	}else{
            		//搜索框插询的省份不在改账号权限内
            		//myqx 没有权限
        			proState.setCustomerName("myqx");
        			allDatas.add(proState);
        			return allDatas;
            	}
        	}
    	}else if("wd".equals(boReportTotaldata.getTmpField())){
    		//网点权限，直接返回，无数据
    		//网点权限账号不会走到这里，保险起见
    		return allDatas;
    	}else{
    		//总部权限没有限制
    		//搜索框省份查询
    		cityMap.put("provinceId", boReportTotaldata.getProvinceId());
    		cityMap.put("cityId", boReportTotaldata.getCityId());
    	}
    	cityMap.put("startDate", boReportTotaldata.getStartDate());
    	cityMap.put("endDate", boReportTotaldata.getEndDate());
    	if(boReportTotaldata.getLimit() == 0){
    		
    	}else{
        	cityMap.put("offset", boReportTotaldata.getOffset());
        	cityMap.put("limit", boReportTotaldata.getLimit());
    	}
		String cityMapString = JSON.toJSONString(cityMap);
	    String cityMapPrefix = MD5Utils.encrypt(cityMapString);
		List<ReportTotaldataDO> shiDatas = operations.get(cache.getSeed(Constant.QUERYCITYTOTALINFO+cityMapPrefix+"13",SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		int differ =StringUtils.isEmpty(boReportTotaldata.getEndDate())||StringUtils.isEmpty(boReportTotaldata.getStartDate())? 0:Integer.parseInt(boReportTotaldata.getEndDate().substring(boReportTotaldata.getEndDate().length()-2))-Integer.parseInt(boReportTotaldata.getStartDate().substring(boReportTotaldata.getStartDate().length()-2));
		if(shiDatas==null||shiDatas.size()<1){
			if(differ>0)
				shiDatas= reportTotaldataDao.queryCityTotalInfoSJD(cityMap);
			else
				shiDatas= reportTotaldataDao.queryCityTotalInfo(cityMap);
			if(shiDatas.size() > 0 && shiDatas.get(0) != null){
				operations.set(cache.getSeed(Constant.QUERYCITYTOTALINFO+cityMapPrefix+"13",SuckCacheKeyPerfixEnum.zongbiao.getCode()), shiDatas,86400, TimeUnit.SECONDS);
			}
		}		
		for (ReportTotaldataDO cityData : shiDatas) {
			cityData.setBigarea(cityData.getBigarea());
			cityData.setProvinceid(cityData.getProvinceid());
			cityData.setCityid(cityData.getCityid());
			cityData.setCustomerName(cityData.getCityname());
			allDatas.add(cityData.toReckonList());
		}
		return allDatas;
	}
	
	
	
	
	
	
	@Override
	public List<ReportTotaldataDO> list(Bo_ReportTotaldata boReportTotaldata,UserDO user){
		List<ReportTotaldataDO> totalData =new ArrayList<ReportTotaldataDO>();
		ReportTotaldataDO proState =new ReportTotaldataDO();
        String btFlag = "";
		String startDate = "";
		String endDate = "";
		//String date_Today = DateUtils.format(new java.util.Date());
		//年
		//String[] fege = date_Today.split("-");
		//String year = fege[0];//年
		//加日期条件---日
		if(boReportTotaldata.getQu_date() !=null && ! boReportTotaldata.getQu_date().isEmpty()){
			 startDate = boReportTotaldata.getQu_date();
			 endDate = boReportTotaldata.getQu_date();
		}
		//加日期条件---月
/*		if(boReportTotaldata.getMonth_year() !=null && ! boReportTotaldata.getMonth_year().isEmpty()){  
			 String a = boReportTotaldata.getMonth_year()+"-01";
			 String endDatenum = DateUtils.getMonthEnd(a);
			 String[] b = endDatenum.split("-");
			 String daynum = b[2];
			 startDate = boReportTotaldata.getMonth_year()+"-01";
			 endDate = boReportTotaldata.getMonth_year()+"-"+daynum;
		}*/
		//加日期条件---季度
		/*if(boReportTotaldata.getQuarter_date() !=null && ! boReportTotaldata.getQuarter_date().isEmpty() && boReportTotaldata.getQuarter_year() !=null && ! boReportTotaldata.getQuarter_year().isEmpty()){		
			if("1".equals(boReportTotaldata.getQuarter_date())){
				 startDate = boReportTotaldata.getQuarter_year()+"-01-01";
				 endDate = boReportTotaldata.getQuarter_year()+"-03-31";
				}
			if("2".equals(boReportTotaldata.getQuarter_date())){
				 String a = boReportTotaldata.getQuarter_year()+"-04-01";
				 String endDatenum = DateUtils.getMonthEnd(a);
				 String[] b = endDatenum.split("-");
				 String daynum = b[2];
				 startDate = boReportTotaldata.getQuarter_year()+"-04-"+daynum;
				 String c = boReportTotaldata.getQuarter_year()+"-06-01";
				 String endnum = DateUtils.getMonthEnd(c);
				 String[] d = endnum.split("-");
				 String num = d[2];
				 endDate = boReportTotaldata.getQuarter_year()+"-06-"+num;
			}
			if("3".equals(boReportTotaldata.getQuarter_date())){
				 startDate = boReportTotaldata.getQuarter_year()+"-07-01";
				 String a = boReportTotaldata.getQuarter_year()+"-09-01";
				 String endDatenum = DateUtils.getMonthEnd(a);
				 String[] b = endDatenum.split("-");
				 String daynum = b[2];
				 endDate = boReportTotaldata.getQuarter_year()+"-09-"+daynum;
			}
			if("4".equals(boReportTotaldata.getQuarter_date())){
				 startDate = boReportTotaldata.getQuarter_year()+"-10-01";
				 endDate = boReportTotaldata.getQuarter_year()+"-12-31";
			}
		}*/
		//年
/*		if(boReportTotaldata.getYear() !=null && ! boReportTotaldata.getYear().isEmpty()){
			startDate = boReportTotaldata.getYear()+"-01-01";
			endDate = boReportTotaldata.getYear()+"-12-31";
		}*/
		//时间段
		if(boReportTotaldata.getStart_date() !=null && ! boReportTotaldata.getStart_date().isEmpty() && boReportTotaldata.getEnd_date() !=null && ! boReportTotaldata.getEnd_date().isEmpty()){
			startDate = boReportTotaldata.getStart_date();
			endDate = boReportTotaldata.getEnd_date();
		}		
		//不能选今天和今天之后
		int TableNum = reportTotaldataDao.findIfHasDate(endDate);		
		if(TableNum == 0){
			proState.setCustomerName("rqcw");
			totalData.add(proState);
			return totalData;
		}/*else {		
			Date currentTime = new Date();// 当前时间
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowtime = formatter.format(currentTime);
			Calendar now = Calendar.getInstance();
			Calendar nowAdd = Calendar.getInstance();
			Calendar c1 = Calendar.getInstance();
		    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
			String current = sdf.format(now.getTime());
			try {
				now.setTime(sdf.parse(current));
				now.add(Calendar.DATE, -1);
				now.add(Calendar.HOUR, 10);
				now.add(Calendar.MINUTE, 20);
				
				c1.setTime(formatter.parse(nowtime));
				c1.add(Calendar.DATE, -1);

			} catch (ParseException e1) {
			}		
			nowAdd.setTime(new Date());
			nowAdd.add(Calendar.DATE, -1);			
		}*/
		HashMap<String,String> paramMap = new HashMap<>();
		paramMap.put("yone", startDate);
		paramMap.put("ytwo", endDate);
		
		//判断是否是时间段
		if(boReportTotaldata.getStart_date() !=null && ! boReportTotaldata.getStart_date().isEmpty() && boReportTotaldata.getEnd_date() !=null && ! boReportTotaldata.getEnd_date().isEmpty()){
		    //判断总表查询时间段数据是否存在
			int count = reportTotaldataDao.countJeByCustomerSJD(paramMap);
			int differ =StringUtils.isEmpty(endDate)||StringUtils.isEmpty(startDate)? 0:Integer.parseInt(endDate.substring(endDate.length()-2))-Integer.parseInt(startDate.substring(startDate.length()-2));
			//查询时间段数据不存在且查询的是时间段，执行sql,按时间段生成数据并落表
			//只要有时间段查询，就生成全网数据，所以生成数据缓存key = startDate + endDate + SuckCacheKeyPerfixEnum.zongbiao.getCode()
			if(count == 0 && differ>0){
				try{
					ValueOperations<String, String> operations = redisTemplate.opsForValue();
					String cacheTotal = operations.get(startDate+endDate+SuckCacheKeyPerfixEnum.zongbiao.getCode());
					cacheTotal = null;
					logger.warn("准备执行任务:queryReport" + startDate + endDate + "_" + cacheTotal);
					if(CRMKHReportTotalManageServiceImpl.REPORT_STATUS_WAIT.equals(cacheTotal)) {
						proState.setCustomerName("wait");
						totalData.add(0, proState);
						return totalData;
					} else if(!CRMKHReportTotalManageServiceImpl.REPORT_STATUS_OK.equals(cacheTotal)) {
						logger.warn("提交任务:queryReport" + startDate + endDate);
						muitiPriceDate(startDate,endDate);
						totalManageServiceImpl.setTotalReportStatus(startDate, endDate, CRMKHReportTotalManageServiceImpl.REPORT_STATUS_WAIT);
						proState.setCustomerName("no");
						totalData.add(0, proState);
						return totalData;
					}	
				}catch (Exception e) {
						totalManageServiceImpl.setTotalReportStatus(startDate, endDate, "fail");
						logger.error("总部查询异常", e);
					}
			}
		}
 
		if(user.hasPerms(Constant.REPORT_ADMIN_PERMS)) {
			   //超级用户权限   无限制
			//系统菜单配置了report:admin:allperms权限标识   角色管理中当该用户的角色勾选该权限标识就说明该用户是超级用户  能查看所有报表的集团大区省市等所有数据	
			btFlag = "bt";
			boReportTotaldata.setTmp_field("bt");

		}else {
				if(user.isProvinceqx()){
					if(!"city".equals(boReportTotaldata.getShowType()) & !"branch".equals(boReportTotaldata.getShowType()) & !"customer".equals(boReportTotaldata.getShowType())){
						if(user.getProvinceIds().size()>0){
							boReportTotaldata.setProvinceIdsqx(user.getProvinceIds());
						}	
						boReportTotaldata.setProvinceIdsqx(user.getProvinceIds());
						}
					if("customer".equals(boReportTotaldata.getShowType())){
						proState.setCustomerName("---------------------请返回---------------------");
						totalData.add(0, proState);
						return totalData;
					}
						boReportTotaldata.setTmp_field("S");
				}else if(user.isBigareaqx()){
					if(user.getBigareaNames().size()>0){
						boReportTotaldata.setRegionIds(user.getBigareaNames());
					}		
	               boReportTotaldata.setTmp_field("D");
					
				}else if(user.getOrgCode() != null & ! user.getOrgCode().isEmpty()){
				 	boReportTotaldata.setBranch_code(user.getOrgCode());
						boReportTotaldata.setShowType("customer");
						boReportTotaldata.setTmp_field("W"); 
							proState.setCustomerName(startDate+","+endDate+",W"+",wddl"+","+user.getOrgCode());		
						totalData.add(0, proState);
						return totalData;
				 }else{
						proState.setCustomerName("wsq");
						totalData.add(0, proState);
						return totalData;
				 }			
			}
			// --------------------------------------------------------------------------------------------------------------
			boReportTotaldata.setUserId(user.getUsername());
			if("city".equals(boReportTotaldata.getShowType())) {
				totalData=doWithCity(boReportTotaldata,startDate, endDate);
			} else if ("branch".equals(boReportTotaldata.getShowType())) {
				totalData=doWithBranch(boReportTotaldata,startDate, endDate);
			}else {
				totalData=doWithProvince(boReportTotaldata,startDate, endDate);				
			}
			if(totalData.size()>0){
				totalData.get(0).setBranchName(btFlag);
				totalData.get(0).setTmpField(boReportTotaldata.getTmp_field());
				if(StringUtils.isEmpty(totalData.get(0).getCustomerName())){
					totalData.get(0).setCustomerName("bt");
				}
				for(ReportTotaldataDO data :totalData){
					data.setStartDate(startDate);
					data.setEndDate(endDate);
				}			
			}
		return totalData;	
	}
	
	private void muitiPriceDate(String startDate,String endDate){
		final String ssdate = startDate;
		final String eedate = endDate;

		logger.warn("开始执行任务:queryReport" + ssdate + eedate);
		String prefix = startDate.replaceAll("-", "") + "_" +endDate.replaceAll("-", "");
		long y = System.currentTimeMillis();
		executeTotalPriceSJD(startDate,endDate);
		executeTotalLJSJD(startDate,endDate);
		logger.warn("is_finish:汇总金额"+prefix+"--"+ (System.currentTimeMillis() - y));
	}
/*	private void muitiPriceDate(String startDate,String endDate){
		final String ssdate = startDate;
		final String eedate = endDate;
		ValueOperations<String, List<ReportTotaldataDO>> operations = redisTemplate.opsForValue();
		CRMKHReportTotalManageServiceImpl.fixedThreadPool.submit(new Runnable() {			
		@Override
		public void run() {
			logger.warn("开始执行任务:queryReport" + ssdate + eedate);
			try {
				if(!reportTotalSqltableServince.totalSql(ssdate, eedate)){
					totalManageServiceImpl.setTotalReportStatus(ssdate, eedate, CRMKHReportTotalManageServiceImpl.REPORT_STATUS_OK);
				}
			} catch (Exception e) {
				logger.warn(e.getMessage());
			}
			logger.warn("任务执行完成:queryReport" + ssdate + eedate);
 
			}
		});
	}*/
	

	
	
	private List<ReportTotaldataDO> doWithCity(Bo_ReportTotaldata boReportTotaldata,String startDate,String endDate) {
		String prefix = startDate.replaceAll("-", "") + "_" +endDate.replaceAll("-", "");
		List<ReportTotaldataDO> allDatas = new ArrayList<ReportTotaldataDO>();
		boReportTotaldata.setStart_date(startDate);
		boReportTotaldata.setEnd_date(endDate);
		CachePrefixConformity cache = new CachePrefixConformity();
		ValueOperations<String, List<ReportTotaldataDO>> operations = redisTemplate.opsForValue();
		String provinceTotalInfo  = JSON.toJSONString(boReportTotaldata);
	    String cacheTotalInfo = MD5Utils.encrypt(provinceTotalInfo);
		List<ReportTotaldataDO> shengDatas = operations.get(cache.getSeed(Constant.QUERYPROVINCETOTALINFO+cacheTotalInfo+"3",SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		int differ =StringUtils.isEmpty(endDate)||StringUtils.isEmpty(startDate)? 0:Integer.parseInt(endDate.substring(endDate.length()-2))-Integer.parseInt(startDate.substring(startDate.length()-2));
		if(shengDatas==null||shengDatas.size()<1){
			if(differ>0)
				shengDatas= reportTotaldataDao.queryProvinceTotalInfoSJD(boReportTotaldata);
			else
				shengDatas= reportTotaldataDao.queryProvinceTotalInfo(boReportTotaldata);
			operations.set(cache.getSeed(Constant.QUERYPROVINCETOTALINFO+cacheTotalInfo+"3",SuckCacheKeyPerfixEnum.zongbiao.getCode()), shengDatas,86400, TimeUnit.SECONDS);
		}
		if(shengDatas.size()>0){
		shengDatas.get(0).setCustomerName(shengDatas.get(0).getProvincename()+"合计");
		allDatas.add(shengDatas.get(0).toReckonList());
		}
		List<ReportTotaldataDO> shiDatas = operations.get(cache.getSeed(Constant.QUERYCITYTOTALINFO+boReportTotaldata.getProvince_id()+boReportTotaldata.getUserId()+prefix+"4",SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		if(shiDatas==null||shiDatas.size()<1){
			if(differ>0)
				shiDatas= reportTotaldataDao.queryCityTotalInfoSJD(boReportTotaldata.getProvince_id(), startDate,endDate,"");
			else
				shiDatas= reportTotaldataDao.queryCityTotalInfo(boReportTotaldata.getProvince_id(), startDate,endDate,"");
			operations.set(cache.getSeed(Constant.QUERYCITYTOTALINFO+boReportTotaldata.getProvince_id()+boReportTotaldata.getUserId()+prefix+"4",SuckCacheKeyPerfixEnum.zongbiao.getCode()), shiDatas,86400, TimeUnit.SECONDS);
		}		
		for (ReportTotaldataDO cityData : shiDatas) {
			cityData.setBigarea(cityData.getBigarea());
			cityData.setProvinceid(cityData.getProvinceid());
			cityData.setCityid(cityData.getCityid());
			cityData.setCustomerName(cityData.getCityname());
			allDatas.add(cityData.toReckonList());
		}
		return allDatas;
	}
	
	
	private  List<ReportTotaldataDO> doWithBranch(Bo_ReportTotaldata boReportTotaldata,String startDate,String endDate) {
		String prefix = startDate.replaceAll("-", "") + "_" +endDate.replaceAll("-", "");
		List<ReportTotaldataDO> allDatas = new ArrayList<ReportTotaldataDO>();
		boReportTotaldata.setStart_date(startDate);
		boReportTotaldata.setEnd_date(endDate);
	    String startDay = DateUtils.getMonthBegin(endDate);
		CachePrefixConformity cache = new CachePrefixConformity();
		int differ =StringUtils.isEmpty(endDate)||StringUtils.isEmpty(startDate)? 0:Integer.parseInt(endDate.substring(endDate.length()-2))-Integer.parseInt(startDate.substring(startDate.length()-2));
		ValueOperations<String, List<ReportTotaldataDO>> operations = redisTemplate.opsForValue();
		List<ReportTotaldataDO> shiDatas = operations.get(cache.getSeed(Constant.QUERYCITYTOTALINFO+boReportTotaldata.getCity_id()+"branch"+boReportTotaldata.getUserId()+prefix+"5",SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		if(shiDatas==null||shiDatas.size()<1){
			if(differ>0)
				shiDatas= !"wddc".equals(boReportTotaldata.getTmp_field())?reportTotaldataDao.queryCityTotalInfoSJD("", startDate,endDate,boReportTotaldata.getCity_id()):
					reportTotaldataDao.queryCityTotalInfoSJD("", startDate,endDate,"");
			else
				shiDatas= !"wddc".equals(boReportTotaldata.getTmp_field())?reportTotaldataDao.queryCityTotalInfo("", startDate,endDate,boReportTotaldata.getCity_id()):
					reportTotaldataDao.queryCityTotalInfo("", startDate,endDate,"");
			operations.set(cache.getSeed(Constant.QUERYCITYTOTALINFO+boReportTotaldata.getCity_id()+"branch"+boReportTotaldata.getUserId()+prefix+"5",SuckCacheKeyPerfixEnum.zongbiao.getCode()), shiDatas,86400, TimeUnit.SECONDS);
		}	
		
		shiDatas.get(0).setCustomerName(shiDatas.get(0).getCityname()+"合计");
		allDatas.add(shiDatas.get(0).toReckonList());
		List<ReportTotaldataDO> gsDatas = operations.get(cache.getSeed(Constant.QUERYBRANCHTOTALINFO+boReportTotaldata.getCity_id()+"branch"+boReportTotaldata.getUserId()+prefix+"4",SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		if(gsDatas==null||gsDatas.size()<1){
			if(differ>0)
				gsDatas= !"wddc".equals(boReportTotaldata.getTmp_field())?reportTotaldataDao.queryBranchTotalInfoSJD(startDate,endDate,boReportTotaldata.getCity_id(),startDay)
						:reportTotaldataDao.queryBranchTotalInfoSJD(startDate,endDate,"",startDay);
				else
				gsDatas= !"wddc".equals(boReportTotaldata.getTmp_field())?reportTotaldataDao.queryBranchTotalInfo(startDate,endDate,boReportTotaldata.getCity_id())
						:reportTotaldataDao.queryBranchTotalInfo(startDate,endDate,"");
			operations.set(cache.getSeed(Constant.QUERYBRANCHTOTALINFO+boReportTotaldata.getCity_id()+"branch"+boReportTotaldata.getUserId()+prefix+"4",SuckCacheKeyPerfixEnum.zongbiao.getCode()), gsDatas,86400, TimeUnit.SECONDS);
		}	
		for (ReportTotaldataDO branchData : gsDatas) {
			branchData.setBigarea(branchData.getBigarea());
			branchData.setProvinceid(branchData.getProvinceid());
			branchData.setCityid(branchData.getCityid());
			branchData.setCustomerName(branchData.getBranchName()+"("+branchData.getBranchCode()+")");
			branchData.setBranchCode(branchData.getBranchCode());
			branchData.setTmpField(boReportTotaldata.getTmp_field());

			allDatas.add(branchData.toReckonList());
		}
		return allDatas;

	}
	
	private List<ReportTotaldataDO> doWithProvince(Bo_ReportTotaldata boReportTotaldata,UserDO loginUser) {
		List<ReportTotaldataDO> allDatas = new ArrayList<ReportTotaldataDO>();
		CachePrefixConformity cache = new CachePrefixConformity();
		ValueOperations<String, List<ReportTotaldataDO>> operations = redisTemplate.opsForValue();
		int differ =StringUtils.isEmpty(boReportTotaldata.getEndDate())||StringUtils.isEmpty(boReportTotaldata.getStartDate())? 0:Integer.parseInt(boReportTotaldata.getEndDate().substring(boReportTotaldata.getEndDate().length()-2))-Integer.parseInt(boReportTotaldata.getStartDate().substring(boReportTotaldata.getStartDate().length()-2));
		//总部权限
		if("zongbu".equals(boReportTotaldata.getTmpField())){
			// 集团合计查询
	    	Map<String,Object> allMap = new HashMap<String, Object>();
	    	allMap.put("startDate", boReportTotaldata.getStartDate());
	    	allMap.put("endDate", boReportTotaldata.getEndDate());
			String allMapString = JSON.toJSONString(allMap);
		    String allMapPrefix = MD5Utils.encrypt(allMapString);
			List<ReportTotaldataDO> allcountDatas = operations.get(cache.getSeed(Constant.QUERYCOMPANYTOTALINFO+allMapPrefix+"1",SuckCacheKeyPerfixEnum.zongbiao.getCode()));
			if(allcountDatas==null||allcountDatas.size()<1){
				if(differ>0){
					allcountDatas= reportTotaldataDao.queryCompanyTotalInfoSJD(allMap);
				}else{
					allcountDatas= reportTotaldataDao.queryCompanyTotalInfo(allMap);
				}
				if(allcountDatas.size() > 0 && allcountDatas.get(0) != null){
					operations.set(cache.getSeed(Constant.QUERYCOMPANYTOTALINFO+allMapPrefix+"1",SuckCacheKeyPerfixEnum.zongbiao.getCode()), allcountDatas,86400, TimeUnit.SECONDS);

				}
			}
			if (allcountDatas.size() > 0 && allcountDatas.get(0) != null) {
				allcountDatas.get(0).setCustomerName("集团合计");
				allDatas.add(allcountDatas.get(0).toReckonList());
			} else {
				allDatas.add(new ReportTotaldataDO());
				return allDatas;
			}
			//大区查询
			Map<String,Object> bigareaMap = new HashMap<String, Object>();
	    	bigareaMap.put("startDate", boReportTotaldata.getStartDate());
	    	bigareaMap.put("endDate", boReportTotaldata.getEndDate());
	    	//bigareaMap.put("bigareas", boReportTotaldata.getBigareaNames());
	    	//bigareaMap.put("TTmpField", boReportTotaldata.getTmpField());
			String bigareaMapString = JSON.toJSONString(bigareaMap);
		    String bigareaMapPrefix = MD5Utils.encrypt(bigareaMapString);
			List<ReportTotaldataDO> allBigareaDatas = operations.get(cache.getSeed(Constant.QUERYBIGAREATOTALINFO+bigareaMapPrefix+"1",SuckCacheKeyPerfixEnum.zongbiao.getCode()));
			if(allBigareaDatas==null||allBigareaDatas.size()<1){
				if(differ>0){
					allBigareaDatas= reportTotaldataDao.queryBigareaTotalInfoSJD(bigareaMap);
				}else{
					allBigareaDatas= reportTotaldataDao.queryBigareaTotalInfo(bigareaMap);
				}
				if(allBigareaDatas.size() > 0 && allBigareaDatas != null){
					operations.set(cache.getSeed(Constant.QUERYBIGAREATOTALINFO+bigareaMapPrefix+"1",SuckCacheKeyPerfixEnum.zongbiao.getCode()), allBigareaDatas,86400, TimeUnit.SECONDS);
				}
			}
			
			for (ReportTotaldataDO bigareaData : allBigareaDatas) {
				if(StringUtils.isEmpty(bigareaData.getBigarea())) {
					continue;
				}
				// 大区合计
				bigareaData.setCustomerName(bigareaData.getBigarea()+"合计");
				allDatas.add(bigareaData.toReckonList());
				//省份查询
				Map<String,Object> provinceMap = new HashMap<String, Object>();
		    	provinceMap.put("startDate", boReportTotaldata.getStartDate());
		    	provinceMap.put("endDate", boReportTotaldata.getEndDate());
		    	provinceMap.put("bigarea", bigareaData.getBigarea());
		    	//provinceMap.put("TProvinceID",boReportTotaldata.getProvinceids());
		    	//provinceMap.put("tProvinceID",boReportTotaldata.getProvinceids());
		    	//provinceMap.put("TTmpField", boReportTotaldata.getTmpField());
				String provinceMapString = JSON.toJSONString(provinceMap);
			    String provinceMapPrefix = MD5Utils.encrypt(provinceMapString);
				List<ReportTotaldataDO> shengDatas = operations.get(cache.getSeed(Constant.QUERYPROVINCETOTALINFO+provinceMapPrefix+"1",SuckCacheKeyPerfixEnum.zongbiao.getCode()));
				if(shengDatas==null||shengDatas.size()<1){
					if(differ>0){
						shengDatas= reportTotaldataDao.queryProvinceTotalInfoSJD(provinceMap);
					}else{
						shengDatas= reportTotaldataDao.queryProvinceTotalInfo(provinceMap);
					}
					if(shengDatas.size() > 0 && shengDatas != null){
						operations.set(cache.getSeed(Constant.QUERYPROVINCETOTALINFO+provinceMapPrefix+"1",SuckCacheKeyPerfixEnum.zongbiao.getCode()), shengDatas,86400, TimeUnit.SECONDS);
					}
				}
				// 循环省份
				for (ReportTotaldataDO shengData : shengDatas) {
					// 省份合计
					shengData.setCustomerName(shengData.getProvincename());
					shengData.setProvinceid(shengData.getProvinceid());
					shengData.setBigarea(shengData.getBigarea());
					allDatas.add(shengData.toReckonList());

				}
			}
		}else if("D".equals(boReportTotaldata.getTmpField())){//大区权限
	    	Map<String,Object> bigareaMap = new HashMap<String, Object>();
	    	bigareaMap.put("startDate", boReportTotaldata.getStartDate());
	    	bigareaMap.put("endDate", boReportTotaldata.getEndDate());
	    	bigareaMap.put("bigareas", boReportTotaldata.getBigareaNames());
	    	bigareaMap.put("bigareaS", boReportTotaldata.getBigareaNames());
	    	bigareaMap.put("TTmpField", boReportTotaldata.getTmpField());
			String bigareaMapString = JSON.toJSONString(bigareaMap);
		    String bigareaMapPrefix = MD5Utils.encrypt(bigareaMapString);
			List<ReportTotaldataDO> allBigareaDatas = operations.get(cache.getSeed(Constant.QUERYBIGAREATOTALINFO+bigareaMapPrefix,SuckCacheKeyPerfixEnum.zongbiao.getCode()));
			if(allBigareaDatas==null||allBigareaDatas.size()<1){
				if(differ>0){
					allBigareaDatas= reportTotaldataDao.queryBigareaTotalInfoSJD(bigareaMap);
				}else{
					allBigareaDatas= reportTotaldataDao.queryBigareaTotalInfo(bigareaMap);
				}
				if(allBigareaDatas.size() > 0 && allBigareaDatas != null){
					operations.set(cache.getSeed(Constant.QUERYBIGAREATOTALINFO+bigareaMapPrefix,SuckCacheKeyPerfixEnum.zongbiao.getCode()), allBigareaDatas,86400, TimeUnit.SECONDS);
				}
			}
			
			for (ReportTotaldataDO bigareaData : allBigareaDatas) {
				if(StringUtils.isEmpty(bigareaData.getBigarea())) {
					continue;
				}
				//大区合计
				bigareaData.setCustomerName(bigareaData.getBigarea()+"合计");
				allDatas.add(bigareaData.toReckonList());
				//大区下面的省合计
				Map<String,Object> provinceMap = new HashMap<String, Object>();
		    	provinceMap.put("startDate", boReportTotaldata.getStartDate());
		    	provinceMap.put("endDate", boReportTotaldata.getEndDate());
		    	provinceMap.put("bigarea", bigareaData.getBigarea());
				String provinceMapString = JSON.toJSONString(provinceMap);
			    String provinceMapPrefix = MD5Utils.encrypt(provinceMapString);
				List<ReportTotaldataDO> shengDatas = operations.get(cache.getSeed(Constant.QUERYPROVINCETOTALINFO+provinceMapPrefix,SuckCacheKeyPerfixEnum.zongbiao.getCode()));
				if(shengDatas==null||shengDatas.size()<1){
					if(differ>0){
						shengDatas= reportTotaldataDao.queryProvinceTotalInfoSJD(provinceMap);
					}else{
						shengDatas= reportTotaldataDao.queryProvinceTotalInfo(provinceMap);
					}
					if(shengDatas.size() > 0 && shengDatas != null){
						operations.set(cache.getSeed(Constant.QUERYPROVINCETOTALINFO+provinceMapPrefix,SuckCacheKeyPerfixEnum.zongbiao.getCode()), shengDatas,86400, TimeUnit.SECONDS);
					}
				}
				// 循环省份
				for (ReportTotaldataDO shengData : shengDatas) {
					// 省份合计
					shengData.setCustomerName(shengData.getProvincename());
					shengData.setProvinceid(shengData.getProvinceid());
					shengData.setBigarea(shengData.getBigarea());
					allDatas.add(shengData.toReckonList());

				}
			}
		}else if("S".equals(boReportTotaldata.getTmpField())){//省总权限
			Map<String,Object> provinceMap = new HashMap<String, Object>();
	    	provinceMap.put("startDate", boReportTotaldata.getStartDate());
	    	provinceMap.put("endDate", boReportTotaldata.getEndDate());
	    	provinceMap.put("TProvinceID",boReportTotaldata.getProvinceids());
	    	provinceMap.put("tProvinceID",boReportTotaldata.getProvinceids());
	    	provinceMap.put("TTmpField", boReportTotaldata.getTmpField());
			String provinceMapString = JSON.toJSONString(provinceMap);
		    String provinceMapPrefix = MD5Utils.encrypt(provinceMapString);
			List<ReportTotaldataDO> shengDatas = operations.get(cache.getSeed(Constant.QUERYPROVINCETOTALINFO+provinceMapPrefix,SuckCacheKeyPerfixEnum.zongbiao.getCode()));
			if(shengDatas==null||shengDatas.size()<1){
				if(differ>0){
					shengDatas= reportTotaldataDao.queryProvinceTotalInfoSJD(provinceMap);
				}else{
					shengDatas= reportTotaldataDao.queryProvinceTotalInfo(provinceMap);
				}
				if(shengDatas.size() > 0 && shengDatas != null){
					operations.set(cache.getSeed(Constant.QUERYPROVINCETOTALINFO+provinceMapPrefix,SuckCacheKeyPerfixEnum.zongbiao.getCode()), shengDatas,86400, TimeUnit.SECONDS);
				}
			}
			// 循环省份
			for (ReportTotaldataDO shengData : shengDatas) {
				// 省份合计
				shengData.setCustomerName(shengData.getProvincename());
				shengData.setProvinceid(shengData.getProvinceid());
				shengData.setBigarea(shengData.getBigarea());
				allDatas.add(shengData.toReckonList());
			}
		} 
    	
		//查询截止时间当月的最后一天 ，决定基础信息生效的时间
		String endDay = DateUtils.getMonthEnd(boReportTotaldata.getEndDate());
		if("D".equals(boReportTotaldata.getTmpField()) || "S".equals(boReportTotaldata.getTmpField()) ||  "wd".equals(boReportTotaldata.getTmpField())){
			
		}else{
			//一类省份id
			if(DateUtils.parseDate(boReportTotaldata.getEndDate()).before(DateUtils.parseDate(Constant.FIRST_PROVINCEIDSBEFORE2019_WORK))){
				// 广东+海南
				doWithProvinceCount(allDatas,boReportTotaldata,new String[]{"440000","460000"}, "广东+海南");
			}else{
				// 广东+海南
				doWithProvinceCount(allDatas,boReportTotaldata,new String[]{"440001","440002","440003","460000"}, "广东广州+广东东莞+广东揭阳+海南");
			}
		

			// 陕西+甘肃+青海+宁夏回族自治区
			doWithProvinceCount(allDatas, boReportTotaldata, new String[]{"610000","620000","630000","640000"}, "陕西+甘肃+青海+宁夏");
			// 四川+西藏
			doWithProvinceCount(allDatas,boReportTotaldata, new String[]{"510000","540000"}, "四川+西藏");
			//苏州+南京
			doWithProvinceCount(allDatas,boReportTotaldata, new String[]{"320001","320002"}, "苏州+南京");
			//浙南+浙北
			doWithProvinceCount(allDatas,boReportTotaldata, new String[]{"330001","330002"}, "浙南+浙北");
			// 上海大区+广东大区+华中西南大区（合计）
			doWithProvinceCount(allDatas,boReportTotaldata, searchBigAreaCode(endDay,new String[]{"上海大区","广东大区","华中西南大区"}), " 上海大区+广东大区+华中西南大区（合计）");
			// 浙江大区+江苏大区（合计）
			doWithProvinceCount(allDatas,boReportTotaldata, searchBigAreaCode(endDay,new String[]{"浙江大区","江苏大区"}), " 浙江大区+江苏大区（合计）");	

		}

		return allDatas;
	}
	
	
	private List<ReportTotaldataDO> doWithProvince(Bo_ReportTotaldata boReportTotaldata,String startDate,String endDate) {
		String prefix = startDate.replaceAll("6-", "") + "_" +endDate.replaceAll("-", "");
		List<ReportTotaldataDO> allDatas = new ArrayList<ReportTotaldataDO>();
		boReportTotaldata.setStart_date(startDate);
		boReportTotaldata.setEnd_date(endDate);
		CachePrefixConformity cache = new CachePrefixConformity();
		int differ =StringUtils.isEmpty(endDate)||StringUtils.isEmpty(startDate)? 0:Integer.parseInt(endDate.substring(endDate.length()-2))-Integer.parseInt(startDate.substring(startDate.length()-2));

		// 集团合计
		ValueOperations<String, List<ReportTotaldataDO>> operations = redisTemplate.opsForValue();
		List<ReportTotaldataDO> allcountDatas = operations.get(cache.getSeed(Constant.QUERYCOMPANYTOTALINFO+boReportTotaldata.getUserId()+prefix+"3",SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		if(allcountDatas==null||allcountDatas.size()<1){
			if(differ>0){
				allcountDatas= reportTotaldataDao.queryCompanyTotalInfoSJD(startDate,endDate);
			}else{
				allcountDatas= reportTotaldataDao.queryCompanyTotalInfo(startDate,endDate);
			}
			if(allcountDatas.size() > 0 && allcountDatas.get(0) != null){
				operations.set(cache.getSeed(Constant.QUERYCOMPANYTOTALINFO+boReportTotaldata.getUserId()+prefix+"3",SuckCacheKeyPerfixEnum.zongbiao.getCode()), allcountDatas,86400, TimeUnit.SECONDS);

			}
		}

		if("D".equals(boReportTotaldata.getTmp_field())){
		}else if("S".equals(boReportTotaldata.getTmp_field())){
		} else {
			if (allcountDatas.size() > 0 && allcountDatas.get(0) != null) {
				allcountDatas.get(0).setCustomerName("集团合计");
				allDatas.add(allcountDatas.get(0).toReckonList());
			} else {
				allDatas.add(new ReportTotaldataDO());
				return allDatas;
			}
		}
		List<ReportTotaldataDO> allBigareaDatas = operations.get(cache.getSeed(Constant.QUERYBIGAREATOTALINFO+boReportTotaldata.getUserId()+prefix+"3",SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		if(allBigareaDatas==null||allBigareaDatas.size()<1){
			if(differ>0){
				allBigareaDatas= reportTotaldataDao.queryBigareaTotalInfoSJD(boReportTotaldata);
			}else{
				allBigareaDatas= reportTotaldataDao.queryBigareaTotalInfo(boReportTotaldata);
			}
			if(allBigareaDatas.size() > 0 && allBigareaDatas != null){
				operations.set(cache.getSeed(Constant.QUERYBIGAREATOTALINFO+boReportTotaldata.getUserId()+prefix+"3",SuckCacheKeyPerfixEnum.zongbiao.getCode()), allBigareaDatas,86400, TimeUnit.SECONDS);
			}
		}
		
		for (ReportTotaldataDO bigareaData : allBigareaDatas) {
			if(StringUtils.isEmpty(bigareaData.getBigarea())) {
				continue;
			}
			// 大区合计
			if("S".equals(boReportTotaldata.getTmp_field())){
				
			}else{
				bigareaData.setCustomerName(bigareaData.getBigarea()+"合计");
				allDatas.add(bigareaData.toReckonList());
			}
			boReportTotaldata.setBig_area(bigareaData.getBigarea());
			List<ReportTotaldataDO> shengDatas = operations.get(cache.getSeed(Constant.QUERYPROVINCETOTALINFO+boReportTotaldata.getUserId()+bigareaData.getProvinceid()+bigareaData.getBigarea()+prefix+"3",SuckCacheKeyPerfixEnum.zongbiao.getCode()));
			if(shengDatas==null||shengDatas.size()<1){
				if(differ>0){
					shengDatas= reportTotaldataDao.queryProvinceTotalInfoSJD(boReportTotaldata);
				}else{
					shengDatas= reportTotaldataDao.queryProvinceTotalInfo(boReportTotaldata);
				}
				if(shengDatas.size() > 0 && shengDatas != null){
					operations.set(cache.getSeed(Constant.QUERYPROVINCETOTALINFO+boReportTotaldata.getUserId()+bigareaData.getProvinceid()+bigareaData.getBigarea()+prefix+"3",SuckCacheKeyPerfixEnum.zongbiao.getCode()), shengDatas,86400, TimeUnit.SECONDS);
				}
			}
			// 循环省份
			for (ReportTotaldataDO shengData : shengDatas) {
				// 省份合计
				shengData.setCustomerName(shengData.getProvincename());
				shengData.setProvinceid(shengData.getProvinceid());
				shengData.setBigarea(shengData.getBigarea());
				allDatas.add(shengData.toReckonList());

			}
		}
		//查询截止时间当月的最后一天 ，决定基础信息生效的时间
		String startDay = DateUtils.getMonthEnd(endDate);
		if("D".equals(boReportTotaldata.getTmp_field()) || "S".equals(boReportTotaldata.getTmp_field())){}else{
			//一类省份id
			if(DateUtils.parseDate(endDate).before(DateUtils.parseDate(Constant.FIRST_PROVINCEIDSBEFORE2019_WORK))){
				// 广东+海南
				doWithProvinceCount(allDatas, startDate, endDate, new String[]{"440000","460000"}, "广东+海南");
			}else{
				// 广东+海南
				doWithProvinceCount(allDatas,startDate, endDate, new String[]{"440001","440002","440003","460000"}, "广东广州+广东东莞+广东揭阳+海南");
			}
		

		// 陕西+甘肃+青海+宁夏回族自治区
		doWithProvinceCount(allDatas, startDate, endDate, new String[]{"610000","620000","630000","640000"}, "陕西+甘肃+青海+宁夏");
		// 四川+西藏
		doWithProvinceCount(allDatas,startDate, endDate, new String[]{"510000","540000"}, "四川+西藏");
		//苏州+南京
		doWithProvinceCount(allDatas,startDate, endDate, new String[]{"320001","320002"}, "苏州+南京");
		//浙南+浙北
		doWithProvinceCount(allDatas, startDate, endDate, new String[]{"330001","330002"}, "浙南+浙北");
		// 上海大区+广东大区+华中西南大区（合计）
		doWithProvinceCount(allDatas, startDate, endDate, searchBigAreaCode(startDay,new String[]{"上海大区","广东大区","华中西南大区"}), " 上海大区+广东大区+华中西南大区（合计）");
		// 浙江大区+江苏大区（合计）
		doWithProvinceCount(allDatas,startDate, endDate, searchBigAreaCode(startDay,new String[]{"浙江大区","江苏大区"}), " 浙江大区+江苏大区（合计）");	

		}	
		return allDatas;
	}
	
	private String[] searchBigAreaCode (String startDay,String[] bigAreaName){
		
		List<HashMap<String,Object>> province = reportTotaldataDao.searchBigAreaCode(startDay,bigAreaName);		 
		String[] result =new String[province.size()];
		for(int i=0;i<province.size();i++){
			result[i]=province.get(i).get("province")+"";
		}
		 
		return result;
	}
	
	private String[] getCityIdsByBigarea(String endDate,String[] bigAreaName){
		
		List<HashMap<String,Object>> city = reportTotaldataDao.getCityIdsByBigarea(endDate,bigAreaName);		 
		String[] result =new String[city.size()];
		for(int i=0;i<city.size();i++){
			result[i]=city.get(i).get("CityID")+"";
		}
		 
		return result;
	}
	
	private String[] getCityIdsByProvinces(String endDate,List<Long> provinceIds){
		
		List<HashMap<String,Object>> city = reportTotaldataDao.getCityIdsByProvinces(endDate,provinceIds);		 
		String[] result =new String[city.size()];
		for(int i=0;i<city.size();i++){
			result[i]=city.get(i).get("CityID")+"";
		}
		 
		return result;
	}
	
	/**
	 * 根据账号的省份权限获取所属的网点公司编码
	 * @param endDate
	 * @param provinceIds
	 * @return
	 */
	private String[] getGSByBigarea(String endDate,String[] provinceIds){
		
		List<HashMap<String,Object>> gsList = reportTotaldataDao.getGSByProvinces(endDate,provinceIds);		 
		String[] result =new String[gsList.size()];
		for(int i=0;i<gsList.size();i++){
			result[i]=gsList.get(i).get("gs")+"";
		}
		 
		return result;
	}
	
	/** 多省合计 */
	private void doWithProvinceCount(List<ReportTotaldataDO> allData,Bo_ReportTotaldata boReportTotaldata, String[] provinceIds, String tagName){
		//String province = provinceIds[0];
    	Map<String,Object> provinceMap = new HashMap<String, Object>();
    	provinceMap.put("startDate", boReportTotaldata.getStartDate());
    	provinceMap.put("endDate", boReportTotaldata.getEndDate());
    	provinceMap.put("provinceIds",provinceIds);
    	//provinceMap.put("province",province);
		String provinceMapString = JSON.toJSONString(provinceMap);
	    String provinceMapPrefix = MD5Utils.encrypt(provinceMapString);
		ValueOperations<String, List<ReportTotaldataDO>> operations = redisTemplate.opsForValue();
		CachePrefixConformity cache = new CachePrefixConformity();
		int differ =StringUtils.isEmpty(boReportTotaldata.getEndDate())||StringUtils.isEmpty(boReportTotaldata.getStartDate())? 0:Integer.parseInt(boReportTotaldata.getEndDate().substring(boReportTotaldata.getEndDate().length()-2))-Integer.parseInt(boReportTotaldata.getStartDate().substring(boReportTotaldata.getStartDate().length()-2));

		List<ReportTotaldataDO> shengDatas = operations.get(cache.getSeed(Constant.QUERYMULTIPROVINCETOTALINFO+provinceMapPrefix+"1",SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		if(shengDatas==null||shengDatas.size()<1){
			if(differ>0)
				shengDatas= reportTotaldataDao.queryMultiProvinceTotalInfoSJD(provinceMap);
			else
				shengDatas= reportTotaldataDao.queryMultiProvinceTotalInfo(provinceMap);
			operations.set(cache.getSeed(Constant.QUERYMULTIPROVINCETOTALINFO+provinceMapPrefix+"1",SuckCacheKeyPerfixEnum.zongbiao.getCode()), shengDatas,86400, TimeUnit.SECONDS);
		}
		for(ReportTotaldataDO shengData : shengDatas){
			shengData.setCustomerName(tagName);
			allData.add(shengData.toReckonList());
		}
	}
	
	
	/** 多省合计 */
	private void doWithProvinceCount(List<ReportTotaldataDO> allData,String startDate,String endDate, String[] provinceIds, String tagName){
		String province = provinceIds[0];
		//provinceIds[0]="";
		Bo_ReportTotaldata boReportTotaldata =new Bo_ReportTotaldata();
		boReportTotaldata.setProvinceIds(provinceIds);
		boReportTotaldata.setStart_date(startDate);
		boReportTotaldata.setEnd_date(endDate);
		boReportTotaldata.setProvince_id(province);
		String prefix = startDate.replaceAll("-", "") + "_" +endDate.replaceAll("-", "");	
		ValueOperations<String, List<ReportTotaldataDO>> operations = redisTemplate.opsForValue();
		CachePrefixConformity cache = new CachePrefixConformity();
		int differ =StringUtils.isEmpty(endDate)||StringUtils.isEmpty(startDate)? 0:Integer.parseInt(endDate.substring(endDate.length()-2))-Integer.parseInt(startDate.substring(startDate.length()-2));

		List<ReportTotaldataDO> shengDatas = operations.get(cache.getSeed(Constant.QUERYMULTIPROVINCETOTALINFO+tagName+boReportTotaldata.getUserId()+prefix+"5",SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		if(shengDatas==null||shengDatas.size()<1){
			if(differ>0)
				shengDatas= reportTotaldataDao.queryMultiProvinceTotalInfoSJD(boReportTotaldata);
			else
				shengDatas= reportTotaldataDao.queryMultiProvinceTotalInfo(boReportTotaldata);
			operations.set(cache.getSeed(Constant.QUERYMULTIPROVINCETOTALINFO+tagName+boReportTotaldata.getUserId()+prefix+"5",SuckCacheKeyPerfixEnum.zongbiao.getCode()), shengDatas,86400, TimeUnit.SECONDS);
		}
		for(ReportTotaldataDO shengData : shengDatas){
			shengData.setCustomerName(tagName);
			allData.add(shengData.toReckonList());
		}
	}
	
	
	@Override
	public int cityCount(Bo_ReportTotaldata boReportTotaldata,UserDO cipuser){
		//不能选今天和今天之后
		int TableNum = reportTotaldataDao.findIfHasDate(boReportTotaldata.getEndDate());		
		if(TableNum == 0){
			return 1;
		}
		CachePrefixConformity cache = new CachePrefixConformity();
		ValueOperations<String, Integer> operations = redisTemplate.opsForValue();
		Map<String,Object> cityCountMap = new HashMap<String, Object>();
    	if("D".equals(boReportTotaldata.getTmpField())){
    		//大区权限  
    		//根据大区获取对应省份
    		if(boReportTotaldata.getBigareaNames() != null && boReportTotaldata.getBigareaNames().size() > 0){
            	String[] bigareas = new String[boReportTotaldata.getBigareaNames().size()];
            	boReportTotaldata.getBigareaNames().toArray(bigareas);
            	String[] TProvinceIDs = searchBigAreaCode(boReportTotaldata.getEndDate(),bigareas);
            	cityCountMap.put("provinceIds", TProvinceIDs);
            	cityCountMap.put("provinceIDs", TProvinceIDs);
            	cityCountMap.put("TTmpField", boReportTotaldata.getTmpField());
            	//搜索框查询省份id是否在账号所属省份权限内
            	if(null != boReportTotaldata.getProvinceId() && !"".equals(boReportTotaldata.getProvinceId())){
                	if(Arrays.asList(TProvinceIDs).contains(boReportTotaldata.getProvinceId())){
                		cityCountMap.put("provinceId", boReportTotaldata.getProvinceId());
                	}else{
                		//搜索框插询的省份不在改账号权限内
                		return 0;
                	}
            	}
            	//根据大区获取对应的城市id
            	String[] cityIds = getCityIdsByBigarea(boReportTotaldata.getEndDate(),bigareas);
            	//查询城市在所属账号权限内，放入参数map
            	if(null != boReportTotaldata.getCityId() && !"".equals(boReportTotaldata.getCityId())){
                	if(Arrays.asList(cityIds).contains(boReportTotaldata.getCityId())){
                		cityCountMap.put("cityId", boReportTotaldata.getCityId());
                	}else{
                		//搜索框插询的省份不在改账号权限内
            			return 0;
                	}
        		}
            }

    	}else if("S".equals(boReportTotaldata.getTmpField())){
        	Long[] provinces = new Long[boReportTotaldata.getProvinceids().size()];
        	boReportTotaldata.getProvinceids().toArray(provinces);
    		//省权限
    		cityCountMap.put("provinceIds", provinces);
    		cityCountMap.put("TTmpField", boReportTotaldata.getTmpField());
        	//搜索框查询省份id是否在账号所属省份权限内
        	if(null != boReportTotaldata.getProvinceId() && !"".equals(boReportTotaldata.getProvinceId())){
            	//搜索框查询省份id是否在账号所属省份权限内
            	if(boReportTotaldata.getProvinceids().contains(Long.parseLong(boReportTotaldata.getProvinceId()))){
            		cityCountMap.put("provinceId", boReportTotaldata.getProvinceId());
            	}else{
            		//搜索框插询的省份不在改账号权限内
            		return 0;
            	}
        	}

        	//根据省份获取对应的城市id
        	String[] cityIds = getCityIdsByProvinces(boReportTotaldata.getEndDate(),boReportTotaldata.getProvinceids());
        	//查询城市在所属账号权限内，放入参数map
        	if(null != boReportTotaldata.getCityId() && !"".equals(boReportTotaldata.getCityId())){
            	//查询城市在所属账号权限内，放入参数map
            	if(Arrays.asList(cityIds).contains(boReportTotaldata.getCityId())){
            		cityCountMap.put("cityId", boReportTotaldata.getCityId());
            	}else{
            		//搜索框插询的省份不在改账号权限内;
        			return 0;
            	}
        	}
    	}else if("wd".equals(boReportTotaldata.getTmpField())){
    		//网点权限，直接返回，无数据
    		//网点权限账号不会走到这里，保险起见
    		return 0;
    	}else{
    		//总部权限没有限制
    		cityCountMap.put("provinceId", boReportTotaldata.getProvinceId());
    		cityCountMap.put("cityId", boReportTotaldata.getCityId());
    	}
    	cityCountMap.put("startDate", boReportTotaldata.getStartDate());
    	cityCountMap.put("endDate", boReportTotaldata.getEndDate());
		String cityCountMapString = JSON.toJSONString(cityCountMap);
	    String cityCountMapPrefix = MD5Utils.encrypt(cityCountMapString);
		Integer cityCount = operations.get(cache.getSeed(Constant.QUERYCITYTOTALINFO+"cityCount"+cityCountMapPrefix+"10",SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		int differ =StringUtils.isEmpty(boReportTotaldata.getEndDate())||StringUtils.isEmpty(boReportTotaldata.getStartDate())? 0:Integer.parseInt(boReportTotaldata.getEndDate().substring(boReportTotaldata.getEndDate().length()-2))-Integer.parseInt(boReportTotaldata.getStartDate().substring(boReportTotaldata.getStartDate().length()-2));
		if(cityCount==null){
			if(differ>0){
				cityCount= reportTotaldataDao.cityCountSJD(cityCountMap);
			}else{
				cityCount= reportTotaldataDao.cityCount(cityCountMap);
			}
			if(cityCount > 0 && cityCount != null){
				operations.set(cache.getSeed(Constant.QUERYCITYTOTALINFO+"cityCount"+cityCountMapPrefix+"10",SuckCacheKeyPerfixEnum.zongbiao.getCode()), cityCount,86400, TimeUnit.SECONDS);
			}
		}
		return cityCount;
	}
	
	@Override
	public int save(ReportTotaldataDO reportTotaldata){
		return reportTotaldataDao.save(reportTotaldata);
	}
	
	@Override
	public int update(ReportTotaldataDO reportTotaldata){
		return reportTotaldataDao.update(reportTotaldata);
	}
	
	@Override
	public int remove(String bigarea){
		return reportTotaldataDao.remove(bigarea);
	}
	
	@Override
	public int batchRemove(String[] bigareas){
		return reportTotaldataDao.batchRemove(bigareas);
	}

/*	@Override
	public List<ReportTotaldataDO> queryProvinceMapReport(
			Bo_ReportTotaldata boReportTotaldata, UserDO UserDO) {
		
		String startDate = "";
		String endDate = "";
		List<ReportTotaldataDO> provinceData = new ArrayList<ReportTotaldataDO>();
		ReportTotaldataDO proState =new ReportTotaldataDO();
		String date_Today = DateUtils.format(new java.util.Date());
		//年
		String[] fege = date_Today.split("-");
		String year = fege[0];//年
		//加日期条件---日
		if(boReportTotaldata.getRi_date() !=null && ! boReportTotaldata.getRi_date().isEmpty()&& "0".equals(boReportTotaldata.getTotal_flag())){
			 startDate = boReportTotaldata.getRi_date();
			 endDate = boReportTotaldata.getRi_date();
		}
		//加日期条件---月
		else if(boReportTotaldata.getYue() !=null && ! boReportTotaldata.getYue().isEmpty()&& "1".equals(boReportTotaldata.getTotal_flag())){
			if(Integer.parseInt(boReportTotaldata.getYue().split("-")[1])<10){
				boReportTotaldata.setYue(boReportTotaldata.getYue().split("-")[0]+"-0"+boReportTotaldata.getYue().split("-")[1]);
			}
			 String a = boReportTotaldata.getYue()+"-01";
			 String endDatenum = DateUtils.getMonthEnd(a);
			 String[] b = endDatenum.split("-");
			 String daynum = b[2];
			 startDate = boReportTotaldata.getYue()+"-01";
			 endDate = boReportTotaldata.getYue()+"-"+daynum;
		}
		//不能选今天和今天之后
//		int TableNum = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM crmkh_report_order_stats_all WHERE qu_date = '"+endDate+"'", Integer.class);
		//不能选今天和今天之后
		int TableNum = reportTotaldataDao.findIfHasDate(endDate);
		if(TableNum == 0){
			proState.setCustomerName("rqcw");
			provinceData.add(0, proState);
			return provinceData;
		}else {
		
			Date currentTime = new Date();// 当前时间
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowtime = formatter.format(currentTime);
			Calendar now = Calendar.getInstance();
			Calendar nowAdd = Calendar.getInstance();

			Calendar c1 = Calendar.getInstance();
		    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
			String current = sdf.format(now.getTime());
			try {
				now.setTime(sdf.parse(current));
				now.add(Calendar.DATE, -1);
				now.add(Calendar.HOUR, 10);
				now.add(Calendar.MINUTE, 20);

				c1.setTime(formatter.parse(nowtime));
				c1.add(Calendar.DATE, -1);

			} catch (ParseException e1) {
			//	e1.printStackTrace();
			}
			nowAdd.setTime(new Date());
			nowAdd.add(Calendar.DATE, -1);		
			
		    String lastDate=sdf.format(nowAdd.getTime());

			if(endDate.equals(lastDate)){
				int result1 = now.compareTo(c1);// 比开始时间小，未开始
                if(result1>0){
                	proState.setCustomerName("rqcw");
                	provinceData.add(0, proState);
        			return provinceData;
                }
			}
	
		}
		
		
		//判断表是否存在
		final String traceId = System.currentTimeMillis() + "_" + new Random().nextInt();
		final String ssdate = startDate;
		final String eedate = endDate;
		//存redis之前再进行一次判断
		String pricePrefix = startDate.replaceAll("-", "") + "_" +endDate.replaceAll("-", "");
		StringBuffer htmlBuffer = new StringBuffer();
		 //权限管理-------------------------------------------------------------------------------------------"+UserDO.getUserId()+"
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("approvalState", 1);
		map.put("jobNumber", UserDO.getUserId());
		
		List<ReportJurisdictionTableDO> crmkh_report_jurisdiction_tableData = reportJurisdictionTableDao.list(map);

		 if(crmkh_report_jurisdiction_tableData != null & ! crmkh_report_jurisdiction_tableData.isEmpty()){
		 for (ReportJurisdictionTableDO qx : crmkh_report_jurisdiction_tableData) {
			if("C".equals(qx.getPermissionType()) || "J".equals(qx.getPermissionType())){
            //无权限限制
			}else if("D".equals(qx.getPermissionType())){
				boReportTotaldata.setRegion_id(qx.getBigarea());
                boReportTotaldata.setTmp_field("D");	
			}else if("S".equals(qx.getPermissionType())){
				if(!"city".equals(boReportTotaldata.getShowType()) & !"branch".equals(boReportTotaldata.getShowType()) & !"customer".equals(boReportTotaldata.getShowType())){
					boReportTotaldata.setProvince_id(qx.getProvince());}
				if("customer".equals(boReportTotaldata.getShowType())){
					//return "---------------------请返回---------------------";
					proState.setCustomerName("---------------------请返回---------------------");
					provinceData.add(0, proState);
					return provinceData;
				}
					boReportTotaldata.setTmp_field("S");
			}else{
				//return"qxyw";	//权限错误
				proState.setCustomerName("qxyw");
				provinceData.add(0, proState);
				return provinceData;
			}
		}	    
		 }else if(UserDO.getUserId().toString().equals("root")){
	      //无限制
		 }else if(UserDO.getOrgCode() != null & ! UserDO.getOrgCode().isEmpty()){
			 boReportTotaldata.setBranch_code(UserDO.getOrgCode());
				htmlBuffer.setLength(0);
				boReportTotaldata.setShowType("customer");
				boReportTotaldata.setTmp_field("W"); 
				proState.setCustomerName(pricePrefix+",wddl"+",zb,"+UserDO.getOrgCode());
				provinceData.add(0, proState);
				return provinceData;
		 }else{
				proState.setCustomerName("wsq");
				provinceData.add(0, proState);
				return provinceData;
		 }
		// --------------------------------------------------------------------------------------------------------------
		Map<String, String> sqlParam = new HashMap<String, String>();
		sqlParam.put("startDate", startDate);
		sqlParam.put("endDate",endDate);
		return doWithProvinceMap(htmlBuffer,boReportTotaldata,startDate, endDate);
	}*/
	
	
	
	/*private List<ReportTotaldataDO> doWithProvinceMap(StringBuffer htmlBuffer,Bo_ReportTotaldata boReportTotaldata,String startDate,String endDate) {
		String prefix = startDate.replaceAll("-", "") + "_" +endDate.replaceAll("-", "");
		String pricePrefix = startDate.replaceAll("-", "") + "_" +endDate.replaceAll("-", "");
		String bia = " WHERE 1=1";
		String pro = "";
		// 集团合计
		List<ReportTotaldataDO> allcountDatas = reportTotaldataDao.queryCompanyTotalInfo(startDate,endDate);
		//没有超级权限  不显示集团合计
		if("D".equals(boReportTotaldata.getTmp_field())){
			bia = bia +" and a.bigarea in('"+boReportTotaldata.getRegion_id()+"')";
		}else if("S".equals(boReportTotaldata.getTmp_field())){
            pro = pro +" and a.ProvinceID in("+boReportTotaldata.getProvince_id()+")";
		}else{
        }
		DecimalFormat df = new DecimalFormat("######.00");
		ReportTotaldataDO provinceMap = new ReportTotaldataDO();
		List<ReportTotaldataDO> provinceMapData =new ArrayList<ReportTotaldataDO>();
		provinceMap.setProvincename("广东");
		provinceMapData.add(0, provinceMap);
		ReportTotaldataDO provinceMap1 = new ReportTotaldataDO();
		provinceMap1.setProvincename("广西");
		provinceMapData.add(1, provinceMap1);
		ReportTotaldataDO provinceMap2 = new ReportTotaldataDO();
		provinceMap2.setProvincename("海南");
		provinceMapData.add(2, provinceMap2);
		ReportTotaldataDO provinceMap3 = new ReportTotaldataDO();
		provinceMap3.setProvincename("上海");
		provinceMapData.add(3, provinceMap3);
		ReportTotaldataDO provinceMap4 = new ReportTotaldataDO();
		provinceMap4.setProvincename("陕西");
		provinceMapData.add(4, provinceMap4);
		ReportTotaldataDO provinceMap5 = new ReportTotaldataDO();
		provinceMap5.setProvincename("甘肃");
		provinceMapData.add(5, provinceMap5);
		ReportTotaldataDO provinceMap6 = new ReportTotaldataDO();

		provinceMap6.setProvincename("宁夏");
		provinceMapData.add(6, provinceMap6);
		ReportTotaldataDO provinceMap7 = new ReportTotaldataDO();

		provinceMap7.setProvincename("新疆");
		provinceMapData.add(7, provinceMap7);
		ReportTotaldataDO provinceMap8 = new ReportTotaldataDO();

		provinceMap8.setProvincename("青海");
		provinceMapData.add(8, provinceMap8);
		ReportTotaldataDO provinceMap9 = new ReportTotaldataDO();

		provinceMap9.setProvincename("北京");
		provinceMapData.add(9, provinceMap9);
		ReportTotaldataDO provinceMap10 = new ReportTotaldataDO();

		provinceMap10.setProvincename("天津");
		provinceMapData.add(10, provinceMap10);
		ReportTotaldataDO provinceMap11 = new ReportTotaldataDO();

		provinceMap11.setProvincename("河北");
		provinceMapData.add(11, provinceMap11);
		ReportTotaldataDO provinceMap12 = new ReportTotaldataDO();

		provinceMap12.setProvincename("黑龙江");
		provinceMapData.add(12, provinceMap12);
		ReportTotaldataDO provinceMap13 = new ReportTotaldataDO();

		provinceMap13.setProvincename("吉林");
		provinceMapData.add(13, provinceMap13);
		ReportTotaldataDO provinceMap14 = new ReportTotaldataDO();

		provinceMap14.setProvincename("辽宁");
		provinceMapData.add(14, provinceMap14);
		ReportTotaldataDO provinceMap15 = new ReportTotaldataDO();

		provinceMap15.setProvincename("山西");
		provinceMapData.add(15, provinceMap15);
		ReportTotaldataDO provinceMap16 = new ReportTotaldataDO();

		provinceMap16.setProvincename("内蒙古");
		provinceMapData.add(16, provinceMap16);
		ReportTotaldataDO provinceMap17 = new ReportTotaldataDO();

		provinceMap17.setProvincename("江苏");
		provinceMapData.add(17, provinceMap17);
		ReportTotaldataDO provinceMap18 = new ReportTotaldataDO();

		provinceMap18.setProvincename("安徽");
		provinceMapData.add(18, provinceMap18);
		ReportTotaldataDO provinceMap19 = new ReportTotaldataDO();

		provinceMap19.setProvincename("山东");
		provinceMapData.add(19, provinceMap19);
		ReportTotaldataDO provinceMap20 = new ReportTotaldataDO();

		provinceMap20.setProvincename("浙江");
		provinceMapData.add(20, provinceMap20);
		ReportTotaldataDO provinceMap21 = new ReportTotaldataDO();

		provinceMap21.setProvincename("福建");
		provinceMapData.add(21, provinceMap21);
		ReportTotaldataDO provinceMap22 = new ReportTotaldataDO();

		provinceMap22.setProvincename("江西");
		provinceMapData.add(22, provinceMap22);
		ReportTotaldataDO provinceMap23 = new ReportTotaldataDO();

		provinceMap23.setProvincename("湖北");
		provinceMapData.add(23, provinceMap23);
		ReportTotaldataDO provinceMap24 = new ReportTotaldataDO();

		provinceMap24.setProvincename("湖南");
		provinceMapData.add(24, provinceMap24);
		ReportTotaldataDO provinceMap25 = new ReportTotaldataDO();

		provinceMap25.setProvincename("河南");
		provinceMapData.add(25, provinceMap25);
		ReportTotaldataDO provinceMap26 = new ReportTotaldataDO();

		provinceMap26.setProvincename("四川");
		provinceMapData.add(26, provinceMap26);
		ReportTotaldataDO provinceMap27 = new ReportTotaldataDO();

		provinceMap27.setProvincename("重庆");
		provinceMapData.add(27, provinceMap27);
		ReportTotaldataDO provinceMap28 = new ReportTotaldataDO();

		provinceMap28.setProvincename("云南");
		provinceMapData.add(28, provinceMap28);
		ReportTotaldataDO provinceMap29 = new ReportTotaldataDO();

		provinceMap29.setProvincename("贵州");
		provinceMapData.add(29, provinceMap29);
		ReportTotaldataDO provinceMap30 = new ReportTotaldataDO();

		provinceMap30.setProvincename("西藏");
		provinceMapData.add(30, provinceMap30);

		// 循环大区
		allcountDatas = reportTotaldataDao.queryBigareaTotalInfo(boReportTotaldata);
		
		for (ReportTotaldataDO bigareaData : allcountDatas) {
			if(StringUtils.isEmpty(bigareaData.getBigarea())) {
				continue;
			}
			// 大区合计
			if("S".equals(boReportTotaldata.getTmp_field())){}else{
			}
			List<ReportTotaldataDO> shengDatas = reportTotaldataDao.queryProvinceTotalInfo(boReportTotaldata);

			// 循环省份
            for (ReportTotaldataDO provinceData : provinceMapData){      	
				Double jiangsu_customer_sum =0.00;
				Double jiangsu_order_sum =0.00;
				Double jiangsu_dianzi_order_sum =0.00;

				Double zhejiang_customer_sum =0.00;
				Double zhejiang_order_sum =0.00;
				Double zhejiang_dianzi_order_sum =0.00;

				Double customer_sum =0.00;
				Double order_sum =0.00;
				Double dianzi_order_sum =0.00;
			for (ReportTotaldataDO shengData : shengDatas) {
				if(!StringUtils.isEmpty(shengData.getProvincename())){
					if((shengData.getProvincename().equals("苏州区省公司")||shengData.getProvincename().equals("南京区省公司")||shengData.getProvincename().equals("淮安区省公司"))&&provinceData.getProvincename().equals("江苏")){
						jiangsu_customer_sum+=shengData.getCustomerSum();
						jiangsu_order_sum+=shengData.getOrderSum();
						jiangsu_dianzi_order_sum+=shengData.getDianziOrderSum();
						provinceData.setOrderSum(Double.parseDouble(df.format(jiangsu_order_sum/10000)));
						provinceData.setDianziOrderSum(Double.parseDouble(df.format(jiangsu_dianzi_order_sum/10000)));
						provinceData.setCustomerSum(jiangsu_customer_sum);
					}
					else if((shengData.getProvincename().equals("浙南区省公司")||shengData.getProvincename().equals("浙北区省公司"))&&provinceData.getProvincename().equals("浙江")){
						zhejiang_customer_sum+=shengData.getCustomerSum();
						zhejiang_order_sum+=shengData.getOrderSum();
						zhejiang_dianzi_order_sum+=shengData.getDianziOrderSum();
						provinceData.setOrderSum(Double.parseDouble(df.format(zhejiang_order_sum/10000)));
						provinceData.setDianziOrderSum(Double.parseDouble(df.format(zhejiang_dianzi_order_sum/10000)));
						provinceData.setCustomerSum(zhejiang_customer_sum);

					}
					else if(shengData.getProvincename().indexOf(provinceData.getProvincename())>=0){
						customer_sum+=shengData.getCustomerSum();
						order_sum+=shengData.getOrderSum();
						dianzi_order_sum+=shengData.getDianziOrderSum();
						provinceData.setOrderSum(Double.parseDouble(df.format(order_sum/10000)));
						provinceData.setDianziOrderSum(Double.parseDouble(df.format(dianzi_order_sum/10000)));
						provinceData.setCustomerSum(customer_sum);
					}
				}			
			}
          }
		}
		return provinceMapData;	
	}*/

/*	@Override
	public List<ReportTotaldataDO> queryBranchMapReport(
			Bo_ReportTotaldata boReportTotaldata, UserDO UserDO) {

		String startDate = "";
		String endDate = "";
		List<ReportTotaldataDO> provinceData = new ArrayList<ReportTotaldataDO>();
		ReportTotaldataDO proState =new ReportTotaldataDO();
		String date_Today = DateUtils.format(new java.util.Date());
		//年
		String[] fege = date_Today.split("-");
		String year = fege[0];//年
		//加日期条件---日
		if(boReportTotaldata.getRi_date() !=null && ! boReportTotaldata.getRi_date().isEmpty()&& "0".equals(boReportTotaldata.getTotal_flag())){
			 startDate = boReportTotaldata.getRi_date();
			 endDate = boReportTotaldata.getRi_date();
		}
		//加日期条件---月
		else if(boReportTotaldata.getYue() !=null && ! boReportTotaldata.getYue().isEmpty()&& "1".equals(boReportTotaldata.getTotal_flag())){
			if(Integer.parseInt(boReportTotaldata.getYue().split("-")[1])<10){
				boReportTotaldata.setYue(boReportTotaldata.getYue().split("-")[0]+"-0"+boReportTotaldata.getYue().split("-")[1]);
			}
			 String a = boReportTotaldata.getYue()+"-01";
			 String endDatenum = DateUtils.getMonthEnd(a);
			 String[] b = endDatenum.split("-");
			 String daynum = b[2];
			 startDate = boReportTotaldata.getYue()+"-01";
			 endDate = boReportTotaldata.getYue()+"-"+daynum;
		}
		//不能选今天和今天之后
//		int TableNum = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM crmkh_report_order_stats_all WHERE qu_date = '"+endDate+"'", Integer.class);
		int TableNum = reportTotaldataDao.findIfHasDate(endDate);

		if(TableNum == 0){
			proState.setCustomerName("rqcw");
			provinceData.add(0, proState);
			return provinceData;
		}
		//存redis之前再进行一次判断
		String pricePrefix = startDate.replaceAll("-", "") + "_" +endDate.replaceAll("-", "");	
			Map<String,Object> map =new HashMap<String,Object>();
			map.put("approvalState", 1);
			map.put("jobNumber", UserDO.getUserId());
			
			List<ReportJurisdictionTableDO> crmkh_report_jurisdiction_tableData = reportJurisdictionTableDao.list(map);
	 
		 if(crmkh_report_jurisdiction_tableData != null & ! crmkh_report_jurisdiction_tableData.isEmpty()){
		 for (ReportJurisdictionTableDO qx : crmkh_report_jurisdiction_tableData) {
			if(qx.getPermissionType().equals("C") || qx.getPermissionType().equals("J")){
           //无权限限制
			}else if(qx.getPermissionType().equals("D")){
				boReportTotaldata.setRegion_id(qx.getBigarea());
               boReportTotaldata.setTmp_field("D");	
			}else if(qx.getPermissionType().equals("S")){
				if(!"city".equals(boReportTotaldata.getShowType()) & !"branch".equals(boReportTotaldata.getShowType()) & !"customer".equals(boReportTotaldata.getShowType())){
					boReportTotaldata.setProvince_id(qx.getProvince());}
				if("customer".equals(boReportTotaldata.getShowType())){
					//return "---------------------请返回---------------------";
					proState.setCustomerName("---------------------请返回---------------------");
					provinceData.add(0, proState);
					return provinceData;
				}
					boReportTotaldata.setTmp_field("S");
			}else{
				proState.setCustomerName("qxyw");
				provinceData.add(0, proState);
				return provinceData;
			}
		}	    
		 }else if(UserDO.getUserId().toString().equals("root")){
	      //无限制
		 }else if(UserDO.getOrgCode() != null & ! UserDO.getOrgCode().isEmpty()){
			 boReportTotaldata.setBranch_code(UserDO.getOrgCode());
				boReportTotaldata.setShowType("customer");
				boReportTotaldata.setTmp_field("W"); 
				proState.setCustomerName(pricePrefix+",wddl"+",zb,"+UserDO.getOrgCode());
				provinceData.add(0, proState);
				return provinceData;
		 }else{
				proState.setCustomerName("wsq");
				provinceData.add(0, proState);
				return provinceData;
		 }
		// --------------------------------------------------------------------------------------------------------------
		Map<String, String> sqlParam = new HashMap<String, String>();
		sqlParam.put("startDate", startDate);
		sqlParam.put("endDate",endDate);
		String prefix = startDate.replaceAll("-", "") + "_" +endDate.replaceAll("-", "");
		String bia = "";
		String pro = "";
		// 集团合计
		List<ReportTotaldataDO> allcountDatas = reportTotaldataDao.queryCompanyTotalInfo(startDate,endDate);
		//没有超级权限  不显示集团合计
		if("D".equals(boReportTotaldata.getTmp_field())){
			bia = bia +" and a.bigarea in('"+boReportTotaldata.getRegion_id()+"')";
		}else if("S".equals(boReportTotaldata.getTmp_field())){
            pro = pro +" and a.ProvinceID in("+boReportTotaldata.getProvince_id()+")";
		}else{
}	
		boReportTotaldata.setStart_date(startDate);
		boReportTotaldata.setEnd_date(endDate);
		String startDay = DateUtils.getMonthBegin(DateUtils.formatDate(new Date()));
		boReportTotaldata.setStartDay(startDay);
		List<ReportTotaldataDO> branchCountSql = reportTotaldataDao.queryBranchMapReport(boReportTotaldata);
		return branchCountSql;
		}*/

/*	@Override
	public List<ReportTotaldataDO> queryTotalMapReport(
			Bo_ReportTotaldata boReportTotaldata, UserDO UserDO) {

		String startDate = "";
		String endDate = "";
		List<ReportTotaldataDO> provinceData = new ArrayList<ReportTotaldataDO>();
		ReportTotaldataDO proState =new ReportTotaldataDO();
		String date_Today = DateUtils.format(new java.util.Date());
		//年
		String[] fege = date_Today.split("-");
		String year = fege[0];//年
		//加日期条件---日
		if(boReportTotaldata.getRi_date() !=null && ! boReportTotaldata.getRi_date().isEmpty()&& "0".equals(boReportTotaldata.getTotal_flag())){
			 startDate = boReportTotaldata.getRi_date();
			 endDate = boReportTotaldata.getRi_date();
		}
		//加日期条件---月
		else if(boReportTotaldata.getYue() !=null && ! boReportTotaldata.getYue().isEmpty()&& "1".equals(boReportTotaldata.getTotal_flag())){
			if(Integer.parseInt(boReportTotaldata.getYue().split("-")[1])<10){
				boReportTotaldata.setYue(boReportTotaldata.getYue().split("-")[0]+"-0"+boReportTotaldata.getYue().split("-")[1]);
			}
			 String a = boReportTotaldata.getYue()+"-01";
			 String endDatenum = DateUtils.getMonthEnd(a);
			 String[] b = endDatenum.split("-");
			 String daynum = b[2];
			 startDate = boReportTotaldata.getYue()+"-01";
			 endDate = boReportTotaldata.getYue()+"-"+daynum;
		}

		
		//不能选今天和今天之后
		int TableNum = reportTotaldataDao.findIfHasDate(endDate);
		if(TableNum == 0){
			proState.setCustomerName("rqcw");
			provinceData.add(0, proState);
			return provinceData;
		}	
		
		//存redis之前再进行一次判断
		String pricePrefix = startDate.replaceAll("-", "") + "_" +endDate.replaceAll("-", "");	
			Map<String,Object> map =new HashMap<String,Object>();
			map.put("approvalState", 1);
			map.put("jobNumber", UserDO.getUserId());
			
		List<ReportJurisdictionTableDO> crmkh_report_jurisdiction_tableData = reportJurisdictionTableDao.list(map);
 
		 if(crmkh_report_jurisdiction_tableData != null & ! crmkh_report_jurisdiction_tableData.isEmpty()){
		 for (ReportJurisdictionTableDO qx : crmkh_report_jurisdiction_tableData) {
			if(qx.getPermissionType().equals("C") || qx.getPermissionType().equals("J")){
           //无权限限制
			}else if(qx.getPermissionType().equals("D")){
				boReportTotaldata.setRegion_id(qx.getBigarea());
               boReportTotaldata.setTmp_field("D");	
				proState.setCustomerName("D");
				provinceData.add(0, proState);
				return provinceData;
			}else if(qx.getPermissionType().equals("S")){
				if(!"city".equals(boReportTotaldata.getShowType()) & !"branch".equals(boReportTotaldata.getShowType()) & !"customer".equals(boReportTotaldata.getShowType())){
					boReportTotaldata.setProvince_id(qx.getProvince());
					proState.setCustomerName("S");
					provinceData.add(0, proState);
					return provinceData;
					}
				if("customer".equals(boReportTotaldata.getShowType())){
					//return "---------------------请返回---------------------";
					proState.setCustomerName("S");
					provinceData.add(0, proState);
					return provinceData;
				}
					boReportTotaldata.setTmp_field("S");
			}else{
				//return"qxyw";	//权限错误
				proState.setCustomerName("qxyw");
				provinceData.add(0, proState);
				return provinceData;
			}
		}	    
		 }else if(UserDO.getUserId().toString().equals("root")){
	      //无限制
		 }else if(UserDO.getOrgCode() != null & ! UserDO.getOrgCode().isEmpty()){
			 boReportTotaldata.setBranch_code(UserDO.getOrgCode());
				boReportTotaldata.setShowType("customer");
				boReportTotaldata.setTmp_field("W"); 
				proState.setCustomerName("wd");
				provinceData.add(0, proState);
				return provinceData;
		 }else{
				proState.setCustomerName("wsq");
				provinceData.add(0, proState);
				return provinceData;
		 }
		// --------------------------------------------------------------------------------------------------------------	
		Map<String, String> sqlParam = new HashMap<String, String>();
		sqlParam.put("startDate", startDate);
		sqlParam.put("endDate",endDate);
		String prefix = startDate.replaceAll("-", "") + "_" +endDate.replaceAll("-", "");
		boReportTotaldata.setStart_date(startDate);
		List<ReportTotaldataDO> allcountDatas = reportTotaldataDao.queryTotalMapReport(boReportTotaldata);

		return allcountDatas;
	}*/

	@Override
	public int countSearchData(Map<String, Object> crmkh_report_cust_od_sumData){	
		String endDate =crmkh_report_cust_od_sumData.get("end_date").toString();
		String startDate =crmkh_report_cust_od_sumData.get("start_date").toString();
		int differ =StringUtils.isEmpty(endDate)||StringUtils.isEmpty(startDate)? 0:Integer.parseInt(endDate.substring(endDate.length()-2))-Integer.parseInt(startDate.substring(startDate.length()-2));
        if(differ>0){
    		return reportTotaldataDao.countSearchDataSJD(crmkh_report_cust_od_sumData);
        }else
        	return reportTotaldataDao.countSearchData(crmkh_report_cust_od_sumData);
	}
	
	@Override
	public List<ReportTotaldataDO> searchData(
			Map<String, Object> crmkh_report_cust_od_sumData) {
		String prefix = crmkh_report_cust_od_sumData.get("start_date").toString().replaceAll("-", "") + "_" +crmkh_report_cust_od_sumData.get("end_date").toString().replaceAll("-", "");	
		ValueOperations<String, List<ReportTotaldataDO>> operations = redisTemplate.opsForValue();
		CachePrefixConformity cache = new CachePrefixConformity();
		ValueOperations<String, Map<String, Object>> operationsCqkh = redisTemplate.opsForValue();
		String endDate =crmkh_report_cust_od_sumData.get("end_date").toString();
		String startDate =crmkh_report_cust_od_sumData.get("start_date").toString();

		List<ReportTotaldataDO> datas = operations.get(cache.getSeed(Constant.QUERYCUSTOMERTOTALINFO+prefix+crmkh_report_cust_od_sumData.get("offset")+crmkh_report_cust_od_sumData.get("limit")+crmkh_report_cust_od_sumData.get("customer_id")+crmkh_report_cust_od_sumData.get("branch_code")+"3",SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		if(datas==null||datas.size()<1){
			int differ =StringUtils.isEmpty(endDate)||StringUtils.isEmpty(startDate)? 0:Integer.parseInt(endDate.substring(endDate.length()-2))-Integer.parseInt(startDate.substring(startDate.length()-2));
            if(differ>0){
    			datas= reportTotaldataDao.searchDataSJD(crmkh_report_cust_od_sumData);
            }else
            	datas= reportTotaldataDao.searchData(crmkh_report_cust_od_sumData);
			operations.set(cache.getSeed(Constant.QUERYCUSTOMERTOTALINFO+prefix+crmkh_report_cust_od_sumData.get("offset")+crmkh_report_cust_od_sumData.get("limit")+crmkh_report_cust_od_sumData.get("customer_id")+crmkh_report_cust_od_sumData.get("branch_code")+"3",SuckCacheKeyPerfixEnum.zongbiao.getCode()), datas,86400, TimeUnit.SECONDS);
		}
		for (ReportTotaldataDO aa : datas) {
			if(aa.getCustomerId() != null && !aa.getCustomerId().isEmpty()){
				Map<String, Object> customerInfo = operationsCqkh.get(aa.getCustomerId());
				if(customerInfo != null && customerInfo.get("khmc") != null) {
					aa.setCustomerName(customerInfo.get("khmc").toString());
				}
			}	
			//菜鸟和京东库取商家id和店铺名称
			if("菜鸟".equals(aa.getCustomerSourceType())){
				String customerId = aa.getCustomerId()+"cn";
				Map<String, Object> sellerCNInfo = operationsCqkh.get(customerId);
				if(sellerCNInfo != null && sellerCNInfo.get("seller_id") != null) {
					aa.setSellerId(sellerCNInfo.get("seller_id").toString());
					aa.setSellerName(sellerCNInfo.get("seller_name").toString());
				}
			}
			if("京东".equals(aa.getCustomerSourceType())){
				String customerId = aa.getCustomerId()+"jd";
				Map<String, Object> sellerJDInfo = operationsCqkh.get(customerId);
				if(sellerJDInfo != null && sellerJDInfo.get("vendor_code") != null) {
					aa.setSellerId(sellerJDInfo.get("vendor_code").toString());
					aa.setSellerName(sellerJDInfo.get("vendor_name").toString());
				}
			}
			if("拼多多".equals(aa.getCustomerSourceType())){
				String customerId = aa.getCustomerId()+"pdd";
				Map<String, Object> sellerPDDInfo = operationsCqkh.get(customerId);
				if(sellerPDDInfo != null && sellerPDDInfo.get("vendor_code") != null) {
					aa.setSellerId(sellerPDDInfo.get("vendor_code").toString());
					aa.setSellerName(sellerPDDInfo.get("vendor_name").toString());
				}
			}
			
		}
			return datas;
	}
	
	@DataSourceAnnotation
	@Override
	public List<Map<String,Object>> queryGpCustomerSource(Map<String,Object> map,String dsId){
		return gpBasSCustPickTmpDao.queryGpCustomerSource(map);
	}
	
	@DataSourceAnnotation
	@Override
	public List<Map<String,Object>> queryGpSource(Map<String,Object> map,String dsId){
		return gpBasSCustPickTmpDao.queryGpSource(map);

	}
//获取菜鸟和京东的商家id和店铺名称
	@Override
	@DataSourceAnnotation
	public List<Map<String, Object>> searchSellerCN(
			Map<String, Object> map, String dsId) {
		
		return gpBasSCustPickTmpDao.searchSellerCN(map);
	}
	@Override
	@DataSourceAnnotation
	public List<Map<String, Object>> searchSellerJD(
			HashMap<String, Object> cqkh_paramMap, String dsId) {
		return gpBasSCustPickTmpDao.searchSellerJD(cqkh_paramMap);
	}
	@Override
	@DataSourceAnnotation
	public List<Map<String, Object>> searchSellerPDD(
			HashMap<String, Object> cqkh_paramMap, String dsId) {
		return gpBasSCustPickTmpDao.searchSellerPDD(cqkh_paramMap);
	}
	@Override
	public List<ExportReportTotaldataDO> filterData(
			List<ReportTotaldataDO> reportTotaldata) {
		List<ExportReportTotaldataDO> totalDate = new ArrayList<ExportReportTotaldataDO>();
		ExportReportTotaldataDO newTotal = new ExportReportTotaldataDO();
			
		for(ReportTotaldataDO data : reportTotaldata){			
	      if( data.getCustomerName() !=null&& !"".equals(data.getCustomerName())){
	    	  if("no".equals(data.getCustomerName())||"wait".equals(data.getCustomerName())||"rqcw".equals(data.getCustomerName())||"bt".equals(data.getCustomerName())){    		  
	    	  }else{
	    		  newTotal = new ExportReportTotaldataDO();  
	    		  newTotal.setCustomerName(data.getCustomerName());
	    		  newTotal.setOrderSum(data.getOrderSum());
	    		  newTotal.setOrderAvg(data.getOrderAvg());
	    		  newTotal.setDianziOrderSum(data.getDianziOrderSum());
	    		  newTotal.setOrdinaryOrderSum(data.getOrdinaryOrderSum());
	    		  newTotal.setDianziPercent(data.getDianziPercent());
	    		  newTotal.setCustomerSum(data.getCustomerSum());
	    		  
	    		  newTotal.setDianziOrderSumAvg(data.getDianziOrderSumAvg()); 
	    		  newTotal.setDianziPriceSumAvg(data.getDianziPriceSumAvg());
	    		  newTotal.setDianziPriceSum(data.getDianziPriceSum());
	    		  
	    		  newTotal.setaCustomerSum( data.getACustomerSum());
	    		  newTotal.setaOrderAvg(data.getAOrderAvg());
	    		  newTotal.setaOrderSum( data.getAOrderSum());
	    		  newTotal.setaPricePercent( data.getAPricePercent());
	    		  newTotal.setaPriceSum(data.getAPriceSum());
	    		  newTotal.setaAllPriceSum(data.getAAllPriceSum());

	    		  newTotal.setbCustomerSum( data.getBCustomerSum());
	    		  newTotal.setbOrderAvg(data.getBOrderAvg());
	    		  newTotal.setbOrderSum( data.getBOrderSum());
	    		  newTotal.setbPricePercent( data.getBPricePercent());
	    		  newTotal.setbPriceSum(data.getBPriceSum());
	    		  newTotal.setbAllPriceSum(data.getBAllPriceSum());
	    		  
	    		  newTotal.setcCustomerSum( data.getCCustomerSum());
	    		  newTotal.setcOrderAvg(data.getCOrderAvg());
	    		  newTotal.setcOrderSum( data.getCOrderSum());
	    		  newTotal.setcPricePercent( data.getCPricePercent());
	    		  newTotal.setcPriceSum(data.getCPriceSum());
	    		  newTotal.setcAllPriceSum(data.getCAllPriceSum());  
	    		
	    		  newTotal.setdCustomerSum( data.getDCustomerSum());
	    		  newTotal.setdOrderAvg(data.getDOrderAvg());
	    		  newTotal.setdOrderSum( data.getDOrderSum());
	    		  newTotal.setdPricePercent( data.getDPricePercent());
	    		  newTotal.setdPriceSum(data.getDPriceSum());
	    		  newTotal.setdAllPriceSum(data.getDAllPriceSum()); 
	    		  
	    		  newTotal.seteCustomerSum( data.getECustomerSum());
	    		  newTotal.seteOrderAvg(data.getEOrderAvg());
	    		  newTotal.seteOrderSum( data.getEOrderSum());
	    		  newTotal.setePricePercent( data.getEPricePercent());
	    		  newTotal.setePriceSum(data.getEPriceSum());
	    		  newTotal.seteAllPriceSum(data.getEAllPriceSum()); 
	    		  
	    		  newTotal.setfCustomerSum( data.getFCustomerSum());
	    		  newTotal.setfOrderAvg(data.getFOrderAvg());
	    		  newTotal.setfOrderSum( data.getFOrderSum());
	    		  newTotal.setfPricePercent( data.getFPricePercent());
	    		  newTotal.setfPriceSum(data.getFPriceSum());
	    		  newTotal.setfAllPriceSum(data.getFAllPriceSum()); 
	    		  
	    		  newTotal.setgCustomerSum( data.getGCustomerSum());
	    		  newTotal.setgOrderAvg(data.getGOrderAvg());
	    		  newTotal.setgOrderSum( data.getGOrderSum());
	    		  newTotal.setgPricePercent( data.getGPricePercent());
	    		  newTotal.setgPriceSum(data.getGPriceSum());
	    		  newTotal.setgAllPriceSum(data.getGAllPriceSum()); 
	    		  totalDate.add(newTotal);
	    		  
	    		  
	    	  }
	    	  
	      }
		
		}
		return totalDate;
	}
	
	@Override
	public List<ExportCustReportTotaldataDO> filterCustData(
			List<ReportTotaldataDO> reportTotaldata) {
		List<ExportCustReportTotaldataDO> totalDate = new ArrayList<ExportCustReportTotaldataDO>();
		ExportCustReportTotaldataDO newTotal = new ExportCustReportTotaldataDO();
		for(ReportTotaldataDO data : reportTotaldata){			
	     // if( data.getCustomerName() != null && "".equals(data.getCustomerName()) && "rqcw".equals(data.getCustomerName())){
	    	  
	    		  newTotal = new ExportCustReportTotaldataDO(); 
	    		  
	    		  newTotal.setCustomerId(data.getCustomerId());
	    		  newTotal.setCustomerName(data.getCustomerName());
	    		  newTotal.setSellerId(data.getSellerId());
	    		  newTotal.setSellerName(data.getSellerName());
	    		  newTotal.setBranchCode(data.getBranchCode()+"");
	    		  newTotal.setBranchName(data.getBranchName());
	    		  newTotal.setCustomerSourceType(data.getCustomerSourceType());
	    		  newTotal.setYjbm(data.getYjbm()+"");
	    		  newTotal.setYjmc(data.getYjmc()+"");
                  newTotal.setOrderSum(data.getOrderSum()+"");
	    		  newTotal.setOrderAvg(data.getOrderAvg()+"");
	    		  newTotal.setPriceLevel(data.getPriceLevel()+"");
	    		  newTotal.setDianziPriceSumAvg(data.getDianziPriceSumAvg()+"");
	    		  newTotal.setDianziPriceSum(data.getDianziPriceSum()+"");
	    		  totalDate.add(newTotal);	    	  
	      //}		
		}
		return totalDate;
	}
	
	@Override
	public List<ExportCustBranchReportTotaldataDO> filterCustBranchData(
			List<ReportTotaldataDO> reportTotaldata) {
		List<ExportCustBranchReportTotaldataDO> totalDate = new ArrayList<ExportCustBranchReportTotaldataDO>();
		ExportCustBranchReportTotaldataDO newTotal = new ExportCustBranchReportTotaldataDO();
		for(ReportTotaldataDO data : reportTotaldata){			
	      if( data.getCustomerName() !=null&& !"".equals(data.getCustomerName())){	    	  
	    		  newTotal = new ExportCustBranchReportTotaldataDO();  
	    		  newTotal.setCustomerId(data.getCustomerId());
	    		  newTotal.setCustomerName(data.getCustomerName());
	    		  newTotal.setSellerId(data.getSellerId());
	    		  newTotal.setSellerName(data.getSellerName());
	    		  newTotal.setBranchCode(data.getBranchCode()+"");
	    		  newTotal.setBranchName(data.getBranchName());
	    		  newTotal.setCustomerSourceType(data.getCustomerSourceType());
	    		  newTotal.setYjbm(data.getYjbm()+"");
	    		  newTotal.setYjmc(data.getYjmc()+"");
                  newTotal.setOrderSum(data.getOrderSum()+"");
	    		  newTotal.setOrderAvg(data.getOrderAvg()+"");
	    		  newTotal.setPriceLevel(data.getPriceLevel()+"");
	    		  totalDate.add(newTotal);	    	  
	      }		
		}
		return totalDate;
	}

	@Override
	public int branchCount(Bo_ReportTotaldata boReportTotaldata, UserDO cipuser) {
		//不能选今天和今天之后
		int TableNum = reportTotaldataDao.findIfHasDate(boReportTotaldata.getEndDate());		
		if(TableNum == 0){
			return 1; 
		}
		CachePrefixConformity cache = new CachePrefixConformity();
		int differ =StringUtils.isEmpty(boReportTotaldata.getEndDate())||StringUtils.isEmpty(boReportTotaldata.getStartDate())? 0:Integer.parseInt(boReportTotaldata.getEndDate().substring(boReportTotaldata.getEndDate().length()-2))-Integer.parseInt(boReportTotaldata.getStartDate().substring(boReportTotaldata.getStartDate().length()-2));
		ValueOperations<String, List<ReportTotaldataDO>> operations = redisTemplate.opsForValue();
		ValueOperations<String, Integer> coutnOperations = redisTemplate.opsForValue();
    	//Map<String,Object> cityMap = new HashMap<String, Object>();
    	Map<String,Object> branchMap = new HashMap<String, Object>();
    	
    	if("D".equals(boReportTotaldata.getTmpField())){	//大区权限  
    		//根据大区获取对应省份
    		if(boReportTotaldata.getBigareaNames() != null && boReportTotaldata.getBigareaNames().size() > 0){
            	String[] bigareas = new String[boReportTotaldata.getBigareaNames().size()];
            	boReportTotaldata.getBigareaNames().toArray(bigareas);
            	String[] TProvinceIDs = searchBigAreaCode(boReportTotaldata.getEndDate(),bigareas);
            	branchMap.put("provinceIds", TProvinceIDs);
            	branchMap.put("provinceIDs", TProvinceIDs);
            	branchMap.put("TTmpField", boReportTotaldata.getTmpField());
            	
            	//搜索框查询省份id是否在账号所属省份权限内
            	if(null != boReportTotaldata.getProvinceId() && !"".equals(boReportTotaldata.getProvinceId())){
    				if(Arrays.asList(TProvinceIDs).contains(boReportTotaldata.getProvinceId())){
    					branchMap.put("provinceId", boReportTotaldata.getProvinceId());
    				}else{
    					//搜索框插询的省份不在改账号权限内
    					//myqx 没有权限
    					return 0;
    				}
            	}

				
            	//根据大区获取对应的城市id
            	String[] cityIds = getCityIdsByBigarea(boReportTotaldata.getEndDate(),bigareas);
            	//查询城市在所属账号权限内，放入参数map
            	if(null != boReportTotaldata.getCityId() && !"".equals(boReportTotaldata.getCityId())){
                	if(Arrays.asList(cityIds).contains(boReportTotaldata.getCityId())){
                		branchMap.put("cityId", boReportTotaldata.getCityId());
                	}else{
                		//搜索框插询的省份不在改账号权限内
            			return 0;
                	}
            	}

            	//查询公司编码在所属账号权限内，放入参数map
            	if(null != boReportTotaldata.getGsbm() && !"".equals(boReportTotaldata.getGsbm())){
                	//根据大区获取对应的网点公司编码
                	String[] gsbm = getGSByBigarea(boReportTotaldata.getEndDate(),TProvinceIDs);
                	if(Arrays.asList(gsbm).contains(boReportTotaldata.getGsbm())){
                		branchMap.put("branchCode", boReportTotaldata.getGsbm());
                	}else{
                		//搜索框插询的公司编码不在改账号权限内
            			return 0;
                	}
            	}
    		}
    	}else if("S".equals(boReportTotaldata.getTmpField())){//省总权限
        	Long[] provinces = new Long[boReportTotaldata.getProvinceids().size()];
        	boReportTotaldata.getProvinceids().toArray(provinces);
    		//省权限
    		branchMap.put("provinceIds", provinces);
    		branchMap.put("provinceIDs", provinces);
    		branchMap.put("TTmpField", boReportTotaldata.getTmpField());
    		
        	
        	//搜索框查询省份id是否在账号所属省份权限内
    		if(null != boReportTotaldata.getProvinceId() && !"".equals(boReportTotaldata.getProvinceId())){
        		if(boReportTotaldata.getProvinceids().contains(Long.parseLong(boReportTotaldata.getProvinceId()))){
    				branchMap.put("provinceId", boReportTotaldata.getProvinceId());
    			}else{
    				//搜索框插询的省份不在改账号权限内
    				return 0;
    			}
    		}
    		

        	//查询城市在所属账号权限内，放入参数map
        	if(null != boReportTotaldata.getCityId() && !"".equals(boReportTotaldata.getCityId())){
            	//根据省份获取对应的城市id
            	String[] cityIds = getCityIdsByProvinces(boReportTotaldata.getEndDate(),boReportTotaldata.getProvinceids());
            	if(Arrays.asList(cityIds).contains(boReportTotaldata.getCityId())){
            		branchMap.put("cityId", boReportTotaldata.getCityId());
            	}else{
            		//搜索框插询的省份不在改账号权限内
        			return 0;
            	}
        	}


        	//查询公司编码在所属账号权限内，放入参数map
        	if(null != boReportTotaldata.getGsbm() && !"".equals(boReportTotaldata.getGsbm())){
        		String[] provinceids = new String[boReportTotaldata.getProvinceids().size()];
        		for(int i=0;i<boReportTotaldata.getProvinceids().size();i++){
        			provinceids[i] = boReportTotaldata.getProvinceids().get(i).toString();
        		}
            	//根据省份获取对应的网点公司编码
            	String[] gsbm = getGSByBigarea(boReportTotaldata.getEndDate(),provinceids);
            	if(Arrays.asList(gsbm).contains(boReportTotaldata.getGsbm())){
            		branchMap.put("branchCode", boReportTotaldata.getGsbm());
            	}else{
            		//搜索框插询的公司编码不在改账号权限内
        			return 0;
            	}
        	}

    	}else if("wd".equals(boReportTotaldata.getTmpField())){//网点权限
    		branchMap.put("TTmpField", boReportTotaldata.getTmpField());
    		branchMap.put("branchCode", boReportTotaldata.getBranchCode());
        	//查询公司编码在所属账号权限内，放入参数map
        	if(null != boReportTotaldata.getGsbm() && !"".equals(boReportTotaldata.getGsbm())){
        		if(boReportTotaldata.getGsbm().equals(boReportTotaldata.getBranchCode())){
        			
        		}else{
            		//搜索框插询的公司编码不在改账号权限内
        			return 0;
            	}
        	}
    	}else{
    		//总部权限没有限制
    		branchMap.put("provinceId", boReportTotaldata.getProvinceId());
    		branchMap.put("cityId", boReportTotaldata.getCityId());
    		branchMap.put("branchCode", boReportTotaldata.getGsbm());
    	}
    	branchMap.put("startDate", boReportTotaldata.getStartDate());
    	branchMap.put("endDate", boReportTotaldata.getEndDate());
		String branchCountMapString = JSON.toJSONString(branchMap);
	    String branchCountMapPrefix = MD5Utils.encrypt(branchCountMapString);
		Integer branchCount = coutnOperations.get(cache.getSeed(Constant.QUERYCITYTOTALINFO+"branchCount"+branchCountMapPrefix+"5",SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		if(branchCount==null){
			if(differ>0){
				branchCount= reportTotaldataDao.branchCountSJD(branchMap);
			}else{
				branchCount= reportTotaldataDao.branchCount(branchMap);
			}
			if(branchCount > 0 && branchCount != null){
				coutnOperations.set(cache.getSeed(Constant.QUERYCITYTOTALINFO+"branchCount"+branchCountMapPrefix+"5",SuckCacheKeyPerfixEnum.zongbiao.getCode()), branchCount,86400, TimeUnit.SECONDS);
			}
		}
		return branchCount;
	}

	@Override
	public int custCount(Bo_ReportTotaldata boReportTotaldata, UserDO cipuser) {
		CachePrefixConformity cache = new CachePrefixConformity();
		ValueOperations<String, Integer> operations = redisTemplate.opsForValue();
    	Map<String,Object> custCountMap = new HashMap<String, Object>();
		//不能选今天和今天之后
		int TableNum = reportTotaldataDao.findIfHasDate(boReportTotaldata.getEndDate());		
		if(TableNum == 0){;
			return 1;
		}
    	if("D".equals(boReportTotaldata.getTmpField())){
    		//大区权限  
    		//根据大区获取对应省份
    		if(boReportTotaldata.getBigareaNames() != null && boReportTotaldata.getBigareaNames().size() > 0){
            	String[] bigareas = new String[boReportTotaldata.getBigareaNames().size()];
            	boReportTotaldata.getBigareaNames().toArray(bigareas);
            	String[] TProvinceIDs = searchBigAreaCode(boReportTotaldata.getEndDate(),bigareas);
            	custCountMap.put("provinceIds", TProvinceIDs);
            	custCountMap.put("TTmpField", boReportTotaldata.getTmpField());
    		}
    	}else if("S".equals(boReportTotaldata.getTmpField())){
    		//省权限
    		custCountMap.put("provinceIds", boReportTotaldata.getProvinceIds());
    		custCountMap.put("TTmpField", boReportTotaldata.getTmpField());
    	}else if("wd".equals(boReportTotaldata.getTmpField())){
    		//网点权限
    		custCountMap.put("branchCode", boReportTotaldata.getBranchCode());
    		custCountMap.put("TTmpField", boReportTotaldata.getTmpField());
    	}else{
    		//总部权限没有限制
    	}
		//一类省份id
		if(DateUtils.parseDate(boReportTotaldata.getEndDate()).before(DateUtils.parseDate(Constant.FIRST_PROVINCEIDSBEFORE2019_WORK))){
			custCountMap.put("firstProvinceids", Constant.FIRST_PROVINCEIDSBEFORE2019);
		}else{
			custCountMap.put("firstProvinceids", Constant.FIRST_PROVINCEIDS);
		}
    	custCountMap.put("startDate", boReportTotaldata.getStartDate());
    	custCountMap.put("endDate", boReportTotaldata.getEndDate());
    	custCountMap.put("customerId", boReportTotaldata.getCustomerId());
    	custCountMap = readyMap(boReportTotaldata.getStartDate(), custCountMap);
		String custCountMapString = JSON.toJSONString(custCountMap);
	    String custCountMapPrefix = MD5Utils.encrypt(custCountMapString);
	    Integer custCount = operations.get(cache.getSeed(Constant.QUERYCUSTOMERTOTALINFO+"custCount"+custCountMapPrefix+"4",SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		if(custCount==null){
			int differ =StringUtils.isEmpty(boReportTotaldata.getEndDate())||StringUtils.isEmpty(boReportTotaldata.getStartDate())? 0:Integer.parseInt(boReportTotaldata.getEndDate().substring(boReportTotaldata.getEndDate().length()-2))-Integer.parseInt(boReportTotaldata.getStartDate().substring(boReportTotaldata.getStartDate().length()-2));
            if(differ>0){
            	//查询日期为2019.1.1日前的，按照老逻辑，2019.1.1日后的（if(true)），按照新逻辑
    			if(!DateUtils.parseDate(boReportTotaldata.getEndDate()).before(DateUtils.parseDate(Constant.NEWBEGINRUNTIME))){
    				//新逻辑账号是网点权限，不显示返利金额
    				if("wd".equals(boReportTotaldata.getTmpField())){
    					custCount= reportTotaldataDao.branchCustCountSJDNew(custCountMap);
    				}else{
    					custCount= reportTotaldataDao.custCountSJDNew(custCountMap);
    				}
    			}else{
    				//老逻辑账号是网点权限，不显示返利金额
    				if("wd".equals(boReportTotaldata.getTmpField())){
    					custCount= reportTotaldataDao.branchCustCountSJD(custCountMap);
    				}else{
    					custCount= reportTotaldataDao.custCountSJD(custCountMap);
    				}

    			}
            	
            }else{
            	
				//老逻辑账号是网点权限，不显示返利金额
				if("wd".equals(boReportTotaldata.getTmpField())){
					custCount= reportTotaldataDao.branchCustCount(custCountMap);
				}else{
					custCount= reportTotaldataDao.custCount(custCountMap);
				}
            }
            if(null != custCount && custCount > 0){
                operations.set(cache.getSeed(Constant.QUERYCUSTOMERTOTALINFO+"custCount"+custCountMapPrefix+"4",SuckCacheKeyPerfixEnum.zongbiao.getCode()), custCount,86400, TimeUnit.SECONDS);
            }
		}
		return custCount;
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
