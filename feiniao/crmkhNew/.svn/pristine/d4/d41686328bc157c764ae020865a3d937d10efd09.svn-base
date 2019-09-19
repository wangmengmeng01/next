package com.yunda.base.common.task;

import com.yunda.base.feiniao.report.utils.DateUtils;
import com.yunda.base.system.config.SysConfig;
import org.apache.commons.io.FileUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class DeleteFileTask extends TaskAbs {
	Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void run(JobExecutionContext arg0) {
		String path = SysConfig.exportExcelPath + DateUtils.getReqDate();
		try {
			deleteFile(path);
		} catch (IOException e) {
			log.error("删除文件失败,原因是" + e);
		}
	}

	private void deleteFile(String path) throws IOException {
		if (path != null) {
			File file = new File(path);
			FileUtils.deleteDirectory(file);
		}
	}

	@Override
	public String whoareyou() {
		return "按天定时删除文件任务";
	}
}