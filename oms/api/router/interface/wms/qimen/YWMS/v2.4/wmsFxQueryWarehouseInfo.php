<?php
/**
 * 纸品分销目的分拨和仓编码查询接口处理类
 * User: Renee
 * Date: 2018/11/21
 * Time: 11:01
 */
require API_ROOT . '/router/interface/wms/qimen/YWMS/wmsRequest.php';
class wmsFxQueryWarehouseInfo extends wmsRequest {
    public function query($params)
    {
        if (empty($params)) {
            return $this->msgObj->outputQimen('failure', '失败：请求的数据为空', 'S003');
        } else {
            $senderAddress = $params['senderInfo']['province'] . $params['senderInfo']['city'] . $params['senderInfo']['area'] . $params['senderInfo']['town'] . $params['senderInfo']['detailAddress'];
            $receiverAddress = $params['receiverInfo']['province'] . $params['receiverInfo']['city'] . $params['receiverInfo']['area'] . $params['receiverInfo']['town'] . $params['receiverInfo']['detailAddress'];

            if ($receiverAddress != '' || $senderAddress != '') {
                //组合调用地址归集三期接口的xml数据
                $xmlData = '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>';
                $xmlData .= '<list  xmlns="http://www.yundaex.com/schema/2016" auxids="aux_pack,bigpen">';
                $xmlData .= '<item>';
                $xmlData .= '<rid>1</rid>';
                $xmlData .= '<address_s>' . $senderAddress . '</address_s>';
                $xmlData .= '<address_e>' . $receiverAddress . '</address_e>';
                $xmlData .= '</item>';
                $response = $this->utilObj->curl(URL_GET_DISTRICENTER_CODE, $xmlData);

                $xmlObj = new xml();
                if ($response != '' && $xmlObj->isXml($response)) {
                    $responseArr = $xmlObj->xmlStr2array($response);

                    //判断是网点下单还是客户下单
                    if ($params['custmSource'] == 'customer') {//客户
                        if (!empty($responseArr['item']['site_e']) && preg_match("/^\d{6}$/", $responseArr['item']['site_e'])) {
                            $warehouseCode = $responseArr['item']['site_e'];
                            $warehouseName = $this->getDistbtName($warehouseCode);
                            $warehouseName = empty($warehouseName) ? $responseArr['item']['site_e'] : $warehouseName ;
                            $distbtCode='';
                            $distbtName='';
                        } else {
                            return $this->msgObj->outputQimen('failure', '失败：匹配到的仓库编码' . $responseArr['item']['site_e'] . '格式错误！', 'S003');
                        }
                    } else {
                        //判断接口返回的分拨中心编码是否为空及其格式是否符合
                        if (!empty($responseArr['item']['districenter_code']) && preg_match("/^\d{6}$/", $responseArr['item']['districenter_code'])) {
                            $distbtCode = $responseArr['item']['districenter_code'];
                            $warehouseInfo = $this->getWarehouse($distbtCode);
                            if (empty($warehouseInfo['warehouse_code'])) {
                                return $this->msgObj->outputQimen('failure', '失败：没有匹配到区域仓!', 'S003');
                            } else {
                                if (empty($responseArr['item']['districenter_name']) || $responseArr['item']['districenter_name']='null') {
                                    $distbtName = $this->getDistbtName($responseArr['item']['districenter_code']);
                                } else {
                                    $distbtName = $responseArr['item']['districenter_name'];
                                }
                                $warehouseCode = $warehouseInfo['warehouse_code'];
                                $warehouseName = empty($warehouseInfo['warehouse_name']) ? $warehouseInfo['warehouse_code'] : $warehouseInfo['warehouse_name'] ;
                            }
                        } else {
                            if (empty($responseArr['item']['districenter_code'])) {
                                return $this->msgObj->outputQimen('failure', '失败：没有匹配到区域仓编码!', 'S003');
                            } else {
                                return $this->msgObj->outputQimen('failure', '失败：匹配到的目的分拨编码!' . $responseArr['item']['districenter_code'] . '格式错误！', 'S003');
                            }
                        }
                    }

                    //拼接报文请求
                    $str  = '<distributionCode>' . $distbtCode . '</distributionCode>';
                    $str .= '<distributionName>' . $distbtName . '</distributionName>';
                    $str .= '<warehouseCode>' . $warehouseCode . '</warehouseCode>';
                    $str .= '<warehouseName>' . $warehouseName . '</warehouseName>';

                    $data = '<?xml version="1.0" encoding="utf-8"?><response><flag>success</flag><code>0000</code> <message>succ</message><info>';
                    $data .= $str;
                    $data .= '</info></response>';

                    $addon = array(
                        'api_url' => self::$wmsApi,
                        'api_method' => qimen_service::$_method,
                        'return_msg' => $data
                    );
                    return $this->msgObj->outputQimen('success', 'succ', '0000', $addon);
                } else {
                    return $this->msgObj->outputQimen('failure', '失败：目的分拨获取失败!', 'S003');
                }
            }
        }
    }

    /**
     * 获取分拨中心名称
     * @param $bm
     * @return mixed
     */
    public function getDistbtName($bm)
    {
        global $db;
        $sql = "SELECT mc FROM ydserver.gs WHERE bm={$bm}";
        $distbtInfo = $db->query($sql)->fetch(PDO::FETCH_ASSOC);

        return $distbtInfo['mc'];
    }

    /**
     * 获取仓库编码信息
     * @param $districenterCode
     * @return mixed
     */
    public function getWarehouse($districenterCode)
    {
        global $db;

        $sql  = "SELECT a.warehouse_code AS warehouse_code,b.descr_c AS warehouse_name FROM t_fx_area_warehouse_relation a ";
        $sql .= " LEFT JOIN t_base_warehouse b ON a.warehouse_code=b.warehouse_code ";
        $sql .= " WHERE a.fb_code='$districenterCode';";
        $warehouseInfo = $db->query($sql)->fetch(PDO::FETCH_ASSOC);

        return $warehouseInfo;
    }
}