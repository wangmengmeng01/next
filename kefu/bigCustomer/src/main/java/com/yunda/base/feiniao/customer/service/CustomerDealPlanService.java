package com.yunda.base.feiniao.customer.service;

import java.util.List;

import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.feiniao.customer.bo.CustomerDealPlanBO;
import com.yunda.base.feiniao.customer.domain.CustomerDealPlanDO;
import com.yunda.base.system.domain.UserDO;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-03-19144936
 */
public interface CustomerDealPlanService {
	
	CustomerDealPlanDO get(Integer id);
	
	PageUtils list(CustomerDealPlanBO customerDealPlanBO, UserDO loginUser);
	
	int count(CustomerDealPlanBO customerDealPlanBO);
	
	int save(CustomerDealPlanDO customerDealPlan);
	
	int update(CustomerDealPlanDO customerDealPlan);
	
	int remove(String branchCode);
	
	int batchRemove(Integer[] ids);

	List<String> getProvinceNameByProvinceId(List<Long> provinceIds);

	CustomerDealPlanDO getNumByProvinceName(CustomerDealPlanBO customerDealPlanBO);

	CustomerDealPlanDO getNumByProvinceAndNotState(CustomerDealPlanBO customerDealPlanBO);

	int getNumByProvinceAndState(CustomerDealPlanBO customerDealPlanBO);

	CustomerDealPlanDO getNumByDateAndMcCode(CustomerDealPlanBO customerDealPlanBO);

}
