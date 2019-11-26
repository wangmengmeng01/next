package com.yunda.base.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunda.base.common.utils.Query;
import com.yunda.base.system.dao.RoleDao;
import com.yunda.base.system.dao.RoleMenuDao;
import com.yunda.base.system.dao.UserDao;
import com.yunda.base.system.dao.UserRoleDao;
import com.yunda.base.system.domain.RoleDO;
import com.yunda.base.system.domain.RoleMenuDO;
import com.yunda.base.system.service.RoleService;
import com.yunda.ydmbspringbootstarter.common.utils.StringUtils;

@Service
public class RoleServiceImpl implements RoleService {

	public static final String ROLE_ALL_KEY = "\"role_all\"";

	public static final String DEMO_CACHE_NAME = "role";

	@Autowired
	RoleDao roleMapper;

	@Autowired
	RoleMenuDao roleMenuMapper;

	@Autowired
	UserDao userMapper;

	@Autowired
	UserRoleDao userRoleMapper;
	
	@Override
	public List<RoleDO> list(Map<String, Object> map) {
		return roleMapper.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map) {
		return roleMapper.count(map);
	}

	@Override
	public List<RoleDO> list() {
		List<RoleDO> roles = roleMapper.list(new HashMap<String, Object>(16));
		return roles;
	}

	@Override
	public List<RoleDO> list(Long userId) {
		List<Long> rolesIds = userRoleMapper.listRoleId(userId);

		/*获取该用户的角色
		  1 如果是总部      不限制
		  2 如果是客服主管  限制只能建客服主管和客服角色
		 */
		String roleId = null;
		if(rolesIds.size()==1){
			roleId = rolesIds.get(0)+"";
		}
		List<RoleDO> roles = roleMapper.listRoleQX(roleId);
		for (RoleDO roleDO : roles) {
			roleDO.setRoleSign("false");
			for (Long role_Id : rolesIds) {
				if (Objects.equals(roleDO.getRoleId(), role_Id)) {
					roleDO.setRoleSign("true");
					break;
				}
			}
		}
		return roles;
	}

	@Transactional
	@Override
	public int save(RoleDO role) {
		int count = roleMapper.save(role);
		List<Long> menuIds = role.getMenuIds();
		Long roleId = role.getRoleId();
		List<RoleMenuDO> rms = new ArrayList<>();
		for (Long menuId : menuIds) {
			RoleMenuDO rmDo = new RoleMenuDO();
			rmDo.setRoleId(roleId);
			rmDo.setMenuId(menuId);
			rms.add(rmDo);
		}
		roleMenuMapper.removeByRoleId(roleId);
		if (!rms.isEmpty()) {
			roleMenuMapper.batchSave(rms);
		}
		return count;
	}

	@Transactional
	@Override
	public int remove(Long id) {
		int count = roleMapper.remove(id);
		roleMenuMapper.removeByRoleId(id);
		return count;
	}

	@Override
	public RoleDO get(Long roleId) {
		RoleDO roleDO = roleMapper.get(roleId);
		return roleDO;
	}

	@Override
	public int update(RoleDO role) {
		if(role.getRoleName()!=null || role.getRemark()!=null || role.getDataPermissions()!=null){
			roleMapper.update(role);
		}
		List<Long> menuIds = role.getMenuIds();
		Long roleId = role.getRoleId();
		roleMenuMapper.removeByRoleId(roleId);
		List<RoleMenuDO> rms = new ArrayList<>();
		for (Long menuId : menuIds) {
			RoleMenuDO rmDo = new RoleMenuDO();
			rmDo.setRoleId(roleId);
			rmDo.setMenuId(menuId);
			rms.add(rmDo);
		}
		if (!rms.isEmpty()) {
			roleMenuMapper.batchSave(rms);
		}
		return 1;
	}

	@Override
	public int batchremove(Long[] ids) {
		int r = roleMapper.batchRemove(ids);
		return r;
	}

	@Override
	public List<RoleDO> listRole(Query query) {
		List<RoleDO> roleDOS = roleMapper.listRole(query);
		//1.遍历如果有上级角色编码则根据上级角色编码查询上级角色名称
		for (RoleDO roleDO : roleDOS) {
			if(StringUtils.isBlank(roleDO.getUpRoleId())){
				continue;
			}
			String upRoleName = roleMapper.getRoleNameByRoleId(roleDO.getUpRoleId());
			if(StringUtils.isBlank(upRoleName)){
				continue;
			}
			roleDO.setUpRoleName(upRoleName);
		}
		return roleDOS;
	}
	//统一授权
	@Override
	public void addRole(Long userId, String userRole) {
		// TODO Auto-generated method stub
		userRoleMapper.addRole(userId,userRole);
	}

	@Override
	public int stateUpdate(Long roleId, String state) {
		return roleMapper.stateUpdate(roleId,state);
	}

	@Override
	public String getDataPermissionsByUserId(Long userId) {
		return roleMapper.getDataPermissionsByUserId(userId);
	}

	@Override
	public Long getLastRoleId() {
		return roleMapper.getLastRoleId();
	}

}