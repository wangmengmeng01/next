package com.yunda.base.feiniao.report.service.impl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
import com.yunda.base.feiniao.report.domain.ReportJurisdictionTableDO;
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
	private CRMKHReportTotalSqltableServinceImpl reportTotalSqltableServince;
	
	@Autowired
	private CRMKHReportTotalManageServiceImpl totalManageServiceImpl;
	
	@Override
	public ReportTotaldataDO get(String bigarea){
		return reportTotaldataDao.get(bigarea);
	}
	
	@Override
	public List<ReportTotaldataDO> list(Bo_ReportTotaldata bo_ReportTotaldata,UserDO user){
		List<ReportTotaldataDO> totalData =new ArrayList<ReportTotaldataDO>();
		ReportTotaldataDO proState =new ReportTotaldataDO();
        String btFlag = "";
		String startDate = "";
		String endDate = "";
		String date_Today = DateUtils.format(new java.util.Date());
		//年
		String[] fege = date_Today.split("-");
		String year = fege[0];//年
		//加日期条件---日
		if(bo_ReportTotaldata.getQu_date() !=null && ! bo_ReportTotaldata.getQu_date().isEmpty()){
			 startDate = bo_ReportTotaldata.getQu_date();
			 endDate = bo_ReportTotaldata.getQu_date();
		}
		//加日期条件---月
		if(bo_ReportTotaldata.getMonth_year() !=null && ! bo_ReportTotaldata.getMonth_year().isEmpty()){  
			 String a = bo_ReportTotaldata.getMonth_year()+"-01";
			 String endDatenum = DateUtils.getMonthEnd(a);
			 String[] b = endDatenum.split("-");
			 String daynum = b[2];
			 startDate = bo_ReportTotaldata.getMonth_year()+"-01";
			 endDate = bo_ReportTotaldata.getMonth_year()+"-"+daynum;
		}
		//加日期条件---季度
		if(bo_ReportTotaldata.getQuarter_date() !=null && ! bo_ReportTotaldata.getQuarter_date().isEmpty() && bo_ReportTotaldata.getQuarter_year() !=null && ! bo_ReportTotaldata.getQuarter_year().isEmpty()){		
			if("1".equals(bo_ReportTotaldata.getQuarter_date())){
				 startDate = bo_ReportTotaldata.getQuarter_year()+"-01-01";
				 endDate = bo_ReportTotaldata.getQuarter_year()+"-03-31";
				}
			if("2".equals(bo_ReportTotaldata.getQuarter_date())){
				 String a = bo_ReportTotaldata.getQuarter_year()+"-04-01";
				 String endDatenum = DateUtils.getMonthEnd(a);
				 String[] b = endDatenum.split("-");
				 String daynum = b[2];
				 startDate = bo_ReportTotaldata.getQuarter_year()+"-04-"+daynum;
				 String c = bo_ReportTotaldata.getQuarter_year()+"-06-01";
				 String endnum = DateUtils.getMonthEnd(c);
				 String[] d = endnum.split("-");
				 String num = d[2];
				 endDate = bo_ReportTotaldata.getQuarter_year()+"-06-"+num;
			}
			if("3".equals(bo_ReportTotaldata.getQuarter_date())){
				 startDate = bo_ReportTotaldata.getQuarter_year()+"-07-01";
				 String a = bo_ReportTotaldata.getQuarter_year()+"-09-01";
				 String endDatenum = DateUtils.getMonthEnd(a);
				 String[] b = endDatenum.split("-");
				 String daynum = b[2];
				 endDate = bo_ReportTotaldata.getQuarter_year()+"-09-"+daynum;
			}
			if("4".equals(bo_ReportTotaldata.getQuarter_date())){
				 startDate = bo_ReportTotaldata.getQuarter_year()+"-10-01";
				 endDate = bo_ReportTotaldata.getQuarter_year()+"-12-31";
			}
		}
		//年
		if(bo_ReportTotaldata.getYear() !=null && ! bo_ReportTotaldata.getYear().isEmpty()){
			startDate = bo_ReportTotaldata.getYear()+"-01-01";
			endDate = bo_ReportTotaldata.getYear()+"-12-31";
		}
		//时间段
		if(bo_ReportTotaldata.getStart_date() !=null && ! bo_ReportTotaldata.getStart_date().isEmpty() && bo_ReportTotaldata.getEnd_date() !=null && ! bo_ReportTotaldata.getEnd_date().isEmpty()){
			startDate = bo_ReportTotaldata.getStart_date();
			endDate = bo_ReportTotaldata.getEnd_date();
		}		
		//不能选今天和今天之后
		int TableNum = reportTotaldataDao.findIfHasDate(endDate);		
		if(TableNum == 0){
			proState.setCustomerName("rqcw");
			totalData.add(proState);
			return totalData;
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
			}		
			nowAdd.setTime(new Date());
			nowAdd.add(Calendar.DATE, -1);			
		}
		HashMap<String,String> result = new HashMap<>();
		result.put("yone", startDate);
		result.put("ytwo", endDate);
	    //判断数据是否存在
		int count = reportTotaldataDao.countJeByCustomerSJD(result);
		int differ =StringUtils.isEmpty(endDate)||StringUtils.isEmpty(startDate)? 0:Integer.parseInt(endDate.substring(endDate.length()-2))-Integer.parseInt(startDate.substring(startDate.length()-2));
		if(count == 0 && differ>0){
		try{
			ValueOperations<String, String> operations = redisTemplate.opsForValue();
			String cacheTotal = operations.get(startDate+endDate+SuckCacheKeyPerfixEnum.zongbiao.getCode());		
			logger.warn("准备执行任务:queryReport" + startDate + endDate + "_" + cacheTotal);
			if(CRMKHReportTotalManageServiceImpl.REPORT_STATUS_WAIT.equals(cacheTotal)) {
				//return "wait";
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
		//存redis之前再进行一次判断
		String pricePrefix = startDate.replaceAll("-", "") + "_" +endDate.replaceAll("-", "");
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("approvalState", 1);
		map.put("jobNumber", user.getUserId());

		if(user.hasPerms(Constant.REPORT_ADMIN_PERMS)) {
			   //超级用户权限   无限制
			//系统菜单配置了report:admin:allperms权限标识   角色管理中当该用户的角色勾选该权限标识就说明该用户是超级用户  能查看所有报表的集团大区省市等所有数据	
			btFlag = "bt";
			bo_ReportTotaldata.setTmp_field("bt");

		}else {
				if(user.isProvinceqx()){
					if(!"city".equals(bo_ReportTotaldata.getShowType()) & !"branch".equals(bo_ReportTotaldata.getShowType()) & !"customer".equals(bo_ReportTotaldata.getShowType())){
						String province = "";
						if(user.getProvinceIds().size()>0){
							bo_ReportTotaldata.setProvinceIdsqx(user.getProvinceIds());

						}	
						bo_ReportTotaldata.setProvinceIdsqx(user.getProvinceIds());
						}
					if("customer".equals(bo_ReportTotaldata.getShowType())){
						//return "---------------------请返回---------------------";
						proState.setCustomerName("---------------------请返回---------------------");
						totalData.add(0, proState);
						return totalData;
					}
						bo_ReportTotaldata.setTmp_field("S");
				}else if(user.isBigareaqx()){
					String bigarea="";
					if(user.getBigareaNames().size()>0){
						bo_ReportTotaldata.setRegionIds(user.getBigareaNames());
					}		
	               bo_ReportTotaldata.setTmp_field("D");
					
				}else if(user.getOrgCode() != null & ! user.getOrgCode().isEmpty()){
				 	bo_ReportTotaldata.setBranch_code(user.getOrgCode());
						bo_ReportTotaldata.setShowType("customer");
						bo_ReportTotaldata.setTmp_field("W"); 
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
			Map<String, String> sqlParam = new HashMap<String, String>();
			sqlParam.put("startDate", startDate);
			sqlParam.put("endDate",endDate);
			bo_ReportTotaldata.setUserId(user.getUsername());
			if("city".equals(bo_ReportTotaldata.getShowType())) {
				totalData=doWithCity(new StringBuffer(),bo_ReportTotaldata,startDate, endDate);
			} else if ("branch".equals(bo_ReportTotaldata.getShowType())) {
				totalData=doWithBranch(new StringBuffer(),bo_ReportTotaldata,startDate, endDate);
			}else {
				totalData=doWithProvince(new StringBuffer(),bo_ReportTotaldata,startDate, endDate);				
			}
			if(totalData.size()>0){
				totalData.get(0).setBranchName(btFlag);
				totalData.get(0).setTmpField(bo_ReportTotaldata.getTmp_field());
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
		ValueOperations<String, List<ReportTotaldataDO>> operations = redisTemplate.opsForValue();
		CRMKHReportTotalManageServiceImpl.fixedThreadPool.submit(new Runnable() {			
		@Override
		public void run() {
			logger.warn("开始执行任务:queryReport" + ssdate + eedate);
			try {
				reportTotalSqltableServince.totalSql(ssdate, eedate);
				totalManageServiceImpl.setTotalReportStatus(ssdate, eedate, CRMKHReportTotalManageServiceImpl.REPORT_STATUS_OK);
			} catch (Exception e) {
				logger.warn(e.getMessage());
			}
			logger.warn("任务执行完成:queryReport" + ssdate + eedate);
 
			}
		});
	}
	
	private List<ReportTotaldataDO> doWithCity(StringBuffer htmlBuffer,Bo_ReportTotaldata bo_ReportTotaldata,String startDate,String endDate) {
		String prefix = startDate.replaceAll("-", "") + "_" +endDate.replaceAll("-", "");
		List<ReportTotaldataDO> allDatas = new ArrayList<ReportTotaldataDO>();
		bo_ReportTotaldata.setStart_date(startDate);
		bo_ReportTotaldata.setEnd_date(endDate);
		CachePrefixConformity cache = new CachePrefixConformity();
		ValueOperations<String, List<ReportTotaldataDO>> operations = redisTemplate.opsForValue();
		String provinceTotalInfo  = JSON.toJSONString(bo_ReportTotaldata);
	    String cacheTotalInfo = MD5Utils.encrypt(provinceTotalInfo);
		List<ReportTotaldataDO> shengDatas = operations.get(cache.getSeed(Constant.QUERYPROVINCETOTALINFO+cacheTotalInfo,SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		int differ =StringUtils.isEmpty(endDate)||StringUtils.isEmpty(startDate)? 0:Integer.parseInt(endDate.substring(endDate.length()-2))-Integer.parseInt(startDate.substring(startDate.length()-2));
		if(shengDatas==null||shengDatas.size()<1){
			if(differ>0)
				shengDatas= reportTotaldataDao.queryProvinceTotalInfoSJD(bo_ReportTotaldata);
			else
				shengDatas= reportTotaldataDao.queryProvinceTotalInfo(bo_ReportTotaldata);
			operations.set(cache.getSeed(Constant.QUERYPROVINCETOTALINFO+cacheTotalInfo,SuckCacheKeyPerfixEnum.zongbiao.getCode()), shengDatas,86400, TimeUnit.SECONDS);
		}
		if(shengDatas.size()>0){
		shengDatas.get(0).setCustomerName(shengDatas.get(0).getProvincename()+"合计");
		allDatas.add(shengDatas.get(0).toReckonList());
		}
		List<ReportTotaldataDO> shiDatas = operations.get(cache.getSeed(Constant.QUERYCITYTOTALINFO+bo_ReportTotaldata.getProvince_id()+bo_ReportTotaldata.getUserId()+prefix,SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		if(shiDatas==null||shiDatas.size()<1){
			if(differ>0)
				shiDatas= reportTotaldataDao.queryCityTotalInfoSJD(bo_ReportTotaldata.getProvince_id(), startDate,endDate,"");
			else
				shiDatas= reportTotaldataDao.queryCityTotalInfo(bo_ReportTotaldata.getProvince_id(), startDate,endDate,"");
			operations.set(cache.getSeed(Constant.QUERYCITYTOTALINFO+bo_ReportTotaldata.getProvince_id()+bo_ReportTotaldata.getUserId()+prefix,SuckCacheKeyPerfixEnum.zongbiao.getCode()), shiDatas,86400, TimeUnit.SECONDS);
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
	
	
	private  List<ReportTotaldataDO> doWithBranch(StringBuffer htmlBuffer,  Bo_ReportTotaldata bo_ReportTotaldata,String startDate,String endDate) {
		String prefix = startDate.replaceAll("-", "") + "_" +endDate.replaceAll("-", "");
		List<ReportTotaldataDO> allDatas = new ArrayList<ReportTotaldataDO>();
		bo_ReportTotaldata.setStart_date(startDate);
		bo_ReportTotaldata.setEnd_date(endDate);
		CachePrefixConformity cache = new CachePrefixConformity();
		int differ =StringUtils.isEmpty(endDate)||StringUtils.isEmpty(startDate)? 0:Integer.parseInt(endDate.substring(endDate.length()-2))-Integer.parseInt(startDate.substring(startDate.length()-2));
		ValueOperations<String, List<ReportTotaldataDO>> operations = redisTemplate.opsForValue();
		List<ReportTotaldataDO> shiDatas = operations.get(cache.getSeed(Constant.QUERYCITYTOTALINFO+bo_ReportTotaldata.getCity_id()+"branch"+bo_ReportTotaldata.getUserId()+prefix,SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		if(shiDatas==null||shiDatas.size()<1){
			if(differ>0)
				shiDatas= !"wddc".equals(bo_ReportTotaldata.getTmp_field())?reportTotaldataDao.queryCityTotalInfoSJD("", startDate,endDate,bo_ReportTotaldata.getCity_id()):
					reportTotaldataDao.queryCityTotalInfoSJD("", startDate,endDate,"");
			else
				shiDatas= !"wddc".equals(bo_ReportTotaldata.getTmp_field())?reportTotaldataDao.queryCityTotalInfo("", startDate,endDate,bo_ReportTotaldata.getCity_id()):
					reportTotaldataDao.queryCityTotalInfo("", startDate,endDate,"");
			operations.set(cache.getSeed(Constant.QUERYCITYTOTALINFO+bo_ReportTotaldata.getCity_id()+"branch"+bo_ReportTotaldata.getUserId()+prefix,SuckCacheKeyPerfixEnum.zongbiao.getCode()), shiDatas,86400, TimeUnit.SECONDS);
		}	
		
		shiDatas.get(0).setCustomerName(shiDatas.get(0).getCityname()+"合计");
		allDatas.add(shiDatas.get(0).toReckonList());
		List<ReportTotaldataDO> gsDatas = operations.get(cache.getSeed(Constant.QUERYBRANCHTOTALINFO+bo_ReportTotaldata.getCity_id()+"branch"+bo_ReportTotaldata.getUserId()+prefix,SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		if(gsDatas==null||gsDatas.size()<1){
			if(differ>0)
				gsDatas= !"wddc".equals(bo_ReportTotaldata.getTmp_field())?reportTotaldataDao.queryBranchTotalInfoSJD(startDate,endDate,bo_ReportTotaldata.getCity_id())
						:reportTotaldataDao.queryBranchTotalInfoSJD(startDate,endDate,"");
				else
				gsDatas= !"wddc".equals(bo_ReportTotaldata.getTmp_field())?reportTotaldataDao.queryBranchTotalInfo(startDate,endDate,bo_ReportTotaldata.getCity_id())
						:reportTotaldataDao.queryBranchTotalInfo(startDate,endDate,"");
			operations.set(cache.getSeed(Constant.QUERYBRANCHTOTALINFO+bo_ReportTotaldata.getCity_id()+"branch"+bo_ReportTotaldata.getUserId()+prefix,SuckCacheKeyPerfixEnum.zongbiao.getCode()), gsDatas,86400, TimeUnit.SECONDS);
		}	
		for (ReportTotaldataDO branchData : gsDatas) {
			branchData.setBigarea(branchData.getBigarea());
			branchData.setProvinceid(branchData.getProvinceid());
			branchData.setCityid(branchData.getCityid());
			branchData.setCustomerName(branchData.getBranchName());
			branchData.setBranchCode(branchData.getBranchCode());
			branchData.setTmpField(bo_ReportTotaldata.getTmp_field());

			allDatas.add(branchData.toReckonList());
		}
		return allDatas;

	}
	
	private List<ReportTotaldataDO> doWithProvince(StringBuffer htmlBuffer,Bo_ReportTotaldata bo_ReportTotaldata,String startDate,String endDate) {
		String prefix = startDate.replaceAll("-", "") + "_" +endDate.replaceAll("-", "");
		String pricePrefix = startDate.replaceAll("-", "") + "_" +endDate.replaceAll("-", "");
		String bia = " ";
		String pro = "";
		List<ReportTotaldataDO> allDatas = new ArrayList<ReportTotaldataDO>();
		bo_ReportTotaldata.setStart_date(startDate);
		bo_ReportTotaldata.setEnd_date(endDate);
		CachePrefixConformity cache = new CachePrefixConformity();
		int differ =StringUtils.isEmpty(endDate)||StringUtils.isEmpty(startDate)? 0:Integer.parseInt(endDate.substring(endDate.length()-2))-Integer.parseInt(startDate.substring(startDate.length()-2));

		// 集团合计
		ValueOperations<String, List<ReportTotaldataDO>> operations = redisTemplate.opsForValue();
		List<ReportTotaldataDO> allcountDatas = operations.get(cache.getSeed(Constant.QUERYCOMPANYTOTALINFO+bo_ReportTotaldata.getUserId()+prefix,SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		if(allcountDatas==null||allcountDatas.size()<1){
			if(differ>0)
				allcountDatas= reportTotaldataDao.queryCompanyTotalInfoSJD(startDate,endDate);
			else
				allcountDatas= reportTotaldataDao.queryCompanyTotalInfo(startDate,endDate);
			if(allcountDatas.size() > 0 && allcountDatas.get(0) != null){
				operations.set(cache.getSeed(Constant.QUERYCOMPANYTOTALINFO+bo_ReportTotaldata.getUserId()+prefix,SuckCacheKeyPerfixEnum.zongbiao.getCode()), allcountDatas,86400, TimeUnit.SECONDS);

			}
		}

		if("D".equals(bo_ReportTotaldata.getTmp_field())){
		}else if("S".equals(bo_ReportTotaldata.getTmp_field())){
		} else {
			if (allcountDatas.size() > 0 && allcountDatas.get(0) != null) {
				allcountDatas.get(0).setCustomerName("集团合计");
				allDatas.add(allcountDatas.get(0).toReckonList());
			} else {
				allDatas.add(new ReportTotaldataDO());
				return allDatas;
			}
		}
		allcountDatas = operations.get(cache.getSeed(Constant.QUERYBIGAREATOTALINFO+bo_ReportTotaldata.getUserId()+prefix,SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		if(allcountDatas==null||allcountDatas.size()<1){
			if(differ>0)
				allcountDatas= reportTotaldataDao.queryBigareaTotalInfoSJD(bo_ReportTotaldata);
			else
				allcountDatas= reportTotaldataDao.queryBigareaTotalInfo(bo_ReportTotaldata);
			operations.set(cache.getSeed(Constant.QUERYBIGAREATOTALINFO+bo_ReportTotaldata.getUserId()+prefix,SuckCacheKeyPerfixEnum.zongbiao.getCode()), allcountDatas,86400, TimeUnit.SECONDS);
		}
		
		for (ReportTotaldataDO bigareaData : allcountDatas) {
			if(StringUtils.isEmpty(bigareaData.getBigarea())) {
				continue;
			}
			// 大区合计
			if("S".equals(bo_ReportTotaldata.getTmp_field())){
				
			}else{
				bigareaData.setCustomerName(bigareaData.getBigarea()+"合计");
				allDatas.add(bigareaData.toReckonList());
			}
			bo_ReportTotaldata.setBig_area(bigareaData.getBigarea());
			List<ReportTotaldataDO> shengDatas = operations.get(cache.getSeed(Constant.QUERYPROVINCETOTALINFO+bo_ReportTotaldata.getUserId()+bigareaData.getProvinceid()+bigareaData.getBigarea()+prefix,SuckCacheKeyPerfixEnum.zongbiao.getCode()));
			if(shengDatas==null||shengDatas.size()<1){
				if(differ>0)
					shengDatas= reportTotaldataDao.queryProvinceTotalInfoSJD(bo_ReportTotaldata);
				else
					shengDatas= reportTotaldataDao.queryProvinceTotalInfo(bo_ReportTotaldata);
				operations.set(cache.getSeed(Constant.QUERYPROVINCETOTALINFO+bo_ReportTotaldata.getUserId()+bigareaData.getProvinceid()+bigareaData.getBigarea()+prefix,SuckCacheKeyPerfixEnum.zongbiao.getCode()), shengDatas,86400, TimeUnit.SECONDS);
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
		if("D".equals(bo_ReportTotaldata.getTmp_field()) || "S".equals(bo_ReportTotaldata.getTmp_field())){}else{
			//一类省份id
			if(DateUtils.parseDate(endDate).before(DateUtils.parseDate(Constant.FIRST_PROVINCEIDSBEFORE2019_WORK))){
				// 广东+海南
				doWithProvinceCount(allDatas,htmlBuffer, startDate, endDate, new String[]{"440000","460000"}, "广东+海南");
			}else{
				// 广东+海南
				doWithProvinceCount(allDatas,htmlBuffer, startDate, endDate, new String[]{"440001","440002","440003","460000"}, "广东广州+广东东莞+广东揭阳+海南");
			}
		

		// 陕西+甘肃+青海+宁夏回族自治区
		doWithProvinceCount(allDatas,htmlBuffer, startDate, endDate, new String[]{"610000","620000","630000","640000"}, "陕西+甘肃+青海+宁夏");
		// 四川+西藏
		doWithProvinceCount(allDatas,htmlBuffer, startDate, endDate, new String[]{"510000","540000"}, "四川+西藏");
		//苏州+南京
		doWithProvinceCount(allDatas,htmlBuffer, startDate, endDate, new String[]{"320001","320002"}, "苏州+南京");
		//浙南+浙北
		doWithProvinceCount(allDatas,htmlBuffer, startDate, endDate, new String[]{"330001","330002"}, "浙南+浙北");
		// 上海大区+广东大区+华中西南大区（合计）
		doWithProvinceCount(allDatas,htmlBuffer, startDate, endDate, searchBigAreaCode(startDay,new String[]{"上海大区","广东大区","华中西南大区"}), " 上海大区+广东大区+华中西南大区（合计）");
		// 浙江大区+江苏大区（合计）
		doWithProvinceCount(allDatas,htmlBuffer, startDate, endDate, searchBigAreaCode(startDay,new String[]{"浙江大区","江苏大区"}), " 浙江大区+江苏大区（合计）");	

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
	
	
	
	/** 多省合计 */
	private void doWithProvinceCount(List<ReportTotaldataDO> allData,StringBuffer htmlBuffer, String startDate,String endDate, String[] provinceIds, String tagName){
		String province = provinceIds[0];
		provinceIds[0]="";
		Bo_ReportTotaldata bo_ReportTotaldata =new Bo_ReportTotaldata();
		bo_ReportTotaldata.setProvinceIds(provinceIds);
		bo_ReportTotaldata.setStart_date(startDate);
		bo_ReportTotaldata.setEnd_date(endDate);
		bo_ReportTotaldata.setProvince_id(province);
		String prefix = startDate.replaceAll("-", "") + "_" +endDate.replaceAll("-", "");	
		ValueOperations<String, List<ReportTotaldataDO>> operations = redisTemplate.opsForValue();
		CachePrefixConformity cache = new CachePrefixConformity();
		int differ =StringUtils.isEmpty(endDate)||StringUtils.isEmpty(startDate)? 0:Integer.parseInt(endDate.substring(endDate.length()-2))-Integer.parseInt(startDate.substring(startDate.length()-2));

		List<ReportTotaldataDO> shengDatas = operations.get(cache.getSeed(Constant.QUERYMULTIPROVINCETOTALINFO+tagName+bo_ReportTotaldata.getUserId()+prefix,SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		if(shengDatas==null||shengDatas.size()<1){
			if(differ>0)
				shengDatas= reportTotaldataDao.queryMultiProvinceTotalInfoSJD(bo_ReportTotaldata);
			else
				shengDatas= reportTotaldataDao.queryMultiProvinceTotalInfo(bo_ReportTotaldata);
			operations.set(cache.getSeed(Constant.QUERYMULTIPROVINCETOTALINFO+tagName+bo_ReportTotaldata.getUserId()+prefix,SuckCacheKeyPerfixEnum.zongbiao.getCode()), shengDatas,86400, TimeUnit.SECONDS);
		}
		 for (ReportTotaldataDO shengData : shengDatas) {
			shengData.setCustomerName(tagName);
			allData.add(shengData.toReckonList());
		}
	}
	
	
	@Override
	public int count(Map<String, Object> map){
		return reportTotaldataDao.count(map);
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

	@Override
	public List<ReportTotaldataDO> queryProvinceMapReport(
			Bo_ReportTotaldata bo_ReportTotaldata, UserDO UserDO) {
		
		String startDate = "";
		String endDate = "";
		List<ReportTotaldataDO> provinceData = new ArrayList<ReportTotaldataDO>();
		ReportTotaldataDO proState =new ReportTotaldataDO();
		String date_Today = DateUtils.format(new java.util.Date());
		//年
		String[] fege = date_Today.split("-");
		String year = fege[0];//年
		//加日期条件---日
		if(bo_ReportTotaldata.getRi_date() !=null && ! bo_ReportTotaldata.getRi_date().isEmpty()&& "0".equals(bo_ReportTotaldata.getTotal_flag())){
			 startDate = bo_ReportTotaldata.getRi_date();
			 endDate = bo_ReportTotaldata.getRi_date();
		}
		//加日期条件---月
		else if(bo_ReportTotaldata.getYue() !=null && ! bo_ReportTotaldata.getYue().isEmpty()&& "1".equals(bo_ReportTotaldata.getTotal_flag())){
			if(Integer.parseInt(bo_ReportTotaldata.getYue().split("-")[1])<10){
				bo_ReportTotaldata.setYue(bo_ReportTotaldata.getYue().split("-")[0]+"-0"+bo_ReportTotaldata.getYue().split("-")[1]);
			}
			 String a = bo_ReportTotaldata.getYue()+"-01";
			 String endDatenum = DateUtils.getMonthEnd(a);
			 String[] b = endDatenum.split("-");
			 String daynum = b[2];
			 startDate = bo_ReportTotaldata.getYue()+"-01";
			 endDate = bo_ReportTotaldata.getYue()+"-"+daynum;
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
				bo_ReportTotaldata.setRegion_id(qx.getBigarea());
                bo_ReportTotaldata.setTmp_field("D");	
			}else if("S".equals(qx.getPermissionType())){
				if(!"city".equals(bo_ReportTotaldata.getShowType()) & !"branch".equals(bo_ReportTotaldata.getShowType()) & !"customer".equals(bo_ReportTotaldata.getShowType())){
					bo_ReportTotaldata.setProvince_id(qx.getProvince());}
				if("customer".equals(bo_ReportTotaldata.getShowType())){
					//return "---------------------请返回---------------------";
					proState.setCustomerName("---------------------请返回---------------------");
					provinceData.add(0, proState);
					return provinceData;
				}
					bo_ReportTotaldata.setTmp_field("S");
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
			 bo_ReportTotaldata.setBranch_code(UserDO.getOrgCode());
				htmlBuffer.setLength(0);
				bo_ReportTotaldata.setShowType("customer");
				bo_ReportTotaldata.setTmp_field("W"); 
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
		return doWithProvinceMap(htmlBuffer,bo_ReportTotaldata,startDate, endDate);
	}
	
	
	
	private List<ReportTotaldataDO> doWithProvinceMap(StringBuffer htmlBuffer,Bo_ReportTotaldata bo_ReportTotaldata,String startDate,String endDate) {
		String prefix = startDate.replaceAll("-", "") + "_" +endDate.replaceAll("-", "");
		String pricePrefix = startDate.replaceAll("-", "") + "_" +endDate.replaceAll("-", "");
		String bia = " WHERE 1=1";
		String pro = "";
		// 集团合计
		List<ReportTotaldataDO> allcountDatas = reportTotaldataDao.queryCompanyTotalInfo(startDate,endDate);
		//没有超级权限  不显示集团合计
		if("D".equals(bo_ReportTotaldata.getTmp_field())){
			bia = bia +" and a.bigarea in('"+bo_ReportTotaldata.getRegion_id()+"')";
		}else if("S".equals(bo_ReportTotaldata.getTmp_field())){
            pro = pro +" and a.ProvinceID in("+bo_ReportTotaldata.getProvince_id()+")";
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
		allcountDatas = reportTotaldataDao.queryBigareaTotalInfo(bo_ReportTotaldata);
		
		for (ReportTotaldataDO bigareaData : allcountDatas) {
			if(StringUtils.isEmpty(bigareaData.getBigarea())) {
				continue;
			}
			// 大区合计
			if("S".equals(bo_ReportTotaldata.getTmp_field())){}else{
			}
			List<ReportTotaldataDO> shengDatas = reportTotaldataDao.queryProvinceTotalInfo(bo_ReportTotaldata);

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
	}

	@Override
	public List<ReportTotaldataDO> queryBranchMapReport(
			Bo_ReportTotaldata bo_ReportTotaldata, UserDO UserDO) {

		String startDate = "";
		String endDate = "";
		List<ReportTotaldataDO> provinceData = new ArrayList<ReportTotaldataDO>();
		ReportTotaldataDO proState =new ReportTotaldataDO();
		String date_Today = DateUtils.format(new java.util.Date());
		//年
		String[] fege = date_Today.split("-");
		String year = fege[0];//年
		//加日期条件---日
		if(bo_ReportTotaldata.getRi_date() !=null && ! bo_ReportTotaldata.getRi_date().isEmpty()&& "0".equals(bo_ReportTotaldata.getTotal_flag())){
			 startDate = bo_ReportTotaldata.getRi_date();
			 endDate = bo_ReportTotaldata.getRi_date();
		}
		//加日期条件---月
		else if(bo_ReportTotaldata.getYue() !=null && ! bo_ReportTotaldata.getYue().isEmpty()&& "1".equals(bo_ReportTotaldata.getTotal_flag())){
			if(Integer.parseInt(bo_ReportTotaldata.getYue().split("-")[1])<10){
				bo_ReportTotaldata.setYue(bo_ReportTotaldata.getYue().split("-")[0]+"-0"+bo_ReportTotaldata.getYue().split("-")[1]);
			}
			 String a = bo_ReportTotaldata.getYue()+"-01";
			 String endDatenum = DateUtils.getMonthEnd(a);
			 String[] b = endDatenum.split("-");
			 String daynum = b[2];
			 startDate = bo_ReportTotaldata.getYue()+"-01";
			 endDate = bo_ReportTotaldata.getYue()+"-"+daynum;
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
				bo_ReportTotaldata.setRegion_id(qx.getBigarea());
               bo_ReportTotaldata.setTmp_field("D");	
			}else if(qx.getPermissionType().equals("S")){
				if(!"city".equals(bo_ReportTotaldata.getShowType()) & !"branch".equals(bo_ReportTotaldata.getShowType()) & !"customer".equals(bo_ReportTotaldata.getShowType())){
					bo_ReportTotaldata.setProvince_id(qx.getProvince());}
				if("customer".equals(bo_ReportTotaldata.getShowType())){
					//return "---------------------请返回---------------------";
					proState.setCustomerName("---------------------请返回---------------------");
					provinceData.add(0, proState);
					return provinceData;
				}
					bo_ReportTotaldata.setTmp_field("S");
			}else{
				proState.setCustomerName("qxyw");
				provinceData.add(0, proState);
				return provinceData;
			}
		}	    
		 }else if(UserDO.getUserId().toString().equals("root")){
	      //无限制
		 }else if(UserDO.getOrgCode() != null & ! UserDO.getOrgCode().isEmpty()){
			 bo_ReportTotaldata.setBranch_code(UserDO.getOrgCode());
				bo_ReportTotaldata.setShowType("customer");
				bo_ReportTotaldata.setTmp_field("W"); 
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
		if("D".equals(bo_ReportTotaldata.getTmp_field())){
			bia = bia +" and a.bigarea in('"+bo_ReportTotaldata.getRegion_id()+"')";
		}else if("S".equals(bo_ReportTotaldata.getTmp_field())){
            pro = pro +" and a.ProvinceID in("+bo_ReportTotaldata.getProvince_id()+")";
		}else{
}	
		bo_ReportTotaldata.setStart_date(startDate);
		bo_ReportTotaldata.setEnd_date(endDate);
		String startDay = DateUtils.getMonthBegin(DateUtils.formatDate(new Date()));
		bo_ReportTotaldata.setStartDay(startDay);
		List<ReportTotaldataDO> branchCountSql = reportTotaldataDao.queryBranchMapReport(bo_ReportTotaldata);
		return branchCountSql;
		}

	@Override
	public List<ReportTotaldataDO> queryTotalMapReport(
			Bo_ReportTotaldata bo_ReportTotaldata, UserDO UserDO) {

		String startDate = "";
		String endDate = "";
		List<ReportTotaldataDO> provinceData = new ArrayList<ReportTotaldataDO>();
		ReportTotaldataDO proState =new ReportTotaldataDO();
		String date_Today = DateUtils.format(new java.util.Date());
		//年
		String[] fege = date_Today.split("-");
		String year = fege[0];//年
		//加日期条件---日
		if(bo_ReportTotaldata.getRi_date() !=null && ! bo_ReportTotaldata.getRi_date().isEmpty()&& "0".equals(bo_ReportTotaldata.getTotal_flag())){
			 startDate = bo_ReportTotaldata.getRi_date();
			 endDate = bo_ReportTotaldata.getRi_date();
		}
		//加日期条件---月
		else if(bo_ReportTotaldata.getYue() !=null && ! bo_ReportTotaldata.getYue().isEmpty()&& "1".equals(bo_ReportTotaldata.getTotal_flag())){
			if(Integer.parseInt(bo_ReportTotaldata.getYue().split("-")[1])<10){
				bo_ReportTotaldata.setYue(bo_ReportTotaldata.getYue().split("-")[0]+"-0"+bo_ReportTotaldata.getYue().split("-")[1]);
			}
			 String a = bo_ReportTotaldata.getYue()+"-01";
			 String endDatenum = DateUtils.getMonthEnd(a);
			 String[] b = endDatenum.split("-");
			 String daynum = b[2];
			 startDate = bo_ReportTotaldata.getYue()+"-01";
			 endDate = bo_ReportTotaldata.getYue()+"-"+daynum;
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
				bo_ReportTotaldata.setRegion_id(qx.getBigarea());
               bo_ReportTotaldata.setTmp_field("D");	
				proState.setCustomerName("D");
				provinceData.add(0, proState);
				return provinceData;
			}else if(qx.getPermissionType().equals("S")){
				if(!"city".equals(bo_ReportTotaldata.getShowType()) & !"branch".equals(bo_ReportTotaldata.getShowType()) & !"customer".equals(bo_ReportTotaldata.getShowType())){
					bo_ReportTotaldata.setProvince_id(qx.getProvince());
					proState.setCustomerName("S");
					provinceData.add(0, proState);
					return provinceData;
					}
				if("customer".equals(bo_ReportTotaldata.getShowType())){
					//return "---------------------请返回---------------------";
					proState.setCustomerName("S");
					provinceData.add(0, proState);
					return provinceData;
				}
					bo_ReportTotaldata.setTmp_field("S");
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
			 bo_ReportTotaldata.setBranch_code(UserDO.getOrgCode());
				bo_ReportTotaldata.setShowType("customer");
				bo_ReportTotaldata.setTmp_field("W"); 
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
		bo_ReportTotaldata.setStart_date(startDate);
		List<ReportTotaldataDO> allcountDatas = reportTotaldataDao.queryTotalMapReport(bo_ReportTotaldata);

		return allcountDatas;
	}

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

		List<ReportTotaldataDO> datas = operations.get(cache.getSeed(Constant.QUERYCUSTOMERTOTALINFO+prefix+crmkh_report_cust_od_sumData.get("offset")+crmkh_report_cust_od_sumData.get("limit")+crmkh_report_cust_od_sumData.get("customer_id")+crmkh_report_cust_od_sumData.get("branch_code"),SuckCacheKeyPerfixEnum.zongbiao.getCode()));
		if(datas==null||datas.size()<1){
			int differ =StringUtils.isEmpty(endDate)||StringUtils.isEmpty(startDate)? 0:Integer.parseInt(endDate.substring(endDate.length()-2))-Integer.parseInt(startDate.substring(startDate.length()-2));
            if(differ>0){
    			datas= reportTotaldataDao.searchDataSJD(crmkh_report_cust_od_sumData);
            }else
            	datas= reportTotaldataDao.searchData(crmkh_report_cust_od_sumData);
			operations.set(cache.getSeed(Constant.QUERYCUSTOMERTOTALINFO+prefix+crmkh_report_cust_od_sumData.get("offset")+crmkh_report_cust_od_sumData.get("limit")+crmkh_report_cust_od_sumData.get("customer_id")+crmkh_report_cust_od_sumData.get("branch_code"),SuckCacheKeyPerfixEnum.zongbiao.getCode()), datas,86400, TimeUnit.SECONDS);
		}
		for (ReportTotaldataDO aa : datas) {
			//查询日期为2019.1.1日前的，按照老逻辑，2019.1.1日后的（if(true)），按照新逻辑
			if(!DateUtils.parseDate(endDate).before(DateUtils.parseDate(Constant.NEWBEGINRUNTIME))){
				if(aa.getOrderAvg() <= 50) {
					aa.setPriceLevel("A类");
				}
				if(aa.getOrderAvg() > 50 && aa.getOrderAvg() <=100) {
					aa.setPriceLevel("B类");
				}
				if(aa.getOrderAvg() >100 && aa.getOrderAvg() <=1000) {
					aa.setPriceLevel("C类");
				}
				if(aa.getOrderAvg() >1000 && aa.getOrderAvg() <=3000) {
					aa.setPriceLevel("D类");
				}
				if(aa.getOrderAvg() >3000 && aa.getOrderAvg() <3000) {
					aa.setPriceLevel("E类");
				}
				if(aa.getOrderAvg() >=3000 && aa.getOrderAvg() <=5000) {
					aa.setPriceLevel("F类");
				}
				if(aa.getOrderAvg() >5000) {
					aa.setPriceLevel("G类");
				}
			}else{
				if(aa.getOrderAvg() < 50) {
					aa.setPriceLevel("A类");
				}
				if(aa.getOrderAvg() >= 50 && aa.getOrderAvg() <200) {
					aa.setPriceLevel("B类");
				}
				if(aa.getOrderAvg() >=200 && aa.getOrderAvg() <1000) {
					aa.setPriceLevel("C类");
				}
				if(aa.getOrderAvg() >=1000 && aa.getOrderAvg() <2000) {
					aa.setPriceLevel("D类");
				}
				if(aa.getOrderAvg() >=2000 && aa.getOrderAvg() <3000) {
					aa.setPriceLevel("E类");
				}
				if(aa.getOrderAvg() >=3000 && aa.getOrderAvg() <5000) {
					aa.setPriceLevel("F类");
				}
				if(aa.getOrderAvg() >=5000) {
					aa.setPriceLevel("G类");
				}
			}

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
	    		  newTotal.setCustomerAvgSum(data.getCustomerAvgSum());
	    		  newTotal.setCustomerPriceSum(data.getCustomerPriceSum());
	    		  newTotal.setCustomerAllPriceSum(data.getCustomerAllPriceSum());
	    		  
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
	    		  
	    		  newTotal.setfCustomerSum( data.getECustomerSum());
	    		  newTotal.setfOrderAvg(data.getEOrderAvg());
	    		  newTotal.setfOrderSum( data.getEOrderSum());
	    		  newTotal.setfPricePercent( data.getEPricePercent());
	    		  newTotal.setfPriceSum(data.getEPriceSum());
	    		  newTotal.setfAllPriceSum(data.getEAllPriceSum()); 
	    		  
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
	      if( data.getCustomerName() !=null&& !"".equals(data.getCustomerName())){	    	  
	    		  newTotal = new ExportCustReportTotaldataDO();  
	    		  newTotal.setCustomerId(data.getCustomerId());
	    		  newTotal.setCustomerName(data.getCustomerName());
	    		  newTotal.setBranchCode(data.getBranchCode()+"");
	    		  newTotal.setBranchName(data.getBranchName());
	    		  newTotal.setCustomerSourceType(data.getCustomerSourceType());
	    		  newTotal.setYjbm(data.getYjbm()+"");
	    		  newTotal.setYjmc(data.getYjmc()+"");
                  newTotal.setOrderSum(data.getOrderSum()+"");
	    		  newTotal.setOrderAvg(data.getOrderAvg()+"");
	    		  newTotal.setPriceLevel(data.getPriceLevel()+"");
	    		  newTotal.setPriceSum(data.getPriceSum()+"");
	    		  newTotal.setDianziOrderSum(data.getDianziOrderSum()+"");
	    		  totalDate.add(newTotal);	    	  
	      }		
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

		
}
