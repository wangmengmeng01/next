package com.yunda.base.feiniao.costreport.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yunda.base.feiniao.costreport.domain.CostreportCustCostFinishDO;

/**
 * 客户报表订单统计/客户支出报表(完成统计)
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-09-13145339
 */
@Mapper
public interface CostreportCustCostFinishDao {

	CostreportCustCostFinishDO get(Integer recordId);
	
	List<CostreportCustCostFinishDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(CostreportCustCostFinishDO costreportCustCostFinish);
	
	int update(CostreportCustCostFinishDO costreportCustCostFinish);
	
	int remove(Integer record_id);
	
	int batchRemove(Integer[] recordIds);
	
	List<CostreportCustCostFinishDO> listTotal(Map<String,Object> map);

	
}
