<?php

/**
 * 标准消息类
 * User: 独孤羽<123517746@qq.com>
 * Date: 15-4-27 下午2:47
 */
class msg
{
    public static $err_arr = array(); //存储错误信息

    /**
     * 标准消息格式返回
     * @param $rsp
     * @param $msg
     * @param string $msgCode
     * @param string $data
     * @param mix $addon
     * @param string $xmlData
     * @return array
     */
    public function output($rsp, $msg = '', $msgCode = '', $data = '', $addon = '', $xmlData = '')
    {
        if (!in_array(strval($rsp), array('0', '1', '2'))) {
            $rsp = $rsp == 'succ' ? '1' : '0';
        }
        $rs = array(
            'returnFlag' => $rsp,
            'returnCode' => $msgCode,
            'returnDesc' => $msg,
            'resultInfo' => $data
        );
        if ($addon) {
            $rs['addon'] = $addon;
        }
        //返回库存查询,订单流水查询,商品同步接口返回的库存信息
        if ($xmlData) {
            if (preg_match("/<items>(.*)<\/items>/s", $xmlData, $xmlArr)) {
                $rs['xmldata'] = $xmlArr[0];
            } elseif (preg_match("/<orderProcess>(.*)<\/orderProcess>/s", $xmlData, $xmlArr)) {
                $rs['xmldata'] = $xmlArr[0];
            } elseif (preg_match("/<succInfos>(.*)<\/succInfos>/s", $xmlData, $xmlArr)) {
                $rs['xmldata'] = $xmlArr[0];
            }
        }
        return $rs;
    }

    /**
     * 奇门接口标准输出
     * @param string $rsp
     * @param string $msg
     * @param string $msgCode
     * @param string $addon
     * @return array $rs
     */
    public function outputQimen($rsp, $msg = '', $msgCode = '', $addon = '')
    {
        if (!in_array(strval($rsp), array('success', 'failure'))) {
            $rsp = $rsp == 1 ? 'success' : 'failure';
        }
        $rs = array(
            'flag' => $rsp,
            'code' => $msgCode,
            'message' => $msg,
        );
        if ($addon) {
            $rs['addon'] = $addon;
        }
        return $rs;
    }

    /**
     * 菜鸟电子面单接口返回消息方法
     * @param $flag     信息标志
     * @param $msgCode  返回信息编码
     * @param $msg      返回信息
     * @param $addon    日志
     * @param $subCode
     * @param $subMsg
     * @return $rs
     */
    public function outputCainiao($flag, $msgCode, $msg, $addon, $subCode, $subMsg)
    {
        $xmlObj = new xml();
        //wms的请求数据校验错误
        if ($flag == 0) {
            $rs = array(
                'code' => $msgCode,
                'msg' => $msg,
                'sub_code' => '',
                'sub_msg' => ''
            );
            $rsStr = '<?xml version="1.0" encoding="utf-8"?><error_response>';
            $rsStr .= $xmlObj->array2xml($rs);
            $rsStr .= '</error_response>';
            $addon['return_msg'] = $rsStr;
            $rs['addon'] = $addon;
            return $rs;
        } elseif ($flag == 1) {
            $rs = array(
                'msg' => $msg
            );
            if ($addon) {
                $rs['addon'] = $addon;
            }
            return $rs;
        } elseif ($flag == 2 || $flag == 3) {//商家授权接口返回格式   2是正确的，3是错误的
            if ($msgCode != '0000') {
                $rs = array(
                    'code' => $msgCode,
                    'msg' => $msg,
                    'sub_code' => '',
                    'sub_msg' => ''
                );
            } else {
                $rs = array(
                    'code' => $msgCode,
                    'msg' => $msg,
                );
            }
            $rsStr = '<?xml version="1.0" encoding="utf-8"?>';
            if ($flag == 3) {
                $rsStr .= '<error_response>';
                $rsStr .= $xmlObj->array2xml($rs);
                $rsStr .= '</error_response>';
            } else {
                $rsStr .= '<response>';
                $rsStr .= $xmlObj->array2xml($rs);
                $rsStr .= '</response>';
            }
            $rs['addon'] = array('return_msg' => $rsStr);
            return $rs;
        } elseif ($flag == 4) {
            $rs = array(
                'code' => $msgCode,
                'msg' => $msg,
                'sub_code' => $subCode,
                'sub_msg' => $subMsg
            );
            $returnMsgStr = $addon['return_msg'];
            $addon['return_msg'] = '<?xml version="1.0" encoding="utf-8"?>' . $returnMsgStr;
            $rs['addon'] = $addon;
            return $rs;
        }
    }

    public function outputCnStorage($success, $msg = '', $msgCode = '', $addon = '')
    {
        $rs = array(
            'success' => true,
            'errorCode' => $msgCode,
            'errorMsg' => $msg
        );
        if (!$success) {
            $rs['success'] = false;
        }
        if ($addon) {
            $rs['addon'] = $addon;
        }
        return $rs;
    }

    /**
     * 京东无界电子面单接口返回消息方法
     * @param $code          响应编码
     * @param string $msg 响应信息
     * @param string $rspMsg 返回数据
     * @param string $addon 日志存储
     * @return array
     */
    public function outputJd($code, $msg = '', $rspMsg = '', $addon = '')
    {
        $rs = array(
            'statusCode' => $code,
            'statusMessage' => $msg,
        );
        if ($code != 0) {
            $rspMsg = json_encode($rs);
        }
        if ($addon) {
            $rs['addon'] = $addon;
            $rs['addon']['return_msg'] = $rspMsg;
        }
        return $rs;
    }

    /**
     * 跨境仓储响应数据格式处理
     * @param $success  返回状态
     * @param $msg      返回报文
     * @param $addon    日志数据
     * @return $rs      返回数据【数组】
     */
    public function outputCustom($success, $msg, $addon = '')
    {
        $success = ($success === true || $success == 'true') ? 'true' : 'false';

        if (custom_service::$_msgtype != 'updataDeliveryInfo') {
            $rs_part = array(
                'success' => $success,
                'reasons' => $msg
            );
        } else {
            if ($success != 'true') {
                $status = 0;
            } else {
                $status = 1;
            }
            $rs_part = array(
                'status' => $status,
                'message' => $msg
            );
        }

        $rs_special = custom_service::$_extraValueArr;
        $rs = array_merge($rs_part, $rs_special);

        if ($addon != '') {
            $rs['addon'] = $addon;
        }
        return $rs;
    }

    public function outputKaola($success, $msg = '', $rspMsg = '', $addon = '')
    {
        $rs = array(
            'success' => $success,
            'error_msg' => $msg,
        );

        if (!$success) {
            $respArr = array(
                'success' => false,
                'error_msg' => $msg,
            );
            $rspMsg = json_encode($respArr);
        }

        if ($addon) {
            $rs['addon'] = $addon;
            $rs['addon']['return_msg'] = $rspMsg;
        }
        return $rs;
    }
    /***
     * Notes: 拼多多电子面单接口返回消息方法
     * Date: 2019/4/4
     * Time: 10:14
     * @param $flag    信息标志
     * @param $msgCode 返回信息编码
     * @param $msg     返回信息
     * @param $addon   日志
     * @return array
     */
    public function outputPdd($flag, $msgCode, $msg, $addon='')
    {
        $rs = array();
        if ($flag == 0) {
            $rs = array(
                'error_response' => array(
                    'error_msg' => $msg,
                    'sub_msg' => '',
                    'sub_code' => '',
                    'error_code' => $msgCode,
                    'request_id' => '',
                )
            );
            $addon['return_msg'] = json_encode($rs);
            $rs['addon'] = $addon;
        } elseif ($flag == 1) {
            $rs = array(
                'code' => $msgCode,
                'msg' => $msg,
            );
            if ($addon) {
                $rs['addon'] = $addon;
            }
        } elseif ($flag == 2 || $flag == 3) {//商家授权接口返回格式   2是正确的，3是错误的
            if ($msgCode != '0000') {
                $rs = array(
                    'error_response' => array(
                        'error_msg' => $msg,
                        'sub_msg' => '',
                        'sub_code' => '',
                        'error_code' => $msgCode,
                        'request_id' => '',
                    )
                );
            } else {
                $rs = array(
                    'code' => $msgCode,
                    'msg' => $msg,
                );
            }
            $rs['addon'] = array('return_msg' => json_encode($rs));
        } elseif ($flag == 4) {
            $rs = json_decode($addon['return_msg'],true);
            $rs['addon'] = $addon;
        }
        return $rs;
    }

    /***
     * Notes: 贝贝接口标准输出
     * Date: 2019/6/16
     * Time: 2:52
     * @param $success 是否成功
     * @param string $data 返回数据
     * @param string $message  说明
     * @param string $addon  日志
     * @return array
     */
    public function outputBeibei($success, $data = '', $message = '', $addon = '')
    {
        $rs = array(
            'success' => $success,
            'data' => $data,
            'message' => $message,
        );
        if ($addon) {
            $rs['addon'] = $addon;
        }
        return $rs;
    }

    /**
     * 把错误数据组合成xml格式
     * @param $error_arr 错误数据数组
     * @return string
     */
    public function get_error_str($error_arr)
    {
        $xmlData = '';
        if (!empty($error_arr)) {
            $xml = new xml();
            $xmlData = '';
            //获取method对应的resultInfo返回信息
            $resultInfoStr = service::$_methodErrorInfo[service::$_method];
            if ($resultInfoStr != '') {
                $resultInfoArr = explode(",", $resultInfoStr);
            } else {
                $resultInfoArr = array();
            }
            foreach ($error_arr as $val) {
                $content = array();
                if (!empty($resultInfoArr)) {
                    foreach ($resultInfoArr as $v) {
                        $content[$v] = $val[$v];
                    }
                } else {
                    $content = '';
                }
                $xmlData .= $xml->array2xml(array('resultInfo' => $content));
            }
            //去除xml中前后的resultInfo标签
            $xmlData = substr($xmlData, 12, -13);
        }
        return $xmlData;
    }
}
