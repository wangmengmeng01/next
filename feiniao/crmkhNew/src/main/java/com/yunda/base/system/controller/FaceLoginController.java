package com.yunda.base.system.controller;

import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.enums.ResourceSafeEnum;
import com.yunda.base.common.enums.RespEnum;
import com.yunda.base.common.utils.BaiduFaceUtils;
import com.yunda.base.common.utils.ShiroUtils;
import com.yunda.base.feiniao.report.core.CRM_constants;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.LoginUserDO;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.domain.VideoDownloadDO;
import com.yunda.base.system.service.LoginService;
import com.yunda.base.system.service.LoginUserService;
import com.yunda.base.system.service.ResourceSafeConfigService;
import com.yunda.base.system.service.VideoDownloadService;
import com.yunda.base.system.vo.RspBean;
import com.yunda.base.system.vo.ValidParam;
import com.yunda.ydmbspringbootstarter.common.utils.PictureHandlerUtils;
import com.yunda.ydmbspringbootstarter.common.utils.TuoMinUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述: <br>
 * 〈人脸识别〉
 *
 * @Author:22374
 * @Date: 2019/3/22 15:07
 * @Param:
 * @return:
 * @since: 1.0.0
 */

@Controller
@RequestMapping("/system")
public class FaceLoginController extends BaseController {
    Logger log = LoggerFactory.getLogger(FaceLoginController.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private LoginUserService loginUserService;
    @Autowired
    private VideoDownloadService videoDownloadService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private ResourceSafeConfigService resourceSafeConfigService;

    /**
     * 功能描述: <br>
     * 〈人脸识别登录方法〉
     *
     * @Author:22374
     * @Date: 2019/3/5 15:52
     * @Param: [base, username]
     * @return:com.yunda.base.system.vo.RspBean<T>
     * @since: 1.0.0
     */
    @RequestMapping("/facelogin")
    @ResponseBody
    public <T> RspBean<T> facelogin(String base, String username) {
        try {
            // 图片信息入库
            File file = new File(SysConfig.PICTIRE_Address);
            if (!file.exists() && !file.isDirectory()) {
                file.mkdirs();
            }
            String face_address = loginUserService.getFaceimageByUsername(username);
            LoginUserDO loginUserDO = loginUserService.getUserDOByName(username);
            if (!ObjectUtils.isEmpty(loginUserDO)) {
                String face_image = SysConfig.PICTIRE_Address + username + "(" + loginUserDO.getName() + ")" + ".jpg";
                if (StringUtils.isBlank(face_address)) {
                    PictureHandlerUtils.generateImage(base, face_image);
                    Thread.sleep(1);
                    String base64 = PictureHandlerUtils.getImageStr(face_image);
                    boolean result = BaiduFaceUtils.getResult(base, base64);
                    if (result) {
                        // 成功登陆，就将用户的照片路径记录下来
                        loginUserDO.setFaceImage(face_image);
                        loginUserService.update(loginUserDO);
                        // 将用户照片存进百度AI的人脸库中
                        BaiduFaceUtils.face_add(base, loginUserDO.getUsername(), loginUserDO.getName());

                        return new RspBean<T>().success();
                    } else {
                        // 登录失败,删除生成的照片
                        File face_file = new File(face_image);
                        if (face_file.exists() && face_file.isFile()) {
                            face_file.delete();
                        }
                        return new RspBean<T>().failure(RespEnum.FACE_FAIL);
                    }
                } else {
                    String face_base64 = PictureHandlerUtils.getImageStr(face_address);
                    boolean result = BaiduFaceUtils.getResult(base, face_base64);
                    if (result) {
                        return new RspBean<T>().success();
                    } else {
                        return new RspBean<T>().failure(RespEnum.FACE_FAIL);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new RspBean<T>().failure(RespEnum.FACE_FAIL);
    }

    /**
     * @param request
     * @param response
     * @param validParam
     * @return RspBean<T>
     * @Title: validLogin
     * @Description: 安全登录验证
     * @author 22374
     * @date 2019年2月26日上午11:50:38
     */
    @RequestMapping("/validLogin")
    @ResponseBody
    public <T> RspBean<T> validLogin(HttpServletRequest request, HttpServletResponse response, @ModelAttribute @Validated ValidParam validParam) {
        // 验证信息初始化
        String id_card = validParam.getId_card();
        String message_valid = validParam.getMessage_valid();
        String username = validParam.getUsername();
        String base = validParam.getBase();
        String mac1 = validParam.getMac();
        // 用户正确身份信息
        String userName = ShiroUtils.getUser().getUsername();
        LoginUserDO loginUserDO = loginUserService.getUserDOByName(username);
        String phone = loginUserDO.getMobile();
        // 开始验证
        try {
            // 1级Mac地址验证
            if (StringUtils.isNotBlank(mac1)) {
            	//客户端获取到的mac地址是00:50:56:c0:00:01格式  数据库存的是"-"格式  将mac1转成mac
            	String mac = mac1.replaceAll(":", "-");
            	if (!StringUtils.equalsAnyIgnoreCase(mac, loginUserDO.getMacAdress(), loginUserDO.getWirelessMacAdress())) {
                    log.error("用户名:[{}],Mac地址[{}],校验异常", userName, mac);
                    return new RspBean<T>().failure(RespEnum.MAC_FAIL);
                }
            }
            // 2级输入身份证号码校验
            if (StringUtils.isNotBlank(id_card)) {
                if (!StringUtils.equals(id_card, loginUserDO.getIdcdNo())) {
                    log.error("用户名:[{}],身份证:[{}],校验异常", userName, id_card);
                    return new RspBean<T>().failure(RespEnum.IDCARD_FAIL);
                }
            }
            // 3级短信验证码验证
            String value = stringRedisTemplate.opsForValue().get(phone);
            if (StringUtils.isNotBlank(message_valid)) {
                if (!StringUtils.equals(value, message_valid)) {
                    // 若不成功就加速缓存的时效，避免被暴力尝试
                    long v = stringRedisTemplate.boundValueOps(phone).getExpire();
                    if (v > 0) {
                        System.out.println("得到==" + v);
                        // 加速过期
                        v = v / 2;
                        stringRedisTemplate.boundValueOps(phone).expire(v, TimeUnit.SECONDS);
                    }
                    log.error("用户名:[{}],手机号码:[{}],短信验证码:[{}],校验异常", userName, phone, message_valid);
                    return new RspBean<T>().failure(RespEnum.SMS_VALID_FAIL);
                }
            }

            // 4级人脸识别验证
            if (StringUtils.isNotBlank(base)) {
                RspBean<Object> resp = this.facelogin(base, userName);
                if (resp.getCode() != RespEnum.SUCCESS.getCode()) {
                    log.error("用户名:[{}],人脸识别校验异常", userName);
                    return new RspBean<T>().failure(RespEnum.FACE_FAIL);
                }
            }

            // 5级校验全程录像,本类中recordVideo方法

            // 用户通过了安全验证，将用户提升安全等级
            log.info("用户名:[{}],安全验证通过", userName);
            String targetLevel = stringRedisTemplate.opsForValue().get(request.getSession().getId() + "_targetlevel");
            if (StringUtils.isNotBlank(targetLevel)) {
                UserDO userdo = (UserDO) request.getSession().getAttribute(CRM_constants.AUTH_USER);
                userdo.setSafeLevel(targetLevel);
                request.getSession().setAttribute(CRM_constants.AUTH_USER, userdo);
            }

            // 若登录成功通过就删除手机验证码缓存
            stringRedisTemplate.delete(phone);

            return new RspBean<T>().success();
        } catch (Exception e) {
            log.error(e.getMessage());
            return new RspBean<T>().failure(RespEnum.ERROR_BUSINESS_OPERATE);
        }
    }

    /**
     * @param request
     * @param response void
     * @Title: heartCheck
     * @Description: 等级高页面心跳检测
     * @author 22374
     * @date 2019年2月25日下午3:28:00
     */
    @RequestMapping("/heartCheck")
    @ResponseBody
    public RspBean heartCheck(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = request.getSession().getId();
        String safe_level = stringRedisTemplate.opsForValue().get(sessionId + "_targetlevel");

        long timecurrrent = System.currentTimeMillis();
        log.info("当前sessonid:[{}],更新的时间戳:[{}]", sessionId, timecurrrent);

        stringRedisTemplate.opsForValue().set(sessionId, String.valueOf(timecurrrent), 1, TimeUnit.DAYS);
        return new RspBean().success();
    }

    /**
     * @param request
     * @param response void
     * @throws Exception
     * @Title: warningSMS
     * @Description: 发送预警短信 ,2. 每隔【30】秒请求一次人脸识别，如果连续【5】次视频中无人，该用户的飞鸟系统强制退出。
     * 3. 每隔【30】秒请求一次人脸识别，如果视频中连续【2】次请求非本人，或视频中连续【3】次请求均有出现除本人以外的其他人，
     * 则该用户的飞鸟系统强制退出，并且发送预警短信发到大区区总、总裁、副董事长和监察部【短信模版及以上人员信息待业务确定】
     * @author 22374
     * @date 2019年2月25日下午4:27:12
     */
    @RequestMapping("/warningSMS")
    @ResponseBody
    public RspBean warningSMS(HttpServletRequest request, HttpServletResponse response, @RequestParam("base") String base) throws Exception {
        UserDO userdo = (UserDO) request.getSession().getAttribute(CRM_constants.AUTH_USER);
        String safe_level = userdo.getSafeLevel();
        //只有当用户资源等级是5的时候才进行发送短信的操作
        if (StringUtils.equals(safe_level, ResourceSafeEnum.VIDEO.getNum())) {
            if (BaiduFaceUtils.face_search(request, base)) {
                request.getSession().invalidate();
                ShiroUtils.logout();
                log.warn("触发退出系统机制...................");
                return new RspBean(RespEnum.ALREADY_EXIT);
            }
        } else {
            log.info("用户名:[{}],访问的页面资源安全等级较低,无需验证!!", userdo.getUsername());
        }
        return new RspBean().failure(RespEnum.NO_NEED_MESSAGE);
    }

    /**
     * @param request
     * @param response
     * @return RspBean
     * @Title: collectReport
     * @Description: 首页收集每个高等级页面心跳, 符合条件才进行录制
     * @author 22374
     * @date 2019年2月25日下午3:28:24
     */
    @RequestMapping("/collectReport")
    @ResponseBody
    public RspBean collectReport(HttpServletRequest request, HttpServletResponse response) {
        String time = stringRedisTemplate.opsForValue().get(request.getSession().getId());
        UserDO userDo = (UserDO) request.getSession().getAttribute(CRM_constants.AUTH_USER);
        String user_level = userDo.getSafeLevel();
        String num = ResourceSafeEnum.VIDEO.getNum();// 最高等级
        long timecurrrent = System.currentTimeMillis();
        if (StringUtils.isNotBlank(time)) {
            // 超出时间 ,说明页面关闭了,停止摄像
            log.info("redis最新时间和当前发送心跳时间间隔是:[{}]", (timecurrrent - Long.valueOf(time)) / 1000 % 60);
            if (StringUtils.isBlank(user_level)) {
                user_level = ResourceSafeEnum.TOGE_AUTH.getNum();
            }
            log.info("当前用户:[{}],等级是:[{}]:", userDo.getUsername(), user_level);
            // 只有当用户等级是最高级别,才进行录制
            if (StringUtils.equals(user_level, num)) {
                if (stopRecord(time, userDo, timecurrrent)) {
                    return new RspBean<>(RespEnum.STOP_RECORD);
                }
                // 正式开始拍摄
                log.info("用户名:[{}],当前用户等级:[{}],保持摄制视频", userDo.getUsername(), user_level);
                return new RspBean<>(RespEnum.KEEP_RECORD);
            }
            if (stopRecord(time, userDo, timecurrrent)) {
                return new RspBean<>(RespEnum.STOP_RECORD);
            }
        }
        return null;
    }

    /**
     * 功能描述: <br>
     * 〈停止录像功能〉
     *
     * @Author:22374
     * @Date: 2019/3/6 18:29
     * @Param: [time, userDo, timecurrrent]
     * @return:boolean
     * @since: 1.0.0
     */
    private boolean stopRecord(String time, UserDO userDo, long timecurrrent) {
        if ((timecurrrent - Long.valueOf(time)) / 1000 % 60 > Long.valueOf(SysConfig.intervalTime)) {
            log.info("心跳检测超出时间间隔:[{}],停止录制视频", (timecurrrent - Long.valueOf(time)) / 1000 % 60 - Long.valueOf(SysConfig.intervalTime));
            return true;
        }
        return false;
    }

    /**
     * @param request
     * @param formData
     * @return
     * @throws IllegalStateException
     * @throws IOException           RspBean<T>
     * @Title: recordVideo
     * @Description: 视频录制, 保存到服务器
     * @author 22374
     * @date 2019年2月26日上午11:50:14
     */
    @PostMapping("/recordVideo")
    @ResponseBody
    public <T> RspBean<T> recordVideo(HttpServletRequest request, @RequestParam(required = false, value = "video-blob") MultipartFile formData) throws IllegalStateException, IOException {
        String dateformat = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String username = getUsername(request);
        String hourformat = new SimpleDateFormat("HHmmssSSS").format(new Date());
        String operateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // 生成规则:日期 用户名 操作时间
        String DirPath = SysConfig.VIDEOFILEPATH + dateformat + File.separator + username + File.separator + hourformat;
        File file = new File(DirPath);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        log.info("生成视频中.............");

        String uuid = UUID.randomUUID().toString().replace("-", "");
        int randomNum = new Random().nextInt(999);
        String fileType = formData.getOriginalFilename().substring(formData.getOriginalFilename().lastIndexOf("."));
        String realFileName = uuid + randomNum + fileType;
        String localPath = DirPath + File.separator + realFileName;
        formData.transferTo(new File(localPath));

        VideoDownloadDO videoDownloadDO = new VideoDownloadDO();
        videoDownloadDO.setUsername(username);
        videoDownloadDO.setDate(operateformat);
        videoDownloadDO.setFilePath(localPath);
        videoDownloadService.save(videoDownloadDO);
        return null;
    }

    /**
     * @param request
     * @param response
     * @param model
     * @return String
     * @Title: validate
     * @Description: 安全登录页面参数获取
     * @author 22374
     * @date 2019年3月5日上午10:47:09
     */
    @RequestMapping("/validate")
    String validate(HttpServletRequest request, HttpServletResponse response, Model model) {
        String userlevel = request.getParameter("userlevel");
        // 从数据库中查询用户手机号显示到前端页面(只读),并脱敏
        String userName = ShiroUtils.getUser().getUsername();
        String userMobile = loginUserService.getUserByName(userName).getMobile();

        if (StringUtils.isNotBlank(userMobile)) {
            // 手机号脱敏
            userMobile = TuoMinUtil.TuoMinMobile(userMobile);
        }
        model.addAttribute("safelevel", userlevel);
        model.addAttribute("mobile", userMobile);

        log.info("用户名:[{}],等级是:[{}],手机号码:[{}]", userName, userlevel, userMobile);
        return "facevalidate";
    }

    /**
     * 功能描述: <br>
     * 〈查看是否领导人,校验浏览器型号〉
     *
     * @Author:22374
     * @Date: 2019/3/6 13:04
     * @Param: []
     * @return:com.yunda.base.system.vo.RspBean
     * @since: 1.0.0
     */
    @GetMapping("/leader")
    @ResponseBody
    public RspBean isLeader() {
        Long userId = ShiroUtils.getUser().getUserId();
        List<String> roleIds = loginService.getRoleIds(userId.toString());
        if (StringUtils.isNotBlank(SysConfig.LEADERROLEID)) {
            List<String> leaderRoleIds = Arrays.asList(SysConfig.LEADERROLEID.split(","));
            for (String roleId : roleIds) {
                if (leaderRoleIds.contains(roleId)) {
                    return new RspBean().success();
                }
            }
        }
        return new RspBean().failure(RespEnum.ERROR_BUSINESS_OPERATE);
    }
}
