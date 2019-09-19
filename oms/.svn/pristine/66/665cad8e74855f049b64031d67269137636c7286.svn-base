<?php
/**
 * 考拉出库单出库确认接口过滤类
 * User: Renee
 * Date: 2018/5/16
 * Time: 16:59
 */
class filterSoConfirm extends msg{
    public function confirm(&$requestData)
    {
        //判断请求数据不能为空
        if (empty($requestData)) {
            return $this->outputKaola(false,'请求数据为空！');
        }

        //校验出库单号不能为空
        if (empty($requestData['outbound_id'])) {
            return $this->outputKaola(false,'出库单号不能为空！');
        }

        return $this->outputKaola(true,'校验成功！');
    }
}