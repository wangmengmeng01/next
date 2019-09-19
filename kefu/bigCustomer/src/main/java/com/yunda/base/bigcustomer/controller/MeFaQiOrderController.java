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
import com.yunda.base.bigcustomer.domain.ConsultFileDO;
import com.yunda.base.bigcustomer.domain.OperateDO;
import com.yunda.base.bigcustomer.domain.OrderDO;
import com.yunda.base.bigcustomer.service.ConsultConfigService;
import com.yunda.base.bigcustomer.service.ConsultFileService;
import com.yunda.base.bigcustomer.service.CustomerManageService;
import com.yunda.base.bigcustomer.service.OrderService;
import com.yunda.base.bigcustomer.service.OrganizationManageService;
import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.R;
import com.yunda.base.common.utils.ShiroUtils;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.service.RoleService;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;

import net.sf.json.JSONObject;

/**
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-04-25154305
 */

@Controller
@RequestMapping("/bigcustomer/meFaQiOrder")
public class MeFaQiOrderController extends BaseController {
    Logger log = Logger.getLogger(getClass());
    @Autowired
    private OrderService orderService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private OrganizationManageService organizationManageService;
    @Autowired
    private CustomerManageService customerManageService;
    @Autowired
    private ConsultConfigService consultConfigService;
    @Autowired
    private ConsultFileService consultFileService;

    @GetMapping()
    @RequiresPermissions("bigcustomer:meFaQiOrder:meFaQiOrder")
    String Order() {
        return "bigcustomer/meFaQiOrder/meFaQiOrder";
    }

    @GetMapping("/orderByWillbillNum/{startDate}/{endDate}/{waybillNum}")
    @RequiresPermissions("bigcustomer:meFaQiOrder:meFaQiOrder")
    String OrderByWillbillNum(@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate, @PathVariable("waybillNum") String waybillNum, Model model) {
        Map<String, Object> map = new HashMap<>();
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("waybillNum", waybillNum);
        List orderList = orderService.getListByWaybillNum(map);
        model.addAttribute("orderList", orderList);
        return "bigcustomer/order/order";
    }

    /**
     * 没有数据权限限制,只能看到登录人发起的数据
     * @param params
     * @return
     */
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("bigcustomer:meFaQiOrder:meFaQiOrder")
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


        //获取用户角色(客服只可以看自己的数据,客服主管看本机构人发起的数据,总部及超级用户角色可以看所有数据)
        String username = ShiroUtils.getUser().getUsername();
        String roleName = orderService.getRoleNameByUserName(username);
        if (roleName != null && roleName.equals("客服")) {
            //我发起的工单里面只能看到我发起的数据
            params.put("faqiCode", username);
        } else if (roleName != null && roleName.equals("客服主管")) {
            //获取本机构编码
            String orgCode = orderService.getOrgCodeByUserName(username);
            //可以看到发起机构是本机构的数据
            params.put("faqiOrgCode",orgCode);
        } else if (roleName != null && (roleName.equals("总部") || roleName.equals("超级用户角色"))) {
            //可以查看所有数据,不用做任何操作
        } else {
            //代表没有权限查看数据
            return null;
        }


        //如果时间没传递过来,默认查询当天
        if (params.get("startDate").equals("") || params.get("endDate").equals("")) {
            params.put("startDate", DateUtils.formatDate(DateUtils.getDate(new Date(),-15), "yyyy-MM-dd 00:00:00"));
            params.put("endDate", DateUtils.formatDate(new Date(), "yyyy-MM-dd 23:59:59"));
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
            //获取小时
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
            if(orderDO.getState()!=null && orderDO.getState().equals("已结单")){
                orderDO.setShengYuTime("-");
            }
        }

        int total = orderService.count(query);
        PageUtils pageUtils = new PageUtils(orderList, total);
        return success(pageUtils);
    }


    //把判断语句抽出来
    public OrderDO getYuJingOrderDO(OrderDO orderDO,Long currentHours,Long resH,int consultTimeHours,String date){
       /* if(orderDO.getState()!=null && orderDO.getState().equals("已结单") && orderDO.getJieDanTime()!=null && !orderDO.getJieDanTime().equals("")){
            consultTimeHours = Integer.parseInt(orderDO.getJieDanTime().substring(11, 13));
        }*/
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
    @RequiresPermissions("bigcustomer:meFaQiOrder:exportExcel")
    public void exportExcel(HttpServletResponse response, @RequestParam Map<String, Object> params) {
        params.put("limit", SysConfig.LIMIT_OUT_EXCEL);// 上限保护
        //获取用户角色(客服只可以看自己的数据,客服主管看本机构人发起的数据,总部及超级用户角色可以看所有数据)
        String username = ShiroUtils.getUser().getUsername();
        String roleName = orderService.getRoleNameByUserName(username);
        if (roleName != null && roleName.equals("客服")) {
            //我发起的工单里面只能看到我发起的数据
            params.put("faqiCode", username);
        } else if (roleName != null && roleName.equals("客服主管")) {
            //获取本机构编码
            String orgCode = orderService.getOrgCodeByUserName(username);
            //可以看到发起机构是本机构的数据
            params.put("faqiOrgCode",orgCode);
        } else if (roleName != null && (roleName.equals("总部") || roleName.equals("超级用户角色"))) {
            //可以查看所有数据,不用做任何操作
        } else {
            //代表没有权限查看数据
            return;
        }
        //如果时间没传递过来,默认查询当天
        if (params.get("startDate").equals("") || params.get("endDate").equals("")) {
            params.put("startDate", DateUtils.formatDate(DateUtils.getDate(new Date(),-15), "yyyy-MM-dd 00:00:00"));
            params.put("endDate", DateUtils.formatDate(new Date(), "yyyy-MM-dd 23:59:59"));
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

    @GetMapping("/detail/{orderStr}")
    String detail(@PathVariable("orderStr") String orderStr, Model model) {
        orderStr = orderStr.replace("&^","'");
        orderStr = orderStr.replace("&_",";");
        JSONObject jsonObject= JSONObject.fromObject(orderStr);
        OrderDO orderDO = (OrderDO)JSONObject.toBean(jsonObject, OrderDO.class);
        OrderDO order = orderService.getByOrderId(orderDO.getOrderId());
        order.setOrganizationName(orderDO.getOrganizationName());
        order.setFaqiCode(orderDO.getFaqiCode());
        order.setFaqiName(orderDO.getFaqiName());
        order.setShiXiaoState(orderDO.getShiXiaoState());
        order.setShengYuTime(orderDO.getShengYuTime());
        model.addAttribute("order", order);
        model.addAttribute("orderId", orderDO.getOrderId());
        model.addAttribute("waybillNum", order.getWaybillNum());
        return "bigcustomer/meFaQiOrder/meFaQiDetail";
    }

    @ResponseBody
    @GetMapping("/caoZuo")
    PageUtils caoZuo(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        //查询列表数据
        List<OperateDO> orderList = orderService.getListByOrderId(query);
        List<ConsultFileDO> consultFileDOList =null;
        for(OperateDO data:orderList){
            /*Map<String, Object> map = new HashMap<>();
            map.put("orderId",data.getOrderId());
            map.put("operateCode",data.getOperateCode());
            map.put("time",data.getTime());*/
            String orderId = data.getOrderId();
            String time = data.getTime();
            consultFileDOList = consultFileService.getListByOrderIdAndTime(orderId,time);
            data.setConsultFileDOList(consultFileDOList);
        }
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
     * 在咨询单类型与配置表中获取所有咨询单类型
     */
    @ResponseBody
    @GetMapping("/getAllConsultype")
    public List<String> getAllConsultype() {
        List allConsulType = orderService.getAllConsultype();
        return allConsulType;
    }


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
        filePath=SysConfig.uploadPath+filePath;
        FileUtil.download(resp,fileName,filePath);
        return R.ok("下载成功");
    }

    @GetMapping("/getOrganizationNameByCode/{organizationNum}")
    @ResponseBody
    public RspBean getOrganizationNameByCode(@PathVariable("organizationNum") String organizationNum){
        System.out.println("organizationNum=========="+organizationNum);
        String orgName = organizationManageService.listOrgName(organizationNum);
        if(orgName!=null && !orgName.equals("")){
            return success(orgName);
        }else {
            return failure("该机构编码不存在!");
        }
    }

}

