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
import com.yunda.base.bigcustomer.domain.PermissionsManageDO;
import com.yunda.base.bigcustomer.service.PermissionsManageService;
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
 * @date 2019-05-22111446
 */
 
@Controller
@RequestMapping("/bigcustomer/permissionsManage")
public class PermissionsManageController {
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private PermissionsManageService permissionsManageService;
	
	@GetMapping()
	@RequiresPermissions("bigcustomer:permissionsManage:permissionsManage")
	String PermissionsManage(){
	    return "bigcustomer/permissionsManage/permissionsManage";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("bigcustomer:permissionsManage:permissionsManage")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<PermissionsManageDO> permissionsManageList = permissionsManageService.list(query);
		int total = permissionsManageService.count(query);
		PageUtils pageUtils = new PageUtils(permissionsManageList, total);
		return pageUtils;
	}

	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@RequiresPermissions("bigcustomer:permissionsManage:exportExcel")
	public void exportExcel(HttpServletResponse response) {
		Map<String, Object> params = new HashMap<>(16);
		params.put("limit", "10000");// 上限保护
		Query query = new Query(params);

		//int nums = permissionsManageService.count(query);

		List<PermissionsManageDO> result = permissionsManageService.list(query);
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";

		try {
			ExcelUtils.getInstance().exportObjects2Excel(result, PermissionsManageDO.class, targetFile);

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
	@RequiresPermissions("bigcustomer:permissionsManage:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<PermissionsManageDO>(), PermissionsManageDO.class, targetFile);

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
	@RequiresPermissions("bigcustomer:permissionsManage:importExcel")
	public R importExcel(MultipartFile file) {
		List<PermissionsManageDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, PermissionsManageDO.class, 0, 0);
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
			for (PermissionsManageDO _do : list) {
				permissionsManageService.save(_do);
			}
		}

		return R.ok();
	}		
	
	@GetMapping("/add")
	@RequiresPermissions("bigcustomer:permissionsManage:add")
	String add(){
	    return "bigcustomer/permissionsManage/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("bigcustomer:permissionsManage:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		PermissionsManageDO permissionsManage = permissionsManageService.get(id);
		model.addAttribute("permissionsManage", permissionsManage);
	    return "bigcustomer/permissionsManage/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("bigcustomer:permissionsManage:add")
	public R save( PermissionsManageDO permissionsManage){
		if(permissionsManageService.save(permissionsManage)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("bigcustomer:permissionsManage:edit")
	public R update( PermissionsManageDO permissionsManage){
		permissionsManageService.update(permissionsManage);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("bigcustomer:permissionsManage:remove")
	public R remove( Integer id){
		if(permissionsManageService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("bigcustomer:permissionsManage:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] ids){
		permissionsManageService.batchRemove(ids);
		return R.ok();
	}

	/**
	 * 修改状态(启用或者禁止)
	 * @return
	 */
	@PostMapping( "/stateUpdate")
	@ResponseBody
	public R stateUpdate(@ModelAttribute PermissionsManageDO permissionsManageDO){
		String state = permissionsManageDO.getState();
		Integer id = permissionsManageDO.getId();
		if(state.equals("1")){
			//将状态改为禁用
			state="0";
			permissionsManageService.stateUpdate(id,state);
			return R.ok("已禁止");
		}else {
			//将状态改为禁用
			state="1";
			permissionsManageService.stateUpdate(id,state);
			return R.ok("已启用");
		}

	}

}