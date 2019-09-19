<?php
/**
 * 仓库无库存反馈接口处理类
 * User: Renee
 * Date: 2018/1/19
 * Time: 13:34
 */
require API_ROOT . 'router/interface/erp/custom/common/erpRequest.php';
class erpKjLackGoods extends erpRequest {
    public function lack($params){
        if (empty($params)) {
            return $this->msgObj->outputCustom('false', '失败：请求的数据为空!');
        } else {
            try {
                $response = $this->send();
                if (!empty($response)) {
                    if ($response['success'] == 'true') {
                        return $this->msgObj->outputCustom('true', $response['reasons'],$response['addon']);
                    } else {
                        return $this->msgObj->outputCustom('false', $response['reasons'],$response['addon']);
                    }
                } else {
                    return $this->msgObj->outputCustom('false', $response['reasons'],$response['addon']);
                }
            } catch (Exception $e){
                return $this->msgObj->outputCustom('false', $e->getMessage());
            }
        }
    }
}