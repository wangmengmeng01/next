package com.yunda.base.feiniao.costreport.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.yunda.base.feiniao.costreport.bo.BoBasicCitys;
import com.yunda.base.feiniao.costreport.dao.CostreportOrderCostChangeDao;
import com.yunda.base.feiniao.costreport.domain.CostreportOrderCostChangeDO;
import com.yunda.base.feiniao.costreport.enums.SoaOrgType;
import com.yunda.base.feiniao.costreport.service.CostreportOrderCostChangeService;
import com.yunda.base.system.domain.UserDO;



@Service
public class CostreportOrderCostChangeServiceImpl implements CostreportOrderCostChangeService {
	@Autowired
	private CostreportOrderCostChangeDao costreportOrderCostChangeDao;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	protected static final Logger logger = LoggerFactory.getLogger(CostreportOrderCostChangeServiceImpl.class);

	@Override
	public CostreportOrderCostChangeDO get(Integer recordId){
		return costreportOrderCostChangeDao.get(recordId);
	}
	
	@Override
	public List<CostreportOrderCostChangeDO> list(Map<String, Object> map){
		return costreportOrderCostChangeDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return costreportOrderCostChangeDao.count(map);
	}
	
	@Override
	public int save(CostreportOrderCostChangeDO costreportOrderCostChange){
		return costreportOrderCostChangeDao.save(costreportOrderCostChange);
	}
	
	@Override
	public int update(CostreportOrderCostChangeDO costreportOrderCostChange){
		return costreportOrderCostChangeDao.update(costreportOrderCostChange);
	}
	
	@Override
	public int remove(Integer recordId){
		return costreportOrderCostChangeDao.remove(recordId);
	}
	
	@Override
	public int batchRemove(Integer[] recordIds){
		return costreportOrderCostChangeDao.batchRemove(recordIds);
	}

	@Override
	public Double calculateData(CostreportOrderCostChangeDO vo, UserDO user) {
//		user
		String orgType = user.getOrgType();
  		String gs = null;
        int center = 0;
		if(StringUtils.isNotBlank(orgType)&&(SoaOrgType.HEAD_OFFICE.getCode()==Integer.parseInt(orgType)||
				SoaOrgType.DISTRIBUTION_CENTER.getCode()==Integer.parseInt(orgType)||SoaOrgType.PROVINCIAL_CENTER.getCode()==Integer.parseInt(orgType))){			
			try {
				
				gs = costreportOrderCostChangeDao.getYdserverOrgCodeByUserId(user.getUserId()+"");
			} catch (Exception e) {
			}
			
			} else if (StringUtils.isNotBlank(orgType)&&(SoaOrgType.BRANCH.getCode()==Integer.parseInt(orgType))) {
  				gs = user.getOrgCode();
	  		}
		
		
		String[] customerBranchOrg;
		try {
			customerBranchOrg = queryOrgInfo(gs + "", "21"+ "");
			
			int sjgs =	StringUtils.isBlank(customerBranchOrg[0]) == true ? 0 : Integer.valueOf(customerBranchOrg[0]);

    		// 获取网点类型branchType
    		String branchType = getYdserverOrgTypeByOrgCode(sjgs+"");
    		
    		// 获取所属机构的上级机构
    		String[] customerBranchOrg1 = queryOrgInfo(sjgs + "", branchType+ "");
    		// 添加所属网点，所属分拨中心
    		if(customerBranchOrg1!=null){
    	//	co.setBelong_branch(StringUtils.isBlank(customerBranchOrg1[0]) == true ? 0 : Integer.valueOf(customerBranchOrg1[0]));
    		center = StringUtils.isBlank(customerBranchOrg1[1]) == true ? 0 : Integer.valueOf(customerBranchOrg1[1]);
    		}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}  		
		
		if(gs != null)
            vo.setBranchCode(Integer.parseInt(gs));
			String[] destinationName =	vo.getDestinationProvinceId()!=null? vo.getDestinationProvinceId().split(","):null;
			String[] calculate_sum =	vo.getCalculateSum()!=null? vo.getCalculateSum().split(","):null;
            Double calculate = 0.00;
		    if(destinationName !=null){
		    	for(int i=0 ;i<destinationName.length;i++){
		    		vo.setDestinationProvinceId(destinationName[i]);
		    		vo.setCalculateSum(calculate_sum[i]);
		  
		    		
			        Map<String, String> cityList =new HashMap<String, String>();
			        Map<String, String> cityListDestination=new HashMap<String, String>();

			    	BoBasicCitys proxy = new BoBasicCitys();
		    		proxy.setParent(destinationName[i]);
    				try {
						cityList=getCitysByProvinceExcel(proxy);
			    		proxy.setParent(vo.getProvenanceProvinceId());
			    		cityListDestination=getCitysByProvinceExcel(proxy);

					} catch (Exception e) {
						// TODO Auto-generated catch block
//						e.printStackTrace();
						logger.error(e.getMessage(), e);
					}
		    		
		    		
				    //查询中转费
				    List<Map<String, Object>> resultZZF = zzfData(vo,center,cityListDestination,cityList);
		    		
		    		//查询面单费
		    		List<Map<String, Object>> result =wuliaoData(vo);

		    		if(resultZZF.size()>0){	
	                    Double first_wgt = resultZZF.get(0).get("szzl")!=null ?Double.parseDouble(resultZZF.get(0).get("szzl").toString()):0.00;
	                    Double first_prc = resultZZF.get(0).get("szjg")!=null ?Double.parseDouble(resultZZF.get(0).get("szjg").toString()):0.00;
	                    Double continue_wgt = resultZZF.get(0).get("xzzl")!=null ?Double.parseDouble(resultZZF.get(0).get("xzzl").toString()):0.00;
	                    Double continue_prc = resultZZF.get(0).get("xzjg")!=null ?Double.parseDouble(resultZZF.get(0).get("xzjg").toString()):0.00;

		    			if(first_wgt>vo.getCalculateWeightAll().doubleValue()){
	                    calculate +=first_prc*Integer.parseInt(calculate_sum[i]);
		    			}else{
		                calculate +=(first_prc*first_wgt+(vo.getCalculateWeightAll().doubleValue()-first_wgt)*continue_prc)*Integer.parseInt(calculate_sum[i]);
		    			}
		    		}
	                    calculate+=0.3*Integer.parseInt(calculate_sum[i]);
	                    
	                    if(!"540000".equals(destinationName[i])&&!"650000".equals(destinationName[i])){
		                    calculate+=0.3*Integer.parseInt(calculate_sum[i]);
	                    }
			    		if(result.size()>0){
	                    	if(result.size()>0)
		    			calculate+=	(result.get(0).get("fl")!=null ?Double.parseDouble(result.get(0).get("fl").toString()):0.00)*Integer.parseInt(calculate_sum[i]);
	                    }
		    	}
		    }
		
		return null;
	}
	
	public List<Map<String, Object>> wuliaoData(CostreportOrderCostChangeDO vo) {
		List<Map<String, Object>> result =	costreportOrderCostChangeDao.queryForwlgm(vo.getOrderType(), vo.getBranchCode()+"");		
		return result;
	}
	
	public List<Map<String, Object>> zzfData(
			CostreportOrderCostChangeDO vo,int center,Map<String, String> provinanceMap,Map<String, String> destinationMap) {
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (String key : provinanceMap.keySet()) {  
				 for (String destinationKey : destinationMap.keySet()) {  
				 result =	costreportOrderCostChangeDao.queryForzzf(key, destinationKey);
	             if(result.size()>0)
	             return   result;
		 	 }  
		 }	
		return result;
	}
	
	public Map<String, String> getCitysByProvinceExcel(BoBasicCitys proxy) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String name = "";
		String code = "";
		try {
			List<Map<String, Object>> list = costreportOrderCostChangeDao.getCitysByProvinceExcel(proxy.getParent());
			for (Map<String, Object> m : list) {
				name = m.get("name") == null ? "" : (String) m.get("name");
				code = m.get("code") == null ? "" : (String)m.get("code");
				map.put(code, name);
			}
		} catch (Exception e) {
			
		}
		return map;
	}
	
	/***
	 * 根据组织机构编码，获取Ydserver组织机构类型
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public String getYdserverOrgTypeByOrgCode(String orgCode) throws Exception {
			List<Map<String, Object>> list = costreportOrderCostChangeDao.getYdserverOrgTypeByOrgCode(orgCode);
			if (list == null || list.size() == 0) {
				return "";
			} else {
				String orgType = list.get(0).get("lb").toString();
				return orgType;
			}
	}
	
	/***
	 * 根据组织机构编码和机构类型获其上级机构信息
	 * 
	 * @param orgCode
	 * @param orgType
	 * @throws Exception
	 */
	public String[] queryOrgInfo(String orgCode, String orgType) throws Exception {
		ValueOperations<String, Object> operations = redisTemplate.opsForValue();

		String[] arrs = new String[2];
		if (com.yunda.base.feiniao.costreport.enums.BranchType.HEAD_OFFICE.getCode() == Integer.parseInt(orgType)) {
			// 总部
			arrs[0] = StringUtils.EMPTY;
			arrs[1] = StringUtils.EMPTY;
		} else if (com.yunda.base.feiniao.costreport.enums.BranchType.DISTRIBUTION_CENTER.getCode() == Integer.parseInt(orgType)) {
			// 分拨
			arrs[0] = StringUtils.EMPTY;
			arrs[1] = orgCode;
		} else if (com.yunda.base.feiniao.costreport.enums.BranchType.BRANCH.getCode() == Integer.parseInt(orgType)) {
			// 网点
			arrs[0] = orgCode;
			Object obj = operations.get("queryZzzByOrgCode(" + orgCode + ")");
			if (obj == null) {
				arrs[1] = queryZzzByOrgCode(orgCode);
			} else {
				arrs[1] = obj.toString();
				operations.set("queryZzzByOrgCode(" + orgCode + ")", arrs[1], 86400, TimeUnit.SECONDS);
		    }
		} else if (com.yunda.base.feiniao.costreport.enums.BranchType.SEGMENT.getCode() == Integer.parseInt(orgType) || com.yunda.base.feiniao.costreport.enums.BranchType.SERVICE_BUREAU.getCode()==Integer.parseInt(orgType)) {
			// 分部和服务部
			// 获取网点
			Object obj1 = operations.get("queryWdByOrgCode(" + orgCode + ")");
			if (obj1 == null) {
				arrs[0] = queryWdByOrgCode(orgCode);
			} else {
				arrs[0] = obj1.toString();
				operations.set("queryZzzByOrgCode(" + orgCode + ")", arrs[0], 86400, TimeUnit.SECONDS);
			}
			// 获取分拨中心
			Object obj2 = operations.get("queryZzzByOrgCode(" + orgCode + ")");
			if (obj2 == null) {
				arrs[1] = queryZzzByOrgCode(arrs[0]);
			} else {
				arrs[1] = obj2.toString();
				operations.set("queryZzzByOrgCode(" + arrs[0] + ")", arrs[1], 86400, TimeUnit.SECONDS);
			}
		}
		return arrs;
	}
	
	
	/***
	 * 根据组织机构编码获取分拨中心编码
	 * 
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	private String queryZzzByOrgCode(String orgCode) throws Exception {
		List<Map<String, Object>> list = costreportOrderCostChangeDao.queryZzzByOrgCode(orgCode);
		return list == null || list.size() == 0 ? "" : list.get(0).get("zzz").toString();
	}
	
	/***
	 * 根据组织机构编码获取网点编码
	 * 
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	private String queryWdByOrgCode(String orgCode) throws Exception {
    	List<Map<String, Object>> list = costreportOrderCostChangeDao.queryWdByOrgCode(orgCode);
		return list == null || list.size() == 0 ? "" : list.get(0).get("wd").toString();
	}

	
}
