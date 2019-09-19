package com.yunda.base.feiniao.report.dao;

import com.yunda.base.feiniao.report.domain.ProvinceOdSumDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-19 09:25:02
 */
@Mapper
public interface ProvinceOdSumDao {

	ProvinceOdSumDO get(String provinceid);
	
	List<ProvinceOdSumDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ProvinceOdSumDO provinceOdSum);
	
	int update(ProvinceOdSumDO provinceOdSum);
	
	int remove(String provinceid);
	
	int batchRemove(String[] provinceids);
}
