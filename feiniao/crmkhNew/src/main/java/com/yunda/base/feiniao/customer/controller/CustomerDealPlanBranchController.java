package com.yunda.base.feiniao.customer.controller;

import com.github.crab2died.ExcelUtils;
import com.yunda.base.common.config.Constant;
import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.feiniao.customer.bo.CustomerDealPlanBO;
import com.yunda.base.feiniao.customer.domain.CustomerDealPlanDO;
import com.yunda.base.feiniao.customer.service.CustomerDealPlanService;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.UserDO;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-03-19144936
 */
 
@Controller
@RequestMapping("/customer/customerDealPlanBranch")
public class CustomerDealPlanBranchController extends BaseController {
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private CustomerDealPlanService customerDealPlanService;
	
	@GetMapping()
	@RequiresPermissions("customer:customerDealPlanBranch:customerDealPlanBranch")
	String CustomerDealPlan(Model model){
		//获取当前月初时间
        String defaultStartDate = DateUtils.getMonthBegin();
        String dafaultEndDate = DateUtils.getLastDay();
		CustomerDealPlanBO customerDealPlanBO = new CustomerDealPlanBO();
		customerDealPlanBO.setStartDate(defaultStartDate);
		customerDealPlanBO.setEndDate(dafaultEndDate);
		model.addAttribute("customerDealPlanBO",customerDealPlanBO);
	    return "feiniao/customer/customerDealPlan/customerDealPlanBranch";
	}

	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("customer:customerDealPlanBranch:customerDealPlanBranch")
	public PageUtils list(@ModelAttribute CustomerDealPlanBO customerDealPlanBO, HttpServletRequest request){
        UserDO loginUser = getUser(request);
		if(loginUser.hasPerms(Constant.REPORT_ADMIN_PERMS)){
			//总部权限,可以查看所有数据
			//groupBy一级网点编码取数据就行
			return new PageUtils();
		}else{
			//是否有省权限
			if(loginUser.isProvinceqx()){
				//获取这个省下面的所有的数据(group by province)
				return customerDealPlanService.list(customerDealPlanBO,loginUser);
			}else {
				if(loginUser.getOrgCode() != null & !loginUser.getOrgCode().isEmpty()){
					//如果是网点的话(一级网点)
					String mcCode = loginUser.getOrgCode();
					//获取这个一级网点下的所有网点的数据

				}
				return new PageUtils();
			}
		}

	}

    // 导出excel
    @RequestMapping("/exportExcel")
    @MethodLock(key = "exportExcel")
    @RequiresPermissions("customer:customerDealPlanBranch:exportExcel")
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
	@RequiresPermissions("customer:customerDealPlanBranch:importExcel")
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


}
