<?php
/**
 * Description: 贝贝天舟-商品同步接口
 * User: XL
 * Date: 2019/6/19 0019 13:17
 */

class filterProductSync extends msg
{
    public function create($requestData)
    {
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputBeibei(false, 'data中数据不能为空');
        }

        $request = $requestData;


        $info = [
            'company' => '货主',
            'sku' => '产品sku',
            'skuDesc' => '商品名称',
            'grossWeight' => '毛重量',
            'netWeight' => '净重量',
            'wmsLength' => '长',
            'wmsWidth' => '宽',
            'wmsHeight' => '高',
            'wmsVolume' => '体积',
            'brand' => '品牌',
            'category' => '货物类型',
            'isShelfLife' => '是否有效期',
            'shelfLifeDays' => '有效期',
            'shelfLifeType' => '周期类型',
            'warehouseInDays' => '入库期',
            'produceType' => '生产类型',
        ];

        foreach ($request as $key => $val) {

            if (!is_numeric($key)) {

                if (key_exists($key, $info) && (empty($val) && $val !== 0)) {

                    return $this->outputBeibei(false, '', $info[$key] . '不能为空');
                }

            } else {

                foreach ($val as $k => $item) {

                    if (key_exists($k, $info) && (empty($item) && $item !== 0)) {

                        return $this->outputBeibei(false, '', $info[$k] . '不能为空');
                    }
                }
            }
        }

        return $this->outputBeibei(true, '', '成功');

        /*//货主
        if (empty($request['company'])) {
            return $this->outputBeibei(false, '货主不能为空');
        }
        //sku
        if (empty($request['sku'])) {
            return $this->outputBeibei(false, 'sku不能为空');
        }
        //商品名称
        if (empty($request['skuDesc'])) {
            return $this->outputBeibei(false, '商品名称不能为空');
        }
        //毛重量
        if (empty($request['grossWeight'])) {
            return $this->outputBeibei(false, '毛重量不能为空');
        }
        //货主
        if (empty($request['netWeight'])) {
            return $this->outputBeibei(false, '净重量不能为空');
        }
        //货主
        if (empty($request['wmsLength'])) {
            return $this->outputBeibei(false, '长不能为空');
        }
        //货主
        if (empty($request['wmsWidth'])) {
            return $this->outputBeibei(false, '宽不能为空');
        }
        //货主
        if (empty($request['wmsHight'])) {
            return $this->outputBeibei(false, '高不能为空');
        }
        //货主
        if (empty($request['wmsVolume'])) {
            return $this->outputBeibei(false, '体积不能为空');
        }
        if (empty($request['brand'])) {
            return $this->outputBeibei(false, '品牌不能为空');
        }
        if (empty($request['category'])) {
            return $this->outputBeibei(false, '货物类型不能为空');
        }
        if (empty($request['isShelfLife'])) {
            return $this->outputBeibei(false, '是否有效期不能为空');
        }
        if (empty($request['shelfLifeDays'])) {
            return $this->outputBeibei(false, '有效期不能为空');
        }
        if (empty($request['shelfLifeType'])) {
            return $this->outputBeibei(false, '周期类型不能为空');
        }
        if (empty($request['warehouseInDays'])) {
            return $this->outputBeibei(false, '入库期不能为空');
        }
        if (empty($request['productType'])) {
            return $this->outputBeibei(false, '生产类型不能为空');
        }


        foreach ($request as $key => $item) {
            $i = $key + 1;
            // 操作人
            if (empty($item['operator'])) {
                return $this->outputBeibei(false, '第'.$i.'个详情中，操作人不能为空');
            }

            //sku
            if (empty($item['sku'])) {
                return $this->outputBeibei(false, '第'.$i.'个详情中，sku不能为空');
            }

            //商品名称
            if (empty($item['skuDesc'])) {
                return $this->outputBeibei(false, '第'.$i.'个详情中，商品名称不能为空');
            }
            //数量
            if (empty($item['quantity'])) {
                return $this->outputBeibei(false, '第'.$i.'个详情中，数量不能为空');
            }
            //入库编号
            if (empty($item['lot'])) {
                return $this->outputBeibei(false, '第'.$i.'个详情中，入库编号不能为空');
            }
            //良品
            if (empty($item['inventoryStatus'])) {
                return $this->outputBeibei(false, '第'.$i.'个详情中，良品不能为空');
            }
        }

        return $this->outputBeibei(true, '成功');*/

    }
}