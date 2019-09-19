<?php

/**
 * 组合商品列表model
 * table: t_base_combine_product
 */
class CombineProduct extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *            active record class name.
     * @return CombineProduct the static model class
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
        return '{{base_combine_product}}';
    }

    /**
     * Retrieves a list of models based on the current search/filter conditions.
     * 
     * @return CActiveDataProvider the data provider that can return the models based on the search/filter conditions.
     */
    public static function search($param)
    {
        $criteria = new CDbCriteria();
        //校验是否有查看所有商品的权限
        $viewFlag = util::hasViewAll();
        if ($viewFlag == 0) {
        	$existsArr = util::getCustomer();
        	if (!empty($existsArr)) {
        		$criteria->addInCondition('customer_id', $existsArr);
        	} else {
        		return ;
        	}
        }
        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '20' : $param['rows'];
        if (isset($param['CombineProduct']['item_code'])) {
            $criteria->compare('item_code', trim($param['CombineProduct']['item_code']));
        }
        if (isset($param['CombineProduct']['customer_id'])) {
        	$criteria->compare('customer_id', trim($param['CombineProduct']['customer_id']));
        }
        if (isset($param['CombineProduct']['warehouse_code'])) {
        	$criteria->compare('warehouse_code', trim($param['CombineProduct']['warehouse_code']));
        }
        $criteria->compare('is_valid', 1);
        $dataProvider = new CActiveDataProvider('CombineProduct', array(
            'sort'=>array(
    		        'defaultOrder'=>'create_time Desc',
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
     * 组合商品接口单头信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
    			'组合商品的ERP编码' => 'item_code',
    			'货主id ' => 'customer_id',
    			'仓库编码' => 'warehouse_code',
    			'入库时间 ' => 'create_time'	
    	);
        return $columns;	
    }
}