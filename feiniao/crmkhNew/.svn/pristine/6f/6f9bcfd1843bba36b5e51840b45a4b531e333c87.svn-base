package com.yunda.base.feiniao.warning.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunda.base.common.config.Constant;
import com.yunda.base.feiniao.report.domain.ReportWarningDO;
import com.yunda.base.feiniao.report.utils.CachePrefixConformity;
import com.yunda.base.feiniao.warning.bo.Bo_warningHandleDO;
import com.yunda.base.feiniao.warning.dao.WarningHandleDao;
import com.yunda.base.feiniao.warning.domain.ExportWarningHandleDO;
import com.yunda.base.feiniao.warning.domain.WarningBenchmarkDO;
import com.yunda.base.feiniao.warning.domain.WarningHandleDO;
import com.yunda.base.feiniao.warning.service.WarningHandleService;
import com.yunda.base.system.domain.UserDO;



@Service
public class WarningHandleServiceImpl implements WarningHandleService {
	@Autowired
	private WarningHandleDao warningHandleDao;
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	public WarningHandleDO get(Long id){
		return warningHandleDao.get(id);
	}
	
	@Override
	public List<WarningHandleDO> list(Bo_warningHandleDO boInterface, UserDO loginUser){
		
		if(loginUser.hasPerms(Constant.REPORT_ADMIN_PERMS)) {
		   //超级用户权限   无限制
		//系统菜单配置了report:admin:allperms权限标识   角色管理中当该用户的角色勾选该权限标识就说明该用户是超级用户  能查看所有报表的集团大区省市等所有数据	
		}else {
			if(loginUser.isProvinceqx()){
				
				boInterface.setProvinceIdsqx(loginUser.getProvinceIds());
				boInterface.setTmp_field("S");
			}/*else if(loginUser.isBigareaqx()){
				boInterface.setRegionIds(loginUser.getBigareaNames());
				boInterface.setTmp_field("D");
			}*/else {
				if(	loginUser.getOrgCode() != null & ! loginUser.getOrgCode().isEmpty()){
					boInterface.setBranchCodeQX(Integer.parseInt(loginUser.getOrgCode()));
					boInterface.setTmp_field("W");//先展示该网点的统计数据
					
				}
			 }
		}
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("customerId", boInterface.getCustomerId());
		//map.put("customerName", boInterface.getCustomerName());
		map.put("branchCode", boInterface.getBranchCode());//搜索头
		map.put("priceLevel", boInterface.getPriceLevel());
		map.put("branchDealType", boInterface.getBranchDealType());
		map.put("feedbackStatus", boInterface.getFeedbackStatus());
		map.put("startDate", boInterface.getStartDate());
		map.put("endDate", boInterface.getEndDate());
		map.put("offset", boInterface.getOffset());
		map.put("limit", boInterface.getLimit());
		map.put("provinceName", boInterface.getProvinceName());
		map.put("bigarea", boInterface.getBigarea());
		//权限控制
		map.put("type", boInterface.getTmp_field());
		map.put("ProvinceId",boInterface.getProvinceIdsqx());//省权限
		map.put("BranchCodeQX",boInterface.getBranchCodeQX());//网点权限  一级网点
		
		//CachePrefixConformity cache = new CachePrefixConformity();
		//ValueOperations<String, List<WarningHandleDO>> operations = redisTemplate.opsForValue();
		
		ValueOperations<String, Map<String, Object>> operationsCqkh = redisTemplate.opsForValue();
		//查询
		List<WarningHandleDO> datas = warningHandleDao.list(map);
		
		for(WarningHandleDO data :datas){
			if(data.getCustomerId() != null && !data.getCustomerId().isEmpty()){
				//客户名称
				Map<String, Object> customerInfo = operationsCqkh.get(data.getCustomerId());
				if(customerInfo != null && customerInfo.get("khmc") != null) {
					data.setCustomerName(customerInfo.get("khmc").toString());
				}
			}
			
			BigDecimal loa = new BigDecimal(data.getLastOrderAvg());//上周日均量取2位小数 
			data.setLastOrderAvg(loa.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			
			double a=data.getOrderAvg();//日均件量
			double b=data.getLastOrderAvg();//上周日均件量 
			BigDecimal ba = new BigDecimal(b-a);//上周日均量取2位小数 
			data.setReducedOrder(ba.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());;//降幅单量
			String num = Math.round((b-a) / b * 10000) / 100.00 + "%";
			data.setReducedRatio(num);
			
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
			data.setShowWarnDate(date.format(data.getWarnDate()));
			data.setShowFeedbackDeadline(date.format(data.getFeedbackDeadline()));
			//System.out.println(data.getShowWarnDate()+"!!!!!!!!!!!!!!"+data.getShowFeedbackDeadline());
			
		}
		
		
		return datas;
	}
	
	@Override
	public int count(Bo_warningHandleDO boInterface, UserDO loginUser){
		
		if(loginUser.hasPerms(Constant.REPORT_ADMIN_PERMS)) {
			   //超级用户权限   无限制
			//系统菜单配置了report:admin:allperms权限标识   角色管理中当该用户的角色勾选该权限标识就说明该用户是超级用户  能查看所有报表的集团大区省市等所有数据	
			}else {
				if(loginUser.isProvinceqx()){
					
					boInterface.setProvinceIdsqx(loginUser.getProvinceIds());
					boInterface.setTmp_field("S");
				}/*else if(loginUser.isBigareaqx()){
					boInterface.setRegionIds(loginUser.getBigareaNames());
					boInterface.setTmp_field("D");
				}*/else {
					if(	loginUser.getOrgCode() != null & ! loginUser.getOrgCode().isEmpty()){
						boInterface.setBranchCodeQX(Integer.parseInt(loginUser.getOrgCode()));
						boInterface.setTmp_field("W");//先展示该网点的统计数据
						
					}
				 }
			}
			
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("customerId", boInterface.getCustomerId());
			//map.put("customerName", boInterface.getCustomerName());
			map.put("branchCode", boInterface.getBranchCode());//搜索头
			map.put("priceLevel", boInterface.getPriceLevel());
			map.put("branchDealType", boInterface.getBranchDealType());
			map.put("feedbackStatus", boInterface.getFeedbackStatus());
			map.put("startDate", boInterface.getStartDate());
			map.put("endDate", boInterface.getEndDate());
			map.put("offset", boInterface.getOffset());
			map.put("limit", boInterface.getLimit());
			map.put("provinceName", boInterface.getProvinceName());
			map.put("bigarea", boInterface.getBigarea());
			//权限控制
			map.put("type", boInterface.getTmp_field());
			map.put("ProvinceId",boInterface.getProvinceIdsqx());//省权限
			map.put("BranchCodeQX",boInterface.getBranchCodeQX());//网点权限  一级网点
			
		return warningHandleDao.count(map);
	}
	/**
	 * 导出整理数据
	 */
	@Override
	public List<ExportWarningHandleDO> filterData(List<WarningHandleDO> warningHandleList) {
		List<ExportWarningHandleDO> ewhDataList= new ArrayList<ExportWarningHandleDO>();
		ExportWarningHandleDO ewhData = new ExportWarningHandleDO();
		
		for(WarningHandleDO data : warningHandleList){
			if(data.getCustomerId()!= null &!"".equals(data.getCustomerId())){
				SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				ewhData = new ExportWarningHandleDO();
				//ewhData.setId(data.getId());
				ewhData.setCustomerId(data.getCustomerId());
				ewhData.setCustomerName(data.getCustomerName());
				ewhData.setShowCustomerSourceType(data.getShowCustomerSourceType());
				ewhData.setShowPriceLevel(data.getShowPriceLevel());
				
				ewhData.setShowWarnDate(df1.format(data.getWarnDate()));
				ewhData.setOrderAvg(data.getOrderAvg());
				ewhData.setLastOrderAvg(data.getLastOrderAvg());
				ewhData.setReducedRatio(data.getReducedRatio());
				ewhData.setReducedOrder(data.getReducedOrder());
				
				ewhData.setGs(data.getGs());
				ewhData.setGsmc(data.getGsmc());
				ewhData.setBranchCode(data.getBranchCode());
				ewhData.setBranchName(data.getBranchName());
				ewhData.setCityid(data.getCityid());
				ewhData.setCityname(data.getCityname());
				ewhData.setProvinceid(data.getProvinceid());
				ewhData.setProvincename(data.getProvincename());
				ewhData.setBigarea(data.getBigarea());
				
				ewhData.setShowFeedbackDeadline(df1.format(data.getFeedbackDeadline()));
				ewhData.setShowFeedbackStatus(data.getShowFeedbackStatus());
				ewhData.setRemark(data.getRemark());
				
				if(data.getBranchFeedbackDate() != null && !"".equals(data.getBranchFeedbackDate())){
				ewhData.setBarnchDealUser(data.getBarnchDealUser());
				ewhData.setShowBranchDealType(data.getShowBranchDealType());
				ewhData.setBranchDealDesc(data.getBranchDealDesc());
				ewhData.setShowBranchFeedbackDate(df2.format(data.getBranchFeedbackDate()));
				ewhData.setBranchCategory(data.getBranchCategory());
				ewhData.setBranchAveWeight(data.getBranchAveWeight());
				}
				
				if(data.getProvinceFeedbackDate() != null && !"".equals(data.getProvinceFeedbackDate())){
				ewhData.setProvinceDealUser(data.getProvinceDealUser());
				ewhData.setProvinceDealDesc(data.getProvinceDealDesc());
				ewhData.setShowProvinceFeedbackDate(df2.format(data.getProvinceFeedbackDate()));
				}
				
				if(data.getZbFeedbackDate() != null && !"".equals(data.getZbFeedbackDate())){
				ewhData.setZbDealUser(data.getZbDealUser());
				ewhData.setZbDealDesc(data.getZbDealDesc());
				ewhData.setShowZbFeedbackDate(df2.format(data.getZbFeedbackDate()));
				}
				ewhDataList.add(ewhData);
			}
		}
		return ewhDataList;
	}
	
	@Override
	public int save(WarningHandleDO warningHandle){
		return warningHandleDao.save(warningHandle);
	}
	
	@Override
	public WarningHandleDO getWDFeedback(Map<String, Object> map){
		return warningHandleDao.getWDFeedback(map);
	}
	
	@Override
	public int update(WarningHandleDO warningHandle){
		return warningHandleDao.update(warningHandle);
	}
	
	@Override
	public int remove(Long id){
		return warningHandleDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return warningHandleDao.batchRemove(ids);
	}

	
	
}
