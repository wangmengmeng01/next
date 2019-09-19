<?php

/**
 * This is the model class for table "{{inbound_detail}}".
 *
 * The followings are the available columns in table '{{inbound_detail}}':
 * @property integer $order_id
 */
class InboundDetail extends CActiveRecord
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
        return '{{inbound_detail}}';
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
        if (isset($param['order_id'])) {
            $criteria->compare('order_id', trim($param['order_id']));
        }
        $dataProvider = new CActiveDataProvider('InboundDetail', array(
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
     * 入库单下发明细信息
     */
    public static function getColumns()
    {
    	$columns = array(
    			'行号' => 'line_no',
    			'货主ID' => 'customer_id',
    			'SKU' => 'sku',
    			'预期数量' => 'expected_qty',
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