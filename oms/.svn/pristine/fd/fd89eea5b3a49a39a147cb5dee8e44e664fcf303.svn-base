<?php
/**
 * WMS订单状态回传接口 Version:1.0.0
 * Created by PhpStorm.
 * User: xl
 * Date: 2017/7/19
 * Time: 9:19
 */
class filterOrderStatusUpload extends msg
{

    public function upload(&$requestData)
    {
        //校验数据是否为空
        if(empty($requestData)) {
            return $this->outputCnStorage(false, 'body中数据不能为空', 'S003');
        }

        $request = $requestData;
        //仓库订单类型
        if(empty($request['orderType'])) {
            return $this->outputCnStorage(false, '仓库订单类型不能为空', 'S003');
        }
        //仓库订单编码
        if(empty($request['orderCode'])) {
            return $this->outputCnStorage(false, '仓库订单编码不能为空', 'S003');
        }
        //订单状态
        if(empty($request['status'])) {
            return $this->outputCnStorage(false, '订单状态不能为空', 'S003');
        }
        //操作人
        if(empty($request['operator'])) {
            return $this->outputCnStorage(false, '操作人不能为空', 'S003');
        }
        //操作人联系方式
        /*if(empty($request['operatorContact'])) {
            return $this->outputCnStorage(false, '操作人联系方式不能为空', 'S003');
        }*/
        //操作时间
        if(empty($request['operateDate'])) {
            return $this->outputCnStorage(false, '操作时间不能为空', 'S003');
        }

        return $this->outputCnStorage(true,'成功','');
    }
}
