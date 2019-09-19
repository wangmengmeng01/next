<?php
/**
 * @User: [cf]
 * @DateTime: 2017/7/14 16:27
 * @description:  wms ->erp 商品查询接口参数校验
 */

class filterItemQuery extends msg{
    public  function create($requestData){
        //货主id


        if(!isset($requestData['providerTpId']) || !$requestData['providerTpId']){

            return $this->outputCnStorage(false,'货主id不能为空','s003');

        }

         return $this->outputCnStorage(true,'','');
    }
}