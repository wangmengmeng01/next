package com.yunda.base.feiniao.warning.controller;

import java.util.List;
import java.util.Map;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.yunda.base.feiniao.warning.bo.Bo_WarningBranchMobile;
import com.yunda.base.feiniao.warning.domain.WarningBranchMobileDO;
import com.yunda.base.feiniao.warning.service.WarningBranchMobileService;
import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.R;
import com.github.crab2died.ExcelUtils;
import com.yunda.base.system.annotation.SensitiveOperateLog;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;

/**
 * 大客户预警短信--网点手机号信息表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-17132313
 */
 
@Controller
@RequestMapping("/warning/warningBranchMobile")
public class WarningBranchMobileController extends BaseController{
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private WarningBranchMobileService warningBranchMobileService;
	
	@GetMapping()
	@RequiresPermissions("warning:warningBranchMobile:warningBranchMobile")
	String WarningBranchMobile(){
	    return "feiniao/warning/warningBranchMobile/warningBranchMobile";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@MethodLock(key = "request")
	@SensitiveOperateLog(value = "预警短信网点手机号信息表",type = "查询")
	@RequiresPermissions("warning:warningBranchMobile:warningBranchMobile")
	public RspBean<PageUtils> list(HttpServletRequest request, HttpServletResponse response,
		@ModelAttribute @Validated Bo_WarningBranchMobile boInterface, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			notifyPage(bindingResult.getFieldError().getDefaultMessage());
			// 如果验参报错，返回空值给页面
			return success(new PageUtils());
		}
		//UserDO loginUser = getUser(request);
		
		List<WarningBranchMobileDO> warningBranchMobileList = warningBranchMobileService.list(boInterface);
		//int total = warningBranchMobileList.size();
		int total = warningBranchMobileService.count(boInterface);
		PageUtils pageUtils = new PageUtils(warningBranchMobileList, total);
		return success(pageUtils);
	}

	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@SensitiveOperateLog(value ="预警短信网点手机号信息表",type="导出")
	@RequiresPermissions("warning:warningBranchMobile:exportExcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute @Validated Bo_WarningBranchMobile boInterface, BindingResult bindingResult) {
		if(SysConfig.DAOCHU.equals("false")){
			return;
		}else if(SysConfig.DAOCHU.equals("true")){
			if(bindingResult.hasErrors()){
				FieldError fieldError = bindingResult.getFieldError();
				notifyPage(fieldError.getField() + ":" +fieldError.getDefaultMessage());
				return;
			}
		
		boInterface.setLimit(0);
		boInterface.setOffset(10000);
		List<WarningBranchMobileDO> warningBranchMobileList = warningBranchMobileService.list(boInterface);
		String targetFile = SysConfig.TARGET + UUID.randomUUID().toString() + ".xlsx";
		File downloadFile = new File(targetFile);
		
		try {
			File file = new File(SysConfig.TEMPLATE+"mobile.xlsx");
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			
			// set headers for the response
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
			response.setHeader(headerKey, headerValue);
			
			//List<ExportWarningBranchMobileDO> result = warningBranchMobileService.filterData(warningBranchMobileList);
			
			if(file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")){
				Map<String, String> data = new HashMap<>();
				// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
				//ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, result,data,
				//		ExportWarningBranchMobileDO.class, false, response.getOutputStream());
			} else {
				//模板文件不存在  默认输出
				//ExcelUtils.getInstance().exportObjects2Excel(result,ExportWarningBranchMobileDO.class,response.getOutputStream());
			}
			/*ExcelUtils.getInstance().exportObjects2Excel(result, ExportWarningBranchMobileDO.class, targetFile);
			写入response
			File downloadFile = new File(targetFile);
			FileUtil.downloadByResponse(response, downloadFile);
			downloadFile.delete();*/
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		}
	}

	// 下载excel模版
	@RequestMapping("/downTemplate")
	@MethodLock(key = "downTemplate")
	// 下载模版和导入共用同一个权限
	@RequiresPermissions("warning:warningBranchMobile:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<WarningBranchMobileDO>(), WarningBranchMobileDO.class, targetFile);

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
	@RequiresPermissions("warning:warningBranchMobile:importExcel")
	public R importExcel(MultipartFile file) {
		List<WarningBranchMobileDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, WarningBranchMobileDO.class, 0, 0);
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
			for (WarningBranchMobileDO _do : list) {
				warningBranchMobileService.save(_do);
			}
		}

		return R.ok();
	}		
	
	@GetMapping("/add")
	@RequiresPermissions("warning:warningBranchMobile:add")
	String add(){
	    return "feiniao/warning/warningBranchMobile/add";
	}

	@GetMapping("/edit/{orgid}")
	@RequiresPermissions("warning:warningBranchMobile:edit")
	String edit(@PathVariable("orgid") Integer orgid,Model model){
		WarningBranchMobileDO warningBranchMobile = warningBranchMobileService.get(orgid);
		model.addAttribute("warningBranchMobile", warningBranchMobile);
	    return "feiniao/warning/warningBranchMobile/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("warning:warningBranchMobile:add")
	public R save( WarningBranchMobileDO warningBranchMobile){
		if(warningBranchMobileService.save(warningBranchMobile)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("warning:warningBranchMobile:edit")
	public R update( WarningBranchMobileDO warningBranchMobile){
		warningBranchMobileService.update(warningBranchMobile);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("warning:warningBranchMobile:remove")
	public R remove( Integer orgid){
		if(warningBranchMobileService.remove(orgid)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("warning:warningBranchMobile:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] orgids){
		warningBranchMobileService.batchRemove(orgids);
		return R.ok();
	}
	
}
