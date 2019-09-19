package com.yunda.base.feiniao.log.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

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
import com.yunda.base.feiniao.log.domain.LogSuckdataDO;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.system.config.SysConfig;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;
/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-15 21:56:56
 */

@Controller
@RequestMapping("/log/logSuckdata")
public class LogSuckdataController {
	@Autowired
	private LogSuckdataService logSuckdataService;

	@GetMapping()
	@RequiresPermissions("log:logSuckdata:logSuckdata")
	String LogSuckdata() {
		return "feiniao/log/logSuckdata/logSuckdata";
	}

	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("log:logSuckdata:logSuckdata")
	public PageUtils list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		List<LogSuckdataDO> logSuckdataList = logSuckdataService.list(query);
		int total = logSuckdataService.count(query);
		PageUtils pageUtils = new PageUtils(logSuckdataList, total);
		return pageUtils;
	}

	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@RequiresPermissions("log:logSuckdata:exportExcel")
	public void exportExcel(HttpServletResponse response) {
		Map<String, Object> params = new HashMap<>(16);
		params.put("limit", "10000");// 上限保护
		Query query = new Query(params);

		// int nums = logSuckdataService.count(query);

		List<LogSuckdataDO> result = logSuckdataService.list(query);
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";

		try {
			ExcelUtils.getInstance().exportObjects2Excel(result, LogSuckdataDO.class, targetFile);

			// 写入response
			File downloadFile = new File(targetFile);
			FileUtil.downloadByResponse(response, downloadFile);
			downloadFile.delete();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	// 下载excel模版
	@RequestMapping("/downTemplate")
	@MethodLock(key = "downTemplate")
	// 下载模版和导入共用同一个权限
	@RequiresPermissions("log:logSuckdata:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<LogSuckdataDO>(), LogSuckdataDO.class,
					targetFile);

			// 写入response
			File downloadFile = new File(targetFile);
			FileUtil.downloadByResponse(response, downloadFile);
			downloadFile.delete();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	// 导入excel
	@ResponseBody
	@MethodLock(key = "importExcel")
	@RequestMapping(value = "/importExcel", consumes = "multipart/*", headers = "content-type=mutipart/form-data", method = RequestMethod.POST)
	@RequiresPermissions("log:logSuckdata:importExcel")
	public R importExcel(MultipartFile file) {
		List<LogSuckdataDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, LogSuckdataDO.class, 0, 0);
			} catch (Exception e) {
				
				//e.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}

		if (list != null) {
			for (LogSuckdataDO _do : list) {
				logSuckdataService.save(_do);
			}
		}

		return R.ok();
	}

	@GetMapping("/add")
	@RequiresPermissions("log:logSuckdata:add")
	String add() {
		return "feiniao/log/logSuckdata/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("log:logSuckdata:edit")
	String edit(@PathVariable("id") Integer id, Model model) {
		LogSuckdataDO logSuckdata = logSuckdataService.get(id);
		model.addAttribute("logSuckdata", logSuckdata);
		return "feiniao/log/logSuckdata/edit";
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("log:logSuckdata:add")
	public R save(LogSuckdataDO logSuckdata) {
		if (logSuckdataService.save(logSuckdata) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("log:logSuckdata:edit")
	public R update(LogSuckdataDO logSuckdata) {
		logSuckdataService.update(logSuckdata);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	@RequiresPermissions("log:logSuckdata:remove")
	public R remove(Integer id) {
		if (logSuckdataService.remove(id) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 删除
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	@RequiresPermissions("log:logSuckdata:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] ids) {
		logSuckdataService.batchRemove(ids);
		return R.ok();
	}

}
