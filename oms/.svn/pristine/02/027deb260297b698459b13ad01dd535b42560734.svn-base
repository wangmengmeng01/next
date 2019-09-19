<?php
/**
 * YWMS系统奇门接口签名算法
 * 
 */
class sign
{

    /**
     * 签名校验
     * @param $appKey WMS系统APP
     * @param $appSecret WMS系统中客户密码
     * @param $data body中的xml数据
     * @param $appSign 签名
     * @param $request 请求参数
     * @return bool
     */
    public function check($appSecret, $data, $appSign, $request)
    {

        $wmsMethod = array(
            'taobao.qimen.entryorder.confirm',      'entryorder.confirm',           //入库单确认接口
            'taobao.qimen.returnorder.confirm',     'returnorder.confirm',          //退货入库单确认接口
            'taobao.qimen.stockout.confirm',        'stockout.confirm',             //出库单确认接口
            'taobao.qimen.deliveryorder.confirm',   'deliveryorder.confirm',        //发货单确认接口
            'taobao.qimen.deliveryorder.batchconfirm','deliveryorder.batchconfirm', //发货单确认接口  (批量)
            'taobao.qimen.sn.report',               'sn.report',                    //发货单SN通知接口
            'taobao.qimen.orderprocess.report',     'orderprocess.report',          //订单流水通知接口
            'taobao.qimen.itemlack.report',         'itemlack.report',              //发货单缺货通知接口
            'taobao.qimen.inventory.report',        'inventory.report',             //库存盘点通知接口
            'taobao.qimen.storeprocess.confirm',    'storeprocess.confirm',         //仓内加工单确认接口
            'taobao.qimen.stockchange.report',      'stockchange.report',           //库存异动通知接口
            'taobao.qimen.warehouse.reg',                                           //仓库注册接口
            'taobao.qimen.warehouse.update',                                        //仓库更新接口
            'taobao.qimen.warehouse.query',                                         //仓库查询接口
            'taobao.qimen.customer.reg',                                            //用户注册接口
            'taobao.qimen.deliveryorder.shortage',                                  //发货单缺货通知接口
            'taobao.qimen.transwarehouse.report',                                   //缺货转仓通知接口
        );

        //校验签名
        $method = $request['method'];
        if (in_array($method,$wmsMethod)) {//wms请求
            $sign = strtoupper(base64_encode(md5($appSecret . $data . $appSecret)));
            if ($sign != $appSign) {
                return false;
            } else {
                return true;
            }
        } else {//erp请求
            unset($request['sign']);
            ksort($request);
            $str = $appSecret;
            foreach ($request as $key => $val)
            {
                if (in_array($key, qimen_service::$_systemParams)) {
                    $str .= $key . $val;
                }
            }
            $str .= $data . $appSecret;
            $sign = strtoupper(md5($str));
            if ($sign != $appSign) {
                return false;
            } else {
                return true;
            }
        }
    }

}