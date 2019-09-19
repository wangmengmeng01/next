package com.yunda.base.system.config;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

//配置参数从数据库中，动态注入静态变量，方便非spring托管的普通类使用常量
//不采用配置文件提供配置是为了避免修改配置还需要重启服务，采用数据库取值可通过定时任务热更新
//数据库config表中需要存在同名的参数，数值会被自动注入对应的静态值
public class SysConfig<stat> {
    // 上传路径
    //public static String uploadPath = "D:/temp/";
    public static String uploadPath = "/u02/bigcustomer/shareData/uploadPath/";

    // 图片主机
    public static String uploadLocal = "/vipkf/files";

    // 下载文件临时文件目录
    public static String tempDownload = "D:/temp/";

    // 模版文件
    public static String templateExcel = "D:/template/";

    public static String USER_TEMPLATE = "false";

    // 导出
    public static String DAOCHU = "true";
    public static String TARGET = "D:/u02/webapps/crmkhnew/resource/";
    public static String TEMPLATE = "D:/u02/webapps/crmkhnew/template/";

    // 是否启用统一授权登录本系统 false表示使用本系统的账号密码登录
    public static String TYSQFLAG = "true";
    public static String urlNameStringLogin = "http://uat.ydauth.yundasys.com:16120/ydauth/actions/outer/user/login.action";
    public static String urlNameStringRole = "http://uat.ydauth.yundasys.com:16120/ydauth/actions/outer/user/queryUserInfoAndRole.action";
    public static String authId = "100000229";
    // refere跨站点请求伪造,该值不要放默认值，让数据库提供
    public static String configFilter = "";
    public static Set<String> configFilters = new HashSet<String>();

    // 抽数步长
    public static String suckLimit = "10000";
    public static int suckLimitInt = Integer.valueOf(suckLimit);
    // 按照步长获取数据
    public static String exportExcelLimit = "50000";
    // 按照步长截取数据
    public static String splitLimit = "50000";
    // 创建放置Excel文件位置
    public static String exportExcelPath = "D:/Excel/";
    // 创建放置Zip文件位置
    public static String exportZIPPath = "D:/ExcelZip/";

    // 开启任务互斥判断
    public static String openTaskCheck = "close";

    // 是否启用报表线程池机制
    public static String report_thread_flag = "open";
    // 处理thread_nums_fluctuate报表的线程数
    public static String thread_nums_fluctuate = "1";
    // 处理custReward报表的线程数
    public static String thread_nums_custReward = "1";
    // 处理totaldata报表的线程数
    public static String thread_nums_totaldata = "1";
    // 处理warning报表的线程数
    public static String thread_nums_warning = "1";
    // 处理非指定报表的线程数
    public static String thread_nums_common = "1";

    // 发送短信接口地址
    public static String SMS_URL = "http://10.19.18.50:10214/smsinterface/sendInterface/sendSms_xml.do";

    // 用于检测心跳检测报告中心最新时间和现在时间的相差值
    public static String intervalTime = "10";

    // 短信登录账号
    public static String SMS_ADMIN = "cbs1_member_valid";
    // 短信登录密码
    public static String SMS_PASS = "FOXTgS4blHzgWsyX";
    // 配置需要安全登录的角色
    public static String USER_ROLE = "1,2";
    // 上传视频文件路径
    public static String VIDEOFILEPATH = "E:" + File.separator + "u02" + File.separator + "video" + File.separator;
    // 生成图片的地址
    public static String PICTIRE_Address = "E:" + File.separator + "u02" + File.separator + "face_image" + File.separator;
    // 百度人脸库人脸添加接口
    public static String FACE_ADD = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add";
    // 百度人脸搜索地址
    public static String FACE_SEARCH = "https://aip.baidubce.com/rest/2.0/face/v3/search";
    // 百度人脸对比地址
    public static String FACE_COMPARE = "https://aip.baidubce.com/rest/2.0/face/v3/match";
    // 百度人脸检测接口地址
    public static String FACE_DETECT = "https://aip.baidubce.com/rest/2.0/face/v3/detect";
    // 本人以及其他人出现次数
    public static String UNSELFANDSELF = "3";
    // 非本人
    public static String UNSELF = "2";
    // 无人次数
    public static String NOPERSON = "5";
    // 领导角色编号,用于判断是否弹出谷歌浏览器警告标识
    public static String LEADERROLEID = "1";
    //省总短信模板
    public static String MESSAGE_TEMPLATE_SZ = "重点关注：省总:%s,您好，%s（前一日）飞鸟系统登陆时因视频检测非本人面孔出现产生预警，并关闭系统，产生预警：%s次，请及时了解预警原因，加强登陆安全管理，具体详情请登陆“飞鸟系统”！";
    //区总协助人短信模板
    public static String MESSAGE_TEMPLATE_QZXZR = "重点关注：区总协助人:%s,您好，%S（前一日）飞鸟系统登陆时因视频检测非本人面孔出现产生预警，并关闭系统，产生预警：%s次，请及时了解预警原因，加强登陆安全管理，具体详情请登陆“飞鸟系统”！";
    //区总短信模板
    public static String MESSAGE_TEMPLATE_QZ = "重点关注：区总:%s,您好，%S（前一日）飞鸟系统登陆时因视频检测非本人面孔出现产生预警，并关闭系统，产生预警合计:%s次,%s个省%s次(其中,本大区%s次,%s)，请及时了解预警原因，加强登陆安全管理，具体详情请登陆“飞鸟系统”！";
    //总部短信模板
    public static String MESSAGE_TEMPLATE_ZB = "重点关注：%s您好，%S（前一日）飞鸟系统登陆时因视频检测非本人面孔出现产生预警合计:%s个区%s次,%s个省%s次(分别为%s%s)，请及时了解预警原因，加强登陆安全管理，具体详情请登陆“飞鸟系统”！";
    //报警图片地址
    public static String WarningPictureAddress ="/u02/webapps/crmkhnew/img/";
    //IE浏览器跳转到谷歌浏览器地址
    public static String IE_TO_CHROME_ADDRESS = "10.19.105.137/vipkf/login";
    //服务器地址(前面需要加http://)(https://devkyweixin.yundasys.com/)
    public static String FU_WU_IP = "http://10.19.105.137";

    /*//用户画像和从菜鸟仓的运单信息接口地址(uat)
    public static String YUNDAN_CAINIAO = "http://uat.kuaiyun.yundasys.com:8081/person_info/public/api/person_interface/GetPersonInfo";
    public static String YUNDAN_CAINIAO_APPID = "123456";
    public static String YUNDAN_CAINIAO_APPKEY = "test";
    //二维码3DES解密条件
    public static String DES_IV = "ZGm1wRTOu6E=";
    public static String DES_KEYS = "EDCXPB78JOCNWEE3JH3OF0G7";
    //录单接口地址(UAT)
    public static String LUDAN = "http://10.19.106.248:7001/orderinfo/query/recordinfo/getData.do";
    //流水号
    public static String STR_FORMAT = "00000000";
    //咨询来源
    public static String CONSULT_SOURCE = "工作台发起";
    //快件扫描记录key16(测试写死,生产要配置QConf)
    public static String aa="C52B701iONqytaUkst";*/

    //用户画像和从菜鸟仓的运单信息接口地址(生产)
    /*public static String YUNDAN_CAINIAO = "http://custinfo.yundasys.com:37141/person_info/public/index.php/api/person_interface/getPersonInfo";
    public static String YUNDAN_CAINIAO_APPID = "123456";
    public static String YUNDAN_CAINIAO_APPKEY = "test";*/
    //二维码3DES解密条件(生产)
    public static String DES_IV = "ZGm1wRTOu6E=";
    public static String DES_KEYS = "EH966BQJ0ACYW40DRI77FC4O";
    //录单生产
    //public static String LUDAN = "http://shipinfo.yundasys.com:30007/orderinfo/query/recordinfo/getData.do";
    //录单接口地址(UAT)
    public static String LUDAN = "http://10.19.106.248:7001/orderinfo/query/recordinfo/getData.do";
    //流水号
    public static String STR_FORMAT = "00000000";
    //咨询来源
    public static String CONSULT_SOURCE = "工作台发起";
    //快件扫描记录key16(测试写死,生产要配置QConf)
    public static String aa="C52B701iONqytaUkst";

    //录单解密key
    public static String LUDAN_KEY="AUKW8FuV9rsJmiPzZDiQ";

    //导出数量限制
    public static String LIMIT_OUT_EXCEL="10000";
    //导入数量限制
    public static String LIMIT_UPLOAD_EXCEL="5000";

    //角色id
    //管理员权限id(最大)
    public static String ROLE_ADMIN_id="1";
    //总部角色id
    public static String ROLE_ZB_id="102";
    //客服主管角色id
    public static String ROLE_KFZG_id="103";
    //客服角色id
    public static String ROLE_KF_id="104";

    //拦截uat地址
    public static String LAN_JIE="http://10.19.160.113:16113/tmm/busi_datatransfer/interface/interceptAccept.do?";
    //拦截信息来源(先用7-订单系统)
    public static String LAN_JIE_INFO_SOURCE_TYPE="7";
    //拦截表示填1
    public static String LAN_JIE_FLAG="1";


}
