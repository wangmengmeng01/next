package com.yunda.base.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.common.domain.Tree;
import com.yunda.base.common.utils.BuildTree;
import com.yunda.base.system.dao.BigareaDao;
import com.yunda.base.system.dao.UserBigareaDao;
import com.yunda.base.system.domain.BigareaDO;
import com.yunda.base.system.service.BigareaService;


@Service
public class BigareaServiceImpl implements BigareaService {
	@Autowired
	private BigareaDao bigareaDao;
	
	@Autowired
	private UserBigareaDao userBigareaDao;
	
	@Override
	public BigareaDO get(String bigareaname){
		return bigareaDao.get(bigareaname);
	}
	
	@Override
	public List<BigareaDO> list(Map<String, Object> map){
		return bigareaDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return bigareaDao.count(map);
	}
	
	@Override
	public int save(BigareaDO bigarea){
		return bigareaDao.save(bigarea);
	}
	
	@Override
	public int update(BigareaDO bigarea){
		return bigareaDao.update(bigarea);
	}
	
	@Override
	public int remove(String bigareaname){
		return bigareaDao.remove(bigareaname);
	}
	
	@Override
	public int batchRemove(String[] bigareanames){
		return bigareaDao.batchRemove(bigareanames);
	}

	@Override
	public Tree<BigareaDO> getTree(Long id) {
		// 根据userId查询大区权限
		List<String> bigareaNames = userBigareaDao.listBigareaNameByUserId(id);
		List<Tree<BigareaDO>> trees = new ArrayList<Tree<BigareaDO>>();
		List<BigareaDO> bigareaDOs = bigareaDao.getAllBigareaName();
		for (BigareaDO sysBigareaDO : bigareaDOs) {
			Tree<BigareaDO> tree = new Tree<BigareaDO>();
			tree.setId(sysBigareaDO.getBigareaname());
			tree.setText(sysBigareaDO.getBigareaname());
			Map<String, Object> state = new HashMap<>(16);
			if (bigareaNames.contains(sysBigareaDO.getBigareaname())) {
				state.put("selected", true);
			} else {
				state.put("selected", false);
			}
			tree.setState(state);
			trees.add(tree);
		}
		// 默认顶级菜单为０，根据数据库实际情况调整
		Tree<BigareaDO> t = BuildTree.build(trees);
		return t;
	}

	public List<String> queryUP(Long userId) {
		List<String> bigarea = userBigareaDao.queryUP(userId);
		return bigarea;
	}
	
}
