package com.yunda.base.feiniao.warning.controller;

import java.util.List;
import java.util.Map;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.yunda.base.feiniao.warning.bo.Bo_warningBenchmark;
import com.yunda.base.feiniao.warning.domain.ExportWarningBenchmarkDO;
import com.yunda.base.feiniao.warning.domain.WarningBenchmarkDO;
import com.yunda.base.feiniao.warning.service.WarningBenchmarkService;
import com.yunda.base.feiniao.report.core.CRM_constants;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.R;
import com.github.crab2died.ExcelUtils;
import com.yunda.base.common.controller.BaseController;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.*;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;

import io.swagger.annotations.ApiOperation;
/**
 * 预警基准设置
 * 【发短信基准】及【预警处理基准】
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-03-06143800
 */
 
@Controller
@RequestMapping("/warning/warningBenchmark")
public class WarningBenchmarkController extends BaseController{
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private WarningBenchmarkService warningBenchmarkService;
	
	@GetMapping()
	@RequiresPermissions("warning:warningBenchmark:warningBenchmark")
	String WarningBenchmark(){
	    return "feiniao/warning/warningBenchmark/warningBenchmark";
	}
	
	@ApiOperation(value = "获取预警基准设置表")
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("warning:warningBenchmark:warningBenchmark")
	@MethodLock(key = "request")
	public RspBean<PageUtils> list(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute @Validated Bo_warningBenchmark boInterface, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();

			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			// 如果验参报错，返回空值给页面
			PageUtils pageUtils = new PageUtils(null, 0);
			return success(pageUtils);
		}
		//获取用户
		UserDO loginUser = getUser(request);
			
		List<WarningBenchmarkDO> warningBenchmarkList = warningBenchmarkService.list(boInterface, loginUser);
		int total = warningBenchmarkService.count(boInterface, loginUser);
		PageUtils pageUtils = new PageUtils(warningBenchmarkList, total);
		return success(pageUtils);
	}

	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportwarningBenchmarkExcel")
	@RequiresPermissions("warning:warningBenchmark:exportExcel")
	public void exportExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute @Validated Bo_warningBenchmark boInterface) {
		if (SysConfig.DAOCHU.equals("false")) {
			return;
		} else if (SysConfig.DAOCHU.equals("true")) {
			BufferedInputStream bin = null;
	        OutputStream out = null;
			//获取用户
			UserDO loginUser = getUser(request);
			
			boInterface.setLimit(10000);//设置导出保护上限
			boInterface.setOffSet(0);
			List<WarningBenchmarkDO> WarningBenchmarklist = warningBenchmarkService.list(boInterface, loginUser);
			/*String targetFile = SysConfig.TARGET + UUID.randomUUID().toString() + ".xlsx";
			File downloadFile = new File(targetFile);*/
			
			try {
				// 按命名规则找模版文件
				/*File file = new File(SysConfig.TEMPLATE+"warning.xlsx");
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setCharacterEncoding("utf-8");
				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes("utf-8"), "iso8859-1"));
				response.setHeader(headerKey, headerValue);
				*/
				//这个是导出的DO   order=1是序号   如果没有模板  则title = "客户名称"作为表头
				//filterData方法起到整理数据的作用    从list中筛选需要导出的数据result
				List<ExportWarningBenchmarkDO> result = warningBenchmarkService.filterData(WarningBenchmarklist);
				/*if(file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")){
					//前端界面喂参  控制是否使用模板
					Map<String, String> data = new HashMap<>();
					// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
					//ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, 
					//		result,data, ExportXXXXDO.class, false, response.getOutputStream());
				} else {*/
					//模板文件不存在  默认输出
					ExcelUtils.getInstance().exportObjects2Excel(result,ExportWarningBenchmarkDO.class,response.getOutputStream());
				//}
			} catch (Exception e) {
				//e.printStackTrace();
				log.error(e.getMessage(), e);
			}
		}
	}

	// 下载excel模版
	@RequestMapping("/downTemplate")
	@MethodLock(key = "downTemplate")
	// 下载模版和导入共用同一个权限
	@RequiresPermissions("warning:warningBenchmark:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<WarningBenchmarkDO>(), WarningBenchmarkDO.class, targetFile);

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
	@RequiresPermissions("warning:warningBenchmark:importExcel")
	public R importExcel(MultipartFile file) {
		List<WarningBenchmarkDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, WarningBenchmarkDO.class, 0, 0);
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
			for (WarningBenchmarkDO _do : list) {
				warningBenchmarkService.save(_do);
			}
		}

		return R.ok();
	}		
	
	@GetMapping("/add")
	@RequiresPermissions("warning:warningBenchmark:add")
	String add(){
	    return "feiniao/warning/warningBenchmark/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("warning:warningBenchmark:edit")
	String edit(@PathVariable("id") Long id,Model model){
		WarningBenchmarkDO warningBenchmark = warningBenchmarkService.get(id);
		model.addAttribute("warningBenchmark", warningBenchmark);
	    return "feiniao/warning/warningBenchmark/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("warning:warningBenchmark:add")
	public R save(@ModelAttribute @Validated WarningBenchmarkDO warningBenchmark,BindingResult bindingResult){
		if(bindingResult.hasErrors()){
			notifyPage(bindingResult.getFieldError().getDefaultMessage());
			return R.error();
		}
		Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();

        UserDO userDo = (UserDO) session.getAttribute(CRM_constants.AUTH_USER);
        warningBenchmark.setUpdateName(userDo.getName());
        
		if(warningBenchmarkService.save(warningBenchmark)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("warning:warningBenchmark:edit")
	public R update(@ModelAttribute @Validated WarningBenchmarkDO warningBenchmark, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			notifyPage(bindingResult.getFieldError().getDefaultMessage());
			// 如果验参报错，返回空值給頁面
			return R.error();
		}
		
		Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();

        UserDO userDo = (UserDO) session.getAttribute(CRM_constants.AUTH_USER);
        warningBenchmark.setUpdateName(userDo.getName());
        
		warningBenchmarkService.update(warningBenchmark);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("warning:warningBenchmark:remove")
	public R remove( Long id){
		if(warningBenchmarkService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("warning:warningBenchmark:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		warningBenchmarkService.batchRemove(ids);
		return R.ok();
	}
	
}