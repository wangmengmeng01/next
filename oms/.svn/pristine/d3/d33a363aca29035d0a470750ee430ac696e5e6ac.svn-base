<?php
/**
 * 菜鸟电子面单接口交互日志model
 * table: t_order_interface_log
 */

class WaybillLog extends CActiveRecord
{
	public static function model($className=__CLASS__)
	{
		return parent::model($className);
	}
	
	public function tableName()
	{
		return 'csk_interface_log';
	}
	
	public static function search()
	{
		$criteria = new CDbCriteria();
		$_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
		$_POST['rows'] = empty($_POST['rows']) ? '50' : $_POST['rows'];
		if (isset($_POST['WaybillLog']['order_list']) && !empty($_POST['WaybillLog']['order_list'])) {
			$criteria->compare('order_list',trim($_POST['WaybillLog']['order_list']));
		} else {
			if (isset($_POST['WaybillLog']['app_code']) && !empty($_POST['WaybillLog']['app_code']) ) {
				$criteria->compare('app_code',trim($_POST['WaybillLog']['app_code']));
			}
			if (isset($_POST['WaybillLog']['customer_id']) && !empty($_POST['WaybillLog']['customer_id'])) {
				$criteria->compare('customer_id',trim($_POST['WaybillLog']['customer_id']));
			}
			if (isset($_POST['WaybillLog']['method']) && !empty($_POST['WaybillLog']['method'])) {
				$criteria->compare('method', $_POST['WaybillLog']['method']);
			}
			if (isset($_POST['WaybillLog']['return_status']) && $_POST['WaybillLog']['return_status'] != '') {
				$criteria->compare('return_status', $_POST['WaybillLog']['return_status']);
			}
			if (!empty($_POST['WaybillLog']['start_time']) && !empty($_POST['WaybillLog']['end_time'])) {
				$criteria->addBetweenCondition('create_time', trim($_POST['WaybillLog']['start_time']), trim($_POST['WaybillLog']['end_time']));
			} else {
				$criteria->addBetweenCondition('create_time', date("Y-m-d").' 00:00:00', date("Y-m-d").' 23:59:59');
			}
		}
		$dataProvider = new CActiveDataProvider('WaybillLog',array(
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
?>