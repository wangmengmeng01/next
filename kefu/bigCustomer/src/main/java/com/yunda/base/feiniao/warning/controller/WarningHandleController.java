package com.yunda.base.feiniao.warning.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.yunda.base.common.enums.ExportEnum;
import com.yunda.base.common.enums.RespEnum;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.R;
import com.yunda.base.common.utils.SpringUtil;
import com.yunda.base.feiniao.report.utils.JsonFilterUtils;
import com.yunda.base.feiniao.warning.bo.Bo_warningHandleDO;
import com.yunda.base.feiniao.warning.domain.ExportWarningHandleDO;
import com.yunda.base.feiniao.warning.domain.WarningHandleDO;
import com.yunda.base.feiniao.warning.service.WarningHandleService;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.FileExportDO;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.service.FileExportService;
import com.yunda.base.system.service.SessionService;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;
import com.yunda.ydmbspringbootstarter.common.utils.StringUtils;

import io.swagger.annotations.ApiOperation;
/**
 * 预警反馈表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-03-13095948
 */
 
@Controller
@RequestMapping("/warning/warningHandle")
public class WarningHandleController extends BaseController{
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private WarningHandleService warningHandleService;
	@Autowired
	public FileExportService fileExportService;
	@Autowired
	public SessionService sessionService;
	
	@GetMapping()
	@RequiresPermissions("warning:warningHandle:warningHandle")
	String WarningHandle(){
	    return "feiniao/warning/warningHandle/warningHandle";
	}
	
	@ApiOperation(value = "获取预警反馈列表")
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("warning:warningHandle:warningHandle")
	//@MethodLock(key = "warningHandle")
	public RspBean<PageUtils> list(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute @Validated Bo_warningHandleDO boInterface, BindingResult bindingResult){
		//Bo_Interface换成对应的Bo类
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();

			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			// 如果验参报错，返回空值给页面
			PageUtils pageUtils = new PageUtils(null, 0);
			return success(pageUtils);
		}
		//获取用户
		UserDO loginUser = getUser(request);
		
		List<WarningHandleDO> warningHandleList = warningHandleService.list(boInterface, loginUser);
		int total = warningHandleService.count(boInterface, loginUser);
		PageUtils pageUtils = new PageUtils(warningHandleList, total);
		return success(pageUtils);
	}

	// 导出excel
	@ApiOperation(value = "导出excel")
	@RequestMapping("/exportExcel")
	//@MethodLock(key = "exportwarningHandleExcel")
	@RequiresPermissions("warning:warningHandle:exportExcel")
	@ResponseBody
	@SuppressWarnings("all")
	public RspBean exportExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute @Validated Bo_warningHandleDO boInterface, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			// 如果验参报错，不往下走
			return failure(RespEnum.ERROR_BUSINESS_OPERATE.getCode());
		}
		
		UserDO loginUser = getUser(request);
		String filterJson = JsonFilterUtils.filterJson(boInterface);
		String filteruser = JsonFilterUtils.filterJson(loginUser);
		StackTraceElement[] stes = Thread.currentThread().getStackTrace();
		String userId = sessionService.findSession(request.getSession().getId()).getUsername();
		
		FileExportDO fileExportDO = new FileExportDO();
		fileExportDO.setCreateTime(new Date());
		fileExportDO.setExecuteClass(stes[1].getClassName());
		
		if (loginUser.hasPerms(Constant.REPORT_ADMIN_PERMS)) {
			// 超级用户权限 无限制
			// 系统菜单配置了report:admin:allperms权限标识 角色管理中当该用户的角色勾选该权限标识就说明该用户是超级用户
			// 跳到非网点权限页面
			fileExportDO.setExecuteMethod(Constant.exportQueryMethodName);
			fileExportDO.setSpecClass(ExportWarningHandleDO.class.getName());
			fileExportDO.setTitle(Constant.warningHandleTitle);
		}else{
			if(loginUser.isBigareaqx()){//是否有大区权限
				fileExportDO.setExecuteMethod(Constant.exportQueryMethodName);
				fileExportDO.setSpecClass(ExportWarningHandleDO.class.getName());
				fileExportDO.setTitle(Constant.warningHandleTitle);
			}else if(loginUser.isProvinceqx()){//是否有省权限
				fileExportDO.setExecuteMethod(Constant.exportQueryMethodName);
				fileExportDO.setSpecClass(ExportWarningHandleDO.class.getName());
				fileExportDO.setTitle(Constant.warningHandleTitle);
			}else {
				//账号是网点权限,到网点页面； 跳到非网点权限页面
				if(loginUser.getOrgCode() != null & !loginUser.getOrgCode().isEmpty()){
					fileExportDO.setExecuteMethod(Constant.exportQueryMethodName);
					fileExportDO.setSpecClass(ExportWarningHandleDO.class.getName());
					fileExportDO.setTitle(Constant.warningHandleTitle);
				}
			}
		}
		
		fileExportDO.setExecuteParam(filterJson);
		fileExportDO.setState(ExportEnum.StandingBy.getNum());// 等待中
		fileExportDO.setUserId(filteruser);
		List<FileExportDO> repeatData = fileExportService.getRepeatData(filterJson, stes[1].getClassName(), Constant.exportQueryMethodName, filteruser);
		if (!ObjectUtils.isEmpty(repeatData)) {
			for (FileExportDO fileExportdo : repeatData) {
				if(StringUtils.equalsAny(fileExportdo.getState(), ExportEnum.StandingBy.getNum(), ExportEnum.Handing.getNum())){
					return new RspBean(RespEnum.NO_CLICK);
				}
			}
		} // 添加到队列中
		fileExportService.save(fileExportDO);// 开始保存数据的一些基本信息
		return new RspBean(RespEnum.STANDING_BY);
		
			/*BufferedInputStream bin = null;
	        OutputStream out = null;
			//Map<String, Object> params = new HashMap<>(16);
			//params.put("offset", "0");
			//params.put("limit", "10000");// 上限保护
			//Query query = new Query(params);
			//获取用户
			

			List<WarningHandleDO> WarningHandlelist = warningHandleService.list(boInterface, loginUser);
			String targetFile = SysConfig.TARGET + UUID.randomUUID().toString() + ".xlsx";
			File downloadFile = new File(targetFile);
			
			try {
				// 按命名规则找模版文件
				File file = new File(SysConfig.TEMPLATE+"warning.xlsx");
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setCharacterEncoding("utf-8");
				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes("utf-8"), "iso8859-1"));
				response.setHeader(headerKey, headerValue);
				
				//这个是导出的DO   order=1是序号   如果没有模板  则title = "客户名称"作为表头
				//filterData方法起到整理数据的作用    从list中筛选需要导出的数据result
				//List<ExportXXXXDO> result = warningHandleService.filterData(WarningHandlelist);
				if(file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")){
					//前端界面喂参  控制是否使用模板
					Map<String, String> data = new HashMap<>();
					// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
					//ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, 
					//		result,data, ExportXXXXDO.class, false, response.getOutputStream());
				} else {
					//模板文件不存在  默认输出
					//ExcelUtils.getInstance().exportObjects2Excel(result,ExportXXXXDO.class,response.getOutputStream());
				}
			} catch (Exception e) {
				//e.printStackTrace();
				log.error(e.getMessage(), e);
			}*/
	}

	@SuppressWarnings("all")
	public List<ExportWarningHandleDO> exportQuery(String bo_warningHandleDO, UserDO loginUser, String offset){
		if(warningHandleService == null){
			warningHandleService =SpringUtil.getBean(WarningHandleService.class);
		}
		Bo_warningHandleDO boInterface = JSON.parseObject(bo_warningHandleDO,Bo_warningHandleDO.class);
		
		boInterface.setOffset(Integer.valueOf(offset));
		boInterface.setLimit(Integer.valueOf(SysConfig.exportExcelLimit));
		if(!ObjectUtils.isEmpty(boInterface)){
			List<WarningHandleDO> warningHandleList = null;
			try {
				warningHandleList = warningHandleService.list(boInterface, loginUser);
				
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			return warningHandleService.filterData(warningHandleList);
		}
		
		return null;
	}
	
	
	// 下载excel模版
	@RequestMapping("/downTemplate")
	@MethodLock(key = "downTemplate")
	// 下载模版和导入共用同一个权限
	@RequiresPermissions("warning:warningHandle:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<WarningHandleDO>(), WarningHandleDO.class, targetFile);

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
	@RequiresPermissions("warning:warningHandle:importExcel")
	public R importExcel(MultipartFile file) {
		List<WarningHandleDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, WarningHandleDO.class, 0, 0);
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
			for (WarningHandleDO _do : list) {
				warningHandleService.save(_do);
			}
		}

		return R.ok();
	}	
	/**网点反馈
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/wdFeedback/{id}")
	@RequiresPermissions("warning:warningHandle:wdFeedback")
	String wdFeedback(@PathVariable("id") Long id,Model model){
		WarningHandleDO warningHandle = warningHandleService.get(id);
		model.addAttribute("warningHandle", warningHandle);
	    return "feiniao/warning/warningHandle/wdFeedback";
	}
	@ResponseBody
	@RequestMapping("/update/wdFeedback")
	@RequiresPermissions("warning:warningHandle:wdFeedback")
	public R updateWD(HttpServletRequest request, HttpServletResponse response, WarningHandleDO warningHandle){
		
		warningHandle.setFeedbackStatus("C");
		//获取用户
		UserDO loginUser = getUser(request);
		warningHandle.setBarnchDealUser(loginUser.getName()+"("+loginUser.getUsername()+")");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		warningHandle.setShowBranchFeedbackDate(df.format(new Date()));
		warningHandleService.update(warningHandle);
		return R.ok();
	}
	/**省总反馈
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/szFeedback/{id}")
	@RequiresPermissions("warning:warningHandle:szFeedback")
	String szFeedback(@PathVariable("id") Long id,Model model){
		WarningHandleDO warningHandle = warningHandleService.get(id);
		model.addAttribute("warningHandle", warningHandle);
	    return "feiniao/warning/warningHandle/szFeedback";
	}
	@ResponseBody
	@RequestMapping("/update/szFeedback")
	@RequiresPermissions("warning:warningHandle:szFeedback")
	public R updateSZ(HttpServletRequest request, HttpServletResponse response, WarningHandleDO warningHandle){
		
		warningHandle.setFeedbackStatus("D");
		//获取用户
		UserDO loginUser = getUser(request);
		warningHandle.setProvinceDealUser(loginUser.getName()+"("+loginUser.getUsername()+")");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		warningHandle.setShowProvinceFeedbackDate(df.format(new Date()));
		warningHandleService.update(warningHandle);
		return R.ok();
	}
	/**总部反馈
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/zbFeedback/{id}")
	@RequiresPermissions("warning:warningHandle:zbFeedback")
	String zbFeedback(@PathVariable("id") Long id,Model model){
		WarningHandleDO warningHandle = warningHandleService.get(id);
		model.addAttribute("warningHandle", warningHandle);
	    return "feiniao/warning/warningHandle/zbFeedback";
	}
	@ResponseBody
	@RequestMapping("/update/zbFeedback")
	@RequiresPermissions("warning:warningHandle:zbFeedback")
	public R updateZB(HttpServletRequest request, HttpServletResponse response, WarningHandleDO warningHandle){
		
		warningHandle.setFeedbackStatus("E");
		//获取用户
		UserDO loginUser = getUser(request);
		warningHandle.setZbDealUser(loginUser.getName()+"("+loginUser.getUsername()+")");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		warningHandle.setShowZbFeedbackDate(df.format(new Date()));
		warningHandleService.update(warningHandle);
		return R.ok();
	}
	/**
	 * 查看处理记录
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/handleRecord/{id}")
	@RequiresPermissions("warning:warningHandle:handleRecord")
	String getHandleRecord(@PathVariable("id") Long id,Model model){
		WarningHandleDO warningHandle = warningHandleService.get(id);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(warningHandle.getBranchFeedbackDate() != null && !"".equals(warningHandle.getBranchFeedbackDate())){
			warningHandle.setShowBranchFeedbackDate(df.format(warningHandle.getBranchFeedbackDate()));	
		}
		if(warningHandle.getProvinceFeedbackDate() != null && !"".equals(warningHandle.getProvinceFeedbackDate())){
			warningHandle.setShowProvinceFeedbackDate(df.format(warningHandle.getProvinceFeedbackDate()));	
		}
		if(warningHandle.getZbFeedbackDate() != null && !"".equals(warningHandle.getZbFeedbackDate())){
			warningHandle.setShowZbFeedbackDate(df.format(warningHandle.getZbFeedbackDate()));
		}
		
		model.addAttribute("warningHandle", warningHandle);
	    return "feiniao/warning/warningHandle/handleRecord";
	}
	
	@GetMapping("/add")
	@RequiresPermissions("warning:warningHandle:add")
	String add(){
	    return "feiniao/warning/warningHandle/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("warning:warningHandle:edit")
	String edit(@PathVariable("id") Long id,Model model){
		WarningHandleDO warningHandle = warningHandleService.get(id);
		model.addAttribute("warningHandle", warningHandle);
	    return "feiniao/warning/warningHandle/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("warning:warningHandle:add")
	public R save( WarningHandleDO warningHandle){
		if(warningHandleService.save(warningHandle)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("warning:warningHandle:edit")
	public R update( WarningHandleDO warningHandle){
		warningHandleService.update(warningHandle);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("warning:warningHandle:remove")
	public R remove( Long id){
		if(warningHandleService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("warning:warningHandle:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		warningHandleService.batchRemove(ids);
		return R.ok();
	}
	
}
