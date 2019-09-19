<?php
/**
 * ERP客商档案内部类
 * User:  wp
 */
require_once API_ROOT . '/router/interface/erp/common/erpRequestInner.php';
class erpCustomerInner extends erpRequestInner
{

    public function __construct()
    {
        parent::__construct();
    }

    /**
     * 货主推送
     * @param $params
     * @return mixed
     */
    public function push($params)
    {
        $method = inner_service::$_methodTo;
        return $this->send($method, $params);
    }


}