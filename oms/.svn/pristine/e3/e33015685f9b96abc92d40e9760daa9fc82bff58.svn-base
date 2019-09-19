<?php

/**
 * 唯品会JIT拣货单明细model
 * table: t_vip_po_list
 */
class VipJitPickDetail extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *            active record class name.
     * @return StoreProcessCreate the static model class
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
        return '{{vip_pick_product}}';
    }

    /**
     * Retrieves a list of models based on the current search/filter conditions.
     * 
     * @return CActiveDataProvider the data provider that can return the models based on the search/filter conditions.
     */
    public static function search($param)
    {
        //组合查询条件
        $select = ' SELECT t.pick_no,t.stock,t.barcode,t.art_no,t.product_name,t.size,p.jit_type,d.amount ';
        $selectNum = ' SELECT t.id';
//        $from = ' FROM t_vip_pick_product as t
//                  LEFT JOIN t_vip_pick_list AS p ON p.pick_no=t.pick_no
//                  LEFT JOIN t_vip_delivery_detail AS d ON d.barcode=t.barcode AND d.pick_no=p.pick_no ';


        $from = ' FROM t_vip_pick_product as t 
                  LEFT JOIN t_vip_pick_list AS p ON p.id=t.pick_id 
                  LEFT JOIN t_vip_delivery_info AS i ON i.vendor_id = p.vendor_id AND i.storage_no = p.storage_no
                  LEFT JOIN t_vip_delivery_detail AS d ON i.id = d.dlv_id AND t.barcode = d.barcode ';




        $where = " WHERE 1=1";
        if (isset($param['pickNo'])) {
            $where .= " AND p.id='{$param['pickNo']}'";
        }
        $groupBy = '';
        $order = '';
        $page = empty($param['page']) ? '1' : $param['page'];
        $pageSize = empty($param['rows']) ? '20' : $param['rows'];
        $offset = ($page - 1) * $pageSize;
        $limit = " LIMIT $offset, $pageSize";

        //excel导出时limit参数做特殊处理
        if (isset($param['operate_type']) && $param['operate_type'] == 'excel_export') {
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
//        $totalPage = ceil($total/$pageSize);

        //获取每页数据
        $sql = $select . $from . $where . $groupBy . $order . $limit;
        $model = $db->createCommand($sql);
        $row = $model->queryAll();
        //组合返回的数据
        return '{"total":' . $total . ',"rows":' . CJSON::encode($row) . '}';
    }
    
    /**
     * 唯品会JIT拣货单明细导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
			'商品拣货数量' => 'stock',
    	    '商品条码' => 'barcode',
    	    '货号' => 'art_no',
    	    '商品名称' => 'product_name',
    	    '尺码' => 'size',
    	    'jit类型' => 'jit_type',
    	    '商品数量' => 'amount',
    	);
    	return $columns;
    }
}