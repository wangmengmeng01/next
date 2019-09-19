package com.yunda.base.feiniao.customer.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.yunda.base.common.config.Constant;
import com.yunda.base.feiniao.customer.bo.NotCooperateCustomerBO;
import com.yunda.base.feiniao.customer.dao.CooperatePeerDao;
import com.yunda.base.feiniao.customer.dao.CustomerDealPlanDao;
import com.yunda.base.feiniao.customer.dao.NotCooperateCustomerDao;
import com.yunda.base.feiniao.customer.domain.CooperatePeerDO;
import com.yunda.base.feiniao.customer.domain.CustomerDealPlanDO;
import com.yunda.base.feiniao.customer.domain.NotCooperateCustomerDO;
import com.yunda.base.feiniao.customer.service.NotCooperateCustomerService;
import com.yunda.base.feiniao.report.utils.DateUtils;


@Service
public class NotCooperateCustomerServiceImpl implements NotCooperateCustomerService {
	@Autowired
	private NotCooperateCustomerDao notCooperateCustomerDao;

	@Autowired
	private CooperatePeerDao cooperatePeerDao;

	@Autowired
	private CustomerDealPlanDao customerDealPlanDao;
	
	@Override
	public NotCooperateCustomerDO get(Integer id){
		//首先根据id获取信息
		NotCooperateCustomerDO notCooperateCustomerDO = notCooperateCustomerDao.get(id);
		//再根据所获得信息去查询合作同行信息
		List<CooperatePeerDO> listCooperateDO = cooperatePeerDao.get(notCooperateCustomerDO.getBranchCode(),notCooperateCustomerDO.getProductType());
		notCooperateCustomerDO.setListCooperatePeer(listCooperateDO);
		String cooperatePeerCase="";
		for (CooperatePeerDO co : listCooperateDO) {
			//判断有没有包含其他
			if(co.getCooperatePeerName().equals("其他")){
				co.setCooperatePeerName("其他("+co.getRemark()+")");
			}
			//拼装成(类似中通70%，2.8kg；百世30%)
			cooperatePeerCase +=co.getCooperatePeerName()+co.getCooperateRatio()+","+co.getCooperatePrice()+"; ";
		}
		notCooperateCustomerDO.setCooperatePeerCase(cooperatePeerCase);
		//客户所属平台展示(类似淘宝、拼多多、其他(唯品会))
		notCooperateCustomerDO.setCustomerPlatform(notCooperateCustomerDO.getCustomerPlatform().replace(",","、"));
		return notCooperateCustomerDO;
	}

	@Override
	public List<NotCooperateCustomerDO> list(NotCooperateCustomerBO notCooperateCustomerBO){
		return notCooperateCustomerDao.list(notCooperateCustomerBO);
	}
	
	@Override
	public int count(NotCooperateCustomerBO notCooperateCustomerBO){
		return notCooperateCustomerDao.count(notCooperateCustomerBO);
	}

	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int save(NotCooperateCustomerDO notCooperateCustomer){
		// 获取复选框数据
		String customerPlatform = notCooperateCustomer.getCustomerPlatform();
		// 复选框最后会出现一个逗号,去掉最后一个逗号
		if(null != customerPlatform && !customerPlatform.equals("") && customerPlatform.endsWith(",")){
			notCooperateCustomer.setCustomerPlatform(customerPlatform.substring(0,customerPlatform.length()-1));
		}
		// 将合作同行属性由String类型转换成对象
		List<CooperatePeerDO> listCooperatePeerDO = JSON.parseArray(notCooperateCustomer.getCooperatePeerCase(), CooperatePeerDO.class);
		if(null != listCooperatePeerDO && listCooperatePeerDO.size()!=0){
			// 保存数据到合作同行表中
			for (CooperatePeerDO cooperatePeerDO : listCooperatePeerDO) {
				if (!cooperatePeerDO.getCooperatePrice().equals("") || !cooperatePeerDO.getCooperateRatio().equals("") || !cooperatePeerDO.getRemark().equals("")){
					cooperatePeerDao.save(cooperatePeerDO);
				}
			}
		}
		//创建后为省公司待处理状态
		notCooperateCustomer.setState(Constant.PROVINCE_WAIT_DEAL);
		//设置上传时间
		String time = DateUtils.formatDate(new Date());
		notCooperateCustomer.setTime(time);
		//判断网点编码不为空
		String branchCode = notCooperateCustomer.getBranchCode();
		if(null != branchCode && !"".equals(branchCode)){
			//获取网点所在的省(业务省)
			NotCooperateCustomerDO  notCooperateCustomerDO = notCooperateCustomerDao.getProvinceNameByBranchCode(branchCode);
			//获取网点所在的市
			String cityName = notCooperateCustomerDao.getCityNameByBranchCode(branchCode);
			//一级网点编码
			String mcCode = notCooperateCustomerDao.getMcByBranchCode(branchCode);
			//根据编码获取一级网点名称
			String mc = notCooperateCustomerDao.getMcCodeByBranchCode(mcCode);
			notCooperateCustomer.setProvince(notCooperateCustomerDO.getProvince());
			notCooperateCustomer.setProvinceId(notCooperateCustomerDO.getProvinceId());
			notCooperateCustomer.setCity(cityName);
			notCooperateCustomer.setMc(mc);
			notCooperateCustomer.setMcCode(mcCode);
		}
		//在客户处理进度表中存放各个网点的基础信息
		CustomerDealPlanDO customerDealPlanDO = getCustomerDealPlanDO(notCooperateCustomer);
		customerDealPlanDO.setTime(notCooperateCustomer.getTime());
		customerDealPlanDao.save(customerDealPlanDO);
		return notCooperateCustomerDao.save(notCooperateCustomer);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int update(NotCooperateCustomerDO notCooperateCustomer){
		//判断是否填写了合作网点编码和绑定客户vip账号
		//如果有就调用更新客户编码
		if(null != notCooperateCustomer.getBoundVipAccount() && !notCooperateCustomer.getBoundVipAccount().equals("")){
			updateBoundVipAccountById(notCooperateCustomer);
		}
		//在客户处理进度表中修改各个网点的基础信息
		CustomerDealPlanDO customerDealPlanDO = getCustomerDealPlanDO(notCooperateCustomer);
		customerDealPlanDao.update(customerDealPlanDO);
		//先删除再添加
		cooperatePeerDao.remove(notCooperateCustomer.getBranchCode(),notCooperateCustomer.getProductType());
		//将合作同行属性由String类型转换成对象
		List<CooperatePeerDO> listCooperatePeerDO = JSON.parseArray(notCooperateCustomer.getCooperatePeerCase(), CooperatePeerDO.class);
		if(null != listCooperatePeerDO && listCooperatePeerDO.size()!=0){
			//保存数据到合作同行表中
			for (CooperatePeerDO cooperatePeerDO : listCooperatePeerDO) {
				if (!cooperatePeerDO.getCooperatePrice().equals("") || !cooperatePeerDO.getCooperateRatio().equals("") || !cooperatePeerDO.getRemark().equals("")){
					cooperatePeerDO.setBranchCode(notCooperateCustomer.getBranchCode());
					cooperatePeerDao.save(cooperatePeerDO);
				}
			}
		}
		return notCooperateCustomerDao.update(notCooperateCustomer);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int remove(Integer id){
		//先根据id查询网点编码,删除同行的数据
		NotCooperateCustomerDO notCooperateCustomerDO = notCooperateCustomerDao.get(id);
		customerDealPlanDao.remove(notCooperateCustomerDO.getBranchCode());
		cooperatePeerDao.remove(notCooperateCustomerDO.getBranchCode(),notCooperateCustomerDO.getProductType());
		return notCooperateCustomerDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return notCooperateCustomerDao.batchRemove(ids);
	}

	/**
	 * 页面汇总字段逻辑
	 * @return
	 */
	@Override
	public String getSummary() {
		//先获取当前年月
		Calendar cal = Calendar.getInstance();
		String year = String.valueOf(cal.get(Calendar.YEAR));
		String month = String.valueOf(cal.get(Calendar.MONTH)+1);
		//拼接查询条件(开始时间-结束时间)
		String startTime = DateUtils.getReqDate(DateUtils.getMonthStart(new Date()))+" 00:00:00";
		String endTime = DateUtils.getReqDate(DateUtils.getMonthEnd(new Date()))+" 23:59:59";
		HashMap<String, Object> map = new HashMap<>();
		map.put("startDate",startTime);
		map.put("endDate",endTime);
		map.put("state","省公司待处理");
		//待处理量
		//【省公司待处理】状态客户的客户数量及日均件量之和
		Map<String,Object> pendingMap = notCooperateCustomerDao.queryByState(map);
		//已处理量
		//状态不为【省公司待处理】客户的客户数量及日均件量之和
		Map<String, Object> dealMap = notCooperateCustomerDao.queryByNotState(map);
		//以达成客户合作量
		map.put("state","已达成合作");
		Map<String, Object> cooperationMap = notCooperateCustomerDao.queryByNotState(map);
		//未合作合作客户数量1
		map.put("state","未达成合作");
		Map<String, Object> notCooperationMap = notCooperateCustomerDao.queryByNotState(map);
		//拼接给前端的数据
		//2019年02月，待处理1000/量2000，已处理100/量2000，已达成客户合作数200/量2000，未合作合作客户数100/量2000
		String summary = year+"年"+month+"月,"+"待处理"+pendingMap.get("customerNumber")+"/量"+pendingMap.get("sumDayAverageAmount")+
				",已处理"+dealMap.get("customerNumber")+"/量"+dealMap.get("sumDayAverageAmount")+
				",已达成客户合作数"+cooperationMap.get("customerNumber")+"/量"+cooperationMap.get("sumDayAverageAmount")+
				",未合作合作客户数"+notCooperationMap.get("customerNumber")+"/量"+notCooperationMap.get("sumDayAverageAmount");
		return summary;
	}

	/**
	 * 根据网点编码获取网点名
	 * @param orgCode
	 * @return
	 */
	@Override
	public String getCompanyNameByOrgCode(String orgCode) {
		return notCooperateCustomerDao.getgetCompanyNameByOrgCode(orgCode);
	}

	//根据省id获取省名称
	@Override
	public List<String> getProvinceNameByProvinceId(List<Long> provinceIds) {
		return notCooperateCustomerDao.getProvinceNameByProvinceId(provinceIds);
	}

	//根据省名称获取信息
	@Override
	public List<NotCooperateCustomerDO> listInfoByProvinceName(NotCooperateCustomerBO notCooperateCustomerBO) {
		return notCooperateCustomerDao.listInfoByProvinceName(notCooperateCustomerBO);
	}

	//根据声明称获取数量
	@Override
	public int countByProvinceName(NotCooperateCustomerBO notCooperateCustomerBO) {
		return notCooperateCustomerDao.countByProvinceName(notCooperateCustomerBO);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateBoundVipAccountById(NotCooperateCustomerDO notCooperateCustomer) {
        CustomerDealPlanDO customerDealPlanDO = new CustomerDealPlanDO();
        //拼接网点编码(网点编码+绑定的VIP账号)
        String customerCode=notCooperateCustomer.getBranchCode()+notCooperateCustomer.getBoundVipAccount();
        customerDealPlanDO.setBranchCode(notCooperateCustomer.getBranchCode());
        customerDealPlanDO.setCustomerCode(customerCode);
        customerDealPlanDO.setTime(notCooperateCustomer.getTime());
        customerDealPlanDao.update(customerDealPlanDO);
        notCooperateCustomer.setBoundTime(DateUtils.formatDate(new Date()));
		return notCooperateCustomerDao.updateBoundVipAccountById(notCooperateCustomer);
	}

	@Override
	public int checkCooperateBranch(String cooperateBranch) {
		return notCooperateCustomerDao.checkCooperateBranch(cooperateBranch);
	}

	@Override
	public List<CooperatePeerDO> huiXianCooperatePeer(String branchCode, String productType) {
		return notCooperateCustomerDao.huiXianCooperatePeer(branchCode,productType);
	}

	//抽出来的
	public CustomerDealPlanDO getCustomerDealPlanDO(NotCooperateCustomerDO notCooperateCustomer){
		CustomerDealPlanDO customerDealPlanDO = new CustomerDealPlanDO();
		if(null !=notCooperateCustomer.getProvince()){
			customerDealPlanDO.setProvinceName(notCooperateCustomer.getProvince());
		}
		if (null !=notCooperateCustomer.getProvinceId()){
			customerDealPlanDO.setProvinceId(notCooperateCustomer.getProvinceId());
		}
		if(null != notCooperateCustomer.getBranchName()){
			customerDealPlanDO.setBranchName(notCooperateCustomer.getBranchName());
		}
		if (null != notCooperateCustomer.getBranchCode()){
			customerDealPlanDO.setBranchCode(notCooperateCustomer.getBranchCode());
		}
		if (null != notCooperateCustomer.getMc()){
			customerDealPlanDO.setMc(notCooperateCustomer.getMc());
		}
		if (null != notCooperateCustomer.getMcCode()){
			customerDealPlanDO.setMcCode(notCooperateCustomer.getMcCode());
		}
		//上报客户数
		customerDealPlanDO.setCustomerNum("1");
		//上报客户单量
		if(null != notCooperateCustomer.getDayAverageAmount()){
			customerDealPlanDO.setAverageAmount(notCooperateCustomer.getDayAverageAmount());
		}
		//数据库默认字段是0
		if (null != notCooperateCustomer.getState() && !notCooperateCustomer.getState().equals("") && !notCooperateCustomer.getState().equals(Constant.PROVINCE_WAIT_DEAL)){
			//处理客户数
			customerDealPlanDO.setDealCustomerNum("1");
            //待处理客户数
            customerDealPlanDO.setWaitDealCustomerNum("0");
			//处理客户单量
			customerDealPlanDO.setDealCustomerAmount(notCooperateCustomer.getDayAverageAmount());
		}
		if(null != notCooperateCustomer.getState() && notCooperateCustomer.getState().equals(Constant.PROVINCE_WAIT_DEAL)){
			//待处理客户数
			customerDealPlanDO.setWaitDealCustomerNum("1");
		}

		if(null != notCooperateCustomer.getState() && notCooperateCustomer.getState().equals(Constant.SUCCESS_COOPERATE)){
			customerDealPlanDO.setSuccessCooperateNum("1");
		}
		customerDealPlanDO.setTime(notCooperateCustomer.getTime());
		return customerDealPlanDO;
	}

}
