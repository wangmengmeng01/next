package com.yunda.base.system.service;

import com.yunda.base.system.domain.OutLogDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-01-24170431
 */
public interface OutLogService {
	
	OutLogDO get(String sessionId);
	
	List<OutLogDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(OutLogDO outLog);
	
	int update(OutLogDO outLog);
	
	int remove(String sessionId);
	
	int batchRemove(String[] sessionIds);
}
