package com.yunda.base.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.common.domain.Tree;
import com.yunda.base.common.utils.BuildTree;
import com.yunda.base.system.dao.CityDao;
import com.yunda.base.system.dao.ProvinceDao;
import com.yunda.base.system.dao.UserProvinceDao;
import com.yunda.base.system.domain.CityDO;
import com.yunda.base.system.domain.ProvinceDO;
import com.yunda.base.system.service.ProvinceService;

@Service
public class ProvinceServiceImpl implements ProvinceService {
	@Autowired
	private ProvinceDao provinceDao;
	@Autowired
	private UserProvinceDao userProvinceDao;
	
	@Autowired
	private CityDao cityDao;
	
	@Override
	public ProvinceDO get(String provinceid){
		return provinceDao.get(provinceid);
	}
	
	@Override
	public List<ProvinceDO> list(Map<String, Object> map){
		return provinceDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return provinceDao.count(map);
	}
	
	@Override
	public int save(ProvinceDO province){
		return provinceDao.save(province);
	}
	
	@Override
	public int update(ProvinceDO province){
		return provinceDao.update(province);
	}
	
	@Override
	public int remove(String provinceid){
		return provinceDao.remove(provinceid);
	}
	
	@Override
	public int batchRemove(String[] provinceids){
		return provinceDao.batchRemove(provinceids);
	}

	@Override
	public Tree<ProvinceDO> getTree(Long id) {
		// 根据roleId查询权限
				//List<ProvinceDO> provinces = provinceDao.list(new HashMap<String, Object>(16));
				List<Long> provinceIds = userProvinceDao.listProvinceIdByUserId(id);
				//List<Long> temp = provinceIds;
				/*for(ProvinceDO province : provinces) {
					if (temp.contains(province.getParentId())) {
						provinceIds.remove(province.getParentId());
					}
				}*/
				List<Tree<ProvinceDO>> trees = new ArrayList<Tree<ProvinceDO>>();
				List<ProvinceDO> provinceDOs = provinceDao.list(new HashMap<String, Object>(16));
				for (ProvinceDO sysProvinceDO : provinceDOs) {
					Tree<ProvinceDO> tree = new Tree<ProvinceDO>();
					tree.setId(sysProvinceDO.getProvinceid());
					//tree.setParentId(sysProvinceDO.getParentId().toString());
					tree.setText(sysProvinceDO.getProvincename());
					Map<String, Object> state = new HashMap<>(16);
					String provinceid = sysProvinceDO.getProvinceid();
					long provinceId =  Long.parseLong(provinceid);
					if (provinceIds.contains(provinceId)) {
						state.put("selected", true);
					} else {
						state.put("selected", false);
					}
					tree.setState(state);
					trees.add(tree);
				}
				// 默认顶级菜单为０，根据数据库实际情况调整
				Tree<ProvinceDO> t = BuildTree.build(trees);
				return t;
	}
	
	@Override
	public Tree<CityDO> getCityTree(Long ProvinceID) {
		// 根据roleId查询权限
				HashMap<String, Object>  map  = new HashMap<String, Object>(16);
				map.put("provinceid", ProvinceID);
				//获取该省总公司所管辖的市
				List<Long> cityIds = cityDao.list(map);
				List<Tree<CityDO>> trees = new ArrayList<Tree<CityDO>>();
				String provinceId = String.valueOf(ProvinceID);
				String provinceid = "%" + provinceId.substring(0, 4) + "%";
				List<CityDO> allCitys = cityDao.getAllCitylist(provinceid);
				for (CityDO city : allCitys) {
					Tree<CityDO> tree = new Tree<CityDO>();
					tree.setId(city.getCityid());
					tree.setText(city.getCityname());
					Map<String, Object> state = new HashMap<>(16);
					String cityId = city.getCityid();
					long cityid =  Long.parseLong(cityId);
					if (cityIds.contains(cityid)) {
						state.put("selected", true);
					} else {
						state.put("selected", false);
					}
					tree.setState(state);
					trees.add(tree);
				}
				// 默认顶级菜单为０，根据数据库实际情况调整
				Tree<CityDO> t = BuildTree.build(trees);
				return t;
	}

	public List<Long> queryUP(Long userId) {
		List<Long> provinceIds = userProvinceDao.queryUP(userId);
		return provinceIds;
	}

	@Override
	public List<ProvinceDO> maintainProvincelist(Map<String, Object> map) {
		return provinceDao.maintainProvincelist(map);
	}

	@Override
	/**
	 * 更新省总公司管辖市
	 */
	public int updateCity(ProvinceDO province) {
		int r = 0;
		String provinceId = province.getProvinceid();
		List<Long> cityIds = province.getCityIds();
		if(cityIds.size() > 0){
			for(int i = 0;i < cityIds.size();i++){
				if(cityIds.get(i) == -1){
					cityIds.remove(i);
					i--;
				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("provinceid", provinceId);
			for(Long cityid:cityIds){
				map.put("cityid", cityid);
				r = cityDao.updateCity(map);
				map.remove(cityid.toString());//泛型是String类型的,传的键是Long
			}
		}
		
		return r;
	}
	
}
