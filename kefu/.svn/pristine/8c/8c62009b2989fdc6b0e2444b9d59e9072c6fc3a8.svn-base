package com.yunda.base.system.service.impl;

import com.yunda.base.common.config.Constant;
import com.yunda.base.common.enums.RespEnum;
import com.yunda.base.common.utils.HttpPostUtil;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.dao.UserDao;
import com.yunda.base.system.service.AlarmService;
import com.yunda.base.system.service.LoginService;
import com.yunda.base.system.vo.RspBean;
import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    AlarmService alarmService;
    @Autowired
    UserDao userDao;

    @Override
    public RspBean smsLogin(String phone) throws DocumentException {
        int nextInt = (int) (Math.random() * 9000) + 1000; // 生成4位随机数,发送短信
        String smsContent = "本次登录飞鸟系统验证码是:" + nextInt;
        String sendMsm = sendMsm(smsContent, phone);
        Document document = DocumentHelper.parseText(sendMsm);
        Element root = document.getRootElement();
        List<Element> elements = root.elements();
        for (Iterator<Element> it = elements.iterator(); it.hasNext(); ) {
            Element element = it.next();
            List<Attribute> attributes = element.attributes();
            for (int i = 0; i < attributes.size(); i++) {
                Attribute attribute = attributes.get(i);
                if (Constant.SMS_CODE.equals(attribute.getText())) {
                    // 1代表成功
                    logger.info("手机号码:" + phone + "验证码是:" + smsContent);
                    stringRedisTemplate.opsForValue().set(phone, String.valueOf(nextInt), 180, TimeUnit.SECONDS);
                    return new RspBean<>().success();
                }
            }
            logger.error("短信发送失败:" + phone + ",短信平台返回code:" + attributes.get(attributes.size() - 1).getText());
        }
        return new RspBean<>().failure(RespEnum.SMS_FAIL);
    }


    // 发送短信
    @Override
    public String sendMsm(String message, Object phone_no) {
        Long item = (long) (Math.random() * 9 * Math.pow(10, 15)) + (long) Math.pow(10, 15);
        StringBuffer xmldata = new StringBuffer();
        xmldata.append("<req op=\"sms_01send\"><h><ver>" + "1.0" + "</ver>" + "<user>" + SysConfig.SMS_ADMIN + "</user>" + "<pass>" + SysConfig.SMS_PASS + "</pass></h>");
        xmldata.append("<items><item> <id>").append(item).append("</id>").append("<content>").append(message).append("</content>").append("<tele>").append(phone_no).append("</tele></item></items></req>");

        HttpPostUtil postutil = new HttpPostUtil();
        String post = postutil.post(SysConfig.SMS_URL, xmldata.toString());
        logger.info("短信平台发送状态:" + post);
        return post;
    }



    @Override
    public List<String> getRoleIds(String userId) {
        return userDao.getRoleIds(userId);
    }
}
