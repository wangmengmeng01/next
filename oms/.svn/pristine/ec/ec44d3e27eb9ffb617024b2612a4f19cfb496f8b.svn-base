<?php
    class ShipmentsQtyCommand extends CConsoleCommand {
        /***
         * Notes:计算仓库发货量
         * Date: 2019/6/25
         * Time: 13:48
         */
        public function actionCount() {
        	//只有每天早上6点之后执行一次
        	$endDate = date("Y-m-d", strtotime("-1 day"));  //需要统计截止时间
        	
        	if (date('H:i') < '06:00') {
        	    die('time error');
        	}
        	
        	include_once ROOT_DIR . 'api/ext/httpclient.php';
        	include_once ROOT_DIR . 'api/ext/xml.php';
        	
        	//连接数据库
        	$db = Yii::app()->db;
        	
        	//获取日志中的时间
        	$logFileName = ROOT_DIR . 'protected/runtime/uploadFiles/shipmentQty.log';
        	if (file_exists($logFileName)) {
        		$logDate = file_get_contents($logFileName);
        		if ($logDate >= $endDate) {
        			die('date is exists');
        		} else {
        			$startDate = $logDate;
        		}
        	} else {
        		$startDate = $endDate;
        	}
        	
        	//查询货主对应的网点和省份编码
        	$sql = 'SELECT a.customer_id,a.branch_code,d.provinceid FROM t_base_customer a LEFT JOIN ydserver.gs b ON a.branch_code=b.bm LEFT JOIN ydserver.county c ON b.szd=c.CountyID LEFT JOIN ydserver.city d on c.CityID=d.CityID';
        	$model = $db->createCommand($sql);
        	$rsCustomer = $model->queryAll();
        	$customerArr = array();
        	if (!empty($rsCustomer)) {
        	    foreach ($rsCustomer as $val)
        	    {
        	        $customerArr[$val['customer_id']]['customer_id'] = $val['customer_id'];
        	        $customerArr[$val['customer_id']]['branch_code'] = $val['branch_code'];
        	        $customerArr[$val['customer_id']]['provinceid'] = $val['provinceid'];
        	    }
        	}
        	
        	//获取发货量表字段信息
        	$column_arr = $this->get_dataBase_relation('shipments_info');
        	$column_key = implode(',', array_values($column_arr)) . ',create_time';
        	$column_value = ":" . implode(",:", array_keys($column_arr)) . ",now()";
        	
        	//按天循环统计开始日期至截止日期之间的数据
        	for ($start = $startDate; $start <= $endDate; $start = date("Y-m-d", strtotime("+1 day", strtotime($start))))
        	{
        	    $startTime = $start . ' 00:00:00';
        	    $endTime = $start . ' 23:59:59';
        		//删除历史数据
        		$sqlDel = "DELETE FROM t_shipments_info WHERE rq = '$start'";
        		$model = $db->createCommand($sqlDel);
        		$model->execute();
        		
        		/**
        		 * 写入发货量数据
        		 */
        		//根据货主划分查询出当前时间前一天的发货量
        		$sql = "SELECT COUNT(DISTINCT a.delivery_no) as shipments_qty,b.customer_id as customer_id,b.warehouse_code as warehouse_code
        		FROM t_outbound_detail_record a
        		LEFT JOIN t_outbound_info_record b ON a.order_id = b.order_id
        		WHERE a.delivery_no !='' AND a.create_time>='$startTime' AND a.create_time<='$endTime'
        		GROUP BY b.customer_id";
        		$model = $db->createCommand($sql);
        		$rs = $model->queryAll();
        		
        		if (!empty($rs)) {
        		    foreach ($rs as $val)
        		    {
        		        //查询出wms编码
        		        $customerId = $val['customer_id'];
        		        $warehouseCode = $val['warehouse_code'];
        		        $shipmentsQty = $val['shipments_qty'];
        		        $wSql = 'SELECT a.wms_code FROM t_bind_relation a
        		             LEFT JOIN t_base_wms b
        		             ON a.wms_code = b.wms_code
        		             WHERE a.customer_id = :customer_id';
        		        $wModel = $db->createCommand($wSql);
        		        $wModel->bindValue(':customer_id', $customerId);
        		        $wmsRs = $wModel->queryRow();
        		        $wmsCode = $wmsRs['wms_code'];
        		    
        		        if (!empty($wmsRs) && $wmsCode != 'YWMS') {
    		                $countSql = "INSERT INTO t_shipments_info({$column_key}) VALUES({$column_value})";
    		                $countModel = $db->createCommand($countSql);
    		                $countModel->bindValue(':customer_id', $customerId);
    		                $countModel->bindValue(':warehouse_code', $warehouseCode);
    		                $countModel->bindValue(':branch_code', $customerArr[$customerId]['branch_code']);
    		                $countModel->bindValue(':province_id', $customerArr[$customerId]['provinceid']);
    		                $countModel->bindValue(':wms_code', $wmsCode);
    		                $countModel->bindValue(':shipments_qty', $shipmentsQty);
    		                $countModel->bindValue(':rq',$start);
    		                $countModel->execute();
        		        }
        		    }
        		}
        		echo $start . ': first finish !!!' . PHP_EOL;
        		
        		//调用YWMS接口
        		$xmlStr = "<?xml version='1.0' encoding='utf-8'?><request><ownerCode></ownerCode><warehouseCode></warehouseCode><startTime>$startTime</startTime><endTime>$endTime</endTime></request>";
        		
        		$apiParams = array(
        		    'method' => 'yd.shipnum.query',
        			'data' => $xmlStr
        		);
        		$httpObj = new httpclient();
        		error_log(print_r($apiParams,1),3,'F:/log/aaaa.log');
        		$rs = $httpObj->post('http://10.20.24.140:7001/wiq/external/serviceForQM.do',$apiParams);//  http://10.19.105.161:7001/wiq/external/serviceForQM.do
        		//uat环境：http://10.19.106.187:8081/wif/external/material/serviceForOms.do
        		error_log(print_r($rs,1),3,'F:/log/bbbbbb.log');
        		$xml = new xml();
        		$rs = urldecode($rs);
        		$response = $xml->xmlStr2array($rs);
        		
        		if ($response['flag'] == 'success') {
        		    $shipInfos = $response['shipInfos']['shipInfo'];
        		    if (!empty($shipInfos)) {
        		        if (empty($shipInfos[0])) {
        		            $shipInfos = array($shipInfos);
        		        }
        		        foreach ($shipInfos as $v) {
        		            $warehouseCode = $v['orgCode'];
        		            $customerId = $v['ownCode'];
        		            $shipmentsQty = $v['shipNum'];
        		            $date = $v['datetime'];
        		            $customerName = $v['ownName'];
        		            $warehouseName = $v['orgName'];
        		    
        		            $branchCode = empty($customerArr[$customerId]['branch_code']) ? '' : $customerArr[$customerId]['branch_code'];
        		            $provinceId = empty($customerArr[$customerId]['provinceid']) ? '' : $customerArr[$customerId]['provinceid'];
        		    
        		            $ySql = "INSERT INTO t_shipments_info({$column_key}". ",customer_name,warehouse_name" . ") VALUES({$column_value}" . ",:customer_name,:warehouse_name" . ")";
        		            $countModel = $db->createCommand($ySql);
        		            $countModel->bindValue(':customer_id', $customerId);
        		            $countModel->bindValue(':warehouse_code', $warehouseCode);
        		            $countModel->bindValue(':branch_code', $branchCode);
        		            $countModel->bindValue(':province_id', $provinceId);
        		            $countModel->bindValue(':wms_code', 'YWMS');
        		            $countModel->bindValue(':shipments_qty', $shipmentsQty);
        		            $countModel->bindValue(':rq', $date);
        		            $countModel->bindValue(':customer_name', $customerName);
        		            $countModel->bindValue(':warehouse_name', $warehouseName);
        		            $countModel->execute();
        		        }      
        		    }
        		}
        		echo $start . ': second finish !!!' . PHP_EOL;
        		
        		//写入已统计的日期
        		file_put_contents($logFileName, $start);
        	}
        	echo 'OK';
        }
        
        //获取t_shipments_info
        public function get_dataBase_relation($type) {
            $return_arr = array();
            if ($type == 'shipments_info') {
                $return_arr['customer_id'] = 'customer_id';
                $return_arr['warehouse_code'] = 'warehouse_code';
                $return_arr['branch_code'] = 'branch_code';
                $return_arr['province_id'] = 'province_id';
                $return_arr['wms_code'] = 'wms_code';
                $return_arr['shipments_qty'] = 'shipments_qty';
                $return_arr['rq'] = 'rq';
            }
            return $return_arr;
        }
        
    }
?>