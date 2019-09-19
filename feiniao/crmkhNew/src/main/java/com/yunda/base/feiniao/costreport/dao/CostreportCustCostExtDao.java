package com.yunda.base.feiniao.costreport.dao;

import com.yunda.base.feiniao.costreport.domain.CostreportCustCostExtDO;
import com.yunda.base.feiniao.costreport.domain.CostreportOrderCostDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 客户报表订单统计/客户拓展支出
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-09-13145432
 */
@Mapper
public interface CostreportCustCostExtDao {

	CostreportCustCostExtDO get(Integer recordId);
	
	List<CostreportCustCostExtDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(CostreportCustCostExtDO costreportCustCostExt);
	
	int update(CostreportCustCostExtDO costreportCustCostExt);
	
	int remove(Integer record_id);
	
	int batchRemove(Integer[] recordIds);
	
	void groupCostExtData(String date);
	
	List<CostreportOrderCostDO> listDetail(Map<String,Object> map);

}
