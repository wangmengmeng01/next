<?php
/**
 * 考拉库存调整接口处理类
 * User: Renee
 * Date: 2018/6/15
 * Time: 14:53
 */
require_once API_ROOT . 'router/interface/erp/kaola/common/erpRequest.php';
class KlStoreAjust extends erpRequest
{
    /**
     * 请求并根据返回处理数据
     * @param $params 请求数据
     * @return array  返回网易报文格式
     */
    public function ajust($params)
    {
        if (empty($params)) {
            return $this->msgObj->outputKaola(false, '执行错误：请求数据为空！');
        } else {
            try {
                //转发数据
                $rspArr = $this->send();
                if (empty($rspArr)) {
                    return $this->msgObj->outputKaola(false, '返回数据为空！');
                } else {
                    return $this->msgObj->outputKaola(true, $rspArr['error_msg'], kaola_service::$_rsMsg);
                }
            } catch (Exception $e) {
                return $this->msgObj->outputKaola(false, $e->getMessage());
            }
        }
    }
}