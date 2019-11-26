package com.yunda.base.feiniao.customer.service;

import com.yunda.base.feiniao.customer.bo.NotCooperateCustomerBO;
import com.yunda.base.feiniao.customer.domain.CooperatePeerDO;
import com.yunda.base.feiniao.customer.domain.NotCooperateCustomerDO;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-03-01105527
 */
public interface NotCooperateCustomerService {
	
	NotCooperateCustomerDO get(Integer id);
	
	List<NotCooperateCustomerDO> list(NotCooperateCustomerBO notCooperateCustomerBO);
	
	int count(NotCooperateCustomerBO notCooperateCustomerBO);
	
	int save(NotCooperateCustomerDO notCooperateCustomer);
	
	int update(NotCooperateCustomerDO notCooperateCustomer);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

    String getSummary();

	String getCompanyNameByOrgCode(String orgCode);
	
	List<String> getProvinceNameByBigareaNames(List<String> regionIdsQX);
	List<String> getProvinceNameByProvinceId(List<Long> provinceIds);
	
	List<NotCooperateCustomerDO> listInfoByProvinceName(NotCooperateCustomerBO notCooperateCustomerBO);

	int countByProvinceName(NotCooperateCustomerBO notCooperateCustomerBO);

	int updateBoundVipAccountById(NotCooperateCustomerDO notCooperateCustomer);

	int checkCooperateBranch(String cooperateBranch);

	List<CooperatePeerDO> huiXianCooperatePeer(String branchCode, String productType);
}