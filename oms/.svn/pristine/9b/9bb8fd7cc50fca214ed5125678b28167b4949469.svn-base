<?php
/**
 * 商品备案(货品)信息获取
 * Created by PhpStorm.
 * User: xl
 * Date: 2018-01-10
 * Time: 10:00
 */

require_once API_ROOT . '/router/interface/wms/custom/common/wmsRequest.php';

class wmsKjSkuInterface extends wmsRequest
{

    public function create($param)
    {

        try {
            if (empty($param)) {
                return $this->msgObj->outputCustom(false, '失败:请求数据为空');
            }


            # 转发商品备案数据给WMS
            $response = $this->send();

            # 检测数据转发是否成功
            if (!$response['success']) {
                return $this->msgObj->outputCustom(false, $response['reasons'], $response['addon']);
            }


            # 检测货品备案是否已存在，（根据商品编号代码skuKey 存在更新，不存在新增）
            global $db;
            $sql = "SELECT sku FROM t_base_product WHERE sku = '{$param['skuKey']}'";
            $model = $db->prepare($sql);
            $model->execute();
            $res = $model->fetch(PDO::FETCH_ASSOC);

            # 商品编码代码不存在，直接插入数据库
            if (empty($res)) {

                $sql = 'INSERT INTO t_base_product (customer_id, warehouse_code, sku, descr_c,
                                                stock_unit, gross_weight, hs_code, firstunit_code,
                                                firstqty)
                                         VALUE (:customer_id, :warehouse_code, :sku, :descr_c,
                                                :stock_unit, :gross_weight, :hs_code, :firstunit_code,
                                                :firstqty)';

                $data = $this->dataManage($param);

                $model = $db->prepare($sql);

                # 数据绑定并执行入库操作
                if ($model->execute($data)) {

                    return $this->msgObj->outputCustom(true, '商品备案并入库成功', $response['addon']);
                }

                return $this->msgObj->outputCustom(false, '商品备案成功，单入库失败');

            } else {

                # 根据商品编码代码，更新对应货品信息
                $sql = 'UPDATE t_base_product SET customer_id = :customer_id,
                                            warehouse_code = :warehouse_code,
                                            descr_c = :descr_c,
                                            stock_unit = :stock_unit,
                                            gross_weight = :gross_weight,
                                            hs_code = :hs_code,
                                            firstunit_code = :firstunit_code,
                                            firstqty = :firstqty
                            WHERE sku = :sku';

                $data = $this->dataManage($param);

                $model = $db->prepare($sql);

                # 数据绑定并执行更新操作
                if ($model->execute($data)) {

                    return $this->msgObj->outputCustom(true, '商品备案并更新数据库信息成功', $response['addon']);
                }

                return $this->msgObj->outputCustom(false, '商品备案成功，但更新数据库信息失败');

            }

        } catch (Exception $e) {

            return $this->msgObj->outputCustom(false, $e->getMessage());
        }

    }

    /**
     * 数据组合整理
     * @param $param
     * @return array
     */
    public function dataManage($param)
    {
        $data = array(
            ':customer_id' => $param['storer'],
            ':warehouse_code' => $param['wmwhseid'],
            ':sku' => $param['skuKey'],
            ':descr_c' => $param['sku'],
            ':stock_unit' => $param['uom'],
            ':gross_weight' => $param['swt'],
            ':hs_code' => $param['hsNumber'],
            ':firstunit_code' => $param['firstunitcode'],
            ':firstqty' => $param['firstqty']
        );

        return $data;

    }
}