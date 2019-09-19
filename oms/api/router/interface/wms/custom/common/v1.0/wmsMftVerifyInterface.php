<?php
/**
 * 订单审核接口
 * Created by PhpStorm.
 * User: xl
 * Date: 2018-01-17
 * Time: 13:31
 */

require_once API_ROOT . '/router/interface/wms/custom/common/wmsRequest.php';

class wmsMftVerifyInterface extends wmsRequest
{

    public function create($param)
    {

        try {

            # 数据校验
            if (empty($param)) {

                return $this->msgObj->outputCustom(false, '错误：请求数据不能为空');
            }

            # 转发订单审核相关信息
            $response = $this->send();

            # 返回校验,订单审核接口请求失败
            if (!$response['success']) {

                return $this->msgObj->outputCustom(false, '订单审核接口：' . $response['reasons'], $response['addon']);
            }

            # 数据库更新
            global $db;

            $sql = 'UPDATE t_delivery_order_info
                       SET verify_type = :verify_type, verify_flag = :verify_flag, `operate_time` = :operate_time
                     WHERE delivery_order_code = :delivery_order_code';

            # 数据预处理准备
            $model = $db->prepare($sql);

            # 数据绑定
            $model->bindParam(':verify_type', $param['type']);
            $model->bindParam(':verify_flag', $param['flag']);
            $model->bindParam(':operate_time', $param['date']);
            $model->bindParam(':delivery_order_code', $param['externalNo']);

            # 审核数据执行更新成功
            if ($model->execute()) {

                return $this->msgObj->outputCustom(true, '订单审核接口下发并更新数据库成功', $response['addon']);
            }

            # 审核下发成功，数据库更新失败
            return $this->msgObj->outputCustom(false, '订单审核成功，但数据库更新审核信息失败');


        } catch (Exception $e) {

            return $this->msgObj->outputCustom(false, $e->getMessage());
        }
    }
}