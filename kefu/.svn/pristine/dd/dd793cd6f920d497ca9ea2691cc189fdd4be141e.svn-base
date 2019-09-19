package com.yunda.base.feiniao.customer.controller;

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
import com.yunda.base.feiniao.customer.domain.CustomerVisitRecordDO;
import com.yunda.base.feiniao.customer.service.CustomerVisitRecordService;
import com.yunda.base.system.config.SysConfig;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;
/**
 * 客户拜访记录表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-10-31144058
 */
 
@Controller
@RequestMapping("/customer/customerVisitRecord")
public class CustomerVisitRecordController {
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private CustomerVisitRecordService customerVisitRecordService;
	
	@GetMapping()
	@RequiresPermissions("customer:customerVisitRecord:customerVisitRecord")
	String CustomerVisitRecord(){
	    return "feiniao/customer/customerVisitRecord/customerVisitRecord";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("customer:customerVisitRecord:customerVisitRecord")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<CustomerVisitRecordDO> customerVisitRecordList = customerVisitRecordService.list(query);
		int total = customerVisitRecordService.count(query);
		PageUtils pageUtils = new PageUtils(customerVisitRecordList, total);
		
		return pageUtils;
	}

	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@RequiresPermissions("customer:customerVisitRecord:exportExcel")
	public void exportExcel(HttpServletResponse response) {
		Map<String, Object> params = new HashMap<>(16);
		params.put("limit", "10000");// 上限保护
		Query query = new Query(params);

		//int nums = customerVisitRecordService.count(query);

		List<CustomerVisitRecordDO> result = customerVisitRecordService.list(query);
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";

		try {
			ExcelUtils.getInstance().exportObjects2Excel(result, CustomerVisitRecordDO.class, targetFile);

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
	@RequiresPermissions("customer:customerVisitRecord:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<CustomerVisitRecordDO>(), CustomerVisitRecordDO.class, targetFile);

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
	@RequiresPermissions("customer:customerVisitRecord:importExcel")
	public R importExcel(MultipartFile file) {
		List<CustomerVisitRecordDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, CustomerVisitRecordDO.class, 0, 0);
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
			for (CustomerVisitRecordDO _do : list) {
				customerVisitRecordService.save(_do);
			}
		}

		return R.ok();
	}		
	
	@GetMapping("/add")
	@RequiresPermissions("customer:customerVisitRecord:add")
	String add(){
	    return "feiniao/customer/customerVisitRecord/add";
	}

	@GetMapping("/edit/{recordId}")
	@RequiresPermissions("customer:customerVisitRecord:edit")
	String edit(@PathVariable("recordId") Integer recordId,Model model){
		CustomerVisitRecordDO customerVisitRecord = customerVisitRecordService.get(recordId);
		model.addAttribute("customerVisitRecord", customerVisitRecord);
	    return "feiniao/customer/customerVisitRecord/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("customer:customerVisitRecord:add")
	public R save( CustomerVisitRecordDO customerVisitRecord){
		if(customerVisitRecordService.save(customerVisitRecord)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("customer:customerVisitRecord:edit")
	public R update( CustomerVisitRecordDO customerVisitRecord){
		customerVisitRecordService.update(customerVisitRecord);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("customer:customerVisitRecord:remove")
	public R remove( Integer recordId){
		if(customerVisitRecordService.remove(recordId)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("customer:customerVisitRecord:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] recordIds){
		customerVisitRecordService.batchRemove(recordIds);
		return R.ok();
	}
	
}
