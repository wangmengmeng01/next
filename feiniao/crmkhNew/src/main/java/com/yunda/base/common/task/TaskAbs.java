package com.yunda.base.common.task;

import com.alibaba.fastjson.JSON;
import com.yunda.base.common.config.Constant;
import com.yunda.base.system.config.SysConfig;
import com.yunda.ydmbspringbootstarter.common.utils.StringUtils;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 调度任务，互斥保护<br>
 * 
 * @author grimm
 * 
 */
public abstract class TaskAbs implements Job {
	static Logger log = Logger.getLogger(TaskAbs.class);

	public abstract void run(JobExecutionContext arg0);

	public abstract String whoareyou();
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	@Value("${taskAbs.url.ymlTaskCheck}")
	private String ymlTaskCheck;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			log.info("11------运行" + whoareyou());
			// 集群部署的时候，只有主服务才可以运行调度
			if (!checkMaster()) {
				log.info("------你不是主服务------");
				return;
			}
			log.info("------运行" + whoareyou());
			run(arg0);
		} catch (Exception e) {
			log.error("任务  ----- " + whoareyou() + "执行异常  ============ " + e.getMessage(), e);
		}
	}

	// 从集中式缓存中获取标识判断自己是不是主服务，只有主服务才可以运行调度任务
	public boolean checkMaster() {
		//application-dev.yml配置文件读取本台服务器是否参与竞争主服务以提供task任务
		if (StringUtils.isNoneBlank(ymlTaskCheck) && ymlTaskCheck.equals("close")) {
			log.debug("==调度，主服务判定：不参与主服务竞争");
			return false;
		}
		if (SysConfig.openTaskCheck == null || !SysConfig.openTaskCheck.equals("open")) {
			log.debug("==调度，主服务判定：我就是主服务");
			// 互斥检测功能的开关关闭，不用检测了
			return true;
		}

		//String info = JedisUtil.getByKey(Constant.SERVER_ID_CACHENAME);
		String info=stringRedisTemplate.opsForValue().get(Constant.SERVER_ID_CACHENAME);

		Vo_Common vo = null;
		if (StringUtils.isNotEmpty(info)) {
			try {
				vo = JSON.parseObject(info, Vo_Common.class);
			} catch (Exception e) {
				log.info("------TaskAbs的info-----" + info);
				log.error(e.getMessage(), e);
			}
		}

		if (vo == null) {
			// 申明自己为主
			vo = new Vo_Common();
			vo.setInfo(Constant.SERVER_ID);
			vo.setValue(System.currentTimeMillis());

			//JedisUtil.set(Constant.SERVER_ID_CACHENAME, JSON.toJSONString(vo));
			stringRedisTemplate.opsForValue().set(Constant.SERVER_ID_CACHENAME, JSON.toJSONString(vo));
			log.debug("==调度，主服务判定：申明自己为主");
			// System.out.println("==调度，主服务判定：申明自己为主");
			return true;
		} else {
			if (vo.getInfo().equals(Constant.SERVER_ID)) {
				// 续约
				vo.setValue(System.currentTimeMillis());

				//JedisUtil.set(Constant.SERVER_ID_CACHENAME, JSON.toJSONString(vo));
				stringRedisTemplate.opsForValue().set(Constant.SERVER_ID_CACHENAME, JSON.toJSONString(vo));
				log.debug("==调度，主服务判定：续约");
				// System.out.println("==调度，主服务判定：续约");
				return true;
			} else {
				long record = vo.getValue();
				if ((System.currentTimeMillis() - record) > Constant.SERVER_ID_DEADTIME) {
					// 夺权
					vo.setInfo(Constant.SERVER_ID);
					vo.setValue(System.currentTimeMillis());

					//JedisUtil.set(Constant.SERVER_ID_CACHENAME, JSON.toJSONString(vo));
					stringRedisTemplate.opsForValue().set(Constant.SERVER_ID_CACHENAME, JSON.toJSONString(vo));
					log.debug("==调度，主服务判定：夺权");
					// System.out.println("==调度，主服务判定：夺权");
					return true;
				} else {
					log.debug("==调度，主服务判定：待命");
					// System.out.println("==调度，主服务判定：待命");
				}
			}
		}

		return false;
	}

}

class Vo_Common {
	private String info;
	private long value;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

}
