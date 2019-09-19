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
import com.yunda.base.feiniao.customer.bo.CustomerDealPlanBO;
import com.yunda.base.feiniao.customer.domain.CustomerDealPlanDO;
import com.yunda.base.feiniao.customer.service.CustomerDealPlanService;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.UserDO;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-03-19144936
 */
 
@Controller
@RequestMapping("/customer/customerDealPlan")
public class CustomerDealPlanController extends BaseController {
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private CustomerDealPlanService customerDealPlanService;
	
	@GetMapping()
	@RequiresPermissions("customer:customerDealPlan:customerDealPlan")
	String CustomerDealPlan(Model model){
		//获取当前月初时间
        String defaultStartDate = DateUtils.getMonthBegin();
        String dafaultEndDate = DateUtils.getLastDay();
		CustomerDealPlanBO customerDealPlanBO = new CustomerDealPlanBO();
		customerDealPlanBO.setStartDate(defaultStartDate);
		customerDealPlanBO.setEndDate(dafaultEndDate);
		model.addAttribute("customerDealPlanBO",customerDealPlanBO);
	    return "feiniao/customer/customerDealPlan/customerDealPlan";
	}

	@GetMapping("/customerDealPlanBranch/{organizationH}/{startDateH}/{endDateH}")
	@RequiresPermissions("customer:customerDealPlan:customerDealPlan")
	String customerDealPlanBranch(@PathVariable("organizationH")String organizationH,@PathVariable("startDateH")String startDateH,@PathVariable("endDateH")String endDateH,Model model){
		CustomerDealPlanBO customerDealPlanBO = new CustomerDealPlanBO();
		customerDealPlanBO.setOrganizationH(organizationH);
		customerDealPlanBO.setStartDate(startDateH);
		customerDealPlanBO.setEndDate(endDateH);
		model.addAttribute("customerDealPlanBO",customerDealPlanBO);
		return "feiniao/customer/customerDealPlanBranch/customerDealPlanBranch";
	}

	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("customer:customerDealPlan:customerDealPlan")
	public PageUtils list(@ModelAttribute CustomerDealPlanBO customerDealPlanBO, HttpServletRequest request){
        UserDO loginUser = getUser(request);
        return customerDealPlanService.list(customerDealPlanBO,loginUser);
	}

    // 导出excel
    @RequestMapping("/exportExcel")
    @MethodLock(key = "exportExcel")
    @RequiresPermissions("customer:customerDealPlan:exportExcel")
    public void exportExcel(HttpServletResponse response, HttpServletRequest request, @ModelAttribute CustomerDealPlanBO customerDealPlanBO) {
        //导出功能是否开放  true表示开放
        if (SysConfig.DAOCHU.equals("false")) {
            return;
        } else if (SysConfig.DAOCHU.equals("true")) {
            BufferedInputStream bin = null;
            OutputStream out = null;
            UserDO loginUser = getUser(request);

            //查询列表数据
            customerDealPlanBO.setOffset(0);
            customerDealPlanBO.setLimit(10000);
            PageUtils pageUtils = this.list(customerDealPlanBO,request);
            List<CustomerDealPlanDO> result = (List<CustomerDealPlanDO>) pageUtils.getRows();
            String targetFile = SysConfig.TARGET + "客户处理进度信息" + DateUtils.getExcludeHMS(customerDealPlanBO.getStartDate())+"至"+DateUtils.getExcludeHMS(customerDealPlanBO.getEndDate()) + ".xlsx";
            File downloadFile = new File(targetFile);
            try {
                //ExcelUtils.getInstance().exportObjects2Excel(result, ReportTotaldataDO.class, targetFile);
                // 按命名规则找模版文件
                File file = new File(SysConfig.TEMPLATE + "customerDealPlan.xlsx");
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                response.setCharacterEncoding("utf-8");

                // set headers for the response
                String headerKey = "Content-Disposition";
                String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
                response.setHeader(headerKey, headerValue);

                if (file.exists() && SysConfig.USER_TEMPLATE.equals("true")) {
                    //前端界面喂参  控制是否使用模板
                    Map<String, String> data = new HashMap<>();
                    // 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
                    ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0,
                            result, data, CustomerDealPlanDO.class, false, response.getOutputStream());
                } else {
                    //模板文件不存在  默认输出
                    ExcelUtils.getInstance().exportObjects2Excel(result, CustomerDealPlanDO.class, response.getOutputStream());
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
	@RequiresPermissions("customer:customerDealPlan:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<CustomerDealPlanDO>(), CustomerDealPlanDO.class, targetFile);

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
	@RequiresPermissions("customer:customerDealPlan:importExcel")
	public R importExcel(MultipartFile file) {
		List<CustomerDealPlanDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, CustomerDealPlanDO.class, 0, 0);
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
			for (CustomerDealPlanDO _do : list) {
				customerDealPlanService.save(_do);
			}
		}

		return R.ok();
	}		
	
	@GetMapping("/add")
	@RequiresPermissions("customer:customerDealPlan:add")
	String add(){
	    return "customer/customerDealPlan/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("customer:customerDealPlan:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		CustomerDealPlanDO customerDealPlan = customerDealPlanService.get(id);
		model.addAttribute("customerDealPlan", customerDealPlan);
	    return "customer/customerDealPlan/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("customer:customerDealPlan:add")
	public R save( CustomerDealPlanDO customerDealPlan){
		if(customerDealPlanService.save(customerDealPlan)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("customer:customerDealPlan:edit")
	public R update( CustomerDealPlanDO customerDealPlan){
		customerDealPlanService.update(customerDealPlan);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("customer:customerDealPlan:remove")
	public R remove( String branchCode){
		if(customerDealPlanService.remove(branchCode)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("customer:customerDealPlan:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] ids){
		customerDealPlanService.batchRemove(ids);
		return R.ok();
	}




}
