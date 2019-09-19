package com.yunda.base.common.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.yunda.base.common.config.Constant;
import com.yunda.base.common.enums.RoleEnum;
import com.yunda.base.common.utils.SpringUtil;
import com.yunda.base.feiniao.schedule.suckdata.vo.TaskParams;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.AlarmSmsDO;
import com.yunda.base.system.domain.LoginUserDO;
import com.yunda.base.system.service.AlarmService;
import com.yunda.base.system.service.AlarmSmsService;
import com.yunda.base.system.service.LoginService;
import com.yunda.base.system.service.LoginUserService;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;

/**
 * 功能描述: <br>
 * 〈定时发送预警短信〉
 *
 * @Author:22374
 * @Date: 2019/3/11 16:46
 * @Param:
 * @return:
 * @since: 1.0.0
 */
@DisallowConcurrentExecution
public class SchedualMessageSendTask extends TaskAbs {
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private AlarmSmsService alarmSmsService;

    @Override
    public void run(JobExecutionContext arg0) {
        AlarmSmsService alarmSmsService = SpringUtil.getBean(AlarmSmsService.class);
        AlarmService alarmService = SpringUtil.getBean(AlarmService.class);
        LoginService loginService = SpringUtil.getBean(LoginService.class);
        StringRedisTemplate stringRedisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        LoginUserService loginUserService = SpringUtil.getBean(LoginUserService.class);

        TaskParams tp = TaskParams.newInstance(arg0);
        int from = tp.getTargetDay();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.getDate(from));

        Map<String, Object> map = new HashMap<>();
        map.put("alarmTime", date);
        List<AlarmSmsDO> alarmSmsDOList = alarmSmsService.list(map);

        List<LoginUserDO> list = new ArrayList<>();
        List<String> phoneList = new ArrayList<>();
        Set<String> usernames = new HashSet<>();
        if (alarmSmsDOList != null && alarmSmsDOList.size() > 0) {
            for (AlarmSmsDO alarmSmsDO : alarmSmsDOList) {
                if (StringUtils.isNoneBlank(alarmSmsDO.getProvinceName())) {
                    stringRedisTemplate.opsForHash().increment("crmkh_" + alarmSmsDO.getBigarea(), alarmSmsDO.getProvinceName(), alarmSmsDO.getProvinceCount());
                    stringRedisTemplate.expire(alarmSmsDO.getBigarea(), 1, TimeUnit.HOURS);
                }
            }
        }


        if (alarmSmsDOList != null && alarmSmsDOList.size() > 0) {
            for (AlarmSmsDO alarmSmsDO : alarmSmsDOList) {
                if (StringUtils.isNotBlank(alarmSmsDO.getProvinceName())) {
                    //本省触发的报警次数
                    log.info("省总姓名:[{}],触发报警的次数:[{}]", alarmSmsDO.getUserName(), alarmSmsDO.getProvinceCount());
                    String sz_message_format = String.format(SysConfig.MESSAGE_TEMPLATE_SZ, alarmSmsDO.getUserName(), date, alarmSmsDO.getProvinceCount());
                    Map<String, Object> provinceMap = new HashMap<>();
                    provinceMap.put("institution", alarmSmsDO.getProvinceName());
                    List<LoginUserDO> loginUserDOList = loginUserService.list(provinceMap);
                    if (loginUserDOList != null && loginUserDOList.size() > 0) {
                        String messageFallback = loginService.sendMsm(sz_message_format, loginUserDOList.get(0).getMobile());
                        parseMessage(messageFallback, loginUserDOList.get(0).getMobile());
                    }
                }
            }
        }
        //给区总协助人发送预警短信
        int provinceCount = 0;
        Set<String> keys = stringRedisTemplate.keys("crmkh_*");
        for (String bigarea : keys) {
            Map<String, Object> bigAreaMap = new HashMap<>(16);
            String bigArea = bigarea.replaceAll("crmkh_", "");
            bigAreaMap.put("institution", bigArea);
            List<LoginUserDO> list1 = loginUserService.list(bigAreaMap);
            for (LoginUserDO loginUserDO : list1) {
                if (StringUtils.equals(loginUserDO.getRole(), RoleEnum.QZXZR.getRolename())) {
                    Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries("crmkh_" + loginUserDO.getInstitution());
                    Set<Object> objects1 = entries.keySet();
                    for (Object o : objects1) {
                        provinceCount += Integer.valueOf((String) entries.get(o));
                    }
                    String qzxzrMessageFormat = String.format(SysConfig.MESSAGE_TEMPLATE_QZXZR, loginUserDO.getName(), date, provinceCount);
                    String messageFallback2Qzxzr = loginService.sendMsm(qzxzrMessageFormat, loginUserDO.getMobile());
                    parseMessage(messageFallback2Qzxzr, loginUserDO.getMobile());
                }
            }
        }
        //给区总发送预警短信
        if (alarmSmsDOList != null && alarmSmsDOList.size() > 0) {
            for (AlarmSmsDO alarmSmsDO : alarmSmsDOList) {
                stringRedisTemplate.opsForValue().increment("crmkh_qz" + alarmSmsDO.getBigarea(), alarmSmsDO.getProvinceCount());
            }
            Set<String> crmkh_qz = stringRedisTemplate.keys("crmkh_qz*");
            for (String vipkf : crmkh_qz) {
                String bigArea = vipkf.replace("crmkh_qz", "");
                String s = "";
                int localArea = 0;
                int provinceSum = 0;
                int provinceCountQZ = 0;
                int sumQZ = 0;
                String s1 = stringRedisTemplate.opsForValue().get(bigArea);
                Map<String, Object> stringObjectHashMap = new HashMap<>();
                stringObjectHashMap.put("bigarea", bigArea);
                List<AlarmSmsDO> list1 = alarmSmsService.list(stringObjectHashMap);
                for (AlarmSmsDO alarmSmsDO : list1) {
                    if (StringUtils.isNotBlank(alarmSmsDO.getProvinceName())) {
                        //多少省
                        provinceSum++;
                        //多少次
                        provinceCountQZ += alarmSmsDO.getProvinceCount();
                        sumQZ += alarmSmsDO.getProvinceCount();
                        //省计算
                        s += alarmSmsDO.getProvinceName() + ":" + alarmSmsDO.getProvinceCount() + "次;";
                    } else {
                        //本大区
                        localArea += alarmSmsDO.getProvinceCount();
                        sumQZ += alarmSmsDO.getProvinceCount();
                    }
                }
                Map<String, Object> map1 = new HashMap<>(16);
                map1.put("institution", bigArea);
                map1.put("role", RoleEnum.QZ.getRolename());
                List<LoginUserDO> list2 = loginUserService.list(map1);
                if (!CollectionUtils.isEmpty(list2)) {
                    String messageTemQZ = String.format(SysConfig.MESSAGE_TEMPLATE_QZ, list2.get(0).getName(), date, sumQZ, provinceSum, provinceCountQZ, localArea, s);
                    String messageFallback2QZ = loginService.sendMsm(messageTemQZ, list2.get(0).getMobile());
                    parseMessage(messageFallback2QZ, list2.get(0).getMobile());
                }
            }
        }
        //给总部发送预警短信
        if (alarmSmsDOList != null && alarmSmsDOList.size() > 0) {
            for (AlarmSmsDO alarmSmsDO : alarmSmsDOList) {
                stringRedisTemplate.opsForValue().increment("crmkh_zb" + alarmSmsDO.getBigarea(), alarmSmsDO.getProvinceCount());
            }
            //多少次
            int AreaCount = 0;
            int provinceSum = 0;
            int provinceCountZB = 0;
            int sumZB = 0;
            int areaSum = 0;
            int areaCount = 0;
            String s = "";
            String areaTemplate = "";
            Set<String> crmkh_zb = stringRedisTemplate.keys("crmkh_zb*");
            for (String key : crmkh_zb) {
                AreaCount += Integer.valueOf(stringRedisTemplate.opsForValue().get(key));
                areaTemplate += key.replace("crmkh_zb", "") + ":" + Integer.valueOf(stringRedisTemplate.opsForValue().get(key)) + "次;";
            }

            Map<String, Object> alarmTimeMap = new HashMap<>();
            alarmTimeMap.put("alarmTime", date);
            List<AlarmSmsDO> list1 = alarmSmsService.list(alarmTimeMap);
            for (AlarmSmsDO alarmSmsDO : list1) {
                if (StringUtils.isNotBlank(alarmSmsDO.getProvinceName())) {
                    //多少省
                    provinceSum++;
                    //多少次
                    provinceCountZB += alarmSmsDO.getProvinceCount();
                    sumZB += alarmSmsDO.getProvinceCount();
                    //省计算
                    s += alarmSmsDO.getProvinceName() + ":" + alarmSmsDO.getProvinceCount() + "次;";
                }
            }

            Map<String, Object> stringObjectHashMap = new HashMap<>();
            stringObjectHashMap.put("role", RoleEnum.ZB.getRolename());
            List<LoginUserDO> UserList = loginUserService.list(stringObjectHashMap);
            for (LoginUserDO loginUserDO : UserList) {
                String messageTemZB = String.format(SysConfig.MESSAGE_TEMPLATE_ZB, loginUserDO.getName(), date, keys.size(), AreaCount, provinceSum, provinceCountZB, areaTemplate, s);
                String messageFallback2ZB = loginService.sendMsm(messageTemZB, loginUserDO.getMobile());
                parseMessage(messageFallback2ZB, loginUserDO.getMobile());
            }
        }
    }


    private AlarmSmsDO getUserInfo(AlarmSmsService alarmSmsService, String date, LoginUserDO loginUserDO) {
        Map<String, Object> info = new HashMap<>();
        info.put("userName", loginUserDO.getName());
        info.put("alarmTime", date);

        return CollectionUtils.isEmpty(alarmSmsService.list(info)) ? null : alarmSmsService.list(info).get(0);
    }


    private void parseMessage(String message, String phone) {
        try {
            Document document = DocumentHelper.parseText(message);
            Element root = document.getRootElement();
            List<Element> elements = root.elements();
            for (Iterator<Element> it = elements.iterator(); it.hasNext(); ) {
                Element element = it.next();
                List<Attribute> attributes = element.attributes();
                for (int i = 0; i < attributes.size(); i++) {
                    Attribute attribute = attributes.get(i);
                    if (Constant.SMS_CODE.equals(attribute.getText())) {
                        // 1代表成功
                        log.info("手机号码:[{}],安全校验预警短信发送成功!!", phone);
                    }
                }
                log.error("短信发送失败:" + phone + ",短信平台返回code:" + attributes.get(attributes.size() - 1).getText());
            }
        } catch (DocumentException e) {
            log.error("xml解析异常");
        }
    }

    @Override
    public String whoareyou() {
        return "定时发送预警短信";
    }
}