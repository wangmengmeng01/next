package com.yunda.base.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yunda.base.system.domain.CityDO;

/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-08-21 16:04:31
 */
@Mapper
public interface CityDao {

	CityDO get(String cityid);
	
	List<Long> list(Map<String,Object> map);
	
	List<CityDO>  getAllCitylist(String provinceid);
	
	int count(Map<String,Object> map);
	
	int save(CityDO city);
	
	int update(CityDO city);
	
	int remove(String CityID);
	
	int batchRemove(String[] cityids);
	
	int batchUpdateCity(String provinceId,String[] cityids );
	
	int updateCity(Map<String,Object> map);
	
	
}
