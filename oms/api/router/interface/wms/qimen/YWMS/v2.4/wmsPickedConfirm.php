<?php
/**
 * 家乐福卖场拣货任务拣货完成接口处理类
 * User: Renee
 * Date: 2018/8/15
 * Time: 15:11
 */
require API_ROOT . '/router/interface/wms/qimen/YWMS/wmsRequest.php';
class wmsPickedConfirm extends wmsRequest
{
    /**
     * 家乐福卖场拣货任务拣货完成处理方法
     * @param $params
     */
    public function confirm($params)
    {
        if (empty($params)) {
            return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
        } else {
            //转发数据给wms
            $response = $this->send();
            //解析返回的数据
            if (!empty($response)) {
                return $this->msgObj->outputQimen($response['flag'], $response['message'], $response['code'], $response['addon']);
            } else {
                return $this->msgObj->outputQimen('failure', 'wms接口调用失败', 'S007');
            }
        }
    }
}