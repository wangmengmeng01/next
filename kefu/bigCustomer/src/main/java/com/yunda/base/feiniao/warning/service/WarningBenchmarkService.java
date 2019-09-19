package com.yunda.base.feiniao.warning.service;

import java.util.List;

import com.yunda.base.feiniao.warning.bo.Bo_warningBenchmark;
import com.yunda.base.feiniao.warning.domain.ExportWarningBenchmarkDO;
import com.yunda.base.feiniao.warning.domain.WarningBenchmarkDO;
import com.yunda.base.system.domain.UserDO;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-03-06143800
 */
public interface WarningBenchmarkService {
	
	WarningBenchmarkDO get(Long id);
	
	List<WarningBenchmarkDO> list(Bo_warningBenchmark boInterface, UserDO loginUser);
	
	int count(Bo_warningBenchmark boInterface, UserDO loginUser);
	
	int save(WarningBenchmarkDO warningBenchmark);
	
	int update(WarningBenchmarkDO warningBenchmark);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<ExportWarningBenchmarkDO> filterData(
			List<WarningBenchmarkDO> warningBenchmarklist);
}
