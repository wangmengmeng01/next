<?php
/**
 * 仓库无库存反馈接口过滤类
 * User: Renee
 * Date: 2018/1/18
 * Time: 15:46
 */
class filterKjLackGoods extends msg{
    public function lack(&$requestData){
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputCustom(false,'请求报文不能为空！');
        }
        //校验通过
        return $this->outputCustom(true,'成功');
    }
}