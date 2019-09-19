package com.yunda.base.system.dao;

import com.yunda.base.system.domain.ProvinceDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 省
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-08-15 16:55:51
 */
@Mapper
public interface ProvinceDao {

	ProvinceDO get(String provinceid);
	
	List<ProvinceDO> list(Map<String,Object> map);
	
	List<ProvinceDO> maintainProvincelist(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ProvinceDO province);
	
	int update(ProvinceDO province);
	
	int remove(String ProvinceID);
	
	int batchRemove(String[] provinceids);
	
	List<ProvinceDO> getAllProvinces();
}
