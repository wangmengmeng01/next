package com.yunda.base.system.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.crab2died.ExcelUtils;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.R;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.ResourceSafeConfigDO;
import com.yunda.base.system.service.ResourceSafeConfigService;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;
/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-02-18091552
 */
 
@Controller
@RequestMapping("/system/resourceSafeConfig")
public class ResourceSafeConfigController {
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private ResourceSafeConfigService resourceSafeConfigService;
	
	@GetMapping()
	@RequiresPermissions("system:resourceSafeConfig:resourceSafeConfig")
	String ResourceSafeConfig(){
	    return "system/resourceSafeConfig/resourceSafeConfig";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("system:resourceSafeConfig:resourceSafeConfig")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<ResourceSafeConfigDO> resourceSafeConfigList = resourceSafeConfigService.list(query);
		int total = resourceSafeConfigService.count(query);
		PageUtils pageUtils = new PageUtils(resourceSafeConfigList, total);
		return pageUtils;
	}

	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@RequiresPermissions("system:resourceSafeConfig:exportExcel")
	public void exportExcel(HttpServletResponse response) {
		Map<String, Object> params = new HashMap<>(16);
		params.put("limit", "10000");// 上限保护
		Query query = new Query(params);

		//int nums = resourceSafeConfigService.count(query);

		List<ResourceSafeConfigDO> result = resourceSafeConfigService.list(query);
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";

		try {
			ExcelUtils.getInstance().exportObjects2Excel(result, ResourceSafeConfigDO.class, targetFile);

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
	@RequiresPermissions("system:resourceSafeConfig:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<ResourceSafeConfigDO>(), ResourceSafeConfigDO.class, targetFile);

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
	@RequiresPermissions("system:resourceSafeConfig:importExcel")
	public R importExcel(MultipartFile file) {
		List<ResourceSafeConfigDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, ResourceSafeConfigDO.class, 0, 0);
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
			for (ResourceSafeConfigDO _do : list) {
				resourceSafeConfigService.save(_do);
			}
		}

		return R.ok();
	}		
	
	@GetMapping("/add")
	@RequiresPermissions("system:resourceSafeConfig:add")
	String add(){
	    return "system/resourceSafeConfig/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("system:resourceSafeConfig:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		ResourceSafeConfigDO resourceSafeConfig = resourceSafeConfigService.get(id);
		model.addAttribute("resourceSafeConfig", resourceSafeConfig);
	    return "system/resourceSafeConfig/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("system:resourceSafeConfig:add")
	public R save( ResourceSafeConfigDO resourceSafeConfig){
		if(resourceSafeConfigService.save(resourceSafeConfig)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("system:resourceSafeConfig:edit")
	public R update( ResourceSafeConfigDO resourceSafeConfig){
		resourceSafeConfigService.update(resourceSafeConfig);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("system:resourceSafeConfig:remove")
	public R remove( Integer id){
		if(resourceSafeConfigService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("system:resourceSafeConfig:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] ids){
		resourceSafeConfigService.batchRemove(ids);
		return R.ok();
	}
	
}
