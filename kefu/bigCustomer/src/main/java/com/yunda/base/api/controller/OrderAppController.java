package com.yunda.base.api.controller;

import static java.time.LocalDate.now;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.yunda.base.api.domain.JieDanDTO;
import com.yunda.base.api.domain.OperateDTO;
import com.yunda.base.api.domain.OrderDTO;
import com.yunda.base.api.domain.RoleDTO;
import com.yunda.base.api.service.OrderAppService;
import com.yunda.base.bigcustomer.domain.ConsultFileDO;
import com.yunda.base.bigcustomer.domain.CustomerManageDO;
import com.yunda.base.bigcustomer.domain.OperateDO;
import com.yunda.base.bigcustomer.domain.OrderDO;
import com.yunda.base.bigcustomer.domain.OrganizationManageDO;
import com.yunda.base.bigcustomer.service.ConsultFileService;
import com.yunda.base.bigcustomer.service.CustomerManageService;
import com.yunda.base.bigcustomer.service.OrganizationManageService;
import com.yunda.base.bigcustomer.service.StatementTypeService;
import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.enums.DictEnum;
import com.yunda.base.common.enums.RespEnum;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.service.RoleService;
import com.yunda.base.system.service.UserService;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.StringUtils;

/**
 * @program: bigCustomer->OrderForAppController
 * @author: luzhiwei
 * @email: luzhiwei8794@yundasys.com
 * @create: 2019-07-22 11:28
 * @description: App咨询单接口
 */
@RestController
@RequestMapping("/bigcustomer/order/api/")
public class OrderAppController extends BaseController {

    Logger log = Logger.getLogger(getClass());

    //13位数字正则
    private static final String REGEX_13_DIGITAL = "^\\d{13}$";

    //数字和26个英文字母组成的字符串正则
    private static final String REGEX_ENGLISH_DIGITAL = "^[A-Za-z0-9]+$";

    //不超过8位数字正则
    private static final String REGEX_LIMIT_8_DIGITAL = "^\\d{1,8}$";

    //11位或者12位数字
    private static final String REGEX_PHONE_DIGITAL = "^\\d{11,12}$";

    //1000
    private static final Integer ONE_THOUSAND = 1000;

    @Autowired
    private OrderAppService orderAppService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private OrganizationManageService organizationManageService;
    @Autowired
    private CustomerManageService customerManageService;
    @Autowired
    private ConsultFileService consultFileService;
    @Autowired
    private StatementTypeService statementTypeService;
    @Autowired
    private UserService userService;

    @GetMapping("/getOrderDetail")
    public RspBean getOrderDetail(@RequestParam String orderId, HttpServletRequest request) {
        //APP请求校验token，根据需要解析token获取相应的用户信息和权限
        RspBean rspBean = getUserDOByToken(request);
        if (rspBean.getCode() != RespEnum.SUCCESS.getCode()) {
            return rspBean;
        }
        if (StringUtils.isBlank(orderId)) {
            log.error("OrderAppController - getOrderDetail: 请求参数为空[{}]", null);
            return new RspBean(RespEnum.ERROR_REQ_MISSPARAM);
        }
        OrderDO order = orderAppService.getOrderDetailByOrderId(orderId);

        if (null == order) {
            return new RspBean(RespEnum.ERROR_BUSINESS_OPERATE.getCode(), "未查到此工单详情");
       }
        return new RspBean<>(order);
    }

    @GetMapping("/getAllConsultType")
    public RspBean getAllConsultType(HttpServletRequest request) {
        RspBean rspBean = getUserDOByToken(request);
        if (rspBean.getCode() != RespEnum.SUCCESS.getCode()) {
            return rspBean;
        }
        List<String> list = orderAppService.getAllConsultType();
        return new RspBean<>(list);
    }

    @GetMapping("getCustomerAll")
    public RspBean getCustomerAll(HttpServletRequest request) {
        RspBean rspBean = getUserDOByToken(request);
        if (rspBean.getCode() != RespEnum.SUCCESS.getCode()) {
            return rspBean;
        }
        List<CustomerManageDO> customerList = customerManageService.getCustomerAll();
        return new RspBean<>(customerList);
    }

    @GetMapping("getPriority")
    public RspBean getPriority(HttpServletRequest request) {
        RspBean rspBean = getUserDOByToken(request);
        if (rspBean.getCode() != RespEnum.SUCCESS.getCode()) {
            return rspBean;
        }
        List<String> priorityList = new ArrayList<>();
        priorityList.add("一般");
        priorityList.add("紧急");
        priorityList.add("低");
        return new RspBean<>(priorityList);
    }

    @PostMapping("/save/{force}")
    public RspBean save(@PathVariable("force") String force,OrderDO order, HttpServletRequest request) {
        //APP请求校验token，根据需要解析token获取相应的用户信息和权限
        RspBean rspBean = getUserDOByToken(request);
        if (rspBean.getCode() != RespEnum.SUCCESS.getCode()) {
            return rspBean;
        }
        UserDO userDO = (UserDO) rspBean.getData();
        //0-第一次保存查询是否存在
        //1-第二次请求并确定强制保存
        if (StringUtils.isBlank(force)) {
            return new RspBean(RespEnum.ERROR_REQ_MISSPARAM.getCode(), "请求路径参数为空");
        }
        if(!"0".equals(force) && !"1".equals(force)) {
            return new RspBean(RespEnum.ERROR_REQ_MISSPARAM.getCode(), "请求路径参数不正确");
        }

        //运单号和物流单号不能同时为空
        if (StringUtils.isBlank(order.getWaybillNum())
                && StringUtils.isBlank(order.getLogisticOrderNum())) {
            return new RspBean(RespEnum.ERROR_REQ_MISSPARAM.getCode(), "运单号和物流单号同时为空");
        }
        //校验不能为空
        if (StringUtils.isBlank(order.getConsultType())
                || StringUtils.isBlank(order.getPriority())
                || StringUtils.isBlank(order.getOrganizationNum())
                || StringUtils.isBlank(order.getProblemDescription())) {
            return new RspBean(RespEnum.ERROR_REQ_MISSPARAM.getCode(), "请求参数存在空参数");
        }
        //运单号校验13位数字
        if (StringUtils.isNotBlank(order.getWaybillNum())) {
            if (!Pattern.matches(REGEX_13_DIGITAL, order.getWaybillNum())) {
                return new RspBean(RespEnum.ERROR_REQ_BADPARAM.getCode(), "运单号校验不通过");
            }
        }
        //物流订单号校验数字或英文字母
        if (StringUtils.isNotBlank(order.getLogisticOrderNum())) {
            if (!Pattern.matches(REGEX_ENGLISH_DIGITAL, order.getLogisticOrderNum())) {
                return new RspBean(RespEnum.ERROR_REQ_BADPARAM.getCode(), "物流订单校验不通过");
            }
        }
        //校验处理机构编码不超过8位
        if (!Pattern.matches(REGEX_LIMIT_8_DIGITAL, order.getOrganizationNum())) {
            return new RspBean(RespEnum.ERROR_REQ_BADPARAM.getCode(), "处理机构编码校验不通过");
        }
        //校验处理机构编码是否存在
        int count = organizationManageService.countByOrganizationNum(order.getOrganizationNum());
        if (count < 1) {
            return new RspBean(RespEnum.ERROR_REQ_PARAM.getCode(), "处理机构编码不存在");
        }
        //校验咨询来电电话(11位 或 12位数字)
        if (StringUtils.isNotBlank(order.getPhone()) && !Pattern.matches(REGEX_PHONE_DIGITAL, order.getPhone())) {
            return new RspBean(RespEnum.ERROR_REQ_BADPARAM.getCode(), "电话号码校验不合法");
        }
        if (order.getProblemDescription().length() > ONE_THOUSAND) {
            return new RspBean(RespEnum.ERROR_REQ_PARAM.getCode(), "问题描述长度不能超过1000个字");
        }

        //重复查询校验：条件运单号或者物流单号，同类型，存在则提示，无则直接进行
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setWaybillNum(order.getWaybillNum());
        orderDTO.setLogisticOrderNum(order.getLogisticOrderNum());
        orderDTO.setConsultType(order.getConsultType());
        int repeatNum = orderAppService.count(orderDTO);
        if ("0".equals(force)) {
            if (repeatNum >= 1) {
                //标识查询成功，存在重复咨询单
                return new RspBean<>("0");
            }
        }
        int flag = orderAppService.save(order, userDO);
        if (flag < 1) {
            return new RspBean(RespEnum.ERROR_BUSINESS_OPERATE.getCode(), "发起咨询单创建失败");
        }
        //标识存储成功
        return new RspBean<>("1");
    }

    @PostMapping("/list")
    public RspBean list(OrderDTO orderDTO, HttpServletRequest request) {
        RspBean rspBean = getUserDOByToken(request);
        if (rspBean.getCode() != RespEnum.SUCCESS.getCode()) {
            return rspBean;
        }
        UserDO userDO = (UserDO) rspBean.getData();

        //请求参数匹配转存，后端数据库存放内容不变，枚举匹配转换，同字段
        //工单状态 0：待申领，1：待处理，2：处理中，3：已结单
        if (StringUtils.isNotBlank(orderDTO.getState())) {
            orderDTO.setState(DictEnum.getValueByKey("state", orderDTO.getState()));
        }
        //优先级请求参数转换 0：一般 1：紧急 2：低
        if (StringUtils.isNotBlank(orderDTO.getPriority())) {
            orderDTO.setPriority(DictEnum.getValueByKey("priority", orderDTO.getPriority()));
        }
        //时效状态 0：全部，1：超时，2：预警，3：正常
        if (StringUtils.isNotBlank(orderDTO.getShiXiaoState())) {
            orderDTO.setShiXiaoState(DictEnum.getValueByKey("shiXiaoState", orderDTO.getShiXiaoState()));
        }
        //获取用户数据权限判断是什么权限
        Long userId = userDO.getUserId();
        //根据roleId去查询该角色的数据权限
        String dataPermissions = roleService.getDataPermissionsByUserId(userId);
        String username = userDO.getUsername();

        //本人发起(本人发起给别的组织机构的单，上级角色也可以看到)
        if (StringUtils.isNotBlank(orderDTO.getFaQiI()) && "true".equals(orderDTO.getFaQiI())) {
            orderDTO.setFaqiCode(username);
        } else {
            if (StringUtils.isNotBlank(dataPermissions)) {
                if ("1".equals(dataPermissions)) {
                    orderDTO.setDataPermissions1(username);
                    orderDTO.setUserName(username);
                } else if ("2".equals(dataPermissions)) {
                    //代表是本部门(获取本部门所有人的账号,可以看本部门处理和本部门创建的数据)
                    //1.获取当前用户的所属机构下的所有人的账号
                    String orgCode = orderAppService.getOrgCodeByUserName(username);
                    //2.根据机构编码获取该机构下的所有用户账号
                    List<String> userNameList = orderAppService.getUserNameListByOrgCode(orgCode);
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
                    orderDTO.setDataPermissions2(res);
                    orderDTO.setOrgCode(orgCode);
                } else if ("3".equals(dataPermissions)) {
                    //具有所有数据权限
                }
            } else {
                //代表没有权限查看数据，返回空数据
                PageUtils pageUtils = new PageUtils(null, 0);
                return success(pageUtils);
            }
        }

        if (StringUtils.isBlank(orderDTO.getWaybillNum())) {
            //如果时间没传递过来,默认查询90天内的数据
            if (StringUtils.isBlank(orderDTO.getStartDate()) || StringUtils.isBlank(orderDTO.getEndDate())) {
                orderDTO.setStartDate(DateUtils.formatDate(DateUtils.getDate(new Date(),-90), "yyyy-MM-dd 00:00:00"));
                orderDTO.setEndDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd 23:59:59"));
            }
            //校验时间格式是否符合要求
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                format.setLenient(false);
                format.parse(orderDTO.getStartDate());
                format.parse(orderDTO.getEndDate());
            } catch (ParseException e) {
                log.error("OrderAppController - list: 传入参数时间格式错误");
                return new RspBean(RespEnum.ERROR_REQ_BADPARAM.getCode(), "时间参数格式不为 yyyy-MM-dd HH:mm:ss");
            }

            //获取间隔天数
            Date startDate = DateUtils.parseDate(orderDTO.getStartDate());
            Date endDate = DateUtils.parseDate(orderDTO.getEndDate());
            int days = DateUtils.getDaysBetween(startDate, endDate);
            //咨询时间间隔大于90天，默认返回90天的数据
            if (days > 90) {
                orderDTO.setStartDate(DateUtils.formatDate(DateUtils.getDate(new Date(),-90), "yyyy-MM-dd 00:00:00"));
                orderDTO.setEndDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd 23:59:59"));
            }
        }

        //本人申领
        if (StringUtils.isNotBlank(orderDTO.getShenLingI()) && "true".equals(orderDTO.getShenLingI())) {
            if (StringUtils.isBlank(orderDTO.getState())) {
                orderDTO.setDealCode(username);
                //我处理的去除待申领状态的单,state 和 shenglingI 不能同时传参数
                orderDTO.setState(DictEnum.STATE_TO_APPLY.getValue());
            } else {
                log.error("OrderAppController - list: 传入参数业务业务逻辑不正确");
                return new RspBean(RespEnum.ERROR_REQ_BADPARAM.getCode(), "state 和 shenlingI 状态不能同时传入");
            }
        }

        //分页参数 排序 设置
        Integer offset = (null == orderDTO.getOffset()) ? 0 : orderDTO.getOffset();
        Integer limit = (null == orderDTO.getLimit()) ? 10 : orderDTO.getLimit();
        String sort = (null == orderDTO.getSort() || "".equals(orderDTO.getSort())) ? "consult_time" : orderDTO.getSort();
        String order = (null == orderDTO.getOrder() || "".equals(orderDTO.getOrder())) ? "asc" : orderDTO.getOrder();
        orderDTO.setOffset(offset);
        orderDTO.setLimit(limit);
        orderDTO.setSort(sort);
        orderDTO.setOrder(order);

        //如果有勾选时效预警状态就作为条件在遍历的时候判断
        String shiXiaoState = orderDTO.getShiXiaoState();
        //log.debug("list执行时间start："  + DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));
        //拿到所有参数进行数据查询
        List<OrderDO> orderList = orderAppService.list(orderDTO);
        //log.debug("list执行时间end："  + DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));
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
            if(StringUtils.isNotBlank(orderDO.getJieDanTime())){
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
            if (StringUtils.isNotBlank(shiXiaoState)) {
                if (null != orderDO.getShiXiaoState() && !shiXiaoState.equals(orderDO.getShiXiaoState())) {
                    //迭代器的删除办法(单线程情况下)
                    iter.remove();
                } else if (orderDO.getShiXiaoState() == null) {
                    //迭代器的删除办法(单线程情况下)
                    iter.remove();
                }

            }
            //判断如果是已申领就将剩余时间改为"-"
            if(null != orderDO.getState() && "已结单".equals(orderDO.getState())){
                orderDO.setShengYuTime("-");
            }
        }
        int total = orderAppService.count(orderDTO);
        PageUtils pageUtils = new PageUtils(orderList, total);
        return new RspBean<>(pageUtils);
    }

    //把判断语句抽出来
    public OrderDO getYuJingOrderDO(OrderDO orderDO,Long currentHours,Long resH,int consultTimeHours,String date){
       /* if(orderDO.getState()!=null && orderDO.getState().equals("已结单") && orderDO.getJieDanTime()!=null && !orderDO.getJieDanTime().equals("")){
            consultTimeHours = Integer.parseInt(orderDO.getJieDanTime().substring(11, 13));
        }*/
        //存入的时候将2的转化成小时
        if (orderDO.getType() != null && ("1".equals(orderDO.getType()) || "2".equals(orderDO.getType()))) {
            //判断如果勾选2并且天为0
            if ("2".equals(orderDO.getType()) && "0".equals(orderDO.getOrderAfterDayT2())) {

                //勾选了第二种,并且天数为0即当天,比较当前时间的小时数,如果当前时间的小时数大于等于配置中的即为超时(将时间转为毫秒值比较)
                if (!date.equals(orderDO.getConsultTime().substring(0, 10)) || (date.equals(orderDO.getConsultTime().substring(0, 10)) && currentHours >= Integer.parseInt(orderDO.getOrderAfterTimeT2()))) {
                    orderDO.setShiXiaoState("超时");
                    //超时的话就把剩余时间设置为0
                    orderDO.setShengYuTime("0");
                } else if (null != orderDO.getYujingTime() && date.equals(orderDO.getConsultTime().substring(0, 10)) && currentHours <= Integer.parseInt(orderDO.getOrderAfterTimeT2()) && Integer.parseInt(orderDO.getOrderAfterTimeT2()) - currentHours <= Integer.parseInt(("".equals(orderDO.getYujingTime()) ? "0" : orderDO.getYujingTime()))) {
                    //如果resH的时间小于设定的超时时间,并且减去设定的超时小时不为福则为预警
                    orderDO.setShiXiaoState("预警");
                    orderDO.setShengYuTime(Integer.parseInt(orderDO.getOrderAfterTimeT2()) - currentHours + "小时");
                } else {
                    orderDO.setShiXiaoState("正常");
                    orderDO.setShengYuTime(Integer.parseInt(orderDO.getOrderAfterTimeT2()) - currentHours + "小时");
                }
            } else if ("2".equals(orderDO.getType()) && !"0".equals(orderDO.getOrderAfterDayT2())) {
                //如果是类型1的话就按照1的方式去去判断是否超时以及剩余时间,预警等信息
                int orderAfterHoursT1 = Integer.parseInt(orderDO.getOrderAfterHoursT1()) + 24 - consultTimeHours - 1;
                //获取当前时间,然后判断,如果当前时间减去咨询时间大于类型1的时间则状态为超时
                if (resH >= orderAfterHoursT1) {
                    orderDO.setShiXiaoState("超时");
                    //超时的话就把剩余时间设置为0
                    orderDO.setShengYuTime("0");
                } else if (null != orderDO.getYujingTime() && resH <= orderAfterHoursT1 && orderAfterHoursT1 - resH <= Integer.parseInt(("".equals(orderDO.getYujingTime()) ? "0" : orderDO.getYujingTime()))) {
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
                } else if (null != orderDO.getYujingTime() && resH <= Integer.parseInt(orderAfterHoursT1) && Integer.parseInt(orderAfterHoursT1) - resH <= Integer.parseInt(("".equals(orderDO.getYujingTime()) ? "0" : orderDO.getYujingTime()))) {
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
        if (null != orderDO.getType() && "3".equals(orderDO.getType())) {
            //当天几点前发起的单子,第几天几点前未结单则为超时
            //1.先判断发起单子的时间是否在这个时间之前(判断小时即便是等于也为之后)
            if (consultTimeHours < Integer.parseInt(orderDO.getTodayOrderBeforeTimeT31())) {
                //如果是0天就代表是今天
                if ("0".equals(orderDO.getOrderAfterDayT31())) {
                    int orderAfterTimeT31 = Integer.parseInt(orderDO.getOrderAfterTimeT31());
                    //判断当前时间减去咨询单创建时间是否大于t31Hours如果大于则为超时
                    if (!date.equals(orderDO.getConsultTime().substring(0, 10)) || (date.equals(orderDO.getConsultTime().substring(0, 10)) && currentHours >= orderAfterTimeT31)) {
                        orderDO.setShiXiaoState("超时");
                        //超时的话就把剩余时间设置为0
                        orderDO.setShengYuTime("0");
                    } else if (null != orderDO.getYujingTime() && date.equals(orderDO.getConsultTime().substring(0, 10)) && currentHours < orderAfterTimeT31 && orderAfterTimeT31 - currentHours <= Integer.parseInt("".equals(orderDO.getYujingTime()) ? "0" : orderDO.getYujingTime())) {
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
                    } else if (null != orderDO.getYujingTime() && resH <= t31Hours && t31Hours - resH <= Integer.parseInt("".equals(orderDO.getYujingTime()) ? "0" : orderDO.getYujingTime())) {
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
                if ("0".equals(orderDO.getOrderBeforeDayT32())) {
                    int orderBeforeTimeT32 = Integer.parseInt(orderDO.getOrderBeforeTimeT32());
                    //后面改的时候加个结单时间
                    //判断当前时间减去咨询单创建时间是否大于t31Hours如果大于则为超时
                    if (!date.equals(orderDO.getConsultTime().substring(0, 10)) || (date.equals(orderDO.getConsultTime().substring(0, 10)) && currentHours >= orderBeforeTimeT32)) {
                        orderDO.setShiXiaoState("超时");
                        //超时的话就把剩余时间设置为0
                        orderDO.setShengYuTime("0");
                    } else if (null != orderDO.getYujingTime() && currentHours <= orderBeforeTimeT32 && orderBeforeTimeT32 - currentHours > Integer.parseInt("".equals(orderDO.getYujingTime()) ? "0" : orderDO.getYujingTime())) {
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
                    } else if (null != orderDO.getYujingTime() && resH <= t32Hours && t32Hours - resH <= Integer.parseInt("".equals(orderDO.getYujingTime()) ? "0" : orderDO.getYujingTime())) {
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

    /**
     * 申领
     * @param orderId 咨询单唯一ID
     * @return ResBean
     */
    @GetMapping("/shenLing")
    @Transactional
    public RspBean shenLing(@RequestParam("orderId") String orderId, HttpServletRequest request) {
        RspBean rspBean = getUserDOByToken(request);
        if (rspBean.getCode() != RespEnum.SUCCESS.getCode()) {
            return rspBean;
        }
        UserDO userDO = (UserDO) rspBean.getData();
        //判断请求参数是否为空
        if (StringUtils.isBlank(orderId)) {
            return new RspBean(RespEnum.ERROR_REQ_MISSPARAM.getCode(), "请求参数orderId为空");
        }
        //判断是否为待申领
        OrderDO orderDO = orderAppService.getOrderByOrderId(orderId);
        if (!"待申领".equals(orderDO.getState())) {
            return new RspBean(RespEnum.ERROR_BUSINESS_OPERATE.getCode(), "咨询单状态不为待申领状态，无法申领");
        }
        //获取登陆用户信息
        String name = userDO.getName();
        //获取用户账号
        String userName = userDO.getUsername();
        String state = "待处理";
        //存入申领人账号(也就是处理人账号)
        orderAppService.shenLing(orderId, userName, name, state);
        //保存操作记录
        OperateDO operate = saveOperateDO(orderId, "申领", userDO);
        orderAppService.saveOperateInfo(operate);
        return new RspBean(RespEnum.SUCCESS);
    }

    /**
     * 指派人列表
     */
    @GetMapping("/zhiPai/list")
    public RspBean zhiPaiList(@RequestParam("name") String name, HttpServletRequest request) {
        RspBean rspBean = getUserDOByToken(request);
        if (rspBean.getCode() != RespEnum.SUCCESS.getCode()) {
            return rspBean;
        }
        UserDO userDO = (UserDO) rspBean.getData();
        String username = userDO.getUsername();
        //根据账号获取角色
        String roleName = orderAppService.getRoleNameByUserName(username);
        //1.获取当前用户的所属机构下的所有人的账号
        String orgCode = orderAppService.getOrgCodeByUserName(username);
        //如果不是总部就只能指派本机构
        List<String> allUserInfo = null;
        if (!roleName.equals("总部") && !roleName.equals("超级用户角色")) {
            //指派的时候只显示本机构的用户
            allUserInfo = orderAppService.getAllUserInfoByOrgCode(orgCode, name);
        }else {
            allUserInfo = orderAppService.getAllUserInfoByOrgCode(null, name);
        }
        return new RspBean<>(allUserInfo);
    }

    /**
     * 指派
     */
    @GetMapping("/zhiPai/save")
    @Transactional
    public RspBean zhiPaiSave(@RequestParam("orderId") String orderId, @RequestParam("dealMan") String dealMan,
                              HttpServletRequest request) {
        RspBean rspBean = getUserDOByToken(request);
        if (rspBean.getCode() != RespEnum.SUCCESS.getCode()) {
            return rspBean;
        }
        UserDO userDO = (UserDO) rspBean.getData();

        if (StringUtils.isBlank(orderId) || StringUtils.isBlank(dealMan)) {
            return new RspBean(RespEnum.ERROR_REQ_MISSPARAM.getCode(), "请求参数不能为空");
        }
        //判断是否为待申领
        OrderDO orderDO = orderAppService.getOrderByOrderId(orderId);
        if (!"待申领".equals(orderDO.getState())) {
            return new RspBean(RespEnum.ERROR_BUSINESS_OPERATE.getCode(), "咨询单状态不为待申领状态，无法指派");
        }
        if (!dealMan.contains("-")) {
            return new RspBean(RespEnum.ERROR_REQ_BADPARAM.getCode(), "您指派的人员信息不规范");
        }
        //获取dealCode
        String dealCode = dealMan.split("-")[1];
        //对条件进行校验,判断用户是否存在
        UserDO user = orderAppService.getUserByUserName(dealCode);
        if (null == user) {
            return new RspBean(RespEnum.ERROR_REQ_BADPARAM.getCode(), "您指派的用户不存在");
        }
        String name = user.getName();
        String state = "待处理";
        //存入申领人账号(也就是处理人账号)
        orderAppService.shenLing(orderId, dealCode, name, state);
        //保存操作记录
        OperateDO operate = saveOperateDO(orderId, "指派", userDO);
        orderAppService.saveOperateInfo(operate);
        return new RspBean(RespEnum.SUCCESS);
    }

    /**
     * 转发组织机构编码列表
     */
    @GetMapping("/zhuanFa/list")
    public RspBean zhuanFaList(@RequestParam("organizationNum") String organizationNum, HttpServletRequest request) {
        RspBean rspBean = getUserDOByToken(request);
        if (rspBean.getCode() != RespEnum.SUCCESS.getCode()) {
            return rspBean;
        }

        List<OrganizationManageDO> allOrgInfo = orderAppService.findAllOrgInfo(organizationNum);
        return new RspBean<>(allOrgInfo);
    }

    /**
     * 转发提交保存
     */
    @ResponseBody
    @GetMapping("/zhuanFa/save")
    public RspBean zhuanFaSave(@RequestParam("orderId") String orderId, @RequestParam("organizationNum") String organizationNum,
                               HttpServletRequest request) {
        RspBean rspBean = getUserDOByToken(request);
        if (rspBean.getCode() != RespEnum.SUCCESS.getCode()) {
            return rspBean;
        }
        UserDO userDO = (UserDO) rspBean.getData();
        if (StringUtils.isBlank(orderId) || StringUtils.isBlank(organizationNum)) {
            return new RspBean(RespEnum.ERROR_REQ_MISSPARAM.getCode(), "请求参数不能为空");
        }
        //判断是否为待申领
        OrderDO orderDO = orderAppService.getOrderByOrderId(orderId);
        if ("已结单".equals(orderDO.getState())) {
            return new RspBean(RespEnum.ERROR_BUSINESS_OPERATE.getCode(), "咨询单状态为已结单状态，无法转发");
        }
        String state = "待申领";
        //存入转发的组织机构和状态
        orderAppService.zhuanFa(orderId, organizationNum, state);
        //保存操作记录
        OperateDO operate = saveOperateDO(orderId, "转发", userDO);
        orderAppService.saveOperateInfo(operate);
        return new RspBean(RespEnum.SUCCESS);
    }

    /**
     * 同一咨询单操作记录
     * @param orderDTO 封装类
     * @return RspBean
     */
    @PostMapping("/caoZuo")
    public RspBean caoZuo(OrderDTO orderDTO, HttpServletRequest request) {
        RspBean rspBean = getUserDOByToken(request);
        if (rspBean.getCode() != RespEnum.SUCCESS.getCode()) {
            return rspBean;
        }

        if (null == orderDTO || StringUtils.isBlank(orderDTO.getOrderId())) {
            return new RspBean(RespEnum.ERROR_REQ_MISSPARAM.getCode(), "请求参数咨询工单号为空");
        }
        List<OperateDO> orderList = orderAppService.getListByOrderId(orderDTO);
        String uploadPathPrefix = SysConfig.uploadPath;
        List<OperateDTO> operateDTOList = new ArrayList<>();
        if (orderList != null && orderList.size() > 0) {
            for (OperateDO operateDO : orderList) {
                List<ConsultFileDO> consultFileDOList = consultFileService.findByOrderIdAndTime(uploadPathPrefix,
                        operateDO.getOrderId(), operateDO.getTime());
                operateDTOList.add(new OperateDTO(operateDO, consultFileDOList));
            }
        }
        int total = orderAppService.countByOrderId(orderDTO.getOrderId());
        PageUtils pageUtils = new PageUtils(operateDTOList, total);
        return new RspBean<>(pageUtils);
    }

    /**
     * 同一运单历史记录
     * @param orderDTO 封装类
     * @return RspBean
     */
    @PostMapping("/qiTaWenTi")
    public RspBean qiTaWenTi(OrderDTO orderDTO, HttpServletRequest request) {
        RspBean rspBean = getUserDOByToken(request);
        if (rspBean.getCode() != RespEnum.SUCCESS.getCode()) {
            return rspBean;
        }

        if (null == orderDTO || StringUtils.isBlank(orderDTO.getWaybillNum())) {
            return new RspBean(RespEnum.ERROR_REQ_MISSPARAM.getCode(), "请求参数运单号为空");
        }
        List<OrderDO> orderList = orderAppService.getListByWaybillNum(orderDTO);
        int total = orderAppService.countByWaybillNum(orderDTO.getWaybillNum());
        PageUtils pageUtils = new PageUtils(orderList, total);
        return new RspBean<>(pageUtils);
    }

    /**
     * 上传附件
     */
    @ResponseBody
    @RequestMapping(value = "/upload", consumes = "multipart/*", headers = "content-type=mutipart/form-data", method = RequestMethod.POST)
    public RspBean upload(MultipartFile file, HttpServletRequest request) {
        RspBean rspBean = getUserDOByToken(request);
        if (rspBean.getCode() != RespEnum.SUCCESS.getCode()) {
            return rspBean;
        }

        String fileKey = UUID.randomUUID().toString();
        // 获取后缀
        String fileName = file.getOriginalFilename();
        Map<String, String> map  = new HashMap<>(2);
        if (fileName.lastIndexOf(".") != -1) {
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            String fullUploadPath = SysConfig.uploadPath + now() + fileKey + suffix;
            String uploadPath = fileKey + suffix;
            File _f = new File(fullUploadPath);
            if (!_f.getParentFile().exists()) {
                _f.getParentFile().mkdirs();
            }
            try {
                file.transferTo(new File(fullUploadPath));
            } catch (IOException e) {
                log.error("OrderAppController - upload: api/upload 文件上传失败");
                return new RspBean(RespEnum.ERROR_BUSINESS_OPERATE.getCode(), "文件上传失败");
            }
            map.put(uploadPath, fileName);
        }
        return new RspBean<>(map);
    }

    /**
     * 咨询单处理
     * @param jieDanDTO 处理
     * @param request 请求
     * @return RepBean
     */
    @PostMapping("/deal")
    @Transactional
    public RspBean dealSave(JieDanDTO jieDanDTO, HttpServletRequest request) {
        RspBean rspBean = getUserDOByToken(request);
        if (rspBean.getCode() != RespEnum.SUCCESS.getCode()) {
            return rspBean;
        }
        UserDO userDO = (UserDO) rspBean.getData();

        if (StringUtils.isBlank(jieDanDTO.getOrderIds())) {
            return new RspBean(RespEnum.ERROR_REQ_MISSPARAM.getCode(), "请求参数咨询单ID为空");
        }
        //判断是否为待处理和处理中
        OrderDO orderDO = orderAppService.getOrderByOrderId(jieDanDTO.getOrderIds());
        if ("待申领".equals(orderDO.getState())) {
            return new RspBean(RespEnum.ERROR_BUSINESS_OPERATE.getCode(), "咨询单未被申领，无法处理");
        }
        if ("已结单".equals(orderDO.getState())) {
            return new RspBean(RespEnum.ERROR_BUSINESS_OPERATE.getCode(), "咨询单已结单，无需处理");
        }
        if (StringUtils.isBlank(jieDanDTO.getJieDanCause())) {
            return new RspBean(RespEnum.ERROR_REQ_MISSPARAM.getCode(), "请求参数处理内容为空");
        }
        if (jieDanDTO.getJieDanCause().length() > ONE_THOUSAND) {
            return new RspBean(RespEnum.ERROR_REQ_BADPARAM.getCode(), "处理内容长度超过1000");
        }

        //更新工单咨询表
        jieDanDTO.setState("处理中");
        jieDanDTO.setJieDanTime(DateUtils.getCurrentTime());
        orderAppService.updateOrderByDeal(jieDanDTO);

        //添加处理操作记录
        OperateDO operate = saveOperateDO(jieDanDTO.getOrderIds(), "处理", userDO);
        operate.setDealContent(jieDanDTO.getJieDanCause());
        operate.setZeRenFang(jieDanDTO.getZeRenFang());
        orderAppService.saveOperateInfo(operate);

        //解析并存放文件
        List<ConsultFileDO> listConsultFileDO = new ArrayList<>();
        String[] fileUrlList = null;
        String[] fileNameList = null;
        if (null != jieDanDTO.getFileUrlList()) {
            fileUrlList = jieDanDTO.getFileUrlList().split(",");
        }
        if (null != jieDanDTO.getFileNameList()) {
            fileNameList = jieDanDTO.getFileNameList().split(",");
        }
        if (null != fileUrlList && fileUrlList.length > 0) {
            for (int i=0; i<fileUrlList.length; i++) {
                ConsultFileDO consultFileDO = new ConsultFileDO();
                consultFileDO.setOrderId(jieDanDTO.getOrderIds());
                consultFileDO.setFileType("0");
                consultFileDO.setStatus("1");
                consultFileDO.setCreateTime(operate.getTime());
                consultFileDO.setOperateCode(operate.getOperateCode());
                consultFileDO.setUploadPath(fileUrlList[i]);
                if (fileUrlList[i].lastIndexOf(".") != -1) {
                    consultFileDO.setFileSuffix(fileUrlList[i].substring(fileUrlList[i].lastIndexOf(".")));
                }
                if (null != fileNameList && null != fileNameList[i]) {
                    consultFileDO.setFileName(fileNameList[i]);
                }
                listConsultFileDO.add(consultFileDO);
            }
        }
        consultFileService.saveBatch(listConsultFileDO);
        return new RspBean(RespEnum.SUCCESS);
    }

    /**
     * 咨询单结单
     * @param jieDanDTO 结单
     * @param request 请求
     * @return RepBean
     */
    @PostMapping("/jieDan")
    @Transactional
    public RspBean jieDanSave(JieDanDTO jieDanDTO, HttpServletRequest request) {
        //申领，可以指派/处理/结单
        RspBean rspBean = getUserDOByToken(request);
        if (rspBean.getCode() != RespEnum.SUCCESS.getCode()) {
            return rspBean;
        }
        UserDO userDO = (UserDO) rspBean.getData();

        if (StringUtils.isBlank(jieDanDTO.getOrderIds())) {
            return new RspBean(RespEnum.ERROR_REQ_MISSPARAM.getCode(), "请求参数咨询单ID为空");
        }
        //判断是否为待处理和处理中
        OrderDO orderDO = orderAppService.getOrderByOrderId(jieDanDTO.getOrderIds());
        if ("待申领".equals(orderDO.getState())) {
            return new RspBean(RespEnum.ERROR_BUSINESS_OPERATE.getCode(), "咨询单未被申领，无法处理");
        }
        if ("已结单".equals(orderDO.getState())) {
            return new RspBean(RespEnum.ERROR_BUSINESS_OPERATE.getCode(), "咨询单已结单，无需处理");
        }
        if (StringUtils.isBlank(jieDanDTO.getJieDanResult())) {
            return new RspBean(RespEnum.ERROR_REQ_MISSPARAM.getCode(), "请求参数处理结果为空");
        }
        if (StringUtils.isBlank(jieDanDTO.getJieDanCause())) {
            return new RspBean(RespEnum.ERROR_REQ_MISSPARAM.getCode(), "请求参数处理内容为空");
        }
        if (jieDanDTO.getJieDanCause().length() > ONE_THOUSAND) {
            return new RspBean(RespEnum.ERROR_REQ_BADPARAM.getCode(), "处理内容长度超过1000");
        }

        //更新咨询单状态
        jieDanDTO.setState("已结单");
        jieDanDTO.setJieDanTime(DateUtils.getCurrentTime());
        orderAppService.updateOrderByDeal(jieDanDTO);

        //添加处理操作记录
        OperateDO operate = saveOperateDO(jieDanDTO.getOrderIds(), "结单", userDO);
        operate.setDealContent(jieDanDTO.getJieDanCause());
        operate.setZeRenFang(jieDanDTO.getZeRenFang());
        orderAppService.saveOperateInfo(operate);

        //解析并存放文件
        List<ConsultFileDO> listConsultFileDO = new ArrayList<>();
        String[] fileUrlList = null;
        String[] fileNameList = null;
        if (null != jieDanDTO.getFileUrlList()) {
            fileUrlList = jieDanDTO.getFileUrlList().split(",");
        }
        if (null != jieDanDTO.getFileNameList()) {
            fileNameList = jieDanDTO.getFileNameList().split(",");
        }
        if (null != fileUrlList && fileUrlList.length > 0) {
            for (int i=0; i<fileUrlList.length; i++) {
                ConsultFileDO consultFileDO = new ConsultFileDO();
                consultFileDO.setOrderId(jieDanDTO.getOrderIds());
                consultFileDO.setFileType("0");
                consultFileDO.setStatus("1");
                consultFileDO.setCreateTime(operate.getTime());
                consultFileDO.setOperateCode(operate.getOperateCode());
                consultFileDO.setUploadPath(fileUrlList[i]);
                if (fileUrlList[i].lastIndexOf(".") != -1) {
                    consultFileDO.setFileSuffix(fileUrlList[i].substring(fileUrlList[i].lastIndexOf(".")));
                }
                if (null != fileNameList && null != fileNameList[i]) {
                    consultFileDO.setFileName(fileNameList[i]);
                }
                listConsultFileDO.add(consultFileDO);
            }
        }
        consultFileService.saveBatch(listConsultFileDO);
        return new RspBean(RespEnum.SUCCESS);
    }

    /**
     * 获取处理结果列表
     * @param consultType  见DictEnum
     * @return 处理结果列表
     */
    @GetMapping("/listStatementResult")
    public RspBean listStatementResult(@RequestParam("consultType") String consultType, HttpServletRequest request) {
        RspBean rspBean = getUserDOByToken(request);
        if (rspBean.getCode() != RespEnum.SUCCESS.getCode()) {
            return rspBean;
        }
        if (StringUtils.isBlank(consultType)) {
            return new RspBean(RespEnum.ERROR_REQ_MISSPARAM.getCode(), "请求参数consultType不能为空");
        }
        if (StringUtils.isBlank(consultType)) {
            return new RspBean(RespEnum.ERROR_REQ_BADPARAM.getCode(), "请确认请求参数值是否正确");
        }
        List<String> list = statementTypeService.getJiedanList(consultType);
        if (null == list || list.size() < 1) {
            return new RspBean(RespEnum.ERROR_REQ_BADPARAM.getCode(), "请确认请求参数值是否正确");
        }
        return new RspBean<>(list);
    }

    /**
     * 获取用户可用应用角色菜单权限
     * @param request 请求
     * @return 菜单权限
     */
    @GetMapping("/findOrderMenuByToken")
    public RspBean findOrderMenuByToken(HttpServletRequest request) {
        RspBean rspBean = getUserDOByToken(request);
        if (rspBean.getCode() != RespEnum.SUCCESS.getCode()) {
            return rspBean;
        }
        UserDO userDO = (UserDO) rspBean.getData();
        Long userId = userDO.getUserId();
        Long parentId = Long.valueOf(DictEnum.ORDER_MANAGE.getKey());
        Long orderForMe = Long.valueOf(DictEnum.ORDER_FOR_ME.getKey());
        Long orderToCreate = Long.valueOf(DictEnum.ORDER_TO_CREATE.getKey());
        Long orderPool = Long.valueOf(DictEnum.ORDER_POOL.getKey());
        Long orderForMyCreate = Long.valueOf(DictEnum.ORDER_FOR_MYCREATE.getKey());
        Map<String, Object> map = new HashMap<>(8);

        List<Long> parentIdList = orderAppService.findOrderMenuByUserIdAndParentId(userId, parentId);
        if (parentIdList.contains(orderForMe)) {
            List<Long> orderForMeList = orderAppService.findOrderMenuByParam(userId, parentId, orderForMe);
            map.put("orderForMe", orderForMeList);
        } else {
            map.put("orderForMe", false);
        }
        if (parentIdList.contains(orderToCreate)) {
            map.put("orderToCreate", true);
        } else {
            map.put("orderToCreate", false);
        }
        if (parentIdList.contains(orderPool)) {
            List<Long> orderPoolList = orderAppService.findOrderMenuByParam(userId, parentId, orderPool);
            map.put("orderPool", orderPoolList);
        } else {
            map.put("orderPool", false);
        }
        if (parentIdList.contains(orderForMyCreate)) {
            map.put("orderForMyCreate", true);
        } else {
            map.put("orderForMyCreate", false);
        }
        return new RspBean<>(map);
    }

    /**
     * 获取用户角色信息
     * @param request 请求
     * @return 角色
     */
    @GetMapping("/getRoleByRequest")
    public RspBean getRoleByRequest(HttpServletRequest request) {
        RspBean rspBean = getUserDOByToken(request);
        if (rspBean.getCode() != RespEnum.SUCCESS.getCode()) {
            return rspBean;
        }
        UserDO userDO = (UserDO) rspBean.getData();
        Long userId = userDO.getUserId();
        RoleDTO roleDTO = orderAppService.getRoleByUserId(userId);
        return new RspBean<>(roleDTO);
    }

    /**
     * 操作保存
     * @param orderId 咨询单ID
     * @param type 操作类型
     * @param userDO 登录用户信息
     */
    private OperateDO saveOperateDO(String orderId, String type, UserDO userDO) {
        //判断是不是勾选了多个,如果勾选多个就要批量存放操作记录
        String userName = userDO.getUsername();
        //操作人名字
        String name = userDO.getName();
        OperateDO operateDO = new OperateDO();
        operateDO.setOrderId(orderId);
        operateDO.setOperateName(name);
        operateDO.setOperateCode(userName);
        operateDO.setType(type);
        //根据用户账号获取用户所属机构,最好存放机构编码
        String orgCodeByUserName = orderAppService.getOrgCodeByUserName(userName);
        operateDO.setOperateOrganization(orgCodeByUserName);
        operateDO.setTime(DateUtils.getCurrentTime());
        return operateDO;
    }

    /**
     * token 校验
     * @param request
     * @return
     */
    private RspBean getUserDOByToken(HttpServletRequest request) {
        //APP请求校验token，更具需要解析token获取相应的用户信息和权限
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token) || !StringUtils.contains(token, "-")) {
            return new RspBean(RespEnum.ERROR_LOGIN_WRONG.getCode(), "token不存在");
        }
        String username = token.substring(token.lastIndexOf("-") + 1);
        if (StringUtils.isBlank(username)) {
            return new RspBean(RespEnum.ERROR_LOGIN_WRONG.getCode(), "用户名为空");
        }
        List<UserDO> userList = userService.queryLoginInfo(username);
        if (userList == null || userList.size() < 1) {
            return new RspBean(RespEnum.ERROR_LOGIN_WRONG.getCode(), "用户不存在");
        }
        if (userList.size() > 1) {
            log.error("OrderAppController - getUserDOByToken: " + username + "出现多个用户，请关注");
        }
        UserDO loginUser = userList.get(0);
        return new RspBean<>(loginUser);
    }
}
