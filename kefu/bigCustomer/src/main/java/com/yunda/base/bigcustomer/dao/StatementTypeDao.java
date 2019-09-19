package com.yunda.base.bigcustomer.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yunda.base.bigcustomer.domain.StatementTypeDO;

/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-07-24153026
 */
@Mapper
public interface StatementTypeDao {

	StatementTypeDO get(Long id);
	
	List<StatementTypeDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(StatementTypeDO statementType);
	
	int update(StatementTypeDO statementType);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<String> getJiedanList(String consultType);
}
