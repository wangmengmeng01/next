<?php
/**
 * 仓库回调推送理货报告详情信息接口处理类
 * User: Renee
 * Date: 2018/5/10
 * Time: 13:21
 */
require_once API_ROOT . 'router/interface/erp/kaola/common/erpRequest.php';
class KlTallyConfirm extends erpRequest
{
    /**
     * 请求并根据返回处理数据
     * @param $params 请求数据
     * @return array  返回网易报文格式
     */
    public function confirm ($params)
    {
        if (empty($params)) {
            return $this->msgObj->outputKaola(false,'执行错误：请求数据为空！');
        } else {
            try {
                //转发数据
                $rspArr = $this->send();
                if (empty($rspArr)) {
                    return $this->msgObj->outputKaola(false,'返回数据为空！');
                } else {
                    return $this->msgObj->outputKaola($rspArr['success'],$rspArr['error_msg'],kaola_service::$_rsMsg);
                }
            } catch (Exception $e) {
                return $this->msgObj->outputKaola(false,$e->getMessage());
            }
        }
    }
}