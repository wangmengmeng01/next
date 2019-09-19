package com.yunda.base.system.controller;

import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.domain.FileDO;
import com.yunda.base.common.domain.Tree;
import com.yunda.base.common.enums.RespEnum;
import com.yunda.base.common.service.FileService;
import com.yunda.base.common.utils.HttpContextUtils;
import com.yunda.base.common.utils.R;
import com.yunda.base.common.utils.ShiroUtils;
import com.yunda.base.feiniao.report.core.CRM_constants;
import com.yunda.base.system.annotation.LogOut;
import com.yunda.base.system.annotation.LoginLog;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.MenuDO;
import com.yunda.base.system.domain.RoleDO;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.domain.ValidateUser;
import com.yunda.base.system.service.*;
import com.yunda.base.system.service.impl.BigareaServiceImpl;
import com.yunda.base.system.service.impl.ProvinceServiceImpl;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.utils.MD5Utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
@Controller
public class LoginController extends BaseController {
	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private ProvinceServiceImpl provinceServiceImpl;

	@Autowired
	private BigareaServiceImpl bigareaServiceImpl;

	@Autowired
	ValidateUserInfo validateUserInfo;

	@Autowired
	MenuService menuService;

	@Autowired
	FileService fileService;
	@Autowired
	LoginService loginService;
	@Autowired
	LoginUserService loginUserService;
	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@GetMapping({ "/crmkh", "" })
	String welcome(Model model) {
		return "redirect:/index";
	}

	@GetMapping({ "/index" })
	String index(Model model, HttpServletRequest request) {
		List<Tree<MenuDO>> menus = menuService.listMenuTree(getUserId(request));
		model.addAttribute("menus", menus);
		model.addAttribute("name", getUser(request).getName());
		FileDO fileDO = fileService.get(getUser(request).getPicId());
		if (fileDO != null && fileDO.getUrl() != null) {
			if (fileService.isExist(fileDO.getUrl())) {
				model.addAttribute("picUrl", SysConfig.uploadLocal + fileDO.getUrl());
			} else {
				model.addAttribute("picUrl", "/crmkh/img/logo.png");
			 }
		} else {
			model.addAttribute("picUrl", "/crmkh/img/logo.png");
		}
		model.addAttribute("username", getUser(request).getUsername());
		return "index_v1";
	}

	@GetMapping("/login")
	String login(HttpServletRequest request) {
		// 会话标识未更新
		// request.getSession().invalidate();
		/*
		 * if (request.getCookies() != null) { Cookie cookie =
		 * request.getCookies()[0];// 获取cookie cookie.setMaxAge(0);// 让cookie过期 }
		 */
		return "login";
		//return "redirect:" + ShiroConfig.loginUrl;
	}

	/**
	 * @param @param  userId
	 * @param @param  password
	 * @param @param  passwordOrgin
	 * @param @param  request
	 * @param @param  response
	 * @param @param  passwordSecret
	 * @param @return 参数说明
	 * @return R 返回类型
	 * @throws
	 * @Title: produceUser
	 * @Description: 将统一授权的用户信息同步到系统本地库
	 */
	@LoginLog("登陆")
	@PostMapping("/login")
	@ResponseBody
	public R login(String username, String password, String code, String passwordOrgin, HttpServletRequest request, HttpServletResponse response, String passwordSecret) {
		if (SysConfig.TYSQFLAG.equals("true")) {
			// 通过页面参数控制是否使用统一授权系统的登录方式 true表示使用 默认false使用本系统账号密码登录
			return loginByTysq(username, password, code, passwordOrgin, request, response, passwordSecret);
		} else {
			String password_MD5 = MD5Utils.encrypt(username, passwordOrgin);
			return loginByPass(username, password_MD5, code, request, response);
		}
	}

	// 通过统一授权登录系统
	private R loginByTysq(String username, String password, String code, String passwordOrgin, HttpServletRequest request, HttpServletResponse response, String passwordSecret) {

		String password_MD5 = MD5Utils.encrypt(username, passwordOrgin);
		String userRole = null;
		ValidateUser userLogin = validateUserInfo.userLogin(username, passwordSecret);
		ValidateUser userInfo = validateUserInfo.userRole(username);
		if (userInfo != null) {
			userRole = userInfo.getUserRole();
		}

		String status = CRM_constants.STATUS;// 用户启用状态
		if (userLogin != null) {
			if (userLogin.isResult()) {
				if (userLogin.getUserData() != null) {
					String name = userLogin.getUserData().getUsername();// 用户姓名
					String companycode = userLogin.getUserData().getOrgCode();//
					boolean exist = userService.isExist(username);
					if (!exist) {
						if (userRole != null) {
							Map<String, Object> roleMap = new HashMap<String, Object>();
							roleMap.put("roleName", userRole);
							List<RoleDO> roleList = roleService.list(roleMap);
							if (roleList ==null) {
								RoleDO role = new RoleDO();
								role.setRoleName(userRole);
								roleService.save(role);
								//return R.error("用户持有的角色不符合飞鸟系统提供的角色");
								userService.insertPasswordAndUser(username, password_MD5, name, status, companycode);
							} else {
								userService.insertPasswordAndUser(username, password_MD5, name, status, companycode);
							}
						} else {
							return R.error("当前用户未进行角色分配,请到统一授权系统注册用户");
						}
					} else {
						if (userRole != null) {
							Map<String, Object> roleMap = new HashMap<String, Object>();
							roleMap.put("roleName", userRole);
							List<RoleDO> roleList = roleService.list(roleMap);
							if (roleList ==null) {
								RoleDO role = new RoleDO();
								role.setRoleName(userRole);
								roleService.save(role);
								userService.updatePasswordAndUser(username, password_MD5, name, status, companycode);
							} else {
								userService.updatePasswordAndUser(username, password_MD5, name, status, companycode);
								//return R.error("用户持有的角色不符合飞鸟系统提供的角色");
							}
						} else {
							return R.error("当前用户未进行角色分配,请到统一授权系统注册用户");
						}
					}

					// 统一授权系统用户信息 添加到飞鸟系统
					// ValidateUser userInfo = validateUserInfo.userRole(username);//
					// 本系统的username即是统一授权的userId
					if (userInfo != null) {
						String roleId = userService.getRoleId(userRole);// 获取到该角色在本系统中的角色编号
						Long userNumber = userService.getUserId(username);// 获取到该用户在用户表中的序号
						if (null !=roleId && null != userNumber) {
							List<String> _userRoleNumber = userService.getUserRole(userNumber);// 通过用户编号查询出所有用户的角色
							if (_userRoleNumber != null && _userRoleNumber.size() > 0) {// 支持用户的多角色分配
								if (!_userRoleNumber.contains(roleId)) {
									roleService.addRole(userNumber, roleId);
								}
							} else {
								roleService.addRole(userNumber, roleId);
							}
						}
					}
					return loginByPass(username, password_MD5, code, request, response);
				}
			} /*
				 * else if (username.equals("admin")) { return loginByPass(username,
				 * password_MD5, code, request, response); }
				 */ else {
				return R.error("用户或密码错误");
			}
		}
		return R.error("用户或密码错误");
	}

	// 通过飞鸟本系统账号密码登录
	private R loginByPass(String username, String password_MD5, String code, HttpServletRequest request, HttpServletResponse response) {
		Cookie[] c = request.getCookies();
		boolean hit = false;
		for (Cookie cookie : c) {
			if (cookie.getName().equals("captchaCode")) {
				String uuid = cookie.getValue();
				//String _code = JedisUtil.getByKey(uuid);
				String _code=stringRedisTemplate.opsForValue().get(uuid);
				logger.info("------get验证码uuid-----" + uuid);
				logger.info("------get验证码_code-----" + _code);
				if (_code != null && code != null && code.equals(_code)) {
					hit = true;
				}
			}
		}
		if (!hit) {
			return R.error(RespEnum.ERROR_VERIFY_CODE);
		}

		UsernamePasswordToken token = new UsernamePasswordToken(username, password_MD5);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token); // 用token登录

			// 飞鸟中按约定用户名是工号，唯一，用于登录
			List<UserDO> userList = userService.queryLoginInfo(username);
			if (userList == null || userList.size() < 1) {
				return R.error(RespEnum.ERROR_LOGIN_WRONG);
			}

			if (userList.size() > 1) {
				logger.error(username + "出现多个用户，请关注");
			}
			UserDO loginUser = userList.get(0);

			// 补全人和省份关系
			List<Long> up = provinceServiceImpl.queryUP(loginUser.getUserId());
			for (int i = 0; i < up.size(); i++) {
				if ("-1".equals(up.get(i) + "")) {
					up.remove(i);
				}
			}
			loginUser.setProvinceIds(up);
			// logger.info("登录时set省权限 setProvinceIds"+up);
			// 补全人和大区关系
			List<String> bigarea = bigareaServiceImpl.queryUP(loginUser.getUserId());
			for (int i = 0; i < bigarea.size(); i++) {
				if ("-1".equals(bigarea.get(i))) {
					bigarea.remove(i);
				}
			}
			loginUser.setBigareaNames(bigarea);

			// 补全用户有自定义权限
			Set<String> perms = menuService.listPerms(loginUser.getUserId());
			loginUser.setPerms(perms);

			Session session = subject.getSession();
			session.setAttribute(CRM_constants.AUTH_USER, loginUser);

			// 会话标识未更新问题
			setCookiesByJsessionid(request, response);
			return R.ok();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return R.error(RespEnum.ERROR_LOGIN_WRONG);
		}
	}

	/**
	 * 更新会话标识
	 *
	 * @param request
	 * @param response
	 */
	private void setCookiesByJsessionid(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		/*
		 * if(cookies!=null){ for(Cookie cookie: cookies){ cookie.setMaxAge(0);
		 * cookie.setPath("/"); } }
		 */

		// 先检查是否存在JSESSIONID
		boolean flag = false;
		for (int i = 0; i < cookies.length; i++) {
			Cookie c = cookies[i];
			if ("JSESSIONID".equals(c.getName())) {
				c.setValue(request.getSession(true).getId());
				response.addCookie(c);
				flag = true;
			}
		}
		// }
		// 如果不存在JSESSIONID就新增一个 存活期为1个月
		if (flag) {
			// 存活期为一个月 (日*时*分*秒)
			int maxAge = 30 * 24 * 60 * 60;
			Cookie cookie = new Cookie("JSESSIONID", request.getSession(true).getId());
			cookie.setMaxAge(maxAge);
			response.addCookie(cookie);
		}

		// logger.info("Cookie已经保存: JSESSIONID=" + request.getSession(true).getId());
	}

	@GetMapping("/logout")
	@LogOut("退出")
	String logout() throws ServletException, IOException {
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		HttpServletResponse response = HttpContextUtils.getHttpServletResponse();
		RequestDispatcher view = request.getRequestDispatcher("/login");
		view.forward(request,response);
		// 会话标识未更新
		request.getSession().invalidate();

		ShiroUtils.logout();
		return "redirect:/login";
	}

	@GetMapping("/main")
	String main() {
		return "main";
	}

	@GetMapping("/403")
	String error403() {
		return "403";
	}

	// 发短信
	@PostMapping("/smsLogin")
	@ResponseBody
	public RspBean smsLogin() {
		String userName = ShiroUtils.getUser().getUsername();
		String phone = loginUserService.getUserByName(userName).getMobile();
		try {
			logger.info("手机号" + phone);
			return loginService.smsLogin(phone);

		} catch (DocumentException e) {
			logger.error("手机号" + phone + "发送短信失败");
		}
		return new RspBean(RespEnum.SMS_FAIL);
	}

	@GetMapping("/tochrome")
	@ResponseBody
	public String tochrome() {
		return SysConfig.IE_TO_CHROME_ADDRESS;
	}

}
