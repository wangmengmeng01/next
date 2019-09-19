<?php

/**
 * 店铺列表model
 * table: t_base_shop
 */
class Shop extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *            active record class name.
     * @return Shop the static model class
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
        return '{{base_shop}}';
    }

    /**
     * Retrieves a list of models based on the current search/filter conditions.
     * 
     * @return CActiveDataProvider the data provider that can return the models based on the search/filter conditions.
     */
    public static function search()
    {
        $criteria = new CDbCriteria();
        //校验是否有查看所有店铺的权限
        $viewFlag = util::hasViewAll();
        if ($viewFlag == 0) {
        	$existsArr = util::getCustomer('OT');
        	if (!empty($existsArr)) {
        		$criteria->addInCondition('shop_code', $existsArr);
        	} else {
        		return ;
        	}
        }
        // $criteria->addCondition("is_valid=1"); //查询条件，即where is_valid = 1               
        if (isset($_POST['Shop']['shop_code'])) {
            $criteria->compare('shop_code', trim($_POST['Shop']['shop_code']));
        }
        if (isset($_POST['Shop']['descr_c'])) {
        	$criteria->compare('descr_c', trim($_POST['Shop']['descr_c']), true);
        }
        if (isset($_POST['Shop']['active_flag'])) {
            $criteria->compare('active_flag', trim($_POST['Shop']['active_flag']));
        }
        $criteria->compare('is_valid', 1);
        $_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
        $_POST['rows'] = empty($_POST['rows']) ? '20' : $_POST['rows'];
        $dataProvider = new CActiveDataProvider('Shop', array(
            'sort'=>array(
    		        'defaultOrder'=>'create_time Desc',
    		 ),
            'pagination' => array(
                'pageSize' => $_POST['rows'],
                'currentPage' => $_POST['page'] - 1
            ),
            'criteria' => $criteria
        ));
        return $dataProvider;
    }
}