package com.yunda.base.bigcustomer.service.impl;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.bigcustomer.dao.CustomerManageDao;
import com.yunda.base.bigcustomer.domain.CustomerManageDO;
import com.yunda.base.bigcustomer.service.CustomerManageService;


@Service
public class CustomerManageServiceImpl implements CustomerManageService {
	@Autowired
	private CustomerManageDao customerManageDao;
	
	@Override
	public CustomerManageDO get(Integer id){
		return customerManageDao.get(id);
	}
	
	@Override
	public List<CustomerManageDO> list(Map<String, Object> map){
		return customerManageDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return customerManageDao.count(map);
	}
	
	@Override
	public int save(CustomerManageDO customerManage){
		return customerManageDao.save(customerManage);
	}
	
	@Override
	public int update(CustomerManageDO customerManage){
		return customerManageDao.update(customerManage);
	}
	
	@Override
	public int remove(Integer id){
		return customerManageDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return customerManageDao.batchRemove(ids);
	}

	@Override
	public List getOrganizationInfo() {
		return customerManageDao.getOrganizationInfo();
	}

	@Override
	public int stateUpdate(Integer id,String state) {
		return customerManageDao.stateUpdate(id,state);
	}

	@Override
	public String getBranchName(String branch) {
		return customerManageDao.getBranchName(branch);
	}

	@Override
	public int checkVipNum(String vipNum) {
		return customerManageDao.checkVipNum(vipNum);
	}

	@Override
	public String getVipNameByVipNum(String vipNum) {
		return customerManageDao.getVipNameByVipNum(vipNum);
	}

	@Override
	public List<CustomerManageDO> getCustomerAll() {
		return customerManageDao.getCustomerAll();
	}

	@Override
	public int countByCustomerName(String customerName) {
		return customerManageDao.countByCustomerName(customerName);
	}

}
