<?php
/**
 * Description: 拼多多查询面单订购及面单使用情况
 * User: XL
 * Date: 2019/3/27 0027 10:19
 */

require API_ROOT . 'router/interface/erp/pdd/erpRequest.php';

class pddWaybillSearch extends erpRequest
{


    public function search($params)
    {

        try {

            if (empty($params['seller_id'])) {

                return $this->outputPdd(0, 'S003', '请求数据不能为空');
            }

            $sellerId = $params['seller_id'];

            # 获取商家token信息
            $accessToken = OmsDatabase::$oms_db->fetchOne('access_token', 'csk_seller_access_token', 'seller_id = :seller_id', [':seller_id' => $sellerId]);

            $res = $this->send($accessToken['access_token']);

            # 错误消息
            if (isset($res['error_response'])) {

                $errorResponse = $res['error_response'];

                return $this->msgObj->outputPdd(4, $errorResponse['error_code'], $errorResponse['error_msg'], $res['addon']);
            }

            # 数据处理更新
            $data = json_decode($res['addon']['return_msg'], true);

            $data = isset($data['pdd_waybill_search_response']['waybill_apply_subscription_cols'][0]) ? $data['pdd_waybill_search_response']['waybill_apply_subscription_cols'] : [$data['pdd_waybill_search_response']['waybill_apply_subscription_cols']];

            # 数据处理
            foreach ($data as $datum) {

                # 查询是否存在此商家面单订购情况[seller_id + wp_type + wp_code + branch_code确定唯一面单信息]
                $branchCols = isset($datum['branch_account_cols'][0]) ? $datum['branch_account_cols'] : [$datum['branch_account_cols']];

                foreach ($branchCols as $item) {

                    $result = OmsDatabase::$oms_db->fetchOne('count(*) as total',
                        'csk_seller_waybill_info',
                        'seller_id = :seller_id and cp_type = :wp_type and cp_code = :wp_code and branch_code = :branch_code and is_jd=2',
                        [
                            ':seller_id' => $sellerId,
                            ':wp_type' => $datum['wp_type'],
                            ':wp_code' => $datum['wp_code'],
                            ':branch_code' => isset($item['branch_code']) ? $item['branch_code'] : $datum['wp_code']
                        ]);

                    # 处理相关快递公司信息，无则新增，有不处理
                    $this->merchantDeal($sellerId, $datum['wp_code']);


                    # 存在更新，不存在新增
                    if ($result['total'] <= 0) {

                        OmsDatabase::$oms_db->insert(
                            'csk_seller_waybill_info',
                            [
                                # 商家ID
                                'seller_id' => $sellerId,
                                # 快递公司类型(1:直营，2:加盟)
                                'cp_type'   => $datum['wp_type'],
                                # 快递公司编码
                                'cp_code'   => $datum['wp_code'],
                                # 发件网点编码
                                'branch_code' => isset($item['branch_code']) ? $item['branch_code'] : $datum['wp_code'],
                                # 发件网点名称
                                'branch_name' => isset($item['branch_name']) ? $item['branch_name'] : '',
                                # 总分配单号数(已用面单数)
                                'allocated_quantity'    => $item['allocated_quantity'],
                                # 单号取消数
                                'cancel_quantity' => $item['cancel_quantity'],
                                # 当前余量
                                'quantity'    => $item['quantity'],
                                # 发货详细地址
                                'ship_detail_address' => $item['shipp_address_cols'][0]['detail'],
                                # 省
                                'ship_prov'     => $item['shipp_address_cols'][0]['province'],
                                # 市
                                'ship_city'     => $item['shipp_address_cols'][0]['city'],
                                # 区
                                'ship_county'     => $item['shipp_address_cols'][0]['district'],
                                # 街道
                                'ship_town'     => $item['shipp_address_cols'][0]['town'],
                                # 创建时间
                                'create_time'   => date("Y-m-d H:i:s"),
                                # 电子面单平台 2: 拼多多
                                'is_jd'         => 2
                            ]
                        );

                    } else {

                        # 更新数据
                        OmsDatabase::$oms_db->update(
                            'csk_seller_waybill_info',
                            [
                                # 发件网点名称
                                'branch_name' => isset($item['branch_name']) ? $item['branch_name'] : '',
                                # 总分配单号数(已用面单数)
                                'allocated_quantity'    => $item['allocated_quantity'],
                                # 单号取消数
                                'cancel_quantity' => $item['cancel_quantity'],
                                # 当前余量
                                'quantity'    => $item['quantity'],
                                # 发货详细地址
                                'ship_detail_address' => $item['shipp_address_cols'][0]['detail'],
                                # 省
                                'ship_prov'     => $item['shipp_address_cols'][0]['province'],
                                # 市
                                'ship_city'     => $item['shipp_address_cols'][0]['city'],
                                # 区
                                'ship_county'     => $item['shipp_address_cols'][0]['district'],
                                # 街道
                                'ship_town'     => $item['shipp_address_cols'][0]['town']
                            ],
                            'seller_id = :seller_id and cp_type = :wp_type and cp_code = :wp_code and branch_code = :branch_code and is_jd=2',
                            [
                                # 商家ID
                                ':seller_id' => $sellerId,
                                # 快递公司类型(1:直营，2:加盟)
                                ':wp_type'   => $datum['wp_type'],
                                # 快递公司编码
                                ':wp_code'   => $datum['wp_code'],
                                # 发件网点编码
                                ':branch_code' => isset($item['branch_code']) ? $item['branch_code'] : $datum['wp_code']
                            ]

                        );
                    }
                }
            }
            return $this->msgObj->outputPdd(1, '0000', '刷新成功', $res['addon']);

        } catch (\PDOException $p) {


            $logName = date('Ymd') . '_pdd_execute_log.log';
            error_log(print_r($p->getMessage(), 1) . PHP_EOL, 3, LOG_PATH . $logName);
            return $this->msgObj->outputPdd(0,$p->getCode(),$p->getMessage());

        } catch (\Exception $e) {

            $logName = date('Ymd') . '_pdd_execute_log.log';
            error_log(print_r($e->getMessage(), 1) . PHP_EOL, 3, LOG_PATH . $logName);
            return $this->msgObj->outputPdd(0,$e->getCode(),$e->getMessage());
        }
    }

    /**
     * 处理相关商家快递公司信息
     * @param $sellerId  商家id
     * @param $wp_code   快递公司编码
     */
    protected function merchantDeal($sellerId, $wp_code)
    {

        # 查询承运商信息
        $merchantInfo = OmsDatabase::$oms_db->fetchOne('provider_code', 'csk_merchant_deploy',
            'vendor_code = :seller_id and provider_code = :wp_code and type = 1',
            [
                ':seller_id' => $sellerId,
                ':wp_code'   => $wp_code
            ]);

        # 没有快递公司相关信息则新增，否则不作处理
        if (empty($merchantInfo)) {

            # 获取快递公司名称和id
            $merchantRes = OmsDatabase::$oms_db->fetchOne('provider_id, provider_name', 'csk_pdd_logistic_info',
                'provider_code = :wp_code',
                [':wp_code' => $wp_code]);


            # 新增快递公司信息到csk_merchant_deploy表
            OmsDatabase::$oms_db->insert(
                'csk_merchant_deploy',
                [
                    'vendor_code' => $sellerId,
                    'provider_code' => $wp_code,
                    'provider_id' => isset($merchantRes['provider_id']) ? $merchantRes['provider_id'] : '',
                    'provider_name' => isset($merchantRes['provider_name']) ? $merchantRes['provider_name'] : '',
                    'type'          => 1,
                    'create_time' => date('Y-m-d H:i:s')
                ]
            );
        }

    }
}