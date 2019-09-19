<?php

/**
 * 出库单创建过滤类
 *
 */
class filterStockOutCreate extends msg
{

    /**
     * 过滤出库单创建订单数据
     * @param  $requestData         
     * @return array
     *
     */
    public function create(&$requestData)
    {
        //连接数据库
		global $db;
		//校验数据是否为空
		if (empty($requestData)) {
			return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
		}
        
        $data = $requestData;
        $request = $data['deliveryOrder'];
        $orderLines = $data['orderLines']['orderLine'];
        
        //校验单据总行数,当单据总行数为空时赋值为1
        if (empty($request['totalOrderLines'])) {
        	$requestData['deliveryOrder']['totalOrderLines'] = 1;
        } elseif (!preg_match("/^\d+$/", $request['totalOrderLines'])) {
        	return $this->outputQimen('failure', '单据总行数必须为整数', 'S003');
        }
        
        //校验出库单号
        if (empty($request['deliveryOrderCode'])) {
            return $this->outputQimen('failure', '出库单号不能为空', 'S003');
        }
        
        //校验出库单类型
        if (empty($request['orderType'])) {
            return $this->outputQimen('failure', '出库单类型不能为空', 'S003');
        }
        
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
        
        //校验出库单创建时间
        if (empty($request['createTime'])) {
            return $this->outputQimen('failure', '出库单创建时间不能为空', 'S003');
        }
        
        //校验收件人信息
        if (empty($request['receiverInfo']['name'])) {
            return $this->outputQimen('failure', '收件人姓名不能为空', 'S003');
        }
        
        if (empty($request['receiverInfo']['mobile'])) {
            return $this->outputQimen('failure', '收件人移动电话不能为空', 'S003');
        }
        
        if (empty($request['receiverInfo']['province'])) {
            return $this->outputQimen('failure', '收件人信息省份不能为空', 'S003');
        }
        
        if (empty($request['receiverInfo']['city'])) {
            return $this->outputQimen('failure', '收件人信息城市不能为空', 'S003');
        }
        
        if (empty($request['receiverInfo']['detailAddress'])) {
            return $this->outputQimen('failure', '收件人详细地址不能为空', 'S003');
        }
        
        //校验出库单明细
        if (empty($orderLines)) {
            return $this->outputQimen('failure', '出库单明细不能为空', 'S003');
        } else {
        	if (empty($orderLines[0])) {
        		$orderLines = array($orderLines);
        	}
            foreach ($orderLines as $val) {
                if (empty($val['ownerCode'])) {
                    return $this->outputQimen('failure', '出库单明细中货主不能为空', 'S003');
                } 
                if (empty($val['itemCode'])) {
                    return $this->outputQimen('failure', '明细中商品编码不能为空', 'S003');
                }
                if (empty($val['planQty']) && $val['planQty'] != 0) {
                    return $this->outputQimen('failure', '明细中应发商品数量不能为空', 'S003');
                }
            }
        }
        return $this->outputQimen('success');
    }
}