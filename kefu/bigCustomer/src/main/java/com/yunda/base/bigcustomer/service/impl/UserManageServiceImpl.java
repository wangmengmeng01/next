package com.yunda.base.bigcustomer.service.impl;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.bigcustomer.dao.UserManageDao;
import com.yunda.base.bigcustomer.domain.UserManageDO;
import com.yunda.base.bigcustomer.service.UserManageService;

@Service
public class UserManageServiceImpl implements UserManageService {
	@Autowired
	private UserManageDao userManageDao;
	
	@Override
	public UserManageDO get(Integer id){
		return userManageDao.get(id);
	}
	
	@Override
	public List<UserManageDO> list(Map<String, Object> map){
		return userManageDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return userManageDao.count(map);
	}
	
	@Override
	public int save(UserManageDO userManage){
		return userManageDao.save(userManage);
	}
	
	@Override
	public int update(UserManageDO userManage){
		return userManageDao.update(userManage);
	}
	
	@Override
	public int remove(Integer id){
		return userManageDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return userManageDao.batchRemove(ids);
	}

	@Override
	public int stateUpdate(Integer id, String state) {
		return userManageDao.stateUpdate(id,state);
	}

}