package com.yunda.base.system.service.impl;

import com.yunda.base.system.dao.SensitiveOperateLogDao;
import com.yunda.base.system.domain.SensitiveOperateLogDO;
import com.yunda.base.system.service.SensitiveOperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class SensitiveOperateLogServiceImpl implements SensitiveOperateLogService {
	@Autowired
	private SensitiveOperateLogDao sensitiveOperateLogDao;
	
	@Override
	public SensitiveOperateLogDO get(Long id){
		return sensitiveOperateLogDao.get(id);
	}
	
	@Override
	public List<SensitiveOperateLogDO> list(Map<String, Object> map){
		return sensitiveOperateLogDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return sensitiveOperateLogDao.count(map);
	}
	
	@Override
	public int save(SensitiveOperateLogDO sensitiveOperateLog){
		return sensitiveOperateLogDao.save(sensitiveOperateLog);
	}
	
	@Override
	public int update(SensitiveOperateLogDO sensitiveOperateLog){
		return sensitiveOperateLogDao.update(sensitiveOperateLog);
	}
	
	@Override
	public int remove(Long id){
		return sensitiveOperateLogDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return sensitiveOperateLogDao.batchRemove(ids);
	}


}
