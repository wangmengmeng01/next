package com.yunda.base.feiniao.log.dao;

import com.yunda.base.feiniao.log.domain.LogSuckdataDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-15 21:56:56
 */
@Mapper
public interface LogSuckdataDao {

	LogSuckdataDO get(Integer id);
	
	List<LogSuckdataDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(LogSuckdataDO logSuckdata);
	
	int update(LogSuckdataDO logSuckdata);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
