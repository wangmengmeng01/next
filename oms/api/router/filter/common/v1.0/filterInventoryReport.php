<?php
/**
 * 库存盘点过滤类
 * @author Renee
 *
 */
class filterInventoryReport extends msg 
{
    public function push(&$requestData) {
        if(empty($requestData['data']['orderinfo'])) {
            //接口层记录日志
            $logExt = array(
                'api_url' => '',
                'api_method' => service::$_method,
                'api_params' => $requestData,
                'return_msg' => 'filter: orderinfo不能为空'
            );
            return $this->output(0, 'orderinfo不能为空', 'S003', '', $logExt);
        }
        
        $utilObj = new util();
        $multiFlag = $utilObj->isArrayMulti($requestData);
        if ($multiFlag) {
            $orderInfos = $requestData['data']['orderinfo'];
        } else {
            $orderInfos = array($requestData['data']['orderinfo']);
        }
        
        $error_arr = array();
        $success_arr = array();
        global $db;
        
        foreach ($orderInfos as $k=>$v) {
            //校验仓库
            if (empty($v['WarehouseID'])) {
                $error_arr[$k] = $this->get_error_data($k, 'S003', '仓库编码不能为空', $v, $error_arr);
                continue;
            }
            //校验盘点单
            if (empty($v['checkOrderCode'])) {
                $error_arr[$k] = $this->get_error_data($k, 'S003', '盘点单编码不能为空', $v, $error_arr);
                continue;
            }
            //校验货主
            if (empty($v['CustomerID'])) {
                $error_arr[$k] = $this->get_error_data($k, 'S003', '货主编码不能为空', $v, $error_arr);
                continue;
            }
            if (empty($v['items']['item'][0])) {
                $v['items']['item'] = array($v['items']['item']);
            }
            //校验明细
            $m = 0;
            if (empty($v['items']['item'])) {
                $error_arr[$k] = $this->get_error_data($k, 'S003', '库存盘点通知明细不能为空', $v, $error_arr);
                continue;
            } else {
                if (empty($v['items']['item'][0])) {
                    $v['items']['item'] = array($v['items']['item']);
                }
                foreach ($v['items']['item'] as $val) {
                    if (empty($val['itemCode'])) {
                        $error_arr[$k] = $this->get_error_data($k, 'S003', '商品编码不能为空', $v, $error_arr);
                        continue;
                    }
                    if (empty($val['quantity'])) {
                        $error_arr[$k] = $this->get_error_data($k, 'S003', '盘盈盘亏商品变化量不能为空', $v, $error_arr);
                        continue;
                    }
                }
            }
            if ($m > 0) {
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
			$requestData = array('data'=>array('orderinfo' => $success_arr));
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