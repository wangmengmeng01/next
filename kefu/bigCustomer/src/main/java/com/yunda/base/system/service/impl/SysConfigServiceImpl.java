package com.yunda.base.system.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.dao.SysConfigDao;
import com.yunda.base.system.domain.SysConfigDO;
import com.yunda.base.system.service.SysConfigService;
import com.yunda.ydmbspringbootstarter.common.utils.ObjectUtils;
@Service
public class SysConfigServiceImpl implements SysConfigService {

	private static final Logger log = LoggerFactory.getLogger(SysConfigServiceImpl.class);

	@Autowired
	SysConfigDao sysConfigDao;

	// 给支付网关PayGateConfig配置对象中的静态属性赋值
	@Override
	public void initConfig() {
		// 获取所有支付网关表中所有数据
		List<SysConfigDO> list = sysConfigDao.queryAll();
		if (list != null && list.size() > 0) {
			// 配置数据放入对象
			for (SysConfigDO config : list) {
				// 反射注入
				try {
					// 给支付网关配置对象的静态属性赋值
					ObjectUtils.setStaticValue(SysConfig.class, config.getKey(), config.getValue());
				} catch (Exception e) {
					log.error("给支付网关配置对象GlobalConstant的静态属性赋值失败" + config.getKey() + "=" + config.getValue(), e);
				}
			}
		}

		// 硬编码处理Set
		SysConfig.configFilters.clear();
		if (StringUtils.isNotBlank(SysConfig.configFilter)) {
			if (SysConfig.configFilters == null) {
				SysConfig.configFilters = new HashSet<String>();
			}
			SysConfig.configFilters.clear();
			SysConfig.configFilters.addAll(Arrays.asList(SysConfig.configFilter.split(",")));
		}
		if(StringUtils.isNotBlank(SysConfig.suckLimit)) {
			SysConfig.suckLimitInt = Integer.valueOf(SysConfig.suckLimit);
		}

		// 赋值完成后，检测下是否有空参数，如果有给出警告提示开发人员配置config
		String alarm = "";
		try {
			Field[] fields = SysConfig.class.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				if (Modifier.isStatic(field.getModifiers())) {
					Object obj = field.get(null);

					// System.out.println(field.getName() + "-> " + obj);
					if (field.getType() == String.class) {
						String str = (String) obj;
						if (StringUtils.isBlank(str) || str.equals("null")) {
							if (alarm.length() > 1) {
								alarm += "，";
							}
							alarm += field.getName();
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		if (StringUtils.isNotBlank(alarm)) {
			log.debug("***************************************");
			log.debug(">>警告：有配置参数没有从数据库中得到：" + alarm);
			log.debug("***************************************");
		}
	}

	@Override
	public SysConfigDO get(Integer id) {
		return sysConfigDao.get(id);
	}

	@Override
	public List<SysConfigDO> list(Map<String, Object> map) {
		return sysConfigDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return sysConfigDao.count(map);
	}

	@Override
	public int save(SysConfigDO config) {
		return sysConfigDao.save(config);
	}

	@Override
	public int update(SysConfigDO config) {
		return sysConfigDao.update(config);
	}

	@Override
	public int remove(Integer id) {
		return sysConfigDao.remove(id);
	}

	@Override
	public int batchRemove(Integer[] ids) {
		return sysConfigDao.batchRemove(ids);
	}

}
