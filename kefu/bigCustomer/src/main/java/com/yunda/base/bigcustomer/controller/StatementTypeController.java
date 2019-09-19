package com.yunda.base.bigcustomer.controller;

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
import com.yunda.base.bigcustomer.domain.StatementTypeDO;
import com.yunda.base.bigcustomer.service.StatementTypeService;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.R;
import com.yunda.base.system.config.SysConfig;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;


/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-07-24153026
 */
 
@Controller
@RequestMapping("/bigcustomer/statementType")
public class StatementTypeController {
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private StatementTypeService statementTypeService;
	
	@GetMapping()
	@RequiresPermissions("bigcustomer:statementType:statementType")
	String StatementType(){
	    return "bigcustomer/statementType/statementType";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("bigcustomer:statementType:statementType")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<StatementTypeDO> statementTypeList = statementTypeService.list(query);
		int total = statementTypeService.count(query);
		PageUtils pageUtils = new PageUtils(statementTypeList, total);
		return pageUtils;
	}

	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@RequiresPermissions("bigcustomer:statementType:exportExcel")
	public void exportExcel(HttpServletResponse response) {
		Map<String, Object> params = new HashMap<>(16);
		params.put("limit", "10000");// 上限保护
		Query query = new Query(params);

		//int nums = statementTypeService.count(query);

		List<StatementTypeDO> result = statementTypeService.list(query);
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";

		try {
			ExcelUtils.getInstance().exportObjects2Excel(result, StatementTypeDO.class, targetFile);

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
	@RequiresPermissions("bigcustomer:statementType:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<StatementTypeDO>(), StatementTypeDO.class, targetFile);

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
	@RequiresPermissions("bigcustomer:statementType:importExcel")
	public R importExcel(MultipartFile file) {
		List<StatementTypeDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, StatementTypeDO.class, 0, 0);
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
			for (StatementTypeDO _do : list) {
				statementTypeService.save(_do);
			}
		}

		return R.ok();
	}		
	
	@GetMapping("/add")
	@RequiresPermissions("bigcustomer:statementType:add")
	String add(){ return "bigcustomer/statementType/add"; }

	@GetMapping("/edit/{id}")
	@RequiresPermissions("bigcustomer:statementType:edit")
	String edit(@PathVariable("id") Long id,Model model){
		StatementTypeDO statementType = statementTypeService.get(id);
		model.addAttribute("statementType", statementType);
	    return "bigcustomer/statementType/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("bigcustomer:statementType:add")
	public R save( StatementTypeDO statementType){
		Map<String,Object> params = new HashMap<>();
		params.put("consultType",statementType.getConsultType());
		params.put("statementResult",statementType.getStatementResult());
		int count = statementTypeService.count(params);
		//int count = statementTypeService.countStatement(statementType);
		if(count>0){
			return R.error("该咨询类型和结单结果关系已存在,请不要重复维护");
		}
		statementType.setStatus("1");//新增默认启用
		if(statementTypeService.save(statementType)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("bigcustomer:statementType:edit")
	public R update( StatementTypeDO statementType){
		Map<String,Object> params = new HashMap<>();
		params.put("consultType",statementType.getConsultType());
		params.put("statementResult",statementType.getStatementResult());
		params.put("status",statementType.getStatus());
		int count = statementTypeService.count(params);
		if(count>0){
			return R.error("该咨询类型和结单结果关系已存在,请不要重复维护");
		}
		//statementType.setStatus("1");
		statementTypeService.update(statementType);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("bigcustomer:statementType:remove")
	public R remove( Long id){
		if(statementTypeService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("bigcustomer:statementType:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		statementTypeService.batchRemove(ids);
		return R.ok();
	}

	/**
	 * 从咨询类型和结单结果关系设置表
	 * 获取结单结果list
	 * @param consultType
	 * @return/{consultType}
	 */
	@PostMapping("/jiedanList")
	@ResponseBody
	public List<String> jiedanList(StatementTypeDO statementType){
		//@PathVariable String consultType
		//List<String> table_list = new ArrayList<>();
		//获取区的名称
		/*List<String> table_list = userService.getBigareaData();
		//获取业务省名称
		List<String> provinceData = userService.getProvinceData();
		table_list.addAll(provinceData);*/

		String consultType = statementType.getConsultType();

		List<String> jiedanList = statementTypeService.getJiedanList(consultType);

		return jiedanList;
	}
	
}
