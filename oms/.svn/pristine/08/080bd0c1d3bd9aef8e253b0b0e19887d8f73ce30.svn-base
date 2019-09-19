<?php
/**
 * 商品同步接口操作类
 *
 */
require API_ROOT . '/router/interface/wms/beibei/YWMS/wmsRequest.php';

class wmsProductSync extends wmsRequest
{

    /**
     * 创建商品信息
     * @param  $params
     * @return array
     */
    public function create($params)
    {
        try {

            # 参数校验
            if (empty($params)) {
                return $this->msgObj->outputBeibei(false, '', '失败：请求的数据为空');
            }

            # 转发数据给WMS
            $response = $this->send();

            # 接口请求失败
            if (empty($response)) {
                return $this->msgObj->outputBeibei(false, '',  'wms接口调用失败');
            }

            # 同步失败
            if (!$response['success']) {
                return $this->msgObj->outputBeibei(false, $response['data'], $response['message'], $response['addon']);
            }
            # 开启事务
            OmsDatabase::$oms_db->getPdo()->beginTransaction();

            # 判断商品是否存在
            $this->checkExits($params);

            # 同步成功，添加同步商品
            $this->addItem($params);

            # 事务提交
            OmsDatabase::$oms_db->getPdo()->commit();

            return $this->msgObj->outputBeibei(true, $response['data'], $response['message'], $response['addon']);

        } catch (PDOException $e) {

            # 事务回滚
            OmsDatabase::$oms_db->getPdo()->rollBack();
            return $this->msgObj->outputBeibei(false, '',  $e->getMessage());

        } catch (Exception $e) {
            return $this->msgObj->outputBeibei(false, '',  $e->getMessage());
        }
    }

    /**
     * 添加商品到数据库
     * @param $params
     */
    public function addItem($params)
    {

        $dataInfo = [];

        foreach ($params as $key => $val) {
            $dataInfo[$key] = [
                # 货主
                'customer_id' => $val['company'],
                # 产品sku
                'sku' => $val['sku'],
                # 商品名称
                'descr_c' => $val['skuDesc'],
                # 毛重
                'gross_weight' => $val['grossWeight'],
                # 净重
                'net_weight' => $val['netWeight'],
                # 长
                'sku_Length' => $val['wmsLength'],
                # 宽
                'sku_width' => $val['wmsWidth'],
                # 高
                'sku_height' => $val['wmsHeight'],
                # 体积
                'cube' => $val['wmsVolume'],
                # 品牌
                'sku_group4' => $val['brand'],
                # 货物类型
                'freight_class' => $val['category'],
                # 是否有效期
                'is_shelfLife_mgmt' => $val['isShelfLife'],
                # 有效期
                'shelf_life' => $val['shelfLifeDays'],
                # 周期类型
                'shelfLife_type' => $val['shelfLifeType'],
                # 扩展数据
                'extend_props' => json_encode($val['extendInfo']),
                # 创建时间
                'create_time' => date('Y-m-d H:i:s'),
            ];
        }

        OmsDatabase::$oms_db->insertAll('t_base_product', $dataInfo);
    }

    /**
     * 商品是否存在校验，存在更新为0
     * @param $params
     */
    public function checkExits($params)
    {

        $where = [];

        $dataInfo = [];

        foreach ($params as $key => $val) {

            $dataInfo = array_merge($dataInfo, [':' . $key . 'customer_id' => $val['company'], ':' . $key . 'sku' => $val['sku']]);

            $where[] = '(customer_id = :' . $key . 'customer_id AND sku = :' . $key . 'sku)';
        }

        $where = implode(' or ', $where);

        $update = ['is_valid' => 0];


        OmsDatabase::$oms_db->update('t_base_product', $update, $where, $dataInfo);
    }
}

