<?php
/**
 * 订单业务基类
 * User: 独孤羽<123517746@qq.com>
 * Date: 15-4-27 下午5:29
 */
require API_ROOT . '/router/interface/base/omsBase.php';
class omsOrder extends omsBase
{
    public function __construct($customerId)
    {
        parent::__construct($customerId);
    }

    /**
     * 订单创建
     * @param $sdf
     */
    public function create($sdf)
    {
        //当前客户数据库实例：$this->customerDB
        $sql = 'INSERT INTO orders(order_id,total_amount) VALUES(:order_id, :total_amount)';
        $oCommand = $this->customerDB->createCommand($sql);
        $oCommand->bindParam(':order_id', $sdf->order_id, PDO::PARAM_INT);
        $oCommand->bindParam(':total_amount', $sdf->total_amount, PDO::PARAM_STR);
        if ($oCommand->query()) {
            return $this->output('succ', '添加订单成功');
        } else {
            return $this->output('fail', '添加订单失败');
        }
    }

    /**
     * 生成订单号
     * @param String
     */
    public function makeBn()
    {
        //当前客户数据库实例： $this->customerDB

    }

}