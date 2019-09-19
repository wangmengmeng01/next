package com.yunda.base.bigcustomer.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.crab2died.ExcelUtils;
import com.yunda.base.bigcustomer.domain.UserManageDO;
import com.yunda.base.bigcustomer.service.UserManageService;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.R;
import com.yunda.base.system.config.SysConfig;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-21164900
 */
 
@Controller
@RequestMapping("/bigcustomer/userManage")
public class UserManageController {
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private UserManageService userManageService;
	
	@GetMapping()
	@RequiresPermissions("bigcustomer:userManage:userManage")
	String UserManage(){
	    return "bigcustomer/userManage/userManage";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("bigcustomer:userManage:userManage")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<UserManageDO> userManageList = userManageService.list(query);
		int total = userManageService.count(query);
		PageUtils pageUtils = new PageUtils(userManageList, total);
		return pageUtils;
	}

	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@RequiresPermissions("bigcustomer:userManage:exportExcel")
	public void exportExcel(HttpServletResponse response) {
		Map<String, Object> params = new HashMap<>(16);
		params.put("limit", "10000");// 上限保护
		Query query = new Query(params);

		//int nums = userManageService.count(query);

		List<UserManageDO> result = userManageService.list(query);
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";

		try {
			ExcelUtils.getInstance().exportObjects2Excel(result, UserManageDO.class, targetFile);

			// 写入response
			File downloadFile = new File(targetFile);
			FileUtil.downloadByResponse(response, downloadFile);
			downloadFile.delete();
		} catch (Exception e) {
			//e.printStackTrace();
			log.error(e.getMessage(), e);
		}
	}

	// 下载excel模版
	@RequestMapping("/downTemplate")
	@MethodLock(key = "downTemplate")
	// 下载模版和导入共用同一个权限
	@RequiresPermissions("bigcustomer:userManage:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<UserManageDO>(), UserManageDO.class, targetFile);

			// 写入response
			File downloadFile = new File(targetFile);
			FileUtil.downloadByResponse(response, downloadFile);
			downloadFile.delete();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			//e.printStackTrace();
		}
	}

	// 导入excel
	@ResponseBody
	@MethodLock(key = "importExcel")
	@RequestMapping(value = "/importExcel", consumes = "multipart/*", headers = "content-type=mutipart/form-data", method = RequestMethod.POST)
	@RequiresPermissions("bigcustomer:userManage:importExcel")
	public R importExcel(MultipartFile file) {
		List<UserManageDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, UserManageDO.class, 0, 0);
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

		if (list != null) {
			for (UserManageDO _do : list) {
				userManageService.save(_do);
			}
		}

		return R.ok();
	}		
	
	@GetMapping("/add")
	@RequiresPermissions("bigcustomer:userManage:add")
	String add(){
	    return "bigcustomer/userManage/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("bigcustomer:userManage:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		UserManageDO userManage = userManageService.get(id);
		model.addAttribute("userManage", userManage);
	    return "bigcustomer/userManage/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("bigcustomer:userManage:add")
	public R save( UserManageDO userManage){
		userManage.setState("1");
		if(userManageService.save(userManage)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("bigcustomer:userManage:edit")
	public R update( UserManageDO userManage){
		userManageService.update(userManage);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("bigcustomer:userManage:remove")
	public R remove( Integer id){
		if(userManageService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("bigcustomer:userManage:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] ids){
		userManageService.batchRemove(ids);
		return R.ok();
	}

	/**
	 * 修改状态(启用或者禁止)
	 * @return
	 */
	@PostMapping( "/stateUpdate")
	@ResponseBody
	public R stateUpdate(@ModelAttribute UserManageDO userManageDO){
		String state = userManageDO.getState();
		Integer id = userManageDO.getId();
		if(state.equals("1")){
			//将状态改为禁用
			state="0";
			userManageService.stateUpdate(id,state);
			return R.ok("已禁止");
		}else {
			//将状态改为禁用
			state="1";
			userManageService.stateUpdate(id,state);
			return R.ok("已启用");
		}

	}




}
