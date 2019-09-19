<?php
/**
 * Description: 贝贝天舟-出库单确认接口
 * User: XL
 * Date: 2019/6/19 0019 15:44
 */

class filterStockoutConfirm extends msg
{

    public function confirm($requestData)
    {

        if (empty($requestData)) {
            return $this->outputBeibei(false, '', 'data中数据不能为空');
        }

        $request = $requestData['stockout'];


        if (empty($request['billId'])) {

            return $this->outputBeibei(false, '', '单据号不能为空');
        }
        if (empty($request['billType'])) {

            return $this->outputBeibei(false, '', '单据类型不能为空');
        }
        if (empty($request['opTime'])) {

            return $this->outputBeibei(false, '', '操作时间不能为空');
        }
        if (empty($request['warehouse'])) {

            return $this->outputBeibei(false, '', '仓库编码不能为空');
        }
        if ($request['billType'] == 'SO') {
            if (!isset($request['estimatedWeight'])) {

                return $this->outputBeibei(false, '', '预计重量不能为空');
            }
            if (!isset($request['actualWeight'])) {

                return $this->outputBeibei(false, '', '实际重量不能为空');
            }
        }

        if (empty($request['details'])) {
            return $this->outputBeibei(false, '', '回传详情不能为空');
        }

        foreach ($request['details'] as $key => $item) {

            $i = $key + 1;

            if (empty($item['lineNo'])) {
                return $this->outputBeibei(false, '', '第'.$i.'个详情中，行号不能为空');
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
                return $this->outputBeibei(false, '', '第'.$i.'个详情中，贝贝发起的单据单号不能为空');
            }
            if (empty($item['productionLot'])) {
                return $this->outputBeibei(false, '', '第'.$i.'个详情中，批次号不能为空');
            }
            if (empty($item['inventoryStatus'])) {
                return $this->outputBeibei(false, '', '第'.$i.'个详情中，良品标识不能为空');
            }
            if (empty($item['company'])) {
                return $this->outputBeibei(false, '', '第'.$i.'个详情中，货主不能为空');
            }
            if (empty($item['operator'])) {
                return $this->outputBeibei(false, '', '第'.$i.'个详情中，操作人不能为空');
            }
        }

        return $this->outputBeibei(true, '', '成功');

    }

}