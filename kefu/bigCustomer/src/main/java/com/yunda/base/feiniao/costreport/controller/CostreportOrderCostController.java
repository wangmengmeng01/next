package com.yunda.base.feiniao.costreport.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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
import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.R;
import com.yunda.base.feiniao.costreport.domain.CostreportOrderCostDO;
import com.yunda.base.feiniao.costreport.service.CostreportOrderCostService;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;

/**
 * 客户报表订单统计/客户单票成本
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-09-14092504
 */
 
@Controller
@RequestMapping("/costreport/costreportOrderCost")
public class CostreportOrderCostController  extends BaseController{
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private CostreportOrderCostService costreportOrderCostService;
	
	@GetMapping()
	@RequiresPermissions("costreport:costreportOrderCost:costreportOrderCost")
	String CostreportOrderCost(){
	    return "feiniao/costreport/costreportOrderCost/costreportOrderCost";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("costreport:costreportOrderCost:costreportOrderCost")
	@MethodLock(key = "request")
	public RspBean<PageUtils> list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
        List a = new LinkedList();
		List<CostreportOrderCostDO> costreportOrderCostList = costreportOrderCostService.list(query,"db2");
		int total = costreportOrderCostService.count(query,"db2");
		PageUtils pageUtils = new PageUtils(costreportOrderCostList, total);
		return success(pageUtils);
	}
	
//	String queryString = request.getParameter("q");
//	CIPHttpSession session = CIPSessionManager.getSession(request, response);
//	CIPUser user = session.getAttribute(CIPRuntimeConstants.LOGIN_USER, CIPUser.class);
//	//String provinceID = request.getParameter("province_id");
//	if(queryString != null || !"".equals(queryString)) {
//		msg.errorCode = CIPErrorCode.CALL_SUCCESS.code;
//		msg.msg = CIPErrorCode.CALL_SUCCESS.name;
//		List<Map<String, Object>> vos = dataService.searchCustomerData(queryString,user);
//		msg.data = vos;
//	}


	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@RequiresPermissions("costreport:costreportOrderCost:exportExcel")
	public void exportExcel(HttpServletResponse response) {
		Map<String, Object> params = new HashMap<>(16);
		params.put("limit", "10000");// 上限保护
		Query query = new Query(params);

		//int nums = costreportOrderCostService.count(query);

		List<CostreportOrderCostDO> result = costreportOrderCostService.list(query,"db2");
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";

		try {
			ExcelUtils.getInstance().exportObjects2Excel(result, CostreportOrderCostDO.class, targetFile);

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
	@RequiresPermissions("costreport:costreportOrderCost:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<CostreportOrderCostDO>(), CostreportOrderCostDO.class, targetFile);

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
	@RequiresPermissions("costreport:costreportOrderCost:importExcel")
	public R importExcel(MultipartFile file) {
		List<CostreportOrderCostDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, CostreportOrderCostDO.class, 0, 0);
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
			for (CostreportOrderCostDO _do : list) {
				costreportOrderCostService.save(_do,"db2");
			}
		}

		return R.ok();
	}		
	
	@GetMapping("/add")
	@RequiresPermissions("costreport:costreportOrderCost:add")
	String add(){
	    return "feiniao/costreport/costreportOrderCost/add";
	}

	@GetMapping("/edit/{recordId}")
	@RequiresPermissions("costreport:costreportOrderCost:edit")
	String edit(@PathVariable("recordId") Integer recordId,Model model){
		CostreportOrderCostDO costreportOrderCost = costreportOrderCostService.get(recordId,"db2");
		model.addAttribute("costreportOrderCost", costreportOrderCost);
	    return "feiniao/costreport/costreportOrderCost/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("costreport:costreportOrderCost:add")
	public R save( CostreportOrderCostDO costreportOrderCost){
		if(costreportOrderCostService.save(costreportOrderCost,"db2")>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("costreport:costreportOrderCost:edit")
	public R update( CostreportOrderCostDO costreportOrderCost){
		costreportOrderCostService.update(costreportOrderCost,"db2");
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("costreport:costreportOrderCost:remove")
	public R remove( Integer recordId){
		if(costreportOrderCostService.remove(recordId,"db2")>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("costreport:costreportOrderCost:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] recordIds){
		costreportOrderCostService.batchRemove(recordIds,"db2");
		return R.ok();
	}
	
}
