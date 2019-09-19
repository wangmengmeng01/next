package com.yunda.base.bigcustomer.controller;

import static java.time.LocalDate.now;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.github.crab2died.ExcelUtils;
import com.yunda.base.bigcustomer.domain.ConsultFileDO;
import com.yunda.base.bigcustomer.domain.CustomerManageDO;
import com.yunda.base.bigcustomer.domain.JieDanDO;
import com.yunda.base.bigcustomer.domain.OperateDO;
import com.yunda.base.bigcustomer.domain.OrderDO;
import com.yunda.base.bigcustomer.domain.OrderTemplateDO;
import com.yunda.base.bigcustomer.domain.ZhuanFaDO;
import com.yunda.base.bigcustomer.service.ConsultConfigService;
import com.yunda.base.bigcustomer.service.ConsultFileService;
import com.yunda.base.bigcustomer.service.CustomerManageService;
import com.yunda.base.bigcustomer.service.OrderService;
import com.yunda.base.bigcustomer.service.OrganizationManageService;
import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.enums.DictEnum;
import com.yunda.base.common.enums.RespEnum;
import com.yunda.base.common.utils.HttpPostXml;
import com.yunda.base.common.utils.ImportUtils;
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

import net.sf.json.JSONObject;

/**
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-04-25154305
 */

@Controller
@RequestMapping("/bigcustomer/order")
public class OrderController extends BaseController {
    private static Logger log = Logger.getLogger(OrderController.class);
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
    @RequiresPermissions("bigcustomer:order:order")
    String Order() {
        return "bigcustomer/order/order";
    }

    @GetMapping("/orderByWillbillNum/{startDate}/{endDate}/{waybillNum}")
    @RequiresPermissions("bigcustomer:order:order")
    String OrderByWillbillNum(@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate, @PathVariable("waybillNum") String waybillNum, Model model) {
        Map<String, Object> map = new HashMap<>();
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("waybillNum", waybillNum);
        List orderList = orderService.getListByWaybillNum(map);
        model.addAttribute("orderList", orderList);
        return "bigcustomer/order/order";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("bigcustomer:order:order")
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
            /*//2.根据机构编码获取该机构下的所有用户账号
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
            params.put("dataPermissions2", res);*/
            params.put("dataPermissions2", dataPermissions);
            params.put("orgCode", orgCode);
        } else if (dataPermissions != null && dataPermissions.equals("3")) {
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
    @RequiresPermissions("bigcustomer:order:exportExcel")
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

    // 下载excel模版
    @RequestMapping("/downTemplate")
    @MethodLock(key = "downTemplate")
    // 下载模版和导入共用同一个权限
    @RequiresPermissions("bigcustomer:order:importExcel")
    public void downTemplate(HttpServletResponse response) {
        String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
        try {
            ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<OrderTemplateDO>(), OrderTemplateDO.class, targetFile);
            // 写入response
            File downloadFile = new File(targetFile);
            if (!downloadFile.getParentFile().exists()) {
                downloadFile.getParentFile().mkdirs();
            }
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
    @RequiresPermissions("bigcustomer:order:importExcel")
    public RspBean<ImportUtils> importExcel(MultipartFile file) {
    	//long startTime=System.currentTimeMillis();
        //System.out.println("执行代码块/方法");

        List<OrderTemplateDO> list = null;
        String username = ShiroUtils.getUser().getUsername();
        //根据账号获取角色
        String roleName = orderService.getRoleNameByUserName(username);
        //1.获取当前用户的所属机构下的所有人的账号
        String orgCode = orderService.getOrgCodeByUserName(username);
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

                list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, OrderTemplateDO.class, 0, 0);
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
        //long endTime1=System.currentTimeMillis();

        if (list != null) {
            if(list.size()>Integer.parseInt(SysConfig.LIMIT_UPLOAD_EXCEL)){
                return failure(RespEnum.ORDER_DATA_EXCEED.getCode());
                //return new RspBean("导入数据限制"+SysConfig.LIMIT_UPLOAD_EXCEL+"条");
                //return R.error("导入数据限制"+SysConfig.LIMIT_UPLOAD_EXCEL+"条");
            }
            String userName = ShiroUtils.getUser().getUsername();
            String name = ShiroUtils.getUser().getName();
            String orgCodeByUserName = orderService.getOrgCodeByUserName(userName);

            //校验数据遍历
            int excelRow = 2;
            //String errorList = "";
            List<String> errorList = new ArrayList<>();
            for (OrderTemplateDO orderDO : list) {

                String errorString = "";
                //近十天重复单号查询=================================开始===========================
                //String redisOrderId =  "";
                if ((orderDO.getWaybillNum() == null || orderDO.getWaybillNum().equals("")) ) {

                    OrderDO order = new OrderDO();
                    String tenBeforeTime = DateUtils.format(DateUtils.getDate(new Date(), -10), "yyyy-MM-dd 00:00:00");
                    order.setConsultTime(tenBeforeTime);
                    order.setWaybillNum(orderDO.getWaybillNum());
                    OrderDO checkOrder = orderService.checkOrder(order);
                    if(orderDO != null && orderDO.getOrderId()!=null){
                        errorString += orderDO.getWaybillNum()+"此单号近10天已发起咨询记录;";
                    }
                }
                //近十天重复单号查询=================================结束===========================

                if ((orderDO.getWaybillNum() == null || orderDO.getWaybillNum().equals("")) && (orderDO.getLogisticOrderNum() == null || orderDO.getLogisticOrderNum().equals(""))) {
                    errorString += "物流订单号和运单号不能同时为空!";
                }
                if (orderDO.getOrganizationNum() == null || orderDO.getOrganizationNum().equals("")) {
                    errorString +="责任机构编码不能为空!";
                }
                if (orderDO.getConsultType() == null || orderDO.getConsultType().equals("")) {
                    errorString +="咨询类型不能为空!";
                }
                if (orderDO.getPriority() == null || orderDO.getPriority().equals("")) {
                    errorString +="优先级不能为空!";
                }
                if (orderDO.getProblemDescription() == null || orderDO.getProblemDescription().equals("")) {
                    errorString +="问题描述不能为空!";
                }
                //查看客户是否存在,不存在也直接返回
                if (orderDO.getMerchant() != null && !orderDO.getMerchant().equals("") && customerManageService.countByCustomerName(orderDO.getMerchant()) < 1) {
                    errorString +=orderDO.getMerchant() + "客户不存在!";
                }
                //校验数据
                if (null == orderDO.getPriority() || !orderDO.getPriority().equals("一般") && !orderDO.getPriority().equals("紧急") && !orderDO.getPriority().equals("低")) {
                    errorString +="优先级不符合规范!(提示:一般,紧急,低)!";
                }
                //校验咨询类型
                int countByConsultType = orderService.countByConsultType(orderDO.getConsultType());
                if (countByConsultType < 1) {
                    errorString +="该" + orderDO.getConsultType() + "咨询类型不存在!";
                }
                //校验责任机构编码
                int countByOrganizationNum = organizationManageService.countByOrganizationNum(orderDO.getOrganizationNum());
                if (countByOrganizationNum < 1) {
                    errorString +="该" + orderDO.getOrganizationNum() + "责任机构编码不存在!";
                }
                if(null != errorString){
                    //errorList += "第"+excelRow + "行,";
                    errorList.add("第"+excelRow + "行,"+errorString+";");
                }
                excelRow ++;
            }
            //errorList += "数据格式不正确,请修改错误数据后重新导入全部数据";
            if(null!=errorList || errorList.size()>0){
                ImportUtils importUtils = new ImportUtils(RespEnum.ORDER_DATA_ERROR.getCode(),"",errorList);
                return failure(importUtils);
            }

            /*if(null !=errorList && !"数据格式不正确,请修改错误行后重新导入全部数据".equals(errorList)){
                return R.error(errorList);//success(errorList);//
            }*/

          //批量插入数据
            orderService.saveList(list);
        }
        /*long endTime=System.currentTimeMillis();
        long time1 = endTime1-startTime;
        long time = endTime-startTime;
        log.info("解析excel共耗时"+time1+"毫秒");
        log.info("本次导入共耗时"+time+"毫秒");*/
        //return R.ok();
        return null;
    }

    @GetMapping("/add")
    @RequiresPermissions("bigcustomer:order:add")
    String add(Model model) {
        //查看所有的客户信息
        List<CustomerManageDO> customerList = customerManageService.getCustomerAll();
        model.addAttribute("customerList", customerList);
        return "bigcustomer/order/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("bigcustomer:order:edit")
    String edit(@PathVariable("id") Integer id, Model model) {
        OrderDO order = orderService.get(id);
        model.addAttribute("order", order);
        return "bigcustomer/order/edit";
    }

    /**
     * 保存前检查最近10天内有没有发起运单号和咨询类型相同的单号
     */
    @ResponseBody
    @PostMapping("/checkOrder")
    public RspBean checkOrder(OrderDO order){
        //提交工单时，校验是否在10天内发起过【运单号】相同且【咨询类型】相同的工单
        //取最新的数据展示
        //获取10天前的日期
        String tenBeforeTime = DateUtils.format(DateUtils.getDate(new Date(),-10),"yyyy-MM-dd 00:00:00");
        order.setConsultTime(tenBeforeTime);
        OrderDO orderDO = orderService.checkOrder(order);
        if(orderDO != null && orderDO.getOrderId()!=null){
            return success(orderDO);
        }else {
            return failure(RespEnum.ORDER_DATA_NONEXIST);
        }
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
//	@RequiresPermissions("bigcustomer:order:add")
    public R save(OrderDO order) {
        //首先查看责任机构编码是否存在存在的话返回
        int count = organizationManageService.countByOrganizationNum(order.getOrganizationNum());
        if (count < 1) {
            R error = R.error("填写的责任机构编码不存在!");
            return error;
        }
        String username = ShiroUtils.getUser().getUsername();
        String name = ShiroUtils.getUser().getName();
        //存放发起人的编码
        order.setFaqiCode(username);
        order.setFaqiName(name);
        //获取发起机构编码和机构名称
        String orgCode = orderService.getOrgCodeByUserName(username);
        order.setFaqiOrgCode(orgCode);
        String orgName = organizationManageService.listOrgName(orgCode);
        order.setFaqiOrgName(orgName);
        int flag = orderService.save(order);
        if (flag >=1) {
            return R.ok();
        }
        return R.error(flag, "填入的运单号不存在!");
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("bigcustomer:order:edit")
    public R update(OrderDO order) {
        orderService.update(order);

        //处理操作记录(后面用aop来处理)
        //判断是不是勾选了多个,如果勾选多个就要批量存放操作记录
        //获取用户账号
        String userName = ShiroUtils.getUser().getUsername();
        OperateDO operateDO = new OperateDO();
        operateDO.setOrderId(order.getOrderId());
        operateDO.setOperateName(ShiroUtils.getUser().getName());
        operateDO.setOperateCode(userName);
        operateDO.setType("修改咨询");
        //根据用户账号获取用户所属机构,最好存放机构编码
        String orgCodeByUserName = orderService.getOrgCodeByUserName(userName);
        operateDO.setOperateOrganization(orgCodeByUserName);
        operateDO.setTime(DateUtils.getCurrentTime());
        orderService.saveOperateInfo(operateDO);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("bigcustomer:order:remove")
    public R remove(Integer id) {
        if (orderService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("bigcustomer:order:batchRemove")
    public R remove(@RequestParam("ids[]") Integer[] ids) {
        orderService.batchRemove(ids);
        return R.ok();
    }

    /**
     * 申领
     */
    @PostMapping("/shenLing")
    @ResponseBody
    @RequiresPermissions("bigcustomer:order:shenLing")
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
    @RequiresPermissions("bigcustomer:order:zhiPai")
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
        return "bigcustomer/order/zhiPai";
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

    /**
     * 批量结单
     */
    @GetMapping("/piLiangJieDan/{orderIds}")
    @RequiresPermissions("bigcustomer:order:piLiangJieDan")
    public String piLiangJieDan(@PathVariable("orderIds") String orderIds, Model model) {
		/*if(orderIds.contains(",")){
			String[] orderIdArr = orderIds.split(",");
			for (String orderId : orderIdArr) {

			}
		}*/
        model.addAttribute("orderIds", orderIds);
        String[] orderIdArr = orderIds.split(",");
        OrderDO orderDO = orderService.getByOrderId(orderIdArr[0]);
        model.addAttribute("consultType", orderDO.getConsultType());
        return "bigcustomer/order/piLiangJieDan";
    }

    /**
     * 上传附件
     */
   /* @ResponseBody
    @MethodLock(key = "import")
    @RequestMapping(value = "/upload", consumes = "multipart/*", headers = "content-type=mutipart/form-data", method = RequestMethod.POST)
    public R upload(MultipartFile file) {
        R r = new R();
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
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                //e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
            r.put("filePath", uploadFile);
            r.put("fileName", fileName);
        }
        return r;
    }*/

    /**
     * @function 多文件上传(和单文件上传一样,多次调用)
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/upload", consumes = "multipart/*", headers = "content-type=mutipart/form-data", method = RequestMethod.POST)
    public RspBean upload(@RequestParam(value = "file", required = false) MultipartFile file){
        if(file == null){
            return failure("上传文件为空!");
        }else{
            ConsultFileDO consultFileDO = this.fileMany(file, SysConfig.uploadPath);
            return success(consultFileDO);
        }
    }


    /**
     * @function 多文件上传
     * @return
     */
    public static ConsultFileDO fileMany(MultipartFile file , String saveUrl){
        ConsultFileDO fileDO = new ConsultFileDO();
        String newUrl = saveUrl + now();
        File saveDir = new File(newUrl);
        if(!saveDir.exists()){
            saveDir.mkdirs();
        }
            if(file != null){
                String fileName = file.getOriginalFilename();
                String suffix = fileName.substring(file.getOriginalFilename().lastIndexOf("."));
                String newFileName = UUID.randomUUID() + suffix;
                String newFileUrl = newUrl + "/" + newFileName;
                File saveFile = new File(newFileUrl);
                //数据库只保存上传文件的新名称,下载的时候拼接
                fileDO.setUploadPath(newFileName);
                fileDO.setFileName(fileName);
                fileDO.setStatus("1");  //1代表可用
                fileDO.setFileSuffix(suffix);
                fileDO.setCreateTime(DateUtils.getCurrentTime());
                fileDO.setOperateCode(ShiroUtils.getUser().getUsername());
                if(suffix.equals(".png") || suffix.equals(".jpg") || suffix.equals(".jpeg") || suffix.equals(".bmp")){
                    fileDO.setFileType("0");    //图片
                }else if(suffix.equals(".zip") || suffix.equals(".rar")){
                    fileDO.setFileType("2");    //其他
                }else {
                    fileDO.setFileType("1");    //文件
                }
                BufferedOutputStream out = null;
                try {
                    saveDir.createNewFile();//创建文件
                    log.info("newFileUrl==========="+newFileUrl+"======="+fileName);
                    /*file.transferTo(saveFile);*/
                    out = new BufferedOutputStream(new FileOutputStream(saveFile));
                    out.write(file.getBytes());
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("上传失败");
                }finally {
                    try {
                        out.close();
                    } catch (Exception e) {
                    }
                }
            }

        return fileDO ;
    }



    /**
     * 批量结单提交保存
     */
    @ResponseBody
    @PostMapping("/piLiangJieDanSave")
    public R piLiangJieDanSave(JieDanDO jieDanDO) {
        jieDanDO.setState("已结单");
        jieDanDO.setJieDanTime(DateUtils.getCurrentTime());
        orderService.updateByOrderIds(jieDanDO);
        String name = ShiroUtils.getUser().getName();

        //判断是不是勾选多个
        String username = ShiroUtils.getUser().getUsername();
        String[] orderIdArr = jieDanDO.getOrderIds().split(",");
        String orgCodeByUserName = "";
        String jieDanCause = jieDanDO.getJieDanCause();
        //String fileName = jieDanDO.getFileName();
        String zeRenFang = jieDanDO.getZeRenFang();
        //String uploadPath = jieDanDO.getUploadPath();
        String jieDanResult = jieDanDO.getJieDanResult();
        //保存上传文件路径
        List<ConsultFileDO> consultFileDOList = new ArrayList<>();
        String currentTime = DateUtils.getCurrentTime();//定义一个时间变量
        for (String orderId : orderIdArr) {
            //处理操作记录
            OperateDO operateDO = new OperateDO();
            operateDO.setDealContent(jieDanResult+";"+jieDanCause);
            operateDO.setOperateName(name);
            operateDO.setOperateCode(username);
            operateDO.setType("结单");
            //根据用户账号获取用户所属机构,最好存放机构编码
            orgCodeByUserName = orderService.getOrgCodeByUserName(username);
            //operateDO.setUploadPath(uploadPath);
            //附件名称
            //operateDO.setFileName(fileName);
            operateDO.setOperateOrganization(orgCodeByUserName);
            operateDO.setTime(currentTime);
            operateDO.setZeRenFang(zeRenFang);
            //判断前后是否有括号,有的话就截取
            String abc = null;
            if (orderId.startsWith("(")) {
                abc = orderId.substring(1, orderId.length());
            } else if (orderId.endsWith(")")) {
                abc = orderId.substring(0, orderId.length() - 1);
            } else {
                abc = orderId;
            }
            operateDO.setOrderId(abc);
            orderService.saveOperateInfo(operateDO);
            if(jieDanDO.getFileDOS()!=null){
                //保存上传文件路径
                for(ConsultFileDO data:jieDanDO.getFileDOS()) {
                    /*ConsultFileDO consultFileDO = new ConsultFileDO();*/
                    data.setOrderId(abc);
                    data.setFileType("0");
                    data.setStatus("1");
                    data.setCreateTime(currentTime);
                    data.setOperateCode(username);
                    /*consultFileDO.setFileName(data.getFileName());
                    consultFileDO.setFileSuffix(data.getFileSuffix());
                    consultFileDO.setUploadPath(data.getUploadPath());*/
                    consultFileDOList.add(data);
                }
            }
        }
        if(consultFileDOList.size()>0){
            consultFileService.saveBatch(consultFileDOList);
        }

        return R.ok();
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
        return "bigcustomer/order/detail";
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
        String currentTime = DateUtils.getCurrentTime();
        OperateDO operateDO = new OperateDO();
        operateDO.setOrderId(jieDanDO.getOrderIds());
        operateDO.setDealContent(jieDanDO.getJieDanCause());
        operateDO.setOperateName(ShiroUtils.getUser().getName());
        String username = ShiroUtils.getUser().getUsername();
        operateDO.setOperateCode(username);
        operateDO.setType("处理");
        //根据用户账号获取用户所属机构,最好存放机构编码
        String orgCodeByUserName = orderService.getOrgCodeByUserName(username);

        operateDO.setOperateOrganization(orgCodeByUserName);
        operateDO.setTime(currentTime);
        operateDO.setZeRenFang(jieDanDO.getZeRenFang());
        operateDO.setUploadPath(jieDanDO.getUploadPath());
        orderService.saveOperateInfo(operateDO);

        //保存上传文件路径
        List<ConsultFileDO> consultFileDOList = new ArrayList<>();
        if(jieDanDO.getFileDOS()!=null){
            for(ConsultFileDO data:jieDanDO.getFileDOS()) {
               /* ConsultFileDO consultFileDO = new ConsultFileDO();*/
                data.setOrderId(jieDanDO.getOrderIds());
                data.setFileType("0");
                data.setStatus("1");
                data.setCreateTime(currentTime);
                data.setOperateCode(username);
                /*consultFileDO.setFileName(data.getFileName());
                consultFileDO.setFileSuffix(data.getFileSuffix());
                consultFileDO.setUploadPath(data.getUploadPath());*/
                consultFileDOList.add(data);
            }

        }
        if(consultFileDOList.size()>0){
            consultFileService.saveBatch(consultFileDOList);
        }
        return R.ok();
    }

    /**
     * 拦截弹框
     */
    @GetMapping("/lanJie/{waybillNum}")
    public String lanJie(@PathVariable("waybillNum") String waybillNum, Model model) {
        //运单号,揽件网点编码
        model.addAttribute("waybillNum", waybillNum);
        return "bigcustomer/order/lanJie";
    }

    /**
     * 调拦截接口
     */
    @ResponseBody
    @PostMapping("/lanJieFeign")
    public R lanJieFeign(@RequestParam Map<String, Object> dataMap){
        String s = HttpPostXml.creatPostAndTransData(dataMap);
        //对结果进行解析判断(返回结果也是xml报文)
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource((new StringReader(s.toString()))));

            NodeList list = doc.getElementsByTagName("d");
            System.out.println("------------节点d如下：------------");
            for (int i = 0; i < list.getLength(); i++) {
                Node book = list.item(i);
                System.out.println("\t节点=" + i + "\t内容="
                        + book.getFirstChild().getNodeValue());
            }
            String nodeValue = list.item(1).getFirstChild().getNodeValue();
            System.out.println("节点1"+"======"+nodeValue);
            if(!nodeValue.equals("0")){
                return R.error("拦截失败");
            }
            System.out.println("------------结束------------");
        } catch (Exception e) {
        // TODO Auto-generated catch block
            e.printStackTrace();
        }
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


    /**
     * 批量转发
     */
    @GetMapping("/zhuanFa/{orderIds}")
    @RequiresPermissions("bigcustomer:orderChi:zhuanFa")
    public String zhuanFa(@PathVariable("orderIds") String orderIds, Model model) {
        model.addAttribute("orderIds", orderIds);
        return "bigcustomer/orderChi/zhuanFa";
    }



    /**
     * 批量转发提交保存
     */
    @ResponseBody
    @PostMapping("/zhuanFaSave")
    public R zhuanFaSave(ZhuanFaDO zhuanFaDO) {
        zhuanFaDO.setState(DictEnum.STATE_TO_APPLY.getValue());
        orderService.updateOrganizationNumByOrderIds(zhuanFaDO);
        String name = ShiroUtils.getUser().getName();
        //判断是不是勾选多个
        String username = ShiroUtils.getUser().getUsername();
        String[] orderIdArr = zhuanFaDO.getOrderIds().split(",");
        String orgCodeByUserName = "";
        String zhuanFaRemark = zhuanFaDO.getZhuanFaRemark();
        for (String orderId : orderIdArr) {
            //处理操作记录
            OperateDO operateDO = new OperateDO();
            operateDO.setDealContent(zhuanFaRemark);
            operateDO.setOperateName(name);
            operateDO.setOperateCode(username);
            operateDO.setType("转发");
            //根据用户账号获取用户所属机构,最好存放机构编码
            orgCodeByUserName = orderService.getOrgCodeByUserName(username);
            operateDO.setOperateOrganization(orgCodeByUserName);
            operateDO.setTime(DateUtils.getCurrentTime());
            //判断前后是否有括号,有的话就截取
            if (orderId.startsWith("(")) {
                operateDO.setOrderId(orderId.substring(1, orderId.length()));
            } else if (orderId.endsWith(")")) {
                operateDO.setOrderId(orderId.substring(0, orderId.length() - 1));
            } else {
                operateDO.setOrderId(orderId);
            }
            orderService.saveOperateInfo(operateDO);
        }


        return R.ok();
    }

}

