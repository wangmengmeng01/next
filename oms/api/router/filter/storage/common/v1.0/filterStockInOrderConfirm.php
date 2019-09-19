<?php
/**
 * WMS入库单确认接口信息校验
 */

class filterStockInOrderConfirm extends msg
{

    /**
     * 信息校验
     * @param $requestData
     * @return array
     */
    public function confirm(&$requestData)
    {
        //校验数据是否为空
        if(empty($requestData)) {
            return $this->outputCnStorage(false, 'body中数据不能为空', 'S003');
        }
        $request = $requestData;

        //仓库订单编码
        if(empty($request['orderCode'])) {
            return $this->outputCnStorage(false, '仓库订单编码不能为空', 'S003');
        }
        //单据类型
        if(empty($request['orderType'])) {
            return $this->outputCnStorage(false, '单据类型不能为空', 'S003');
        }
        //外部业务编码
        if(empty($request['outBizCode'])) {
            return $this->outputCnStorage(false, '外部业务编码不能为空', 'S003');
        }
        //支持出入库单多次确认
        if(empty($request['confirmType']) && $request['confirmType'] !== '0') {
            return $this->outputCnStorage(false, '出入库单多次确认信息不能为空', 'S003');
        }
        //仓库订单完成时间
        if(empty($request['orderConfirmTime'])) {
            return $this->outputCnStorage(false, '仓库订单完成时间不能为空', 'S003');
        }

        //订单商品校验信息列表字段校验
        /*if(empty($request['checkItems'])) {
            return $this->outputCnStorage('failure', '订单商品校验信息列表不能为空', 'S003');
        }
        foreach ($request['checkItems'] as $key => $item) {
            $i = $key+1;
            //订单商品校验信息
            foreach($item as $k => $v) {
                //入库单明细id
                if($k=='orderItemId') {
                    if(empty($v)) {
                        return $this->outputCnStorage('failure', "第{$i}个商品校验信息中 入库单明细id不能为空", 'S003');
                    }
                }
                //该商品实际出入库总量
                if($k=='quantity') {
                    if(empty($v)) {
                        return $this->outputCnStorage('failure', "第{$i}个商品校验信息中 该商品实际出入库总量不能为空", 'S003');
                    }
                }
            }
        }*/

        //订单商品信息列表
        if(empty($request['orderItems'])) {
            return $this->outputCnStorage(false, '订单商品信息列表不能为空', 'S003');
        }

        foreach ($request['orderItems']['orderItem'] as $key => $value) {
            if(is_array($value) && $key !=='items') {
                $msg = $this->switchFun($value);
                if($msg) {
                    return $this->outputCnStorage(false,$msg,'S003');
                }
            } else {
                $msg = $this->switchFun($request['orderItems']['orderItem']);
                if($msg) {
                    return $this->outputCnStorage(false,$msg,'S003');
                }
                break;
            }
        }

        return $this->outputCnStorage(true,'成功','');
    }

    /**
     * 子信息必填字段判定
     * @param $param
     * @return array
     */
    protected function switchFun($param) {

        foreach ($param as $key => $item) {
            switch($key) {
                case 'orderItemId':
                    //商品ID
                    if(empty($item)) {
                        return "商品ID不能为空";
                    }
                    break;
                /*case 'weight':
                    //sku重量不能为空
                    if(empty($item) && $item !==0 ) {
                        return "sku重量不能为空";
                    }
                    break;
                case 'volume':
                    //商品体积不能为空
                    if(empty($item) && $item !==0) {
                        return "商品体积不能为空";
                    }
                    break;
                case 'length':
                    //商品长度不能为空
                    if(empty($item) && $item !==0) {
                        return "商品长度不能为空";
                    }
                    break;
                case 'width':
                    //商品宽度不能为空
                    if(empty($item) && $item !==0) {
                        return "商品宽度不能为空";
                    }
                    break;
                case 'height':
                    //商品高度不能为空
                    if(empty($item) && $item !==0) {
                        return "商品高度不能为空";
                    }
                    break;*/
                case 'items':
                    //商品列表信息

                    if(empty($item) || !isset($item['item'])) {
                        return "商品列表信息不能为空";
                    }

                    foreach ($item['item'] as $l => $list) {
                        if(is_array($list)) {

                            if(empty($list['inventoryType'])) {
                                return "库存类型不能为空";
                            }
                            if(empty($list['quantity'])) {
                                return "商品数量不能为空";
                            }

                        } else {

                            if(empty($item['item']['inventoryType'])) {
                                return "库存类型不能为空";
                            }

                            //数量
                            if(empty($item['item']['quantity'])) {
                                return "商品数量不能为空";
                            }

                            break;

                        }
                    }
                    break;
                default:
                    break;
            }
        }

        return false;
    }
}