package com.yunda.base.system.service;

import java.util.List;
import java.util.Map;

import com.yunda.base.system.domain.SysConfigDO;

public interface SysConfigService {

	// 配置参数从数据库中，动态注入静态变量
    void initConfig();

	SysConfigDO get(Integer id);

	List<SysConfigDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(SysConfigDO config);

	int update(SysConfigDO config);

	int remove(Integer id);

	int batchRemove(Integer[] ids);
}