<?php
/**
 * 订单流水查询操作类
 * @author Renee
 *
 */
require_once API_ROOT . '/router/interface/wms/common/wmsRequest.php';
class wmsQueryOrderProcess extends wmsRequest 
{
    public function create($params) 
    {
        if (!empty($params)) {
            //转发给wms
            $response = $this->send(service::$_methodTo,$params);
            //判断是否正常返回
            if (!empty($response)) {
                //获取错误数据
    			if ($response['returnFlag'] != 1) {
    				$error_info_arr = $this->merge_error_data($response['resultInfo'], msg::$err_arr);
    			} else {
    				$error_info_arr = $this->merge_error_data('', msg::$err_arr);
    			}
    			
    			if ($response['returnFlag'] == 0) {
    			    return $this->msgObj->output(0, $response['returnDesc'], '0001', '', $response['addon']);
    			} elseif ($response['returnFlag'] == 1) {
    			    if (empty($error_info_arr)) {
    					return $this->msgObj->output(1, 'ok', '0000', 0, $response['addon'], $response['addon']['return_msg']);
    				} else {
    					return $this->msgObj->output(2, '部分成功部分失败', '0001', 0, $response['addon'], $response['addon']['return_msg']);
    				}
    			}
            } else {
                return $this->msgObj->output(0, 'fail', 'S007', 0);
            }
        } else {
            return $this->msgObj->output(0, 'fail', '0001', 0);
        }
    }
    
    /**
     * 解析返回的错误数据，并和之前的错误数据进行组合
     * @param $error_info  erp接口返回的resultInfo错误信息
     * @param $error_arr   msg类中存储的错误信息
     * @return array
     */
    public function merge_error_data($error_info, $error_arr)
    {
        $return_arr = array();
        $i=0;
        if (!empty($error_info)) {
            foreach ($error_info as $v)
            {
                $return_arr[$i] = $v;
                $i++;
            }
        }
        if (!empty($error_arr)) {
            foreach ($error_arr as $val)
            {
                $return_arr[$i] = $val;
                $i++;
            }
        }
        return $return_arr;
    }
}

?>