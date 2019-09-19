package com.yunda.base.system.service;

import com.yunda.base.system.domain.LoginLogDO;

import java.util.List;
import java.util.Map;

/**
 * 敏感操作日志
 * 
 * @author xh
 * @email zhanghan813@163.com
 * @date 2018-12-24 16:49:46
 */
public interface LoginLogService {
	
	LoginLogDO get(Long id);
	
	List<LoginLogDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(LoginLogDO loginLog);
	
	int update(LoginLogDO loginLog);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	String queryPictureBySessionId(String sessionId);
}
