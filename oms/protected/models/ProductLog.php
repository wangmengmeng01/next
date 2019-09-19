<?php
/**
 * 商品接口日志model
 * table: t_product_interface_log
 */

class ProductLog extends CActiveRecord
{
	public static function model($className=__CLASS__)
	{
		return parent::model($className);
	}
	
	public function tableName()
	{
		return '{{product_interface_log}}';
	}
	
	public static function search()
	{
		$criteria = new CDbCriteria();
		$_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
		$_POST['rows'] = empty($_POST['rows']) ? '50' : $_POST['rows'];
		if (isset($_POST['ProductLog']['sku']) && !empty($_POST['ProductLog']['sku'])) {
			$criteria->compare('sku',trim($_POST['ProductLog']['sku']));
		} else {
    		if (isset($_POST['ProductLog']['customer_id']) && !empty($_POST['ProductLog']['customer_id'])) {
    			$criteria->compare('customer_id',trim($_POST['ProductLog']['customer_id']));
    		}
    		if (isset($_POST['ProductLog']['return_status']) && $_POST['ProductLog']['return_status'] != '') {
    			$criteria->compare('return_status', $_POST['ProductLog']['return_status']);
    		}
    		if (!empty($_POST['ProductLog']['start_time']) && !empty($_POST['ProductLog']['end_time'])) {
    			$criteria->addBetweenCondition('create_time', trim($_POST['ProductLog']['start_time']), trim($_POST['ProductLog']['end_time']));
    		} else {
    			$criteria->addBetweenCondition('create_time', date("Y-m-d").' 00:00:00', date("Y-m-d").' 23:59:59');
    		}
		}
		$dataProvider = new CActiveDataProvider('ProductLog',array(
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