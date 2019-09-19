package com.yunda.base.system.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.github.crab2died.ExcelUtils;
import com.yunda.base.common.config.Constant;
import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.R;
import com.yunda.base.feiniao.report.utils.ZipUtil;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.FileExportDO;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.service.FileExportService;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;
/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-12-22232134
 */

@Controller
@RequestMapping("/system/fileExport")
public class FileExportController extends BaseController {
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private FileExportService fileExportService;

	@GetMapping()
	@RequiresPermissions("system:fileExport:fileExport")
	String FileExport() {
		return "system/fileExport/fileExport";
	}

	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("system:fileExport:fileExport")
	public PageUtils list(@RequestParam Map<String, Object> params, HttpServletRequest request) {
		final UserDO loginUser = getUser(request);
		if (loginUser.hasPerms(Constant.EXPORT_ADMIN)) {
			Query query = new Query(params);
			List<FileExportDO> fileExportList = fileExportService.list(query);
			for (FileExportDO fileExportDO : fileExportList) {
				UserDO parseObject = JSON.parseObject(String.valueOf(fileExportDO.getUserId()), UserDO.class);
				String username = String.format("%s(%s)", parseObject.getName(), parseObject.getUsername());
				if (!ObjectUtils.isEmpty(parseObject)) {
					fileExportDO.setUserId(username);
				}
			}
			int total = fileExportService.count(query);
			PageUtils pageUtils = new PageUtils(fileExportList, total);
			return pageUtils;

		} else {
			Query query = new Query(params);
			List<FileExportDO> fileExportList = fileExportService.list(query);
			List<FileExportDO> fl = new ArrayList<FileExportDO>();
			for (FileExportDO fileExportDO : fileExportList) {
				UserDO parseObject = JSON.parseObject(String.valueOf(fileExportDO.getUserId()), UserDO.class);
				String username = String.format("%s(%s)", parseObject.getName(), parseObject.getUsername());
				String requsername = String.format("%s(%s)", getUser(request).getName(), getUser(request).getUsername());
				if (!ObjectUtils.isEmpty(parseObject)) {
					if (StringUtils.equals(requsername, username)) {
						fileExportDO.setUserId(username);
						fl.add(fileExportDO);
					}
				}
			}
			PageUtils pageUtils = new PageUtils(fl, fl.size());
			return pageUtils;
		}
	}

	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@RequiresPermissions("system:fileExport:exportExcel")
	public void exportExcel(HttpServletResponse response) {
		Map<String, Object> params = new HashMap<>(16);
		params.put("limit", "10000");// 上限保护
		Query query = new Query(params);

		// int nums = fileExportService.count(query);

		List<FileExportDO> result = fileExportService.list(query);
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";

		try {
			ExcelUtils.getInstance().exportObjects2Excel(result, FileExportDO.class, targetFile);

			// 写入response
			File downloadFile = new File(targetFile);
			FileUtil.downloadByResponse(response, downloadFile);
			downloadFile.delete();
		} catch (Exception e) {
			// e.printStackTrace();
			log.error(e.getMessage(), e);
		}
	}

	// 下载excel模版
	@RequestMapping("/downTemplate")
	@MethodLock(key = "downTemplate")
	// 下载模版和导入共用同一个权限
	@RequiresPermissions("system:fileExport:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<FileExportDO>(), FileExportDO.class, targetFile);

			// 写入response
			File downloadFile = new File(targetFile);
			FileUtil.downloadByResponse(response, downloadFile);
			downloadFile.delete();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// e.printStackTrace();
		}
	}

	// 导入excel
	@ResponseBody
	@MethodLock(key = "importExcel")
	@RequestMapping(value = "/importExcel", consumes = "multipart/*", headers = "content-type=mutipart/form-data", method = RequestMethod.POST)
	@RequiresPermissions("system:fileExport:importExcel")
	public R importExcel(MultipartFile file) {
		List<FileExportDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, FileExportDO.class, 0, 0);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				// e.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}

		if (list != null) {
			for (FileExportDO _do : list) {
				fileExportService.save(_do);
			}
		}

		return R.ok();
	}

	@GetMapping("/add")
	@RequiresPermissions("system:fileExport:add")
	String add() {
		return "system/fileExport/add";
	}

	@ResponseBody
	@GetMapping("/getTitle")
	public List<String> getTitle(@RequestParam String param) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("title", param);
		List<String> itemName = fileExportService.getTitle(params);
		return itemName;
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("system:fileExport:edit")
	String edit(@PathVariable("id") Long id, Model model) {
		FileExportDO fileExport = fileExportService.get(id);
		model.addAttribute("fileExport", fileExport);
		return "system/fileExport/edit";
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("system:fileExport:add")
	public R save(FileExportDO fileExport) {
		if (fileExportService.save(fileExport) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("system:fileExport:edit")
	public R update(FileExportDO fileExport) {
		fileExportService.update(fileExport);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	@RequiresPermissions("system:fileExport:remove")
	public R remove(Long id) {
		if (fileExportService.remove(id) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 删除
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	@RequiresPermissions("system:fileExport:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids) {
		fileExportService.batchRemove(ids);
		return R.ok();
	}

	/**
	 * 
	 * @param ids
	 * @param response
	 * @return 下载
	 */
	@RequiresPermissions("system:fileExport:download")
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public R excelDownload(@RequestParam("ids[]") Long[] ids, HttpServletResponse response) {
		response.reset();
		response.setContentType("application/octet-stream");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"fileExport.zip\"");
		response.setHeader("content-type", "application/octet-stream");

		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(response.getOutputStream());
		} catch (IOException e1) {
			log.error(e1.getMessage(), e1);
		}
		for (int id = 0; id < ids.length; id++) {
			FileExportDO fileExportDO = fileExportService.get(ids[id]);
			String filePath = fileExportDO.getFilePath();
			if (StringUtils.isNoneBlank(filePath)) {
				File path2 = new File(filePath);
				try {
					if (path2 != null && path2.exists()) {
						ZipUtil.zipFile("文件名称含义:时分秒", path2, zos);
					}
				} catch (IOException e1) {
					log.error(e1.getMessage(), e1);
				}
			}
		}
		try {
			zos.flush();
			zos.close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return R.ok();
	}
}
