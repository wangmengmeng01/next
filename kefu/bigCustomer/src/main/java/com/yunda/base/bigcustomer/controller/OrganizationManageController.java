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
import com.yunda.base.bigcustomer.domain.OrganizationManageDO;
import com.yunda.base.bigcustomer.service.OrganizationManageService;
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
 * @date 2019-05-21110249
 */
 
@Controller
@RequestMapping("/bigcustomer/organizationManage")
public class OrganizationManageController {
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private OrganizationManageService organizationManageService;
	
	@GetMapping()
	@RequiresPermissions("bigcustomer:organizationManage:organizationManage")
	String OrganizationManage(){
	    return "bigcustomer/organizationManage/organizationManage";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("bigcustomer:organizationManage:organizationManage")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<OrganizationManageDO> organizationManageList = organizationManageService.list(query);
		int total = organizationManageService.count(query);
		PageUtils pageUtils = new PageUtils(organizationManageList, total);
		return pageUtils;
	}

	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@RequiresPermissions("bigcustomer:organizationManage:exportExcel")
	public void exportExcel(HttpServletResponse response) {
		Map<String, Object> params = new HashMap<>(16);
		params.put("limit", "10000");// 上限保护
		Query query = new Query(params);

		//int nums = organizationManageService.count(query);

		List<OrganizationManageDO> result = organizationManageService.list(query);
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";

		try {
			ExcelUtils.getInstance().exportObjects2Excel(result, OrganizationManageDO.class, targetFile);

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
	@RequiresPermissions("bigcustomer:organizationManage:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<OrganizationManageDO>(), OrganizationManageDO.class, targetFile);

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
	@RequiresPermissions("bigcustomer:organizationManage:importExcel")
	public R importExcel(MultipartFile file) {
		List<OrganizationManageDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, OrganizationManageDO.class, 0, 0);
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
			for (OrganizationManageDO _do : list) {
				organizationManageService.save(_do);
			}
		}

		return R.ok();
	}		
	
	@GetMapping("/add")
	@RequiresPermissions("bigcustomer:organizationManage:add")
	String add(){
	    return "bigcustomer/organizationManage/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("bigcustomer:organizationManage:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		OrganizationManageDO organizationManage = organizationManageService.get(id);
		model.addAttribute("organizationManage", organizationManage);
	    return "bigcustomer/organizationManage/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("bigcustomer:organizationManage:add")
	public R save( OrganizationManageDO organizationManage){
		//首先查看机构编码是否存在存在的话返回
		int count = organizationManageService.countByOrganizationNum(organizationManage.getOrganizationNum());
		if(count>=1){
			return R.error("该机构编码已存在!");
		}
		organizationManage.setState("1");
		if(organizationManageService.save(organizationManage)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("bigcustomer:organizationManage:edit")
	public R update( OrganizationManageDO organizationManage){
		//首先查看机构编码是否存在存在的话返回
		/*int count = organizationManageService.countByOrganizationNum(organizationManage.getOrganizationNum());
		if(count>=1){
			return R.error("该机构编码已存在!");
		}*/
		organizationManageService.update(organizationManage);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("bigcustomer:organizationManage:remove")
	public R remove( Integer id){
		if(organizationManageService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("bigcustomer:organizationManage:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] ids){
		organizationManageService.batchRemove(ids);
		return R.ok();
	}

	/**
	 * 修改状态(启用或者禁止)
	 * @return
	 */
	@PostMapping( "/stateUpdate")
	@ResponseBody
	public R stateUpdate(@ModelAttribute OrganizationManageDO organizationManage){
		String state = organizationManage.getState();
		Integer id = organizationManage.getId();
		if(state.equals("1")){
			//将状态改为禁用
			state="0";
			organizationManageService.stateUpdate(id,state);
			return R.ok("已禁止");
		}else {
			//将状态改为禁用
			state="1";
			organizationManageService.stateUpdate(id,state);
			return R.ok("已启用");
		}

	}
	
}
