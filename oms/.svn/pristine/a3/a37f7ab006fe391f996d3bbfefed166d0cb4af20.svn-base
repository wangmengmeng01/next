<?php
/**
 * User: 谁没有点回忆
 * Date: 2018/9/11 0011
 * Time: 上午 10:33
 */
try {
    include './cainiaoWms.php';
    include_once '../../../../config.php';
    $post_data     = $_POST;
    $cainiao_stock = new cainiaoWms();
    if (!$cainiao_stock->checkSign(STORAGE_APP_SECRET,$post_data['logistics_interface'],$post_data['data_digest'])) {
        $cainiao_stock->writeLog($post_data , '签名错误');
        exit($cainiao_stock->sendError('S001' , '签名错误'));
    }

    //判断用户是否存在，wms请求时还要查询出qimen_customer_id
    $cainiao_array = array(
        'CN_WMS_STOCKOUT_CREATE',
        'CN_WMS_RETURNORDER_CREATE',
        'CN_WMS_ORDER_CANCEL',
        'CN_WMS_ENTRYORDER_CREATE',
        'CN_WMS_DELIVERYORDER_CREATE',
        'CN_WMS_SINGLEITEM_SYNCHRONIZE'
    );
    $partner_code = isset($post_data['partner_code']) ? $post_data['partner_code'] : $post_data['logistic_provider_id'];
    $partner = $cainiao_stock->findQimenCustomerId($partner_code);
    if (empty($partner)) {
        $cainiao_stock->writeLog($post_data, '用户不存在');
        exit($cainiao_stock->sendError('S003' , '用户不存在'));
    }

    //在$cainiao_array数组中是菜鸟请求我们，我们把数据转发wms,否则是wms请求我们，将数据转发菜鸟
    if (in_array($post_data['msg_type'] , $cainiao_array)) {
        $url     = WMS_STOCK_URL;
        $partner = $cainiao_stock->isExist($post_data['partner_code']);
    } else {
        $url     = CN_WMS;
        //$post_data['logistic_provider_id'] = $partner['qimen_customer_id'];
    }
    $get_data = json_encode(simplexml_load_string($post_data['logistics_interface']));//将对象转换个JSON
    $get_data = json_decode($get_data , true);

    switch ($post_data['msg_type']) {
        case 'CN_WMS_STOCKOUT_CREATE':        //出库单创建接口
            $response      = $cainiao_stock->stockOutCreate($get_data);
            $order_id      = $get_data['deliveryOrder']['deliveryOrderCode'];
            $ownerCode     = $get_data['deliveryOrder']['ownerCode'];
            break;
        case 'CN_WMS_STOCKOUT_CONFIRM':       //出库单确认接口
            $response      = $cainiao_stock->stockOutConfirm($get_data);
            $order_id      = $get_data['deliveryOrder']['deliveryOrderCode'];
            $ownerCode     = $get_data['deliveryOrder']['ownerCode'];
            break;
        case 'CN_WMS_RETURNORDER_CONFIRM':    //退货入库单确认接口
            $response      = $cainiao_stock->returnOrderConfirm($get_data);
            $order_id      = $get_data['returnOrder']['returnOrderCode'];
            $ownerCode     = $get_data['returnOrder']['ownerCode'];
            break;
        case 'CN_WMS_RETURNORDER_CREATE':     //退货入库单创建接口
            $response      = $cainiao_stock->returnOrderCreate($get_data);
            $order_id      = $get_data['returnOrder']['returnOrderCode'];
            $ownerCode     = $get_data['returnOrder']['ownerCode'];
            break;
        case 'CN_WMS_INVENTORY_REPORT':       //库存盘点通知接口
            $response      = $cainiao_stock->inventoryReport($get_data);
            $order_id      = $get_data['check_order_code'];
            $ownerCode     = $get_data['ownerCode'];
            break;
        case 'CN_WMS_ORDER_CANCEL':           //单据取消接口
            $response      = $cainiao_stock->orderCancel($get_data);
            $order_id      = $get_data['orderCode'];
            $ownerCode     = $get_data['ownerCode'];
            break;
        case 'CN_WMS_ENTRYORDER_CONFIRM':     //入库单确认接口
            $response      = $cainiao_stock->entryOrderConfirm($get_data);
            $order_id      = $get_data['entryOrder']['entryOrderCode'];
            $ownerCode     = $get_data['entryOrder']['ownerCode'];
            break;
        case 'CN_WMS_ORDERPROCESS_REPORT':    //订单流水通知接口
            $response = $cainiao_stock->orderProcessReport($get_data);
            $order_id      = $get_data['order']['orderCode'];
            $ownerCode     = $get_data['order']['ownerCode'];
            break;
        case 'CN_WMS_DELIVERYORDER_CONFIRM':  //发货单确认接口
            $response = $cainiao_stock->deliveryOrderConfirm($get_data);
            $order_id      = $get_data['deliveryOrder']['deliveryOrderCode'];
            $ownerCode     = $get_data['deliveryOrder']['ownerCode'];
            break;
        case 'CN_WMS_ENTRYORDER_CREATE':      //入库单创建接口
            $response      = $cainiao_stock->entryOrderCreate($get_data);
            $order_id      = $get_data['entryOrder']['entryOrderCode'];
            $ownerCode     = $get_data['entryOrder']['ownerCode'];
            break;
        case 'CN_WMS_DELIVERYORDER_CREATE':   //发货单创建接口
            $response      = $cainiao_stock->deliveryOrderCreate($get_data);
            $order_id      = $get_data['deliveryOrder']['deliveryOrderCode'];
            $ownerCode     = $get_data['deliveryOrder']['ownerCode'];
            break;
        case 'CN_WMS_SINGLEITEM_SYNCHRONIZE': //商品信息通知
            $response      = $cainiao_stock->singleItemSyncronize($get_data);
            $order_id      = $get_data['item']['itemCode'];
            $ownerCode     = $get_data['ownerCode'];
            break;
        default :
            $response = $cainiao_stock->sendError('S002' , '方法不存在');
            break;
    }    

    //将数据转发给wms或菜鸟
    $reult = $cainiao_stock->curl_post($url, $post_data);
    $cainiao_stock->writeLog($post_data, $reult,$order_id,$ownerCode);

    if ($reult['flag'] != 'success') {
        $code    = isset($reult['code']) ? $reult['code'] : $reult['errorCode'];
        $message = isset($reult['message']) ? $reult['message'] : $reult['errMsg'];
        exit($cainiao_stock->sendError($code , $message));
    }

    exit($response);
} catch (Exception $e) {

    exit($cainiao_stock->sendError('S005' , $e->getFile().'***'.$e->getMessage().'***'.$e->getLine()));
}


