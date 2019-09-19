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
import com.yunda.base.feiniao.costreport.bo.Bo_costreportCustCostExt;
import com.yunda.base.feiniao.costreport.domain.CostreportCustCostExtDO;
import com.yunda.base.feiniao.costreport.domain.CostreportOrderCostDO;
import com.yunda.base.feiniao.costreport.domain.ExportCostreportCustCostExtDO;
import com.yunda.base.feiniao.costreport.service.CostreportCustCostExtService;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;
/**
 * 客户报表订单统计/客户拓展支出
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-09-13145432
 */
 
@Controller
@RequestMapping("/costreport/costreportCustCostExt")
public class CostreportCustCostExtController  extends BaseController{
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private CostreportCustCostExtService costreportCustCostExtService;
	
//	@Value("${excel.targetFile.path}")
//	private String target;
//	
//	@Value("${excel.template.path}")
//	private String excelTemplate;
	
	@GetMapping()
	@RequiresPermissions("costreport:costreportCustCostExt:costreportCustCostExt")
	String CostreportCustCostExt(){
	    return "feiniao/costreport/costreportCustCostExt/costreportCustCostExt";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("costreport:costreportCustCostExt:costreportCustCostExt")
	//@MethodLock(key = "request")
	public RspBean<PageUtils> list(HttpServletRequest request, HttpServletResponse response,
								   @ModelAttribute @Validated Bo_costreportCustCostExt boCostreportCustCostExt, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();

			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			// 如果验参报错，返回空值給頁面
			PageUtils pageUtils = new PageUtils(null, 0);
			return success(pageUtils);
		}
		//查询列表数据
		List<CostreportOrderCostDO> costreportCustCostExtList = costreportCustCostExtService.list(boCostreportCustCostExt,"db2");
		int total = costreportCustCostExtService.count(boCostreportCustCostExt,"db2");
		PageUtils pageUtils = new PageUtils(costreportCustCostExtList, total);
		return success(pageUtils);
	}
	
/*	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("costreport:costreportCustCostExt:costreportCustCostExt")
	//@MethodLock(key = "request")
	public RspBean<PageUtils> list2(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
        if(StringUtils.isNoneBlank(query.get("accountDt")+"")){
	       	query.put("accountDtStart", (query.get("accountDt")+"").substring(0, 4)+"-"+(query.get("accountDt")+"").substring(4)+"-01");       
	       	query.put("accountDtEnd", DateUtils.getMonthEnd(query.get("accountDtStart")+""));       
        }
   
		List<CostreportOrderCostDO> costreportCustCostExtList = costreportCustCostExtService.list(query,"db2");
		int total = costreportCustCostExtService.count(query,"db2");
		PageUtils pageUtils = new PageUtils(costreportCustCostExtList, total);
		return success(pageUtils);
	}*/
	
	

/*	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@RequiresPermissions("costreport:costreportCustCostExt:exportExcel")
	public void exportExcel(HttpServletResponse response,HttpServletRequest request,@RequestParam Map<String, Object> params) {
		BufferedInputStream bin = null;
        OutputStream out = null;
		Object obj = request.getSession().getAttribute(CRM_constants.AUTH_USER);
		//查询列表数据
        Query query = new Query(params);
        query.put("offset", 0);
        query.put("limit", 10000);
        if(StringUtils.isNoneBlank(query.get("accountDt")+"")){
	       	query.put("accountDtStart", (query.get("accountDt")+"").substring(0, 4)+"-"+(query.get("accountDt")+"").substring(4)+"-01");       
	       	query.put("accountDtEnd", DateUtils.getMonthEnd(query.get("accountDtStart")+""));       
        }
        List<CostreportOrderCostDO> costreportCustCostExtList = costreportCustCostExtService.list(query,"db2");
		String targetFile = SysConfig.TARGET + "costreportCustCostDetail"+DateUtils.formatHMS(new java.util.Date()).toString()+ ".xlsx";
		File downloadFile = new File(targetFile);
		try {
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
				ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, costreportCustCostExtService.filterCustData(costreportCustCostExtList,"db2"), data, ExportCostreportOrderCostDO.class,
						true, targetFile);
			} else {
				// 模版文件不存在，默认输出
				ExcelUtils.getInstance().exportObjects2Excel(costreportCustCostExtService.filterCustData(costreportCustCostExtList,"db2"), ExportCostreportOrderCostDO.class, targetFile);
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
//				e.printStackTrace();
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
	}
	*/
	
	// 导出支出报表明细
	@RequestMapping("/exportExcel")
	@RequiresPermissions("costreport:costreportCustCostExt:exportExcel")
	public void exportExcel(HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute @Validated Bo_costreportCustCostExt boCostreportCustCostExt, BindingResult bindingResult) {
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
			 List<CostreportOrderCostDO> costreportCustCostExtList  = null;
			
			try {
				costreportCustCostExtList = costreportCustCostExtService.list(boCostreportCustCostExt,"db2");
						//costreportCustRouteIncomeService.getIncomelist(boCostreportCustRouteIncomeList,"db2");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			String targetFile = SysConfig.TARGET + "costreportCustCostExt"+ DateUtils.format(new java.util.Date()) + ".xlsx";
			File downloadFile = new File(targetFile);
	
			try {
				// 按命名规则找模版文件
				File file = new File(SysConfig.TEMPLATE+"costreportCustCostExt.xlsx");
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setCharacterEncoding("utf-8");
				
				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
				response.setHeader(headerKey, headerValue);
				
				List<ExportCostreportCustCostExtDO> result = costreportCustCostExtService.filterData(costreportCustCostExtList);
				if(file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")){
					Map<String, String> data = new HashMap<>();
					// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
					ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, 
							result,data, ExportCostreportCustCostExtDO.class, false, response.getOutputStream());
				} else {
					//模板文件不存在  默认输出
					ExcelUtils.getInstance().exportObjects2Excel(result,ExportCostreportCustCostExtDO.class,response.getOutputStream());
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
	@RequiresPermissions("costreport:costreportCustCostExt:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<CostreportCustCostExtDO>(), CostreportCustCostExtDO.class, targetFile);

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
	@RequiresPermissions("costreport:costreportCustCostExt:importExcel")
	public R importExcel(MultipartFile file) {
		List<CostreportCustCostExtDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, CostreportCustCostExtDO.class, 0, 0);
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
			for (CostreportCustCostExtDO _do : list) {
				costreportCustCostExtService.save(_do,"db2");
			}
		}

		return R.ok();
	}		
	
	@GetMapping("/add")
	@RequiresPermissions("costreport:costreportCustCostExt:add")
	String add(){
	    return "feiniao/costreport/costreportCustCostExt/add";
	}

	@GetMapping("/edit/{recordId}")
	@RequiresPermissions("costreport:costreportCustCostExt:edit")
	String edit(@PathVariable("recordId") Integer recordId,Model model){
		CostreportCustCostExtDO costreportCustCostExt = costreportCustCostExtService.get(recordId,"db2");
		model.addAttribute("costreportCustCostExt", costreportCustCostExt);
	    return "feiniao/costreport/costreportCustCostExt/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("costreport:costreportCustCostExt:add")
	public R save( CostreportCustCostExtDO costreportCustCostExt){
		if(costreportCustCostExtService.save(costreportCustCostExt,"db2")>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("costreport:costreportCustCostExt:edit")
	public R update( CostreportCustCostExtDO costreportCustCostExt){
		costreportCustCostExtService.update(costreportCustCostExt,"db2");
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("costreport:costreportCustCostExt:remove")
	public R remove( Integer recordId){
		if(costreportCustCostExtService.remove(recordId,"db2")>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("costreport:costreportCustCostExt:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] recordIds){
		costreportCustCostExtService.batchRemove(recordIds,"db2");
		return R.ok();
	}
	
}
