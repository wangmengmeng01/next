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

import org.apache.commons.lang3.StringUtils;
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
import com.yunda.base.system.domain.VideoDownloadDO;
import com.yunda.base.system.service.VideoDownloadService;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;
/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-02-25131242
 */

@Controller
@RequestMapping("/system/videoDownload")
public class VideoDownloadController {
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private VideoDownloadService videoDownloadService;

	@GetMapping()
	@RequiresPermissions("system:videoDownload:videoDownload")
	String VideoDownload() {
		return "system/videoDownload/videoDownload";
	}

	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("system:videoDownload:videoDownload")
	public PageUtils list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		List<VideoDownloadDO> videoDownloadList = videoDownloadService.list(query);
		int total = videoDownloadService.count(query);
		PageUtils pageUtils = new PageUtils(videoDownloadList, total);
		return pageUtils;
	}

	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@RequiresPermissions("system:videoDownload:exportExcel")
	public void exportExcel(HttpServletResponse response) {
		Map<String, Object> params = new HashMap<>(16);
		params.put("limit", "10000");// 上限保护
		Query query = new Query(params);

		// int nums = videoDownloadService.count(query);

		List<VideoDownloadDO> result = videoDownloadService.list(query);
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";

		try {
			ExcelUtils.getInstance().exportObjects2Excel(result, VideoDownloadDO.class, targetFile);

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
	@RequiresPermissions("system:videoDownload:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<VideoDownloadDO>(), VideoDownloadDO.class, targetFile);

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
	@RequiresPermissions("system:videoDownload:importExcel")
	public R importExcel(MultipartFile file) {
		List<VideoDownloadDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, VideoDownloadDO.class, 0, 0);
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
			for (VideoDownloadDO _do : list) {
				videoDownloadService.save(_do);
			}
		}

		return R.ok();
	}

	@GetMapping("/add")
	@RequiresPermissions("system:videoDownload:add")
	String add() {
		return "system/videoDownload/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("system:videoDownload:edit")
	String edit(@PathVariable("id") Long id, Model model) {
		VideoDownloadDO videoDownload = videoDownloadService.get(id);
		model.addAttribute("videoDownload", videoDownload);
		return "system/videoDownload/edit";
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("system:videoDownload:add")
	public R save(VideoDownloadDO videoDownload) {
		if (videoDownloadService.save(videoDownload) > 0) {
			return R.ok();
		}
		return R.error();
	}

	@GetMapping("/download/{id}")
	@RequiresPermissions("system:videoDownload:download")
	public void download(@PathVariable("id") Long id, Model model, HttpServletResponse response) throws Exception {
		VideoDownloadDO videoDownloadDO = videoDownloadService.get(id);
		String filePath = videoDownloadDO.getFilePath();
		try {
			if (StringUtils.isNotBlank(filePath)) {
				String filename = filePath.substring(filePath.lastIndexOf(File.separator)+1);
				FileUtil.download(response, filename, filePath);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("system:videoDownload:edit")
	public R update(VideoDownloadDO videoDownload) {
		videoDownloadService.update(videoDownload);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	@RequiresPermissions("system:videoDownload:remove")
	public R remove(Long id) {
		if (videoDownloadService.remove(id) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 删除
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	@RequiresPermissions("system:videoDownload:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids) {
		videoDownloadService.batchRemove(ids);
		return R.ok();
	}

}
