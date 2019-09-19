<?php
/**
 * 仓储普通入库单下发接口信息校验
 */

class filterStockInOrderNotify extends msg {

    /**
     * 字段校验
     * @param $requestData
     * @return array
     */
    public function create(&$requestData) {

        //校验数据是否为空
        if (empty($requestData)) {
                return $this->outputCnStorage(false, 'body中数据不能为空', 'S003');
        }
        $request = $requestData;
        //货主ID
        if(empty($request['ownerUserId'])) {
            return $this->outputCnStorage(false, '货主ID不能为空', 'S003');
        }
        //仓储编码
        if(empty($request['storeCode'])) {
            return $this->outputCnStorage(false, '仓储编码不能为空', 'S003');
        }
        //仓储中心订单编码
        if(empty($request['orderCode'])) {
            return $this->outputCnStorage(false, '仓储中心订单编码不能为空', 'S003');
        }
        //ERP订单号ID
//         if(empty($request['erpOrderCode'])) {
//             return $this->outputCnStorage(false, 'ERP订单号ID不能为空', 'S003');
//         }
        //单据类型
        if(empty($request['orderType'])) {
            return $this->outputCnStorage(false, '单据类型不能为空', 'S003');
        }
        //订单来源
        if(empty($request['orderSource'])) {
            return $this->outputCnStorage(false, '订单来源不能为空', 'S003');
        }
        //订单创建时间
        if(empty($request['orderCreateTime'])) {
            return $this->outputCnStorage(false, '订单创建时间不能为空', 'S003');
        }
        /*
        //发件方信息
        if(empty($request['senderInfo'])) {
            return $this->outputCnStorage(false, '发件方信息不能为空', 'S003');
        }
        //发件方邮编
        if(empty($request['senderInfo']['senderZipCode'])) {
            return $this->outputCnStorage(false, '发件方邮编不能为空', 'S003');
        }
        //发件方省份
        if(empty($request['senderInfo']['senderProvince'])) {
            return $this->outputCnStorage(false, '发件方省份不能为空', 'S003');
        }
        //发件方城市
        if(empty($request['senderInfo']['senderCity'])) {
            return $this->outputCnStorage(false, '发件方城市不能为空', 'S003');
        }
        //收发件方名称:（采购入库放供应商名称），（销退填买家名称），（调拨入库填写仓库名称）
        if(empty($request['senderInfo']['senderName'])) {
            return $this->outputCnStorage(false, '发件方名称不能为空', 'S003');
        }
        //发件方手机
        if(empty($request['senderInfo']['senderMobile'])) {
            return $this->outputCnStorage(false, '发件方手机不能为空', 'S003');
        }
        */
        //订单商品信息列表
        if(empty($request['orderItemList']) || !isset($request['orderItemList']['orderItem'])) {
            return $this->outputCnStorage(false, '订单商品信息列表不能为空', 'S003');
        }
        //订单商品信息列表中字段信息校验
        foreach ($request['orderItemList']['orderItem'] as $key => $value) {

            if(is_array($value)) {
                $msg = $this->switchFun($value);
                if($msg) {
                    return $this->outputCnStorage(false, $msg, 'S003');
                }
            } else {
                $msg = $this->switchFun($request['orderItemList']['orderItem']);
                if($msg) {
                    return $this->outputCnStorage(false, $msg, 'S003');
                }

            }
        } unset($value);

        //装箱列表字段信息校验
        /*if(empty($request['caseInfoList']) || !isset($request['caseInfoList']['wmsStockInCaseInfo'])) {
            return $this->outputCnStorage(false, '装箱列表字段信息不能为空', 'S003');
        }*/
        //装箱列表字段信息校验
        if(!empty($request['caseInfoList']) && isset($request['caseInfoList']['wmsStockInCaseInfo'])) {

            foreach ($request['caseInfoList']['wmsStockInCaseInfo'] as $key => $value) {

                //装箱对象
                if(is_array($value) && $key !== 'caseItemList') {

                    //多组装箱列表

                    $this->caseFun($value);

                } else {

                    $this->caseFun($request['caseInfoList']['wmsStockInCaseInfo']);

                    break;
                }
            }
        }



        return $this->outputCnStorage(true,'成功','');

    }

    /**
     * 子类目必填项校验
     * @param $param
     * @return bool|string
     */
    protected function switchFun($param)
    {

        if(!isset($param['orderItemId']) || empty($param['orderItemId'])) {
            //ERP主键ID
            return "ERP主键ID不能为空";
        }

        /*if(!isset($param['orderSourceCode']) || empty($param['orderSourceCode'])) {
            //平台交易编码：销退入库由ECP填充原平台销售订单，采购入库填写采购订单号，调拨入库填写调拨单号
            return "平台交易编码不能为空";
        }
        if(!isset($param['subSourceCode']) || empty($param['subSourceCode'])) {
            //平台子交易编码
            return "平台子交易编码不能为空";
        }*/
        if(!isset($param['ownerUserId']) || empty($param['ownerUserId'])) {
            //货主ID 代销情况下货主ID和卖家ID不同(销售场景下：代销业务中货主ID和卖家ID不同)
            return "货主ID不能为空";
        }
        /*if(!isset($param['itemId']) || empty($param['itemId'])) {
            //商品ID
            return "商品ID不能为空";
        }*/
        if(!isset($param['itemName']) || empty($param['itemName'])) {
            //商品名称
            return "商品名称不能为空";
        }
        if(!isset($param['itemCode']) || empty($param['itemCode'])) {
            //商家对商品的编码不能为空
            return "商家对商品的编码不能为空";
        }
        if(!isset($param['inventoryType']) || empty($param['inventoryType'])) {
            //库存类型
            return "库存类型不能为空";
        }
        if(!isset($param['itemQuantity']) || empty($param['itemQuantity'])) {
            //商品数量不能为空
            return "商品数量不能为空";
        }
        /* if(!isset($param['itemVersion']) || empty($param['itemVersion'])) {
            //商品版本号不能为空
            return "商品版本号不能为空";
        } */
        /*if(!isset($param['batchCode']) || empty($param['batchCode'])) {
            //批次号不能为空
            return "批次号不能为空";
        }
        if(!isset($param['dueDate']) || empty($param['dueDate'])) {
            //到货日期
            return "到货日期不能为空";
        }
        if(!isset($param['produceDate']) || empty($param['produceDate'])) {
            //生产日期不能为空
            return "生产日期不能为空";
        }
        if(!isset($param['produceCode']) || empty($param['produceCode'])) {
            //生产编码，同一商品可能因商家不同有不同编码
            return "生产编码不能为空";
        }*/

        return false;
    }


    /**
     * 装箱列表比传字段校验
     * @param $param
     * @return array
     */
    protected function caseFun($param) {

        if(empty($param['caseItemList']) || !isset($param['caseItemList']['wmsStockInCaseItem'])) {
            return $this->outputCnStorage(false,"装箱对象的装箱明细列表不能为空", 'S003');
        }

        foreach($param['caseItemList']['wmsStockInCaseItem'] as $k => $v) {
            if(is_array($v)) {
                $msg = $this->itemFun($v);
                if($msg) {
                    return $this->outputCnStorage(false, $msg, 'S003');
                }
            } else {
                $msg = $this->itemFun($param['caseItemList']['wmsStockInCaseItem']);
                if($msg) {
                    return $this->outputCnStorage(false, $msg, 'S003');
                }
                break;
            }
        }
    }

    /**
     * 装箱明细列表中必传字段校验
     * @param $list
     * @return bool|string
     */
    protected function itemFun($list) {

        if(empty($list['itemId'])) {
            return "装箱明细列表中 商品ID不能为空";
        }

        if(empty($list['inventoryType'])) {
            return "装箱明细列表中 库存类型不能为空";
        }
        if(empty($list['itemQuantity'])) {
            return "装箱明细列表中 商品数量不能为空";
        }

        return false;
    }
}