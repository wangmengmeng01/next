package com.yunda.base.feiniao.costreport.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

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
import com.yunda.base.feiniao.costreport.bo.Bo_CostreportCustCostFinish;
import com.yunda.base.feiniao.costreport.bo.Bo_CostreportOrderCostFinish;
import com.yunda.base.feiniao.costreport.bo.Bo_costreportOrderCostFinish_two;
import com.yunda.base.feiniao.costreport.domain.CostreportOrderCostFinishDO;
import com.yunda.base.feiniao.costreport.domain.ExportCostreportOrderCostFinishDO;
import com.yunda.base.feiniao.costreport.service.CostreportOrderCostFinishService;
import com.yunda.base.system.annotation.SensitiveOperateLog;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;
/**
 * 客户报表订单统计/客户收益报表(完成统计)
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-09-14092455
 */
 
@Controller
@RequestMapping("/costreport/costreportOrderCostFinish")
public class CostreportOrderCostFinishController extends BaseController{
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private CostreportOrderCostFinishService costreportOrderCostFinishService;

//	@Value("${excel.targetFile.path}")
//	private String target;
//	
//	@Value("${excel.template.path}")
//	private String excelTemplate;	
	
	@GetMapping()
	@RequiresPermissions("costreport:costreportOrderCostFinish:costreportOrderCostFinish")
	String CostreportOrderCostFinish(){
	    return "feiniao/costreport/costreportOrderCostFinish/costreportOrderCostFinish";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@SensitiveOperateLog(value = "量本利表",type = "查询")
	@RequiresPermissions("costreport:costreportOrderCostFinish:costreportOrderCostFinish")
	@MethodLock(key = "request")
	public RspBean<PageUtils> list(HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute @Validated Bo_costreportOrderCostFinish_two boCostreportOrderCostFinishTwo, BindingResult bindingResult){	
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();

			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			// 如果验参报错，返回空值給頁面
			PageUtils pageUtils = new PageUtils(null, 0);
			return success(pageUtils);
		}
		UserDO loginUser = getUser(request);
		
		//查询列表数据
       // Query query = new Query(params);
		List<CostreportOrderCostFinishDO> costreportOrderCostFinishList = costreportOrderCostFinishService.list(boCostreportOrderCostFinishTwo,loginUser,"db2");
		int total = costreportOrderCostFinishService.count(boCostreportOrderCostFinishTwo,loginUser,"db2");
		PageUtils pageUtils = new PageUtils(costreportOrderCostFinishList, total);
		return success(pageUtils);
	}
	
/*	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("costreport:costreportOrderCostFinish:costreportOrderCostFinish")
	@MethodLock(key = "request")
	public RspBean<PageUtils> list(HttpServletRequest request,@RequestParam Map<String, Object> params){	
		Object obj = request.getSession().getAttribute(CRM_constants.AUTH_USER);
		UserDO loginUser =new UserDO();
		if (null == obj){
		} else {
			loginUser = (UserDO)obj;
		}
		
		//查询列表数据
        Query query = new Query(params);
		List<CostreportOrderCostFinishDO> costreportOrderCostFinishList = costreportOrderCostFinishService.list(query,loginUser,"db2");
		int total = costreportOrderCostFinishService.count(query,"db2");
		PageUtils pageUtils = new PageUtils(costreportOrderCostFinishList, total);
		return success(pageUtils);
	}*/
	
	/**
	 * 
	 * 查询客户信息前端查询条件。
	 * 
	 * {@link Map}
	 * @param request
	 * @param params
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年9月20日
	 */
	@ResponseBody
	@PostMapping("/searchCustomerData")
	@RequiresPermissions("costreport:costreportOrderCostFinish:costreportOrderCostFinish")
	public String searchCustomerData(HttpServletRequest request,@RequestParam Map<String, Object> params){
		UserDO loginUser = getUser(request);
        List<Map<String, Object>> costreportOrderCostFinishList = costreportOrderCostFinishService.searchCustomerData(params,loginUser,"db2");
		String jsonStr = JSONArray.fromObject(costreportOrderCostFinishList).toString();
		return jsonStr;
	}
	
	/**
	 * 
	 * 查询网点信息前端查询条件。
	 * 
	 * @param request
	 * @param params
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年9月20日
	 */
	@ResponseBody
	@PostMapping("/searchCustBraData")
	@RequiresPermissions("costreport:costreportOrderCostFinish:costreportOrderCostFinish")
	public String searchCustBraData(HttpServletRequest request,@RequestParam Map<String, Object> params){
		UserDO loginUser = getUser(request);
        List<Map<String, Object>> costreportOrderCostFinishList = costreportOrderCostFinishService.searchCustomerBraData(params,loginUser,"db2");
		String jsonStr = JSONArray.fromObject(costreportOrderCostFinishList).toString();
		return jsonStr;
	}
	
/*	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@RequiresPermissions("costreport:costreportOrderCostFinish:exportExcel")
	public void exportExcel(HttpServletResponse response,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		  BufferedInputStream bin = null;
	        OutputStream out = null;
			Object obj = request.getSession().getAttribute(CRM_constants.AUTH_USER);
			UserDO loginUser =new UserDO();
			if (null == obj){
			} else {
				loginUser = (UserDO)obj;
			}
			//查询列表数据
	        Query query = new Query(params);
	        query.put("offset", 0);
	        query.put("limit", 10000);

			List<CostreportOrderCostFinishDO> costreportOrderCostFinishList = costreportOrderCostFinishService.list(query,loginUser,"db2");

//			List<ReportTotaldataDO> reportTotaldataList = reportTotaldataService.searchData(query);
			String targetFile = SysConfig.TARGET + "量本利汇总报表"+DateUtils.formatHMS(new java.util.Date()).toString()+ ".xlsx";
			File downloadFile = new File(targetFile);

			try {
//					ExcelUtils.getInstance().exportObjects2Excel(result, ReportTotaldataDO.class, targetFile);
				// 按命名规则找模版文件
				File file = new File(SysConfig.TEMPLATE+"demo.xlsx");
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setCharacterEncoding("utf-8");

				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes("utf-8"), "iso8859-1"));
				response.setHeader(headerKey, headerValue);
		
				if (file.exists()) {
					Map<String, String> data = new HashMap<>();
					// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
					ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, costreportOrderCostFinishService.filterCustData(costreportOrderCostFinishList,"db2"), data, ExportCostreportOrderCostFinishDO.class,
							true, targetFile);
				} else {
					// 模版文件不存在，默认输出
					ExcelUtils.getInstance().exportObjects2Excel(costreportOrderCostFinishService.filterCustData(costreportOrderCostFinishList,"db2"), ExportCostreportOrderCostFinishDO.class, targetFile);
				}
				
				
				// 写入response
				try {
					InputStream myStream = new FileInputStream(targetFile);
					 bin = new BufferedInputStream(myStream);
			           out = response.getOutputStream();
			            int size = 0;
			            byte[] buf = new byte[1024];
			            while ((size = bin.read(buf)) != -1) {
			                out.write(buf, 0, size);
			            }
				
				} catch (IOException e) {
//					e.printStackTrace();
					log.error(e.getMessage(), e);
				}finally {
		            try {
		                bin.close();
		                out.close(); 
		            } catch (IOException e) {
		                log.warn(e.getMessage(),e);;
		            }
		        }	
			} catch (Exception e) {
				//e.printStackTrace();
				log.error(e.getMessage(), e);
			}
	}*/
	
	
	// 导出量本利首页数据
	@RequestMapping("/exportExcel")
	@SensitiveOperateLog(value = "量本利表",type = "导出")
	@RequiresPermissions("costreport:costreportOrderCostFinish:exportExcel")
	public void exportExcel(HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute @Validated Bo_costreportOrderCostFinish_two boCostreportOrderCostFinishTwo, BindingResult bindingResult) {
		UserDO loginUser = getUser(request);
		//导出功能是否开放  true表示开放
		if (SysConfig.DAOCHU.equals("false")) {
			return;
		} else if (SysConfig.DAOCHU.equals("true")) {
			if (bindingResult.hasErrors()) {
				FieldError fieldError = bindingResult.getFieldError();

				notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
				// 如果验参报错，不往下走
				return;
			}
			// 导出上限保护  因为每页查询数据有首行合计，所以导出只导出当前页面看到的数据，即查询参数和导出参数完全一致 
			//boCostreportCustCostFinishTwo.setLimit(10000);
			//查询列表数据
			List<CostreportOrderCostFinishDO> costreportOrderCostFinishList  = null;
			
			try {
				costreportOrderCostFinishList = costreportOrderCostFinishService.list(boCostreportOrderCostFinishTwo,loginUser,"db2");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			String targetFile = SysConfig.TARGET + "costreportOrderCostFinish"+ DateUtils.format(new java.util.Date()) + ".xlsx";
			File downloadFile = new File(targetFile);
	
			try {
				// 按命名规则找模版文件
				File file = new File(SysConfig.TEMPLATE+"costreportOrderCostFinish.xlsx");
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setCharacterEncoding("utf-8");
				
				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
				response.setHeader(headerKey, headerValue);
				
				List<ExportCostreportOrderCostFinishDO> result = costreportOrderCostFinishService.filterData(costreportOrderCostFinishList);
				if(file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")){
					Map<String, String> data = new HashMap<>();
					// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
					ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, 
							result,data, ExportCostreportOrderCostFinishDO.class, false, response.getOutputStream());
				} else {
					//模板文件不存在  默认输出
					ExcelUtils.getInstance().exportObjects2Excel(result,ExportCostreportOrderCostFinishDO.class,response.getOutputStream());
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	// 下载excel模版
	@RequestMapping("/downTemplate")
	@MethodLock(key = "downTemplate")
	// 下载模版和导入共用同一个权限
	@RequiresPermissions("costreport:costreportOrderCostFinish:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<CostreportOrderCostFinishDO>(), CostreportOrderCostFinishDO.class, targetFile);

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
	@RequiresPermissions("costreport:costreportOrderCostFinish:importExcel")
	public R importExcel(MultipartFile file) {
		List<CostreportOrderCostFinishDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, CostreportOrderCostFinishDO.class, 0, 0);
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
			for (CostreportOrderCostFinishDO _do : list) {
				costreportOrderCostFinishService.save(_do,"db2");
			}
		}

		return R.ok();
	}	
	
	
	
	@GetMapping("/reportProvinceExpenditure/{customerId}/{accountDt}")
	@RequiresPermissions("costreport:costreportOrderCostFinish:costreportOrderCostFinish")
	String ReportProvinceExpenditure(@PathVariable("customerId") String customerId
			,@PathVariable("accountDt") String accountDt,Model model){
		Bo_CostreportOrderCostFinish costreportOrderCostFinish = new Bo_CostreportOrderCostFinish();

		costreportOrderCostFinish.setAccountDt(accountDt);
		costreportOrderCostFinish.setCustomerId(customerId);
		model.addAttribute("costreportCustCostFinish", costreportOrderCostFinish);
	    return "feiniao/costreport/costreportCustCostFinish/costreportCustCostFinish";

	}

	@GetMapping("/reportProvinceExpenditureDetail/{customerId}/{accountDt}/{customerSourceType}/{branchCode}/{startProvinceId}/{endProvinceId}")
	@RequiresPermissions("costreport:costreportOrderCostFinish:costreportOrderCostFinish")
	String ReportProvinceExpenditureDetail(@PathVariable("customerId") String customerId
			,@PathVariable("accountDt") String accountDt,@PathVariable("customerSourceType") String customerSourceType
			,@PathVariable("branchCode") String branchCode,@PathVariable("startProvinceId") String startProvinceId,@PathVariable("endProvinceId") String endProvinceId,Model model){
		Bo_CostreportCustCostFinish costreportCustCostFinish = new Bo_CostreportCustCostFinish();

		costreportCustCostFinish.setAccountDt(accountDt);
		costreportCustCostFinish.setCustomerId(customerId);
		costreportCustCostFinish.setCustomerSourceType(customerSourceType);
		costreportCustCostFinish.setBranchCode(branchCode);
		costreportCustCostFinish.setEndProvinceId(Integer.parseInt(endProvinceId));
		costreportCustCostFinish.setStartProvinceId(Integer.parseInt(startProvinceId));
		model.addAttribute("costreportCustCostDetailFinish", costreportCustCostFinish);
	    return "feiniao/costreport/costreportCustCostExt/costreportCustCostExt";

	}
	
	
	@GetMapping("/add")
	@RequiresPermissions("costreport:costreportOrderCostFinish:add")
	String add(){
	    return "feiniao/costreport/costreportOrderCostFinish/add";
	}

	@GetMapping("/edit/{recordId}")
	@RequiresPermissions("costreport:costreportOrderCostFinish:edit")
	String edit(@PathVariable("recordId") Integer recordId,Model model){
		CostreportOrderCostFinishDO costreportOrderCostFinish = costreportOrderCostFinishService.get(recordId,"db2");
		model.addAttribute("costreportOrderCostFinish", costreportOrderCostFinish);
	    return "feiniao/costreport/costreportOrderCostFinish/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("costreport:costreportOrderCostFinish:add")
	public R save( CostreportOrderCostFinishDO costreportOrderCostFinish){
		if(costreportOrderCostFinishService.save(costreportOrderCostFinish,"db2")>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("costreport:costreportOrderCostFinish:edit")
	public R update( CostreportOrderCostFinishDO costreportOrderCostFinish){
		costreportOrderCostFinishService.update(costreportOrderCostFinish,"db2");
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("costreport:costreportOrderCostFinish:remove")
	public R remove( Integer recordId){
		if(costreportOrderCostFinishService.remove(recordId,"db2")>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("costreport:costreportOrderCostFinish:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] recordIds){
		costreportOrderCostFinishService.batchRemove(recordIds,"db2");
		return R.ok();
	}
	
}
