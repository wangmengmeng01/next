package com.yunda.base.feiniao.costreport.controller;

import io.swagger.annotations.ApiOperation;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.R;
import com.yunda.base.feiniao.costreport.domain.CostreportOrderCostChangeDO;
import com.yunda.base.feiniao.costreport.service.CostreportOrderCostChangeService;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;
/**
 * 客户报表订单统计/客户月结账单
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-11-09140854
 */
 
@Controller
@RequestMapping("/costreport/costreportOrderCostChange")
public class CostreportOrderCostChangeController  extends BaseController{
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private CostreportOrderCostChangeService costreportOrderCostChangeService;
	@Autowired
	private RedisTemplate redisTemplate;
	
	@GetMapping()
	@RequiresPermissions("costreport:costreportOrderCostChange:costreportOrderCostChange")
	String CostreportOrderCostChange(){
	    return "feiniao/costreport/costreportOrderCostChange/costreportOrderCostChange";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("costreport:costreportOrderCostChange:costreportOrderCostChange")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<CostreportOrderCostChangeDO> costreportOrderCostChangeList = costreportOrderCostChangeService.list(query);
		int total = costreportOrderCostChangeService.count(query);
		PageUtils pageUtils = new PageUtils(costreportOrderCostChangeList, total);
		return pageUtils;
	}


	@ApiOperation(value = "中转费计算")
	@ResponseBody
	@GetMapping("/getData")
	@RequiresPermissions("costreport:costreportOrderCostChange:costreportOrderCostChange")
	@MethodLock(key="request")
	public RspBean<String> searchData(HttpServletRequest request, HttpServletResponse response,@ModelAttribute @Validated CostreportOrderCostChangeDO costreportOrderCostChangeDO ,BindingResult bindingResult,@RequestParam Map<String, Object> params){
		
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			
			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			//如果验参报错，返回空值给页面
//			PageUtils pageUtils = new PageUtils(null, 0);
			return success(null);			
		}
		UserDO loginUser = getUser(request);
		//查询列表数据
//        Query query = new Query(params);
//		RedisCacheManager manager = RedisCacheManager.getInstance();
		ValueOperations<String, Object> operations = redisTemplate.opsForValue();
		operations.set(loginUser.getUserId()+"destination_province_id", costreportOrderCostChangeDO.getDestinationProvinceId(), 86400, TimeUnit.SECONDS);
		operations.set(loginUser.getUserId()+"calculate_order_sum", costreportOrderCostChangeDO.getCalculateOrderSum()+"", 86400, TimeUnit.SECONDS);
		operations.set(loginUser.getUserId()+"calculate_weight_all", costreportOrderCostChangeDO.getCalculateWeightAll()+"", 86400, TimeUnit.SECONDS);
		operations.set(loginUser.getUserId()+"calculate_sum", costreportOrderCostChangeDO.getCalculateSum()+"", 86400, TimeUnit.SECONDS);
		operations.set(loginUser.getUserId()+"provenance_province_id", costreportOrderCostChangeDO.getProvenanceProvinceId()+"", 86400, TimeUnit.SECONDS);
		operations.set(loginUser.getUserId()+"order_Type", costreportOrderCostChangeDO.getOrderType()+"", 86400, TimeUnit.SECONDS);
	    String rusult = null;
		Double vos = costreportOrderCostChangeService.calculateData(costreportOrderCostChangeDO,loginUser);
		if(vos != null){
			DecimalFormat df = new DecimalFormat("#,###"); 
			rusult = df.format(vos);
		}
		return success(rusult);
	}
	
	
//	Object obj = request.getSession().getAttribute(CRM_constants.AUTH_USER);
//	UserDO loginUser =new UserDO();
//	if (null == obj){
//	} else {
//		loginUser = (UserDO)obj;
//	}
	
	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@RequiresPermissions("costreport:costreportOrderCostChange:exportExcel")
	public void exportExcel(HttpServletResponse response) {
		Map<String, Object> params = new HashMap<>(16);
		params.put("limit", "10000");// 上限保护
		Query query = new Query(params);

		//int nums = costreportOrderCostChangeService.count(query);

		List<CostreportOrderCostChangeDO> result = costreportOrderCostChangeService.list(query);
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";

		try {
			ExcelUtils.getInstance().exportObjects2Excel(result, CostreportOrderCostChangeDO.class, targetFile);

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
	@RequiresPermissions("costreport:costreportOrderCostChange:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<CostreportOrderCostChangeDO>(), CostreportOrderCostChangeDO.class, targetFile);

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
	@RequiresPermissions("costreport:costreportOrderCostChange:importExcel")
	public R importExcel(MultipartFile file) {
		List<CostreportOrderCostChangeDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, CostreportOrderCostChangeDO.class, 0, 0);
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
			for (CostreportOrderCostChangeDO _do : list) {
				costreportOrderCostChangeService.save(_do);
			}
		}

		return R.ok();
	}		
	
	@GetMapping("/add")
	@RequiresPermissions("costreport:costreportOrderCostChange:add")
	String add(){
	    return "feiniao/costreport/costreportOrderCostChange/add";
	}

	@GetMapping("/edit/{recordId}")
	@RequiresPermissions("costreport:costreportOrderCostChange:edit")
	String edit(@PathVariable("recordId") Integer recordId,Model model){
		CostreportOrderCostChangeDO costreportOrderCostChange = costreportOrderCostChangeService.get(recordId);
		model.addAttribute("costreportOrderCostChange", costreportOrderCostChange);
	    return "feiniao/costreport/costreportOrderCostChange/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("costreport:costreportOrderCostChange:add")
	public R save( CostreportOrderCostChangeDO costreportOrderCostChange){
		if(costreportOrderCostChangeService.save(costreportOrderCostChange)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("costreport:costreportOrderCostChange:edit")
	public R update( CostreportOrderCostChangeDO costreportOrderCostChange){
		costreportOrderCostChangeService.update(costreportOrderCostChange);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("costreport:costreportOrderCostChange:remove")
	public R remove( Integer recordId){
		if(costreportOrderCostChangeService.remove(recordId)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("costreport:costreportOrderCostChange:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] recordIds){
		costreportOrderCostChangeService.batchRemove(recordIds);
		return R.ok();
	}
	
}
