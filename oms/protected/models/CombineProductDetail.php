<?php

/**
 * 组合商品明细列表model
 * table: t_base_combine_product_detail
 */
class CombineProductDetail extends CActiveRecord
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
        return '{{base_combine_product_detail}}';
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
        if (isset($param['combine_id'])) {
            $criteria->compare('combine_id', trim($param['combine_id']));
        }
        $dataProvider = new CActiveDataProvider('CombineProductDetail', array(
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
     * 组合商品明细信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
    			'组合商品的ERP编码' => 'combine_item_code',
    			'商品编码' => 'item_code',
    			'组合商品中的该商品个数' => 'quantity',
    			'入库时间 ' => 'create_time'	
    	);
        return $columns;	
    }
}