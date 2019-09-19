package com.yunda.base.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yunda.base.system.domain.VideoDownloadDO;

/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-02-25131242
 */
@Mapper
public interface VideoDownloadDao {

	VideoDownloadDO get(Long id);
	
	List<VideoDownloadDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(VideoDownloadDO videoDownload);
	
	int update(VideoDownloadDO videoDownload);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
