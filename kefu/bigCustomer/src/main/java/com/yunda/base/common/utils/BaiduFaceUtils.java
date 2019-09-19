/**
 * Copyright © 2019 eSunny Info. Tech Ltd. All rights reserved.
 *
 * @Package: com.yunda.base.common.utils
 * @author: 22374
 * @date: 2019年2月26日 上午11:44:23
 */
package com.yunda.base.common.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.JsonPath;
import com.yunda.base.common.config.Constant;
import com.yunda.base.common.enums.AlarmEnum;
import com.yunda.base.common.enums.RoleEnum;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.dao.LoginUserDao;
import com.yunda.base.system.dao.ProvinceDao;
import com.yunda.base.system.domain.AlarmDO;
import com.yunda.base.system.domain.AlarmSmsDO;
import com.yunda.base.system.domain.LoginUserDO;
import com.yunda.base.system.service.AlarmService;
import com.yunda.base.system.service.AlarmSmsService;
import com.yunda.ydmbspringbootstarter.common.utils.PictureHandlerUtils;
import com.yunda.ydmbspringbootstarter.common.utils.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.MediaType;

/**
 * @ClassName: BaiduFaceUtils
 * @Description: TODO
 * @author: 22374
 * @date: 2019年2月26日 上午11:44:23
 */
public class BaiduFaceUtils {
    private static Logger log = LoggerFactory.getLogger(BaiduFaceUtils.class);

    public static final MediaType JSONTYPE = MediaType.parse("application/json; charset=utf-8");

    /**
     * @param imStr1
     * @param imgStr2
     * @return boolean
     * @Title: getResult
     * @Description: 人脸对比
     * @author 22374
     * @date 2019年2月25日下午7:37:27
     */
    public static boolean getResult(String imStr1, String imgStr2) {

        String accessToken = GetTon.getToken();
        boolean flag = false;
        String genrearlURL = SysConfig.FACE_COMPARE + "?access_token=" + accessToken;

        List<Map<String, Object>> images = new ArrayList<>();

        Map<String, Object> map1 = new HashMap<>();
        map1.put("image", imStr1);
        map1.put("image_type", "BASE64");
        map1.put("face_type", "LIVE");
        map1.put("quality_control", "LOW");
        map1.put("liveness_control", "NORMAL");

        Map<String, Object> map2 = new HashMap<>();
        map2.put("image", imgStr2);
        map2.put("image_type", "BASE64");
        map2.put("face_type", "LIVE");
        map2.put("quality_control", "LOW");
        map2.put("liveness_control", "NORMAL");
        images.add(map1);
        images.add(map2);

        try {
            JSONObject res = HttpUtil.doPost(genrearlURL, JSONArray.fromObject(images));
            JSONObject jsonObject = res.getJSONObject("result");
            if (!ObjectUtils.isEmpty(jsonObject)) {
                String score = jsonObject.get("score").toString();
                if (Double.valueOf(score) >= 80) {
                    log.info("百度AI人脸对比得分:[{}]", score);
                    flag = true;
                } else {
                    log.info("百度AI人脸对比失败得分:[{}]", score);
                }
            }
        } catch (Exception e) {
            log.error("百度AI人脸对比失败:[{}]", e.getMessage());
        }
        return flag;
    }

    /**
     * @param imgStr
     * @param request
     * @return boolean
     * @throws Exception
     * @Title: face_search
     * @Description: 人脸搜索
     * @author 22374
     * @date 2019年2月25日下午5:36:54
     */
    public static boolean face_search(HttpServletRequest request, String imgStr) throws Exception {
        StringRedisTemplate redisTemplate = SpringUtil.getBean("stringRedisTemplate", StringRedisTemplate.class);
        AlarmSmsService alarmSmsService = SpringUtil.getBean(AlarmSmsService.class);
        AlarmService alarmService = SpringUtil.getBean(AlarmService.class);
        ProvinceDao provinceDao = SpringUtil.getBean(ProvinceDao.class);
        LoginUserDao loginUserDao = SpringUtil.getBean(LoginUserDao.class);

        String accessToken = GetTon.getToken();
        String session_id = request.getSession().getId();
        String username = ShiroUtils.getUser().getUsername();
        // 非本人
        String unself = session_id + "_unself";
        // 除本人以外其他人
        String allself = session_id + "_allself";
        // 无人
        String noperson = session_id + "_noperson";

        boolean flag = false;
        int count_unself = 0;
        int face_num = 0;

        Map<String, Object> map = new HashMap<>();
        map.put("image", imgStr);
        map.put("image_type", "BASE64");
        map.put("group_id_list", "yunda");
        map.put("quality_control", "LOW");
        map.put("liveness_control", "NORMAL");
        map.put("user_id", username);
        map.put("max_user_num", 15);

        // 记录预警信息
        /*AlarmSmsDO alarmSmsDO = new AlarmSmsDO();
        alarmSmsDO.setUserName(ShiroUtils.getUser().getUsername());
        alarmSmsDO.setAlarmTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));*/

        try {
            log.info("百度AI人脸搜索参数[{}]", JSON.toJSONString(map));
            String result = HttpUtil.post(SysConfig.FACE_SEARCH, accessToken, "application/json", JSON.toJSONString(map));
            log.info("百度AI人脸搜索返回结果:[{}]", result);

            String res_detect = face_detect(imgStr);
            int errorCode = JsonPath.read(res_detect, "$.error_code");

            if (errorCode != Constant.BAIDU_NOPERSONCODE) {
                face_num = JsonPath.read(res_detect, "$.result.face_num");

                List<Double> scores = JsonPath.read(result, "$.result..score");
                int error_code = JsonPath.read(result, "$.error_code");

                // 逻辑验证
                if (error_code == Constant.BAIDU_ERRORCODE) {
                    // 搜索成功
                    // 用于计算是否连续
                    for (Double score : scores) {
                        // 过滤出照片中所有人脸和对照人脸的比分
                        if (score < 80f) {
                            // 非本人
                            count_unself++;
                        }
                    }
                    if (face_num > 1 && count_unself == 0) {
                        // 有出现除本人以外的其他人
                        redisTemplate.opsForValue().increment(allself, 1);
                        redisTemplate.opsForValue().set(noperson, "0");
                        redisTemplate.opsForValue().set(unself, "0");
                    }
                    if (count_unself == 1 && face_num == 1) {
                        // 非本人
                        redisTemplate.opsForValue().increment(unself, 1);
                        redisTemplate.opsForValue().set(noperson, "0");
                        redisTemplate.opsForValue().set(allself, "0");
                    }
                    if (count_unself == 0 && face_num == 1) {
                        //本人
                        redisTemplate.opsForValue().set(unself, "0");
                        redisTemplate.opsForValue().set(noperson, "0");
                        redisTemplate.opsForValue().set(allself, "0");
                    }


                    // 视频中连续【3】次请求均有出现除本人以外的其他人，则该用户的飞鸟系统强制退出
                    if (StringUtils.equals(redisTemplate.opsForValue().get(allself), SysConfig.UNSELFANDSELF)) {
                        flag = true;

                        // 记录报警信息
                        RecordSmsInfo(alarmSmsService, loginUserDao, provinceDao);
                        PictureHandlerUtils.generateImage(imgStr, SysConfig.WarningPictureAddress + request.getSession().getId() + ShiroUtils.getUser().getUsername() + ".jpg");
                        AlarmDO alarmDO = new AlarmDO();
                        alarmDO.setUserName(ShiroUtils.getUser().getUsername());
                        alarmDO.setSessionId(session_id);
                        alarmDO.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
                        alarmDO.setAlarmType(AlarmEnum.allself.getName());
                        alarmDO.setImgAddress(SysConfig.WarningPictureAddress + request.getSession().getId() + ShiroUtils.getUser().getUsername() + ".jpg");
                        alarmService.save(alarmDO);

                    }
                    // 如果视频中连续【2】次请求非本人，则该用户的飞鸟系统强制退出
                    if (StringUtils.equals(redisTemplate.opsForValue().get(unself), SysConfig.UNSELF)) {
                        flag = true;

                        // 记录报警信息
                        RecordSmsInfo(alarmSmsService, loginUserDao, provinceDao);
                        PictureHandlerUtils.generateImage(imgStr, SysConfig.WarningPictureAddress + request.getSession().getId() + ShiroUtils.getUser().getUsername() + ".jpg");
                        AlarmDO alarmDO = new AlarmDO();
                        alarmDO.setUserName(ShiroUtils.getUser().getUsername());
                        alarmDO.setSessionId(session_id);
                        alarmDO.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
                        alarmDO.setAlarmType(AlarmEnum.unself.getName());
                        alarmDO.setImgAddress(SysConfig.WarningPictureAddress + request.getSession().getId() + ShiroUtils.getUser().getUsername() + ".jpg");
                        alarmService.save(alarmDO);
                    }
                } else {
                    redisTemplate.opsForValue().increment(unself, 1);
                    redisTemplate.opsForValue().set(noperson, "0");
                    redisTemplate.opsForValue().set(allself, "0");
                    log.error("当前用户:[{}],错误码是[{}],其他原因导致非本人", username, error_code);
                    // 如果视频中连续【2】次请求非本人，则该用户的飞鸟系统强制退出
                    if (StringUtils.equals(redisTemplate.opsForValue().get(unself), SysConfig.UNSELF)) {
                        flag = true;
                        PictureHandlerUtils.generateImage(imgStr, SysConfig.WarningPictureAddress + request.getSession().getId() + ShiroUtils.getUser().getUsername() + ".jpg");
                        // 记录报警信息
                        RecordSmsInfo(alarmSmsService, loginUserDao, provinceDao);

                        AlarmDO alarmDO = new AlarmDO();
                        alarmDO.setUserName(ShiroUtils.getUser().getUsername());
                        alarmDO.setSessionId(session_id);
                        alarmDO.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
                        alarmDO.setAlarmType(AlarmEnum.allself.getName());
                        alarmDO.setImgAddress(SysConfig.WarningPictureAddress + request.getSession().getId() + ShiroUtils.getUser().getUsername() + ".jpg");
                        alarmService.save(alarmDO);
                    }
                }
            } else {
                // 无人
                redisTemplate.opsForValue().increment(noperson, 1);
                redisTemplate.opsForValue().set(unself, "0");
                redisTemplate.opsForValue().set(allself, "0");

                if (StringUtils.equals(redisTemplate.opsForValue().get(noperson), SysConfig.NOPERSON)) {
                    // 如果连续【5】次视频中无人，该用户的飞鸟系统强制退出。
                    flag = true;
//                    PictureHandlerUtils.generateImage(imgStr, ResourceUtils.getURL("classpath:").getPath()+"static/img/" + request.getSession().getId() + ShiroUtils.getUser().getUsername() + ".jpg");
                    PictureHandlerUtils.generateImage(imgStr, SysConfig.WarningPictureAddress + request.getSession().getId() + ShiroUtils.getUser().getUsername() + ".jpg");

                    AlarmDO alarmDO = new AlarmDO();
                    alarmDO.setUserName(ShiroUtils.getUser().getUsername());
                    alarmDO.setSessionId(session_id);
                    alarmDO.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
                    alarmDO.setAlarmType(AlarmEnum.noperson.getName());
                    alarmDO.setImgAddress(SysConfig.WarningPictureAddress + request.getSession().getId() + ShiroUtils.getUser().getUsername() + ".jpg");
                    alarmService.save(alarmDO);
                }
            }
        } catch (IOException e) {
            log.error("百度AI人脸搜索异常:[{}]", e.getMessage());
        }

        log.info("当前用户:[{}],非本人特征值是:[{}]", username, redisTemplate.opsForValue().get(unself));
        log.info("当前用户:[{}],本人以及他人特征值是:[{}]", username, redisTemplate.opsForValue().get(allself));
        log.info("当前用户:[{}],无人特征值是:[{}]", username, redisTemplate.opsForValue().get(noperson));

        // 存一天失效
        redisTemplate.expire(unself, 1, TimeUnit.DAYS);
        redisTemplate.expire(allself, 1, TimeUnit.DAYS);
        redisTemplate.expire(noperson, 1, TimeUnit.DAYS);

        return flag;
    }


    /**
     * 功能描述: <br>
     * 〈记录报警信息〉
     *
     * @Author:22374
     * @Date: 2019/3/22 15:17
     * @Param: [alarmSmsService, userProvinceDao, alarmSmsDO]
     * @return:void
     * @since: 1.0.0
     */

    private static void RecordSmsInfo(AlarmSmsService alarmSmsService, LoginUserDao loginUserDao, ProvinceDao provinceDao) {
        String username = ShiroUtils.getUser().getUsername();
        Map<String, Object> map = new HashMap<>(16);
        map.put("username", username);
        LoginUserDO loginUserDO = loginUserDao.list(map).get(0);
        String timeFormatter = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        if (StringUtils.equals(loginUserDO.getRole(), RoleEnum.SZ.getRolename())) {
            if (StringUtils.isNotBlank(loginUserDO.getInstitution())) {
                Map<String, Object> provinceMap = new HashMap<>(16);
                provinceMap.put("provincename", loginUserDO.getInstitution());
                String bigArea = provinceDao.list(provinceMap).get(0).getBigarea();

                AlarmSmsDO alarmSmsDO = new AlarmSmsDO();

                alarmSmsDO.setProvinceName(loginUserDO.getInstitution());
                alarmSmsDO.setBigarea(bigArea);
                alarmSmsDO.setUserName(ShiroUtils.getUser().getName());
                alarmSmsDO.setAlarmTime(timeFormatter);

                Map<String, Object> stringObjectHashMap = new HashMap<>();
                stringObjectHashMap.put("userName", ShiroUtils.getUser().getName());
                stringObjectHashMap.put("alarmTime", timeFormatter);
                stringObjectHashMap.put("provinceName", loginUserDO.getInstitution());
                stringObjectHashMap.put("bigarea", bigArea);

                if (ObjectUtils.isEmpty(alarmSmsService.list(stringObjectHashMap))) {
                    alarmSmsDO.setProvinceCount(1);
                }
                alarmSmsService.saveOrUpdate(alarmSmsDO);
            }
        }

        //区总协助人或者区总触发的预警
        if (StringUtils.equalsAny(loginUserDO.getRole(), RoleEnum.QZXZR.getRolename(), RoleEnum.QZ.getRolename())) {

            AlarmSmsDO alarmSmsDO = new AlarmSmsDO();
            alarmSmsDO.setUserName(ShiroUtils.getUser().getName());
            alarmSmsDO.setAlarmTime(timeFormatter);
            alarmSmsDO.setProvinceName("");
            alarmSmsDO.setBigarea(loginUserDO.getInstitution());

            Map<String, Object> stringObjectHashMap = new HashMap<>();
            stringObjectHashMap.put("userName", ShiroUtils.getUser().getName());
            stringObjectHashMap.put("alarmTime", timeFormatter);

            if (ObjectUtils.isEmpty(alarmSmsService.list(stringObjectHashMap))) {
                alarmSmsDO.setProvinceCount(1);
            }
            alarmSmsService.saveOrUpdate(alarmSmsDO);
        }
    }

    /**
     * @param name
     * @param username
     * @return String
     * @Title: face_add
     * @Description: 人脸添加到百度人脸库中
     * @author 22374
     * @date 2019年2月26日上午11:51:34
     */
    public static void face_add(String face_image, String username, String name) {

        String accessToken = GetTon.getToken();

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("image", face_image);
            map.put("group_id", "yunda");
            map.put("user_id", username);
            map.put("user_info", name);
            map.put("liveness_control", "NORMAL");
            map.put("image_type", "BASE64");
            map.put("quality_control", "LOW");

            log.info("百度AI人脸添加参数[{}]", JSON.toJSONString(map));
            String result = HttpUtil.post(SysConfig.FACE_ADD, accessToken, "application/json", JSON.toJSONString(map));
            log.info("百度AI人脸添加返回结果:[{}]", result);
        } catch (Exception e) {
            log.error("百度AI人脸添加异常:[{}]", e.getMessage());
        }
    }

    private static String face_detect(String face_image) {
        String accessToken = GetTon.getToken();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("image", face_image);
            map.put("image_type", "BASE64");
            map.put("max_face_num", 5);
            String result = HttpUtil.post(SysConfig.FACE_DETECT, accessToken, "application/json", JSON.toJSONString(map));
            log.info("百度AI人脸检测返回结果:[{}]", result);
            return result;
        } catch (Exception e) {
            log.error("百度人脸检测出现异常[{}]", e.getMessage());
        }
        return null;
    }
}