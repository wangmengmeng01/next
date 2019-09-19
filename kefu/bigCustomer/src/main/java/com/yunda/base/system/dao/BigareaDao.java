package com.yunda.base.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yunda.base.system.domain.BigareaDO;

/**
 * 大区
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-08-17 10:41:52
 */
@Mapper
public interface BigareaDao {

	BigareaDO get(String bigareaname);
	
	List<BigareaDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(BigareaDO bigarea);
	
	int update(BigareaDO bigarea);
	
	int remove(String bigareaName);
	
	int batchRemove(String[] bigareanames);
	List<BigareaDO> getAllBigareaName();
}
