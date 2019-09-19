package com.yunda.base.system.service.impl;

import com.yunda.base.system.dao.OutLogDao;
import com.yunda.base.system.domain.OutLogDO;
import com.yunda.base.system.service.OutLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service
public class OutLogServiceImpl implements OutLogService {
	@Autowired
	private OutLogDao outLogDao;
	
	@Override
	public OutLogDO get(String sessionId){
		return outLogDao.get(sessionId);
	}
	
	@Override
	public List<OutLogDO> list(Map<String, Object> map){
		return outLogDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return outLogDao.count(map);
	}
	
	@Override
	public int save(OutLogDO outLog){
		//先查询如果没有再insert,如果有,就update
        int count = outLogDao.countBySessionId(outLog.getSessionId());
        if(count>=1){
			//因为发生一次锁表所以改成这个
			return 1;
        }
        return outLogDao.save(outLog);
	}

	@Override
	public int update(OutLogDO outLog){
		return outLogDao.update(outLog);
	}
	
	@Override
	public int remove(String sessionId){
		return outLogDao.remove(sessionId);
	}
	
	@Override
	public int batchRemove(String[] sessionIds){
		return outLogDao.batchRemove(sessionIds);
	}
	
}
