<?php
/**
 * 入库单接口 cnec_wh_4
 * Created by PhpStorm.
 * User: xl
 * Date: 2018-01-10
 * Time: 10:23
 */

require_once API_ROOT . 'router/interface/wms/custom/common/wmsRequest.php';

class wmsKjAsnInterface extends wmsRequest
{
    public function create(&$param)
    {

        try {

            # 数据校验
            if (empty($param)) {
                return $this->msgObj->outputCustom(false, '错误：请求数据为空');
            }

            # 转发入库信息
            $response = $this->send();

            # 检测转发结果
            if (!$response['success']) {
                return $this->msgObj->outputCustom(false, $response['reasons'], $response['addon']);
            }

            # 转发成功，存入数据库
            global $db;

            # 检测单号是否已存在，存在则作废之前的订单，再新增。否则直接
            $sql = 'SELECT order_id FROM oms.t_inbound_info WHERE order_no = :order_no AND is_valid = 1';

            $model = $db->prepare($sql);
            $model->bindParam(':order_no', $param['externalNo']);
            $model->execute();
            $res = $model->fetch(PDO::FETCH_ASSOC);

            # 开始事务
            $db->beginTransaction();

            if (!empty($res)) {

                $sql_update = "UPDATE oms.t_inbound_info SET is_valid = 0, operate_time = now() WHERE order_id = {$res['order_id']}";

                $model_update = $db->prepare($sql_update);

                $res_update = $model_update->execute();

            }

            $sql = 'INSERT INTO t_inbound_info
                          (order_no,external_no2,order_type,customer_id,warehouse_code,total_order_lines,bill_date,remark,create_time)
                    VALUE (:externalNo, :externalNo2, :receipType, :storer, :wmwhseid, :tdq, :bill_date, :remark, now());';


            $model = $db->prepare($sql);
            $model->bindParam(':externalNo', $param['externalNo']);
            $model->bindParam(':externalNo2', $param['externalNo2']);
            $model->bindParam(':receipType', $param['receipType']);
            $model->bindParam(':storer', $param['storer']);
            $model->bindParam(':wmwhseid', $param['wmwhseid']);
            $model->bindParam(':tdq', $param['tdq']);
            $model->bindParam(':bill_date', $param['billDate']);
            $model->bindParam(':remark', $param['remark']);

            $res_insert = $model->execute();

            # 事务回滚
            if (!$res_insert && (isset($res_update) && !$res_update)) {

                $db->rollBack();
            }

            # 获取最后插入的数据对应id
            $lastId = $db->lastInsertId();

            # 事务提交
            $db->commit();

            # 入库单信息存入t_inbound_info表中
            # 入库单详情信息存入 t_inbound_detail （商品入库明细表）
            # 入库单详情信息预处理数据整理
            $values = '';

            if (empty($param['item'][0])) {
                $param['item'] = array($param['item']);
            }
            foreach ($param['item'] as $value) {
                if (!empty($value)) {

                    /*# values
                    $values .= "({$lastId}, :customer_id, :sku, :decl_no, :expected_qty, :uom, :currency_value, :currency),";

                    # 实际数据datas  数据绑定
                    $datas[$key][':customer_id'] = $param['storer'];
                    $datas[$key][':sku'] = $value['sku'];
                    $datas[$key][':decl_no'] = $value['declNo'];
                    $datas[$key][':expected_qty'] = $value['qty'];
                    $datas[$key][':uom'] = $value['uom'];
                    $datas[$key][':currency_value'] = $value['currencyValue'];
                    $datas[$key][':currency'] = $value['currency'];*/

                    $values .= "({$lastId}, '{$param['storer']}', '{$value['sku']}', '{$value['declNo']}',
                                 '{$value['qty']}', '{$value['uom']}', '{$value['currencyValue']}',
                                 '{$value['currency']}', now()),";
                }
            }

            unset($value);

            # 去除最后一位字符 ','
            $values = rtrim($values, ',');

            # 语句拼接
            $sql = 'INSERT INTO t_inbound_detail
                      (order_id, customer_id, sku, decl_no, expected_qty, uom, currency_value, currency, create_time)
                    VALUES ' . $values;

            $model = $db->prepare($sql);

            if ($model->execute()) {

                return $this->msgObj->outputCustom('success', '入库单下发且数据存入数据库成功', $response['addon']);
            }

            return $this->msgObj->outputCustom(false, '入库单下发成功，但入库单详情存储失败。入库单跨境平台系统采购单号：' . $param['externalNo']);


        } catch (Exception $e) {

            return $this->msgObj->outputCustom(false, $e->getMessage());

        }


    }

}