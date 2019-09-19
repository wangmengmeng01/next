package com.yunda.base.bigcustomer.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yunda.base.bigcustomer.domain.ConsultConfigDO;


/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-22162719
 */
@Mapper
public interface ConsultConfigDao {

	ConsultConfigDO get(Integer id);
	
	List<ConsultConfigDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ConsultConfigDO consultConfig);
	
	int update(ConsultConfigDO consultConfig);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	int stateUpdate(@Param("id") Integer id, @Param("state") String state);

	List<String> searchConsultType();
}