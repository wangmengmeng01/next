package com.yunda.base.system.service;

import com.yunda.base.system.domain.FileExportDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-12-22232134
 */
public interface FileExportService {
	
	FileExportDO get(Long id);
	
	List<FileExportDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(FileExportDO fileExport);
	
	int update(FileExportDO fileExport);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
    //限制重复点击
	List<FileExportDO> getRepeatData(String filterJson, String className, String methodName, String username);
    //获取等待中的任务
	List<FileExportDO> getWaitingTask(String string);

	List<String> getTitle(Map<String, Object> params);
}
