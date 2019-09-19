package com.yunda.base.system.service;

import java.util.List;
import java.util.Map;

import com.yunda.base.common.domain.Tree;
import com.yunda.base.system.domain.CityDO;
import com.yunda.base.system.domain.ProvinceDO;

/**
 * 省
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-08-15 16:55:51
 */
public interface ProvinceService {
	
	ProvinceDO get(String provinceid);
	
	List<ProvinceDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ProvinceDO province);
	
	int update(ProvinceDO province);
	
	int remove(String provinceid);
	
	int batchRemove(String[] provinceids);
	
	Tree<ProvinceDO> getTree(Long id);
	
	List<ProvinceDO> maintainProvincelist(Map<String, Object> map);
	
	Tree<CityDO> getCityTree(Long provinceId);
	
    int updateCity(ProvinceDO province);
}
