<?php

/**
 * 发货单回传包裹model
 * table: delivery_package_record
 */
class DeliveryRecordPackage extends CActiveRecord
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
        return '{{delivery_package_record}}';
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
        if (isset($param['delivery_id'])) {
            $criteria->compare('delivery_id', trim($param['delivery_id']));
        }
        $dataProvider = new CActiveDataProvider('DeliveryRecordPackage', array(
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
     * 发货单回传包裹信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
			'物流公司编码' => 'logistics_code',
    	    '物流公司名称' => 'logistics_name',
    	    '运单号' => 'express_code',
    	    '包裹编号' => 'package_code',
    	    '包裹长度 (厘米)' => 'length',
    	    '包裹宽度 (厘米)' => 'width',
    	    '包裹高度 (厘米)' => 'height',
    	    '包裹理论重量(千克)' => 'theoretical_weight',
    	    '包裹重量 (千克)' => 'weight',
    	    '包裹体积 (升, L)' => 'volume',
    	    '发票号' => 'invoice_no',
    	    '创建时间' => 'create_time'
    	);
    	return $columns;
    }
}