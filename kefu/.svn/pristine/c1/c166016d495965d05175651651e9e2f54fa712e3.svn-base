package com.yunda.base.feiniao.report.dao;

import com.yunda.base.feiniao.report.domain.ReportOrderStatsAllDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 客户报表订单统计-总表
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-18 16:23:26
 */
@Mapper
public interface ReportOrderStatsAllDao {

	ReportOrderStatsAllDO get(String customerId);
	
	List<ReportOrderStatsAllDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ReportOrderStatsAllDO reportOrderStatsAll);
	
	int update(ReportOrderStatsAllDO reportOrderStatsAll);
	
	int remove(String customer_id);
	
	int batchRemove(String[] customerIds);
}
