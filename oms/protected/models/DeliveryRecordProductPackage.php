<?php

/**
 * 发货单回传包裹商品明细信息model
 * table: t_delivery_package_product_record
 */
class DeliveryRecordProductPackage extends CActiveRecord
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
        return '{{delivery_package_product_record}}';
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
        if (isset($param['package_id'])) {
            $criteria->compare('package_id', trim($param['package_id']));
        }
        $dataProvider = new CActiveDataProvider('DeliveryRecordProductPackage', array(
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
     * 发货单回传包裹商品明细信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
    	    '商品编码' => 'item_code',
    	    '商品仓储系统编码' => 'item_id',
    	    '包裹内该商品的数量' => 'quantity',
    	    '创建时间' => 'create_time'
    	);
    	return $columns;
    }
}