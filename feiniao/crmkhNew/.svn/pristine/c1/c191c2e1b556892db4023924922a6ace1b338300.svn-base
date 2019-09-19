package com.yunda.base.feiniao.market.controller;

import java.util.List;
import java.util.Map;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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

import com.yunda.base.feiniao.market.bo.Bo_marketOccupancyTaoxi;
import com.yunda.base.feiniao.market.domain.MarketOccupancyTaoxiDO;
import com.yunda.base.feiniao.market.service.MarketOccupancyTaoxiService;
import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.R;
import com.github.crab2died.ExcelUtils;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-07150734
 */
 
@Controller
@RequestMapping("/market/marketOccupancyTaoxi")
public class MarketOccupancyTaoxiController  extends BaseController{
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private MarketOccupancyTaoxiService marketOccupancyTaoxiService;
	
	@GetMapping()
	@RequiresPermissions("market:marketOccupancyTaoxi:marketOccupancyTaoxi")
	String MarketOccupancyTaoxi(){
	    return "feiniao/market/marketOccupancyTaoxi/marketOccupancyTaoxi";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@MethodLock(key = "request")
	@RequiresPermissions("market:marketOccupancyTaoxi:marketOccupancyTaoxi")
	public RspBean<PageUtils> list(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute @Validated Bo_marketOccupancyTaoxi boInterface, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			notifyPage(bindingResult.getFieldError().getDefaultMessage());
			// 如果验参报错，返回空值給頁面
			return success(new PageUtils());
		}
		//UserDO loginUser = getUser(request);
		
		List<MarketOccupancyTaoxiDO> marketOccupancyTaoxiList = marketOccupancyTaoxiService.list(boInterface);
		int total;
		if("1".equals(boInterface.getTimeType())){
			total= marketOccupancyTaoxiService.count(boInterface);
		}else{
			total  = marketOccupancyTaoxiList.size();
		}
		PageUtils pageUtils = new PageUtils(marketOccupancyTaoxiList, total);
		return success(pageUtils);
	}

	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@RequiresPermissions("market:marketOccupancyTaoxi:exportExcel")
	public void exportExcel(HttpServletResponse response) {
		Map<String, Object> params = new HashMap<>(16);
		params.put("limit", "10000");// 上限保护
		Query query = new Query(params);

		//int nums = marketOccupancyTaoxiService.count(query);

		//List<MarketOccupancyTaoxiDO> result = marketOccupancyTaoxiService.list(query);
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";

		try {
			//ExcelUtils.getInstance().exportObjects2Excel(result, MarketOccupancyTaoxiDO.class, targetFile);

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
	@RequiresPermissions("market:marketOccupancyTaoxi:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<MarketOccupancyTaoxiDO>(), MarketOccupancyTaoxiDO.class, targetFile);

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
	@RequiresPermissions("market:marketOccupancyTaoxi:importExcel")
	public R importExcel(MultipartFile file) {
		List<MarketOccupancyTaoxiDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, MarketOccupancyTaoxiDO.class, 0, 0);
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
			for (MarketOccupancyTaoxiDO _do : list) {
				marketOccupancyTaoxiService.save(_do);
			}
		}

		return R.ok();
	}		
	
	@GetMapping("/add")
	@RequiresPermissions("market:marketOccupancyTaoxi:add")
	String add(){
	    return "feiniao/market/marketOccupancyTaoxi/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("market:marketOccupancyTaoxi:edit")
	String edit(@PathVariable("id") Long id,Model model){
		MarketOccupancyTaoxiDO marketOccupancyTaoxi = marketOccupancyTaoxiService.get(id);
		model.addAttribute("marketOccupancyTaoxi", marketOccupancyTaoxi);
	    return "feiniao/market/marketOccupancyTaoxi/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("market:marketOccupancyTaoxi:add")
	public R save( MarketOccupancyTaoxiDO marketOccupancyTaoxi){
		if(marketOccupancyTaoxiService.save(marketOccupancyTaoxi)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("market:marketOccupancyTaoxi:edit")
	public R update( MarketOccupancyTaoxiDO marketOccupancyTaoxi){
		if(marketOccupancyTaoxiService.update(marketOccupancyTaoxi)>0){;
			return R.ok();
		}
		return R.error(1,"各快递行业占比总计不能超过100%");
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("market:marketOccupancyTaoxi:remove")
	public R remove( Long id){
		if(marketOccupancyTaoxiService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("market:marketOccupancyTaoxi:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		marketOccupancyTaoxiService.batchRemove(ids);
		return R.ok();
	}
	
}
