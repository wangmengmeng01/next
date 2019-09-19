<?php
/**
 * 客商档案接口交互日志model
 * table: t_customer_interface_log
 */

class CustomerLog extends CActiveRecord
{
	public static function model($className=__CLASS__)
	{
		return parent::model($className);
	}
	
	public function tableName()
	{
		return '{{customer_interface_log}}';
	}
	
	public static function search()
	{
		$criteria = new CDbCriteria();
		$_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
		$_POST['rows'] = empty($_POST['rows']) ? '50' : $_POST['rows'];
		if (isset($_POST['CustomerLog']['customer_id']) && !empty($_POST['CustomerLog']['customer_id'])) {
			$criteria->compare('customer_id',trim($_POST['CustomerLog']['customer_id']));
		} else {
			if (isset($_POST['CustomerLog']['customer_type']) && !empty($_POST['CustomerLog']['customer_type']) ) {
				$criteria->compare('customer_type',trim($_POST['CustomerLog']['customer_type']));
			}
			if (isset($_POST['CustomerLog']['return_status']) && $_POST['CustomerLog']['return_status'] != '') {
				$criteria->compare('return_status', $_POST['CustomerLog']['return_status']);
			}
			if (!empty($_POST['CustomerLog']['start_time']) && !empty($_POST['CustomerLog']['end_time'])) {
				$criteria->addBetweenCondition('create_time', trim($_POST['CustomerLog']['start_time']), trim($_POST['CustomerLog']['end_time']));
			} else {
				$criteria->addBetweenCondition('create_time', date("Y-m-d").' 00:00:00', date("Y-m-d").' 23:59:59');
			}
		}
		$dataProvider = new CActiveDataProvider('CustomerLog',array(
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