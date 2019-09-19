<?php
/**
 * Created by PhpStorm.
 * User: 20171012
 * Date: 2018/3/21
 * Time: 15:04
 * 京东查询商家签约信息
 * jingdong.ldop.alpha.provider.sign.success.info.get
 */

require_once API_ROOT . '/router/interface/erp/cainiao/JdRequest.php';

class JdGetProvider extends JdRequest
{
    public function get($param)
    {
        try {

            try{


                # 校验数据
                if (empty($param['venderCode'])) {
                    return $this->msgObj->outputJd(1, '错误：请求参数商家编码数据不能为空', '', $this->logTxt);
                }

                $param['vendorCode'] = $param['venderCode'];

                unset($param['venderCode']);

                $param = $this->getSenderInfo($param);

                # 信息校验
                if (isset($param['statusCode']) && $param['statusCode'] == 1) {
                    return $this->msgObj->outputJd(1, $param['statusMessage'], '', $this->logTxt);
                }

                if (array_key_exists('access_token', $param)) {
                    $accessToken = $param['access_token'];
                } else {
                    return $param;
                }

                # 数据发送
                $c = new JdClient();

                $c->appKey = JD_APP_KEY;

                $c->appSecret = JD_APP_SECRET;

                $req = new LdopAlphaProviderSignSuccessInfoGetRequest();

                $req->setVenderCode($param['seller_id']);

                $resp = $c->execute($req, $accessToken);

                # json格式转换为数组
                $response = json_decode($resp, true);

                //系统级参数错误
                if (isset($response['error_response'])) {
                    return $this->msgObj->outputJd(1, $response['error_response']['en_desc'] . '(' . $response['error_response']['code'] . '):' . $response['error_response']['zh_desc'], '', $this->logTxt);
                }

                $response = $response['jingdong_ldop_alpha_provider_sign_success_info_get_responce']['resultInfo'];

                # 响应校验
                if ($response['statusCode']) {
                    return $this->msgObj->outputJd(1, '错误:' . $response['statusMessage'], '', $this->logTxt);
                }

                $responseData = $response['data'];

                # 入库操作
                # 检查审核成功签约信息是否已存在，存在更新，不存在新增
                $db = new DbAction();

                # 开启事务
                $db->beginTransaction();

                # csk_seller_waybill_info表操作
                $sqlBill = 'SELECT
                            count(*) AS total
                        FROM
                            csk_seller_waybill_info
                        WHERE
                            seller_id = :seller_id
                            AND cp_code = :providerCode
                            AND branch_code = :branchCode
                            AND is_jd = \'1\' ';


                # 京东商家对应配置信息存放
                $sqlAmount = 'SELECT
                                    count(*) AS total
                                FROM
                                    csk_merchant
                                WHERE
                                    vendor_code = :vendorCode
                                AND provider_code = :providerCode';


                # 批量处理商家签约审核信息
                foreach ($responseData as $item) {

                    $item['branchCode'] = empty($item['branchCode']) ? 0 : $item['branchCode'];

                    # 预处理数据绑定
                    $tmpParams = array(':seller_id' => $param['seller_id'],
                        ':providerCode' => $item['providerCode'],
                        ':branchCode' => $item['branchCode']);

                    $result = $db->fetchOne($sqlBill, $tmpParams);

                    # 地址详情
                    $address = $item['address'];

                    # 已存在审核信息，更新
                    if ($result['total'] > 0) {
                        # 更新
                        # $info = '更新';

                        $data = array('ship_prov' => $address['provinceName'],
                            'ship_prov_id' => $address['provinceId'],
                            'ship_city' => $address['cityName'],
                            'ship_city_id' => $address['cityId'],
                            'ship_county' => $address['countryName'],
                            'ship_county_id' => $address['countryId'],
                            'ship_town' => $address['countrysideName'],
                            'ship_town_id' => $address['countrysideId'],
                            'ship_detail_address' => $address['address'],
                            'branch_name' => $item['branchName'],
                            'quantity' => $item['amount'],
                            'cp_type' => $item['operationType'],
                            'cp_code' => $item['providerCode'],
                            'provider_name' => $item['providerName'],
                            'provider_type' => $item['providerType'],
                            'support_cod' => $item['supportCod'],
                            'settlement_code' => $item['settlementCode'],
                            'update_time' => date('Y-m-d H:i:s')
                        );

                        # 参数绑定
                        $tmpWhere = array(':seller_id' => $param['seller_id'],
                            ':providerCode' => $item['providerCode'],
                            ':branchCode' => $item['branchCode']);

                        # 更新数据
                        $db->update(
                            'csk_seller_waybill_info',
                            $data,
                            'seller_id = :seller_id AND cp_code = :providerCode AND branch_code = :branchCode AND is_jd = \'1\'',
                            $tmpWhere);

                    } else {
                        # $info = '新增';

                        # 参数绑定
                        $tmpParams = array('seller_id' => $param['seller_id'], 'ship_prov' => $address['provinceName'], 'ship_prov_id' => $address['provinceId'], 'ship_city' => $address['cityName'], 'ship_city_id' => $address['cityId'], 'ship_county' => $address['countryName'], 'ship_county_id' => $address['countryId'], 'ship_town' => $address['countrysideName'], 'ship_town_id' => $address['countrysideId'], 'ship_detail_address' => $address['address'], 'branch_code' => $item['branchCode'], 'branch_name' => $item['branchName'], 'quantity' => $item['amount'], 'cp_type' => $item['operationType'], 'cp_code' => $item['providerCode'], 'provider_name' => $item['providerName'], 'provider_type' => $item['providerType'], 'support_cod' => $item['supportCod'], 'settlement_code' => $item['settlementCode'], 'is_jd' => '1', 'create_time' => date('Y-m-d H:i:s'));

                        $db->insert('csk_seller_waybill_info', $tmpParams);

                    }

                    # 预处理绑定
                    $tmpParamsA = array(':vendorCode' => $param['seller_id'], ':providerCode' => $item['providerCode']);

                    $resultA = $db->fetchOne($sqlAmount, $tmpParamsA);

                    # 存在更新，不存在新增
                    if ($resultA['total'] > 0) {

                        $db->update('oms.csk_merchant', array('provider_id' => $item['providerId'], 'provider_name' => $item['providerName'], 'update_time' => date('Y-m-d H:i:s')), 'vendor_code = :vendorCode AND provider_code = :providerCode', array(':vendorCode' => $param['seller_id'], ':providerCode' => $item['providerCode']));

                    } else {

                        $db->insert('oms.csk_merchant', array('vendor_code' => $param['seller_id'], 'provider_code' => $item['providerCode'], 'provider_id' => $item['providerId'], 'provider_name' => $item['providerName'], 'create_time' => date('Y-m-d H:i:s')));

                    }

                }

                $db->commit();

                $rs = array('statusCode' => 0, 'statusMessage' => '商家审核信息处理成功',);

                return $this->msgObj->outputJd(0, '商家审核信息处理成功', json_encode($rs), $this->logTxt);


            } catch (PDOException $e) {

                $db->rollback();

                return $this->msgObj->outputJd(1, $e->getMessage(), '', $this->logTxt);
            }

        } catch (Exception $e) {

            return $this->msgObj->outputJd(1, $e->getMessage(), '', $this->logTxt);
        }
    }

    /**
     * 预处理间值组合
     * @param $data
     * @return array
     */
    public function keyChange($data)
    {
        $res = array();

        foreach ($data as $item) {

            $res[':seller_id'] = $item['venderCode'];
            $res[':ship_prov'] = $item['provinceName'];
            $res[':ship_prov_id'] = $item['provinceId'];
            $res[':ship_city'] = $item['cityName'];
            $res[':ship_city_id'] = $item['cityId'];
            $res[':ship_county'] = $item['countryName'];
            $res[':ship_county_id'] = $item['countryId'];
            $res[':ship_town'] = $item['countrysideName'];
            $res[':ship_town_id'] = $item['countrysideId'];
            $res[':ship_detail_address'] = $item['address'];
            $res[':branch_code'] = $item['branchCode'];
            $res[':branch_name'] = $item['branchName'];
            $res[':quantity'] = $item['amount'];
            $res[':cp_type'] = $item['operationType'];
            $res[':cp_code'] = $item['providerCode'];
            $res[':provider_name'] = $item['providerName'];
            $res[':provider_type'] = $item['providerType'];
            $res[':support_cod'] = $item['supportCod'];
            $res[':settlement_code'] = $item['settlementCode'];
        }


        return $res;
    }
}