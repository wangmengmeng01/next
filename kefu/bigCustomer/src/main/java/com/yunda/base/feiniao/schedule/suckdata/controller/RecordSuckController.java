package com.yunda.base.feiniao.schedule.suckdata.controller;

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
import com.yunda.base.feiniao.schedule.suckdata.domain.RecordSuckDO;
import com.yunda.base.feiniao.schedule.suckdata.service.RecordSuckService;
import com.yunda.base.system.config.SysConfig;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;
/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-21 23:55:47
 */
 
@Controller
@RequestMapping("/suckdata/recordSuck")
public class RecordSuckController {
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private RecordSuckService recordSuckService;
	
	@GetMapping()
	@RequiresPermissions("suckdata:recordSuck:recordSuck")
	String RecordSuck(){
	    return "feiniao/schedule/suckdata/recordSuck/recordSuck";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("suckdata:recordSuck:recordSuck")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<RecordSuckDO> recordSuckList = recordSuckService.list(query);
		int total = recordSuckService.count(query);
		PageUtils pageUtils = new PageUtils(recordSuckList, total);
		return pageUtils;
	}

	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@RequiresPermissions("suckdata:recordSuck:exportExcel")
	public void exportExcel(HttpServletResponse response) {
		Map<String, Object> params = new HashMap<>(16);
		params.put("limit", "10000");// 上限保护
		Query query = new Query(params);

		//int nums = recordSuckService.count(query);

		List<RecordSuckDO> result = recordSuckService.list(query);
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";

		try {
			ExcelUtils.getInstance().exportObjects2Excel(result, RecordSuckDO.class, targetFile);

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
	@RequiresPermissions("suckdata:recordSuck:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<RecordSuckDO>(), RecordSuckDO.class, targetFile);

			// 写入response
			File downloadFile = new File(targetFile);
			FileUtil.downloadByResponse(response, downloadFile);
			downloadFile.delete();
		} catch (Exception e) {
			//e.printStackTrace();
			log.error(e.getMessage(), e);
		}
	}

	// 导入excel
	@ResponseBody
	@MethodLock(key = "importExcel")
	@RequestMapping(value = "/importExcel", consumes = "multipart/*", headers = "content-type=mutipart/form-data", method = RequestMethod.POST)
	@RequiresPermissions("suckdata:recordSuck:importExcel")
	public R importExcel(MultipartFile file) {
		List<RecordSuckDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, RecordSuckDO.class, 0, 0);
			} catch (Exception e) {
				//e.printStackTrace();
				log.error(e.getMessage(), e);
			} finally {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}

		if (list != null) {
			for (RecordSuckDO _do : list) {
				recordSuckService.save(_do);
			}
		}

		return R.ok();
	}		
	
	@GetMapping("/add")
	@RequiresPermissions("suckdata:recordSuck:add")
	String add(){
	    return "feiniao/schedule/suckdata/recordSuck/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("suckdata:recordSuck:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		RecordSuckDO recordSuck = recordSuckService.get(id);
		model.addAttribute("recordSuck", recordSuck);
	    return "feiniao/schedule/suckdata/recordSuck/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("suckdata:recordSuck:add")
	public R save( RecordSuckDO recordSuck){
		if(recordSuckService.save(recordSuck)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("suckdata:recordSuck:edit")
	public R update( RecordSuckDO recordSuck){
		recordSuckService.update(recordSuck);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("suckdata:recordSuck:remove")
	public R remove( Integer id){
		if(recordSuckService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("suckdata:recordSuck:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] ids){
		recordSuckService.batchRemove(ids);
		return R.ok();
	}
	
}
