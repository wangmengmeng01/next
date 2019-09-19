package com.yunda.base.feiniao.customer.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.yunda.base.common.config.Constant;
import com.yunda.base.feiniao.customer.bo.Bo_CustomerPotentialPersonNew;
import com.yunda.base.feiniao.customer.dao.CustomerPotentialPersonNewDao;
import com.yunda.base.feiniao.customer.domain.CustomerPotentialPersonNewDO;
import com.yunda.base.feiniao.customer.domain.ExportExcelCustomerPotentialPersonNewDO;
import com.yunda.base.feiniao.customer.service.CustomerPotentialPersonNewService;
import com.yunda.base.system.domain.UserDO;



@Service
public class CustomerPotentialPersonNewServiceImpl implements CustomerPotentialPersonNewService {
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private CustomerPotentialPersonNewDao customerPotentialPersonNewDao;
	@Autowired
	private CustomerPotentialPersonNewServiceImpl customerPotentialPersonNewServiceImpl;
	@Override
	public CustomerPotentialPersonNewDO get(Integer recordId){
		return customerPotentialPersonNewDao.get(recordId);
	}
	
	@Override
	public List<CustomerPotentialPersonNewDO> list(Bo_CustomerPotentialPersonNew boCustomerPotentialPersonNew, UserDO loginUser){
		//权限控制----------------------------------------------------------------------------------------------------------------
		if(loginUser.hasPerms(Constant.REPORT_ADMIN_PERMS)) {
			   //超级用户权限   无限制
			//系统菜单配置了report:admin:allperms权限标识   角色管理中当该用户的角色勾选该权限标识就说明该用户是超级用户  能查看所有报表的集团大区省市等所有数据	
		}else{
			if(loginUser.isProvinceqx()){//是否有省权限
				boCustomerPotentialPersonNew.setProvinceids(loginUser.getProvinceIds());
				//boCustomerPotentialPersonNew.setTmpField("S");
			}else {
				if(loginUser.getOrgCode() != null & ! loginUser.getOrgCode().isEmpty()){
					//List<Long> branchcode = new ArrayList<Long>();
					//branchcode = customerPotentialPersonNewDao.ydserverGSSJDW(loginUser.getOrgCode());
					boCustomerPotentialPersonNew.setBranchCodeQX(loginUser.getOrgCode());
				}
			}	
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		//权限
		map.put("branchCodeQX",boCustomerPotentialPersonNew.getBranchCodeQX());//网点权限
		map.put("provinceids", boCustomerPotentialPersonNew.getProvinceids());//省权限
		//搜索头查询
		map.put("customerName", boCustomerPotentialPersonNew.getCustomerName());
		map.put("shopName", boCustomerPotentialPersonNew.getShopName());
		//map.put("provinceId", boCustomerPotentialPersonNew.getProvinceId());
		map.put("provinceName", boCustomerPotentialPersonNew.getProvinceName());
		map.put("bigarea", boCustomerPotentialPersonNew.getBigarea());
		map.put("handlerName", boCustomerPotentialPersonNew.getHandlerName());
		//map.put("handlerId", boCustomerPotentialPersonNew.getHandlerId());
		//map.put("updateTime", boCustomerPotentialPersonNew.getUpdateTime());
		map.put("startupdateTime", boCustomerPotentialPersonNew.getStartupdateTime());
		map.put("endupdateTime", boCustomerPotentialPersonNew.getEndupdateTime());
		
		if(boCustomerPotentialPersonNew.getStartDailyOrderAvg() != null & ! boCustomerPotentialPersonNew.getStartDailyOrderAvg().isEmpty()){
			double startDailyOrderAvg = Double.parseDouble(boCustomerPotentialPersonNew.getStartDailyOrderAvg());
			double endDailyOrderAvg = Double.parseDouble(boCustomerPotentialPersonNew.getEndDailyOrderAvg());
			map.put("startDailyOrderAvg", startDailyOrderAvg);
			map.put("endDailyOrderAvg", endDailyOrderAvg);
		}
		map.put("offset", boCustomerPotentialPersonNew.getOffset());
		map.put("limit", boCustomerPotentialPersonNew.getLimit());
		
		//缓存
		/*CachePrefixConformity cache = new CachePrefixConformity();
		ValueOperations<String, List<CustomerPotentialPersonNewDO>> operations = redisTemplate.opsForValue();
				
		String khJLMapStrin  = JSON.toJSONString(map);
	    String khJLMapprefix = MD5Utils.encrypt(khJLMapStrin);
		List<CustomerPotentialPersonNewDO> allData = operations.get(cache.getSeed(Constant.POTENTIALNEW+khJLMapprefix,SuckCacheKeyPerfixEnum.potentialNew.getCode()));
		
	    if(allData == null || allData.size() < 1){
	    	allData = customerPotentialPersonNewDao.list(map);
	    	operations.set(cache.getSeed(Constant.POTENTIALNEW+khJLMapprefix,SuckCacheKeyPerfixEnum.potentialNew.getCode()),allData,86400, TimeUnit.SECONDS);
	    }*/
		List<CustomerPotentialPersonNewDO>  allData = customerPotentialPersonNewDao.list(map);
    	for(CustomerPotentialPersonNewDO data: allData){
    		if(data.getUpdateTime() !=null && !"".equals(data.getUpdateTime())){
    			if(data.getUpdateTime().contains(".")){
    				String updatetime = data.getUpdateTime().substring(0,19);
			         data.setUpdateTime(updatetime);
    			}
    		}
    	}
    	
		return allData;
	}
	
	@Override
	public int count(Bo_CustomerPotentialPersonNew boCustomerPotentialPersonNew, UserDO loginUser){
		//权限控制----------------------------------------------------------------------------------------------------------------
		if(loginUser.hasPerms(Constant.REPORT_ADMIN_PERMS)) {
			   //超级用户权限   无限制
			//系统菜单配置了report:admin:allperms权限标识   角色管理中当该用户的角色勾选该权限标识就说明该用户是超级用户  能查看所有报表的集团大区省市等所有数据	
		}else{
			if(loginUser.isProvinceqx()){//是否有省权限
				boCustomerPotentialPersonNew.setProvinceids(loginUser.getProvinceIds());
				//boCustomerPotentialPersonNew.setTmpField("S");
			}else {
				if(loginUser.getOrgCode() != null & ! loginUser.getOrgCode().isEmpty()){
					//List<Long> branchcode = new ArrayList<Long>();
					//branchcode = customerPotentialPersonNewDao.ydserverGSSJDW(loginUser.getOrgCode());
					boCustomerPotentialPersonNew.setBranchCodeQX(loginUser.getOrgCode());
				}
			}	
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("branchCodeQX",boCustomerPotentialPersonNew.getBranchCodeQX());//网点权限
		map.put("provinceids", boCustomerPotentialPersonNew.getProvinceids());//省权限
		//搜索头查询
		map.put("customerName", boCustomerPotentialPersonNew.getCustomerName());
		map.put("shopName", boCustomerPotentialPersonNew.getShopName());
		//map.put("provinceId", boCustomerPotentialPersonNew.getProvinceId());
		map.put("provinceName", boCustomerPotentialPersonNew.getProvinceName());
		map.put("bigarea", boCustomerPotentialPersonNew.getBigarea());
		map.put("handlerName", boCustomerPotentialPersonNew.getHandlerName());
		//map.put("handlerId", boCustomerPotentialPersonNew.getHandlerId());
		//map.put("updateTime", boCustomerPotentialPersonNew.getUpdateTime());
		map.put("startupdateTime", boCustomerPotentialPersonNew.getStartupdateTime());
		map.put("endupdateTime", boCustomerPotentialPersonNew.getEndupdateTime());
		
		if(boCustomerPotentialPersonNew.getStartDailyOrderAvg() != null & ! boCustomerPotentialPersonNew.getStartDailyOrderAvg().isEmpty()){
			double startDailyOrderAvg = Double.parseDouble(boCustomerPotentialPersonNew.getStartDailyOrderAvg());
			double endDailyOrderAvg = Double.parseDouble(boCustomerPotentialPersonNew.getEndDailyOrderAvg());
			map.put("startDailyOrderAvg", startDailyOrderAvg);
			map.put("endDailyOrderAvg", endDailyOrderAvg);
		}
		return customerPotentialPersonNewDao.count(map);
	}
	
	@Override
	public int countwd(String branchcode){
		return customerPotentialPersonNewDao.countwd(branchcode);
	}
	
	@Override
	public int save(CustomerPotentialPersonNewDO potentialNew,UserDO loginUser){
		String branchcode = potentialNew.getBranchCode();
		//用网点查网点名称  市  省  大区等信息
		List<CustomerPotentialPersonNewDO> cp= customerPotentialPersonNewDao.searchYdserver(branchcode);
			
			potentialNew.setBranchName(cp.get(0).getBranchName());
			potentialNew.setCityName(cp.get(0).getCityName());
			potentialNew.setProvinceId(cp.get(0).getProvinceId());
			potentialNew.setProvinceName(cp.get(0).getProvinceName());
			potentialNew.setBigarea(cp.get(0).getBigarea());
			potentialNew.setSjwd(cp.get(0).getSjwd());//上级网点编码   作为网点权限
			//用户信息
			potentialNew.setHandlerId(loginUser.getUsername());//用户id
			potentialNew.setHandlerName(loginUser.getName());//用户姓名
			//维护时间
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = df.format(new Date());
			/*java.util.Date time=null; 改成sql里date_format (#{updateTime},'%Y-%m-%d')
			try {
				time= df.parse(df.format(new Date()));
			} catch (Exception e) {
				
			}*/
			potentialNew.setUpdateTime(time);
		
		return customerPotentialPersonNewDao.save(potentialNew);
	}
	
	@Override
	public int update(CustomerPotentialPersonNewDO potentialNew,UserDO loginUser){
		String branchcode = potentialNew.getBranchCode();
		//用网点查网点名称  市  省  大区等信息
		List<CustomerPotentialPersonNewDO> cp= customerPotentialPersonNewDao.searchYdserver(branchcode);
			
			potentialNew.setBranchName(cp.get(0).getBranchName());
			potentialNew.setCityName(cp.get(0).getCityName());
			potentialNew.setProvinceId(cp.get(0).getProvinceId());
			potentialNew.setProvinceName(cp.get(0).getProvinceName());
			potentialNew.setBigarea(cp.get(0).getBigarea());
			potentialNew.setSjwd(cp.get(0).getSjwd());//上级网点编码   作为网点权限
			//用户信息
			potentialNew.setHandlerId(loginUser.getUsername());//用户id
			potentialNew.setHandlerName(loginUser.getName());//用户姓名
			//维护时间
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = df.format(new Date());
			potentialNew.setUpdateTime(time);
			
		return customerPotentialPersonNewDao.update(potentialNew);
	}
	
	@Override
	public int remove(Integer recordId){
		return customerPotentialPersonNewDao.remove(recordId);
	}
	
	@Override
	public int batchRemove(Integer[] recordIds){
		return customerPotentialPersonNewDao.batchRemove(recordIds);
	}

	@Override
	public boolean checkData(
			CustomerPotentialPersonNewDO row, UserDO loginUser) {
		CustomerPotentialPersonNewDO vo = new CustomerPotentialPersonNewDO();
		//数据校验
		if(row.getCustomerName()==null || "".equals(row.getCustomerName())){
			return false;
		}
		/*if(row.getDailyOrderAvg() !=null && !"".equals(row.getDailyOrderAvg())){
			Integer.parseInt(row.getDailyOrderAvg());//String字符类型数据转换为Integer类型,遇到不能转换会抛出异常。
        	vo.setDailyOrderAvg(Double.valueOf(row.getDailyOrderAvg()));
			//vo.setSendNumber(row.getSendNumber().toString());
		}*/
		if(row.getBulkyCargo() !=null && !"".equals(row.getBulkyCargo())){
			if(!"是".equals(row.getBulkyCargo()) && !"否".equals(row.getBulkyCargo())){
				return false;
			}
		}
		if(row.getBranchCode() !=null && !"".equals(row.getBranchCode())){
			int wd = customerPotentialPersonNewServiceImpl.countwd(row.getBranchCode());
            return wd != 0;
		}
		return true;
	}
//导出整理数据
	@Override
	public List<ExportExcelCustomerPotentialPersonNewDO> filterData(
			List<CustomerPotentialPersonNewDO> potentialNewlist) {
		List<ExportExcelCustomerPotentialPersonNewDO> exportExcelList = new ArrayList<ExportExcelCustomerPotentialPersonNewDO>();
		ExportExcelCustomerPotentialPersonNewDO exportExcel;// =new ExportExcelCustomerPotentialPersonNewDO();
		for(CustomerPotentialPersonNewDO data : potentialNewlist){
			exportExcel =new ExportExcelCustomerPotentialPersonNewDO();
			//整理数据
			exportExcel.setCustomerName(data.getCustomerName());
			exportExcel.setSendNumber(data.getSendNumber());
			exportExcel.setShopName(data.getShopName());
			exportExcel.setProduct(data.getProduct());
			exportExcel.setBulkyCargo(data.getBulkyCargo());
			exportExcel.setWeight(data.getWeight());
			exportExcel.setDailyOrderAvg(data.getDailyOrderAvg()+"");
			exportExcel.setExpressCompany(data.getExpressCompany());
			exportExcel.setUnitPrice(data.getUnitPrice());
			exportExcel.setSendAddress(data.getSendAddress());
			exportExcel.setBranchCode(data.getBranchCode());
			exportExcel.setBranchName(data.getBranchName());
			exportExcel.setCityName(data.getCityName());
			exportExcel.setProvinceName(data.getProvinceName());
			exportExcel.setBigarea(data.getBigarea());
			exportExcel.setHandlerName(data.getHandlerName());
			exportExcel.setUpdateTime(data.getUpdateTime());
			
			//数据存入list
			exportExcelList.add(exportExcel);
		}
		return exportExcelList;
	}

	@Override
	public List<Map<String, Object>> searchCustomerName() {
		// TODO Auto-generated method stub
		return customerPotentialPersonNewDao.searchCustomerName();
	}


	
	
}
