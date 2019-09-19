package com.yunda.base.system.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.crab2died.ExcelUtils;
import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.R;
import com.yunda.base.feiniao.report.core.CRM_constants;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.LoginUserDO;
import com.yunda.base.system.domain.RoleDO;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.service.FileExportService;
import com.yunda.base.system.service.LoginUserService;
import com.yunda.base.system.service.RoleService;
import com.yunda.base.system.service.SessionService;
import com.yunda.base.system.service.UserService;
import com.yunda.ydmbspringbootstarter.common.annotation.Log;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;
import com.yunda.ydmbspringbootstarter.common.utils.StringUtils;
/**
 * @Author: pyj
 * @Date: 2019/1/25 10:40
 * 安全信息维护表
 * 维护省总  大区  总部业务人员  这部分用户的安全校验信息  身份证号和人脸识别等
 */
@RequestMapping("/sys/loginUser")
@Controller
public class LoginUserController extends BaseController {
    Logger log = Logger.getLogger(getClass());

    private String prefix = "system/user";
    @Autowired
    LoginUserService userService;

    @Autowired
    UserService userService_1;
    @Autowired
    RoleService roleService;
    @Autowired
    FileExportService fileExportService;
    @Autowired
    SessionService sessionService;

    @RequiresPermissions("sys:loginuser:user")
    @GetMapping("")
    String user(Model model) {
        return prefix + "/loginUser";
    }

    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, Object> params) {
        try {
            // 查询列表数据
            Query query = new Query(params);
            List<LoginUserDO> sysUserList = userService.listUsers(query);
            int total = userService.count(query);
            PageUtils pageUtil = new PageUtils(sysUserList, total);
            return pageUtil;
        } catch (Exception e) {
            return null;
        }

    }

    @RequiresPermissions("sys:loginuser:add")
    @Log("添加用户")
    @GetMapping("/add")
    String add(Model model) {
        List<RoleDO> roles = roleService.list();
        model.addAttribute("roles", roles);
        return prefix + "/loginAdd";
    }

    @RequiresPermissions("sys:user:edit")
    @Log("编辑用户")
    @GetMapping("/edit/{username}")
    String edit(Model model, @PathVariable("username") String username) {
        //先根据用户登录账号获取userId
        LoginUserDO userDO = userService.getUserByName(username);
        //根据账号获取userId从bootdo_sys_user表中
        model.addAttribute("user", userDO);
        //model.addAttribute("smsTypes",userDO.getSmsTypeList());
        //List<RoleDO> roles = roleService.list(userDO.getUserId());
        //model.addAttribute("roles", roles);
        return prefix + "/loginEdit";
    }

    @RequiresPermissions("sys:loginuser:add")
    @Log("保存用户")
    @PostMapping("/save")
    @ResponseBody
    R save(@ModelAttribute @Validated LoginUserDO user, BindingResult bindingResult) {

        try {
            if (bindingResult.hasErrors()) {
                FieldError fieldError = bindingResult.getFieldError();
                if (fieldError.getDefaultMessage().contains("Exception"))
                    return R.error();
                return R.error();
            }
//            user.setPassword(MD5Utils.encrypt(user.getUsername(), user.getPassword()));
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();

            UserDO userDo = (UserDO) session.getAttribute(CRM_constants.AUTH_USER);
            user.setUpdateName(userDo.getUsername());
            if (userService.save(user) > 0) {
                return R.ok();
            }
        } catch (Exception e) {
            return R.error();
        }
        return R.error();
    }

    @RequiresPermissions("sys:loginuser:edit")
    @Log("更新用户")
    @PostMapping("/update")
    @ResponseBody
    R update(@ModelAttribute @Validated LoginUserDO user, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                for (FieldError fieldError : bindingResult.getFieldErrors()) {
                    if (!StringUtils.equals(fieldError.getField(), "idcdNo") || fieldError.getDefaultMessage().contains("Exception")) {
                        return R.error();
                    }
                    if (StringUtils.equals(String.valueOf(fieldError.getRejectedValue()), user.getHiddenIdcdNo())) {
                        user.setIdcdNo(null);
                    } else {
                        return R.error();
                    }
                }
            }
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();
            UserDO userDo = (UserDO) session.getAttribute(CRM_constants.AUTH_USER);
            user.setUpdateName(userDo.getUsername());
            if (userService.update(user) > 0) {
                return R.ok();
            }
        } catch (
                Exception e)

        {
            return R.error();
        }
        return R.error();
    }

    @RequiresPermissions("sys:user:remove")
    @Log("删除用户")
    @PostMapping("/remove")
    @ResponseBody
    public R remove(String username) {
        if (username == null)
            return R.error();
        //LoginUserDO userDO = userService.get(id);
        //int status = userDO.getStatus();
        if (userService.remove(username) > 0) {
            return R.ok();
        }
        return R.error();
    }


    // 导出excel
    @RequestMapping("/exportExcel")
    @MethodLock(key = "exportExcel")
    @RequiresPermissions("system:loginLog:exportExcel")
    public void exportExcel(HttpServletResponse response, HttpServletRequest request, @RequestParam Map<String, Object> params) {
        //导出功能是否开放  true表示开放
        if (SysConfig.DAOCHU.equals("false")) {
            return;
        } else if (SysConfig.DAOCHU.equals("true")) {
            BufferedInputStream bin = null;
            OutputStream out = null;
            UserDO loginUser = getUser(request);

            //查询列表数据
            Query query = new Query(params);
            query.put("offset", 0);
            query.put("limit", 10000);
            List<LoginUserDO> sysUserList = userService.listUsers(query);
            String targetFile = SysConfig.TARGET + "安全信息维护" + DateUtils.format(new Date()) + ".xlsx";
            File downloadFile = new File(targetFile);
            try {
                //ExcelUtils.getInstance().exportObjects2Excel(result, ReportTotaldataDO.class, targetFile);
                // 按命名规则找模版文件
                File file = new File(SysConfig.TEMPLATE + "LoginLog.xlsx");
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                response.setCharacterEncoding("utf-8");

                // set headers for the response
                String headerKey = "Content-Disposition";
                String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
                response.setHeader(headerKey, headerValue);

                if (file.exists() && SysConfig.USER_TEMPLATE.equals("true")) {
                    //前端界面喂参  控制是否使用模板
                    Map<String, String> data = new HashMap<>();
                    // 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
                    ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0,
                            sysUserList, data, LoginUserDO.class, false, response.getOutputStream());
                } else {
                    //模板文件不存在  默认输出
                    ExcelUtils.getInstance().exportObjects2Excel(sysUserList, LoginUserDO.class, response.getOutputStream());
                }
            } catch (Exception e) {
                //e.printStackTrace();
                log.error(e.getMessage(), e);
            }

        }
    }

    // 下载excel模版
    @RequestMapping("/downTemplate")
    @MethodLock(key = "downTemplate")
    // 下载模版和导入共用同一个权限
    @RequiresPermissions("system:operateLog:importExcel")
    public void downTemplate(HttpServletResponse response) {
        String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
        try {
            ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<LoginUserDO>(), LoginUserDO.class, targetFile);

            // 写入response
            File downloadFile = new File(targetFile);
            FileUtil.downloadByResponse(response, downloadFile);
            downloadFile.delete();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            //e.printStackTrace();
        }
    }

    @GetMapping("/bigareaAndProvinceData")
    @ResponseBody
    public List<String> bigareaAndProvinceData(){
        //List<String> table_list = new ArrayList<>();
        //获取区的名称
        List<String> table_list = userService.getBigareaData();
        //获取业务省名称
        List<String> provinceData = userService.getProvinceData();
        table_list.addAll(provinceData);
        return table_list;
    }

}
