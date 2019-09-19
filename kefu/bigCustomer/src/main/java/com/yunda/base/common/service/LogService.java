package com.yunda.base.common.service;

import org.springframework.stereotype.Service;

import com.yunda.base.common.domain.LogDO;
import com.yunda.base.common.domain.PageDO;
import com.yunda.base.common.utils.Query;

@Service
public interface LogService {
	void save(LogDO logDO);
	PageDO<LogDO> queryList(Query query);
	int remove(Long id);
	int batchRemove(Long[] ids);
}
