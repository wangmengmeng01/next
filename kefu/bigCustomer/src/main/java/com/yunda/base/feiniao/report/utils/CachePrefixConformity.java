package com.yunda.base.feiniao.report.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class CachePrefixConformity {

	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 拼接目标在redis中的key
	 * 
	 * @param key
	 *            业务定义的目标key
	 * @param subPerfix
	 *            业务命名空间
	 * @return 最终的拼接key
	 */
	public String getSeed(String key, String subPerfix) {
		ValueOperations<String, String> operations = TaskBeanUtils.getRedisTemplate().opsForValue();

		String value = operations.get(subPerfix);
		if (StringUtils.isBlank(value)) {
			value = resetSeed(subPerfix);
		}

		return value + key;
	}

	// 重置缓存命名空间
	public String resetSeed(String subPerfix) {
		String seedPro="";
		if(subPerfix!=null && !"".equals(subPerfix)){
		ValueOperations<String, String> operations = TaskBeanUtils.getRedisTemplate().opsForValue();
	    seedPro = System.currentTimeMillis() + "";
		operations.set(subPerfix, seedPro);
		}
		return seedPro;
	}

}
