package com.yunda.base.bigcustomer.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

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
import com.yunda.base.bigcustomer.domain.CustomerManageDO;
import com.yunda.base.bigcustomer.service.CustomerManageService;
import com.yunda.base.bigcustomer.service.OrganizationManageService;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.R;
import com.yunda.base.system.config.SysConfig;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;
import com.yunda.ydmbspringbootstarter.common.utils.StringUtils;

/**
 *
 *
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-20111513
 */

@Controller
@RequestMapping("/bigcustomer/customerManage")
public class CustomerManageController {
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private CustomerManageService customerManageService;
	@Autowired
	private OrganizationManageService organizationManageService;

	@GetMapping()
	@RequiresPermissions("bigcustomer:customerManage:customerManage")
	String CustomerManage(){
	    return "bigcustomer/customerManage/customerManage";
	}

	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("bigcustomer:customerManage:customerManage")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<CustomerManageDO> customerManageList = customerManageService.list(query);
		int total = customerManageService.count(query);
		PageUtils pageUtils = new PageUtils(customerManageList, total);
		return pageUtils;
	}

	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@RequiresPermissions("bigcustomer:customerManage:exportExcel")
	public void exportExcel(HttpServletResponse response) {
		Map<String, Object> params = new HashMap<>(16);
		params.put("limit", "10000");// 上限保护
		Query query = new Query(params);

		//int nums = customerManageService.count(query);

		List<CustomerManageDO> result = customerManageService.list(query);
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";

		try {
			ExcelUtils.getInstance().exportObjects2Excel(result, CustomerManageDO.class, targetFile);

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
	@RequiresPermissions("bigcustomer:customerManage:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<CustomerManageDO>(), CustomerManageDO.class, targetFile);

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
	@RequiresPermissions("bigcustomer:customerManage:importExcel")
	public R importExcel(MultipartFile file) {
		List<CustomerManageDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, CustomerManageDO.class, 0, 0);
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
			for (CustomerManageDO _do : list) {
				customerManageService.save(_do);
			}
		}

		return R.ok();
	}

	@GetMapping("/add")
	@RequiresPermissions("bigcustomer:customerManage:add")
	String add(Model model){
		//查询所有启用状态下的机构编码
		List organizationList = organizationManageService.listOrganization();
		model.addAttribute("organizationList", organizationList);
	    return "bigcustomer/customerManage/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("bigcustomer:customerManage:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		CustomerManageDO customerManage = customerManageService.get(id);
		//查询所有启用状态下的机构编码
		List organizationList = organizationManageService.listOrganization();
		model.addAttribute("organizationList", organizationList);
		model.addAttribute("customerManage", customerManage);
	    return "bigcustomer/customerManage/edit";
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("bigcustomer:customerManage:add")
	public R save( CustomerManageDO customerManage){
		customerManage.setState("1");
		//系统自动生成4位的客户编码
		String customerId = getRandNum(4);
		customerManage.setCustomerId(customerId);
		//对数据进行判断
		if(StringUtils.isBlank(customerManage.getCustomerName())) {
			return R.error("请填写客户名称!");
		}
		if(StringUtils.isBlank(customerManage.getPlatform())){
			return R.error("请填写所属平台!");
		}
		if(StringUtils.isBlank(customerManage.getBranchNum())){
			return R.error("请填写所属网点!");
		}
		if(StringUtils.isBlank(customerManage.getVipName())){
			return R.error("请填写VIP客户名称!");
		}
		if(StringUtils.isBlank(customerManage.getVipNum())){
			return R.error("请填写VIP账号!");
		}
		/*String organization = customerManage.getOrganization();
		if (StringUtils.isBlank(organization) || organization.contains("-")){
			return R.error("所属机构填写不正确!");
		}*/
		//根据所属网点编码从ydserver里有个cqkh表中查询是否存在
		String branchName = customerManageService.getBranchName(customerManage.getBranchNum());
		if(branchName==null || branchName.equals("")){
			return R.error("您填写的网点编码不存在!");
		}
		customerManage.setBranch(branchName);
		//检查vip账号是否存在,如果不存在提示
		int vipNumInt = customerManageService.checkVipNum(customerManage.getVipNum());
		if(vipNumInt<1){
			return R.error("您填写的VIP账号不存在!");
		}
		if(StringUtils.isBlank(customerManage.getVipName())){
			return R.error("VIP名称不能为空!");
		}
		/*//拆分后分为机构名称和机构编码
		String[] organ = organization.split("-");
		//存放机构名称
		customerManage.setOrganizationName(organ[0]);
		//存放机构编码
		customerManage.setOrganizationNum(organ[1]);*/
		if(customerManageService.save(customerManage)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("bigcustomer:customerManage:edit")
	public R update( CustomerManageDO customerManage){
		//对数据进行判断
		if(StringUtils.isBlank(customerManage.getCustomerName())) {
			return R.error("请填写客户名称!");
		}
		if(StringUtils.isBlank(customerManage.getPlatform())){
			return R.error("请填写所属平台!");
		}
		if(StringUtils.isBlank(customerManage.getBranchNum())){
			return R.error("请填写所属网点!");
		}
		if(StringUtils.isBlank(customerManage.getVipName())){
			return R.error("请填写VIP客户名称!");
		}
		if(StringUtils.isBlank(customerManage.getVipNum())){
			return R.error("请填写VIP账号!");
		}
		/*String organization = customerManage.getOrganization();
		if (StringUtils.isBlank(organization) || organization.contains("-")){
			return R.error("所属机构填写不正确!");
		}*/
		//根据所属网点编码从ydserver里有个cqkh表中查询是否存在
		String branchName = customerManageService.getBranchName(customerManage.getBranchNum());
		if(branchName==null || branchName.equals("")){
			return R.error("您填写的网点编码不存在!");
		}
		customerManage.setBranch(branchName);
		//检查vip账号是否存在,如果不存在提示
		int vipNumInt = customerManageService.checkVipNum(customerManage.getVipNum());
		if(vipNumInt<1){
			return R.error("您填写的VIP账号不存在!");
		}
		if(StringUtils.isBlank(customerManage.getVipName())){
			return R.error("VIP名称不能为空!");
		}
		//拆分后分为机构名称和机构编码
		/*String[] organ = organization.split("-");*/
		//存放机构名称
		/*customerManage.setOrganizationName(organ[0]);
		customerManage.setOrganizationNum(organ[1]);*/
		customerManageService.update(customerManage);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("bigcustomer:customerManage:remove")
	public R remove( Integer id){
		if(customerManageService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}

	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("bigcustomer:customerManage:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] ids){
		customerManageService.batchRemove(ids);
		return R.ok();
	}

	/**
	 * 在客户管理表中新增的时候获取所有的机构信息
	 */
	@ResponseBody
	@GetMapping("/getOrganizationInfo")
	public List getOrganizationInfo(){
		List allOrganization = customerManageService.getOrganizationInfo();
		return allOrganization;
	}


	/**
	 * 修改状态(启用或者禁止)
	 *
	 * @return
	 */
	@PostMapping("/stateUpdate")
	@ResponseBody
	public R stateUpdate(@ModelAttribute CustomerManageDO customerManageDO) {
		String state = customerManageDO.getState();
		Integer id = customerManageDO.getId();
		if (state.equals("1")) {
			//将状态改为禁用
			state = "0";
			customerManageService.stateUpdate(id, state);
			return R.ok("已禁止");
		} else {
			//将状态改为禁用
			state = "1";
			customerManageService.stateUpdate(id, state);
			return R.ok("已启用");
		}

	}

	/**
	 * 根据填写的vip账号获取vip名称
	 */
	@ResponseBody
	@GetMapping("/getVipNameByVipNum/{vipNum}")
	public R getVipNameByVipNum(@PathVariable("vipNum") String vipNum){
		String vipName = customerManageService.getVipNameByVipNum(vipNum);
		if(StringUtils.isBlank(vipName)){
			return R.error("未获得vip名称,请检查vip编码是否填写正确!");
		}
		return R.ok(vipName);
	}


	/**
	 * 获取四位随机数
	 * @param leng  随机数长度
	 * @return
	 */
	public String getRandNum(int leng){
		Random random = new Random();
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < leng; i++) {
			result.append(random.nextInt(10));
		}
		if(result.length()>0){
			return result.toString();
		}
		return null;
	}

}
