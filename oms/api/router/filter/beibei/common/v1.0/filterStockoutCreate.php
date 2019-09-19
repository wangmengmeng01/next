<?php
/**
 * Description: 贝贝天舟-出库单创建接口
 * User: XL
 * Date: 2019/6/19 0019 16:27
 */

class filterStockoutCreate extends msg
{

    public function create($requestData)
    {
        if (empty($requestData)) {

            return $this->outputBeibei(false, '', 'data中的数据不能为空');
        }

        $data = $requestData;

        if (empty($data['billId'])) {
            return $this->outputBeibei(false, '', '单据编号不能为空');
        }

        if (empty($data['billType'])) {
            return $this->outputBeibei(false, '', '单据类型不能为空');
        }

        if (empty($data['opTime'])) {
            return $this->outputBeibei(false, '', '操作时间不能为空');
        }
        if (empty($data['warehouse'])) {
            return $this->outputBeibei(false, '', '仓库编码不能为空');
        }
        if (empty($data['company'])) {
            return $this->outputBeibei(false, '', '货主不能为空');
        }

        if (empty($data['details'])) {
            return $this->outputBeibei(false, '', '回传详情不能为空');
        }


        foreach ($data['details'] as $k => $item) {

            $i = $k + 1;

            if (empty($item['sku'])) {
                return $this->outputBeibei(false, '', '第'.$i.'个详情中，sku不能为空');
            }
            if (empty($item['lineNo'])) {
                return $this->outputBeibei(false, '', '第'.$i.'个详情中，行号不能为空');
            }
            if (empty($item['quantity']) && $item['quantity'] !== 0) {
                return $this->outputBeibei(false, '', '第'.$i.'个详情中，数量不能为空');
            }
            if (empty($item['inventoryStatus'])) {
                return $this->outputBeibei(false, '', '第'.$i.'个详情中，良品标识不能为空');
            }
        }

        return $this->outputBeibei(true, '', '成功');
    }

}