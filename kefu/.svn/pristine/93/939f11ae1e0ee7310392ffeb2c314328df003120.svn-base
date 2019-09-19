package com.yunda.base.system.dao;

import com.yunda.base.common.domain.LogDO;
import com.yunda.base.common.utils.Query;
import com.yunda.base.system.domain.LoginLogDO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 系统日志
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-03 15:45:42
 */
@Repository
public interface LoginLogDao {

	/**
	 * 根据id获取信息
	 * @param id
	 * @return
	 */
	LoginLogDO get(Long id);

	List<LoginLogDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);
	
	int save(LoginLogDO loginLog);
	
	int update(LoginLogDO loginLog);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

/*	List<LogDO> listLog(Query query);*/

    String queryPictureBySessionId(String sessionId);
}
