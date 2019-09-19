<?php
/**
 * 考拉出库单推送接口（新）接口过滤类
 * User: Renee
 * Date: 2018/5/15
 * Time: 19:57
 */
class filterSoCreate extends msg{
    public function create(&$requestData)
    {
        //判断请求数据不能为空
        if (empty($requestData)) {
            return $this->outputKaola(false,'请求数据为空！');
        }

        //校验出库单号不能为空
        if (empty($requestData['outbound_id'])) {
            return $this->outputKaola(false,'出库单号不能为空！');
        }

        //校验出库单联系人，联系电话，联系地址
        if (isset($requestData['is_dispatched']) && $requestData['is_dispatched']==1) {
            if (empty($requestData['contacts'])) {
                return $this->outputKaola(false,'出库单联系人不能为空！');
            }
            if (empty($requestData['phone'])) {
                return $this->outputKaola(false,'出库单联系电话不能为空！');
            }
            if (empty($requestData['address'])) {
                return $this->outputKaola(false,'出库单联系地址不能为空！');
            }
        }

        //校验出库单明细不能为空
        if (empty($requestData['items'])) {
            return $this->outputKaola(false,'出库单明细不能为空！');
        }

        return $this->outputKaola(true,'校验成功！');
    }
}