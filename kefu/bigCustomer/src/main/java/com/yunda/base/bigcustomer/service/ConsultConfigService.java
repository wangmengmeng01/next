package com.yunda.base.bigcustomer.service;
import java.util.List;
import java.util.Map;

import com.yunda.base.bigcustomer.domain.ConsultConfigDO;
/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-22162719
 */
public interface ConsultConfigService {
	
	ConsultConfigDO get(Integer id);
	
	List<ConsultConfigDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ConsultConfigDO consultConfig);
	
	int update(ConsultConfigDO consultConfig);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

    int stateUpdate(Integer id, String state);

    List<String> searchConsultType();
}