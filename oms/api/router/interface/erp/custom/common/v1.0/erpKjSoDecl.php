<?php
/**
 * 出库单报检确认接口处理类
 * User: Renee
 * Date: 2018/1/18
 * Time: 19:42
 */
require API_ROOT . 'router/interface/erp/custom/common/erpRequest.php';
class erpKjSoDecl extends erpRequest {
    public function decl($params){
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