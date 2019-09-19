<?php
/**
 * Description: 订单推送接口 数据过滤
 * Date: 2018-05-10 11:10
 * Created by XL.
 */

class filterOrderPush extends msg
{

    public function push($params)
    {

        if (empty($params)) {
            return $this->outputKaola(false,'请求数据为空！');
        }

        # 订单号
        if (empty($params['order_id'])) {

            return $this->outputKaola(false, '订单号不能为空');
        }

        /*# 收件人省份
       if (empty($params['receiver_province'])) {

           return $this->outputKaola(false, '收件人省份不能为空');
       }

       /*#证信息
       if (empty($params['receiver_id'])) {

           return $this->outputKaola(false, '身份证信息不能为空');
       }

       # 收件人城市
       if (empty($params['receiver_city'])) {

           return $this->outputKaola(false, '收件人城市不能为空');
       }

       # 收件人区域
       if (empty($params['receiver_county'])) {

           return $this->outputKaola(false, '收件人区域不能为空');
       }*/

        # 收件人地址
        if (empty($params['receiver_address'])) {

            return $this->outputKaola(false, '收件人地址不能为空');
        }

        # 收件人姓名
        if (empty($params['receiver_name'])) {

            return $this->outputKaola(false, '收件人姓名不能为空');
        }

        # 收件人电话
        if (empty($params['receiver_mobile'])) {

            return $this->outputKaola(false, '收件人电话不能为空');
        }

       /* # 收件人手机
        if (empty($params['receiver_phone'])) {

            return $this->outputKaola(false, '收件人手机不能为空');
        }*/

        # 物品里列表
        if (empty($params['order_items'])) {

            return $this->outputKaola(false, '物品列表不能为空');
        }

        $items = $params['order_items'];

        foreach ($items as $k => $item) {

            $line = $k + 1;

            # 商品编码
            if (empty($item['sku_id'])) {

                return $this->outputKaola(false, '第' . $line . '物品的商品编码sku_id不能为空');
            }
        }
        return $this->outputKaola(true,'');
    }
}