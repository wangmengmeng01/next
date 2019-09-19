<?php
/**
 * Description: 库存查询接口
 * Date: 2018-05-07 16:28
 * Created by XL.
 */

require_once API_ROOT . '/router/interface/wms/kaola/common/wmsRequest.php';

class KlStockSearch extends wmsRequest
{

    public function search($params)
    {


        if (empty($params)) {

            return $this->msgObj->outputKaola(false, '库存查询接口错误：请求数据不能为空');
        }

        $response = $this->send();

        if (!$response['success']) {

//            return $this->msgObj->outputKaola(false, '库存查询接口错误:' . $response['error_msg'], '', $response['inventory']);
            return $this->msgObj->outputKaola(false, $response['error_msg']);
        }

        return $this->msgObj->outputKaola(true, $response['error_msg']);

    }

}