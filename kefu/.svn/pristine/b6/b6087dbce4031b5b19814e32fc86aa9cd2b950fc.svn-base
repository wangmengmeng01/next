package com.yunda.base.system.controller;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.R;
import com.yunda.base.system.domain.AlarmDO;
import com.yunda.base.system.service.AlarmService;
/**
 *
 *
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-02-21151731
 */

@Controller
@RequestMapping("/system/alarm")
public class AlarmController {
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private AlarmService alarmService;

	@GetMapping()
	@RequiresPermissions("system:alarm:alarm")
	String Alarm(){
	    return "system/alarm/alarm";
	}

	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("system:alarm:alarm")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<AlarmDO> alarmList = alarmService.list(query);
		int total = alarmService.count(query);
		PageUtils pageUtils = new PageUtils(alarmList, total);
		return pageUtils;
	}


	@GetMapping("/add")
	@RequiresPermissions("system:alarm:add")
	String add(){
	    return "system/alarm/add";
	}

	@GetMapping("/edit/{sessionId}")
	@RequiresPermissions("system:alarm:edit")
	String edit(@PathVariable("sessionId") String sessionId,Model model){
		AlarmDO alarm = alarmService.get(sessionId);
		model.addAttribute("alarm", alarm);
	    return "system/alarm/edit";
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("system:alarm:add")
	public R save( AlarmDO alarm){
		if(alarmService.save(alarm)>0){
			return R.ok();
		}
		return R.error();
	}
}
