package com.yunda.base.feiniao.warning.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.yunda.base.feiniao.warning.bo.Bo_WarningBranchMobile;
import com.yunda.base.feiniao.warning.dao.WarningBranchMobileDao;
import com.yunda.base.feiniao.warning.domain.WarningBranchMobileDO;
import com.yunda.base.feiniao.warning.service.WarningBranchMobileService;

@Service
public class WarningBranchMobileServiceImpl implements WarningBranchMobileService {
	@Autowired
	private WarningBranchMobileDao warningBranchMobileDao;
	
	@Override
	public WarningBranchMobileDO get(Integer orgid){
		return warningBranchMobileDao.get(orgid);
	}
	
	@Override
	public List<WarningBranchMobileDO> list(Bo_WarningBranchMobile boInterface){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", boInterface.getLimit());
		map.put("offset", boInterface.getOffset());
		map.put("branchCode", boInterface.getBranchCode());
		map.put("status", boInterface.getStatus());
		List<WarningBranchMobileDO> dataList = warningBranchMobileDao.list(map);
		for(WarningBranchMobileDO data :dataList){
			data.setShowStatus(data.getStatus());
		}
		return dataList;
	}
	
	@Override
	public int count(Bo_WarningBranchMobile boInterface){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("branchCode", boInterface.getBranchCode());
		map.put("status", boInterface.getStatus());
		return warningBranchMobileDao.count(map);
	}
	
	@Override
	public int save(WarningBranchMobileDO warningBranchMobile){
		return warningBranchMobileDao.save(warningBranchMobile);
	}
	
	@Override
	public int update(WarningBranchMobileDO warningBranchMobile){
		return warningBranchMobileDao.update(warningBranchMobile);
	}
	
	@Override
	public int remove(Integer orgid){
		return warningBranchMobileDao.remove(orgid);
	}
	
	@Override
	public int batchRemove(Integer[] orgids){
		return warningBranchMobileDao.batchRemove(orgids);
	}
	
}
