package com.yunda.base.feiniao.report.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.yunda.base.common.config.Constant;
import com.yunda.base.feiniao.report.bo.Bo_ReportWarning;
import com.yunda.base.feiniao.report.dao.ReportWarningDao;
import com.yunda.base.feiniao.report.domain.ExportReportWarningBranchDO;
import com.yunda.base.feiniao.report.domain.ExportReportWarningdataDO;
import com.yunda.base.feiniao.report.domain.ReportWarningDO;
import com.yunda.base.feiniao.report.service.ReportWarningService;
import com.yunda.base.feiniao.report.utils.CachePrefixConformity;
import com.yunda.base.feiniao.schedule.suckdata.enums.SuckCacheKeyPerfixEnum;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.service.MenuService;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.MD5Utils;

@Service
public class ReportWarningServiceImpl implements ReportWarningService {
	
	@Autowired
	private CqkhCustomerCacheService cqkhCustomerCacheService;
	
	@Autowired
	private ReportWarningDao reportWarningDao;
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	public ReportWarningDO get(String bigarea){
		return reportWarningDao.get(bigarea);
	}
	
	@Override
	public List<ReportWarningDO> list(Map<String, Object> map){
		return reportWarningDao.list(map);
	}
	
	@Override
	public int count(ReportWarningDO reportWarningDO){
		String startDate =  reportWarningDO.getStartDate();
		Date _startDate = DateUtils.parseDate(startDate, "yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
		cal.setTime(_startDate);
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得目标跑数日期是一个星期的第几天  
        if (1 == dayWeek) {  
           cal.add(Calendar.DAY_OF_MONTH, -1);  
        }  
         // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
        cal.setFirstDayOfWeek(Calendar.MONDAY);  
        // 获得当前日期是一个星期的第几天  
        int day = cal.get(Calendar.DAY_OF_WEEK); 
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day-7); //获得上周周一日期
		String searchWeek = sdf.format(cal.getTime()); 
		
		reportWarningDO.setSearch_week(searchWeek);
		return reportWarningDao.count(reportWarningDO);
	}
	
	@Override
	public int save(ReportWarningDO reportWarning){
		
		return reportWarningDao.save(reportWarning);
	}
	
	@Override
	public int update(ReportWarningDO reportWarning){
		return reportWarningDao.update(reportWarning);
	}
	
	@Override
	public int remove(String bigarea){
		return reportWarningDao.remove(bigarea);
	}
	
	@Override
	public int batchRemove(String[] bigareas){
		return reportWarningDao.batchRemove(bigareas);
	}
//查询预警数据
	@Override
	public List<ReportWarningDO> list(Bo_ReportWarning boReportWarning, UserDO loginUser) {
		List<ReportWarningDO> totalData =new ArrayList<ReportWarningDO>();
		
		//权限
		ReportWarningDO proState =new ReportWarningDO();
		
		String startDate = boReportWarning.getStartDate() ;
		String endDate = boReportWarning.getEndDate();
		
		Date _startDate = DateUtils.parseDate(startDate, "yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
		cal.setTime(_startDate);
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得目标跑数日期是一个星期的第几天  
        if (1 == dayWeek) {  
           cal.add(Calendar.DAY_OF_MONTH, -1);  
        }  
         // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
        cal.setFirstDayOfWeek(Calendar.MONDAY);  
        // 获得当前日期是一个星期的第几天  
        int day = cal.get(Calendar.DAY_OF_WEEK); 
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day-7); //获得上周周一日期
		String searchWeek = sdf.format(cal.getTime());
		boReportWarning.setSearch_week(searchWeek);
		
		/**1  生成数据部分
		 * 比较 tmpv2_cust_od_sum(要查询的时间段) 和   task每月1号跑上个月的预警月份数据tmpv2_cust_warning_Month_Sum(上个月)
		 * 生成该时间段内预警的数据  之前的逻辑是生成临时表  延用该逻辑但不生成临时表    改成从这个查询结果中再嵌套查询   展示到前端
		 */
		
		
//============================================================================		
		/**2  权限管理部分
		 * 
		 */
		
		if(loginUser.hasPerms(Constant.REPORT_ADMIN_PERMS)) {
		   //超级用户权限   无限制
		//系统菜单配置了report:admin:allperms权限标识   角色管理中当该用户的角色勾选该权限标识就说明该用户是超级用户  能查看所有报表的集团大区省市等所有数据	
		}else {
			if(loginUser.isProvinceqx()){
				if(!"city".equals(boReportWarning.getShowType()) & !"branch".equals(boReportWarning.getShowType()) & !"customer".equals(boReportWarning.getShowType())
					& !"custallnumber".equals(boReportWarning.getShowType()) & !"custbignumber".equals(boReportWarning.getShowType()) & !"custpronumber".equals(boReportWarning.getShowType()) 
					& !"custcitnumber".equals(boReportWarning.getShowType())  & !"custbranumber".equals(boReportWarning.getShowType())){
					
						boReportWarning.setProvinceIdsqx(loginUser.getProvinceIds());
					}
				boReportWarning.setTmp_field("S");
			}else if(loginUser.isBigareaqx()){
				boReportWarning.setRegionIds(loginUser.getBigareaNames());
				boReportWarning.setTmp_field("D");
			}else {
				/**思路: 如果是网点权限   
				 * 如果按老系统的方式   直接查询该网点的详细数据是不合适的   即setShowType("customer")直接进入doWithCustPronumber()方法 
				 * 因为后端查询到的字段数据是reportWarningBranch.js  却进入了reportWarning.js(主页面)前端页面来展示数据    显然数据是对应不上的
				 * 
				 * 所以如果是网点权限登录系统  还是需要先查询该网点的统计数据   传给reportWarning.js
				 * 即ShowType("customer")的doWithCustomer()方法   把这个网点的统计数据查询出来作为主页面展示
				 * 就是把公司表作为主页面  之后再下钻
				 * 
				 * ydserver.gs表中，
				 * yjdw一级网点(可理解为本部)  bm一级网点/分部/服务部的编码  mc一级网点/分部/服务部的公司名称
				 * 网点 lb是2   分部 lb是21   服务部 lb是22
				 * 服务部 是挂靠在一级网点名下，数据和一级网点及所属一级网点的分部没有关系
				 * 飞鸟系统中的网点权限:一级网点 一级网点的所属分部权限
				 * 即可以这样理解:用户信息中的网点编码是一级网点 他能看到的数据包括一级网点/分部/服务部的数据
				 * 
				 */
				if(	loginUser.getOrgCode() != null & ! loginUser.getOrgCode().isEmpty()){
					 boReportWarning.setBranchCode(Integer.parseInt(loginUser.getOrgCode()));
					 boReportWarning.setTmp_field("W");//先展示该网点的统计数据
					 //boReportWarning.setShowType("customer");
				}
			 }
		}	
		
//============================================================================		
		/**3  数据展示部分
		 * 
		 */
		
		//判断是查询省份  城市 客户  等的数据  具体是哪种就调用哪个方法  
		if("city".equals(boReportWarning.getShowType())) {
			totalData=doWithCity(boReportWarning,startDate, endDate); 
			
		} else if ("branch".equals(boReportWarning.getShowType())) {
			totalData=doWithBranch(boReportWarning,startDate, endDate);
			
		} else if("customer".equals(boReportWarning.getShowType())){
			totalData=doWithCustomer(boReportWarning,startDate, endDate);
			
		} else if("custallnumber".equals(boReportWarning.getShowType())){
			totalData= doWithCustPronumber(boReportWarning,startDate, endDate,"custallnumber");
			
		} else if("custbignumber".equals(boReportWarning.getShowType())){
			doWithCustPronumber(boReportWarning,startDate, endDate, "custbignumber");
			
		} else if("custpronumber".equals(boReportWarning.getShowType())){
			totalData= doWithCustPronumber(boReportWarning,startDate, endDate, "custpronumber");
			
		} else if("custcitnumber".equals(boReportWarning.getShowType())){
			totalData=doWithCustPronumber(boReportWarning,startDate, endDate, "custcitnumber");
			
		} else if("custbranumber".equals(boReportWarning.getShowType())){
			totalData=doWithCustPronumber(boReportWarning,startDate, endDate, "custbranumber");
			
		}else {
			totalData=doWithProvince(boReportWarning,startDate, endDate,loginUser);
		}

		for(ReportWarningDO allData:totalData){
			allData.setStartDate(startDate);
			allData.setEndDate(endDate);
		}
		
		return totalData;
	}
	
	private List<ReportWarningDO> doWithCustPronumber(Bo_ReportWarning boReportWarning, String startDate,
			String endDate, String type) {
		
		List<ReportWarningDO> allDatas = new ArrayList<ReportWarningDO>();
			//预警表以上个月的平均做为比对的基础数据   改为周
		   //Date _startDate = com.yunda.base.common.utils.DateUtils.parseDate(startDate, "yyyy-MM-dd");
		   //String search_month = DateUtils.format(_startDate, "yyyyMM");
		 
		Map<String,Object> custMap = new HashMap<String, Object>();
		  custMap.put("startDate", startDate);
		  custMap.put("endDate", endDate);
		  custMap.put("price_level",boReportWarning.getPriceLevel());//map.get("priceLevel"));// boReportWarning.getNumber_level()
		  //custMap.put("TBdType",boReportWarning.getBd_type());
		  custMap.put("type", type);
		  custMap.put("ProvinceId",boReportWarning.getProvinceId());//map.get("provinceId"));
		  custMap.put("CityId",boReportWarning.getCityId());//map.get("cityId"));
		  /**说明:如果是网点权限  该用户的网点编码是一级网点的编码
		   * 非网点权限账号城市表下钻到公司表  参数是一级网点的编码
		   * 2种情况不同时出现   参数意义不同  值相同  这里用同一个字段接收参数 */  
		  custMap.put("BranchCode",boReportWarning.getBranchCode());//boReportWarning.getBranch_code()
		  
		  custMap.put("RegionId",boReportWarning.getBigarea());//map.get("bigarea"));
		  //custMap.put("search_month",search_month);
		  custMap.put("search_week", boReportWarning.getSearch_week());
		  custMap.put("offset",boReportWarning.getOffset());//map.get("offset"));
		  custMap.put("limit", boReportWarning.getLimit());//map.get("limit"));
		//缓存
		CachePrefixConformity cache = new CachePrefixConformity();
		ValueOperations<String, List<ReportWarningDO>> operations = redisTemplate.opsForValue();
		//客户名称 
		ValueOperations<String, Map<String, Object>> operationsCqkh = redisTemplate.opsForValue();
				
		//缓存key 
		String mapString  = JSON.toJSONString(custMap);
		String prefix = MD5Utils.encrypt(mapString);
		  //客户
		List<ReportWarningDO> custDatas = operations.get(cache.getSeed(Constant.YJCUSTPRONUMBERCUST+prefix,SuckCacheKeyPerfixEnum.yujin.getCode()));
		if(custDatas==null||custDatas.size()<1){
			//查库List<ReportWarningDO> 
			custDatas = reportWarningDao.doWithCustPronumbercust(custMap);
			//再放入缓存
			operations.set(cache.getSeed(Constant.YJCUSTPRONUMBERCUST+prefix,SuckCacheKeyPerfixEnum.yujin.getCode()), custDatas,86400, TimeUnit.SECONDS);

		}
		for (ReportWarningDO custData : custDatas) {
			/*老项目
			 * Map<String, Object> customerInfo = cqkhCustomerCacheService.getCustomerInfo(custData.getCustomerId());
			if(customerInfo != null && customerInfo.get("khmc") != null) {
				custData.setCustomerName(customerInfo.get("khmc").toString());
			}*/
			//客户名称
			if(custData.getCustomerId() != null && !custData.getCustomerId().isEmpty()){
				//客户名称
				Map<String, Object> customerInfo = operationsCqkh.get(custData.getCustomerId());
				if(customerInfo != null && customerInfo.get("khmc") != null) {
					custData.setCustomerName(customerInfo.get("khmc").toString());
					//custData.setMobile(customerInfo.get("mobile").toString());
					//custData.setGsaddr(customerInfo.get("gsaddr").toString());//客户公司地址
				}
				if(customerInfo != null && customerInfo.get("mobile") != null && customerInfo.get("gsaddr") != null) {
					custData.setMobile(customerInfo.get("mobile").toString());
					custData.setGsaddr(customerInfo.get("gsaddr").toString());//客户公司地址
				}
				//商家id和店铺名称
				if("1".equals(custData.getCustomerSourceType())){
					String customerId = custData.getCustomerId()+"cn";
					Map<String, Object> sellerCNInfo = operationsCqkh.get(customerId);
					if(sellerCNInfo != null && sellerCNInfo.get("seller_id") != null && sellerCNInfo.get("seller_name") != null) {
						custData.setSellerId(sellerCNInfo.get("seller_id").toString());
						custData.setSellerName(sellerCNInfo.get("seller_name").toString());
					}
				}
				if("4".equals(custData.getCustomerSourceType())){
					String customerId = custData.getCustomerId()+"jd";
					Map<String, Object> sellerJDInfo = operationsCqkh.get(customerId);
					if(sellerJDInfo != null && sellerJDInfo.get("vendor_code") != null && sellerJDInfo.get("vendor_name") != null) {
						custData.setSellerId(sellerJDInfo.get("vendor_code").toString());
						custData.setSellerName(sellerJDInfo.get("vendor_name").toString());
					}
				}
			}
			custData.setYjmc(custData.getYjmc()+"("+custData.getYjbm()+")");
			custData.setBranchName(custData.getBranchName()+"("+custData.getBranchCode()+")");
            
			BigDecimal bg = new BigDecimal(custData.getOrderAvg()); 
			custData.setOrderAvg(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			
			BigDecimal loa = new BigDecimal(custData.getLastOrderAvg()); 
			custData.setLastOrderAvg(loa.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			
			double a=custData.getOrderAvg();//上月日均件量
			double b=custData.getLastOrderAvg();//日均件量 
			//DecimalFormat df = new DecimalFormat("#.00");//格式化小数  
			//String num = df.format(b/a);//返回的是String类型 
			String num = Math.round(b / a * 10000) / 100.00 + "%";
			custData.setMonthRatio(num);
			
			allDatas.add(custData);//.toReckonList()
			
		}
		
		return allDatas;
	}

	private List<ReportWarningDO> doWithCustomer(
			Bo_ReportWarning boReportWarning, String startDate,
			String endDate) {
		
		List<ReportWarningDO> allDatas = new ArrayList<ReportWarningDO>();
		
		Map<String,Object> custMap = new HashMap<String, Object>();
		  custMap.put("startDate", startDate);
		  custMap.put("endDate", endDate);
		  custMap.put("BranchCode",boReportWarning.getBranchCode());//map.get("branchCode"));
		  
		  //custMap.put("search_month",search_month);
		  custMap.put("search_week", boReportWarning.getSearch_week());
		  custMap.put("offset",boReportWarning.getOffset());//map.get("offset"));
		  custMap.put("limit", boReportWarning.getLimit());//map.get("limit"));
		 //缓存 
		CachePrefixConformity cache = new CachePrefixConformity();
		ValueOperations<String, List<ReportWarningDO>> operations = redisTemplate.opsForValue();
		//客户名称 
		ValueOperations<String, Map<String, Object>> operationsCqkh = redisTemplate.opsForValue();
				
		String mapString  = JSON.toJSONString(custMap);
		String prefix = MD5Utils.encrypt(mapString);
		//客户
		List<ReportWarningDO> custDatas = operations.get(cache.getSeed(Constant.YJCUSTOMERCUST+prefix,SuckCacheKeyPerfixEnum.yujin.getCode()));
		if(custDatas==null||custDatas.size()<1){
			//查库List<ReportWarningDO> 
			custDatas = reportWarningDao.doWithCustomercust(custMap);
			//再放入缓存
			operations.set(cache.getSeed(Constant.YJCUSTOMERCUST+prefix,SuckCacheKeyPerfixEnum.yujin.getCode()), custDatas,86400, TimeUnit.SECONDS);			
		}
		for(ReportWarningDO custData : custDatas){
			
			/*Map<String, Object> customerInfo = cqkhCustomerCacheService.getCustomerInfo(custData.getCustomerId());
			if(customerInfo != null && customerInfo.get("khmc") != null) {
				custData.setCustomerName(customerInfo.get("khmc").toString());
			}*/
			
			if(custData.getCustomerId() != null && !custData.getCustomerId().isEmpty()){
				//客户名称
				Map<String, Object> customerInfo = operationsCqkh.get(custData.getCustomerId());
				if(customerInfo != null && customerInfo.get("khmc") != null) {
					custData.setCustomerName(customerInfo.get("khmc").toString());
					//custData.setMobile(customerInfo.get("mobile").toString());
					//custData.setGsaddr(customerInfo.get("gsaddr").toString());//客户公司地址
				}
				if(customerInfo != null && customerInfo.get("mobile") != null && customerInfo.get("gsaddr") != null) {
					custData.setMobile(customerInfo.get("mobile").toString());
					custData.setGsaddr(customerInfo.get("gsaddr").toString());//客户公司地址
				}
				//商家id和店铺名称
				if("1".equals(custData.getCustomerSourceType())){
					String customerId = custData.getCustomerId()+"cn";
					Map<String, Object> sellerCNInfo = operationsCqkh.get(customerId);
					if(sellerCNInfo != null && sellerCNInfo.get("seller_id") != null&& sellerCNInfo.get("seller_name") != null) {
						custData.setSellerId(sellerCNInfo.get("seller_id").toString());
						custData.setSellerName(sellerCNInfo.get("seller_name").toString());
					}
				}
				if("4".equals(custData.getCustomerSourceType())){
					String customerId = custData.getCustomerId()+"jd";
					Map<String, Object> sellerJDInfo = operationsCqkh.get(customerId);
					if(sellerJDInfo != null && sellerJDInfo.get("vendor_code") != null&& sellerJDInfo.get("vendor_name") != null) {
						custData.setSellerId(sellerJDInfo.get("vendor_code").toString());
						custData.setSellerName(sellerJDInfo.get("vendor_name").toString());
					}
				}
			}
			
			custData.setYjmc(custData.getYjmc()+"("+custData.getYjbm()+")");
			custData.setBranchName(custData.getBranchName()+"("+custData.getBranchCode()+")");
            
			BigDecimal bg = new BigDecimal(custData.getOrderAvg()); 
			custData.setOrderAvg(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			
			BigDecimal loa = new BigDecimal(custData.getLastOrderAvg()); 
			custData.setLastOrderAvg(loa.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			
			double a=custData.getOrderAvg();//上月件量
			double b=custData.getLastOrderAvg();//日均件量 
			//DecimalFormat df = new DecimalFormat("#.00");//格式化小数  
			//String num = df.format(b/a);//返回的是String类型 
			String num = Math.round(b / a * 10000) / 100.00 + "%";
			custData.setMonthRatio(num);
			
			allDatas.add(custData);//.toReckonList()
		}
		return allDatas;
	}

	private List<ReportWarningDO> doWithBranch(
			Bo_ReportWarning boReportWarning, String startDate, String endDate) {
		
		List<ReportWarningDO> allDatas = new ArrayList<ReportWarningDO>();
		
		Map<String,Object> custMap = new HashMap<String, Object>();
		  custMap.put("startDate", startDate);
		  custMap.put("endDate", endDate);
		  custMap.put("CityId",boReportWarning.getCityId());//map.get("cityId"));//boReportWarning.getCity_id()
		  //custMap.put("search_month",search_month);
		  custMap.put("search_week", boReportWarning.getSearch_week());
		//缓存  
		CachePrefixConformity cache = new CachePrefixConformity();
		ValueOperations<String, List<ReportWarningDO>> operations = redisTemplate.opsForValue();
		String mapString  = JSON.toJSONString(custMap);
		String prefix = MD5Utils.encrypt(mapString);
		
		//城市合计
		List<ReportWarningDO> shiDatas = operations.get(cache.getSeed(Constant.YJBRANCHSHI+prefix,SuckCacheKeyPerfixEnum.yujin.getCode()));
		if(shiDatas==null||shiDatas.size()<1){
			//查库  //List<ReportWarningDO> 
			shiDatas = reportWarningDao.doWithBranchshi(custMap);
			//再放入缓存
			operations.set(cache.getSeed(Constant.YJBRANCHSHI+prefix,SuckCacheKeyPerfixEnum.yujin.getCode()), shiDatas,86400, TimeUnit.SECONDS);

		}
		shiDatas.get(0).setCustomerName(shiDatas.get(0).getCityName()+"合计");
		allDatas.add(shiDatas.get(0));
		
		//网点公司
		List<ReportWarningDO> gsDatas = operations.get(cache.getSeed(Constant.YJBRANCHGS+prefix,SuckCacheKeyPerfixEnum.yujin.getCode()));
		if(gsDatas==null||gsDatas.size()<1){
			//List<ReportWarningDO> 
			gsDatas = reportWarningDao.doWithBranchgs(custMap);
			//再放入缓存
			operations.set(cache.getSeed(Constant.YJBRANCHGS+prefix,SuckCacheKeyPerfixEnum.yujin.getCode()), gsDatas,86400, TimeUnit.SECONDS);
		}
		// 循环市数据   
		for (ReportWarningDO branchData : gsDatas) {
			branchData.setCustomerName(branchData.getBranchName()+"("+branchData.getBranchCode()+")");
			allDatas.add(branchData.toReckonList());
		}
		
		return allDatas;	
	}

	private List<ReportWarningDO> doWithCity(
			Bo_ReportWarning boReportWarning, String startDate, String endDate) {
		
		List<ReportWarningDO> allDatas = new ArrayList<ReportWarningDO>();
		
		Map<String,Object> custMap = new HashMap<String, Object>();
		  custMap.put("startDate", startDate);
		  custMap.put("endDate", endDate);
		  custMap.put("ProvinceId",boReportWarning.getProvinceId());//map.get("provinceId"));//boReportWarning.getProvinceId()
		  //custMap.put("search_month",search_month);
		  custMap.put("search_week", boReportWarning.getSearch_week());
		//缓存
		CachePrefixConformity cache = new CachePrefixConformity();
		ValueOperations<String, List<ReportWarningDO>> operations = redisTemplate.opsForValue();
		String mapString  = JSON.toJSONString(custMap);
		String prefix = MD5Utils.encrypt(mapString);  //把map参数封装成md5签名
		
		//计算省数据
		List<ReportWarningDO> shengDatas = operations.get(cache.getSeed(Constant.YJDOCITYPROVINCE+prefix,SuckCacheKeyPerfixEnum.yujin.getCode()));
		if(shengDatas==null||shengDatas.size()<1){
			//List<ReportWarningDO> 
			shengDatas = reportWarningDao.doWithCityprovince(custMap);
			//再放入缓存
			operations.set(cache.getSeed(Constant.YJDOCITYPROVINCE+prefix,SuckCacheKeyPerfixEnum.yujin.getCode()), shengDatas,86400, TimeUnit.SECONDS);
		
		}
		shengDatas.get(0).setCustomerName(shengDatas.get(0).getProvinceName()+"合计");
		allDatas.add(shengDatas.get(0));
		
		//计算市数据
		//查缓存
		List<ReportWarningDO> shiDatas = operations.get(cache.getSeed(Constant.YJDOCITYCITY+prefix,SuckCacheKeyPerfixEnum.yujin.getCode()));
		if(shiDatas==null||shiDatas.size()<1){
			//查库  List<ReportWarningDO> 
			shiDatas = reportWarningDao.doWithCitycity(custMap);
			//再放入缓存
			operations.set(cache.getSeed(Constant.YJDOCITYCITY+prefix,SuckCacheKeyPerfixEnum.yujin.getCode()), shiDatas,86400, TimeUnit.SECONDS);
		}
		
		// 循环市数据   
		for (ReportWarningDO cityData : shiDatas) {
			
			cityData.setBigarea(cityData.getBigarea());
			cityData.setProvinceId(cityData.getProvinceId());
			cityData.setCityId(cityData.getCityId());
			cityData.setCustomerName(cityData.getCityName());
			allDatas.add(cityData.toReckonList());
			
		}
		
		return allDatas;
	}

	//查询主页面数据
	private List<ReportWarningDO> doWithProvince( Bo_ReportWarning boReportWarning, String startDate, String endDate,UserDO loginUser) {
		
		List<ReportWarningDO> allDatas = new ArrayList<ReportWarningDO>();
		  	
		Map<String,Object> custMap = new HashMap<String, Object>();
		  custMap.put("startDate", startDate);
		  custMap.put("endDate", endDate);
		  custMap.put("type", boReportWarning.getTmp_field());
		  //省  大区权限
		  custMap.put("ProvinceId",boReportWarning.getProvinceIdsqx());
		  //custMap.put("CityId",boReportWarning.getCity_id());
		  custMap.put("RegionId",boReportWarning.getRegionIds());
		  //网店权限  一级网点
		  custMap.put("BranchCode",boReportWarning.getBranchCode());
		  //search_month是作为表tmpv2_cust_od_mouth_sum的月份标签  传给sql查询到上个月的数据   所选时间段和上个月的数据比较获得预警数据
		  //custMap.put("search_month",search_month);
		  //因业务需求   预警表月改成周
		  custMap.put("search_week", boReportWarning.getSearch_week());
		  
		//把map查询条件封装成md5签名  作为缓存key 
		CachePrefixConformity cache = new CachePrefixConformity();
		ValueOperations<String, List<ReportWarningDO>> operations = redisTemplate.opsForValue();
		String mapString  = JSON.toJSONString(custMap);
		String prefix = MD5Utils.encrypt(mapString);
		
		if("W".equals(boReportWarning.getTmp_field())){//如果是网点权限
			List<ReportWarningDO> wangdian = operations.get(cache.getSeed(Constant.QUERYYJWDINFO+prefix,SuckCacheKeyPerfixEnum.yujin.getCode()));
			if(wangdian==null||wangdian.size()<1){
				wangdian  = reportWarningDao.queryWangdian(custMap);
				//查库的数据放缓存
				operations.set(cache.getSeed(Constant.QUERYYJWDINFO+prefix,SuckCacheKeyPerfixEnum.yujin.getCode()), wangdian,86400, TimeUnit.SECONDS);
				
			}
			wangdian.get(0).setCustomerName(wangdian.get(0).getBranchName()+"("+wangdian.get(0).getBranchCode()+")网点");
			allDatas.add(wangdian.get(0).toReckonList());
		} else { //如果不是网点权限
			
		//1  计算 集团合计
		List<ReportWarningDO> allcountDatas = operations.get(cache.getSeed(Constant.QUERYYJCOMPANYINFO+prefix,SuckCacheKeyPerfixEnum.yujin.getCode()));
		//System.out.println("预警表从缓存取值：	"+allcountDatasCache);	
		if(allcountDatas==null||allcountDatas.size()<1){
			allcountDatas = reportWarningDao.queryallcount(custMap);
			//查库的数据放缓存
			operations.set(cache.getSeed(Constant.QUERYYJCOMPANYINFO+prefix,SuckCacheKeyPerfixEnum.yujin.getCode()), allcountDatas,86400, TimeUnit.SECONDS);
			//System.out.println("缓存没有，从数据库查再放入缓存：	"+allcountDatas);
		}
		/**
		 * 权限   没有超级权限  不显示集团合计    bia
		 */
		
		if("D".equals(boReportWarning.getTmp_field())){
		}else if("S".equals(boReportWarning.getTmp_field())){
		}else{
			allcountDatas.get(0).setCustomerName("集团合计");
			allDatas.add(allcountDatas.get(0).toReckonList());
		}
		
		//2  计算大区数据
		//从缓存取大区数据  缓存 key = QUERYBDBIGAREAINFO+用户id+查询时间
		List<ReportWarningDO> bigareaCountDatas = operations.get(cache.getSeed(Constant.QUERYYJBIGAREAINFO+prefix,SuckCacheKeyPerfixEnum.yujin.getCode()));
				//若没有大区数据，则从数据库查
		if(bigareaCountDatas==null||bigareaCountDatas.size()<1){
			bigareaCountDatas = reportWarningDao.querybigarea(custMap);
			//放缓存
			operations.set(cache.getSeed(Constant.QUERYYJBIGAREAINFO+prefix,SuckCacheKeyPerfixEnum.yujin.getCode()), bigareaCountDatas,86400, TimeUnit.SECONDS);
		}
		
		//循环大区   查大区里的省份数据
		for(ReportWarningDO bigareaData : bigareaCountDatas){
			if(StringUtils.isEmpty(bigareaData.getBigarea())) {
				continue;
			}
			// 大区合计
			if("S".equals(boReportWarning.getTmp_field())){}else{
				bigareaData.setCustomerName(bigareaData.getBigarea()+"合计");
				allDatas.add(bigareaData.toReckonList());
			}

			//3  计算  省数据
			//用户每个大区下面的省数据，缓存 key = queryBDProvinceInfo+用户id+大区名+查询时间                                 即+loginUser.getUserId()+bigareaData.getBigarea()
			List<ReportWarningDO> provienceData = operations.get(cache.getSeed(Constant.QUERYYJPROVINCEINFO+prefix,SuckCacheKeyPerfixEnum.yujin.getCode()));
			if(provienceData==null||provienceData.size()<1){
				//查库  //List<ReportWarningDO> 
				provienceData = reportWarningDao.queryprovince(custMap);
				//再放入缓存
				operations.set(cache.getSeed(Constant.QUERYYJPROVINCEINFO+prefix,SuckCacheKeyPerfixEnum.yujin.getCode()), provienceData,86400, TimeUnit.SECONDS);
			
			}
			
			// 循环省份
			for (ReportWarningDO shengData : provienceData) {
				if(!bigareaData.getBigarea().equals(shengData.getBigarea())) {
					continue;
				}
				// 省份合计
				shengData.setCustomerName(shengData.getProvinceName());
				shengData.setProvinceId(shengData.getProvinceId());
				shengData.setBigarea(shengData.getBigarea());
				allDatas.add(shengData.toReckonList());
			}
		}
		}
		return allDatas;
		
	}
//导出功能
	@Override
	public List<ExportReportWarningdataDO> filterData(
			List<ReportWarningDO> reportWarningData) {
		
		List<ExportReportWarningdataDO> warningData = new ArrayList<ExportReportWarningdataDO>();
		ExportReportWarningdataDO newWarning = new ExportReportWarningdataDO();
		
		for(ReportWarningDO data : reportWarningData){		
		     if( data.getCustomerName() !=null&& !"".equals(data.getCustomerName())){
		    	 if("no".equals(data.getCustomerName())||"wait".equals(data.getCustomerName())||"rqcw".equals(data.getCustomerName())||"bt".equals(data.getCustomerName())){    		  
		    	 }else{
		    		 newWarning = new ExportReportWarningdataDO();
		    		 
		    		 newWarning.setCustomerName(data.getCustomerName());
		    		 //newWarning.setaCustomerSum(data.getACustomerSum());
		    		 newWarning.setbCustomerSum(data.getBCustomerSum());
		    		 newWarning.setcCustomerSum(data.getCCustomerSum());
		    		 newWarning.setdCustomerSum(data.getDCustomerSum());
		    		 newWarning.seteCustomerSum(data.getECustomerSum());
		    		 newWarning.setfCustomerSum(data.getFCustomerSum());
		    		 newWarning.setgCustomerSum(data.getGCustomerSum());
		    		 
		    		 warningData.add(newWarning);		    		 
		    	 }
		     }	 
		}      
		return warningData;
	}
	//导出功能--预警客户表
		@Override
		public List<ExportReportWarningBranchDO> filterBranchData(
				List<ReportWarningDO> reportWarningData) {
			
			List<ExportReportWarningBranchDO> warningData = new ArrayList<ExportReportWarningBranchDO>();
			ExportReportWarningBranchDO newWarning = new ExportReportWarningBranchDO();
			
			for(ReportWarningDO data : reportWarningData){		
			     if( data.getCustomerName() !=null&& !"".equals(data.getCustomerName())){
			    	 if("no".equals(data.getCustomerName())||"wait".equals(data.getCustomerName())||"rqcw".equals(data.getCustomerName())||"bt".equals(data.getCustomerName())){    		  
			    	 }else{
			    		 newWarning = new ExportReportWarningBranchDO();
			    		 
			    		 newWarning.setBigarea(data.getBigarea());
			    		 newWarning.setProvinceName(data.getProvinceName());
			    		 newWarning.setCityName(data.getCityName());
			    		 newWarning.setYjmc(data.getYjmc());
			    		 newWarning.setBranchName(data.getBranchName());
			    		 newWarning.setCustomerId(data.getCustomerId());
			    		 newWarning.setCustomerName(data.getCustomerName());
			    		 newWarning.setSellerId(data.getSellerId());
			    		 newWarning.setSellerName(data.getSellerName());
			    		 newWarning.setMobile(data.getMobile());
			    		 newWarning.setGsaddr(data.getGsaddr());
			    		 newWarning.setShowCustomerSourceType(data.getShowCustomerSourceType());
			    		 newWarning.setLastOrderSum(data.getLastOrderSum());
			    		 newWarning.setLastOrderAvg(data.getLastOrderAvg());
			    		 newWarning.setOrderAvg(data.getOrderAvg());
			    		 newWarning.setMonthRatio(data.getMonthRatio());
			    		 newWarning.setShowPriceLevel(data.getShowPriceLevel());
			    		 
			    		 warningData.add(newWarning);		    		 
			    	 }
			     }	 
			}      
			return warningData;
		}
		
}
