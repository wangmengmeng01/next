package com.yunda.base.system.dao;

import com.yunda.base.system.domain.FileExportDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-12-22232134
 */
@Mapper
public interface FileExportDao {

	FileExportDO get(Long id);
	
	List<FileExportDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(FileExportDO fileExport);
	
	int update(FileExportDO fileExport);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<FileExportDO> getRepeatData(@Param("executeParam")String filterJson, @Param("executeClass")String className, @Param("executeMethod")String methodName, @Param("userId")String username);

	List<FileExportDO> getWaitingTask(@Param("state")String name);

	List<String> getTitle(Map<String, Object> params);
}
