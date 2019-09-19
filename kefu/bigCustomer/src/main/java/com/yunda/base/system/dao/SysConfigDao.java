package com.yunda.base.system.dao;

import java.util.List;
import java.util.Map;

import com.yunda.base.system.domain.SysConfigDO;

public interface SysConfigDao {

	// 获取系统的配置表sys_config中所有信息
	List<SysConfigDO> queryAll();

	SysConfigDO get(Integer id);

	List<SysConfigDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(SysConfigDO config);

	int update(SysConfigDO config);

	int remove(Integer id);

	int batchRemove(Integer[] ids);

}
