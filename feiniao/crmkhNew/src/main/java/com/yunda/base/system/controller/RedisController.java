package com.yunda.base.system.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunda.base.common.utils.R;
import com.yunda.base.system.service.RedisService;
/**
 * 
 * 
 * @author chglee
 * @param <T>
 * @email 1992lcg@163.com
 * @date 2018-11-21 19:56:18
 */

@Controller
@RequestMapping("/system/redis")
public class RedisController<T> {
	@Autowired
	private RedisService redisService;

	@GetMapping()
	@RequiresPermissions("system:redis:redis")
	String Redis() {
		return "system/redis/redis";
	}

	@ResponseBody
	@PostMapping("/getRedis")
	public Map<String, Object> getRedis(@RequestParam Map<String, Object> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("key", redisService.getStr(params.get("key") + ""));
		return map;
	}

	@ResponseBody
	@PostMapping("/addRedis")
	public R addRedis(@RequestParam Map<String, Object> params) {
		if (StringUtils.isAnyBlank(params.get("key") + "", params.get("value") + "")) {
			return R.error("请填写key-or-value");
		}
		redisService.putStr(params.get("key") + "", params.get("value") + "");
		return R.ok("添加成功");
	}

	@ResponseBody
	@PostMapping("/deleteRedis")
	public R deleteRedis(@RequestParam Map<String, Object> params) {
		if (StringUtils.isBlank(params.get("key") + "")) {
			return R.error("请填写key");
		}
		if (StringUtils.isEmpty(redisService.getStr(params.get("key") + ""))) {
			return R.error("您要删除的key不存在");
		}
		redisService.delStr(params.get("key") + "");
		return R.ok("删除成功");
	}

	@ResponseBody
	@PostMapping("/updateRedis")
	public R updateRedis(@RequestParam Map<String, Object> params) {
		if (StringUtils.isAnyBlank(params.get("key") + "", params.get("value") + "")) {
			return R.error("请填写key-or-value");
		}
		if (StringUtils.isEmpty(redisService.getStr(params.get("key") + ""))) {
			return R.error("您要更新的key不存在");
		}
		redisService.delStr(params.get("key") + "");
		redisService.putStr(params.get("key") + "", params.get("value") + "");
		return R.ok("更新成功");
	}
}
