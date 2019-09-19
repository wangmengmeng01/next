<?php

/**
 * 单据取消过滤类
 * @author Renee
 * 
 */
class filterOrderCancel extends msg
{

    /**
     * 过滤单据取消请求数据
     * @param  $requestData         
     * @return array
     *
     */
    public function cancel(&$requestData)
    {
        //连接数据库
        global $db;
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
        }
        $request = $requestData;
        
        //校验仓库
        if (empty($request['warehouseCode'])) {
            return $this->outputQimen('failure', '仓库编码不能为空', 'S003');
        } else {
            $sql = "SELECT warehouse_code FROM t_base_warehouse WHERE warehouse_code=:warehouse_code AND active_flag='Y' AND is_valid=1";
            $model = $db->prepare($sql);
            $model->bindParam(':warehouse_code', $request['warehouseCode']);
            $model->execute();
            $rs = $model->fetch(PDO::FETCH_ASSOC);
            if (empty($rs)) {
                return $this->outputQimen('failure', '仓库编码不存在或者无效', 'S003');
            } else {
                if ($rs['warehouse_code'] != $request['warehouseCode']) {
                    return $this->outputQimen('failure', '仓库编码大小写错误', 'S003');
                }
            }
        }
        
        //校验单据号
        if (empty($request['orderCode'])) {
            return $this->outputQimen('failure', '单据编码不能为空', 'S003');
        } 
        /*
        else {
            $orderType = $request['orderType'];
            $inType = array('B2BRK','SCRK','LYRK','CCRK','CGRK','DBRK','QTRK','XTRK','HHRK','THRK');
            $outType = array('PTCK','DBCK','B2BCK','QTCK','CGTH');
            $deliType = array('JYCK','HHCK','BFCK');
            $cnjgType = array('CNJG');
            if (in_array($orderType, $inType)) {//入库单
                $rs = $this->findOrder('t_inbound_info', $request);
                if (empty($rs)) {
                    return $this->outputQimen('failure', '该单据号不存在', 'S003');
                }
            } else if (in_array($orderType, $outType)) {//出库单
                $rs = $this->findOrder('t_outbound_info', $request);
                if (empty($rs)) {
                    return $this->outputQimen('failure', '该单据号不存在', 'S003');
                }
            } else if (in_array($orderType, $deliType)) {//发货单
                $sql = "SELECT delivery_id from t_delivery_order_info where delivery_order_code=:delivery_order_code AND customer_id = :customer_id AND order_type = :order_type AND warehouse_code=:warehouse_code AND is_valid=1";
                $model = $db->prepare($sql);
                $model->bindParam(':delivery_order_code', $request['orderCode']);
                $model->bindParam(':customer_id', $request['ownerCode']);
                $model->bindParam(':order_type', $request['orderType']);
                $model->bindParam(':warehouse_code', $request['warehouseCode']);
                $model->execute();
                $rs_deli = $model->fetch(PDO::FETCH_ASSOC);
                if (empty($rs_deli)) {
                    return $this->outputQimen('failure', '该单据号不存在', 'S003');
                }
            } else if (in_array($orderType, $cnjgType)) {
                $sql = "SELECT process_id from t_store_process_order_info where process_order_code=:process_order_code AND customer_id = :customer_id AND order_type = :order_type AND warehouse_code=:warehouse_code AND is_valid=1";
                $model = $db->prepare($sql);
                $model->bindParam(':process_order_code', $request['orderCode']);
                $model->bindParam(':customer_id', $request['ownerCode']);
                $model->bindParam(':order_type', $request['orderType']);
                $model->bindParam(':warehouse_code', $request['warehouseCode']);
                $model->execute();
                $rs_pro = $model->fetch(PDO::FETCH_ASSOC);
                if (empty($rs_pro)) {//仓内加工单
                    return $this->outputQimen('failure', '该单据号不存在', 'S003');
                }
            } else {
                return $this->outputQimen('failure', '该订单类型不存在', 'S003');
            }
        }*/
		return $this->outputQimen('success');
    }
    
    /**
     * 查询符合条件的单据
     * @param 表名 $tableName
     * @param 请求数据 $request
     * @return 查询结果
     */
    public function findOrder($tableName,$request){
        global $db;
        $sql = "SELECT order_id from ". $tableName ." where order_no = :order_no AND order_type = :order_type AND customer_id = :customer_id AND warehouse_code=:warehouse_code AND is_valid=1";
        $model = $db->prepare($sql);
        $model->bindParam(':order_no', $request['orderCode']);
        $model->bindParam(':order_type', $request['orderType']);
        $model->bindParam(':customer_id', $request['ownerCode']);
        $model->bindParam(':warehouse_code', $request['warehouseCode']);
        $model->execute();
        $rs = $model->fetch(PDO::FETCH_ASSOC);
        return $rs;
    }
}