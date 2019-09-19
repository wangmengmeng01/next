package com.yunda.base.system.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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
import com.yunda.base.common.config.Constant;
import com.yunda.base.common.domain.Tree;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.R;
import com.yunda.base.feiniao.report.utils.CachePrefixConformity;
import com.yunda.base.feiniao.schedule.suckdata.enums.SuckCacheKeyPerfixEnum;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.CityDO;
import com.yunda.base.system.domain.ProvinceDO;
import com.yunda.base.system.service.ProvinceService;
import com.yunda.ydmbspringbootstarter.common.annotation.Log;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;
/**
 * 省
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-08-15 16:55:51
 */
 
@Controller
@RequestMapping("/system/province")
public class ProvinceController {
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private ProvinceService provinceService;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@GetMapping()
	@RequiresPermissions("system:province:province")
	String Province(){
	    return "system/province/province";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("system:province:province")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<ProvinceDO> provinceList = provinceService.list(query);
		int total = provinceService.count(query);
		PageUtils pageUtils = new PageUtils(provinceList, total);
		return pageUtils;
	}

	
	
	@ResponseBody
	@RequiresPermissions("system:province:provinceTree")
	@GetMapping("/tree/{userId}")
	Tree<ProvinceDO> tree(@PathVariable("userId") Long userId) {
		Tree<ProvinceDO> tree = new Tree<ProvinceDO>();
		tree = provinceService.getTree(userId);
		return tree;
	}
	
	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@RequiresPermissions("system:province:exportExcel")
	public void exportExcel(HttpServletResponse response) {
		Map<String, Object> params = new HashMap<>(16);
		params.put("limit", "10000");// 上限保护
		Query query = new Query(params);

		//int nums = provinceService.count(query);

		List<ProvinceDO> result = provinceService.list(query);
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";

		try {
			ExcelUtils.getInstance().exportObjects2Excel(result, ProvinceDO.class, targetFile);

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
	@RequiresPermissions("system:province:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<ProvinceDO>(), ProvinceDO.class, targetFile);

			// 写入response
			File downloadFile = new File(targetFile);
			FileUtil.downloadByResponse(response, downloadFile);
			downloadFile.delete();
		} catch (Exception e) {
			//e.printStackTrace();
			log.error(e.getMessage(), e);
		}
	}

	// 导入excel
	@ResponseBody
	@MethodLock(key = "importExcel")
	@RequestMapping(value = "/importExcel", consumes = "multipart/*", headers = "content-type=mutipart/form-data", method = RequestMethod.POST)
	@RequiresPermissions("system:province:importExcel")
	public R importExcel(MultipartFile file) {
		List<ProvinceDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, ProvinceDO.class, 0, 0);
			} catch (Exception e) {
				//e.printStackTrace();
				log.error(e.getMessage(), e);
			} finally {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}

		if (list != null) {
			for (ProvinceDO _do : list) {
				provinceService.save(_do);
			}
		}

		return R.ok();
	}		
	
	@GetMapping("/add")
	@RequiresPermissions("system:province:add")
	String add(){
	    return "system/province/add";
	}

	@GetMapping("/edit/{provinceid}")
	@RequiresPermissions("system:province:edit")
	String edit(@PathVariable("provinceid") String provinceid,Model model){
		ProvinceDO province = provinceService.get(provinceid);
		model.addAttribute("province", province);
	    return "system/province/edit";
	}
	
	/**
	 * 维护省总公司
	 * @return
	 */
	@GetMapping("/maintainProvince")
	@RequiresPermissions("system:province:maintainProvince")
	String maintainProvince(){
	    return "system/province/province";
	}
	
	/**
	 * 获取可维护的省总公司
	 * @param params
	 * @return
	 */
	@ResponseBody
	@GetMapping("/maintainProvincelist")
	@RequiresPermissions("system:province:maintainProvince")
	public PageUtils maintainProvincelist(@RequestParam Map<String, Object> params){
		//查询列表数据
		params.put("maintainState", 2);
        Query query = new Query(params);
		List<ProvinceDO> provinceList = provinceService.maintainProvincelist(query);
		int total = provinceService.count(query);
		PageUtils pageUtils = new PageUtils(provinceList, total);
		return pageUtils;
	}
	
	/**
	 * 获取维护省数据，并跳转到维护页面
	 * @param provinceid
	 * @param model
	 * @return
	 */
	@GetMapping("/getMaintainProvince/{provinceid}")
	@RequiresPermissions("system:province:maintainProvince")
	String getMaintainProvince(@PathVariable("provinceid") String provinceid,Model model){
		ProvinceDO province = provinceService.get(provinceid);
		model.addAttribute("province", province);
	    return "system/province/editMaintianProvince";
	}
	
	/**
	 * 获取省总公司对应省下的所有市
	 * @param ProvinceID
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("system:province:maintainProvince")
	@GetMapping("/cityTree/{ProvinceID}")
	Tree<CityDO> cityTree(@PathVariable("ProvinceID") Long provinceId) {
		Tree<CityDO> tree = new Tree<CityDO>();
		tree = provinceService.getCityTree(provinceId);
		return tree;
	}
	
	/**
	 * 把市分到哪个省总公司下，该市的所属省id更新成省总公司的省id(排它性，一个市只会属于一个省或一个省总公司)
	 * @param province
	 * @return
	 */
	@RequiresPermissions("system:province:maintainProvince")
	@Log("更新省总公司管辖市份")
	@PostMapping("/updateCity")
	@ResponseBody
	R updateCity(ProvinceDO province) {
		if (provinceService.updateCity(province) > 0) {
			return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("system:province:add")
	public R save( ProvinceDO province){
		if(provinceService.save(province)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("system:province:edit")
	public R update( ProvinceDO province){
		provinceService.update(province);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("system:province:remove")
	public R remove( String provinceid){
		if(provinceService.remove(provinceid)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("system:province:batchRemove")
	public R remove(@RequestParam("ids[]") String[] provinceids){
		provinceService.batchRemove(provinceids);
		return R.ok();
	}
	
	//原理是用爬虫来访问本应用  用于探测数据库和 redis的接口   检测应用是否宕机停止服务
	@ResponseBody
	//@GetMapping("/countProvinceApi")
	@RequestMapping(value = "/countProvinceApi", method = RequestMethod.GET)
	//@CrossOrigin(origins = "http://10.19.105.135:9999")
	public void countProvinceApi(HttpServletResponse response,@RequestParam Map<String, Object> params){
		params.put("maintainState", 2);
        Query query = new Query(params);
		int total = provinceService.count(query);
		//System.out.println("数据库无异常```````````````````````````````````````");
		if(total>0){
			try {
				//response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write("success");
			} catch (IOException e) {
				//e.printStackTrace();
				log.error(e.getMessage(), e);
			}
		}
	}
	@ResponseBody
	@GetMapping("/redisProvinceApi")
	//@CrossOrigin(origins = "http://10.19.105.135:9999")
	public void redisApi(HttpServletResponse response,@RequestParam Map<String, Object> params){
		//缓存
		CachePrefixConformity cache = new CachePrefixConformity();
		ValueOperations<String, List<ProvinceDO>> operations = redisTemplate.opsForValue();
		
		//查询列表数据
		params.put("maintainState", 2);
		
		Query query = new Query(params);
		List<ProvinceDO> provinceList = provinceService.maintainProvincelist(query);
		operations.set(cache.getSeed(Constant.REDISTC,SuckCacheKeyPerfixEnum.redisTC.getCode()), provinceList, 1 , TimeUnit.SECONDS);
		
		try {
			//response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("success");
		} catch (IOException e) {
			//e.printStackTrace();
			log.error(e.getMessage(), e);
		}		
	}
	
}
