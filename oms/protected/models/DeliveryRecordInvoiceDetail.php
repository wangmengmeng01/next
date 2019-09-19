<?php

/**
 * 发货单回传发票明细model
 * table: t_delivery_order_bill_product_detail_record
 */
class DeliveryRecordInvoiceDetail extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *            active record class name.
     * @return DeliveryOrderCreate the static model class
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
        return '{{delivery_order_bill_product_detail_record}}';
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
        if (isset($param['bill_id'])) {
            $criteria->compare('bill_id', trim($param['bill_id']));
        }
        $dataProvider = new CActiveDataProvider('DeliveryRecordInvoiceDetail', array(
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
     * 发货单回传发票明细信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
			'商品名称' => 'item_name',
			'商品单位' => 'unit',
    	    '商品单价' => 'price',
    	    '数量' => 'quantity',
    	    '金额' => 'amount',
    	    '创建时间' => 'create_time'
    	);
    	return $columns;
    }
}