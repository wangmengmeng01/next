package com.yunda.base.feiniao.report.dao;


import com.yunda.base.feiniao.report.domain.RebateAmountInterfaceDO;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 大客户返利新逻辑（财务用）
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-02-20141903
 */
@Mapper
public interface RebateAmountInterfaceDao {

	RebateAmountInterfaceDO get(Integer branchCode);
	
	List<RebateAmountInterfaceDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(RebateAmountInterfaceDO rebateAmountInterface);
	
	int update(RebateAmountInterfaceDO rebateAmountInterface);
	
	int remove(Integer branch_code);
	
	int batchRemove(Integer[] branchCodes);
	
	int saveFinanceCustPriceSum(String targetDay);
	
	int saveFinanceCustPriceSumOld(HashMap<String, Object> targetDay);
	
	
	int saveFinanceGsPriceSum(String targetDay);
	int saveFinanceGsPriceSumOld(String targetDay);
	
	List<RebateAmountInterfaceDO> getFinanceGsPriceSumList(String targetDay);
	
	List<RebateAmountInterfaceDO> getFinanceGsPriceSumOldList(String targetDay);
	
	RebateAmountInterfaceDO getBranchFB(Integer branchCode);
	
	int saveRebateAmount(List<RebateAmountInterfaceDO> dataList);
	
	int removeRebateAmount(String targetDay);
	
	int removeFinanceCustPriceSum(String targetDay);
	
	int removeFinanceGsPriceSum(String targetDay);
	
	int removeFinanceCustPriceSumOld(String targetDay);
	
	int removeFinanceGsPriceSumOld(String targetDay);

	int reportOrderCount(String targetDay);
	
	List<RebateAmountInterfaceDO> getGsPriceSumList(String targetDay);
	
	int saveOldRebateAmount(List<RebateAmountInterfaceDO> dataList);
	
	int reportGsPriceSumCount(String targetDay);
	
	int removeOldRebateAmount(String targetDay);
}
