package com.yunda.base.feiniao.schedule.suckdata.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yunda.base.feiniao.schedule.suckdata.domain.RecordSuckDO;

/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-21 23:55:47
 */
@Mapper
public interface RecordSuckDao {

	RecordSuckDO get(Integer id);
	
	List<RecordSuckDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(RecordSuckDO recordSuck);
	
	int update(RecordSuckDO recordSuck);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
	
	//数据做删除标记
	int delMark(Map<String,Object> map);
}
