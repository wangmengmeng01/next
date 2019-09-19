<?php

/**
 * Created by PhpStorm.
 * Date: 2018-03-26
 * Time: 10:43
 * User: XL
 * 京东解绑接口
 * jingdong.ldop.alpha.waybill.unbind.jos
 */

require_once API_ROOT . '/router/interface/erp/cainiao/JdRequest.php';


class JdUnbindWaybill extends JdRequest
{

    public function unbind($param)
    {
        try {

            # 参数校验
            if (empty($param)) {
                return $this->msgObj->outputJd(1, '错误：解绑参数不能为空', '', $this->logTxt);
            }

            # 承运商编码，承运商ID
            if (empty($param['providerCode']) && empty($param['providerId'])) {
                return $this->msgObj->outputJd(1, '承运商编码 或 承运商ID 必填一个', '', $this->logTxt);
            }

            # 运单号
            if (empty($param['waybillCode'])) {
                return $this->msgObj->outputJd(1, '运单号必填', '', $this->logTxt);
            }

            $dataInfo = $this->getSenderInfo($param);

            $providerInfo = $this->getJdProvider($param, $dataInfo['seller_id']);

            if (empty($providerInfo['provider_code'])) {
                return $this->msgObj->outputJd(1, '该商家承运商配置不完整，请联系实施人员', '', $this->logTxt);
            }

            # 响应信息校验
            if (isset($dataInfo['statusCode']) && $dataInfo['statusCode'] == 1) {
                return $this->msgObj->outputJd(1, $dataInfo['statusMessage'], '', $this->logTxt);
            }

            if (!array_key_exists('access_token', $dataInfo)) {
                return $dataInfo;
            }

            # 数据发送
            $c = new JdClient();

            $c->appKey = JD_APP_KEY;

            $c->appSecret = JD_APP_SECRET;

            $c->accessToken = $dataInfo['access_token'];

            $req = new LdopAlphaWaybillApiUnbindRequest();

            $req->setProviderCode($providerInfo['provider_code']);

            $req->setWaybillCode($param['waybillCode']);

            jd_service::$_requestJdTime = date("Y-m-d H:i:s");
            $resp = $c->execute($req, $c->accessToken);
            jd_service::$_responseOmsTime = date("Y-m-d H:i:s");

            $func = new func();
            $msgId = $func->makeMsgId();

            # 格式转换
            $response = json_decode($resp, true);

            //系统级参数错误
            if (isset($response['error_response'])) {
                $func->addJdWaybillLog($param['customerCode'], jd_service::$_method, $param['waybillCode'], $msgId, jd_service::$_msgId, JD_YD_URL, json_encode($param), $resp, jd_service::$_requestJdTime, jd_service::$_responseOmsTime, $response['error_response']['code']);
                return $this->msgObj->outputJd(1, $response['error_response']['en_desc'] . '(' . $response['error_response']['code'] . '):' . $response['error_response']['zh_desc'], '', $this->logTxt);
            }

            $response = $response['jingdong_ldop_alpha_waybill_api_unbind_responce']['resultInfo'];

            if ($response['statusCode'] != 0) {
                $func->addJdWaybillLog($param['customerCode'], jd_service::$_method, $param['waybillCode'], $msgId, jd_service::$_msgId, JD_YD_URL, json_encode($param), $resp, jd_service::$_requestJdTime, jd_service::$_responseOmsTime, $response['statusCode']);
                return $this->msgObj->outputJd(1, '错误：解绑订单' . $param['waybillCode'] . '失败【' . $response['statusMessage'] . '】', '', $this->logTxt);
            }

            $func->addJdWaybillLog($param['customerCode'], jd_service::$_method, $param['waybillCode'], $msgId, jd_service::$_msgId, JD_YD_URL, json_encode($param), $resp, jd_service::$_requestJdTime, jd_service::$_responseOmsTime, $response['statusCode']);
            return $this->msgObj->outputJd(0, '解绑成功:' . $response['statusMessage'], json_encode($response), $this->logTxt);

        } catch (Exception $e) {

            return $this->msgObj->outputJd(1, $e->getMessage(), '', $this->logTxt);
        }
    }
}