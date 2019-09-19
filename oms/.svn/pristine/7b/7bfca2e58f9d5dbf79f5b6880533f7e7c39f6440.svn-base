<?php
/**
 * 考拉入库单回传接口过滤类
 * User: Renee
 * Date: 2018/5/10
 * Time: 13:30
 */
class filterAsnBack extends msg{
    public function back(&$requestData)
    {
        //判断请求数据不能为空
        if (empty($requestData)) {
            return $this->outputKaola(false,'请求数据为空！');
        }

        //校验入库单号
        if (empty($requestData['inbound_id'])) {
            return $this->outputKaola(false,'入库单号不能为空！');
        }

        return $this->outputKaola(true,'校验成功！');
    }
}