<?php
/**
 * @User: [cf]
 * @DateTime: 2017/7/23 11:14
 * @description:
 */

class filterInventoryAdjustUpload extends msg{
    public  function create($requestData){


        if(!isset($requestData['storeCode']) || !$requestData['storeCode']){
            return $this->outputCnStorage(false,'仓库编码不能为空','s003');

        }

        if(!isset($requestData['ownerUserId']) || !$requestData['ownerUserId']){
            return $this->outputCnStorage(false,'货主id不能为空','s003');

        }

        if(!isset($requestData['outBizCode']) || !$requestData['outBizCode']){
            return $this->outputCnStorage(false,'外部业务编码不能为空','s003');

        }

        return $this->outputCnStorage(true,'','');
    }
}