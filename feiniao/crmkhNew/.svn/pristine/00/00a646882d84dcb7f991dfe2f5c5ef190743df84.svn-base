package com.yunda.base.common.service;

import com.yunda.base.common.domain.LogDO;
import com.yunda.base.common.domain.PageDO;
import com.yunda.base.common.utils.Query;
import org.springframework.stereotype.Service;

@Service
public interface LogService {
	void save(LogDO logDO);
	PageDO<LogDO> queryList(Query query);
	int remove(Long id);
	int batchRemove(Long[] ids);
}
