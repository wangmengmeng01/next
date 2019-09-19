package com.yunda.base.system.service;

import java.util.List;
import java.util.Map;

import com.yunda.base.system.domain.SensitiveOperateLogDO;

/**
 * 敏感操作日志
 * 
 * @author xh
 * @email zhanghan813@163.com
 * @date 2018-12-24 16:49:46
 */
public interface SensitiveOperateLogService {
	
	SensitiveOperateLogDO get(Long id);
	
	List<SensitiveOperateLogDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(SensitiveOperateLogDO sensitiveOperateLog);
	
	int update(SensitiveOperateLogDO sensitiveOperateLog);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

}
