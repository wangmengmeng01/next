<?php

/**
 * 仓库发货量信息model
 * table: t_outbound_info
 */
class WarehouseShipments extends CActiveRecord
{
    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *            active record class name.
     * @return PutSOData the static model class
     */
    public static function model($className = __CLASS__)
    {
        return parent::model($className);
    }

    /**
     *
     * @return string the associated database table name
     */
    public function tableName()
    {
        return '{{shipments_info}}';
    }

    /**
     * Retrieves a list of models based on the current search/filter conditions.
     * 
     * @return CActiveDataProvider the data provider that can return the models based on the search/filter conditions.
     */
    public static function search($param)
    {
    	//校验是否有查看所有货主的权限
    	$viewFlag = util::hasViewAll();
    	if ($viewFlag == 0) {
    		$existsArr = util::getCustomer();
    		if (!empty($existsArr)) {
                $where = "t.customer_id in($existsArr)";
    		} else {
    			return ;
    		}
    	}
    	
    	//组合查询条件
    	$select = 'SELECT t.shipment_id, t.customer_id, t.warehouse_code, c.branch_code, t.province_id, t.wms_code, SUM(t.shipments_qty) AS shipments_qty, t.rq, IF(c.customer_name IS NOT NULL, c.customer_name, t.customer_name) AS customer_name, IF(h.descr_c IS NOT NULL, h.descr_c, t.warehouse_name)  AS warehouse_name, g.mc as branch_name, w.wms_name, p.provincename, y.avg_qty';
    	$selectNum = ' SELECT t.shipment_id';
    	$from = ' FROM t_shipments_info t 
    	        LEFT JOIN t_base_customer c ON t.customer_id = c.customer_id 
    			LEFT JOIN t_base_warehouse h ON t.warehouse_code = h.warehouse_code 
    			LEFT JOIN t_base_wms w ON t.wms_code = w.wms_code 
    			LEFT JOIN ydserver.gs g ON c.branch_code = g.bm
         		LEFT JOIN ydserver.county k ON g.szd = k.countyid 
         		LEFT JOIN ydserver.city m ON k.cityid = m.cityid 
         		LEFT JOIN ydserver.province p ON m.provinceid = p.provinceid';
    	
        if (!empty($param['WarehouseShipments']['start_time']) && !empty($param['WarehouseShipments']['end_time'])) {
        	$startTime = trim($param['WarehouseShipments']['start_time']);
        	$endTime = trim($param['WarehouseShipments']['end_time']);
        } else {
        	$startTime = date("Y-m-d", strtotime("-1 day"));
        	$endTime = date("Y-m-d", strtotime("-1 day"));
        }
        $where = " WHERE t.rq >= '$startTime' AND t.rq <= '$endTime'";
        $lWhere = " WHERE t1.rq >= '$startTime' AND t1.rq <= '$endTime'";
        if (!empty($param['WarehouseShipments']['warehouse_name'])) {
        	$warehouseName = trim($param['WarehouseShipments']['warehouse_name']);
        	$where .= " AND (h.descr_c = '$warehouseName' OR t.warehouse_name = '$warehouseName')";
            $lWhere .= " AND (h1.descr_c = '$warehouseName' OR t1.warehouse_name = '$warehouseName')";
        } 
        if (!empty($param['WarehouseShipments']['warehouse_code'])) {
        	$warehouseCode = trim($param['WarehouseShipments']['warehouse_code']);
        	$where .= " AND t.warehouse_code = '$warehouseCode'";
            $lWhere .= " AND t1.warehouse_code = '$warehouseCode'";
        }
        if (!empty($param['WarehouseShipments']['gs_name'])) {
        	$branchName = trim($param['WarehouseShipments']['gs_name']);
        	$where .= " AND g.mc = '$branchName'";
            $lWhere .= " AND g1.mc = '$branchName'";
        }
        if (!empty($param['WarehouseShipments']['gs_code'])) {
        	$branchCode = trim($param['WarehouseShipments']['gs_code']);
        	$where .= " AND t.branch_code = '$branchCode'";
            $lWhere .= " AND t1.branch_code = '$branchCode'";
        }
        if (!empty($param['WarehouseShipments']['province'])) {
        	$provinceId = trim($param['WarehouseShipments']['province']);
        	$where .= " AND t.province_id = '$provinceId'";
            $lWhere .= " AND t1.province_id = '$provinceId'";
        }
        if (!empty($param['WarehouseShipments']['wms_name'])) {
        	$where .= " AND t.wms_code = '{$param['WarehouseShipments']['wms_name']}'";
            $lWhere .= " AND t1.wms_code = '{$param['WarehouseShipments']['wms_name']}'";
        }

        $d1 = strtotime($startTime);
        $d2 = strtotime($endTime);
        $days = round(($d2-$d1)/3600/24) + 1 ;

        $left = " LEFT JOIN
				(
					SELECT
						t1.customer_id,t1.warehouse_code,SUM(t1.shipments_qty)/". $days ." AS avg_qty
					FROM
						t_shipments_info t1
    				LEFT JOIN t_base_customer c1 ON t1.customer_id = c1.customer_id
    				LEFT JOIN t_base_warehouse h1 ON t1.warehouse_code = h1.warehouse_code 
    				LEFT JOIN ydserver.gs g1 ON c1.branch_code = g1.bm
					".$lWhere."
					GROUP BY t1.warehouse_code
				) y ON t.warehouse_code=y.warehouse_code ";

        $lFrom = $from.$left;

        $groupBy = ' GROUP BY t.rq,t.warehouse_code';
        $order = ' ORDER BY t.rq,t.warehouse_code DESC';
        $page = empty($param['page']) ? '1' : $param['page'];
        $pageSize = empty($param['rows']) ? '20' : $param['rows'];
        $offset = ($page - 1) * $pageSize;
        $limit = " LIMIT $offset, $pageSize";
        
        //excel导出时limit参数做特殊处理
        if (isset($param['WarehouseShipments']['operate_type']) && $param['WarehouseShipments']['operate_type'] == 'excel_export') {
        	$limit = '';
        }


        
        //连接数据库
        $db = Yii::app()->db;
        //获取总记录条数
        $sqlNum = 'SELECT COUNT(*) AS num FROM (' . $selectNum . $from . $where . $groupBy . ') a';
        $model = $db->createCommand($sqlNum);
        $rs = $model->queryRow();
        if (empty($rs['num'])) {
    	    $total = 0;
    	} else {
    	    $total = $rs['num'];
    	}

        //总页数
        $totalPage = ceil($total/$pageSize);

        //获取每页数据
        $sql = $select . $lFrom . $where . $groupBy . $order . $limit;
        $model = $db->createCommand($sql);
        $row = $model->queryAll();

        //获取总发货量
        $sqlTotalQty = 'SELECT SUM(shipments_qty) as sum_qty FROM ('.$select . $lFrom . $where . $groupBy . $order.') AS tt';
        $model = $db->createCommand($sqlTotalQty);
        $sumQty = $model->queryRow();

        if (empty($sumQty)) {
            $sumQty['sum_qty'] = 0;
        }

        if (!empty($row) && $totalPage == $page) {
            $row[] = array(
                'shipment_id'	=>'',
                'provincename'	=>'',
                'branch_code'	=>'',
                'branch_name'=>'',
                'wms_name'=>'',
                'warehouse_code'	=>'',
                'warehouse_name'	=>'合计',
                'shipments_qty'	=>$sumQty['sum_qty'],
                'avg_qty'	=>'',
                'rq'			=>''
            );
        }
        
        //组合返回的数据
        return '{"total":' . $total . ',"rows":' . CJSON::encode($row) . '}';
    }
    
    /**
     * 仓库发货量信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
    			'省份' => 'provincename',
    			'网点名称' => 'branch_name',
    			'网点编码 ' => 'branch_code',
    			'使用系统 ' => 'wms_name',
    			'仓库编码' => 'warehouse_code',
    			'仓库名称' => 'warehouse_name',
        	    '发货量' => 'shipments_qty',
                '日均发货量' 	=> 'avg_qty',
    			'时间' => 'rq'
    	);
    	return $columns;
    }
}