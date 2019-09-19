package com.yunda.base.feiniao.warning.service;

import java.util.List;

import com.yunda.base.feiniao.warning.bo.Bo_warningHandleDO;
import com.yunda.base.feiniao.warning.domain.ExportWarningHandleDO;
import com.yunda.base.feiniao.warning.domain.WarningHandleDO;
import com.yunda.base.system.domain.UserDO;

/**
 * 预警反馈表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-03-13095948
 */
public interface WarningHandleService {
	
	WarningHandleDO get(Long id);
	
	List<WarningHandleDO> list(Bo_warningHandleDO boInterface, UserDO loginUser);
	
	int count(Bo_warningHandleDO boInterface, UserDO loginUser);
	
	int save(WarningHandleDO warningHandle);
	
	int update(WarningHandleDO warningHandle);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<ExportWarningHandleDO> filterData(
			List<WarningHandleDO> warningHandleList);
}
