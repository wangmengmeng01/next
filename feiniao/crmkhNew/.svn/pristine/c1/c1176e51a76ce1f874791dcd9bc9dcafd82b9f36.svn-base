package com.yunda.base.feiniao.report.service;

import com.yunda.base.system.config.SysConfig;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ReportThreadPoolManager {
	// 创建线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
	// corePoolSize： 线程池维护线程的最少数量
	// maximumPoolSize：线程池维护线程的最大数量
	// keepAliveTime： 线程池维护线程所允许的空闲时间
	// unit： 线程池维护线程所允许的空闲时间的单位
	// workQueue： 线程池所使用的缓冲队列
	// handler： 线程池对拒绝任务的处理策略
	private static ThreadPoolExecutor fluctuateThreadPool = new ThreadPoolExecutor(1,
			Integer.parseInt(SysConfig.thread_nums_fluctuate), 60L, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy()); // 不在新线程中执行任务，而是由调用者所在的线程来执行

	private static ThreadPoolExecutor custRewardThreadPool = new ThreadPoolExecutor(1,
			Integer.parseInt(SysConfig.thread_nums_custReward), 60L, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy()); // 不在新线程中执行任务，而是由调用者所在的线程来执行

	private static ThreadPoolExecutor totaldataThreadPool = new ThreadPoolExecutor(1,
			Integer.parseInt(SysConfig.thread_nums_totaldata), 60L, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy()); // 不在新线程中执行任务，而是由调用者所在的线程来执行

	private static ThreadPoolExecutor warningThreadPool = new ThreadPoolExecutor(1,
			Integer.parseInt(SysConfig.thread_nums_warning), 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
			new ThreadPoolExecutor.CallerRunsPolicy()); // 不在新线程中执行任务，而是由调用者所在的线程来执行

	private static ThreadPoolExecutor commonThreadPool = new ThreadPoolExecutor(1,
			Integer.parseInt(SysConfig.thread_nums_common), 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
			new ThreadPoolExecutor.CallerRunsPolicy());

	public static ThreadPoolExecutor getPool(String name) {
		if (name.equalsIgnoreCase("fluctuate")) {
			return fluctuateThreadPool;
		} else if (name.equalsIgnoreCase("custReward")) {
			return custRewardThreadPool;
		} else if (name.equalsIgnoreCase("totaldata")) {
			return totaldataThreadPool;
		} else if (name.equalsIgnoreCase("warning")) {
			return warningThreadPool;
		}
		return commonThreadPool;
	}

}
