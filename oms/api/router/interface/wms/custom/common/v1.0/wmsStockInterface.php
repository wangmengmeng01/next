<?php
/**
 * 库存查询 cnec_wh_7
 * Created by PhpStorm.
 * User: xl
 * Date: 2018-01-11
 * Time: 10:02
 */

require_once API_ROOT . 'router/interface/wms/custom/common/wmsRequest.php';

class wmsStockInterface extends wmsRequest
{

    public function create($param)
    {

        if (empty($param)) {

            return $this->msgObj->outputCustom(false, '错误：请求数据为空');
        }

        # 信息转发
        $response = $this->send();

        if (!$response['success']) {

            return $this->msgObj->outputCustom(false, '库存查询失败：' . $response['reasons'], $response['addon']);

        }

        return $this->msgObj->outputCustom(true, '库存查询成功', $response['addon']);


    }
}