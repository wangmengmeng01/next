package com.yunda.base.system.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.crab2died.ExcelUtils;
import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.domain.Tree;
import com.yunda.base.common.service.DictService;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.R;
import com.yunda.base.feiniao.report.core.CRM_constants;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.BigareaDO;
import com.yunda.base.system.domain.DeptDO;
import com.yunda.base.system.domain.ProvinceDO;
import com.yunda.base.system.domain.RoleDO;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.domain.ValidateUser;
import com.yunda.base.system.service.BigareaService;
import com.yunda.base.system.service.ProvinceService;
import com.yunda.base.system.service.RoleService;
import com.yunda.base.system.service.UserService;
import com.yunda.base.system.service.ValidateUserInfo;
import com.yunda.base.system.vo.UserVO;
import com.yunda.ydmbspringbootstarter.common.annotation.Log;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.MD5Utils;
@RequestMapping("/sys/user")
@Controller
public class UserController extends BaseController {
	private String prefix = "system/user";
	Logger log = Logger.getLogger(getClass());
	
	@Autowired
	UserService userService;

	@Autowired
	RoleService roleService;

	@Autowired
	DictService dictService;
	
	@Autowired
	ProvinceService provinceService;
	
	@Autowired
	BigareaService bigareaService;
	@Autowired
	ValidateUserInfo validateUserInfo;
	
	@RequiresPermissions("sys:user:user")
	@GetMapping("")
	String user(Model model) {
		return prefix + "/user";
	}

	@GetMapping("/list")
	@ResponseBody
	PageUtils list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		List<UserDO> sysUserList = userService.listUsers(query);
		int total = userService.count(query);
		PageUtils pageUtil = new PageUtils(sysUserList, total);
		return pageUtil;
	}

	@RequiresPermissions("sys:user:add")
	@Log("添加用户")
	@GetMapping("/add")
	String add(Model model) {
		List<RoleDO> roles = roleService.list();
		model.addAttribute("roles", roles);
		return prefix + "/add";
	}

	@RequiresPermissions("sys:user:edit")
	@Log("编辑用户")
	@GetMapping("/edit/{id}")
	String edit(Model model, @PathVariable("id") Long id) {
		UserDO userDO = userService.get(id);
		model.addAttribute("user", userDO);
		List<RoleDO> roles = roleService.list(id);
		model.addAttribute("roles", roles);
		return prefix + "/edit";
	}
	@RequiresPermissions("sys:user:editProvince")
	@Log("维护用户省管辖范围范围")
	@GetMapping("/editProvince/{id}")
	String editProvince(Model model, @PathVariable("id") Long id) {
		UserDO user = userService.get(id);
		model.addAttribute("user", user);
		return prefix + "/editProvince";
	}
	
	
	@ResponseBody
	@RequiresPermissions("sys:user:editProvince")
	@GetMapping("/tree/{userId}")
	Tree<ProvinceDO> tree(@PathVariable("userId") Long userId) {
		Tree<ProvinceDO> tree = new Tree<ProvinceDO>();
		tree = provinceService.getTree(userId);
		return tree;
	}
	@RequiresPermissions("sys:user:editBigarea")
	@Log("维护用大区管辖范围")
	@GetMapping("/editBigarea/{id}")
	String editBigarea(Model model, @PathVariable("id") Long id) {
		UserDO user = userService.get(id);
		model.addAttribute("user", user);
		return prefix + "/editBigarea";
	}
	
	@ResponseBody
	@RequiresPermissions("sys:user:editBigarea")
	@GetMapping("/bigareaTree/{userId}")
	Tree<BigareaDO> bigareatTee(@PathVariable("userId") Long userId) {
		Tree<BigareaDO> tree = new Tree<BigareaDO>();
		tree = bigareaService.getTree(userId);
		return tree;
	}
	
	
	@RequiresPermissions("sys:user:add")
	@Log("保存用户")
	@PostMapping("/save")
	@ResponseBody
	R save(UserDO user) {
		user.setPassword(MD5Utils.encrypt(user.getUsername(), user.getPassword()));
		if (userService.save(user) > 0) {
			return R.ok();
		}
		return R.error();
	}

	@RequiresPermissions("sys:user:edit")
	@Log("更新用户")
	@PostMapping("/update")
	@ResponseBody
	R update(UserDO user) {
		if (userService.update(user) > 0) {
			return R.ok();
		}
		return R.error();
	}
	
	@RequiresPermissions("sys:user:editProvince")
	@Log("更新用户管辖省份")
	@PostMapping("/updateProvince")
	@ResponseBody
	R updateProvince(UserDO user) {
		if (userService.updateProvince(user) > 0) {
			return R.ok();
		}
		return R.error();
	}
	
	@RequiresPermissions("sys:user:editBigarea")
	@Log("更新用户管辖大区")
	@PostMapping("/updateBigarea")
	@ResponseBody
	R updateBigarea(UserDO user) {
		if (userService.updateBigarea(user) > 0) {
			return R.ok();
		}
		return R.error();
	}

	@RequiresPermissions("sys:user:edit")
	@Log("更新用户")
	@PostMapping("/updatePeronal")
	@ResponseBody
	R updatePeronal(UserDO user) {
		if (userService.updatePersonal(user) > 0) {
			return R.ok();
		}
		return R.error();
	}

	@RequiresPermissions("sys:user:remove")
	@Log("删除用户")
	@PostMapping("/remove")
	@ResponseBody
	R remove(Long id) {
		if (userService.remove(id) > 0) {
			return R.ok();
		}
		return R.error();
	}

	@RequiresPermissions("sys:user:batchRemove")
	@Log("批量删除用户")
	@PostMapping("/batchRemove")
	@ResponseBody
	R batchRemove(@RequestParam("ids[]") Long[] userIds) {
		int r = userService.batchremove(userIds);
		if (r > 0) {
			return R.ok();
		}
		return R.error();
	}

	@PostMapping("/exit")
	@ResponseBody
	boolean exit(@RequestParam Map<String, Object> params) {
		// 存在，不通过，false
		return !userService.exit(params);
	}

	@RequiresPermissions("sys:user:resetPwd")
	@Log("请求更改用户密码")
	@GetMapping("/resetPwd/{id}")
	String resetPwd(@PathVariable("id") Long userId, Model model) {

		UserDO userDO = new UserDO();
		userDO.setUserId(userId);
		model.addAttribute("user", userDO);
		return prefix + "/reset_pwd";
	}

	@Log("提交更改用户密码")
	@PostMapping("/resetPwd")
	@ResponseBody
	R resetPwd(UserVO userVO, HttpServletRequest request) {
		try {
			userService.resetPwd(userVO, getUser(request));
			return R.ok();
		}
		catch (Exception e) {
			return R.error(1, e.getMessage());
		}

	}

	@RequiresPermissions("sys:user:resetPwd")
	@Log("admin提交更改用户密码")
	@PostMapping("/adminResetPwd")
	@ResponseBody
	R adminResetPwd(UserVO userVO) {
		try {
			userService.adminResetPwd(userVO);
			return R.ok();
		}
		catch (Exception e) {
			return R.error(1, e.getMessage());
		}

	}

	@GetMapping("/tree")
	@ResponseBody
	public Tree<DeptDO> tree() {
		Tree<DeptDO> tree = new Tree<DeptDO>();
		tree = userService.getTree();
		return tree;
	}

	@GetMapping("/treeView")
	String treeView() {
		return prefix + "/userTree";
	}

	@GetMapping("/personal")
	String personal(Model model, HttpServletRequest request) {
		UserDO userDO = userService.get(getUserId(request));
		model.addAttribute("user", userDO);
		model.addAttribute("hobbyList", dictService.getHobbyList(userDO));
		model.addAttribute("sexList", dictService.getSexList());
		return prefix + "/personal";
	}

	@ResponseBody
	@PostMapping("/uploadImg")
	R uploadImg(@RequestParam("avatar_file") MultipartFile file, String avatar_data, HttpServletRequest request) {
		if ("test".equals(getUsername(request))) {
			return R.error(1, "演示系统不允许修改,完整体验请部署程序");
		}
		Map<String, Object> result = new HashMap<>();
		try {
			result = userService.updatePersonalImg(file, avatar_data, getUserId(request));
		}
		catch (Exception e) {
			return R.error("更新图像失败！");
		}
		if (result != null && result.size() > 0) {
			return R.ok(result);
		}
		else {
			return R.error("更新图像失败！");
		}
	}
	
	// 导入excel  新增网点账号专用
	/*@ResponseBody
	@MethodLock(key = "importExcel")
	@RequestMapping(value = "/importExcel", consumes = "multipart/*", headers = "content-type=mutipart/form-data", method = RequestMethod.POST)
	@RequiresPermissions("sys:user:importExcel")
	public R importExcel(HttpServletRequest request,MultipartFile file) {
		final UserDO loginUser = getUser(request);//记录操作人的姓名
		List<UserDO> list = null;
		List<String> errorlistExcel = new ArrayList<>();
		String fileKey = UUID.randomUUID().toString();
		// 获取后缀
		String fileName = file.getOriginalFilename();
		if (fileName.lastIndexOf(".") != -1) {
			String suffix = fileName.substring(fileName.lastIndexOf("."));
			String uploadFile = SysConfig.uploadPath + fileKey + suffix;

			File _f = new File(uploadFile);
			if (!_f.getParentFile().exists()) {
				_f.getParentFile().mkdirs();
			}

			BufferedOutputStream out = null;
			try {
				out = new BufferedOutputStream(new FileOutputStream(_f));
				out.write(file.getBytes());
				out.flush();

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, UserDO.class, 0, 0);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				//e.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
		if(list!= null && list.size()>0){
			for(int r=0;r<list.size();r++){
				UserDO row =list.get(r);
				boolean vo = userService.checkData(row,loginUser);
				if(vo){
				}else{
					int s = r+1;
					errorlistExcel.add("第"+s+"行用户名密码和统一授权不匹配");
				}
			}
			String msg = "";
			if (errorlistExcel != null && errorlistExcel.size()>0) {
				for(int x =0;x <errorlistExcel.size();x++){
					msg +=errorlistExcel.get(x)+";";
				}
			}
			if(!"".equals(msg)){
				return R.error(msg);
			}
		}

		if (list != null) {
			for (UserDO _do : list) {
				boolean exist = userService.isExist(_do.getUsername());
				
					String username = _do.getUsername();
					String password = _do.getPassword();
					String passwordSecret=com.yunda.base.common.utils.MD5Utils.getMd5String(password);//统一授权加密方式
					String password_MD5 = MD5Utils.encrypt(username, password);//飞鸟加密方式
					//去统一授权取用户信息和角色
					ValidateUser userLogin = validateUserInfo.userLogin(username, passwordSecret);
					//ValidateUser userInfo = validateUserInfo.userRole(username);
					String status = CRM_constants.STATUS;// 用户启用状态
					String name = userLogin.getUserData().getUsername();// 用户姓名
					String companycode = userLogin.getUserData().getOrgCode();//
				if (!exist) {	
					userService.insertPasswordAndUser(username, password_MD5, name, status, companycode);
					//userService.save(_do);
				} else {
					userService.updatePasswordAndUser(username, password_MD5, name, status, companycode);
					
				}
			}
		}

		return R.ok();
	}*/
	/*// 通过命令打开对应的操作页面
		@PostMapping("/cmd")
		@ResponseBody
		RspBean<String> cmd(@RequestParam String mingling) {
			if (Constant.cmd.containsKey(mingling)) {
				String cmd = Constant.cmd.get(mingling);
				if (Constant.cmd2Id.containsKey(cmd)) {
					Long id = Constant.cmd2Id.get(cmd);
					MenuDO menu = menuService.get(id);
					if (menu != null) {
						String url = menu.getUrl();
						if (!url.startsWith("/")) {
							url = "/" + url;
						}
						return success(url);
					}
				}
			}

			return failure(RespEnum.ERROR_BUSINESS_OPERATE.getCode());
		}*/
	
}
