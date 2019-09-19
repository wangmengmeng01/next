package com.yunda.base.bigcustomer.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.bigcustomer.dao.PermissionsManageDao;
import com.yunda.base.bigcustomer.domain.PermissionsManageDO;
import com.yunda.base.bigcustomer.service.PermissionsManageService;

@Service
public class PermissionsManageServiceImpl implements PermissionsManageService {
	@Autowired
	private PermissionsManageDao permissionsManageDao;
	
	@Override
	public PermissionsManageDO get(Integer id){
		return permissionsManageDao.get(id);
	}
	
	@Override
	public List<PermissionsManageDO> list(Map<String, Object> map){
		return permissionsManageDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return permissionsManageDao.count(map);
	}
	
	@Override
	public int save(PermissionsManageDO permissionsManage){
		return permissionsManageDao.save(permissionsManage);
	}
	
	@Override
	public int update(PermissionsManageDO permissionsManage){
		return permissionsManageDao.update(permissionsManage);
	}
	
	@Override
	public int remove(Integer id){
		return permissionsManageDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return permissionsManageDao.batchRemove(ids);
	}

	@Override
	public int stateUpdate(Integer id, String state) {
		return permissionsManageDao.stateUpdate(id,state);
	}

}
