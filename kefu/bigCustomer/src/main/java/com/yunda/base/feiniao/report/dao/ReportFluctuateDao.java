package com.yunda.base.feiniao.report.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yunda.base.feiniao.report.domain.ReportFluctuateDO;

/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-18 13:31:19
 */
@Mapper
public interface ReportFluctuateDao {


	
	List<ReportFluctuateDO> getDoWithCitySheng(Map<String,Object> map);
	
	List<ReportFluctuateDO> getDoWithCityShengNew(Map<String,Object> map);
	
	List<ReportFluctuateDO> getDoWithCityShi(Map<String,Object> map);
	
	List<ReportFluctuateDO> getDoWithCityShiNew(Map<String,Object> map);
	
	List<ReportFluctuateDO> getDoWithCustnumber(Map<String,Object> map);
	
	List<ReportFluctuateDO> getDoWithCustnumberNew(Map<String,Object> map);
	
	List<ReportFluctuateDO> getDoWithBranchShi(Map<String,Object> map);
	
	List<ReportFluctuateDO> getDoWithBranchShiNew(Map<String,Object> map);
	
	List<ReportFluctuateDO> getDoWithBranchGs1(Map<String,Object> map);
	
	List<ReportFluctuateDO> getDoWithBranchGs1New(Map<String,Object> map);
	
	List<ReportFluctuateDO> getDoWithBranchGs2(Map<String,Object> map);
	
	List<ReportFluctuateDO> getDoWithBranchGs2New(Map<String,Object> map);
	
	List<ReportFluctuateDO> getDoWithProvinceAllCountData(Map<String,Object> map);
	
	List<ReportFluctuateDO> getDoWithProvinceAllCountDataNew(Map<String,Object> map);
	
	List<ReportFluctuateDO> getDoWithProvinceBigArea(Map<String,Object> map);
	
	List<ReportFluctuateDO> getDoWithProvinceBigAreaNew(Map<String,Object> map);
	
	List<ReportFluctuateDO> getDoWithProvinceSheng(Map<String,Object> map);
	
	List<ReportFluctuateDO> getDoWithProvinceShengNew(Map<String,Object> map);
	
	/**
	 * 抽数-按日客户波动情况
	 * @param targetDay
	 * @return
	 */
    int saveBdByCustomer(Map<String, Object> map);
	
	/**
	 * 删除     每日客户波动基础数据
	 * @param targetDay
	 * @return
	 */
    int removeBdByCustomer(Date targetDay);
	
	
	/**
	 * 抽数-按日客户波动情况
	 * @param targetDay
	 * @return
	 */
    int saveBdByCustomerBefore20180801(Map<String, Object> map);
	
	int  custBDcount(Map<String,Object> map);
	
	int  custBDcountNew(Map<String,Object> map);
}
