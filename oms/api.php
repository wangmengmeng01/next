<?php
header("Content-type:text/html;charset=utf-8");
set_time_limit(0);
error_reporting(0);
date_default_timezone_set('Asia/Shanghai');

define('ROOT_DIR', dirname(realpath(__FILE__)));
define('API_ROOT', ROOT_DIR . '/api');
define('APP_ROOT', __DIR__);

require_once './config.php';
require_once API_ROOT . '/config/config.php';
require_once API_ROOT . '/config/DbAction.php';
require_once API_ROOT . '/ext/xml.php';
require_once API_ROOT . '/ext/util.php';
require_once API_ROOT . '/ext/httpclient.php';
require_once API_ROOT . '/msg.php';
require_once API_ROOT . '/func.php';

$wmsRequestArr = array(
    'taobao.qimen.entryorder.confirm', 'entryorder.confirm',  //入库单确认接口
    'taobao.qimen.returnorder.confirm', 'returnorder.confirm',  //退货入库单确认接口
    'taobao.qimen.stockout.confirm', 'stockout.confirm',  //出库单确认接口
    'taobao.qimen.deliveryorder.confirm', 'deliveryorder.confirm',  //发货单确认接口
    'taobao.qimen.deliveryorder.batchconfirm', 'deliveryorder.batchconfirm',//发货单确认接口  (批量)
    'taobao.qimen.sn.report', 'sn.report',  //发货单SN通知接口
    'taobao.qimen.orderprocess.report', 'orderprocess.report',  //订单流水通知接口
    'taobao.qimen.itemlack.report', 'itemlack.report',  //发货单缺货通知接口
    'taobao.qimen.inventory.report', 'inventory.report',  //库存盘点通知接口
    'taobao.qimen.storeprocess.confirm', 'storeprocess.confirm',  //仓内加工单确认接口
    'taobao.qimen.stockchange.report', 'stockchange.report',  //库存异动通知接口
    'taobao.qimen.warehouse.reg',  //仓库注册接口
    'taobao.qimen.warehouse.update',  //仓库更新接口
    'taobao.qimen.warehouse.query', //仓库查询接口
    'taobao.qimen.customer.reg',  //用户注册接口
    'taobao.qimen.deliveryorder.shortage',//发货单缺货通知接口
    'taobao.qimen.transwarehouse.report',//缺货转仓通知接口
);

if (!empty($_REQUEST['method']) && in_array($_REQUEST['method'], $wmsRequestArr)) {
    require_once API_ROOT . '/ext/tiancan/tiancan.php';
    require_once API_ROOT . '/ext/tiancan/config_tc.php';
    //接入天蚕
    if (CHECK_TIANCAN == 1) {
        //配置接口返回字段属性，用于配置天蚕校验返回与接口返回数据格式保持一致(必写项)
        $apiReturnDataType = '<?xml version="1.0" encoding="utf-8"?><error_response><code>%s</code><msg>%s</msg></error_response>';
        $check_tc = new tiancan($apiReturnDataType);
        $isPass = $check_tc->tiancanUnactive();
        $xmlObj = new xml();
        $result = $xmlObj->xmlStr2array($isPass);
        if ($result['code'] != 'SUCC') die($isPass);
    }
}

//连接数据库
$db = connectDb();

//redis参数配置
$redisApiParam = array(
    'queue_server' => STORAGE_SERVER,
    'queue_name' => REDIS_KEY,
    'fail_queue_name' => REDIS_KEY_FAIL,
    'inner_api_url' => OMS_API_URL,
    'redis_storage' => array(
        'host' => REDIS_HOST,
        'port' => REDIS_PORT,
        'auth' => REDIS_AUTH
    )
);

//定义奇门API名称数组
$methodListArr = array('taobao.qimen.singleitem.synchronize', 'singleitem.synchronize',   //商品同步接口
    'taobao.qimen.items.synchronize', 'items.synchronize',   //商品同步接口(批量)
    'taobao.qimen.combineitem.synchronize', 'combineitem.synchronize', //组合商品接口
    'taobao.qimen.entryorder.create', 'entryorder.create',  //入库单创建接口
    'taobao.qimen.entryorder.confirm', 'entryorder.confirm',  //入库单确认接口
    'taobao.qimen.entryorder.query', 'entryorder.query',  //入库单查询接口
    'taobao.qimen.returnorder.create', 'returnorder.create',  //退货入库单创建接口
    'taobao.qimen.returnorder.confirm', 'returnorder.confirm',  //退货入库单确认接口
    'taobao.qimen.returnorder.query', 'returnorder.query',  //退货入库单查询接口
    'taobao.qimen.stockout.create', 'stockout.create',  //出库单创建接口
    'taobao.qimen.stockout.confirm', 'stockout.confirm',  //出库单确认接口
    'taobao.qimen.stockout.query', 'stockout.query',  //出库单查询接口
    'taobao.qimen.deliveryorder.create', 'deliveryorder.create',  //发货单创建接口
    'taobao.qimen.deliveryorder.batchcreate', 'deliveryorder.batchcreate',//发货单创建接口(批量)
    'taobao.qimen.deliveryorder.confirm', 'deliveryorder.confirm',  //发货单确认接口
    'taobao.qimen.deliveryorder.batchconfirm', 'deliveryorder.batchconfirm',//发货单确认接口  (批量)
    'taobao.qimen.deliveryorder.query', 'deliveryorder.query',  //发货单查询接口
    'taobao.qimen.sn.report', 'sn.report',  //发货单SN通知接口
    'taobao.qimen.orderprocess.query', 'orderprocess.query',  //订单流水查询接口
    'taobao.qimen.orderprocess.report', 'orderprocess.report',  //订单流水通知接口
    'taobao.qimen.orderstatus.batchquery', 'orderstatus.batchquery',  //订单状态查询接口（批量）
    'taobao.qimen.itemlack.report', 'itemlack.report',  //发货单缺货通知接口
    'taobao.qimen.itemlack.query', 'itemlack.query', //发货单缺货查询接口
    'taobao.qimen.order.cancel', 'order.cancel',  //单据取消接口
    'taobao.qimen.inventory.query', 'inventory.query',  //库存查询接口
    'taobao.qimen.inventory.report', 'inventory.report',  //库存盘点通知接口
    'taobao.qimen.inventorycheck.query', 'inventorycheck.query',  //库存盘点查询接口
    'taobao.qimen.storeprocess.create', 'storeprocess.create',  //仓内加工单创建接口
    'taobao.qimen.storeprocess.confirm', 'storeprocess.confirm',  //仓内加工单确认接口
    'taobao.qimen.stockchange.report', 'stockchange.report',  //库存异动通知接口
    'taobao.qimen.autotransfer.query', 'autotransfer.query',  //菜鸟自动流转查询接口  （扩展）
    'service.heartbeat',   //心跳接口
    'taobao.qimen.shop.synchronize', 'shop.synchronize',  //店铺同步接口
    'taobao.qimen.warehouse.reg',  //仓库注册接口
    'taobao.qimen.warehouse.update',  //仓库更新接口
    'taobao.qimen.warehouse.query', //仓库查询接口
    'taobao.qimen.customer.reg',  //用户注册接口
    'taobao.qimen.metadata.query', 'metadata.query',  //数据字典获取接口
    'taobao.qimen.metadata.update', 'metadata.update',  //数据字典获取接口
    'taobao.qimen.stock.query', 'stock.query',  //库存查询接口(多条件)
    'taobao.qimen.order.pending', 'order.pending',  //单据挂起(恢复)接口
    'taobao.qimen.deliveryorder.batchcreate.answer', 'deliveryorder.batchcreate.answer',  //发货单创建结果通知接口 （批量）
    'taobao.qimen.wavenum.report', 'wavenum.report',  //发货单波次通知接口
    'taobao.qimen.deliveryorder.shortage',//发货单缺货通知接口
    'taobao.qimen.transwarehouse.report',//缺货转仓通知接口
    'settlement.query',//美团货主进销存查询接口
    'cf.deliveryorder.picked.confirm',//家乐福卖场拣货任务拣货完成
    'cf.inventory.sync',//家乐福库存信息接收接口
    'fx.warehouseinfo.query',//纸品分销目的分拨和仓编码查询接口
);

//定位接口文件，并实例化接口对象
if (isset($_REQUEST['method']) && in_array($_REQUEST['method'], $methodListArr)) {
    //调用奇门接口
    require API_ROOT . '/qimen_service.php';
    $serviceObj = new qimen_service();
} elseif ($_REQUEST['inner_service'] == 'true') {
    //内部接口调用，即OMS发起
    require API_ROOT . '/inner_service.php';
    $serviceObj = new inner_service();
} else {
    //外部调用，即接收erp或wms请求
    require API_ROOT . '/service.php';
    $serviceObj = new service();
}
$serviceObj->process();

