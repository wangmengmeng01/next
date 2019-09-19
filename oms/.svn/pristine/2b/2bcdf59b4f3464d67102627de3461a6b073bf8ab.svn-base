<?php
/**
 * 业务基类
 * User: 独孤羽<123517746@qq.com>
 * Date: 15-4-27 下午5:29
 */
class omsBase extends msg
{
    public $customerDB = null;

    public function __construct($customerId='')
    {
        $dsn = 'mysql:host='.DB_EM_HOST.';dbname=oms_' . $customerId;
        $this->customerDB = new CDbConnection($dsn, DB_EM_USER, DB_EM_PASS);
        $this->customerDB->active = true; //建立连接
    }

}