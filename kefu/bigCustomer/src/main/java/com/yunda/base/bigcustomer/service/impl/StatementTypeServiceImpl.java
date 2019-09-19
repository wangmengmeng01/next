package com.yunda.base.bigcustomer.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.bigcustomer.dao.StatementTypeDao;
import com.yunda.base.bigcustomer.domain.StatementTypeDO;
import com.yunda.base.bigcustomer.service.StatementTypeService;

@Service
public class StatementTypeServiceImpl implements StatementTypeService {
	@Autowired
	private StatementTypeDao statementTypeDao;
	
	@Override
	public StatementTypeDO get(Long id){
		return statementTypeDao.get(id);
	}
	
	@Override
	public List<StatementTypeDO> list(Map<String, Object> map){
		return statementTypeDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return statementTypeDao.count(map);
	}
	
	@Override
	public int save(StatementTypeDO statementType){
		return statementTypeDao.save(statementType);
	}
	
	@Override
	public int update(StatementTypeDO statementType){
		return statementTypeDao.update(statementType);
	}
	
	@Override
	public int remove(Long id){
		return statementTypeDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return statementTypeDao.batchRemove(ids);
	}

	@Override
	public List<String> getJiedanList(String consultType){
		return statementTypeDao.getJiedanList(consultType);
	}
	
}