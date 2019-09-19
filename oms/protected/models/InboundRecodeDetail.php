<?php

/**
 * 入库单状态明细回传明细信息model
 * inbound_detail_record
 */
class InboundRecodeDetail extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *            active record class name.
     * @return InboundDetail the static model class
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
        return '{{inbound_detail_record}}';
    }

    /**
     * Retrieves a list of models based on the current search/filter conditions.
     * 
     * @return CActiveDataProvider the data provider that can return the models based on the search/filter conditions.
     */
    public static function search($param)
    {
        $criteria = new CDbCriteria();
        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '20' : $param['rows'];
        
        if (isset($param['create_time']) && !empty($param['create_time'])) {
            $time = $param['create_time'];
            if ($time > '2015-12-01 17:00:00') {
                if (isset($param['record_id'])) {
                    $criteria->compare('record_id', trim($param['record_id']));
                }
            } else {
                if (isset($param['order_id'])) {
                    $criteria->compare('order_id', trim($param['order_id']));
                }
            }
        }
        
        $dataProvider = new CActiveDataProvider('InboundRecodeDetail', array(
            'sort' => array(
                'defaultOrder' => 'create_time Desc'
            ),
            'pagination' => array(
                'pageSize' => $param['rows'],
                'currentPage' => $param['page'] - 1
            ),
            'criteria' => $criteria
        ));
        return $dataProvider;
    }
    
    /**
     * 入库单状态明细回传明细信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
    			'行号' => 'line_no',
    			'SKU' => 'sku',
    			'货主ID' => 'customer_id',
    			'订单状态' => 'line_status',
    			'订单状态描述' => 'line_desc',
    			'预期数量' => 'expected_qty',
    			'实收数量' => 'received_qty',
    			'收货时间' => 'received_time',
    			'总价' => 'total_price',
    			'批次属性1' => 'lot_att01',
    			'批次属性2' => 'lot_att02',
    			'批次属性3' => 'lot_att03',
    			'批次属性4' => 'lot_att04',
    			'批次属性5' => 'lot_att05',
    			'批次属性6' => 'lot_att06',
    			'批次属性7' => 'lot_att07',
    			'批次属性8' => 'lot_att08',
    			'批次属性9' => 'lot_att09',
    			'批次属性10' => 'lot_att10',
    			'批次属性11' => 'lot_att11',
    			'批次属性12' => 'lot_att12',
    			'备注' => 'remark',
    			'入库时间' => 'create_time'
    	);
    	return $columns;
    }
}