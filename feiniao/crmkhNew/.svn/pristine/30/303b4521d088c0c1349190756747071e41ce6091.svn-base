package com.yunda.base.system.controller;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yunda.base.system.vo.Data;
import com.yunda.base.system.vo.PictureVO;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.validation.BindingResult;
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
import com.yunda.base.feiniao.report.bo.Bo_ReportTotaldata;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.LoginLogDO;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.service.LoginLogService;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;
/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-01-22185215
 */
 
@Controller
@RequestMapping("/system/loginLog")
public class LoginLogController extends BaseController {
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private LoginLogService loginLogService;
	
	@GetMapping()
	@RequiresPermissions("system:loginLog:loginLog")
	String LoginLog(){
	    return "system/loginLog/loginLog";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("system:loginLog:loginLog")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<LoginLogDO> loginLogList = loginLogService.list(query);
		int total = loginLogService.count(query);
		PageUtils pageUtils = new PageUtils(loginLogList, total);
		return pageUtils;
	}

	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@RequiresPermissions("system:loginLog:exportExcel")
	public void exportExcel(HttpServletResponse response, HttpServletRequest request, @ModelAttribute @Validated Bo_ReportTotaldata bo_ReportTotaldata , BindingResult bindingResult, @RequestParam Map<String, Object> params) {
		//导出功能是否开放  true表示开放
		if (SysConfig.DAOCHU.equals("false")) {
			return;
		} else if (SysConfig.DAOCHU.equals("true")) {
			BufferedInputStream bin = null;
			OutputStream out = null;
			UserDO loginUser = getUser(request);

			//查询列表数据
			Query query = new Query(params);
			query.put("offset", 0);
			query.put("limit", 10000);
			List<LoginLogDO> listLoginLogDO = loginLogService.list(query);
			String targetFile = SysConfig.TARGET + "LoginLog"+ DateUtils.format(new java.util.Date()) + ".xlsx";
			File downloadFile = new File(targetFile);
			try {
				//ExcelUtils.getInstance().exportObjects2Excel(result, ReportTotaldataDO.class, targetFile);
				// 按命名规则找模版文件
				File file = new File(SysConfig.TEMPLATE+"LoginLog.xlsx");
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
					ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0,
							listLoginLogDO,data, LoginLogDO.class, false, response.getOutputStream());

					//System.out.println("111====================="+downloadFile.getPath());
				} else {
					//模板文件不存在  默认输出
					ExcelUtils.getInstance().exportObjects2Excel(listLoginLogDO,LoginLogDO.class,response.getOutputStream());
				}
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
	@RequiresPermissions("system:loginLog:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<LoginLogDO>(), LoginLogDO.class, targetFile);

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
	@RequiresPermissions("system:loginLog:importExcel")
	public R importExcel(MultipartFile file) {
		List<LoginLogDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, LoginLogDO.class, 0, 0);
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
			for (LoginLogDO _do : list) {
				loginLogService.save(_do);
			}
		}

		return R.ok();
	}		
	
	@GetMapping("/add")
	@RequiresPermissions("system:loginLog:add")
	String add(){
	    return "system/loginLog/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("system:loginLog:edit")
	String edit(@PathVariable("id") Long id,Model model){
		LoginLogDO loginLog = loginLogService.get(id);
		model.addAttribute("loginLog", loginLog);
	    return "system/loginLog/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("system:loginLog:add")
	public R save( LoginLogDO loginLog){
		if(loginLogService.save(loginLog)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("system:loginLog:edit")
	public R update( LoginLogDO loginLog){
		loginLogService.update(loginLog);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("system:loginLog:remove")
	public R remove(Long id){
		if(loginLogService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("system:loginLog:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		loginLogService.batchRemove(ids);
		return R.ok();
	}

	@GetMapping("/detail/{sessionId}")
	public String detail(@PathVariable("sessionId") String sessionId,Model model){
		model.addAttribute("sessionId", sessionId);
		return "system/operateLog/detail";
	}

	@GetMapping("/checkPicture/{sessionId}")
	@ResponseBody
	public PictureVO checkPicture(@PathVariable("sessionId") String sessionId){
		String src = loginLogService.queryPictureBySessionId(sessionId);
		PictureVO pictureVO = new PictureVO();
		ArrayList<Data> dataList = new ArrayList<>();
		Data data = new Data();
		if(null != src && !src.equals("")){
			data.setSrc(src);
			data.setAlt("报警图片");
			data.setPid(1);
			data.setThumb(src);
			dataList.add(data);
			pictureVO.setData(dataList);
			pictureVO.setId(1);
			pictureVO.setTitle("报警相册");
			pictureVO.setStart(0);
		}
		return pictureVO;
	}


	@GetMapping("/viewDirect/{sessionId}")
	public void viewDirect(@PathVariable("sessionId")String sessionId,HttpServletResponse response) throws Exception {
		String src = loginLogService.queryPictureBySessionId(sessionId);
		String filePath = src;
		File file = new File(filePath);
		if (file.exists()&&file.isFile()) {
			String suffix = filePath.substring(filePath.lastIndexOf(".")+1, filePath.length());
			if (suffix==null) {
				return;
			}
			String metaType = "";
			if (suffix.equalsIgnoreCase("jpg")||suffix.equalsIgnoreCase("jpeg")||suffix.equalsIgnoreCase("jpe")) {
				metaType = "data:image/jpeg;base64,";
			}else if (suffix.equalsIgnoreCase("gif")) {
				metaType = "data:image/gif;base64,";
			}else if (suffix.equalsIgnoreCase("bmp")) {
				metaType = "data:image/bmp;base64,";
			}else {
				metaType = "data:image/jpeg;base64,";//默认解码方式，不合适
			}
			InputStream inputStream = new FileInputStream(file);
			ServletOutputStream outputStream = response.getOutputStream();
			byte[] bytes = new byte[(int) file.length()];
			inputStream.read(bytes);
			outputStream.write(metaType.getBytes());//这一行代码直接影响是否将接下来的字节解析为图片
			outputStream.write(Base64Utils.encode(bytes));
//			return Base64Utils.encode(bytes);
			inputStream.close();
			outputStream.flush();
			outputStream.close();
		}
	}

}
