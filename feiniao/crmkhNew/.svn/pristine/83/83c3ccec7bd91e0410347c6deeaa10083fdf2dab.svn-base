package com.yunda.base.feiniao.warning.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunda.base.feiniao.warning.bo.Bo_warningBenchmark;
import com.yunda.base.feiniao.warning.dao.WarningBenchmarkDao;
import com.yunda.base.feiniao.warning.domain.ExportWarningBenchmarkDO;
import com.yunda.base.feiniao.warning.domain.WarningBenchmarkDO;
import com.yunda.base.feiniao.warning.service.WarningBenchmarkService;
import com.yunda.base.system.domain.UserDO;



@Service
public class WarningBenchmarkServiceImpl implements WarningBenchmarkService {
	@Autowired
	private WarningBenchmarkDao warningBenchmarkDao;
	
	@Override
	public WarningBenchmarkDO get(Long id){
		return warningBenchmarkDao.get(id);
	}
	
	@Override
	public List<WarningBenchmarkDO> list(Bo_warningBenchmark boInterface, UserDO loginUser){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("jgmc",boInterface.getJgmc());
		
		return warningBenchmarkDao.list(map);
	}
	
	@Override
	public int count(Bo_warningBenchmark boInterface, UserDO loginUser){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("jgmc",boInterface.getJgmc());
		return warningBenchmarkDao.count(map);
	}
	
	@Override
	public int save(WarningBenchmarkDO warningBenchmark){
		int countByJGMC = warningBenchmarkDao.countByJGMC(warningBenchmark);
		int count =0;
		if(countByJGMC>0){
			count = warningBenchmarkDao.update(warningBenchmark);
		}else{
			count =warningBenchmarkDao.save(warningBenchmark);
		}
		
		return count;
	}
	
	@Override
	public int update(WarningBenchmarkDO warningBenchmark){
		return warningBenchmarkDao.update(warningBenchmark);
	}
	
	@Override
	public int remove(Long id){
		return warningBenchmarkDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return warningBenchmarkDao.batchRemove(ids);
	}
//导出
	@Override
	public List<ExportWarningBenchmarkDO> filterData(
			List<WarningBenchmarkDO> warningBenchmarklist) {
		List<ExportWarningBenchmarkDO> exportDo = new ArrayList<ExportWarningBenchmarkDO>();
		ExportWarningBenchmarkDO newData = new ExportWarningBenchmarkDO();
		//日期格式转化为string格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		for(WarningBenchmarkDO data : warningBenchmarklist){
			
			newData = new ExportWarningBenchmarkDO();
			
			newData.setId(data.getId());
			newData.setJgmc(data.getJgmc());
			newData.setBenchmarkSetting(data.getShowBenchmarkSetting());
			newData.setUpdateDate(sdf.format(data.getUpdateDate()));
			newData.setUpdateName(data.getUpdateName());
			
			exportDo.add(newData);
		}

		return exportDo;
	}
	
}
