<?php
/**
 * 报检单接口过滤类
 * User: Renee
 * Date: 2018/1/18
 * Time: 15:40
 */
class filterKjGjRqLock extends msg{
    public function lock(&$requestData){
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputCustom(false,'请求报文不能为空！');
        }
        //校验业务码
        if (empty($requestData['serialCode'])) {
            return $this->outputCustom(false,'业务码不能为空！');
        }
        //校验货主编码
        if (empty($requestData['storer'])) {
            return $this->outputCustom(false,'货主编码不能为空！');
        }
        //校验报检单号
        if (empty($requestData['declNo'])) {
            return $this->outputCustom(false,'报检单号不能为空！');
        }
        //校验商品编码
        if (empty($requestData['sku'])) {
            return $this->outputCustom(false,'商品编码不能为空！');
        }
        //校验锁定状态
        if (!isset($requestData['flag'])) {
            return $this->outputCustom(false,'锁定状态不能为空！');
        }
        //校验仓库代码
        if (empty($requestData['wmwhseid'])) {
            return $this->outputCustom(false,'仓库代码不能为空！');
        }
        //校验通过
        return $this->outputCustom(true,'成功');
    }
}