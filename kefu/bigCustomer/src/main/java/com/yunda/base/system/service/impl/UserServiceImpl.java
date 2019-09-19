package com.yunda.base.system.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yunda.base.common.domain.FileDO;
import com.yunda.base.common.domain.Tree;
import com.yunda.base.common.service.FileService;
import com.yunda.base.common.utils.BuildTree;
import com.yunda.base.common.utils.ImageUtils;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.dao.DeptDao;
import com.yunda.base.system.dao.UserBigareaDao;
import com.yunda.base.system.dao.UserDao;
import com.yunda.base.system.dao.UserProvinceDao;
import com.yunda.base.system.dao.UserRoleDao;
import com.yunda.base.system.domain.DeptDO;
import com.yunda.base.system.domain.UserBigareaDO;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.domain.UserProvinceDO;
import com.yunda.base.system.domain.UserRoleDO;
import com.yunda.base.system.service.UserService;
import com.yunda.base.system.service.ValidateUserInfo;
import com.yunda.base.system.vo.UserVO;
import com.yunda.ydmbspringbootstarter.common.utils.FileType;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;
import com.yunda.ydmbspringbootstarter.common.utils.MD5Utils;

@Transactional
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserDao userMapper;

	@Autowired
	UserRoleDao userRoleMapper;

	@Autowired
	DeptDao deptMapper;
	
	@Autowired
	UserProvinceDao userProvinceDao;

	@Autowired
	private FileService sysFileService;
	
	@Autowired
	UserBigareaDao userBigareaDao;

	@Autowired
	UserDao userDao;
	
	@Autowired
	ValidateUserInfo validateUserInfo;
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Override
	public UserDO get(Long userId) {
		List<Long> roleIds = userRoleMapper.listRoleId(userId);
		UserDO user = userMapper.get(userId);
		//user.setDeptName(deptMapper.get(user.getDeptId()).getName());
		user.setRoleIds(roleIds);
		return user;
	}

	@Override
	public List<UserDO> list(Map<String, Object> map) {
		return userMapper.list(map);
	}
	
	@Override
	public List<UserDO> listUsers(Map<String, Object> map) {
		return userMapper.listUsers(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return userMapper.count(map);
	}

	@Transactional
	@Override
	public int save(UserDO user) {
		int count = userMapper.save(user);
		Long userId = user.getUserId();
		List<Long> roles = user.getRoleIds();
		userRoleMapper.removeByUserId(userId);
		List<UserRoleDO> list = new ArrayList<>();
		for (Long roleId : roles) {
			UserRoleDO ur = new UserRoleDO();
			ur.setUserId(userId);
			ur.setRoleId(roleId);
			list.add(ur);
		}
		if (list.size() > 0) {
			userRoleMapper.batchSave(list);
		}
		//存放机构org_code编码

		return count;
	}

	@Override
	public int update(UserDO user) {
		int r = userMapper.update(user);
		Long userId = user.getUserId();
		List<Long> roles = user.getRoleIds();
		userRoleMapper.removeByUserId(userId);
		List<UserRoleDO> list = new ArrayList<>();
		for (Long roleId : roles) {
			UserRoleDO ur = new UserRoleDO();
			ur.setUserId(userId);
			ur.setRoleId(roleId);
			list.add(ur);
		}
		if (list.size() > 0) {
			userRoleMapper.batchSave(list);
		}
		return r;
	}

	@Override
	public int remove(Long userId) {
		userRoleMapper.removeByUserId(userId);
		return userMapper.remove(userId);
	}

	@Override
	public boolean exit(Map<String, Object> params) {
		boolean exit;
		exit = userMapper.list(params).size() > 0;
		return exit;
	}

	@Override
	public Set<String> listRoles(Long userId) {
		return null;
	}

	@Override
	public int resetPwd(UserVO userVO, UserDO userDO) throws Exception {
		if (Objects.equals(userVO.getUserDO().getUserId(), userDO.getUserId())) {
			if (Objects.equals(MD5Utils.encrypt(userDO.getUsername(), userVO.getPwdOld()), userDO.getPassword())) {
				userDO.setPassword(MD5Utils.encrypt(userDO.getUsername(), userVO.getPwdNew()));
				return userMapper.update(userDO);
			}
			else {
				throw new Exception("输入的旧密码有误！");
			}
		}
		else {
			throw new Exception("你修改的不是你登录的账号！");
		}
	}

	@Override
	public int adminResetPwd(UserVO userVO) throws Exception {
		UserDO userDO = get(userVO.getUserDO().getUserId());
		if ("admin".equals(userDO.getUsername())) {
			throw new Exception("超级管理员的账号不允许直接重置！");
		}
		userDO.setPassword(MD5Utils.encrypt(userDO.getUsername(), userVO.getPwdNew()));
		return userMapper.update(userDO);

	}

	@Transactional
	@Override
	public int batchremove(Long[] userIds) {
		int count = userMapper.batchRemove(userIds);
		userRoleMapper.batchRemoveByUserId(userIds);
		return count;
	}

	@Override
	public Tree<DeptDO> getTree() {
		List<Tree<DeptDO>> trees = new ArrayList<Tree<DeptDO>>();
		List<DeptDO> depts = deptMapper.list(new HashMap<String, Object>(16));
		Long[] pDepts = deptMapper.listParentDept();
		Long[] uDepts = userMapper.listAllDept();
		Long[] allDepts = (Long[]) ArrayUtils.addAll(pDepts, uDepts);
		for (DeptDO dept : depts) {
			if (!ArrayUtils.contains(allDepts, dept.getDeptId())) {
				continue;
			}
			Tree<DeptDO> tree = new Tree<DeptDO>();
			tree.setId(dept.getDeptId().toString());
			tree.setParentId(dept.getParentId().toString());
			tree.setText(dept.getName());
			Map<String, Object> state = new HashMap<>(16);
			state.put("opened", true);
			state.put("mType", "dept");
			tree.setState(state);
			trees.add(tree);
		}
		List<UserDO> users = userMapper.list(new HashMap<String, Object>(16));
		for (UserDO user : users) {
			Tree<DeptDO> tree = new Tree<DeptDO>();
			tree.setId(user.getUserId().toString());
			tree.setParentId(user.getDeptId().toString());
			tree.setText(user.getName());
			Map<String, Object> state = new HashMap<>(16);
			state.put("opened", true);
			state.put("mType", "user");
			tree.setState(state);
			trees.add(tree);
		}
		// 默认顶级菜单为０，根据数据库实际情况调整
		Tree<DeptDO> t = BuildTree.build(trees);
		return t;
	}

	@Override
	public int updatePersonal(UserDO userDO) {
		return userMapper.update(userDO);
	}

	@Override
	public Map<String, Object> updatePersonalImg(MultipartFile file, String avatar_data, Long userId) throws Exception {
		String fileName = file.getOriginalFilename();
		fileName = FileUtil.renameToUUID(fileName);
		FileDO sysFile = new FileDO(FileType.fileType(fileName), "/files/" + fileName, new Date());
		// 获取图片后缀
		String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
		String[] str = avatar_data.split(",");
		// 获取截取的x坐标
		int x = (int) Math.floor(Double.parseDouble(str[0].split(":")[1]));
		// 获取截取的y坐标
		int y = (int) Math.floor(Double.parseDouble(str[1].split(":")[1]));
		// 获取截取的高度
		int h = (int) Math.floor(Double.parseDouble(str[2].split(":")[1]));
		// 获取截取的宽度
		int w = (int) Math.floor(Double.parseDouble(str[3].split(":")[1]));
		// 获取旋转的角度
		int r = Integer.parseInt(str[4].split(":")[1].replaceAll("}", ""));
		try {
			BufferedImage cutImage = ImageUtils.cutImage(file, x, y, w, h, prefix);
			BufferedImage rotateImage = ImageUtils.rotateImage(cutImage, r);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			boolean flag = ImageIO.write(rotateImage, prefix, out);
			// 转换后存入数据库
			byte[] b = out.toByteArray();
			FileUtil.uploadFile(b, SysConfig.uploadPath, fileName);
		}
		catch (Exception e) {
			throw new Exception("图片裁剪错误！！");
		}
		Map<String, Object> result = new HashMap<>();
		if (sysFileService.save(sysFile) > 0) {
			UserDO userDO = new UserDO();
			userDO.setUserId(userId);
			userDO.setPicId(sysFile.getId());
			if (userMapper.update(userDO) > 0) {
				result.put("url", sysFile.getUrl());
			}
		}
		return result;
	}

	@Override
	/**
	 * 更新用户所管辖的省份
	 */
	public int updateProvince(UserDO user) {
		int r = 0;
		Long userId = user.getUserId();
		List<Long> provinceIds = user.getProvinceIds();
		userProvinceDao.removeByUserId(userId);
		List<UserProvinceDO> list = new ArrayList<>();
		for (Long provinceId : provinceIds) {
			UserProvinceDO up = new UserProvinceDO();
			up.setUserId(userId);
			up.setProvinceId(provinceId);
			list.add(up);
		}
		if (list.size() > 0) {
			r = userProvinceDao.batchSave(list);
		}
		return r;
	}

	@Override
	/**
	 * 更新用户管辖的大区
	 */
	public int updateBigarea(UserDO user) {
		int r = 0;
		Long userId = user.getUserId();
		List<String> bigareaNames = user.getBigareaNames();
		userBigareaDao.removeByUserId(userId);
		List<UserBigareaDO> list = new ArrayList<>();
		for (String bigareaName : bigareaNames) {
			UserBigareaDO up = new UserBigareaDO();
			up.setUserId(userId);
			up.setBigareaName(bigareaName);
			list.add(up);
		}
		if (list.size() > 0) {
			r = userBigareaDao.batchSave(list);
		}
		return r;
	}
	
	@Override
	public List<UserDO> queryLoginInfo(String username) {
		
		List<UserDO> userInfo = userDao.queryLoginInfo(username);
		return userInfo;
	}
//统一授权	
	@Override
	public Long getUserId(String userId) {
		// TODO Auto-generated method stub
		return userMapper.getUserId(userId);
	}

	@Override
	public List<String> getUserRole(Long long1) {
		// TODO Auto-generated method stub
		return userMapper.getUserRole(long1);
	}

	@Override
	public String getRoleId(String userRole1) {
		// TODO Auto-generated method stub
		return userMapper.getRoleId(userRole1);
	}
	
	@Override
	public boolean isExist(String userId) {
		// TODO Auto-generated method stub
		return userMapper.isExist(userId);
	}
	
	@Override
	public void insertPasswordAndUser(String userId, String passwordOrgin,String name,String status,String companycode) {
		// TODO Auto-generated method stub
		userMapper.insertPasswordAndUser(userId,passwordOrgin,name,status,companycode);
	}
	
	@Override
	public void updatePasswordAndUser(String userId, String passwordOrgin1, String name, String status, String companycode) {
		// TODO Auto-generated method stub
		userMapper.updatePasswordAndUser(userId,passwordOrgin1,name,status,companycode);
	}

	@Override
	public String selectUserByBase64(String username) {
		// TODO Auto-generated method stub
		return userMapper.selectUserByBase64(username);
	}

	@Override
	public void updateBase64(UserDO user) {
		// TODO Auto-generated method stub
		userMapper.updateBase64(user);
	}

	/* (non Javadoc) 
	 * @Title: getAllUserRole
	 * @Description: TODO
	 * @return 
	 * @see com.yunda.base.system.service.UserService#getAllUserRole() 
	 */
	@Override
	public List<String> getAllUserRole() {
		return userMapper.getAllUserRole();
	}

	/**
	 * 根据网点编码获取网点名称
	 * @return
	 */
	@Override
	public String getMcByBranchCode(String branchCode) {
		return userMapper.getMcByBranchCode(branchCode);
	}

	@Override
	public int stateUpdate(Long userId, int status) {
		return userMapper.stateUpdate(userId,status);
	}

	/*@Override
	public boolean checkData(UserDO row, UserDO loginUser) {
		if(row.getUsername()!=null && !"".equals(row.getUsername())&&row.getPassword() !=null &&!"".equals(row.getPassword())){
			
			String username = row.getUsername();
			String password = row.getPassword();
			String passwordSecret=com.yunda.base.common.utils.MD5Utils.getMd5String(password);//统一授权加密方式
			String password_MD5 = MD5Utils.encrypt(username, password);//飞鸟加密方式
			//去统一授权取用户信息和角色
			ValidateUser userLogin = validateUserInfo.userLogin(username, passwordSecret);
			//ValidateUser userInfo = validateUserInfo.userRole(username);
			String status = CRM_constants.STATUS;// 用户启用状态
			if (userLogin != null) {
				if (userLogin.isResult()) {
					if (userLogin.getUserData() != null) {
						return true;
					}
				}
			}
		}
		return false;
	}*/
}
