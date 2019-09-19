package com.yunda.base.feiniao.customer.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
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
import com.yunda.base.common.config.Constant;
import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.R;
import com.yunda.base.common.utils.ShiroUtils;
import com.yunda.base.feiniao.customer.bo.NotCooperateCustomerBO;
import com.yunda.base.feiniao.customer.domain.CooperatePeerDO;
import com.yunda.base.feiniao.customer.domain.NotCooperateCustomerDO;
import com.yunda.base.feiniao.customer.service.NotCooperateCustomerService;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.service.UserService;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;

@Controller
@RequestMapping("/customer/notCooperateCustomer")
public class NotCooperateCustomerController extends BaseController {
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private NotCooperateCustomerService notCooperateCustomerService;

	@Autowired
	private UserService userService;
	
	@GetMapping()
	@RequiresPermissions("customer:notCooperateCustomer:notCooperateCustomer")
	String NotCooperateCustomer(){
	    return "feiniao/customer/notCooperateCustomer/notCooperateCustomer";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("customer:notCooperateCustomer:notCooperateCustomer")
	public PageUtils list(@ModelAttribute NotCooperateCustomerBO notCooperateCustomerBO, HttpServletRequest request){
		//查询列表数据-
		UserDO loginUser = getUser(request);
        List<NotCooperateCustomerDO> notCooperateCustomerList = new ArrayList<NotCooperateCustomerDO>();
        String provinceNames="";
        int total=0;
        //1.判断是业务省还是网点还是总部
		if (loginUser.hasPerms(Constant.REPORT_ADMIN_PERMS)) {
			// 超级用户权限 无限制
			// 系统菜单配置了report:admin:allperms权限标识 角色管理中当该用户的角色勾选该权限标识就说明该用户是超级用户
			// 能查看所有报表的集团大区省市等所有数据
            notCooperateCustomerList = notCooperateCustomerService.list(notCooperateCustomerBO);
            total= notCooperateCustomerService.count(notCooperateCustomerBO);
		} else {
			if (loginUser.isProvinceqx()) {// 是否有省权限
				//2.如果是业务省,获取业务省下面的所有网点信息,如果是网点就获取网点信息,如果是总部就查询所有
                List<Long> provinceIds = loginUser.getProvinceIds();
                //根据省id查询省名字
                List<String> listProvinceName = notCooperateCustomerService.getProvinceNameByProvinceId(provinceIds);
				for (String provinceName : listProvinceName) {
					provinceNames+=provinceName+",";
				}
				provinceNames = provinceNames.substring(0,provinceNames.length()-1);
				notCooperateCustomerBO.setProvinceName(provinceNames);
				notCooperateCustomerBO.setLimit(10000);
                //根据业务省名称获取业务省下面的所有未合作的网点信息
                notCooperateCustomerList = notCooperateCustomerService.listInfoByProvinceName(notCooperateCustomerBO);
                total = notCooperateCustomerService.countByProvinceName(notCooperateCustomerBO);
			} else if (loginUser.getOrgCode() != null & !loginUser.getOrgCode().isEmpty()) {// 只有某网点权限
				//添加网点条件查询,直接返回什么也没有就行
				if(!notCooperateCustomerBO.getBranchCode().equals(loginUser.getOrgCode()) && notCooperateCustomerBO.getBranchCode()!=null && !notCooperateCustomerBO.getBranchCode().equals("")){
					return new PageUtils();
				}
				notCooperateCustomerBO.setBranchCode(loginUser.getOrgCode());
                notCooperateCustomerList = notCooperateCustomerService.list(notCooperateCustomerBO);
                total = notCooperateCustomerService.count(notCooperateCustomerBO);
			} else {
				return new PageUtils();
			}
		}
		PageUtils pageUtils = new PageUtils(notCooperateCustomerList, total);
		return pageUtils;
	}

	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@RequiresPermissions("customer:notCooperateCustomer:exportExcel")
	public void exportExcel(HttpServletResponse response, HttpServletRequest request, @ModelAttribute NotCooperateCustomerBO notCooperateCustomerBO) {
		//导出功能是否开放  true表示开放
		if (SysConfig.DAOCHU.equals("false")) {
			return;
		} else if (SysConfig.DAOCHU.equals("true")) {
			BufferedInputStream bin = null;
			OutputStream out = null;
			UserDO loginUser = getUser(request);

			//查询列表数据
			notCooperateCustomerBO.setOffset(0);
			notCooperateCustomerBO.setLimit(10000);
			PageUtils pageUtils = list(notCooperateCustomerBO,request);
			List<NotCooperateCustomerDO> result = (List<NotCooperateCustomerDO>) pageUtils.getRows();
			String targetFile = SysConfig.TARGET + "未合作大客户信息" + DateUtils.format(new Date()) + ".xlsx";
			File downloadFile = new File(targetFile);
			try {
				// 按命名规则找模版文件
				File file = new File(SysConfig.TEMPLATE + "notCooperateCustomer.xlsx");
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
							result, data, NotCooperateCustomerDO.class, false, response.getOutputStream());
				} else {
					//模板文件不存在  默认输出
					ExcelUtils.getInstance().exportObjects2Excel(result, NotCooperateCustomerDO.class, response.getOutputStream());
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
	@RequiresPermissions("customer:notCooperateCustomer:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<NotCooperateCustomerDO>(), NotCooperateCustomerDO.class, targetFile);

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
	@RequiresPermissions("customer:notCooperateCustomer:importExcel")
	public R importExcel(MultipartFile file) {
		List<NotCooperateCustomerDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, NotCooperateCustomerDO.class, 0, 0);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			} finally {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}

		if (list != null) {
			for (NotCooperateCustomerDO _do : list) {
				notCooperateCustomerService.save(_do);
			}
		}

		return R.ok();
	}		
	
	@GetMapping("/add")
	@RequiresPermissions("customer:notCooperateCustomer:add")
	String add(){
		//获取用户登录账号对应的机构名称
		UserDO user = ShiroUtils.getUser();
		//根据用户所在的公司编码获取公司名称
		String cpmpanyName = notCooperateCustomerService.getCompanyNameByOrgCode(user.getOrgCode());
		//网点所属业务省和城市
		return "feiniao/customer/notCooperateCustomer/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("customer:notCooperateCustomer:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		NotCooperateCustomerDO notCooperateCustomer = notCooperateCustomerService.get(id);
		model.addAttribute("notCooperateCustomer", notCooperateCustomer);
	    return "feiniao/customer/notCooperateCustomer/edit";
	}

    @ResponseBody
	@GetMapping("/editHuiXianCooperatePeer/{branchCode}/{productType}")
    RspBean editHuiXianCooperatePeer(@PathVariable("branchCode")String branchCode, @PathVariable("productType")String productType){
        List<CooperatePeerDO> listCooperatePeerDO = notCooperateCustomerService.huiXianCooperatePeer(branchCode,productType);
        return new RspBean(listCooperatePeerDO);
    }
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("customer:notCooperateCustomer:add")
	public R save( NotCooperateCustomerDO notCooperateCustomer){
		if(notCooperateCustomerService.save(notCooperateCustomer)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("customer:notCooperateCustomer:edit")
	public R update( NotCooperateCustomerDO notCooperateCustomer){
		//判断是否有填写
		if(null != notCooperateCustomer.getCooperateBranch()){
			//先查询所填写的合作网点编码是否存在
			int count = notCooperateCustomerService.checkCooperateBranch(notCooperateCustomer.getCooperateBranch());
			if(count<1){
				return R.error("合作网点编码不存在!");
			}

		}
		int update = notCooperateCustomerService.update(notCooperateCustomer);
		if(update<1){
			return R.error();
		}
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("customer:notCooperateCustomer:remove")
	public R remove( Integer id){
		if(notCooperateCustomerService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("customer:notCooperateCustomer:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] ids){
		notCooperateCustomerService.batchRemove(ids);
		return R.ok();
	}

	//获取表前面的当前月的总结数据
	@GetMapping("/getSummary")
	@ResponseBody
	public R getSummary(){
		R r = new R();
		String result = notCooperateCustomerService.getSummary();
		r.put("summary",result);
		return r;
	}

	//查看详细信息
	@GetMapping("/seeReport/{id}")
	@RequiresPermissions("customer:notCooperateCustomer:seeReport")
	public String seeReport(@PathVariable("id") Integer id,Model model){
		NotCooperateCustomerDO notCooperateCustomerDO = notCooperateCustomerService.get(id);
		model.addAttribute("notCooperateCustomerDO",notCooperateCustomerDO);
		return "feiniao/customer/notCooperateCustomer/seeReport";
	}

	//处理前查询看该角色是否有权限处理
	@GetMapping("/dealBefore/{id}")
	@ResponseBody
	@RequiresPermissions("customer:notCooperateCustomer:deal")
	public int dealBefore(@PathVariable("id") Integer id,HttpServletRequest request){
		NotCooperateCustomerDO notCooperateCustomerDO = notCooperateCustomerService.get(id);
		String state = notCooperateCustomerDO.getState();
		//判断当前操作人的信息,如果是总部那就把处理事件设置为总部事件,相反就是省总处理时间
		UserDO loginUser = getUser(request);
		if (loginUser.hasPerms(Constant.REPORT_ADMIN_PERMS)) {
			//仅总部待处理才可以允许总部处理
			if(!state.equals(Constant.ZONGBU_WAIT_DEAL)){
				return 300;
			}
		} else {
			if (loginUser.isProvinceqx()) {
				//判断只有省公司待处理状态和洽谈状态可以允许省公司处理
				if (!state.equals(Constant.PROVINCE_WAIT_DEAL) && !state.equals(Constant.DISCUSSE)){
					return 300;
				}
			}else {
				return 300;
			}
		}
		return 200;
	}

	//处理信息
	@GetMapping("/deal/{id}")
	@RequiresPermissions("customer:notCooperateCustomer:deal")
	public String deal(@PathVariable("id") Integer id,Model model,HttpServletRequest request){
		NotCooperateCustomerDO notCooperateCustomerDO = notCooperateCustomerService.get(id);
		String state = notCooperateCustomerDO.getState();
		//判断当前操作人的信息,如果是总部那就把处理事件设置为总部事件,相反就是省总处理时间
		UserDO loginUser = getUser(request);
		String dealName = loginUser.getName()+"("+loginUser.getUsername()+")";
		String dealTime = DateUtils.getCurrentTime();
		if (loginUser.hasPerms(Constant.REPORT_ADMIN_PERMS)) {
			//是总部拼接处理人和处理时间
			//拼接总部处理人
			notCooperateCustomerDO.setZongBuDealName(dealName);
			//总部处理时间
			notCooperateCustomerDO.setZongBuDealTime(dealTime);
			//仅总部待处理才可以允许总部处理
			if(!state.equals(Constant.ZONGBU_WAIT_DEAL)){
				return "feiniao/customer/notCooperateCustomer/stateErro";
			}
		} else {
			if (loginUser.isProvinceqx()) {
				//判断只有省公司待处理状态和洽谈状态可以允许省公司处理
				//省公司处理人
				notCooperateCustomerDO.setProvinceDealName(dealName);
				//省公司处理时间
				notCooperateCustomerDO.setProvinceDealTime(dealTime);
				if (!state.equals(Constant.PROVINCE_WAIT_DEAL) && !state.equals(Constant.DISCUSSE)){
					return "feiniao/customer/notCooperateCustomer/stateErro";
				}
			}else {
				return "feiniao/customer/notCooperateCustomer/stateErro";
			}
		}
		//把当前的账号信息传递到前端,判断是省总还是总部
		model.addAttribute("notCooperateCustomerDO",notCooperateCustomerDO);
		return "feiniao/customer/notCooperateCustomer/deal";
	}

	/**
	 * 处理保存
	 * @param notCooperateCustomer
	 * @return
	 */
	@ResponseBody
	@PostMapping("/dealSave")
	public R dealSave(NotCooperateCustomerDO notCooperateCustomer){
		//省公司处理后为未达成合作,则状态改为待总部处理
	    if(notCooperateCustomer.getState().equals(Constant.NOT_COOPERATE) && (null==notCooperateCustomer.getZongBuDealName() || notCooperateCustomer.getZongBuDealName().equals(""))){
            notCooperateCustomer.setState(Constant.ZONGBU_WAIT_DEAL);
        }
        //如果是总部人员处理,处理状态为总部待处理状态,总部处理后则直接变为未达成合作
		if(null!=notCooperateCustomer.getZongBuDealName() && !notCooperateCustomer.getZongBuDealName().equals("")){
			notCooperateCustomer.setState(Constant.NOT_COOPERATE);
		}
        //校验合作网点编码是否正确
		//先查询所填写的合作网点编码是否存在
		int count = notCooperateCustomerService.checkCooperateBranch(notCooperateCustomer.getCooperateBranch());
		if(count<1){
			return R.error("合作网点编码不存在!");
		}
		//根据id将处理的结果保存
		int update = notCooperateCustomerService.update(notCooperateCustomer);
		if(update<1){
			return R.error(Constant.DEAL_FAIL);
		}
		return R.ok();
	}


	@GetMapping("/boundVip/{id}")
	@RequiresPermissions("customer:notCooperateCustomer:boundVip")
	public String boundVip(@PathVariable("id") Integer id,Model model){
		NotCooperateCustomerDO notCooperateCustomerDO = notCooperateCustomerService.get(id);
		//如果有绑定VIP账号就展示到页面上,修改完保存更新到数据库
		model.addAttribute("notCooperateCustomerDO",notCooperateCustomerDO);
		return "feiniao/customer/notCooperateCustomer/boundVip";
	}

	@PostMapping("/boundVipSave")
	@ResponseBody
	public R boundVipSave(NotCooperateCustomerDO notCooperateCustomer){
	    //先查询所填写的合作网点编码是否存在
        int count = notCooperateCustomerService.checkCooperateBranch(notCooperateCustomer.getCooperateBranch());
        if(count<1){
            return R.error("合作网点编码不存在!");
        }
        if(notCooperateCustomer.getBoundVipAccount()==null){
            return R.error("请填写所绑定的客户VIP账号");
        }
        int update = notCooperateCustomerService.updateBoundVipAccountById(notCooperateCustomer);
        if(update<1){
            return R.error();
        }
        return R.ok();
	}


	@GetMapping("/getBranchInfo")
	@ResponseBody
	public NotCooperateCustomerDO getBranchInfo(HttpServletRequest request){
		UserDO user = ShiroUtils.getUser();
		//根据userId获取所属网点和网点编码(登录账号对应的机构名称)
		UserDO userDO = userService.get(user.getUserId());
		//先获取网点编码,再根据网点编码获取网点名称
		String branchName = userService.getMcByBranchCode(userDO.getOrgCode());
		NotCooperateCustomerDO notCooperateCustomerDO = new NotCooperateCustomerDO();
		notCooperateCustomerDO.setBranchCode(userDO.getOrgCode());
		notCooperateCustomerDO.setBranchName(branchName);
		UserDO loginUser = getUser(request);
		String dealName = loginUser.getName()+"("+loginUser.getOrgCode()+")";
		String dealTime = DateUtils.getCurrentTime();
		if (loginUser.hasPerms(Constant.REPORT_ADMIN_PERMS)) {
			//拼接总部处理人
			notCooperateCustomerDO.setZongBuDealName(dealName);
			//总部处理时间
			notCooperateCustomerDO.setZongBuDealTime(dealTime);
		}else {
			//省公司处理人
			notCooperateCustomerDO.setProvinceDealName(dealName);
			//省公司处理时间
			notCooperateCustomerDO.setProvinceDealTime(dealTime);
		}
		return notCooperateCustomerDO;
	}


}


