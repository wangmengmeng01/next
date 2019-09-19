<?php

class ServiceController extends CController
{
    //定义奇门API名称
    public $methodList = array('taobao.qimen.singleitem.synchronize', 'singleitem.synchronize',   //商品同步接口
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
        'taobao.qimen.deliveryorder.confirm', 'deliveryorder.confirm',  //发货单确认接口
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
        'service.heartbeat'   //心跳接口
    );

    //定义菜鸟API名称
    public $cainiaoMethodList = array('taobao.wlb.waybill.i.get',           //获取物流服务商电子面单号
        'taobao.wlb.waybill.i.search',                  //查询面单服务订购及面单使用情况
        'taobao.wlb.waybill.i.fullupdate',              //面单信息更新接口
        'taobao.wlb.waybill.i.print',                   //打印确认接口
        'taobao.wlb.waybill.i.querydetail',             //查面单号状态
        'taobao.wlb.waybill.i.cancel'                   //商家取消获取的电子面单号
    );

    /**
     * API请求入口
     * @param mixed $_POST OR $_GET
     */
    public function actionIndex()
    {
        if (isset($_REQUEST['method']) && in_array($_REQUEST['method'], $this->methodList)) {   //调用奇门接口
            Yii::import('application.qimen_service');
            $serviceObj = new qimen_service();
        } elseif (isset($_REQUEST['method']) && in_array($_REQUEST['method'], $this->cainiaoMethodList)) {//调用菜鸟接口
            require API_ROOT . 'application.cainiao_service';
            $serviceObj = new cainiao_service();
        } elseif ($_REQUEST['inner_service'] == 'true') {//内部调用，即OMS发起
            Yii::import('application.inner_service');
            $serviceObj = new inner_service();
        } else {//外部调用，即接收erp或wms请求
            Yii::import('application.service');
            $serviceObj = new service();
        }
        $serviceObj->process();
    }

    public function actionError()
    {
        Yii::log('入口请求错误', 'error');
    }

}