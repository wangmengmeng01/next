package com.yunda.base.feiniao.costreport.dao;

import com.yunda.base.feiniao.costreport.domain.CostreportOrderCostChangeDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 客户报表订单统计/客户月结账单
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-11-09140854
 */
@Mapper
public interface CostreportOrderCostChangeDao {

	CostreportOrderCostChangeDO get(Integer recordId);
	
	List<CostreportOrderCostChangeDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(CostreportOrderCostChangeDO costreportOrderCostChange);
	
	int update(CostreportOrderCostChangeDO costreportOrderCostChange);
	
	int remove(Integer record_id);
	
	int batchRemove(Integer[] recordIds);
	
    String getYdserverOrgCodeByUserId(String userId) throws Exception;

	List<Map<String, Object>> queryZzzByOrgCode(String orgCode);
	
	List<Map<String, Object>> queryWdByOrgCode(String orgCode);

    String[] queryOrgInfo(String orgCode, String orgType) throws Exception;

    List<Map<String, Object>> getYdserverOrgTypeByOrgCode(String orgCode) throws Exception;

    List<Map<String, Object>> getCitysByProvinceExcel(String parent) throws Exception;

    List<Map<String, Object>> queryForzzf(String provinanceKey,String destinationKey);

    List<Map<String, Object>> queryForwlgm(String orgType,String branchCode);
}
