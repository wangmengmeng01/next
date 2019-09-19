<?php
/**
 * @User: [cf]
 * @DateTime: 2017/7/12 11:21
 * @description: 商品信息通知
 */

class filterSkuInfoNotify extends msg{

    /*
     *
     *必填字段校验
     *
     */
    public function create($requestData){
        if(!$requestData['ownerUserId']){
           return $this->outputCnStorage(false,'必填字段ownerUserId不能为空','S001');
        }

        if(!$requestData['itemCode']){
           return  $this->outputCnStorage(false,'必填字段itemCode不能为空','S001');
        }

        if(!$requestData['barCode']){
           return  $this->outputCnStorage(false,'必填字段barCode不能为空','S001');
        }

        if(!$requestData['actionType']){
           return  $this->outputCnStorage(false,'必填字段actionType不能为空','S001');
        }

        if(!$requestData['itemVersion']){
           return  $this->outputCnStorage(false,'必填字段itemVersion不能为空','S001');
        }

        if(!$requestData['type']){
           return   $this->outputCnStorage(false,'必填字段type不能为空','S001');
        }

        return $this->outputCnStorage(true,'成功','');

    }


}