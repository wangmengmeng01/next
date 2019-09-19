package com.yunda.base.feiniao.warning.dao;

import com.yunda.base.feiniao.warning.domain.WarningBenchmarkDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-03-06143800
 */
@Mapper
public interface WarningBenchmarkDao {

	WarningBenchmarkDO get(Long id);
	
	List<WarningBenchmarkDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int countByJGMC(WarningBenchmarkDO warningBenchmark);
	
	int save(WarningBenchmarkDO warningBenchmark);
	
	int update(WarningBenchmarkDO warningBenchmark);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
