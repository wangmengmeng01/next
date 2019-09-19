<?php
/**
 * Description: 用户订单出库回调
 * Date: 2018-05-11 14:35
 * Created by XL.
 */

class filterUserOrderOutCallback extends msg
{

    public function back($params)
    {

        if (empty($params)) {

            return $this->outputKaola(false, '请求数据不能为空');
        }

        # 出库单状态
        if (empty($params['order_status'])) {

            return $this->outputKaola(false, '出库单状态不能为空');
        }

        if ($params['order_status'] == 300) {

            # 物流商
            if (empty($params['transport_service_code'])) {

                return $this->outputKaola(false, '发货（300）时,物流商不能为空');
            }

            # 物流号
            if (empty($params['transport_order_id'])) {

                return $this->outputKaola(false, '发货（300）时,物流号不能为空');
            }

            # 物品清单
            if (empty($params['order_items'])) {

                return $this->outputKaola(false, '物品清单不能为空');
            }

            $items = $params['order_items'];


            foreach ($items as $k => $value) {
                $line = $k + 1;

                # 商品编码sku_id
                if (empty($value['sku_id'])) {

                    return $this->outputKaola(false, '物品列表第' . $line . '条信息中，商品编码sku_id不能为空');
                }



            }
        }



        return $this->outputKaola(true, '参数校验通过');

    }
}