package com.yunda.base.feiniao.customer.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yunda.base.feiniao.customer.bo.NotCooperateCustomerBO;
import com.yunda.base.feiniao.customer.domain.ChangeCooperateOrderNumDO;
import com.yunda.base.feiniao.customer.domain.CooperatePeerDO;
import com.yunda.base.feiniao.customer.domain.NotCooperateCustomerDO;

/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-03-01105527
 */
@Mapper
public interface NotCooperateCustomerDao {

	NotCooperateCustomerDO get(Integer id);
	
	List<NotCooperateCustomerDO> list(NotCooperateCustomerBO notCooperateCustomerBO);
	
	int count(NotCooperateCustomerBO notCooperateCustomerBO);
	
	int save(NotCooperateCustomerDO notCooperateCustomer);
	
	int update(NotCooperateCustomerDO notCooperateCustomer);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	/**
	 * 通过时间和状态来获取数据
	 * @param map
	 * @return
	 */
	Map<String,Object> queryByState(HashMap<String,Object> map);
	/**
	 * 通过时间和非该状态来获取数据
	 * @param map
	 * @return
	 */
	Map<String,Object> queryByNotState(HashMap<String,Object> map);

    String getgetCompanyNameByOrgCode(@Param("orgCode") String orgCode);

	NotCooperateCustomerDO getProvinceNameByBranchCode(String branchCode);

	String getCityNameByBranchCode(String branchCode);

	List<String> getProvinceNameByProvinceId(List<Long> provinceIds);

	List<NotCooperateCustomerDO> listInfoByProvinceName(NotCooperateCustomerBO notCooperateCustomerBO);

	int countByProvinceName(NotCooperateCustomerBO notCooperateCustomerBO);

    int updateBoundVipAccountById(NotCooperateCustomerDO notCooperateCustomer);

	String getMcByBranchCode(@Param("branchCode") String branchCode);

	String getMcCodeByBranchCode(@Param("provinceMcCode") String provinceMcCode);

	int checkCooperateBranch(String cooperateBranch);

    List<CooperatePeerDO> huiXianCooperatePeer(@Param("branchCode")String branchCode,@Param("productType")String productType);

    List<ChangeCooperateOrderNumDO> getSuccessCooperateByDate(ChangeCooperateOrderNumDO changeCooperateOrderNumDO);

	int saveChangeCooperateOrderNumDO(ChangeCooperateOrderNumDO changeCooperateOrderNumDO);

	void removeBdByDate(@Param("dqday") String dqday);
}
