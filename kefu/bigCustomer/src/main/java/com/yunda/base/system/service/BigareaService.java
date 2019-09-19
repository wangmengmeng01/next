package com.yunda.base.system.service;

import java.util.List;
import java.util.Map;

import com.yunda.base.common.domain.Tree;
import com.yunda.base.system.domain.BigareaDO;

/**
 * 大区
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-08-17 10:41:52
 */
public interface BigareaService {
	
	BigareaDO get(String bigareaname);
	
	List<BigareaDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(BigareaDO bigarea);
	
	int update(BigareaDO bigarea);
	
	int remove(String bigareaname);
	
	int batchRemove(String[] bigareanames);
	
	Tree<BigareaDO> getTree(Long id);
}
