<?php

/**
 * 商品列表model
 * table: t_base_product
 */
class Product extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *            active record class name.
     * @return Product the static model class
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
        return '{{base_product}}';
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
        if(Yii::app()->user->getState('login_type') != 'OMS' && Yii::app()->user->getState('login_type') != 'ADMIN'){
            $viewFlag = util::hasViewAll();
            if ($viewFlag == 0) {
            	$existsArr = util::getCustomer();
            	if (!empty($existsArr)) {
            		$criteria->addInCondition('customer_id', $existsArr);
            	} else {
            		return ;
            	}
            }
        }
        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '20' : $param['rows'];
        if (isset($param['Product']['sku'])) {
            $criteria->compare('sku', trim($param['Product']['sku']));
        }
        if (isset($param['Product']['descr_c'])) {
            $criteria->compare('descr_c', trim($param['Product']['descr_c']));
        }
        if (isset($param['Product']['customer_id'])) {
        	$criteria->compare('customer_id', trim($param['Product']['customer_id']));
        }
        if (isset($param['Product']['active_flag'])) {
        	$criteria->compare('active_flag', trim($param['Product']['active_flag']));
        }
        $criteria->compare('is_valid', 1);
        $dataProvider = new CActiveDataProvider('Product', array(
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
     * 入库单下发单头信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
    			'SKU ' => 'sku',
    			'中文名称 ' => 'descr_c',
    			'英文名称' => 'descr_e',
    			'商品简称 ' => 'sku_short',
    			'货主ID' => 'customer_id',
    			'毛重量' => 'gross_weight',
    			'净重量 ' => 'net_weight',
    			'体积 ' => 'cube',
    			'价格 ' => 'price',
    			'长 ' => 'sku_Length',
    			'宽' => 'sku_width',
    			'高' => 'sku_height',
    			'循环级别' => 'cycle_class',
    			'货架生命周期' => 'shelfLife_flag',
    			'周期类型' => 'shelfLife_type',
    			'入库有效期' => 'inbound_life_days',
    			'出库有效期' => 'outbound_life_days',
    			'有效期' => 'shelf_life',
    			'货号或款号' => 'sku_group1',
    			'颜色' => 'sku_group2',
    			'尺码' => 'sku_group3',
    			'品牌' => 'sku_group4',
    			'年份' => 'sku_group5',
    			'季节' => 'sku_group6',
    			'大类' => 'sku_group7',
    			'中类' => 'sku_group8',
    			'小类' => 'sku_group9',
    			'库存下限' => 'qty_min',
    			'库存上限' => 'qty_max',
    			'货物类型' => 'freight_class',
    			'组合件标识' => 'kit_flag',
    			'激活标志' => 'active_flag',
    			'入库时间 ' => 'create_time'   			
    	);
        return $columns;	
    }
}