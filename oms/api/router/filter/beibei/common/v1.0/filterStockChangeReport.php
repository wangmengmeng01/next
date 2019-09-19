<?php
/**
 * Description: 贝贝天舟-库存异动接口
 * User: XL
 * Date: 2019/6/19 0019 13:17
 */

class filterStockChangeReport extends msg
{
    public function report($requestData)
    {
        if (empty($requestData)) {
            return $this->outputBeibei(false, '', 'data中数据不能为空');
        }

        $request = $requestData['stockChangeReport'];

        if (empty($request['billId'])) {
            return $this->outputBeibei(false, '', '单据编号不能为空');
        }

        if (empty($request['billType'])) {
            return $this->outputBeibei(false, '', '单据类型不能为空');
        }

        foreach ($request['items'] as $key => $item) {
            $i = $key + 1;

            if (empty($item['operator'])) {
                return $this->outputBeibei(false, '', '第'.$i.'个详情中，操作人不能为空');
            }

            if (empty($item['sku'])) {
                return $this->outputBeibei(false, '', '第'.$i.'个详情中，sku不能为空');
            }

            if (empty($item['skuDesc'])) {
                return $this->outputBeibei(false, '', '第'.$i.'个详情中，商品名称不能为空');
            }

            if (empty($item['quantity']) && $item['quantity'] !== 0) {
                return $this->outputBeibei(false, '', '第'.$i.'个详情中，数量不能为空');
            }

            if (empty($item['lot'])) {
                return $this->outputBeibei(false, '', '第'.$i.'个详情中，入库编号不能为空');
            }

            if (empty($item['inventoryStatus'])) {
                return $this->outputBeibei(false, '', '第'.$i.'个详情中，良品不能为空');
            }
            if (empty($item['company'])) {
                return $this->outputBeibei(false, '', '第'.$i.'个详情中，货主不能为空');
            }
        }

        return $this->outputBeibei(true, '', '成功');

    }
}