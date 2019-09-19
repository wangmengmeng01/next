package com.yunda.base.feiniao.warning.dao;

import com.yunda.base.feiniao.warning.domain.WarningBranchMobileDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 大客户预警短信--网点手机号信息表
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-17132313
 */
@Mapper
public interface WarningBranchMobileDao {

	WarningBranchMobileDO get(Integer orgid);
	
	List<WarningBranchMobileDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(WarningBranchMobileDO warningBranchMobile);
	
	int update(WarningBranchMobileDO warningBranchMobile);
	
	int remove(Integer orgid);
	
	int batchRemove(Integer[] orgids);
}
