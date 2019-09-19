package com.yunda.base.system.service;

import com.yunda.base.common.utils.Query;
import com.yunda.base.system.domain.RoleDO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface RoleService {

	RoleDO get(Long id);

	List<RoleDO> list();
	
	List<RoleDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	int save(RoleDO role);

	int update(RoleDO role);

	int remove(Long id);

	List<RoleDO> list(Long userId);

	int batchremove(Long[] ids);

	List<RoleDO> listRole(Query query);

	void addRole(Long userNumber, String roleId);
}
