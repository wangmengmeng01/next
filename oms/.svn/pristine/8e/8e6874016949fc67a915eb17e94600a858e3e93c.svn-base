<?php

/**
 * 仓内加工单创建过滤类
 *
 */
class filterStoreProcessCreate extends msg
{

    /**
     * 过滤仓内加工单创建订单数据
     * @param  $requestData         
     * @return array
     *
     */
    public function create(&$requestData)
    {
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
        }
        $request = $requestData;
        $materialItem = $request['materialitems']['item'];
        $productItem = $request['productitems']['item'];
        
        //校验加工单编码
        if (empty($request['processOrderCode'])) {
            return $this->outputQimen('failure', '加工单编码不能为空', 'S003');
        }
        //校验仓库
        if (empty($request['warehouseCode'])) {
            return $this->outputQimen('failure', '仓库编码不能为空', 'S003');
        } 
        //校验单据类型
        if (empty($request['orderType'])) {
            return $this->outputQimen('failure', '单据类型不能为空', 'S003');
        }
        //校验加工单创建时间
        if (empty($request['orderCreateTime'])) {
            return $this->outputQimen('failure', '加工单创建时间不能为空', 'S003');
        }
        //校验计划加工时间
        if (empty($request['planTime'])) {
            return $this->outputQimen('failure', '计划加工时间不能为空', 'S003');
        }
        //校验加工类型
        if (empty($request['serviceType'])) {
            return $this->outputQimen('failure', '加工类型不能为空', 'S003');
        }
        //校验材料明细
        if (empty($materialItem)) {
            return $this->outputQimen('failure', '仓内加工单材料明细不能为空', 'S003');
        } else {
            if (empty($materialItem[0])) {
                $materialItem = array($materialItem);
            }
            foreach ($materialItem as $m_v) {
                if (empty($m_v['itemCode'])) {
                    return $this->outputQimen('failure', '仓内加工单材料明细中系统商品编码不能为空', 'S003');
                }
                if (empty($m_v['quantity'])) {
                    return $this->outputQimen('failure', '仓内加工单材料明细中数量不能为空', 'S003');
                }
            }
        }
        //校验商品明细
        if (empty($productItem)) {
            return $this->outputQimen('failure', '仓内加工单商品明细不能为空', 'S003');
        } else {
            if (empty($productItem[0])) {
                $productItem = array($productItem);
            }
            foreach ($productItem as $i_v) {
                if (empty($i_v['itemCode'])) {
                    return $this->outputQimen('failure', '仓内加工单商品明细中系统商品编码不能为空', 'S003');
                }
                if (empty($i_v['quantity'])) {
                    return $this->outputQimen('failure', '仓内加工单商品明细中数量不能为空', 'S003');
                }
            }
        }
        return $this->outputQimen('success');
    }
}
