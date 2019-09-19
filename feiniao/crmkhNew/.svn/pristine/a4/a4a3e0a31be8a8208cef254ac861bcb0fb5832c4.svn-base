package com.yunda.base.feiniao.warning.dao;

import com.yunda.base.feiniao.warning.domain.WarningHandleDO;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 预警反馈表
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-03-13095948
 */
@Mapper
public interface WarningHandleDao {

	WarningHandleDO get(Long id);
	
	List<WarningHandleDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(WarningHandleDO warningHandle);
	
	int update(WarningHandleDO warningHandle);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	WarningHandleDO getWDFeedback(Map<String, Object> map);
//预警反馈表  按天生成预警基础数据
	int removeYjByDay(Date targetDay);
	int saveYjByDay(Map<String, Object> param);
//预警反馈表--预警短信相关sql
	List<WarningHandleDO> provinceList(Map<String,Object> map);//省名称(去重)
	
	List<WarningHandleDO> branchList(Map<String,Object> map);
	
	int customerList(Map<String,Object> map);
	
	List<WarningHandleDO> daquList(Map<String,Object> map);//大区(去重)
	
}
