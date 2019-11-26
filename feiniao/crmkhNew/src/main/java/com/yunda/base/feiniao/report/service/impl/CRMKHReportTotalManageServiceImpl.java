/*
 * Filename	CRMKH_report_totalCacheServiceImpl.java
 * Company	上海东普信息科技有限公司。
 * @author	zhangFeng
 * @version	1.1.0
 */
package com.yunda.base.feiniao.report.service.impl;

import com.yunda.base.feiniao.schedule.suckdata.enums.SuckCacheKeyPerfixEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class CRMKHReportTotalManageServiceImpl {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	public static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);  
	/** 已经生成数据 */
	public static String REPORT_STATUS_OK="ok";
	/** 数据生成中 */
	public static String REPORT_STATUS_WAIT="wait";
	
	protected static final Logger logger = LoggerFactory.getLogger(CRMKHReportTotalManageServiceImpl.class);

	public String getTotalReportStatus(String startDate, String endDate){
		ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
		String result = operations.get(startDate+endDate+SuckCacheKeyPerfixEnum.zongbiao.getCode());		
		return result!=null?result:"no";
	}
	public String setTotalReportStatus(String startDate, String endDate, String status) {
		ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
		operations.set(startDate+endDate+SuckCacheKeyPerfixEnum.zongbiao.getCode(), status,86400, TimeUnit.SECONDS);
		return status;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			fixedThreadPool.submit(new Runnable() {
				@Override
				public void run() {
					try {
						System.out.println(">>>>>>>>>>>>>xxx" + System.currentTimeMillis());
						Thread.sleep(3000L);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
//						e.printStackTrace();
						logger.error(e.getMessage(), e);
					}
				}
			});
		}
		System.out.println("for end");
	}
}