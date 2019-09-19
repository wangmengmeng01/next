<?php

class ManageController extends Controller
{

    public function actionIndex()
    {
        $this->render('index');
    }

    /*
     * 功能：测试接口提交
     */
    public function actionSub()
    {
        $apiList = $_POST['ApiList'];
        $mes = array();
        $compile = array();
        $customerId = '';
        if($this->xml_parser($apiList['xml'])){
            $data = $apiList['xml'];
            $innerApiUrl = OMS_API_URL;
            $appSecret = '1234567890';
            // 获取messageid
            $message = $this->get_message($apiList['method']);
            if(!empty($message)){
                if($message['api_from'] =='erp'){
                    if(preg_match("/^(putCustData)/", $apiList['method'])){
                        $customerId = '5180001001';
                    }else{
                        $apiListArr = $this->xmlStr2array($apiList['xml']);
                        //判断是否为二维数组
                        $multiFlag = $this->isArrayMulti($apiListArr);
                        if ($multiFlag) {
                            $headers = !empty($apiListArr['header']) ? $apiListArr['header'] : $apiListArr['data']['header'];
                        } else {
                            $headers = array(!empty($apiListArr['header']) ? $apiListArr['header'] : $apiListArr['data']['header']);
                        }
                        $customerId = $headers[0]['CustomerID'];
                    }
                }else{
                    $customerId = 'FLUXWMS';
                }
            }else{
                die(json_encode(array('status'=>'fail', 'msg'=>'获取接口信息失败')));
            }
            // 请求内部API接口
            $params = array(
                'method' => $apiList['method'],
                'customerid' => $customerId,
                'warehouseid'=>'WH01',
                'messageid' => $message['message_id'],
                'apptoken'=>'80AC1A3F-F949-492C-A024-7044B28C8025',
                'appkey'=>'test',
                'sign'=>strtoupper(base64_encode(md5($appSecret.$data.$appSecret))),
                'timestamp'=>date("Y-m-d H:i:s"),
                'data' => $data
            );
            //5为时间超时
            $response = util::curl($innerApiUrl, $params, 5);
            $compile['compile'] = $params;
            $mes['compile']= $this->_array2xml($compile);
            $mes['result']= $response;
            die(json_encode(array('status'=>'ok', 'msg'=>$mes)));
        }else{
			die(json_encode(array('status'=>'fail', 'msg'=>'xml格式不合法')));
        }
    }

    /*
     * 获取message
     */
    public function get_message($method)
    {
        // 校验订单号是否存在
        $db = Yii::app()->db;
        $sql = 'SELECT message_id,api_from FROM `t_api_list` a WHERE api_id =:api_id AND is_valid = 1 limit 1;';
        $model = $db->createCommand($sql);
        $model->bindValue(':api_id', $method);
        $rs = $model->queryRow();
        if ($rs) {
            return $rs;
        } else {
            return false;
        }
    }

    /**
     * 解析XML格式的字符串
     *
     * @param string $str
     * @return 解析正确就返回解析结果,否则返回false,说明字符串不是XML格式
     */
    public function xml_parser($str){
        $xml_parser = xml_parser_create();
        if(!xml_parse($xml_parser,$str,true)){
            xml_parser_free($xml_parser);
            return false;
        }else {
            return (json_decode(json_encode(simplexml_load_string($str)),true));
        }
    }
    
    /*
     * xml 转化数组
     */
    public function xmlStr2array($xmlStr)
    {
        $xmlArr = simplexml_load_string($xmlStr, NULL, LIBXML_NOCDATA);
        $xmlArr = json_decode(json_encode($xmlArr), true);
        return $xmlArr;
    }

    private function _array2xml($array)
    {
        $xml = '';
        foreach($array as $key=>$val){
            if(!is_numeric($key)){
                $xml .= "<$key>";
            }

            $xml .= is_array($val) ? $this->_array2xml($val) : $val;
            //去掉空格，只取空格之前文字为key
            list($key,) = explode(' ',$key);

            if(!is_numeric($key)){
                $xml .= "</$key>";
            }
        }
        return $xml;
    }
    
    /**
     * 判断数组是一维数组还是二维数组
     * @param array
     * @return boolean
     */
    public function isArrayMulti($arr)
    {
    	if (!is_array($arr)) {
    		return false;
    	}
    	if (empty($arr)) {
    		return false;
    	} else {
    		if (!empty($arr['xmldata'])) {
    			$arr = $arr['xmldata'];
    		} 
    	}
    	if (!empty($arr['header'])) {
    		$KeyArr = array_keys($arr['header']);
    	} elseif (!empty($arr['data']['orderinfo'])) {
    		$KeyArr = array_keys($arr['data']['orderinfo']);
    	} elseif (!empty($arr['data']['header'])) {
    		$KeyArr = array_keys($arr['data']['header']);
    	} elseif (!empty($arr['data']['ordernos'])) {
    		$KeyArr = array_keys($arr['data']['ordernos']);
    	} else {
    		return false;
    	}

    	foreach ($KeyArr as $val)
    	{
    		if (!is_numeric($val)) {
    			return false;
    		}
    	}
    	return true;
    }
}