<?php
/**
 * Notes:贝贝是否允许发货过滤类
 * Date: 2019/6/19
 * Time: 16:39
 */

class filterSoDeliver extends msg
{
    public function judge(&$requestData)
    {
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputBeibei(false, '', 'body中数据不能为空');
        }
        if (empty($requestData['shippingNo'])) {
            return $this->outputBeibei(false, '', '包裹单号不能为空');
        }
        return $this->outputBeibei(true);
    }
}