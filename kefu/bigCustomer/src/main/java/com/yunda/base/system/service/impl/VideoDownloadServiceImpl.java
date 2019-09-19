package com.yunda.base.system.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.system.dao.VideoDownloadDao;
import com.yunda.base.system.domain.VideoDownloadDO;
import com.yunda.base.system.service.VideoDownloadService;



@Service
public class VideoDownloadServiceImpl implements VideoDownloadService {
	@Autowired
	private VideoDownloadDao videoDownloadDao;
	
	@Override
	public VideoDownloadDO get(Long id){
		return videoDownloadDao.get(id);
	}
	
	@Override
	public List<VideoDownloadDO> list(Map<String, Object> map){
		return videoDownloadDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return videoDownloadDao.count(map);
	}
	
	@Override
	public int save(VideoDownloadDO videoDownload){
		return videoDownloadDao.save(videoDownload);
	}
	
	@Override
	public int update(VideoDownloadDO videoDownload){
		return videoDownloadDao.update(videoDownload);
	}
	
	@Override
	public int remove(Long id){
		return videoDownloadDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return videoDownloadDao.batchRemove(ids);
	}
	
}
