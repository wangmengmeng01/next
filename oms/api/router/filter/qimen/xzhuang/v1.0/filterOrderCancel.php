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
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
        }
        $request = $requestData;
        
        //校验仓库
        if (empty($request['warehouseCode'])) {
            return $this->outputQimen('failure', '仓库编码不能为空', 'S003');
        }
        
        qimen_service::$_warehouseCode = $request['warehouseCode'];
        
        //校验单据号
        if (empty($request['orderCode'])) {
            return $this->outputQimen('failure', '单据编码不能为空', 'S003');
        } 
        
		return $this->outputQimen('success');
    }

}