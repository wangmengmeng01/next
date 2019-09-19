<?php
/**
 * wms订单类
 * User:  独孤羽<123517746@qq.com>
 * Date: 15-4-27 下午4:38
 */
require API_ROOT . '/router/interface/wms/common/wmsRequest.php';
require API_ROOT . '/router/interface/base/omsOrder.php';
class wmsOrder extends wmsRequest
{

    public $orderObj = null;

    public function __construct()
    {
        parent::__construct();
        $this->orderObj = new omsOrder($this->customerId);
    }

    /**
     * wms下单
     * @param $params
     * @return mixed
     */
    public function create($params)
    {
        //订单业务处理

    	print_r($params);die();
        $method = 'wms.order.create';
        return $this->send($method, $params);
    }


}