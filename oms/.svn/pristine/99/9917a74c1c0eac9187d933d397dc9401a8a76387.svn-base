<?php
/**
 * 网易考拉清单信息同步接口
 * User: Renee
 * Date: 2018/8/20
 * Time: 14:42
 */
require API_ROOT . '/router/interface/wms/kaola/common/wmsRequest.php';
class KlInvtOrderSync extends wmsRequest
{
    /**
     * 请求并根据返回处理数据
     * @param $params 请求数据
     * @return array  返回网易报文格式
     */
    public function sync($params)
    {
        if (empty($params)) {
            return $this->msgObj->outputKaola(false, '执行错误：请求数据为空！');
        } else {
            try {
                $resp = $this->send();
                if (empty($resp)) {
                    return $this->msgObj->outputKaola(false, '返回数据为空！');
                } else {
                    return $this->msgObj->outputKaola($resp['success'], $resp['error_msg'], kaola_service::$_rsMsg);
                }
            } catch (Exception $e) {
                return $this->msgObj->outputKaola(false, $e->getMessage());
            }
        }
    }
}