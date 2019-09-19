<?php

/**
 * 公共方法类
 * 使用较多的公共方法可以放入此类中
 * @auth 	DengYun
 * @version 1.0
 */
class util
{

    /**
     * 获取easyUI-datagrid的分页参数
     *
     * @access public
     * @param
     *            int 初始值
     * @param
     *            int 每页数据条数
     * @return array
     */
    public static function getPageParam($offset = 0, $limit = 30)
    {
        $params = array(
            'offset' => $offset,
            'limit' => $limit
        );
        $params['limit'] = isset($_POST['rows']) ? $_POST['rows'] : $params['limit'];
        $params['offset'] = isset($_POST['page']) ? ($_POST['page'] - 1) * $params['limit'] : $params['offset'];
        if (isset($_POST['order']) && isset($_POST['sort']) && $_POST['order'] != '') {
            $params['order'] = $_POST['sort'] . ' ' . $_POST['order'];
        }
        return $params;
    }

    /**
     * 获取easyUI-datagrid的分页参数
     *
     * @access public
     * @param
     *            int 初始值
     * @param
     *            int 每页数据条数
     * @return Object
     */
    public static function getPageParamObj($query, $offset = 0, $limit = 30)
    {
        $params = array(
            'offset' => $offset,
            'limit' => $limit
        );
        $params['limit'] = isset($_POST['rows']) ? $_POST['rows'] : $params['limit'];
        $params['offset'] = isset($_POST['page']) ? ($_POST['page'] - 1) * $params['limit'] : $params['offset'];
        
        if (isset($_POST['order']) && isset($_POST['sort']) && $_POST['order'] != '') {
            $query->order($_POST['sort'] . ' ' . $_POST['order']);
        }
        $query->limit($params['limit']);
        $query->offset($params['offset']);
        
        return $query;
    }

    /**
     * 检查权限
     *
     * @access public
     * @param
     *            int 权限位
     * @return boolean 是否有权限
     */
    public static $power = null;

    public static function checkPower($key)
    {
        if (self::$power == null)
            self::$power = Yii::app()->user->qx;
        $qx = self::$power;
        return isset($qx[$key]) && $qx[$key] == 1 ? true : false;
    }

    /**
     * 将对象转为数组
     *
     * @access public
     * @param Object $obj            
     * @param Array $fields            
     * @return Array
     * @example objToArray($obj,array('filed1','filed2'))
     */
    public static function objToArray($obj, $fields = null)
    {
        $arr = array();
        if (is_object($obj) || is_array($obj)) {
            foreach ($obj as $key => $val) {
                if (is_object($val))
                    $arr[$key] = self::objToArray($val, $fields);
                else {
                    if (is_array($fields) && ! in_array($key, $fields))
                        continue;
                    $arr[$key] = $val;
                }
            }
        } else
            $arr = $obj;
        return $arr;
    }

    /**
     * POST/GET 通信
     *
     * @param String $url            
     * @param Array $postFields            
     * @return mixed
     * @throws Exception
     */
    public static function curl($url, $postFields = null, $timeout = 5)
    {
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_FAILONERROR, false);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_TIMEOUT, $timeout);
        
        if (is_array($postFields) && 0 < count($postFields)) {
            $postBodyString = "";
            foreach ($postFields as $k => $v) {
                $postBodyString .= "$k=" . urlencode($v) . "&";
            }
            unset($k, $v);
            curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
            curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
            curl_setopt($ch, CURLOPT_POST, true);
            curl_setopt($ch, CURLOPT_POSTFIELDS, substr($postBodyString, 0, - 1));
        }
        $reponse = curl_exec($ch);
        if (curl_errno($ch)) {
            return curl_error($ch);
        } else {
            $httpStatusCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
            if (200 !== $httpStatusCode) {
                return $reponse;
            }
        }
        curl_close($ch);
        return $reponse;
    }

    /**
     * 防注入
     */
    public static function checkInput($value)
    {
        // 去除2边空格
        $value = trim($value);
        // 去除斜杠
        if (get_magic_quotes_gpc()) {
            $value = stripslashes($value);
        }
        return $value;
    }

    /**
     * 导出
     *
     * @param
     *            $data
     * @return bool
     */
    public static function export($filename, $columns, $dataProvider)
    {
        $objectPHPExcel = new PHPExcel();
        $objectPHPExcel->setActiveSheetIndex(0);
        
        $page_size = 22;
        if (is_object($dataProvider)) {
        	$dataProvider->setPagination(false);
        	$data = $dataProvider->getData();
        } else {
        	$tmpArr = json_decode($dataProvider, true);
        	$data = $tmpArr['rows'];
        }
        // 总页数的算出
        $current_page = 0;
        $n = 0;
        
        foreach ($data as $value) {
            if ($n % $page_size === 0) {
                $current_page = $current_page + 1;

                $k = 0;
                foreach ($columns as $key => $val) {
                    // 表格头的输出
                    $objectPHPExcel->setActiveSheetIndex(0)->setCellValue(self::getSCell($k) . '1', $key);
                    $k ++;
                }
            }
            $j = 0;
            foreach ($columns as $key => $val) {
                // 明细的输出
                if (is_object($val)) {
                	$newVal = self::getTypeName($value->$val,$val);
                } else {
                	$newVal = self::getTypeName($value[$val],$val);
                }
                if (($key=='发件人--移动电话'||$key=='收件人--移动电话'||$key=='手机号'||$key=='固话') && !empty($newVal)) 
                {
            		$newVal=substr_replace($newVal, '****', 3,4);
                }
                if(($key=='发件人--姓名'||$key=='收件人--姓名'||$key=='收货人名称 '||$key=='固话') && !empty($newVal)){
            		$newVal=substr_replace($newVal, '****', 3);
                }
                if(($key=='地址'||$key=='发件人--详细地址'||$key=='收件人--详细地址') && !empty($newVal)){
            		$where=strrpos($newVal,"区");
					$newVal=substr_replace($newVal, '***路***号', $where+3);
                }
                $objectPHPExcel->getActiveSheet()->setCellValueExplicit(self::getSCell($j) . ($n + 2), $newVal, PHPExcel_Cell_DataType::TYPE_STRING);
                $objectPHPExcel->getActiveSheet()->getStyle(self::getSCell($j) . ($n + 2))->getNumberFormat()->setFormatCode("@");
                $j ++;
            }
            $n ++;
        }
        
        ob_end_clean();//清楚缓冲区
        ob_start();
        ini_set('memory_limit', '400M');
        header('Content-Type : application/vnd.ms-excel;charset=utf-8');
        header('Content-Disposition:attachment;filename="' . $filename . '-' . date("Y年m月j日") . '.xls"');
        $objWriter = PHPExcel_IOFactory::createWriter($objectPHPExcel, 'Excel5');
        $objWriter->save('php://output');
    }
    
    

    /**
     * Excel列转换
     */ 
    public static function getSCell($key)
    {
        $res = range('A', 'Z');
        if ($key >= 52) {
            for ($i = 26; $i < 52; $i ++) {
                $j = $i - 26;
                $res[$i] = 'A' . $res[$j];
            }
            for ($i = 52; $i < 78; $i ++) {
                $j = $i - 52;
                $res[$i] = 'B' . $res[$j];
            }
        
        } else {
            for ($i = 26; $i < 52; $i ++) {
                $j = $i - 26;
                $res[$i] = 'A' . $res[$j];
            }
        }
        return $res[$key];
        /*
        $res = range('A', 'Z');
        for ($i = 26; $i < 52; $i ++) {
            $j = $i - 26;
            $res[$i] = 'A' . $res[$j];
        }
        return $res[$key];
        */
    }

    /**
     * 类型转换
     */
    public static function getTypeName($key,$c_name='')
    {
        if ($c_name == 'order_type') {
            $res = array(
                '40' => '分配完成',
                '60' => '拣货完成',
                '63' => '复核完成',
                '99' => '订单完成',
                '90' => '取消',
                'PO' => '采购入库',
                'TR' => '调拨入库',
                'RS' => '销售退货入库',
                'IP' => '销售退货入库',
                'SO' => '销售出库',
                'TO' => '调拨出库',
                'RP' => '采购退货出库',
                'IL' => '盘亏出库'
            );
        } else {
            $res = array();
        }
        
        if (in_array($key, array_keys($res)) && $key !== 0) {
            return $res[$key];
        } else {
            return $key;
        }
    }

    /**
     * excel导入，建立列于数据库字段对应关系
     */
    public static function get_dataBase_relation($type)
    {
    	$return_arr = array();
    	if ($type == 'outbound_info') {
    		$return_arr['上游订单号SOReference1'] = 'order_no';
    		$return_arr['订单类型OrderType(默认SO)'] = 'order_type';
    		$return_arr['货主代码CustomerID'] = 'customer_id';
    		$return_arr['仓库代码WarehouseId'] = 'warehouse_code';
    		$return_arr['快递单号DeliveryNo'] = 'delivery_no';
    		$return_arr['下单平台ConsigneeID'] = 'consignee_id';
    		$return_arr['收货人ConsigneeName'] = 'consignee_name';
    		$return_arr['省份C_Province'] = 'c_province';
    		$return_arr['城市C_City'] = 'c_city';
    		$return_arr['手机号C_Tel1'] = 'c_tel1';
    		$return_arr['固话C_Tel2'] = 'c_tel2';
    		$return_arr['邮编C_ZIP'] = 'c_zip';
    		$return_arr['旺旺号C_Mail'] = 'c_mail';
    		$return_arr['地址C_Address1'] = 'c_address1';
    		$return_arr['区县C_Address2'] = 'c_address2';
    		$return_arr['下单方(OMS/ERP)UserDefine4'] = 'user_define4';
    		$return_arr['客服备注UserDefine5'] = 'user_define5';
    		$return_arr['是否打印发票InvoicePrintFlag'] = 'invoice_print_flag';
    		$return_arr['顾客留言Notes'] = 'remark';
    		$return_arr['支付方式H_EDI_01'] = 'h_edi_01';
    		$return_arr['订单总价H_EDI_02'] = 'h_edi_02';
    		$return_arr['优惠金额H_EDI_03'] = 'h_edi_03';
    		$return_arr['已付金额H_EDI_04'] = 'h_edi_04';
    		$return_arr['是否货到付款H_EDI_05（Y、N）'] = 'h_edi_05';
    		$return_arr['应付金额H_EDI_06'] = 'h_edi_06';
    		$return_arr['支付宝交易号H_EDI_07'] = 'h_edi_07';
    		$return_arr['是否保价H_EDI_08（Y、N）'] = 'h_edi_08';
    		$return_arr['保价金额H_EDI_09'] = 'h_edi_09';
    		$return_arr['运费H_EDI_10'] = 'h_edi_10';
    		$return_arr['线路RouteCode'] = 'route_code';
    		$return_arr['目的地代码CarrierFax'] = 'carrier_fax';
    		$return_arr['渠道Channel'] = 'channel';
    		$return_arr['承运商编码CarrierId(默认YUNDA)'] = 'carrier_id';
    	}elseif ($type == 'outbound_detail') {
    		$return_arr['货主代码CustomerID'] = 'customer_id';
    		$return_arr['产品代码SKU'] = 'sku';
    		$return_arr['订货数QTYORDERED(数值格式)'] = 'qty_ordered';
    		$return_arr['批次属性01LotAtt01'] = 'lot_att01';
    		$return_arr['批次属性02LotAtt02'] = 'lot_att02';
    		$return_arr['批次属性03LotAtt03'] = 'lot_att03';
    		$return_arr['批次属性04LotAtt04'] = 'lot_att04';
    		$return_arr['批次属性05LotAtt05'] = 'lot_att05';
    		$return_arr['批次属性06LotAtt06'] = 'lot_att06';
    		$return_arr['批次属性07LotAtt07'] = 'lot_att07';
    		$return_arr['批次属性08LotAtt08'] = 'lot_att08';
    		$return_arr['批次属性09LotAtt09'] = 'lot_att09';
    		$return_arr['批次属性10LotAtt10'] = 'lot_att10';
    		$return_arr['批次属性11LotAtt11'] = 'lot_att11';
    		$return_arr['批次属性12LotAtt12'] = 'lot_att12';
    		$return_arr['单位UOM(默认值EA)'] = 'uom';
    		$return_arr['释放状态RELEASESTATUS(Y/N)'] = 'release_status';
    		$return_arr['订单优先级PRIORITY(默认值3)'] = 'priority';
    		$return_arr['单品标记singlematch(Y/N)'] = 'single_match';
    	}
    	return $return_arr;
    }

    /*
     * 功能：获取可查数据。总部账户，可查询所有。网点账户，只可查询自己
     *
     */
    public static function getCustomer($type = 'All')
    {
        $connection = Yii::app()->db;
        $key = 'customer_id';
        $param = array(
            ':branch_code' => Yii::app()->user->gsbm
        );
        $column = 'a.customer_id'; // 查询字段
        $sql = 'select FIELD from oms.`t_base_customer` a '; // 查询语句
        $where = ' WHERE a.branch_code = :branch_code'; // 查询条件
        $join = ''; // 关联表
        
        switch ($type) {
            case 'OT': // 店铺
                $column = 'b.code';
                $key = 'code';
                $where .= ' AND b.`type` = :type';
                $param[':type'] = $type;
                $join = ' LEFT JOIN oms.`t_customer_relation` b ON b.`customer_id` = a.`customer_id`';
                break;
            case 'VE': // 供应商
                $column = 'b.code';
                $key = 'code';
                $where .= ' AND b.`type` = :type';
                $param[':type'] = $type;
                $join = ' LEFT JOIN oms.`t_customer_relation` b ON b.`customer_id` = a.`customer_id`';
                break;
            case 'WH': // 仓库
                $column = 'b.code';
                $key = 'code';
                $where .= ' AND b.`type` = :type';
                $param[':type'] = $type;
                $join = ' LEFT JOIN oms.`t_customer_relation` b ON b.`customer_id` = a.`customer_id`';
                break;
            case 'ERP': // ERP
                $column = 'b.erp_code';
                $key = 'erp_code';
                $join = ' LEFT JOIN oms.`t_bind_relation` b ON b.`customer_id` = a.`customer_id`';
                break;
            case 'WMS': // WMS
                $column = 'b.wms_code';
                $key = 'wms_code';
                $join = ' LEFT JOIN oms.`t_bind_relation` b ON b.`customer_id` = a.`customer_id`';
                break;
            case 'SKU' : //商品
            	$column = 'b.sku';
            	$key = 'sku';
            	$join = ' LEFT JOIN oms.`t_base_product` b ON b.`customer_id` = a.`customer_id` AND b.is_valid=1';
            	break;
        }
        // 查询SQL
        $datas = $connection->createCommand(str_replace('FIELD', $column, $sql) . $join . $where);
        $datas->bindValues($param);
        $res = $datas->queryAll();
        if ($res && is_array($res)) {
            foreach ($res as $val) {
                $newRes[] = $val[$key];
            }
            return $newRes;
        } else {
            return array();
        }
    }
    
    /**
     * 获取登陆者角色数组
     * 
     */
    public static function  getUserRoleArr()
    {
    	$roleStr =  Yii::app()->user->getState('user_role');
    	$roleArr = array();
    	if ($roleStr != '') {
    		$roleArr = explode(",", $roleStr);
    	}
    	
    	return $roleArr;
    }
    
    /**
     * 获取登陆者所有权限数组
     */
    public static function getUserAllPriArr()
    {
    	$priStr = Yii::app()->user->getState('user_all_pri');
    	$priArr = array();
    	if ($priStr != '') {
    		$priArr = explode(",", $priStr);
    	}
    	return $priArr;
    }
    
    /**
     * 获取登陆者是否具有某个权限位的权限
     */
    public static function isHasPri($pos)
    {
    	$flag = 0;
    	//获取登陆者角色
    	$roleArr = self::getUserRoleArr(); 
    	//获取登陆者所有权限位
    	$allPri = self::getUserAllPriArr();
    	
    	if (in_array(AUTH_SYSTEM_MANAGER, $roleArr)) {
    		$flag = 1;
    	} else {
	    	if (in_array($pos, $allPri)) {
	        	$flag = 1;
	        } else {
	        	$flag = 0;
	        }
    	}
        return $flag;
    }
    
    /**
     * 校验是否具有查看所有客户报表的权限
     * 目前只有系统管理员和配置管理员有权限
     */
    public static function hasViewAll()
    {
    	$flag = 0;
    	//获取登陆者的角色
    	$roleArr = self::getUserRoleArr();
    	
    	if (!in_array(AUTH_SYSTEM_MANAGER, $roleArr) && !in_array(AUTH_CONFIGURE_MANAGER, $roleArr)) {
    		if (!in_array(AUTH_HEADER_OPERATOR, $roleArr)) {
    			$flag = 0;
    		} else {
    			$flag = 1;
    		}
    	} else {
    		$flag = 1;
    	}
    	return $flag;
    }
    
    /**
     * 校验登陆者是否有操作的权限
     * @param $pos  权限位
     * @param $type 错误返回格式
     * @return mixed
     */
    public static function operatePriContr($pos, $type='')
    {
    	$operateFlag = self::isHasPri($pos);
    	if ($operateFlag == 0) {
    		if ($type == 'json') {
    			echo '{"status":"0","msg":"非法操作！"}';
    			die();
    		} else if ($type == 'text') {
    			echo '非法操作！';
    			die();
    		} else {
    			echo "<script type='text/javascript'>$('#dlg').dialog('close');$.messager.alert('友情提示', '非法操作！', 'info');</script>";
    			die();
    		}
    	} else {
    		return true;
    	}    		
    }
    
    /**
     * 获得用户的真实IP地址
     *
     * @access  public
     * @return  string
     */
    public static function getRealIP() 
    {
    	if (isset($_SERVER)) {
    		if (isset($_SERVER['HTTP_X_FORWARDED_FOR'])) { //1.有代理
    			$arr = explode(',', $_SERVER['HTTP_X_FORWARDED_FOR']);
    			/* 取X-Forwarded-For中第一个非unknown的有效IP字符串 */
    			foreach ($arr AS $ip)
    			{
    				$ip = trim($ip);
    				if ($ip != 'unknown') {
    					$realip = $ip;
    					break;
    				}
    			}
    		} elseif (isset($_SERVER['HTTP_CLIENT_IP'])) { //2.无代理
    			$realip = $_SERVER['HTTP_CLIENT_IP'];
    		} else {
    			if (isset($_SERVER['REMOTE_ADDR'])) {
    				$realip = $_SERVER['REMOTE_ADDR'];
    			} else {
    				$realip = '0.0.0.0';
    			}
    		}
    	} else { //getenv不支持IIS的isapi方式运行的php
    		if (getenv('HTTP_X_FORWARDED_FOR')) {
    			$realip = getenv('HTTP_X_FORWARDED_FOR');
    		} elseif (getenv('HTTP_CLIENT_IP')) {
    			$realip = getenv('HTTP_CLIENT_IP');
    		} else {
    			$realip = getenv('REMOTE_ADDR');
    		}
    	}
    	preg_match('/^([0-9]{1,3}\.){3}[0-9]{1,3}$/', $realip, $onlineip);
    	$realip = !empty($onlineip[0]) ? $onlineip[0] : '0.0.0.0';
    	return $realip;
    }
    
    /**
     * 记录elk日志
     * @param string $moduel 模块
	 * @param string $operate 操作
	 * @param string $level 日志级别
	 * @param string $primary 参数(单号，主键类) 
	 * @param string $user 人员
	 * @param string $company 公司
	 * @param string $ip 客户端请求IP
	 * @param mix $input 输入数据
	 * @param mix $output 输出数据
	 * @param string $status 状态（Y-成功,N-失败）
	 * @param string $device 设备
	 * @return boolean 
     */
    public static function elkLog($moduel, $operate, $level, $primary, $user, $company, $ip, $input, $output, $status, $device = '')
    {
//     	$logger = new elk();
//     	$log_data = $logger->make_log_data($moduel, $operate, $level, $primary, $user, $company, $ip, $input, $output, $status, $device);
//     	$logger->write_log($log_data);
    	return true;
    }
}