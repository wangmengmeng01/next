package com.yunda.base.bigcustomer.dao;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yunda.base.bigcustomer.domain.ConfigDO;

/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-04-30113019
 */
@Mapper
public interface ConfigDao {

	ConfigDO get(Integer id);
	
	List<ConfigDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ConfigDO config);
	
	int update(ConfigDO config);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
