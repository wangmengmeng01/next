<?php
/**
 * Description: SKU效期信息回调接口
 * Date: 2018-05-23 13:58
 * Created by XL.
 */

class filterSkuPeriod extends msg
{

    public function period($params)
    {

        if (empty($params)) {

            return $this->outputKaola(false, '请求数据不能为空');
        }

        # 有效期信息
        if (empty($params['exp_infos'])) {

            return $this->outputKaola(false, '效期信息不能为空');
        }

        $periodInfo = $params['exp_infos'];

        foreach ($periodInfo as $k => $value) {

            $line = $k + 1;

            # skuId
            if (empty($value['sku_id'])) {

                return $this->outputKaola(false, '效期信息第' . $line . '个，skuId不能为空');
            }

            # PN码
            if (empty($value['pn_id'])) {

                return $this->outputKaola(false, '效期信息第' . $line . '个，PN码不能为空');
            }

            # 仓库库位号
            if (empty($value['stock_position'])) {

                return $this->outputKaola(false, '效期信息第' . $line . '个，仓库库位号不能为空');
            }

            # 仓库库位类型
            if (empty($value['stock_kind'])) {

                return $this->outputKaola(false, '效期信息第' . $line . '个，仓库库位类型不能为空');
            }

            # 上架时间
            if (empty($value['online_date'])) {

                return $this->outputKaola(false, '效期信息第' . $line . '个，上架时间不能为空');
            }

            # 批次号
            if (empty($value['batch_id'])) {

                return $this->outputKaola(false, '效期信息第' . $line . '个，批次号不能为空');
            }

            # 采购单号
            if (empty($value['purchase_id'])) {

                return $this->outputKaola(false, '效期信息第' . $line . '个，采购单号不能为空');
            }

            # 生成日期
            if (empty($value['production_date'])) {

                return $this->outputKaola(false, '效期信息第' . $line . '个，生成日期不能为空');
            }

            # 过期日期
            if (empty($value['expiration_date'])) {

                return $this->outputKaola(false, '效期信息第' . $line . '个，过期日期 不能为空');
            }

            # 有效期天数
            if (empty($value['expiration_time']) && $value['expiration_time'] !== 0) {

                return $this->outputKaola(false, '效期信息第' . $line . '个，有效期天数不能为空');
            }

            # 预警期天数
            if (empty($value['warn_time']) && $value['warn_time'] !== 0) {

                return $this->outputKaola(false, '效期信息第' . $line . '个，预警期天数不能为空');
            }

            # 临保下架期
            if (empty($value['offline_time']) && $value['offline_time'] !== 0) {

                return $this->outputKaola(false, '效期信息第' . $line . '个，临保下架期不能为空');
            }

            # 当前良品数
            /*if (empty($value['good_num']) && $value['good_num'] !== 0) {

                return $this->outputKaola(false, '效期信息第' . $line . '个，当前良品数不能为空');
            }

            # 当前次品数
            if (empty($value['bad_num']) && $value['bad_num'] !== 0) {

                return $this->outputKaola(false, '效期信息第' . $line . '个，当前次品数不能为空');
            }

            # 当前库存总数
            if (empty($value['total_num']) && $value['total_num'] !== 0) {

                return $this->outputKaola(false, '效期信息第' . $line . '个，当前库存总数不能为空');
            }

            # 本次入库时的良品数量(实际理货数据)
            if (empty($value['purchase_good_num']) && $value['purchase_good_num'] !== 0) {

                return $this->outputKaola(false, '效期信息第' . $line . '个，本次入库时的良品数量(实际理货数据)不能为空');
            }

            # 本次入库时的次品数量(实际理货数据)
            if (empty($value['purchase_bad_num']) && $value['purchase_bad_num'] !== 0) {

                return $this->outputKaola(false, '效期信息第' . $line . '个，本次入库时的次品数量(实际理货数据) 不能为空');
            }

            # 本次入库时的总数量(实际理货数据)
            if (empty($value['purchase_total_num']) && $value['purchase_total_num'] !== 0) {

                return $this->outputKaola(false, '效期信息第' . $line . '个，本次入库时的总数量(实际理货数据)不能为空');
            }*/
        }

        return $this->outputKaola(true, '');

    }
}