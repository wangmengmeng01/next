package com.yunda.base.feiniao.schedule.suckdata.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.feiniao.schedule.suckdata.dao.RecordSuckDao;
import com.yunda.base.feiniao.schedule.suckdata.domain.RecordSuckDO;
import com.yunda.base.feiniao.schedule.suckdata.service.RecordSuckService;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;

@Service
public class RecordSuckServiceImpl implements RecordSuckService {
	@Autowired
	private RecordSuckDao recordSuckDao;

	@Override
	public RecordSuckDO get(Integer id) {
		return recordSuckDao.get(id);
	}

	@Override
	public List<RecordSuckDO> list(Map<String, Object> map) {
		return recordSuckDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return recordSuckDao.count(map);
	}

	@Override
	public int save(RecordSuckDO recordSuck) {
		return recordSuckDao.save(recordSuck);
	}

	@Override
	public int update(RecordSuckDO recordSuck) {
		return recordSuckDao.update(recordSuck);
	}

	@Override
	public int remove(Integer id) {
		return recordSuckDao.remove(id);
	}

	@Override
	public int batchRemove(Integer[] ids) {
		return recordSuckDao.batchRemove(ids);
	}

	// 目标日期是否已经抽过数据
	@Override
	public boolean alreadySuck(int targetDay) {
		Map<String, Object> map = new HashMap<String, Object>();

		int td = DateUtils.convertDate2Int4Day(DateUtils.getDate(targetDay));
		map.put("suckDate", td);
		map.put("delFlag", "0");

		int count = recordSuckDao.count(map);

		return count > 0;
	}

	// 数据做删除标记
	@Override
	public int delMark(Map<String, Object> map) {
		return recordSuckDao.delMark(map);
	}

}
