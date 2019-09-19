package com.yunda.base.common.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunda.base.common.domain.TaskDO;
import com.yunda.base.common.service.JobService;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.R;

@Controller
@RequestMapping("/common/job")
public class JobController extends BaseController {
	private static final Logger LOGGER = LoggerFactory.getLogger(JobController.class);

	@Autowired
	private JobService taskScheduleJobService;

	@GetMapping()
	String taskScheduleJob() {
		return "common/job/job";
	}

	@ResponseBody
	@GetMapping("/list")
	public PageUtils list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		List<TaskDO> taskScheduleJobList = taskScheduleJobService.listJob(query);
		 
		int total = taskScheduleJobService.count(query);
		PageUtils pageUtils = new PageUtils(taskScheduleJobList, total);
		return pageUtils;
	}

	@GetMapping("/add")
	String add() {
		return "common/job/add";
	}

	@GetMapping("/edit/{id}")
	String edit(@PathVariable("id") Long id, Model model) {
		TaskDO job = taskScheduleJobService.get(id);
		model.addAttribute("job", job);
		return "common/job/edit";
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Long id) {
		TaskDO taskScheduleJob = taskScheduleJobService.get(id);
		return R.ok().put("taskScheduleJob", taskScheduleJob);
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	public R save(@Valid TaskDO taskScheduleJob ,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			return R.error(fieldError.getField() + ":" + fieldError.getDefaultMessage());
		}

		if (taskScheduleJobService.save(taskScheduleJob) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@PostMapping("/update")
	public R update(@Valid TaskDO taskScheduleJob ,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			return R.error(fieldError.getField() + ":" + fieldError.getDefaultMessage());
		}
		taskScheduleJobService.update(taskScheduleJob);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	public R remove(Long id) {
		if (taskScheduleJobService.remove(id) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 删除
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	public R remove(@RequestParam("ids[]") Long[] ids) {
		taskScheduleJobService.batchRemove(ids);

		return R.ok();
	}

	@PostMapping(value = "/changeJobStatus")
	@ResponseBody
	public R changeJobStatus(Long id, String cmd) {
		String label = "停止";
		if ("start".equals(cmd)) {
			label = "启动";
		} else {
			label = "停止";
		}
		try {
			taskScheduleJobService.changeStatus(id, cmd);
			return R.ok("任务" + label + "成功");
		} catch (Exception e) {
			LOGGER.error("changeJobStatus Error", e);
		}
		return R.ok("任务" + label + "失败");
	}

}
