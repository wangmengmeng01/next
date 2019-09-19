package com.yunda.base.system.service;

import com.yunda.base.system.domain.VideoDownloadDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-02-25131242
 */
public interface VideoDownloadService {
	
	VideoDownloadDO get(Long id);
	
	List<VideoDownloadDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(VideoDownloadDO videoDownload);
	
	int update(VideoDownloadDO videoDownload);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
