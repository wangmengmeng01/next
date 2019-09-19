<?php
/**
 * Created by PhpStorm.
 * Date: 2018-03-23
 * Time: 9:57
 * User: XL
 * 京东商家单号库存查询
 * jingdong.ldop.alpha.vendor.stock.queryByProviderCo
 */
require_once API_ROOT . '/router/interface/erp/cainiao/JdRequest.php';


class JdQueryStock extends jdRequest
{

    public function query($param)
    {
        try {

            # 数据校验
            if (empty($param)) {

                return $this->msgObj->outputJd(1, '错误：请求参数不能为空', '', $this->logTxt);
            }

            # 商家编码（商家ID）
            if (empty($param['vendorCode'])) {

                return $this->msgObj->outputJd(1, '商家编码不能为空', '', $this->logTxt);
            }

            # 承运商编码
            if (empty($param['providerCode'])) {

                return $this->msgObj->outputJd(1, '承运商编码不能为空', '', $this->logTxt);
            }

            # 获取本地信息响应
            $dataInfo = $this->getSenderInfo($param);

            # 信息校验
            if (isset($dataInfo['statusCode']) && $dataInfo['statusCode'] == 1) {
                return $this->msgObj->outputJd(1, $dataInfo['statusMessage'], '', $this->logTxt);
            }

            if (!array_key_exists('access_token',$dataInfo)) {
                return '{"error_response": {"code":"'.$dataInfo['code'].'","zh_desc":"'.$dataInfo['sub_msg'].'","en_desc":"'.$dataInfo['msg'].'"}}';
            }

            $accessToken = $dataInfo['access_token'];


            # 数据发送
            $c = new JdClient();

            $c->appKey = JD_APP_KEY;

            $c->appSecret = JD_APP_SECRET;

            $c->accessToken = $accessToken;

            $req = new LdopAlphaVendorStockQueryByProviderCodeRequest();

            $req->setVendorCode($dataInfo['seller_id']);
            $req->setProviderCode($param['providerCode']);
            $req->setBranchCode($param['branchCode']);

            $resp = $c->execute($req, $c->accessToken);

            # json格式转换为数组
            $response = json_decode($resp, true);

            //系统级参数错误
            if (isset($response['error_response'])) {
                return $this->msgObj->outputJd(1, $response['error_response']['en_desc'].'('.$response['error_response']['code'].'):'.$response['error_response']['zh_desc'], '', $this->logTxt);
            }

            $response = $response['jingdong_ldop_alpha_vendor_stock_queryByProviderCode_responce']['resultInfo'];

            if ($response['statusCode'] != 0) {

                return $this->msgObj->outputJd(1, '错误：商家单号库存查询接口调用失败【' . $response['statusMessage'] . '】', '', $this->logTxt);
            }

            $db = new DbAction();

            # 预处理数据
            foreach ($param as $k => $v) {
                $tmpParams[':' . $k] = $v;
            }

            # 更新剩余单号量
            $res = $db->update('csk_seller_waybill_info',
                array('quantity' => $response['data'][0]['amount']),
                'seller_id = :vendorCode AND cp_code = :providerCode AND branch_code = :branchCode',
                $tmpParams
            );

            if (!$res) {

                return $this->msgObj->outputJd(1, '错误：商家单号库存查询接口，数据存储更新失败', '', $this->logTxt);

            }

            return $this->msgObj->outputJd(0, '成功：商家单号库存查询接口调用成功', $resp, $this->logTxt);


        } catch (Exception $e) {

            return $this->msgObj->outputJd(1, $e->getMessage(), '', $this->logTxt);
        }

    }
}