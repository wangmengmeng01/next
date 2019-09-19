<?php

/**
 * 出库单回传包裹model
 * table: t_outbound_package_record
 */
class OutboundRecordPackage extends CActiveRecord
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
        return '{{outbound_package_record}}';
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
        $dataProvider = new CActiveDataProvider('OutboundRecordPackage', array(
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
     * 出库单回传包裹信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
    	    '物流公司名称' => 'logistics_name',
    	    '运单号' => 'express_code',
    	    '包裹编号' => 'package_code',
    	    '包裹长度 (厘米)' => 'length',
    	    '包裹宽度 (厘米)' => 'width',
    	    '包裹高度 (厘米)' => 'height',
    	    '包裹重量 (千克)' => 'weight',
    	    '包裹体积 (升, L)' => 'volume',
    	    '创建时间' => 'create_time'
    	);
    	return $columns;
    }
}