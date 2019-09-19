<?php
/**
 * 订单取消接口过滤类
 * User: Renee
 * Date: 2018/1/18
 * Time: 15:43
 */
class filterKjSoOrderCancel extends msg{
    public function cancel(&$requestData){
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputCustom(false,'请求报文不能为空！');
        }
        //校验申报单号
        if (empty($requestData['externalNo'])) {
            return $this->outputCustom(false,'申报单号不能为空！');
        }
        //校验电商订单号
        if (empty($requestData['externalNo2'])) {
            return $this->outputCustom(false,'电商订单号不能为空！');
        }
        //校验货主代码
        if (empty($requestData['storer'])) {
            return $this->outputCustom(false,'货主代码不能为空！');
        }
        //校验仓库代码
        if (empty($requestData['wmwhseid'])) {
            return $this->outputCustom(false,'仓库代码不能为空！');
        }

        //校验通过
        return $this->outputCustom(true,'成功');
    }
}