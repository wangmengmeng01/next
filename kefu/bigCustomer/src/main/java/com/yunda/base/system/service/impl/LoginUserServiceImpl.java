package com.yunda.base.system.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunda.base.system.dao.LoginUserDao;
import com.yunda.base.system.dao.UserRoleDao;
import com.yunda.base.system.domain.LoginUserDO;
import com.yunda.base.system.service.LoginUserService;

/**
 * @Author: pyj
 * @Date: 2019/1/25 10:47
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class LoginUserServiceImpl implements LoginUserService {
    @Autowired
    LoginUserDao userMapper;
    @Autowired
    UserRoleDao userRoleMapper;
    @Override
    public LoginUserDO get(Long id) {
        List<Long> roleIds = userRoleMapper.listRoleId(id);
        LoginUserDO user = userMapper.get(id);
        if (null != user.getIdcdNo() && !user.getIdcdNo().equals("")) {
            StringBuilder sb = new StringBuilder(user.getIdcdNo());
            sb.replace(6, 14, "********");
            user.setIdcdNo(sb.toString());
        }
        //user.setDeptName(deptMapper.get(user.getDeptId()).getName());
//        user.setRoleIds(roleIds);
        return user;
    }
    @Override
    public List<LoginUserDO> list(Map<String, Object> map) {
        return userMapper.list(map);
    }

    @Override
    public List<LoginUserDO> listUsers(Map<String, Object> map) {

        List<LoginUserDO> userDOS = userMapper.listUsers(map);
        for (LoginUserDO user:userDOS){
            if (null != user.getIdcdNo() && !user.getIdcdNo().equals("")) {
                StringBuilder sb = new StringBuilder(user.getIdcdNo());
                sb.replace(6, 14, "********");
                user.setIdcdNo(sb.toString());
            }
        }
        return userDOS;
    }

    @Override
    public int count(Map<String, Object> map) {
        return userMapper.count(map);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int save(LoginUserDO user) {
        //先查询后save
        int coutByUserName = userMapper.countByUserName(user.getUsername());
        if(coutByUserName>=1){
            int updatecount = userMapper.update(user);
            return updatecount;
        }
        int count = userMapper.save(user);
        Long userId = user.getUserId();
//        List<Long> roles = user.getRoleIds();
//        userRoleMapper.removeByUserId(userId);
//        List<UserRoleDO> list = new ArrayList<>();
//        for (Long roleId : roles) {
//            UserRoleDO ur = new UserRoleDO();
//            ur.setUserId(userId);
//            ur.setRoleId(roleId);
//            list.add(ur);
//        }
//        if (list.size() > 0) {
//            userRoleMapper.batchSave(list);
//        }
        return count;
    }

    @Override
    public int update(LoginUserDO user) {

        int r = userMapper.update(user);
        Long userId = user.getUserId();
//        List<Long> roles = user.getRoleIds();
//        userRoleMapper.removeByUserId(userId);
//        List<UserRoleDO> list = new ArrayList<>();
//        for (Long roleId : roles) {
//            UserRoleDO ur = new UserRoleDO();
//            ur.setUserId(userId);
//            ur.setRoleId(roleId);
//            list.add(ur);
//        }
//        if (list.size() > 0) {
//            userRoleMapper.batchSave(list);
//        }
        return r;
    }

    @Override
    public int remove(Long userId,int status) {
//		userRoleMapper.removeByUserId(userId);
        if (status==1){
            status=0;
        }else{
            status=1;
        }
        return userMapper.remove(userId,status);
    }

    @Override
    public LoginUserDO getUserByName(String userName) {
        LoginUserDO loginUserDO = userMapper.getUserByName(userName);
        String idcdNo = loginUserDO.getIdcdNo();
        if (null != idcdNo && !idcdNo.equals("")) {
            StringBuilder sb = new StringBuilder(idcdNo);
            sb.replace(6, 14, "********");
            loginUserDO.setIdcdNo(sb.toString());
        }
        return loginUserDO;
    }
	/* (non Javadoc) 
	 * @Title: getFaceimageByUsername
	 * @Description: TODO
	 * @param username
	 * @return 
	 * @see com.yunda.base.system.service.LoginUserService#getFaceimageByUsername(java.lang.String) 
	 */
	@Override
	public String getFaceimageByUsername(String username) {
		return userMapper.getFaceimageByUsername(username);
	}

    @Override
    public List<String> getBigareaData() {
        return userMapper.getBigareaData();
    }

    @Override
    public List<String> getProvinceData() {
        return userMapper.getProvinceData();
    }

    @Override
    public LoginUserDO getUserDOByName(String username) {
        return userMapper.getUserByName(username);
    }
}