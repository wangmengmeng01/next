<?php
/**
 * @User: [cf]
 * @DateTime: 2017/7/18 17:27
 * @description: wms ->菜鸟库存盘点接口参数校验
 *
 */


class filterInventoryCount extends msg{
    public  function create($requestData){


        if(!isset($requestData['orderType']) || !$requestData['orderType']){
            return $this->outputCnStorage(false,'订单类型不能为空','s003');
        }

        if(!isset($requestData['ownerUserId']) || !$requestData['ownerUserId']){
            return $this->outputCnStorage(false,'货主id不能为空','s003');
        }

        if(!isset($requestData['outBizCode']) || !$requestData['outBizCode']){
            return $this->outputCnStorage(false,'外部业务编码不能为空','s003');


        }

        if(!isset($requestData['operateTime']) || !$requestData['operateTime']){
            return $this->outputCnStorage(false,'盘点时间不能为空','s003');

        }

        return $this->outputCnStorage(true,'成功','');
    }
}