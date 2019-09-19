<?php
/**
 * 与服务器相关配置
 * @copyright 2014.09
 */
/*define('DB_EM_HOST', '10.19.156.212');
define('DB_EM_PORT', '3306');
define('DB_EM_USER', 'ggs');
define('DB_EM_PASS', 'IhMacKjCEPLYduzXcGpwyy');*/

define('DB_EM_HOST', '10.19.156.186');
define('DB_EM_PORT', '3306');
define('DB_EM_USER', 'ggs');
define('DB_EM_PASS', 'IhMacKjCEPLYduzXcGpwyy');

define('DB_EM_NAME', 'oms');
define('DB_EM_CHAR', 'utf8');
define('DB_EM_PREFIX', 't_');

/**
 * oms-jd数据库连接配置
 */
define('DB_JD_HOST', '10.19.105.122');
define('DB_JD_USER', 'ggs');
define('DB_JD_PASS', 'HXR0D9RFBnH74L3d');

define('DB_JD_NAME', 'oms');
define('DB_JD_CHAR', 'utf8');

//如意达京东应用信息
define('JD_APP_KEY', '6C212D61CF4CE6E3D9F6FC8891A0D818');
define('JD_APP_SECRET', '06cb8efb467e4ae0992bf15d7bc39e12');
define('JD_YD_URL', 'http://103.235.245.166:30080/test_jd_api_entrance.php');

define('YWMS_WAYBILL_APP_KEY', 'YWMS');
define('YWMS_WAYBILL_APP_SECRET', '1234567890');
define('YWMS_WAYBILL_OMS_URL', 'http://127.0.0.1/oms/jd_api.php');

/**
 * SDK工作目录
 * 存放日志，JD缓存数据
 */
define("JD_SDK_WORK_DIR", "E:/log/");
/**
 * 是否处于开发模式
 * 在你自己电脑上开发程序的时候千万不要设为false，以免缓存造成你的代码修改了不生效
 * 部署到生产环境正式运营后，如果性能压力大，可以把此常量设定为false，能提高运行速度（对应的代价就是你下次升级程序时要清一下缓存）
 */
define("JD_SDK_DEV_MODE", true);
define('CN_WMS' , 'https://link.tbsandbox.com/gateway/link.do');
define('WMS_STOCK_URL' , 'http://10.19.151.161:7001/cnif/external/serviceForCN.do');

// 接口配置
// oms接口入口地址
define('OMS_API_URL', 'http://127.0.0.1/oms/1.0.1/api.php');
// 接口系统级参数中的客户编码
define('OMS_API_CLIENT_CUSTOMERID', 'FLUXWMS');
// wms数据库
define('OMS_API_CLIENT_DB', 'WH01');
// Token 号
define('OMS_API_APPTOKEN', '80AC1A3F-F949-492C-A024-7044B28C8025');
// 验签KEY
define('OMS_API_APPKEY', 'test');
// 客商档案接口中定义的系统级货主
define('CUSTOMER_SYS', 'DATAHUB');
// 验签加密key
define('OMS_API_KEY', '1234567890');
// 接口是否进行严格校验开关
define('STRICT_VERIFY_FLAG', true);
// 接口库存更新开关
define('UPDATE_INVENTORY_FLAG', true);
// 出库单下发接口是否应用缓存机制开关
define('ORDER_DELIVERY_REDIS_FLAG', false);

// 奇门入库单确认，奇门退货入库单确认，奇门发货单确认接口手工推送app_key
define('SRD_APP_KEY', '1023229135');
// 奇门入库单确认，奇门退货入库单确认，奇门发货单确认接口手工推送secret
define('SRD_APP_SECRET', 'sandbox70e25bb970a1ffd3d4f411e1a');

//cainiao接口配置
//cainiao接口入口地址
define('CAINIAO_API_URL', 'http://127.0.0.1/oms/cainiao_api.php');
//cainiao验签key
define('CAINIAO_APP_KEY', '1021629636');
//cainiao秘钥
define('CAINIAO_APP_SECRET', 'sandbox8296a6cef277dea154e112275');

//storage 接口配置
//storage接口入口地址
define('STORAGE_API_URL', 'http://127.0.0.1/oms/1.0.1/storage_api.php');
//wms接口入口地址
define('STORAGE_WMS_API_URL', 'http://10.20.24.229:7001/wiq/external/serviceForQM.do');
// define('STORAGE_WMS_API_URL', 'http://10.19.105.161:7001/wiq/external/serviceForQM.do');
//define('STORAGE_WMS_API_URL', 'http://10.20.24.182:7002/wiq/external/serviceForQM.do');
//storage验签key
define('STORAGE_APP_KEY', 'SANDBOX191340');
//storage秘钥
define('STORAGE_APP_SECRET', '44Y29ECR65RFwx883avkgx684A325D6b');
// define('STORAGE_CN_URL','http://localhost/oms_test/wms_storage/cn_rs.php');
// define('STORAGE_CN_URL','http://pac.tbsandbox.com/gateway/pac_message_receiver.do?session_type=debug'); 
// define('STORAGE_CN_URL','http://linkdaily.tbsandbox.com/gateway/link.do');
// define('STORAGE_CN_URL','https://link.tbsandbox.com/gateway/link.do');
define('STORAGE_CN_URL', 'http://localhost/oms_test/wms_storage/test_receive.php');
//define('STORAGE_CN_URL','https://link.tbsandbox.com/gateway/link.do');

/*
//storage接口入口地址
define('STORAGE_API_URL', 'http://127.0.0.1/oms/storage_api.php'); 
//wms接口入口地址
define('STORAGE_WMS_API_URL', 'http://10.19.105.161:7001/wiq/external/serviceForQM.do');
//storage验签key
define('STORAGE_APP_KEY', '381463');
//storage秘钥
define('STORAGE_APP_SECRET', '4v7vL1X9i84996e392b216efOG4UBQat');
define('STORAGE_CN_URL','http://link.cainiao.com/gateway/link.do');
*/


//定义地址归集三期筛单接口地址
define('URL_SELECT_ORDER_THIRD', 'http://192.168.25.5:25111/ydAddr/call/qrcode/batchAddrCode.do');

// 队列存储服务器
define('STORAGE_SERVER', 'redis_storage'); // redis_storage：redis，其它可扩展
define('STORAGE_THREAD_NUM', 5); // 队列进程数

// -------------------redis配置
define('REDIS_HOST', '10.19.105.105');
define('REDIS_PORT', 9400);
// define('REDIS_HOST', '127.0.0.1');
// define('REDIS_PORT', 6379);
define('REDIS_AUTH', '');
define('REDIS_KEY', 'oms_redis_data'); // 队列存储KEY名称
define('REDIS_KEY_FAIL', 'oms_redis_data_fail'); // 失败队列存储KEY名称

// 日志
define('RECEIVE_LOG_FLAG', true); // 接收接口数据日志开关
define('LOG_PATH', 'D:/log/');
//define('LOG_PATH', '/yd/oms/log/');
define('LOG_FILENAME', date('Y-m-d') . '.log');
define('YII_INTERFACE_LOG_FLAG', true);

// 统一授权配置
//定义系统管理员角色ID
define('AUTH_SYSTEM_MANAGER', 'systemManage');
//定义系统配置管理员角色ID
define('AUTH_CONFIGURE_MANAGER', 'configureMange');
//定义总部操作员角色ID
define('AUTH_HEADER_OPERATOR', 'headquartersOperator');
//定义网点操作员角色ID
define('AUTH_BRANCH_OPERATOR', 'branchOperator');
// 统一授权服务器主机
define('AUTH_SERVER_HOST', 'http://10.19.105.152:7001');
//define('AUTH_SERVER_HOST', 'uat.ydauth.yundasys.com:16120');
// 应用ID即authId
define('AUTH_APPID', '100000115');
// 权限对象ID
define('AUTH_OBJECTID', 'menu_check_qxobj');
// 锁ID
define('AUTH_LOCKID', 'menulock');
//权限对象的属性ID
define('AUTH_ATTRID', 'memuid');
//统一授权系统单个权限查询URL
define('AUTH_SINGLE_PRI_URL', AUTH_SERVER_HOST . '/ydauth/actions/outer/auth/checkWithDynamicLock.action?userId=%s&authId=%s&objectId=%s&lockId=%s&dynamicAttrs=[{"attrId":"%s","attrValType":"S","attrVal1":"%s","attrVal2":""}]');
//统一授权系统获取用户角色URL
define('AUTH_ROLE_URL', AUTH_SERVER_HOST . '/ydauth/actions/outer/user/queryUserInfoAndRole.action?authId=%s&userId=%s');
//统一授权系统获取用户所有的权限URL
define('AUTH_ALL_PRI_URL', AUTH_SERVER_HOST . '/ydauth/actions/outer/auth/getUserAccessedKeyValues.action?authId=%s&userId=%s&objectId=%s');
//统一授权系统获取用户菜单URL
define('AUTH_MENU_URL', AUTH_SERVER_HOST . '/ydauth/actions/outer/user/menu.action?authId=%s&userId=%s');

//跨境仓储出库单状态erp接口地址
//define('UPDATA_DELIVERY_INFO_URL','http://121.40.230.53/api/updataDeliveryInfo');
define('UPDATA_DELIVERY_INFO_URL', 'http://localhost/oms_test/kj/test_receive.php');

//网易考拉wms_id的值
define('KAOLA_WMS_ID', 'yunda');
//网易考拉公钥
//define('KAOLA_WY_PUB_KEY','30819f300d06092a864886f70d010101050003818d0030818902818100a6c302335c7ec6cbd0fc10768c18c423564bafc781d69ddcdfc578ed68765e2b7603f763514bf1fea8273cd155125147ef1d35a5cf8bb19096e0ec370e1916759634bc4b8e2a684ef4211d3320e3d113cacacdf2d20b1c1ccdc8775f894d5d6e5691e706b9751210fa5f52cb4777e4530acde1c3ecc16c58f375b53f3835f69b0203010001');
define('KAOLA_WY_PUB_KEY', '30819f300d06092a864886f70d010101050003818d0030818902818100c7ad2f3cbcd0affca2821c5183739e5976636b36f64492e9966b189e0cf8134624a9be5b9da508a360ae32944a834945e645b10ab90bb7d9f9248c8f611a6d1a42261c5446fe322c78d32de38e131189774c53c49cc939f556438e80a8f49287556dab7167563fe2e692e7ceb96ef88bcf901912c0386968562ec0cd907b55910203010001');
//网易考拉子母件获取单号接口地址
define('KAOLA_GETBILLNO_URL', 'http://engine.kaolatest.netease.com/logistic/billno/getBillno');
//网易考拉子母件KEY值
define('KAOLA_GETBILLNO_KEY', 'test123xyz');

//网易考拉清单信息同步接口WMS接口地址
//define('KAOLA_INVT_WMS_URL','http://10.20.24.225:7001/nwif/external/netease/billsAuditRelease.do');
define('KAOLA_INVT_WMS_URL', 'http://10.19.151.161:7001/nwif/external/netease/billsAuditRelease.do');
//海关多清单归并核放单接口地址
define('CUSTOM_BATCHSENDHFD_URL', 'https://221.224.53.219:8000/BatchSendHfd');

//网易核放单回调接口用户ID
define('REVIEWRELEASE_USERID','3301540039');
//网易核放单回调接口用户密码
define('REVIEWRELEASE_SIGN','3e4e159afd4a47f49db9337c29025a63');
//网易核放单回调接口地址
define('REVIEWRELEASE_URL','https://www.yuncangwl.com/fn_wms/api/sz/reviewRelease');

//地址归集批量获取三段码接口（用于获取目的分拨）
define('URL_GET_DISTRICENTER_CODE','http://192.168.25.5:25111/ydAddr/call/qrcode/threeSegMentCode.do');

define('BSC_URL','http://121.40.230.53:8088/api/updataRKDInfo');
//考拉入口地址
define('KAOLA_URL','http://openapi-test.kaola.com/router');

//唯品会系统级输入参数
define('VIP_SERVICE','vipapis.delivery.JitDeliveryService');
define('VIP_VERSION','1.0.0');
define('VIP_FORMAT','json');
define('VIP_APPURL','http://sandbox.vipapis.com');//沙箱环境
//define('VIP_APPURL','https://gw.vipapis.com/');//正式环境
define('VIP_APPKEY','a876c4cc');
define('VIP_APPSECRET','77780A5819EC3CFBE648436DB9F95492');
//VIP vendor_id
define('VIP_VENDOR_ID','550');

//本系统调用VIP接口秘钥
define('VIP_OMS_SELF_REQ_SECRET','96c76c3f40b00bd9b24602ba96da18e1');
//VIP接口交互秘钥
define('VIP_WMS_SECRET','123456');

//PDD接口交互秘钥
define('PDD_URL','https://gw-api.pinduoduo.com/api/router');//拼多多接口地址
define('CLIENT_ID','928404d8e0364187ad00028a6fdadf59');//POP分配给应用的client_id
define('CLIENT_SECRET','328df06f956a43c587d6c856ad249c8beafc1377');//拼多多平台client_secret
//pdd接口入口地址
define('PDD_API_URL', 'http://127.0.0.1/oms/pdd_api.php');

//PDD用户授权请求地址
define('PDD_AUTH_URL', 'http://mms.pinduoduo.com/open.html?response_type=code&client_id=4b646e71c7f987d5aab5435a672e4c9a &redirect_uri=http://www.oauth.net/2/&state=');
//贝贝_孝感仓
define('BEIBEI_API_URL', 'http://127.0.0.1/oms/beibei_api.php');//接口入口地址
define('BEIBEI_SESSION', 'e7d36997d1fa'); //贝贝session

class OmsDatabase
{
    public static $oms_db = null;
}

require_once dirname(__FILE__) . DIRECTORY_SEPARATOR . '/api/config/database.php';

try {
    OmsDatabase::$oms_db = new Database("mysql:host=" . DB_EM_HOST . ";port=" . DB_EM_PORT . ";dbname=" . DB_EM_NAME . ";", constant("DB_EM_USER"), constant("DB_EM_PASS"));
} catch (PDOException $e) {
    print "Error!: " . $e->getMessage() . "<br/>";
    die();
}

