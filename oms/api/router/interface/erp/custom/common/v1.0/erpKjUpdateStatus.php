<?php
/**
 * Created by PhpStorm.
 * User: Renee
 * Date: 2018/4/23
 * Time: 17:10
 */
require API_ROOT . 'router/interface/erp/custom/common/erpRequest.php';
class erpKjUpdateStatus extends erpRequest {
    public function update($params) {
        $data = json_encode($params);
        //请求参数
        $reqParams = array('data'=>$data);
        //日志
        $logExt = array(
            'api_url'    => self::$erpApi,
            'api_method' => custom_service::$_msgtype,
            'api_params' => $reqParams,
        );
        try {
            $resp = $this->utilObj->post(UPDATA_DELIVERY_INFO_URL,$reqParams);
            $resp = $this->removeBOM($resp);

            if (empty($resp)) {
                $respParam = array(
                    'status' => '0',
                    'message'=> '请求超时'
                );
            } else {
                $respParam = json_decode($resp,true);print_r($respParam);
            }
            $returnXml  = '<updataDeliveryInfoResponse>';
            $returnXml .= '<status>'.$respParam['status'].'</status>';
            $returnXml .= '<message>'.$respParam['message'].'</message>';
            $returnXml .= '</updataDeliveryInfoResponse>';

            $logExt['return_msg'] = $returnXml;

            if ($respParam['status'] == 1) {
                return $this->msgObj->outputCustom('true', $respParam['message'],$logExt);
            } else {
                return $this->msgObj->outputCustom('false', $respParam['message'],$logExt);
            }
        } catch (Exception $e) {
            $logExt['return_msg'] = $returnXml;
            return $this->msgObj->outputCustom('false', $e->getMessage(),$logExt);
        }
    }

    /**
     * @param  string 需要处理的
     * @return string 处理过后的报文
     */
    public function removeBOM($str = '')
    {
        if (substr($str, 0,3) == pack("CCC",0xef,0xbb,0xbf))
            $str = substr($str, 3);

        return $str;
    }
}