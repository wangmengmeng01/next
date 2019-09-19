<?php
/**
 * @User: [cf]
 * @DateTime: 2017/7/14 17:02
 * @description:
 */
require API_ROOT . '/router/interface/erp/storage/common/cnRequest.php';

class erpItemQuery extends cnRequest{
    public function create($data,$arrData){
        $response = $this->send($data);
        
        if ($response['success'] == 1) {
            $returnMsg = $response['addon']['return_msg'];
            $xmlObj = new xml();
            $queryItemsInfoArr = $xmlObj->xmlStr2array($returnMsg);
            $queryItemsInfoArr = $this->utilObj->filter_null($queryItemsInfoArr);
            if (empty($queryItemsInfoArr['itemList']['item'])) {
                return $this->msgObj->outputCnStorage(false, '查询商品失败：商品明细为空！', 'S003');
            } else {
                $pushRs = $this->pushPrdctInfo($queryItemsInfoArr);
                if ($pushRs['success'] != 1) {
                    return $this->msgObj->outputCnStorage(false, $pushRs['errorMsg'], $pushRs['errorCode']);
                } else {
                    return $this->msgObj->outputCnStorage(true);
                }
            }
        } else {
            return $this->msgObj->outputCnStorage(false, $response['errorMsg'], $response['errorCode']);
        }
    }
    
    public function pushPrdctInfo($queryItemsInfoArr){
        if (empty($queryItemsInfoArr['itemList']['item'][0])) {
            $queryItemsInfoArr['itemList']['item'] = array($queryItemsInfoArr['itemList']['item']);
        }
        //商品创建接口报文
        foreach ($queryItemsInfoArr['itemList']['item'] as $item) {
            $itemCreateXml  = '<?xml version="1.0" encoding="utf-8"?><request>';
            $itemCreateXml .= '<actionType>ADD</actionType>';
            $itemCreateXml .= '<storeCode>'       . cn_storage_service::$_logistic_provider_id . '</storeCode>';
            $itemCreateXml .= '<ownerUserId>'     . $item['providerTpId']    . '</ownerUserId>';
            $itemCreateXml .= '<itemId>'          . $item['itemId']          . '</itemId>';
            $itemCreateXml .= '<itemCode>'        . $item['itemCode']        . '</itemCode>';
            $itemCreateXml .= '<name>'            . $item['itemName']        . '</name>';
            $itemCreateXml .= '<barCode>'         . $item['barCode']         . '</barCode>';
            $itemCreateXml .= '<itemVersion>'     . $item['version']         . '</itemVersion>';
            $itemCreateXml .= '<type>'            . $item['type']            . '</type>';
            $itemCreateXml .= '<category>'        . $item['itemName']        . '</category>';
            $itemCreateXml .= '<categoryName>'    . $item['itemName']        . '</categoryName>';
            $itemCreateXml .= '<brand>'           . $item['brand']           . '</brand>';
            $itemCreateXml .= '<brandName>'       . $item['brandName']       . '</brandName>';
            $itemCreateXml .= '<color>'           . $item['color']           . '</color>';
            $itemCreateXml .= '<size>'            . $item['size']            . '</size>';
            $itemCreateXml .= '<grossWeight>'     . $item['grossWeight']     . '</grossWeight>';
            $itemCreateXml .= '<netWeight>'       . $item['netWeight']       . '</netWeight>';
            $itemCreateXml .= '<length>'          . $item['length']          . '</length>';
            $itemCreateXml .= '<width>'           . $item['width']           . '</width>';
            $itemCreateXml .= '<height>'          . $item['height']          . '</height>';
            $itemCreateXml .= '<volume>'          . $item['volume']          . '</volume>';
            $itemCreateXml .= '<pcs>'             . $item['pcs']             . '</pcs>';
            $itemCreateXml .= '<approvalNumber>'  . $item['approvalNumber']  . '</approvalNumber>';
            $itemCreateXml .= '<isShelflife>'     . $item['isShelflife']     . '</isShelflife>';
            $itemCreateXml .= '<lifecycle>'       . $item['lifecycle']       . '</lifecycle>';
            $itemCreateXml .= '<rejectLifecycle>' . $item['rejectLifecycle'] . '</rejectLifecycle >';
            $itemCreateXml .= '<lockupLifecycle>' . $item['lockupLifecycle'] . '</lockupLifecycle>';
            $itemCreateXml .= '<adventLifecycle>' . $item['adventLifecycle'] . '</adventLifecycle>';
            $itemCreateXml .= '<isSnMgt>'         . $item['isSnMgt']         . '</isSnMgt>';
            $itemCreateXml .= '<isHygroscopic>'   . $item['isHygroscopic']   . '</isHygroscopic>';
            $itemCreateXml .= '<isDanger>'        . $item['isDanger']        . '</isDanger>';
            $itemCreateXml .= '<dosageForms>'     . $item['dosageForms']     . '</dosageForms>';
            $itemCreateXml .= '<producingArea>'   . $item['producingArea']   . '</producingArea>';
            $itemCreateXml .= '<firstState>'      . $item['firstState']      . '</firstState>';
            $itemCreateXml .= '<costPrice>'       . $item['costPrice']       . '</costPrice>';
            $itemCreateXml .= '<tagPrice>'        . $item['tagPrice']        . '</tagPrice>';
            $itemCreateXml .= '<retailPrice>'     . $item['retailPrice']     . '</retailPrice>';
            $itemCreateXml .= '<purchasePrice>'   . $item['purchasePrice']   . '</purchasePrice>';
            $itemCreateXml .= '<isProduceCodeMgt>'. $item['isProduceCodeMgt']. '</isProduceCodeMgt>';
            $itemCreateXml .= '</request>';
        
            $syncRs = $this->requestOmsToWms($itemCreateXml, 'WMS_SKU_INFO_NOTIFY');
            if ($syncRs == 0) {
                return $this->msgObj->outputCnStorage(false, $syncRs['errorMsg'], $syncRs['errorCode']);
            }
        }
        return $this->msgObj->outputCnStorage(true);
    }
}
