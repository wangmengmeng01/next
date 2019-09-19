<?php
/**
 * 订单接口交互日志model
 * table: t_order_interface_log
 */

class OrderLog extends CActiveRecord
{
	public static function model($className=__CLASS__)
	{
		return parent::model($className);
	}
	
	public function tableName()
	{
		return '{{order_interface_log}}';
	}
	
	public static function search()
	{
		$criteria = new CDbCriteria();
		
		$_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
		$_POST['rows'] = empty($_POST['rows']) ? '50' : $_POST['rows'];
		if (isset($_POST['OrderLog']['order_no']) && !empty($_POST['OrderLog']['order_no'])) {
			$criteria->compare('order_no',trim($_POST['OrderLog']['order_no']));
		} else {
			if (isset($_POST['OrderLog']['order_type']) && !empty($_POST['OrderLog']['order_type']) ) {
				$criteria->compare('order_type',trim($_POST['OrderLog']['order_type']));
			}
			if (isset($_POST['OrderLog']['customer_id']) && !empty($_POST['OrderLog']['customer_id'])) {
				$criteria->compare('customer_id',trim($_POST['OrderLog']['customer_id']));
			}
			if (isset($_POST['OrderLog']['warehouse_code']) && !empty($_POST['OrderLog']['warehouse_code'])) {
				$criteria->compare('warehouse_code',trim($_POST['OrderLog']['warehouse_code']));
			}
			if (isset($_POST['OrderLog']['method']) && !empty($_POST['OrderLog']['method'])) {
				$criteria->compare('method', $_POST['OrderLog']['method']);
			}
			if (isset($_POST['OrderLog']['return_status']) && $_POST['OrderLog']['return_status'] != '') {
				$criteria->compare('return_status', $_POST['OrderLog']['return_status']);
			}
			if (!empty($_POST['OrderLog']['start_time']) && !empty($_POST['OrderLog']['end_time'])) {
				$criteria->addBetweenCondition('create_time', trim($_POST['OrderLog']['start_time']), trim($_POST['OrderLog']['end_time']));
			} else {
				$criteria->addBetweenCondition('create_time', date("Y-m-d").' 00:00:00', date("Y-m-d").' 23:59:59');
			}
		}
		$dataProvider = new CActiveDataProvider('OrderLog',array(
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