package com.yunda.base.feiniao.customer.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yunda.base.feiniao.customer.bo.CustomerDealPlanBO;
import com.yunda.base.feiniao.customer.domain.CustomerDealPlanDO;

/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-03-19144936
 */
@Mapper
public interface CustomerDealPlanDao {

	CustomerDealPlanDO get(Integer id);
	
	List<CustomerDealPlanDO> list(CustomerDealPlanBO customerDealPlanBO);
	
	int count(CustomerDealPlanBO customerDealPlanBO);
	
	int save(CustomerDealPlanDO customerDealPlan);
	
	int update(CustomerDealPlanDO customerDealPlan);
	
	int remove(String branchCode);
	
	int batchRemove(Integer[] ids);

	List<String> getProvinceNameByProvinceId(List<Long> provinceIds);

	CustomerDealPlanDO getNumByProvinceName(CustomerDealPlanBO customerDealPlanBO);

	CustomerDealPlanDO getNumByNotState(CustomerDealPlanBO customerDealPlanBO);

	int getNumByState(CustomerDealPlanBO customerDealPlanBO);

	List<String> getProvinceNameAll(CustomerDealPlanBO customerDealPlanBO);

	String getChangeCooperationAmount(CustomerDealPlanBO customerDealPlanBO);

	List<String> getBranchId(String branchCode);

	CustomerDealPlanDO getNumByMcCode(CustomerDealPlanBO customerDealPlanBO);

	List<String> getMcCodeByProvinceName(@Param("organization") String organization,@Param("startDate")String statDate,@Param("endDate")String endDate);

    CustomerDealPlanDO getNumByDateAndMcCode(CustomerDealPlanBO customerDealPlanBO);

    List<CustomerDealPlanDO> getDataByProvinceName(CustomerDealPlanBO customerDealPlanBO);

	List<CustomerDealPlanDO> getAllProvinceHuiZongData(CustomerDealPlanBO customerDealPlanBO);

	List<CustomerDealPlanDO> getProvinceHuiZongDataByProvinceIds(CustomerDealPlanBO customerDealPlanBO);

	List<CustomerDealPlanDO> getMcHuiZongDataByMcCode(CustomerDealPlanBO customerDealPlanBO);

	String getOrderNumByProvinceName(CustomerDealPlanDO customerDealPlanDO);

	String getAllProvinceOrderNum(CustomerDealPlanDO customerDealPlanDO);

	String getProvinceOrderNum(CustomerDealPlanDO customerDealPlanDO);

	String getMcOrderNum(CustomerDealPlanDO customerDealPlanDO);
}
