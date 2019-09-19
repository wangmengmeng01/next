package com.yunda.base.common.task;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunda.base.feiniao.report.utils.DateUtils;
import com.yunda.base.feiniao.schedule.suckdata.vo.TaskParams;
import com.yunda.base.system.config.SysConfig;

/** 
 * @ClassName: DeleteVideoTask 
 * @Description: 视频全程录像定时删除任务
 * @author: 22374
 * @date: 2019年2月22日 下午12:00:21  
 */
public class DeleteVideoTask extends TaskAbs {
	Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void run(JobExecutionContext arg0) {
		TaskParams tp = TaskParams.newInstance(arg0);
		int from = tp.getTargetDay();
		String format = new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.getDate(from));

		String path = SysConfig.VIDEOFILEPATH + format;
		try {
			delDir(path);
		} catch (IOException e) {
			log.error("删除文件失败,原因是" + e.getMessage());
		}
	}

	public boolean delDir(String path) throws IOException {
		File file = new File(path);
		log.info("删除文件夹的路径[{}]", path);
		if (StringUtils.isNotBlank(path)) {
			if (file.exists()) {
				FileUtils.deleteDirectory(file);
				log.info("删除文件夹成功[{}]", path);
			} else {
				log.warn("文件夹不存在:[{}]", path);
			}
		}
		return false;
	}

	@Override
	public String whoareyou() {
		return "定时删除视频文件";
	}
}