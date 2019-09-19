package com.yunda.base.bigcustomer.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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

import com.github.crab2died.ExcelUtils;
import com.yunda.base.bigcustomer.domain.JieDanDO;
import com.yunda.base.bigcustomer.domain.OperateDO;
import com.yunda.base.bigcustomer.domain.OrderDO;
import com.yunda.base.bigcustomer.service.OrderService;
import com.yunda.base.bigcustomer.service.OrganizationManageService;
import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.R;
import com.yunda.base.common.utils.ShiroUtils;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.service.RoleService;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;

@Controller
@RequestMapping("/bigcustomer/orderChi")
public class OrderChiController extends BaseController {
    Logger log = Logger.getLogger(getClass());
    @Autowired
    private OrderService orderService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private OrganizationManageService organizationManageService;

    @GetMapping()
    @RequiresPermissions("bigcustomer:orderChi:orderChi")
    String Order() {
        return "bigcustomer/orderChi/orderChi";
    }

    @GetMapping("/orderByWillbillNum/{startDate}/{endDate}/{waybillNum}")
    @RequiresPermissions("bigcustomer:orderChi:orderChi")
    String OrderByWillbillNum(@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate, @PathVariable("waybillNum") String waybillNum, Model model) {
        Map<String, Object> map = new HashMap<>();
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("waybillNum", waybillNum);
        List orderList = orderService.getListByWaybillNum(map);
        model.addAttribute("orderList", orderList);
        return "bigcustomer/orderChi/orderChi";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("bigcustomer:orderChi:orderChi")
    public RspBean<PageUtils> list(@RequestParam Map<String, Object> params) {
        String waybillNum = (String) params.get("waybillNum");
        if(waybillNum.contains("\n")){
            waybillNum = waybillNum.replaceAll("\n",",");
        }
        if(waybillNum!=null && !waybillNum.equals("")){
            if(waybillNum.contains(",,")){
                waybillNum = waybillNum.replaceAll(",,", ",");
            }
            if(waybillNum.contains(",,,")){
                waybillNum = waybillNum.replaceAll(",,,", ",");
            }
            if(waybillNum.endsWith(",")){
                waybillNum=waybillNum.substring(0,waybillNum.length()-1);
            }
            if(waybillNum.endsWith(",,")){
                waybillNum=waybillNum.substring(0,waybillNum.length()-2);
            }
            if(waybillNum.endsWith(",,,")){
                waybillNum=waybillNum.substring(0,waybillNum.length()-3);
            }
            params.put("waybillNum","("+waybillNum+")");
        }
        //获取用户数据权限判断是什么权限
        Long userId = ShiroUtils.getUser().getUserId();
        String username = ShiroUtils.getUser().getUsername();
        String roleName = orderService.getRoleNameByUserName(username);
        //只要不是总部角色以及超级管理员外其他的都只能看到本部门的
       if (!roleName.equals("总部") && !roleName.equals("超级用户角色")) {
            //代表是本部门(获取本部门所有人的账号,可以看本部门处理和本部门创建的数据)
            //1.获取当前用户的所属机构下的所有人的账号
            String orgCode = orderService.getOrgCodeByUserName(username);
            //2.根据机构编码获取该机构下的所有用户账号
            /*List<String> userNameList = orderService.getUserNameListByOrgCode(orgCode);
            String res = "";
            if (userNameList.size() > 1) {
                for (String userName : userNameList) {
                    res += "'" + userName + "',";
                }
                if (res.endsWith(",")) {
                    res = res.substring(0, res.length() - 1);
                }
            } else {
                res = "'" + userNameList.get(0) + "'";
            }
            params.put("dataPermissions2", res);*/
            params.put("dataPermissions2", "2");
            params.put("orgCode", orgCode);
        }
        //可以查看所有数据,不用做任何操作
        //如果时间没传递过来,默认查询当天
        if (params.get("startDate").equals("") || params.get("endDate").equals("")) {
            params.put("startDate", DateUtils.formatDate(DateUtils.getDate(new Date(),-15), "yyyy-MM-dd 00:00:00"));
            params.put("endDate", DateUtils.formatDate(new Date(), "yyyy-MM-dd 23:59:59"));
        }

        if (params.get("shenLingI").equals("true")) {
            //查询条件中勾选了我申领的
            //获取用户账号
            params.put("dealCode", username);
        }
        if (params.get("faQiI").equals("true")) {
            //查询条件中勾选了我发起的
            params.put("faqiCode", username);
        }
        //如果有勾选时效预警状态就作为条件在遍历的时候判断
        final String shiXiaoState = (String) params.get("shiXiaoState");
        //查询列表数据
        Query query = new Query(params);
        //展示时效预警和剩余时间,如果是超时则剩余时间为0
        List<OrderDO> orderList = orderService.list(query);
        //遍历后判断超时,预警,正常,以及剩余时间(使用迭代器,因为下面有要删除自己元素的,使用其他遍历都会出现问题)
        Iterator<OrderDO> iter = orderList.iterator();
        //获取当前时间的小时数
        Long currentHours = 0L;
        Long resH =0L;
        String date = "";
        while (iter.hasNext()) {
            OrderDO orderDO = iter.next();
            String consultTime = orderDO.getConsultTime();
            String hh = consultTime.substring(11, 13);
            int consultTimeHours = Integer.parseInt(hh);
            if(orderDO.getJieDanTime()!=null && !orderDO.getJieDanTime().equals("")){
                resH = DateUtils.dateDiffHours(consultTime, orderDO.getJieDanTime(), "yyyy-MM-dd HH:mm:ss");
                currentHours = Long.parseLong(orderDO.getJieDanTime().substring(11, 13));
                date= orderDO.getJieDanTime().substring(0,10);
            }else {
                resH = DateUtils.dateDiffHours(consultTime, DateUtils.getCurrentTime(), "yyyy-MM-dd HH:mm:ss");
                currentHours = Long.parseLong(DateUtils.format(new Date(), "HH"));
                date = DateUtils.format(new Date(), "yyyy-MM-dd");
            }
            orderDO = getYuJingOrderDO(orderDO, currentHours, resH, consultTimeHours,date);
            //如果有选择预警状态这个条件,那么就把不满足的对象从list集合中去除(orderDO.getShiXiaoState()!=null,这个条件放到第二个)
            if (!shiXiaoState.equals("")) {
                if (orderDO.getShiXiaoState() != null && !orderDO.getShiXiaoState().equals(shiXiaoState)) {
                    //迭代器的删除办法(单线程情况下)
                    iter.remove();
                } else if (orderDO.getShiXiaoState() == null) {
                    //迭代器的删除办法(单线程情况下)
                    iter.remove();
                }

            }
            //判断如果是已申领就将剩余时间改为"-"
            if(orderDO.getState().equals("已结单")){
                orderDO.setShengYuTime("-");
            }
        }

        int total = orderService.count(query);
        PageUtils pageUtils = new PageUtils(orderList, total);
        return success(pageUtils);
    }


    //把判断语句抽出来
    public OrderDO getYuJingOrderDO(OrderDO orderDO,Long currentHours,Long resH,int consultTimeHours,String date){
        //存入的时候将2的转化成小时
        if (orderDO.getType() != null && (orderDO.getType().equals("1") || orderDO.getType().equals("2"))) {
            //判断如果勾选2并且天为0
            if (orderDO.getType().equals("2") && orderDO.getOrderAfterDayT2().equals("0")) {
                //勾选了第二种,并且天数为0即当天,比较当前时间的小时数,如果当前时间的小时数大于等于配置中的即为超时(将时间转为毫秒值比较)
                if (!date.equals(orderDO.getConsultTime().substring(0, 10)) || (date.equals(orderDO.getConsultTime().substring(0, 10)) && currentHours >= Integer.parseInt(orderDO.getOrderAfterTimeT2()))) {
                    orderDO.setShiXiaoState("超时");
                    //超时的话就把剩余时间设置为0
                    orderDO.setShengYuTime("0");
                } else if (orderDO.getYujingTime() != null && date.equals(orderDO.getConsultTime().substring(0, 10)) && currentHours <= Integer.parseInt(orderDO.getOrderAfterTimeT2()) && Integer.parseInt(orderDO.getOrderAfterTimeT2()) - currentHours <= Integer.parseInt((orderDO.getYujingTime().equals("") ? "0" : orderDO.getYujingTime()))) {
                    //如果resH的时间小于设定的超时时间,并且减去设定的超时小时不为福则为预警
                    orderDO.setShiXiaoState("预警");
                    orderDO.setShengYuTime(Integer.parseInt(orderDO.getOrderAfterTimeT2()) - currentHours + "小时");
                } else {
                    orderDO.setShiXiaoState("正常");
                    orderDO.setShengYuTime(Integer.parseInt(orderDO.getOrderAfterTimeT2()) - currentHours + "小时");
                }
            } else if (orderDO.getType().equals("2") && !orderDO.getOrderAfterDayT2().equals("0")) {
                //如果是类型1的话就按照1的方式去去判断是否超时以及剩余时间,预警等信息
                int orderAfterHoursT1 = Integer.parseInt(orderDO.getOrderAfterHoursT1()) + 24 - consultTimeHours - 1;
                //获取当前时间,然后判断,如果当前时间减去咨询时间大于类型1的时间则状态为超时
                if (resH >= orderAfterHoursT1) {
                    orderDO.setShiXiaoState("超时");
                    //超时的话就把剩余时间设置为0
                    orderDO.setShengYuTime("0");
                } else if (orderDO.getYujingTime() != null && resH <= orderAfterHoursT1 && orderAfterHoursT1 - resH <= Integer.parseInt((orderDO.getYujingTime().equals("") ? "0" : orderDO.getYujingTime()))) {
                    //如果resH的时间小于设定的超时时间,并且减去设定的超时小时不为福则为预警
                    orderDO.setShiXiaoState("预警");
                    orderDO.setShengYuTime(orderAfterHoursT1 - resH + "小时");
                } else {
                    orderDO.setShiXiaoState("正常");
                    orderDO.setShengYuTime(orderAfterHoursT1 - resH + "小时");
                }
            } else {
                //如果是类型1的话就按照1的方式去去判断是否超时以及剩余时间,预警等信息
                String orderAfterHoursT1 = orderDO.getOrderAfterHoursT1();
                //获取当前时间,然后判断,如果当前时间减去咨询时间大于类型1的时间则状态为超时
                if (resH >= Integer.parseInt(orderAfterHoursT1)) {
                    orderDO.setShiXiaoState("超时");
                    //超时的话就把剩余时间设置为0
                    orderDO.setShengYuTime("0");
                } else if (orderDO.getYujingTime() != null && resH <= Integer.parseInt(orderAfterHoursT1) && Integer.parseInt(orderAfterHoursT1) - resH <= Integer.parseInt((orderDO.getYujingTime().equals("") ? "0" : orderDO.getYujingTime()))) {
                    //如果resH的时间小于设定的超时时间,并且减去设定的超时小时不为福则为预警
                    orderDO.setShiXiaoState("预警");
                    orderDO.setShengYuTime(Integer.parseInt(orderAfterHoursT1) - resH + "小时");
                } else {
                    orderDO.setShiXiaoState("正常");
                    orderDO.setShengYuTime(Integer.parseInt(orderAfterHoursT1) - resH + "小时");
                }
            }
        }

        //这个特殊
        if (orderDO.getType() != null && orderDO.getType().equals("3")) {
            //当天几点前发起的单子,第几天几点前未结单则为超时
            //1.先判断发起单子的时间是否在这个时间之前(判断小时即便是等于也为之后)
            if (consultTimeHours < Integer.parseInt(orderDO.getTodayOrderBeforeTimeT31())) {
                //如果是0天就代表是今天
                if (orderDO.getOrderAfterDayT31().equals("0")) {
                    int orderAfterTimeT31 = Integer.parseInt(orderDO.getOrderAfterTimeT31());
                    //判断当前时间减去咨询单创建时间是否大于t31Hours如果大于则为超时
                    if (!date.equals(orderDO.getConsultTime().substring(0, 10)) || (date.equals(orderDO.getConsultTime().substring(0, 10)) && currentHours >= orderAfterTimeT31)) {
                        orderDO.setShiXiaoState("超时");
                        //超时的话就把剩余时间设置为0
                        orderDO.setShengYuTime("0");
                    } else if (orderDO.getYujingTime() != null && date.equals(orderDO.getConsultTime().substring(0, 10)) && currentHours < orderAfterTimeT31 && orderAfterTimeT31 - currentHours <= Integer.parseInt(orderDO.getYujingTime().equals("") ? "0" : orderDO.getYujingTime())) {
                        //如果resH的时间小于设定的超时时间,并且减去设定的超时小时不为福则为预警
                        orderDO.setShiXiaoState("预警");
                        orderDO.setShengYuTime(orderAfterTimeT31 - currentHours + "小时");

                    } else {
                        orderDO.setShiXiaoState("正常");
                        orderDO.setShengYuTime(orderAfterTimeT31 - currentHours + "小时");
                    }
                } else {
                    //1.2判断多少天几点前未结单则为超时,将这时间转化为小时
                    int t31Hours = (Integer.parseInt(orderDO.getOrderAfterDayT31()) - 1) * 24 + Integer.parseInt(orderDO.getOrderAfterTimeT31()) + 24 - consultTimeHours - 1;
                    //后面改的时候加个结单时间
                    //判断当前时间减去咨询单创建时间是否大于t31Hours如果大于则为超时
                    if (resH > t31Hours) {
                        orderDO.setShiXiaoState("超时");
                        //超时的话就把剩余时间设置为0
                        orderDO.setShengYuTime("0");
                    } else if (orderDO.getYujingTime() != null && resH <= t31Hours && t31Hours - resH <= Integer.parseInt(orderDO.getYujingTime().equals("") ? "0" : orderDO.getYujingTime())) {
                        //如果resH的时间小于设定的超时时间,并且减去设定的超时小时不为福则为预警
                        orderDO.setShiXiaoState("预警");
                        orderDO.setShengYuTime(t31Hours - resH + "小时");

                    } else {
                        orderDO.setShiXiaoState("正常");
                        orderDO.setShengYuTime(t31Hours - resH + "小时");
                    }
                }
            }
            //判断是几点后发起的单
            if (consultTimeHours >= Integer.parseInt(orderDO.getTodayOrderAfterTime32())) {
                //如果是0天就代表是今天
                if (orderDO.getOrderBeforeDayT32().equals("0")) {
                    int orderBeforeTimeT32 = Integer.parseInt(orderDO.getOrderBeforeTimeT32());
                    //后面改的时候加个结单时间
                    //判断当前时间减去咨询单创建时间是否大于t31Hours如果大于则为超时
                    if (!date.equals(orderDO.getConsultTime().substring(0, 10)) || (date.equals(orderDO.getConsultTime().substring(0, 10)) && currentHours >= orderBeforeTimeT32)) {
                        orderDO.setShiXiaoState("超时");
                        //超时的话就把剩余时间设置为0
                        orderDO.setShengYuTime("0");
                    } else if (orderDO.getYujingTime() != null && currentHours <= orderBeforeTimeT32 && orderBeforeTimeT32 - currentHours > Integer.parseInt(orderDO.getYujingTime().equals("") ? "0" : orderDO.getYujingTime())) {
                        //如果resH的时间小于设定的超时时间,并且减去设定的超时小时不为福则为预警
                        orderDO.setShiXiaoState("预警");
                        orderDO.setShengYuTime(orderBeforeTimeT32 - currentHours + "小时");

                    } else {
                        orderDO.setShiXiaoState("正常");
                        orderDO.setShengYuTime(orderBeforeTimeT32 - currentHours + "小时");
                    }
                } else {
                    //1.2判断多少天几点前未结单则为超时,将这时间转化为小时
                    int t32Hours = (Integer.parseInt(orderDO.getOrderBeforeDayT32()) - 1) * 24 + Integer.parseInt(orderDO.getOrderBeforeTimeT32()) + 24 - consultTimeHours - 1;
                    //后面改的时候加个结单时间
                    //判断当前时间减去咨询单创建时间是否大于t31Hours如果大于则为超时
                    if (resH > t32Hours) {
                        orderDO.setShiXiaoState("超时");
                        //超时的话就把剩余时间设置为0
                        orderDO.setShengYuTime("0");
                    } else if (orderDO.getYujingTime() != null && resH <= t32Hours && t32Hours - resH <= Integer.parseInt(orderDO.getYujingTime().equals("") ? "0" : orderDO.getYujingTime())) {
                        //如果resH的时间小于设定的超时时间,并且减去设定的超时小时不为福则为预警
                        orderDO.setShiXiaoState("预警");
                        orderDO.setShengYuTime(t32Hours - resH + "小时");

                    } else {
                        orderDO.setShiXiaoState("正常");
                        orderDO.setShengYuTime(t32Hours - resH + "小时");
                    }
                }
            }
        }

        return orderDO;
    }

    // 导出excel
    @RequestMapping("/exportExcel")
    @MethodLock(key = "exportExcel")
    @RequiresPermissions("bigcustomer:orderChi:exportExcel")
    public void exportExcel(HttpServletResponse response, @RequestParam Map<String, Object> params) {
        params.put("limit", SysConfig.LIMIT_OUT_EXCEL);// 上限保护
        //获取用户数据权限判断是什么权限
        Long userId = ShiroUtils.getUser().getUserId();
        //根据roleId去查询该角色的数据权限
        String dataPermissions = roleService.getDataPermissionsByUserId(userId);
        String username = ShiroUtils.getUser().getUsername();
        if (dataPermissions != null && dataPermissions.equals("1")) {
            //代表数据权限是本人(可以看见本人处理和本人创建的数据)
            params.put("dataPermissions1", username);
        } else if (dataPermissions != null && dataPermissions.equals("2")) {
            //代表是本部门(获取本部门所有人的账号,可以看本部门处理和本部门创建的数据)
            //1.获取当前用户的所属机构下的所有人的账号
            String orgCode = orderService.getOrgCodeByUserName(username);
            //2.根据机构编码获取该机构下的所有用户账号
            List<String> userNameList = orderService.getUserNameListByOrgCode(orgCode);
            String res = "";
            if (userNameList.size() > 1) {
                for (String userName : userNameList) {
                    res += "'" + userName + "',";
                }
                if (res.endsWith(",")) {
                    res = res.substring(0, res.length() - 1);
                }
            } else {
                res = "'" + userNameList.get(0) + "'";
            }
            params.put("dataPermissions2", res);
            params.put("orgCode", orgCode);
        } else if (dataPermissions != null && dataPermissions.equals("3")) {
            //可以查看所有数据,不用做任何操作
        } else {
            //代表没有权限查看数据
            return;
        }
        //如果时间没传递过来,默认查询当天
        if (params.get("startDate").equals("") || params.get("endDate").equals("")) {
            params.put("startDate", DateUtils.formatDate(DateUtils.getDate(new Date(),-15), "yyyy-MM-dd 00:00:00"));
            params.put("endDate", DateUtils.formatDate(new Date(), "yyyy-MM-dd 23:59:59"));
        } else {
            params.put("startDate", DateUtils.formatDate(DateUtils.parseDate(params.get("startDate") + ""), "yyyy-MM-dd 00:00:00"));
            params.put("endDate", DateUtils.formatDate(DateUtils.parseDate(params.get("endDate") + ""), "yyyy-MM-dd 23:59:59"));
        }

        if (params.get("shenLingI").equals("true")) {
            //查询条件中勾选了我申领的
            //获取用户账号
            params.put("dealCode", username);
        }
        if (params.get("faQiI").equals("true")) {
            //查询条件中勾选了我发起的
            params.put("faqiCode", username);
        }
        //如果有勾选时效预警状态就作为条件在遍历的时候判断
        final String shiXiaoState = (String) params.get("shiXiaoState");
        Query query = new Query(params);

        //int nums = orderService.count(query);

        RspBean<PageUtils> rspBean = this.list(query);
        List<?> result = rspBean.getData().getRows();
        //导出增加工单状态也就是时效预警状态
        String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
        try {
//            ExcelUtils.getInstance().exportObjects2Excel(result, OrderDO.class, targetFile);
//            File file = new File(targetFile);


            // 写入response
            File downloadFile = new File(targetFile);
            if (!downloadFile.getParentFile().exists()) { //如果文件的目录不存在
                downloadFile.getParentFile().mkdirs(); //创建目录

            }
            //2: 实例化OutputString 对象
            OutputStream output = new FileOutputStream(downloadFile);
//            response.addHeader("Content-Length", );
            ExcelUtils.getInstance().exportObjects2Excel(result, OrderDO.class, output);

            FileUtil.downloadByResponse(response, downloadFile);
            downloadFile.delete();
        } catch (Exception e) {
            //e.printStackTrace();
            log.error(e.getMessage(), e);
        }
    }


    /**
     * 申领
     */
    @PostMapping("/shenLing")
    @ResponseBody
    @RequiresPermissions("bigcustomer:orderChi:shenLing")
    public R shenLing(@RequestParam("orderIds[]") String[] orderIds) {
        //获取登陆用户信息
        String name = ShiroUtils.getUser().getName();
        //获取用户账号
        String userName = ShiroUtils.getUser().getUsername();
        String state = "待处理";
        //存入申领人账号(也就是处理人账号)
        orderService.shenLing(orderIds, userName, name, state);

        //处理操作记录(后面用aop来处理)
        //判断是不是勾选了多个,如果勾选多个就要批量存放操作记录
        String orgCodeByUserName = "";
        if (orderIds.length > 1) {
            for (String orderId : orderIds) {
                OperateDO operateDO = new OperateDO();
                operateDO.setOrderId(orderId);
                operateDO.setOperateName(name);
                operateDO.setOperateCode(userName);
                operateDO.setType("申领");
                //根据用户账号获取用户所属机构,最好存放机构编码
                orgCodeByUserName = orderService.getOrgCodeByUserName(userName);
                operateDO.setOperateOrganization(orgCodeByUserName);
                operateDO.setTime(DateUtils.getCurrentTime());
                orderService.saveOperateInfo(operateDO);
            }
        } else {
            OperateDO operateDO = new OperateDO();
            operateDO.setOrderId(orderIds[0]);
            operateDO.setOperateName(name);
            operateDO.setOperateCode(userName);
            operateDO.setType("申领");
            //根据用户账号获取用户所属机构,最好存放机构编码
            orgCodeByUserName = orderService.getOrgCodeByUserName(userName);
            operateDO.setOperateOrganization(orgCodeByUserName);
            operateDO.setTime(DateUtils.getCurrentTime());
            orderService.saveOperateInfo(operateDO);
        }
        return R.ok();
    }

    /**
     * 指派
     */
    @GetMapping("/zhiPai/{orderIds}")
    @RequiresPermissions("bigcustomer:orderChi:zhiPai")
    public String zhiPai(@PathVariable("orderIds") String orderIds, Model model) {
        String username = ShiroUtils.getUser().getUsername();
        //根据账号获取角色
        String roleName = orderService.getRoleNameByUserName(username);
        //1.获取当前用户的所属机构下的所有人的账号
        String orgCode = orderService.getOrgCodeByUserName(username);
        //如果不是总部就只能指派本机构
        List<String> allUserInfo=null;
        if (!roleName.equals("总部") && !roleName.equals("超级用户角色")) {
            //指派的时候只显示本机构的用户
            allUserInfo = orderService.getAllUserInfoByOrgCode(orgCode);
        }else {
            //指派的时候只显示本机构的用户
            allUserInfo = orderService.getAllUserInfoByOrgCode(null);
        }
        model.addAttribute("orderIds", orderIds);
        model.addAttribute("allUserInfo", allUserInfo);
        return "bigcustomer/orderChi/zhiPai";
    }

    /**
     * 指派
     */
    @PostMapping("/zhiPaiSave")
    @ResponseBody
    @RequiresPermissions("bigcustomer:order:zhiPai")
    public R zhiPaiSave(@RequestParam("orderIds") String orderIds, @RequestParam("dealMan") String dealMan) {
        if (dealMan.equals("") || !dealMan.contains("-")) {
            return R.error("您指派的人员信息不规范!");
        }
        //获取dealCode
        String dealCode = dealMan.split("-")[1];
        //对条件进行校验,判断用户是否存在
        UserDO userDO = orderService.getUserByUserName(dealCode);
        if (userDO == null) {
            return R.error("指派用户不存在!");
        }

        String name = userDO.getName();
        String state = "待处理";
        if (StringUtils.isBlank(orderIds)) {
            return R.error("请勾选要指派的数据!");
        }
        String[] orderIdArr = orderIds.split(",");
        //存入申领人账号(也就是处理人账号)
        orderService.shenLing(orderIdArr, dealCode, name, state);

        //处理操作记录(后面用aop来处理)
        //判断是不是勾选了多个,如果勾选多个就要批量存放操作记录
        String userName = ShiroUtils.getUser().getUsername();
        //操作人名字
        String operateName = ShiroUtils.getUser().getName();
        String zhiPaiName = name;
        String orgCodeByUserName = "";
        for (String orderId : orderIdArr) {
            OperateDO operateDO = new OperateDO();
            operateDO.setOrderId(orderId);
            operateDO.setOperateName(operateName);
            operateDO.setOperateCode(userName);
            operateDO.setType("指派");
            //根据用户账号获取用户所属机构,最好存放机构编码
            orgCodeByUserName = orderService.getOrgCodeByUserName(userName);
            operateDO.setOperateOrganization(orgCodeByUserName);
            operateDO.setTime(DateUtils.getCurrentTime());
            orderService.saveOperateInfo(operateDO);
        }
        return R.ok();
    }


    @GetMapping("/detail/{orderId}")
    String edit(@PathVariable("orderId") String orderId, Model model) {
        OrderDO order = orderService.getByOrderId(orderId);
        model.addAttribute("order", order);
        model.addAttribute("orderId", orderId);
        model.addAttribute("waybillNum", order.getWaybillNum());
        return "bigcustomer/orderChi/detail";
    }

    @ResponseBody
    @GetMapping("/caoZuo")
    PageUtils caoZuo(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        //查询列表数据
        List<OperateDO> orderList = orderService.getListByOrderId(query);
        int total = orderService.countByOrderId(query);
        PageUtils pageUtils = new PageUtils(orderList, total);
        return pageUtils;
    }

    @ResponseBody
    @GetMapping("/qiTaWenTi")
    PageUtils qiTaWenTi(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        //查询列表数据
        List<OperateDO> orderList = orderService.getListByWaybillNum(query);
        int total = orderService.countByWaybillNum(query);
        PageUtils pageUtils = new PageUtils(orderList, total);
        return pageUtils;
    }


    /**
     * 处理
     */
    @GetMapping("/deal/{orderId}")
    public String deal(@PathVariable("orderId") String orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "bigcustomer/order/deal";
    }

    /**
     * 处理提交保存
     * 这里处理和结单的内容一样就共用一个实体类
     */
    @ResponseBody
    @PostMapping("/dealSave")
    public R dealSave(JieDanDO jieDanDO) {
        jieDanDO.setState("处理中");
        orderService.updateByOrderIds(jieDanDO);
        //处理操作记录
        OperateDO operateDO = new OperateDO();
        operateDO.setOrderId(jieDanDO.getOrderIds());
        operateDO.setDealContent(jieDanDO.getJieDanCause());
        operateDO.setOperateName(ShiroUtils.getUser().getName());
        String username = ShiroUtils.getUser().getUsername();
        operateDO.setOperateCode(username);
        operateDO.setType("处理");
        //根据用户账号获取用户所属机构,最好存放机构编码
        String orgCodeByUserName = orderService.getOrgCodeByUserName(username);
        //操作类型,附件地址
        operateDO.setUploadPath(jieDanDO.getUploadPath());
        //附件名称
        operateDO.setFileName(jieDanDO.getFileName());
        operateDO.setOperateOrganization(orgCodeByUserName);
        operateDO.setTime(DateUtils.getCurrentTime());
        operateDO.setZeRenFang(jieDanDO.getZeRenFang());
        operateDO.setUploadPath(jieDanDO.getUploadPath());
        orderService.saveOperateInfo(operateDO);
        return R.ok();
    }

    /**
     * 在咨询单类型与配置表中获取所有咨询单类型
     */
    @ResponseBody
    @GetMapping("/getAllConsultype")
    public List<String> getAllConsultype() {
        List allConsulType = orderService.getAllConsultype();
        return allConsulType;
    }

    /**
     * 在全部咨询单中的指派,获取指派人员信息
     */
   /* @ResponseBody
    @GetMapping("/getAllUserInfo")
    public List getAllUserInfo() {
        List<String> allUserInfo = orderService.getAllUserInfo();
        return allUserInfo;
    }*/

    /**
     * 获取页面头上展示的统计数据
     */
    @ResponseBody
    @GetMapping("/getOrderIndex")
    public Map<String, Object> getOrderIndex() {
        String state = "";
        String monthBegin = DateUtils.getMonthBegin() + " 00:00:00";
        String monthEnd = DateUtils.getMonthEnd();
        HashMap<String, Object> map = new HashMap<>(16);
        int count = 0;
        //获取本月咨询单数量
        count = orderService.countOrder(monthBegin, monthEnd);
        map.put("orderNum", count);
        //获取状态为待申领的单量
        state = "待申领";
        count = orderService.countByState(monthBegin, monthEnd, state);
        map.put("waitShenLing", count);
        //获取状态为待处理的单量
        state = "待处理";
        count = orderService.countByState(monthBegin, monthEnd, state);
        map.put("waitDeal", count);
        //获取状态为处理中的单量
        state = "处理中";
        count = orderService.countByState(monthBegin, monthEnd, state);
        map.put("dealing", count);
        //获取状态为已结单的单量
        state = "已结单";
        count = orderService.countByState(monthBegin, monthEnd, state);
        map.put("yiJieDan", count);
        return map;
    }

    /**
     * 下载附件
     */
    @PostMapping("/downFile")
    @ResponseBody
    public R downFile(HttpServletResponse resp,@RequestParam("fileName") String fileName,@RequestParam("filePath") String filePath) {
        /*File downFile = new File(filePath);*/
        /*File.separator*/
        FileUtil.download(resp,fileName,filePath);
        return R.ok("下载成功");
    }



}