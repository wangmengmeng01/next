<?php
/**
 * 仓库虚拟扫描接口过滤类
 * User: Renee
 * Date: 2018/1/18
 * Time: 15:47
 */
class filterKjFx extends msg{
    public function confirm(&$requestData) {
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputCustom(false,'请求报文不能为空！');
        }
        //校验运单号
        if (empty($requestData['mailNo'])) {
            return $this->outputCustom(false,'运单号不能为空！');
        }
        //校验通过
        return $this->outputCustom(true,'成功');
    }
}