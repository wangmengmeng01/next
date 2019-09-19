<?php
/**
 * Description: 转库存出库接口 wh_12
 * Date: 2018-04-16 16:25
 * Created by XL.
 */

require API_ROOT . 'router/interface/wms/custom/common/wmsRequest.php';

class wmsStockOutWarehouse extends wmsRequest
{
    public function stock($param)
    {

        if (empty($param)) {

            return $this->msgObj->outputCustom(false, '错误：请求数据不能为空');
        }

        try {

            $response = $this->send();

            # 转库存出库接口转发失败
            if ($response['success'] != 'true') {

                return $this->msgObj->outputCustom(false, "转库存出库接口转发失败:{$response['reasons']}", $response['addon']);
            }

            # 成功
            return $this->msgObj->outputCustom(true, '转库存出库接口转发成功', $response['addon']);

        } catch (Exception $e) {

            return $this->msgObj->outputCustom(false, "转库存出库接口转发失败:{$e->getMessage()}");

        }


    }

}