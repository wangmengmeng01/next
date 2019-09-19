<?php
/**
 * 入库单取消接口 cnec_wh_5
 * Created by PhpStorm.
 * User: xl
 * Date: 2018-01-11
 * Time: 9:15
 */

require_once API_ROOT . 'router/interface/wms/custom/common/wmsRequest.php';

class wmsKjAsnCancelInterface extends wmsRequest
{

    public function create($param)
    {

        try {

            if (empty($param)) {

                return $this->msgObj->outputCustom(false, '错误：请求的数据为空');
            }

            # 存在则下发至WMS
            $response = $this->send();

            # 入库单取消下发失败
            if (!$response['success']) {

                return $this->msgObj->outputCustom(false, "入库单取消报文下发失败:{$response['reasons']}", $response['addon']);
            }

            global $db;

            # 入库单取消下发成功，更新数据库
            $sql = 'UPDATE t_inbound_info
                            SET order_status = 90,
                                external_no2 = :external_no2,
                                operate_time = now()
                    WHERE order_no = :order_no';

            # 数据绑定
            $model = $db->prepare($sql);
            $model->bindParam(':order_no', $param['externalNo']);
            $model->bindParam(':external_no2', $param['externalNo2']);

            if ($model->execute()) {

                return $this->msgObj->outputCustom(true, '入库单取消下发并更新数据库成功', $response['addon']);
            }


            return $this->msgObj->outputCustom(false, '入库单取消下发成功，但数据库更新失败');

        } catch (Exception $e) {

            return $this->msgObj->outputCustom(false, $e->getMessage());

        }
    }
}