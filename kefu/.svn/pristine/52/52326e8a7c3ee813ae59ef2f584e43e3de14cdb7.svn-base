package com.yunda.base.system.dao;

import com.yunda.base.system.domain.OutLogDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-01-24170431
 */
@Mapper
public interface OutLogDao {

	OutLogDO get(String sessionId);
	
	List<OutLogDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	int countBySessionId(String sessionId);
	
	int save(OutLogDO outLog);
	
	int update(OutLogDO outLog);
	
	int remove(String session_id);
	
	int batchRemove(String[] sessionIds);
}
