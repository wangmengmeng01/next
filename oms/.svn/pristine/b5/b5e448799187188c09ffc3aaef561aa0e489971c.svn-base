<?php
/**
 * 考拉入库单入库确认接口过滤类
 * User: Renee
 * Date: 2018/5/9
 * Time: 16:04
 */

class filterAsnConfirm extends msg{
    public function confirm(&$requestData)
    {
        //判断请求数据不能为空
        if (empty($requestData)) {
            return $this->outputKaola(false,'请求数据为空！');
        }

        //校验入库单号不能为空
        if (empty($requestData['inbound_id'])) {
            return $this->outputKaola(false,'入库单号不能为空！');
        }

        return $this->outputKaola(true,'校验成功！');
    }
}