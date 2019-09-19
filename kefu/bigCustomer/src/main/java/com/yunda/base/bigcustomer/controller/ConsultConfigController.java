package com.yunda.base.bigcustomer.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.crab2died.ExcelUtils;
import com.yunda.base.bigcustomer.domain.ConsultConfigDO;
import com.yunda.base.bigcustomer.service.ConsultConfigService;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.R;
import com.yunda.base.system.config.SysConfig;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;

/**
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-22162719
 */

@Controller
@RequestMapping("/bigcustomer/consultConfig")
public class ConsultConfigController {
    Logger log = Logger.getLogger(getClass());
    @Autowired
    private ConsultConfigService consultConfigService;

    @GetMapping()
    @RequiresPermissions("bigcustomer:consultConfig:consultConfig")
    String ConsultConfig() {
        return "bigcustomer/consultConfig/consultConfig";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("bigcustomer:consultConfig:consultConfig")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<ConsultConfigDO> consultConfigList = consultConfigService.list(query);
        int total = consultConfigService.count(query);
        PageUtils pageUtils = new PageUtils(consultConfigList, total);
        return pageUtils;
    }

    // 导出excel
    @RequestMapping("/exportExcel")
    @MethodLock(key = "exportExcel")
    @RequiresPermissions("bigcustomer:consultConfig:exportExcel")
    public void exportExcel(HttpServletResponse response) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("limit", "10000");// 上限保护
        Query query = new Query(params);

        //int nums = consultConfigService.count(query);

        List<ConsultConfigDO> result = consultConfigService.list(query);
        String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";

        try {
            ExcelUtils.getInstance().exportObjects2Excel(result, ConsultConfigDO.class, targetFile);

            // 写入response
            File downloadFile = new File(targetFile);
            FileUtil.downloadByResponse(response, downloadFile);
            downloadFile.delete();
        } catch (Exception e) {
            //e.printStackTrace();
            log.error(e.getMessage(), e);
        }
    }

    // 下载excel模版
    @RequestMapping("/downTemplate")
    @MethodLock(key = "downTemplate")
    // 下载模版和导入共用同一个权限
    @RequiresPermissions("bigcustomer:consultConfig:importExcel")
    public void downTemplate(HttpServletResponse response) {
        String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
        try {
            ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<ConsultConfigDO>(), ConsultConfigDO.class, targetFile);

            // 写入response
            File downloadFile = new File(targetFile);
            FileUtil.downloadByResponse(response, downloadFile);
            downloadFile.delete();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            //e.printStackTrace();
        }
    }

    // 导入excel
    @ResponseBody
    @MethodLock(key = "importExcel")
    @RequestMapping(value = "/importExcel", consumes = "multipart/*", headers = "content-type=mutipart/form-data", method = RequestMethod.POST)
    @RequiresPermissions("bigcustomer:consultConfig:importExcel")
    public R importExcel(MultipartFile file) {
        List<ConsultConfigDO> list = null;

        String fileKey = UUID.randomUUID().toString();
        // 获取后缀
        String fileName = file.getOriginalFilename();
        if (fileName.lastIndexOf(".") != -1) {
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            String uploadFile = SysConfig.uploadPath + fileKey + suffix;

            File _f = new File(uploadFile);
            if (!_f.getParentFile().exists()) {
                _f.getParentFile().mkdirs();
            }

            BufferedOutputStream out = null;
            try {
                out = new BufferedOutputStream(new FileOutputStream(_f));
                out.write(file.getBytes());
                out.flush();

                list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, ConsultConfigDO.class, 0, 0);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                //e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }

        if (list != null) {
            for (ConsultConfigDO _do : list) {
                consultConfigService.save(_do);
            }
        }

        return R.ok();
    }

    @GetMapping("/add")
    @RequiresPermissions("bigcustomer:consultConfig:add")
    String add() {
        return "bigcustomer/consultConfig/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("bigcustomer:consultConfig:edit")
    String edit(@PathVariable("id") Integer id, Model model) {
        ConsultConfigDO consultConfig = consultConfigService.get(id);
        model.addAttribute("consultConfig", consultConfig);
        return "bigcustomer/consultConfig/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("bigcustomer:consultConfig:add")
    public R save(ConsultConfigDO consultConfig) {
        String dealShiXiao = "";
        String type = consultConfig.getType();
        if (type.equals("1")) {
            dealShiXiao = "发起后" + consultConfig.getOrderAfterHoursT1() + "小时内结单";
        }
        //判断不是0天
        if(type.equals("2") && !consultConfig.getOrderAfterDayT2().equals("0")){
            //将type为2的转化为1的小时
            int res = (Integer.parseInt(consultConfig.getOrderAfterDayT2())-1)*24+Integer.parseInt(consultConfig.getOrderAfterTimeT2());
            consultConfig.setOrderAfterHoursT1(res+"");
            dealShiXiao="当日发起,第"+consultConfig.getOrderAfterDayT2()+"天"+consultConfig.getOrderAfterTimeT2()+"点前结单";
        }else if(type.equals("2") && consultConfig.getOrderAfterDayT2().equals("0")){
            //这个其实是当天几点
            int res = (Integer.parseInt(consultConfig.getOrderAfterDayT2())-1)*24+Integer.parseInt(consultConfig.getOrderAfterTimeT2());
            consultConfig.setOrderAfterHoursT1(res+"");
            dealShiXiao="当日发起,第"+consultConfig.getOrderAfterDayT2()+"天"+consultConfig.getOrderAfterTimeT2()+"点前结单";
        }
        if(type.equals("3")){
            dealShiXiao="当日"+consultConfig.getTodayOrderBeforeTimeT31()+"前发起,第"+consultConfig.getOrderAfterDayT31()+"天"+consultConfig.getOrderAfterTimeT31()+"点前结单,"+"当天"+consultConfig.getTodayOrderAfterTime32()+"后发起,第"+consultConfig.getOrderBeforeDayT32()+"天"+consultConfig.getOrderBeforeTimeT32()+"点前结单";
        }

        consultConfig.setDealShiXiao(dealShiXiao);
        //对传递的条件进行判断
        consultConfig.setState("1");
        if (consultConfigService.save(consultConfig) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("bigcustomer:consultConfig:edit")
    public R update(ConsultConfigDO consultConfig) {
        String dealShiXiao = "";
        String type = consultConfig.getType();
        if (type.equals("1")) {
            dealShiXiao = "发起后" + consultConfig.getOrderAfterHoursT1() + "小时内结单";
        }
        //判断不是0天
        if(type.equals("2") && !consultConfig.getOrderAfterDayT2().equals("0")){
            //将type为2的转化为1的小时
            int res = (Integer.parseInt(consultConfig.getOrderAfterDayT2())-1)*24+Integer.parseInt(consultConfig.getOrderAfterTimeT2());
            consultConfig.setOrderAfterHoursT1(res+"");
            dealShiXiao="当日发起,第"+consultConfig.getOrderAfterDayT2()+"天"+consultConfig.getOrderAfterTimeT2()+"点前结单";
        }else if(type.equals("2") && consultConfig.getOrderAfterDayT2().equals("0")){
            //这个其实是当天几点
            int res = (Integer.parseInt(consultConfig.getOrderAfterDayT2())-1)*24+Integer.parseInt(consultConfig.getOrderAfterTimeT2());
            consultConfig.setOrderAfterHoursT1(res+"");
            dealShiXiao="当日发起,第"+consultConfig.getOrderAfterDayT2()+"天"+consultConfig.getOrderAfterTimeT2()+"点前结单";
        }
        if(type.equals("3")){
            dealShiXiao="当日"+consultConfig.getTodayOrderBeforeTimeT31()+"前发起,第"+consultConfig.getOrderAfterDayT31()+"天"+consultConfig.getOrderAfterTimeT31()+"点前结单,"+"当天"+consultConfig.getTodayOrderAfterTime32()+"后发起,第"+consultConfig.getOrderBeforeDayT32()+"天"+consultConfig.getOrderBeforeTimeT32()+"点前结单";
        }

        consultConfig.setDealShiXiao(dealShiXiao);
        consultConfigService.update(consultConfig);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("bigcustomer:consultConfig:remove")
    public R remove(Integer id) {
        if (consultConfigService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("bigcustomer:consultConfig:batchRemove")
    public R remove(@RequestParam("ids[]") Integer[] ids) {
        consultConfigService.batchRemove(ids);
        return R.ok();
    }


    /**
     * 修改状态(启用或者禁止)
     *
     * @return
     */
    @PostMapping("/stateUpdate")
    @ResponseBody
    public R stateUpdate(@ModelAttribute ConsultConfigDO consultConfigDO) {
        String state = consultConfigDO.getState();
        Integer id = consultConfigDO.getId();
        if (state.equals("1")) {
            //将状态改为禁用
            state = "0";
            consultConfigService.stateUpdate(id, state);
            return R.ok("已禁止");
        } else {
            //将状态改为禁用
            state = "1";
            consultConfigService.stateUpdate(id, state);
            return R.ok("已启用");
        }

    }
    /*
     * 从咨询类型设置表 获取咨询类型list
     */
    @GetMapping("/searchConsultType")
    @ResponseBody
    public List<String> searchConsultType(){

        List<String> consultType = consultConfigService.searchConsultType();
        return consultType;

    }

}
