<?php
/**
 * 订单流水查询过滤类
 * @author Renee
 *
 */
class filterQueryOrderProcess extends msg 
{
    public function create(&$requestData) {
        if(empty($requestData['data']['header'])) {
            //接口层记录日志
            $logExt = array(
                'api_url' => '',
                'api_method' => service::$_method,
                'api_params' => $requestData,
                'return_msg' => 'filter: header不能为空'
            );
            return $this->output(0, 'header不能为空', 'S003', '', $logExt);
        }
        
        $utilObj = new util();
        $multiFlag = $utilObj->isArrayMulti($requestData);
        if ($multiFlag) {
            $headers = $requestData['data']['header'];
        } else {
            $headers = array($requestData['data']['header']);
        }

        $error_arr = array();
        $success_arr = array();
        global $db;
        
        foreach ($headers as $k=>$v) {
            //校验单据号
            if (empty($v['orderCode'])) {
                $error_arr[$k] = $this->get_error_data($k, 'S003', '单据号不能为空', $v, $error_arr);
                continue;
            }
            $success_arr[$k] = $v;
        }
        
        if(!empty($error_arr)) {
            $xmlData = $this->get_error_str($error_arr);
        }
        msg::$err_arr = $error_arr;
        if (empty($success_arr)) {
            //接口层记录日志
            $logExt = array(
                'api_url' => '',
                'api_method' => service::$_method,
                'api_params' => $requestData,
                'return_msg' => 'filter: 数据校验不通过'
            );
            return $this->output(0, 'filter: 数据校验不通过', '0001', $xmlData, $logExt);
        } else {
            $requestData = array('data'=>array('header' => $success_arr));
        }
        return $this->output('succ');
    }
    
    /**
     * 记录错误数据
     * @param $key  键名
     * @param $errorCode  错误编码
     * @param $errorDescr 错误描述
     * @param $data       错误详细数据
     * @param $error_arr  错误数组
     * @return $error_arr
     */
    public function get_error_data($key, $errorCode, $errorDescr, $data, $error_arr)
    {
        $return_arr = array();
        if (empty($error_arr[$key])) {
            $return_arr = $data;
            $return_arr['errorcode'] = $errorCode;
            $return_arr['errordescr'] = $errorDescr;
        } else {
            $return_arr = $error_arr[$key];
        }
        return $return_arr;
    }
}
?>