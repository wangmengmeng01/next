<?php

/**
 * @author 敬
 * 功能： Excel导入
 */
class ImportController extends Controller
{
    private $errorArr = array();
    
    private $successNum = 0;
    
    private $customerArr = array();
    
    private $warehouseArr = array();
	
    public function actionIndex()
    {
        $this->render('index');
    }

    public function actionImport()
    {
    	//校验权限
    	util::operatePriContr(15.2);
    	//判断是否选择了文件
    	if (empty($_FILES["importFile"]['name'])) {
    		$this->render('index');
    		echo "<script type='text/javascript'>$.messager.alert('友情提示', '请先选择需要导入的excel文件！', 'info');</script>";
    		exit();
    	}
        // 判断文件类型，如果不是"xls"或者"xlsx"，则退出
        if ($_FILES["importFile"]["type"] == "application/download" || $_FILES["importFile"]["type"] == 'application/vnd.ms-excel' || $_FILES["importFile"]["type"] == 'text/html') {
        	if (strtolower(substr(strrchr($_FILES["importFile"]["name"],"."),1)) != 'xls') {
        		$this->render('index');
        		echo "<script type='text/javascript'>$.messager.alert('友情提示', '文件类型错误！', 'info');</script>";
        		exit();
        	}
        } elseif ($_FILES["importFile"]["type"] == "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") {
        } else {
        	$this->render('index');
        	echo "<script type='text/javascript'>$.messager.alert('友情提示', '文件类型错误！', 'info');</script>";
            exit();
        }  
        if($_FILES["importFile"]["size"]>5120000){
        	$this->render('index');
        	echo "<script type='text/javascript'>$.messager.alert('友情提示', '文件过大，最大为5M！', 'info');</script>";
        	exit();
        }      
        if ($_FILES["importFile"]["error"] > 0) {
            $this->render('index');
            echo "<script type='text/javascript'>$.messager.alert('友情提示', '" . $_FILES["importFile"]["error"] . "', 'info');</script>";
            exit();
        }       
        $fileName = date('Ymdhis') . '.' . $_FILES["importFile"]["name"];
        $inputFileName = APP_ROOT.'/protected/runtime/uploadFiles/' . $fileName;
        if (move_uploaded_file($_FILES["importFile"]["tmp_name"], $inputFileName)) {
            // PHPExcel读取excel文件
            self::import($inputFileName);
        } else {          
            $this->render('index');
            echo "<script type='text/javascript'>$.messager.alert('友情提示', '文件上传失败！', 'info');</script>";
            exit();
        }
    }

    public function import($filePath, $encode = 'utf-8')
    {
    	@set_time_limit(0);
    	@ini_set("memory_limit","500M");
        $PHPExcel = new PHPExcel();
        // 建立reader对象
        $PHPReader = new PHPExcel_Reader_Excel2007();
        if (! $PHPReader->canRead($filePath)) {
            $PHPReader = new PHPExcel_Reader_Excel5();
            if (! $PHPReader->canRead($filePath)) {
                $this->render('index');
                echo "<script type='text/javascript'>$.messager.alert('友情提示', '文件读取失败！', 'info');</script>";
                exit();
            }
        }
        $keys = array();
        $data = array();
        // 建立excel对象，此时你即可以通过excel对象读取文件，也可以通过它写入文件
        $PHPExcel = $PHPReader->load($filePath);        
        // 读取excel文件中的第一个工作表
        $currentSheet = $PHPExcel->getSheet(0);
        // 取得最大的列号
        $allColumn = $currentSheet->getHighestColumn();
        $columnKey = self::getColumnKey($allColumn);
        // 取得一共有多少行
        $allRow = $currentSheet->getHighestRow();
        // 循环读取每个单元格的内容。注意行从1开始，列从A开始
        for ($rowIndex = 1; $rowIndex <= $allRow; $rowIndex ++) {
            for ($colIndex = 0; $colIndex <= $columnKey; $colIndex ++) {
                $addr = self::getSCell($colIndex) . $rowIndex;
                $cell = $currentSheet->getCell($addr)->getValue();
                if ($cell instanceof PHPExcel_RichText) { // 富文本转换字符串
                    $cell = $cell->__toString();
                }
                if ($rowIndex == 1) {
                    $keys[] = $cell;
                } else {
                    $data[$rowIndex - 2][$keys[$colIndex]] = $cell;
                }
            }
        }
        //转换数据格式
        $formatArr = $this->formatOutbound($data);        
        //数据校验
        $filterArr = $this->filterOutbound($formatArr);
        // 写入数据库
        foreach ($filterArr as $value) {
            $this->insertOutbound($value);
            $this->successNum++;
        }
        $this->render('index', array('errorInfo' => $this->errorArr, 'successNum' => $this->successNum));
        echo "<script type='text/javascript'>$.messager.alert('友情提示', '文件导入成功！', 'info');</script>";
        //删除导入的文件
        @unlink($filePath);
        exit();
    }

    //把导入的excel中的数据转换为出库单标准格式
    public function formatOutbound($data)
    {
    	$returnArr = array();
    	$orderInfoArr = util::get_dataBase_relation('outbound_info');   	
    	$orderDetailArr = util::get_dataBase_relation('outbound_detail');
    	if (!empty($data)) {
    		foreach ($data as $key => $val)
    		{
    			foreach ($val as $k => $v)
    			{
    				$k =trim($k);
    				if (!empty($orderInfoArr[$k])) {
    					$returnArr[$key][$orderInfoArr[$k]] = $v;
    				}    				
    				if (!empty($orderDetailArr[$k])) {
    					$returnArr[$key][$orderDetailArr[$k]] = $v;
    				}    				
    			}   
    		}
    	}
    	return $returnArr;
    }
    
    //晓验出库单数据格式
    public function filterOutbound($data)
    {
    	if (!empty($data)) {
    		$db = Yii::app()->db;
    		$orderArr = array();
    		foreach ($data as $key => $val)
    		{
    			//校验必填字段是否有空
    			//校验订单号
    			if ($val['order_no'] == '') {
    				$this->errorArr[$key] = '订单号为必填项';
    				unset($data[$key]);
    				continue;
    			}
    			//校验订单类型
    			if ($val['order_type'] == '') {
    				$this->errorArr[$key] = '订单类型为必填项';
    				unset($data[$key]);
    				continue;
    			} elseif (!in_array($val['order_type'], array('SO', 'TO', 'RP', 'IL'))) {
    				$this->errorArr[$key] = '订单类型错误';
    				unset($data[$key]);
    				continue;
    			}
    			//校验货主
    			if ($val['customer_id'] == '') {
    				$this->errorArr[$key] = '货主代码为必填项';
    				unset($data[$key]);
    				continue;
    			}    			 
    			//校验仓库
    			if ($val['warehouse_code'] == '') {
    				$this->errorArr[$key] = '仓库代码为必填项';
    				unset($data[$key]);
    				continue;
    			}
    			//校验收货人信息
    			if ($val['consignee_name'] == '') {
    				$this->errorArr[$key] = '收货人为必填项';
    				unset($data[$key]);
    				continue;
    			}
    			if ($val['c_province'] == '') {
    				$this->errorArr[$key] = '省份为必填项';
    				unset($data[$key]);
    				continue;
    			}
    			if ($val['c_city'] == '') {
    				$this->errorArr[$key] = '城市为必填项';
    				unset($data[$key]);
    				continue;
    			}
    			if ($val['c_tel1'] == '') {
    				$this->errorArr[$key] = '手机号为必填项';
    				unset($data[$key]);
    				continue;
    			}		
    			//校验下单方
    			if ($val['user_define4'] == '') {
    				$this->errorArr[$key] = '下单方为必填项';
    				unset($data[$key]);
    				continue;
    			} elseif (!in_array($val['user_define4'], array('ERP', 'OMS'))) {
    				$this->errorArr[$key] = '下单方错误';
    				unset($data[$key]);
    				continue;
    			}
    			//校验渠道
    			if ($val['channel'] == '') {
    				$this->errorArr[$key] = '渠道为必填项';
    				unset($data[$key]);
    				continue;
    			}
    			//校验承运商编码
    			if ($val['carrier_id'] == '') {
    				$this->errorArr[$key] = '承运商编码为必填项';
    				unset($data[$key]);
    				continue;
    			}
    			//校验产品代码
    			if ($val['sku'] == '') {
    				$this->errorArr[$key] = '产品代码为必填项';
    				unset($data[$key]);
    				continue;
    			}
    			//校验订货数
    			if ($val['qty_ordered'] == '') {
    				$this->errorArr[$key] = '订货数为必填项';
    				unset($data[$key]);
    				continue;
    			} else {
    				if (!preg_match("/(^\d{1,18}$)|(^\d{1,10}\.?[\d]{1,8}$)/", $val['qty_ordered'])) {
    					$this->errorArr[$key] = '订货数只能为数值';
    					unset($data[$key]);
    					continue;
    				}
    			}
    			//校验单位
    			if ($val['uom'] == '') {
    				$this->errorArr[$key] = '单位为必填项';
    				unset($data[$key]);
    				continue;
    			}
    			//校验释放状态
    			if ($val['release_status'] == '') {
    				$this->errorArr[$key] = '释放状态为必填项';
    				unset($data[$key]);
    				continue;
    			} elseif (!in_array($val['release_status'], array('Y', 'N'))) {
    				$this->errorArr[$key] = '释放状态只能为Y和N';
    				unset($data[$key]);
    				continue;
    			}
    			//校验订单优先级
    			if ($val['priority'] == '') {
    				$this->errorArr[$key] = '订单优先级为必填项';
    				unset($data[$key]);
    				continue;
    			}
    			//校验单品标记
    			if ($val['single_match'] == '') {
    				$this->errorArr[$key] = '单品标记为必填项';
    				unset($data[$key]);
    				continue;
    			} elseif (!in_array($val['single_match'], array('Y', 'N'))) {
    				$this->errorArr[$key] = '单品标记只能为Y和N';
    				unset($data[$key]);
    				continue;
    			}
    			//校验是否打印发票
    			if ($val['invoice_print_flag'] !='' && !in_array($val['invoice_print_flag'], array('Y', 'N'))) {
    				$this->errorArr[$key] = '是否打印发票只能为Y和N';
    				unset($data[$key]);
    				continue;
    			}
    			//校验是否货到付款
    			if ($val['h_edi_05'] !='' && !in_array($val['h_edi_05'], array('Y', 'N'))) {
    				$this->errorArr[$key] = '是否货到付款只能为Y和N';
    				unset($data[$key]);
    				continue;
    			}
    			//校验是否保价
    			if ($val['h_edi_08'] !='' && !in_array($val['h_edi_08'], array('Y', 'N'))) {
    				$this->errorArr[$key] = '是否保价只能为Y和N';
    				unset($data[$key]);
    				continue;
    			}
    			//校验数据有效性
    			//校验货主是否存在
    			if (!in_array($val['customer_id'], $this->customerArr)) {   				   			
	    			$sql = "SELECT customer_id FROM t_base_customer WHERE customer_id=:customer_id AND active_flag='Y' AND is_valid=1";
	    			$param = $db->createCommand($sql);
	    			$param->bindValue(':customer_id', $val['customer_id']);
	    			$rs = $param->queryRow();
	    			if (empty($rs) || $rs['customer_id'] != $val['customer_id']) {
	    				$this->errorArr[$key] = '货主代码：' . $val['customer_id'] . '不存在或未激活';
	    				unset($data[$key]);
	    				continue;
	    			} else {
	    				$this->customerArr[$key] = $val['customer_id'];
	    			}
    			}
    			//校验仓库是否存在
    			if (!in_array($val['warehouse_code'], $this->warehouseArr)) {
	    			$sql = "SELECT warehouse_code FROM t_base_warehouse WHERE warehouse_code=:warehouse_code AND active_flag='Y' AND is_valid=1";
	    			$param = $db->createCommand($sql);
	    			$param->bindValue(':warehouse_code', $val['warehouse_code']);
	    			$rs = $param->queryRow();
	    			if (empty($rs) || $rs['warehouse_code'] != $val['warehouse_code']) {
	    				$this->errorArr[$key] = '仓库代码：' . $val['warehouse_code'] . '不存在或未激活';
	    				unset($data[$key]);
	    				continue;
	    			} else {
	    				$this->warehouseArr[$key] = $val['warehouse_code'];
	    			}
    			}
    			//校验订单号是否存在
    			if (!empty($orderArr[$val['customer_id']][$val['warehouse_code']][$val['order_type']][$val['order_no']])) {
    				$this->errorArr[$key] = '上游订单号：' . $val['order_no'] . '数据重复';
    				unset($data[$key]);
    				continue;
    			} else {
	    			$sql = "SELECT order_id FROM t_outbound_info WHERE order_no=:order_no AND order_type=:order_type AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND is_valid=1";
	    			$param = $db->createCommand($sql);
	    			$param->bindValue(':order_no', $val['order_no']);
	    			$param->bindValue(':order_type', $val['order_type']);
	    			$param->bindValue(':customer_id', $val['customer_id']);
	    			$param->bindValue(':warehouse_code', $val['warehouse_code']);
	    			$rs = $param->queryRow();
	    			if (!empty($rs)) {
	    				$this->errorArr[$key] = '订单号：' . $val['order_no'] . '已经存在';
	    				unset($data[$key]);
	    				continue;
	    			}
    			}
    			//校验产品代码
    			$sql = "SELECT sku FROM t_base_product WHERE sku=:sku AND customer_id=:customer_id AND active_flag='Y' AND is_valid=1";
    			$param = $db->createCommand($sql);
    			$param->bindValue(':sku', $val['sku']);
    			$param->bindValue(':customer_id', $val['customer_id']);
    			$rs = $param->queryRow();
    			if (empty($rs) || $rs['sku'] != $val['sku']) {
    				$this->errorArr[$key] = '产品代码：' . $val['sku'] . '不存在或不属于货主：' . $val['customer_id'];
    				unset($data[$key]);
    				continue;
    			}
    			$orderArr[$val['customer_id']][$val['warehouse_code']][$val['order_type']][$val['order_no']] = $val['order_no'];
    		}
    	}
    	return $data;   	
    }
    
    
    /**
     * 功能： Excel导入数据，写入库
     */
    public function insertOutbound($data)
    {
        // 获取出库单单头基础信息接口参数与数据库字段对应关系
        $column_orderInfo_arr = util::get_dataBase_relation('outbound_info');
        $column_key_orderInfo = implode(',', array_values($column_orderInfo_arr)) . ',source,create_time';
        
        // 获取出库单明细基础信息接口参数与数据库字段对应关系
        $column_orderDetail_arr = util::get_dataBase_relation('outbound_detail');
        $column_key_orderDetail = implode(',', array_values($column_orderDetail_arr)) . ',order_id,create_time';
        
        // 写入出库单单头信息
        $column_value_orderInfo = ":" . implode(",:", array_values($column_orderInfo_arr)) . ",'excel',now()";
        $sql_orderInfo = "INSERT INTO t_outbound_info({$column_key_orderInfo}) VALUES({$column_value_orderInfo})";
        $db = Yii::app()->db;
        $model = $db->createCommand($sql_orderInfo);
        $values = array();
        foreach ($column_orderInfo_arr as $v)
        {
        	$values[':'.$v] = empty($data[$v]) ? '' : $data[$v];
        }
        $model->bindValues($values);
        $model->execute();
        $orderId = $db->getLastInsertID();       
       
        // 写入出单单明细信息
        $column_value_orderDetail = ":" . implode(",:", array_values($column_orderDetail_arr)) . ",'{$orderId}',now()";
        $sql_orderDetail = "INSERT INTO t_outbound_detail({$column_key_orderDetail}) VALUES({$column_value_orderDetail})";

        $model = $db->createCommand($sql_orderDetail);
        $values = array();
        foreach ($column_orderDetail_arr as $v)
        {
        	$values[':'.$v] = empty($data[$v]) ? '' : $data[$v];
        }
        $model->bindValues($values);
        $model->execute();
        // 更新库存
        self::update_outbound_inventory($data['customer_id'], $data['warehouse_code'], $data['sku'], $data['qty_ordered']);
    }

    /**
     * 功能：判断该订单是否已存在
     */
    public function isInfoHas($param)
    {
        $Ssql = '';
        $connection = Yii::app()->db;
        $Ssql = 'SELECT order_id FROM t_outbound_info WHERE order_no = :order_no AND order_type = :order_type AND customer_id = :customer_id AND warehouse_code = :warehouse_code AND is_valid = 1;';
        $datas = $connection->createCommand($Ssql);
        $datas->bindValues($param);
        $rs = $datas->queryRow();
        if (!empty($rs)) {            
            return true;
        } else {
            return false;
        }
    }

    /**
     * 更新库存
     * @param string $customerId 
     * @param string $warehouseId 
     * @param string $sku  
     * @param int $num  
     * @return boolean
     */
    public function update_outbound_inventory($customerId, $warehouseId, $sku, $num)
    {
    	$db = Yii::app()->db;
    	$sql = "UPDATE t_product_inventory SET qty=qty-:QtyOrdered,occupy_qty=occupy_qty+:QtyOrdered WHERE customer_id=:customer_id AND warehouse_code=:warehouse_code AND sku=:sku";
		$model = $db->createCommand($sql);
		$values = array(
				':QtyOrdered' => $num,
				':customer_id' => $customerId,
				':warehouse_code' => $warehouseId,
				':sku' => $sku
		);
		$model->bindValues($values);
		$model->execute();
    }

    /**
     * 功能：转换列字母为数字
     *
     * @param
     * @$column
     */
    public function getColumnKey($column)
    {
        $res = range('A', 'Z');
        for ($i = 26; $i < 52; $i ++) {
            $j = $i - 26;
            $res[$i] = 'A' . $res[$j];
        }
        $newRes = array_flip($res);
        return $newRes[$column];
    }

    /**
     * Excel列转换
     */
    public static function getSCell($key)
    {
        $res = range('A', 'Z');
        for ($i = 26; $i < 52; $i ++) {
            $j = $i - 26;
            $res[$i] = 'A' . $res[$j];
        }
        return $res[$key];
    }
}