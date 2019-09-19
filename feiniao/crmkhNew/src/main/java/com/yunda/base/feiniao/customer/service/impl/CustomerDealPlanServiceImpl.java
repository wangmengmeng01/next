package com.yunda.base.feiniao.customer.service.impl;

import com.yunda.base.common.config.Constant;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.feiniao.customer.bo.CustomerDealPlanBO;
import com.yunda.base.feiniao.customer.bo.NotCooperateCustomerBO;
import com.yunda.base.feiniao.customer.dao.CustomerDealPlanDao;
import com.yunda.base.feiniao.customer.dao.NotCooperateCustomerDao;
import com.yunda.base.feiniao.customer.domain.CustomerDealPlanDO;
import com.yunda.base.feiniao.customer.domain.NotCooperateCustomerDO;
import com.yunda.base.feiniao.customer.service.CustomerDealPlanService;
import com.yunda.base.feiniao.customer.service.NotCooperateCustomerService;
import com.yunda.base.system.domain.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerDealPlanServiceImpl implements CustomerDealPlanService {
	@Autowired
	private CustomerDealPlanDao customerDealPlanDao;

	@Override
	public CustomerDealPlanDO get(Integer id){
		return customerDealPlanDao.get(id);
	}
	
	@Override
	public PageUtils list(CustomerDealPlanBO customerDealPlanBO,UserDO loginUser){
		int total=0;
		String provinceIds ="";
		//拿到时间,先处理加上时分秒
		customerDealPlanBO.setStartDate(customerDealPlanBO.getStartDate()+" 00:00:00");
		customerDealPlanBO.setEndDate(customerDealPlanBO.getEndDate()+" 23:59:59");
		//判断对象中的隐藏的组织是否为空为空执行下面的,不为空就获取省下的所有1级网点(groupby一级网点编码,条件是省份名称)
        String organization_h = customerDealPlanBO.getOrganizationH();
        if(null != organization_h && !"".equals(organization_h)){
            //表明是下钻的
			List<CustomerDealPlanDO> listCustomerDealPlanDO = customerDealPlanDao.getDataByProvinceName(customerDealPlanBO);
			//根据查询时间和下钻后1级网点名称获取揽件量
			for (CustomerDealPlanDO customerDealPlanDO : listCustomerDealPlanDO) {
				//存放下钻的省名称
				customerDealPlanDO.setOrganizationH(organization_h);
                customerDealPlanDO.setTongjiStartDate(customerDealPlanBO.getTongjiStartDate());
                customerDealPlanDO.setTongjiEndDate(customerDealPlanBO.getTongjiEndDate());
				String changeCooperationAmount = customerDealPlanDao.getOrderNumByProvinceName(customerDealPlanDO);
				if(changeCooperationAmount==null){
                    changeCooperationAmount="0";
                }
				customerDealPlanDO.setChangeCooperationAmount(changeCooperationAmount);
			}
			total = customerDealPlanDao.countDataByProvinceName(customerDealPlanBO);
			PageUtils pageUtils = new PageUtils(listCustomerDealPlanDO, total);
            return pageUtils;
		}
		//先判断是不是总部或者省公司
		//1.判断登录人还是网点还是总部
		if (loginUser.hasPerms(Constant.REPORT_ADMIN_PERMS)) {
			// 超级用户权限 无限制
			//先从数据库中获取所有省的名称(根据时间和是否有填写组织)
			List<CustomerDealPlanDO> listCustomerDealPlanDO = customerDealPlanDao.getAllProvinceHuiZongData(customerDealPlanBO);
			//根据查询时间和省份获取揽件量
			for (CustomerDealPlanDO customerDealPlanDO : listCustomerDealPlanDO) {
				customerDealPlanDO.setTongjiStartDate(customerDealPlanBO.getTongjiStartDate());
				customerDealPlanDO.setTongjiEndDate(customerDealPlanBO.getTongjiEndDate());
				String changeCooperationAmount = customerDealPlanDao.getAllProvinceOrderNum(customerDealPlanDO);
                if(changeCooperationAmount==null){
                    changeCooperationAmount="0";
                }
				customerDealPlanDO.setChangeCooperationAmount(changeCooperationAmount);
			}
			total = customerDealPlanDao.countAllProvinceHuiZongData(customerDealPlanBO);
			PageUtils pageUtils = new PageUtils(listCustomerDealPlanDO, total);
			return pageUtils;
		} else {
			if (loginUser.isProvinceqx()) {// 是否有省权限
				//2.如果是业务省,获取业务省下面的所有网点信息,如果是网点就获取网点信息,如果是总部就查询所有
				List<Long> listProvinceId = loginUser.getProvinceIds();
				for (Long provinceId : listProvinceId) {
					provinceIds += provinceId+",";
				}
				provinceIds = provinceIds.substring(0,provinceIds.length()-1);
				customerDealPlanBO.setProvinceIds(provinceIds);
				List<CustomerDealPlanDO> listCustomerDealPlanDO = customerDealPlanDao.getProvinceHuiZongDataByProvinceIds(customerDealPlanBO);
				//根据查询时间和省份获取揽件量
				for (CustomerDealPlanDO customerDealPlanDO : listCustomerDealPlanDO) {
                    customerDealPlanDO.setTongjiStartDate(customerDealPlanBO.getTongjiStartDate());
                    customerDealPlanDO.setTongjiEndDate(customerDealPlanBO.getTongjiEndDate());
					String changeCooperationAmount = customerDealPlanDao.getProvinceOrderNum(customerDealPlanDO);
                    if(changeCooperationAmount==null){
                        changeCooperationAmount="0";
                    }
					customerDealPlanDO.setChangeCooperationAmount(changeCooperationAmount);
				}
				total = customerDealPlanDao.countProvinceHuiZongDataByProvinceIds(customerDealPlanBO);
				PageUtils pageUtils = new PageUtils(listCustomerDealPlanDO, total);
				return pageUtils;
			}  else {
				//表明是1级网点登录
				String mcCode = loginUser.getOrgCode();
				customerDealPlanBO.setMcCode(mcCode);
				List<CustomerDealPlanDO> listCustomerDealPlanDO = customerDealPlanDao.getMcHuiZongDataByMcCode(customerDealPlanBO);
				//根据查询时间和1级网点获取揽件量
				for (CustomerDealPlanDO customerDealPlanDO : listCustomerDealPlanDO) {
                    customerDealPlanDO.setTongjiStartDate(customerDealPlanBO.getTongjiStartDate());
                    customerDealPlanDO.setTongjiEndDate(customerDealPlanBO.getTongjiEndDate());
					String changeCooperationAmount = customerDealPlanDao.getMcOrderNum(customerDealPlanDO);
                    if(changeCooperationAmount==null){
                        changeCooperationAmount="0";
                    }
					customerDealPlanDO.setChangeCooperationAmount(changeCooperationAmount);
				}
				total = customerDealPlanDao.countMcHuiZongDataByMcCode(customerDealPlanBO);
				PageUtils pageUtils = new PageUtils(listCustomerDealPlanDO, total);
				return pageUtils;
			}
		}
	}
	
	@Override
	public int count(CustomerDealPlanBO customerDealPlanBO){
		return customerDealPlanDao.count(customerDealPlanBO);
	}
	
	@Override
	public int save(CustomerDealPlanDO customerDealPlan){
		return customerDealPlanDao.save(customerDealPlan);
	}
	
	@Override
	public int update(CustomerDealPlanDO customerDealPlan){
		return customerDealPlanDao.update(customerDealPlan);
	}
	
	@Override
	public int remove(String branchCode){
		return customerDealPlanDao.remove(branchCode);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return customerDealPlanDao.batchRemove(ids);
	}

	@Override
	public List<String> getProvinceNameByProvinceId(List<Long> provinceIds) {
		return customerDealPlanDao.getProvinceNameByProvinceId(provinceIds);
	}

	@Override
	public CustomerDealPlanDO getNumByProvinceName(CustomerDealPlanBO customerDealPlanBO) {
		return customerDealPlanDao.getNumByProvinceName(customerDealPlanBO);
	}

	@Override
	public CustomerDealPlanDO getNumByProvinceAndNotState(CustomerDealPlanBO customerDealPlanBO) {
		return customerDealPlanDao.getNumByNotState(customerDealPlanBO);
	}

	@Override
	public int getNumByProvinceAndState(CustomerDealPlanBO customerDealPlanBO) {
		return customerDealPlanDao.getNumByState(customerDealPlanBO);
	}

	@Override
	public CustomerDealPlanDO getNumByDateAndMcCode(CustomerDealPlanBO customerDealPlanBO) {
		return customerDealPlanDao.getNumByDateAndMcCode(customerDealPlanBO);
	}


}
