package com.yunda.base.system.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.base.common.utils.Query;
import com.yunda.base.system.domain.RoleDO;

@Service
public interface RoleService {

	RoleDO get(Long roleId);

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

	int stateUpdate(Long roleId, String state);

	String getDataPermissionsByUserId(Long userId);

    Long getLastRoleId();
}
