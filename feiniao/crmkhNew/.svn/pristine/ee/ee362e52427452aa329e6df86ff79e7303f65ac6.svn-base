package com.yunda.base.system.service.impl;

import com.yunda.base.system.dao.FileExportDao;
import com.yunda.base.system.domain.FileExportDO;
import com.yunda.base.system.service.FileExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service
public class FileExportServiceImpl implements FileExportService {
	@Autowired
	private FileExportDao fileExportDao;
	
	@Override
	public FileExportDO get(Long id){
		return fileExportDao.get(id);
	}
	
	@Override
	public List<FileExportDO> list(Map<String, Object> map){
		return fileExportDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return fileExportDao.count(map);
	}
	
	@Override
	public int save(FileExportDO fileExport){
		return fileExportDao.save(fileExport);
	}
	
	@Override
	public int update(FileExportDO fileExport){
		return fileExportDao.update(fileExport);
	}
	
	@Override
	public int remove(Long id){
		return fileExportDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return fileExportDao.batchRemove(ids);
	}

	@Override
	public List<FileExportDO> getRepeatData(String filterJson, String className, String methodName, String username) {
		return fileExportDao.getRepeatData(filterJson,className,methodName,username);
	}

	@Override
	public List<FileExportDO> getWaitingTask(String name) {
		// TODO Auto-generated method stub
		return fileExportDao.getWaitingTask(name);
	}

	@Override
	public List<String> getTitle(Map<String, Object> params) {
		return fileExportDao.getTitle(params);
	}
	
}
