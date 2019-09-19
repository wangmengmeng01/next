package com.yunda.base.bigcustomer.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.bigcustomer.dao.OrganizationManageDao;
import com.yunda.base.bigcustomer.domain.OrganizationManageDO;
import com.yunda.base.bigcustomer.service.OrganizationManageService;

@Service
public class OrganizationManageServiceImpl implements OrganizationManageService {
	@Autowired
	private OrganizationManageDao organizationManageDao;
	
	@Override
	public OrganizationManageDO get(Integer id){
		return organizationManageDao.get(id);
	}
	
	@Override
	public List<OrganizationManageDO> list(Map<String, Object> map){
		return organizationManageDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return organizationManageDao.count(map);
	}
	
	@Override
	public int save(OrganizationManageDO organizationManage){
		return organizationManageDao.save(organizationManage);
	}
	
	@Override
	public int update(OrganizationManageDO organizationManage){
		return organizationManageDao.update(organizationManage);
	}
	
	@Override
	public int remove(Integer id){
		return organizationManageDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return organizationManageDao.batchRemove(ids);
	}

	@Override
	public int stateUpdate(Integer id, String state) {
		return organizationManageDao.stateUpdate(id,state);
	}

	@Override
	public List listOrganization() {
		return organizationManageDao.listOrganization();
	}

	@Override
	public List listOrganizationName() {
		return organizationManageDao.listOrganizationName();
	}

	@Override
	public int countByOrganizationNum(String organizationNum) {
		return organizationManageDao.countByOrganizationNum(organizationNum);
	}

	@Override
	public String listOrgName(String orgCode){
		return organizationManageDao.listOrgName(orgCode);
	}
}