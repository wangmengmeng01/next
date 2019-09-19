package com.yunda.base.feiniao.warning.service;

import com.yunda.base.feiniao.warning.bo.Bo_WarningBranchMobile;
import com.yunda.base.feiniao.warning.domain.WarningBranchMobileDO;

import java.util.List;
import java.util.Map;

/**
 * 大客户预警短信--网点手机号信息表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-17132313
 */
public interface WarningBranchMobileService {
	
	WarningBranchMobileDO get(Integer orgid);
	
	List<WarningBranchMobileDO> list(Bo_WarningBranchMobile boInterface);
	
	int count(Bo_WarningBranchMobile boInterface);
	
	int save(WarningBranchMobileDO warningBranchMobile);
	
	int update(WarningBranchMobileDO warningBranchMobile);
	
	int remove(Integer orgid);
	
	int batchRemove(Integer[] orgids);
}
