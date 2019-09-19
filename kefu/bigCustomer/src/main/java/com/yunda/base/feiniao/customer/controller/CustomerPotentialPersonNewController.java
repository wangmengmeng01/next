package com.yunda.base.feiniao.customer.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

import com.github.crab2died.ExcelUtils;
import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.R;
import com.yunda.base.feiniao.customer.bo.Bo_CustomerPotentialPersonNew;
import com.yunda.base.feiniao.customer.domain.CustomerPotentialPersonNewDO;
import com.yunda.base.feiniao.customer.domain.ExportExcelCustomerPotentialPersonNewDO;
import com.yunda.base.feiniao.customer.domain.ImportExcelCustomerPotentialPersonNewDO;
import com.yunda.base.feiniao.customer.service.CustomerPotentialPersonNewService;
import com.yunda.base.system.annotation.SensitiveOperateLog;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;

import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
/**
 * 潜在客户新表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-12-14173416
 */
 
@Controller
@RequestMapping("/customer/customerPotentialPersonNew")
public class CustomerPotentialPersonNewController extends BaseController{
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private CustomerPotentialPersonNewService customerPotentialPersonNewService;
	
	@GetMapping()
	@RequiresPermissions("customer:customerPotentialPersonNew:customerPotentialPersonNew")
	String CustomerPotentialPersonNew(){
	    return "feiniao/customer/customerPotentialPersonNew/customerPotentialPersonNew";
	}
	/**
	 * 模糊查询
	 */
	@ResponseBody
	@PostMapping("/fuzzySearchCustomerName")
	@RequiresPermissions("customer:customerPotentialPersonNew:customerPotentialPersonNew")
	public String fuzzySearchCustomerName(HttpServletRequest request,@RequestParam Map<String, Object> params){
        List<Map<String, Object>> customerList = customerPotentialPersonNewService.searchCustomerName();
		String jsonStr = JSONArray.fromObject(customerList).toString();
		return jsonStr;
	}
	
	@ApiOperation(value = "获取潜在客户新表")
	@ResponseBody
	@GetMapping("/list")
	@SensitiveOperateLog(value = "潜在客户新表",type = "查询")
	@RequiresPermissions("customer:customerPotentialPersonNew:customerPotentialPersonNew")
	@MethodLock(key = "request")
	public RspBean<PageUtils> list(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute @Validated Bo_CustomerPotentialPersonNew boCustomerPotentialPersonNew, BindingResult bindingResult){
		//Bo_Interface换成对应的Bo类
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();

			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			// 如果验参报错，返回空值给页面
			PageUtils pageUtils = new PageUtils(null, 0);
			return success(pageUtils);
		}
		/*Object obj = request.getSession().getAttribute(CRM_constants.AUTH_USER);
		UserDO loginUser =new UserDO();
		if(obj == null){
			
		}else{
			loginUser = (UserDO)obj;
		}*/
		UserDO loginUser = getUser(request);
		//log.info("潜在客户新表取到的loginuser"+loginUser);
		//查询列表数据
        //Query query = new Query(params);
		List<CustomerPotentialPersonNewDO> customerPotentialPersonNewList = customerPotentialPersonNewService.list(boCustomerPotentialPersonNew, loginUser);
		int total = customerPotentialPersonNewService.count(boCustomerPotentialPersonNew, loginUser);
		PageUtils pageUtils = new PageUtils(customerPotentialPersonNewList, total);
		return success(pageUtils);
	}

	// 导出excel
	@RequestMapping("/exportExcel")
	@SensitiveOperateLog(value = "潜在客户新表",type = "导出")
	@MethodLock(key = "exportcustomerPotentialPersonNewExcel")
	@RequiresPermissions("customer:customerPotentialPersonNew:exportExcel")
	public void exportExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute @Validated Bo_CustomerPotentialPersonNew boCustomerPotentialPersonNew) {
		if (SysConfig.DAOCHU.equals("false")) {
			return;
		} else if (SysConfig.DAOCHU.equals("true")) {
			BufferedInputStream bin = null;
	        OutputStream out = null;
			//Map<String, Object> params = new HashMap<>(16);
			//params.put("offset", "0");
			//params.put("limit", "10000");// 上限保护
			//Query query = new Query(params);
	        final UserDO loginUser = getUser(request);
			/*Object obj = request.getSession().getAttribute(CRM_constants.AUTH_USER);
			UserDO loginUser =new UserDO();
			if (null == obj){
			} else {
				loginUser = (UserDO)obj;
			}*/

	        boCustomerPotentialPersonNew.setLimit(10000);//上限保护  最多可导出1万条
	        boCustomerPotentialPersonNew.setOffset(0);
	        List<CustomerPotentialPersonNewDO> CustomerPotentialPersonNewlist = customerPotentialPersonNewService.list(boCustomerPotentialPersonNew, loginUser);
			String targetFile = SysConfig.TARGET + UUID.randomUUID().toString() + ".xlsx";
			File downloadFile = new File(targetFile);
			
			try {
				// 按命名规则找模版文件
				//File file = new File(SysConfig.TEMPLATE+"customerPotentialPersonNew.xlsx");
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setCharacterEncoding("utf-8");
				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
				response.setHeader(headerKey, headerValue);
				
				//这个是导出的DO   order=1是序号   如果没有模板  则title = "客户名称"作为表头
				//filterData方法起到整理数据的作用    从list中筛选需要导出的数据result
				List<ExportExcelCustomerPotentialPersonNewDO> result = customerPotentialPersonNewService.filterData(CustomerPotentialPersonNewlist);
				/*if(file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")){
					//前端界面喂参  控制是否使用模板
					Map<String, String> data = new HashMap<>();
					// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
					ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, 
							result,data, ExportXXXXDO.class, false, response.getOutputStream());
				} else {*/
					//模板文件不存在  默认输出
					ExcelUtils.getInstance().exportObjects2Excel(result,ExportExcelCustomerPotentialPersonNewDO.class,response.getOutputStream());
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
	@RequiresPermissions("customer:customerPotentialPersonNew:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.TARGET + UUID.randomUUID().toString() + ".xlsx";
		File downloadFile = new File(targetFile);
		//String targetFile = SysConfig.TEMPLATE + "customerPotentialPersonNew.xlsx";
		try {
			// 按命名规则找模版文件
			File file = new File(SysConfig.TEMPLATE+"customerPotentialPersonNew.xlsx");
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			// set headers for the response
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
			response.setHeader(headerKey, headerValue);
			
			if(file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")){
				//前端界面喂参  控制是否使用模板
				Map<String, String> data = new HashMap<>();
				// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
				ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<ImportExcelCustomerPotentialPersonNewDO>(), ImportExcelCustomerPotentialPersonNewDO.class, targetFile);
			} else {
				ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<ImportExcelCustomerPotentialPersonNewDO>(), ImportExcelCustomerPotentialPersonNewDO.class, targetFile);
			}
			// 写入response
			
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
	@RequiresPermissions("customer:customerPotentialPersonNew:importExcel")
	public R importExcel(HttpServletRequest request,MultipartFile file) {
		//拿到用户信息
		final UserDO loginUser = getUser(request);
		List<CustomerPotentialPersonNewDO> list = null;
		//用这个类接收excel的数据
		//List<ExportExcelCustomerPotentialPersonNewDO> list = null;
		//List<ExportExcelCustomerPotentialPersonNewDO> errorlistExcel = new ArrayList<ExportExcelCustomerPotentialPersonNewDO>();
		List<String> errorlistExcel = new ArrayList<>();
		
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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, CustomerPotentialPersonNewDO.class, 0, 0);
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

		if (list != null && list.size()>0) {
			//CustomerPotentialPersonNewDO row;
			for(int r = 0; r <list.size();r++){
				//row = new CustomerPotentialPersonNewDO();
				CustomerPotentialPersonNewDO row = list.get(r);
				boolean vo= customerPotentialPersonNewService.checkData(row,loginUser);
				if(vo){
				}else{
					int s = r+1;
					errorlistExcel.add("第"+s+"行数据不合法");
				}
			}
			String msg = "";
			if (errorlistExcel != null && errorlistExcel.size()>0) {
				for(int x =0;x <errorlistExcel.size();x++){
					msg +=errorlistExcel.get(x)+";";
				}
			}
			if(!"".equals(msg)){
				return R.error(msg);
			}
			
		}	
		
		for (CustomerPotentialPersonNewDO _do : list) {
			customerPotentialPersonNewService.save(_do, loginUser);
		}
		
		return R.ok();
	}		
	
	@GetMapping("/add")
	@RequiresPermissions("customer:customerPotentialPersonNew:add")
	String add(){
	    return "feiniao/customer/customerPotentialPersonNew/add";
	}

	@GetMapping("/edit/{recordId}")
	@RequiresPermissions("customer:customerPotentialPersonNew:edit")
	String edit(@PathVariable("recordId") Integer recordId,Model model){
		CustomerPotentialPersonNewDO customerPotentialPersonNew = customerPotentialPersonNewService.get(recordId);
		model.addAttribute("customerPotentialPersonNew", customerPotentialPersonNew);
	    return "feiniao/customer/customerPotentialPersonNew/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("customer:customerPotentialPersonNew:add")
	public R save(HttpServletRequest request,HttpServletResponse response, CustomerPotentialPersonNewDO customerPotentialPersonNew){
		String branchcode = customerPotentialPersonNew.getBranchCode();
		//校验网点
		int wd = customerPotentialPersonNewService.countwd(branchcode);
		if(wd!=0){
			final UserDO loginUser = getUser(request);
			
			if(customerPotentialPersonNewService.save(customerPotentialPersonNew,loginUser)>0){
				return R.ok();
			}
		}
		
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("customer:customerPotentialPersonNew:edit")
	public R update(HttpServletRequest request,HttpServletResponse response, CustomerPotentialPersonNewDO customerPotentialPersonNew){
		String branchcode = customerPotentialPersonNew.getBranchCode();
		//校验网点
		int wd = customerPotentialPersonNewService.countwd(branchcode);
		if(wd!=0){
			final UserDO loginUser = getUser(request);
			
			customerPotentialPersonNewService.update(customerPotentialPersonNew,loginUser);
			return R.ok();
		}
		return R.error();
		
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("customer:customerPotentialPersonNew:remove")
	public R remove( Integer recordId){
		if(customerPotentialPersonNewService.remove(recordId)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("customer:customerPotentialPersonNew:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] recordIds){
		customerPotentialPersonNewService.batchRemove(recordIds);
		return R.ok();
	}
	
}
