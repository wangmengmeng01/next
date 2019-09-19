<?php
/**
 * 标准参数格式转换
 */
class format
{

    /**
     * 请求参数转换
     * @param $request
     * @return SimpleXMLElement
     */
    public function request($requestData)
    {   	
    	$xmlObj = new xml();
    	//将xml转换为数组
    	$requestData = $xmlObj->xmlStr2array($requestData);    	
        //过滤数组中的空数组
        $utilObj = new util();
        $requestData = $utilObj->filter_null($requestData);      
        
        //当接口类型为发货单创建接口时，根据发货单中的收件人地址匹配不同的仓库编码(当发货单中有仓库编码时直接使用原有的仓库编码)
        if (qimen_service::$_method == 'deliveryorder.create' && (empty($requestData['deliveryOrder']['warehouseCode']) || $requestData['deliveryOrder']['warehouseCode'] == 'OTHER')) {
        	$receiverAddress = $requestData['deliveryOrder']['receiverInfo']['province'] . $requestData['deliveryOrder']['receiverInfo']['city'] . $requestData['deliveryOrder']['receiverInfo']['area'] . $requestData['deliveryOrder']['receiverInfo']['town'] . $requestData['deliveryOrder']['receiverInfo']['detailAddress'];
        	if ($receiverAddress != '') {
        		//组合调用地址归集三期接口的xml数据
        		$xmlData = '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>';
        		$xmlData .= '<list  xmlns="http://www.yundaex.com/schema/2012">';
        		$xmlData .= '<item>';
        		$xmlData .= '<rid>1</rid>';
        		$xmlData .= '<address>' . $receiverAddress . '</address>';
        		$xmlData .= '</item>';
        		$response = $utilObj->curl(URL_SELECT_ORDER_THIRD, $xmlData);
        		
        		if ($response != '' && $xmlObj->isXml($response)) {
        			$responseArr = $xmlObj->xmlStr2array($response);
        			if (!empty($responseArr['item']['code']) && preg_match("/^\d{6}$/", $responseArr['item']['code'])) {
        				$countyId = $responseArr['item']['code'];
        				$warehouseCode = $this->get_warehouse(qimen_service::$_customerId, $countyId);
        				if ($warehouseCode != '') {
        					$requestData['deliveryOrder']['warehouseCode'] = $warehouseCode;
        					qimen_service::$_data = preg_replace("/<warehouseCode>(.*)<\/warehouseCode>/s", '<warehouseCode>' . $warehouseCode . '</warehouseCode>', qimen_service::$_data);
        				} else {
        					throw new Exception('区域编码' . $countyId . '没有匹配到对应的仓库编码！');
        				}
        			} else {
        				if (empty($responseArr['item']['code'])) {
        					throw new Exception('没有匹配到区域编码！');
        				} else {
        					throw new Exception('匹配到的区域编码' . $responseArr['item']['code'] . '格式错误！');
        				}
        			}
        		} else {
        			throw new Exception('区域编码获取失败！');
        		}
        	} else {
        		throw new Exception('收件人地址不完整！');
        	}
        }
        return $requestData;
    }

    /**
     * 获取根据货主和行政编码获取对应的仓库编码
     */
    public function get_warehouse($customerId, $regionCode)
    {
    	global $db;
    	$warehouseCode = '';
    	//查找货主所有仓库编码对应的行政规划编码
    	$sql = "SELECT * FROM t_region_warehouse_relation WHERE customer_id='$customerId' AND valid=1 GROUP BY region_code,region_type";
    	$rs = $db->query($sql)->fetchAll(PDO::FETCH_ASSOC);
    	$warehouseArr = array();
    	if (!empty($rs)) {
    		foreach ($rs as $val)
    		{
    			$warehouseArr[$val['region_type']][$val['region_code']] = $val['warehouse_code'];
    		}
    		//获取行政编码对应的仓库编码
    		$sql = "SELECT a.countyid,b.cityid,b.provinceid FROM ydserver.county a LEFT JOIN ydserver.city b ON a.cityid=b.cityid WHERE a.countyid='$regionCode'";
    		$rsRegion = $db->query($sql)->fetch(PDO::FETCH_ASSOC);
    		if (empty($rsRegion)) {
    			$sql = "SELECT '' AS countyid,cityid,provinceid FROM ydserver.city WHERE cityid='$regionCode'";
    			$rsRegion = $db->query($sql)->fetch(PDO::FETCH_ASSOC);
    			if (empty($rsRegion)) {
    				$sql = "SELECT '' AS countyid,'' AS cityid,provinceid FROM ydserver.province WHERE provinceid='$regionCode'";
    				$rsRegion = $db->query($sql)->fetch(PDO::FETCH_ASSOC);
    			}
    		}
    		if (empty($rsRegion)) {
    			$rsRegion['countyid'] = $regionCode;
    			$rsRegion['cityid'] = substr($regionCode, 0, 4) . '00';
    			$rsRegion['provinceid'] = substr($regionCode, 0, 2) . '0000';
    		}
    		if (!empty($rsRegion)) {
    			if (!empty($warehouseArr[3][$rsRegion['countyid']])) {
    				$warehouseCode = $warehouseArr[3][$rsRegion['countyid']];
    			} elseif (!empty($warehouseArr[2][$rsRegion['cityid']])) {
    				$warehouseCode = $warehouseArr[2][$rsRegion['cityid']];
    			} elseif (!empty($warehouseArr[1][$rsRegion['provinceid']])) {
    				$warehouseCode = $warehouseArr[1][$rsRegion['provinceid']];
    			} elseif (!empty($warehouseArr[4][0])) {
    				$warehouseCode = $warehouseArr[4][0];
    			}
    		} 
    	}
    	return $warehouseCode;
    }
}
