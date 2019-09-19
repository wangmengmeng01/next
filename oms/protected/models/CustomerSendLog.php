<?php
/**
 * 客商档案推送列表model
 * table: t_customer_send_log
 * @author wp
 *
 */
class CustomerSendLog extends CActiveRecord
{
    public static function model($className=__CLASS__)
	{
		return parent::model($className);
	}
	
	public function tableName()
	{
		return '{{customer_send_log}}';
	}
	
	public static function search()
	{
		$criteria = new CDbCriteria();
		$_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
		$_POST['rows'] = empty($_POST['rows']) ? '50' : $_POST['rows'];
		if ( isset($_POST['CustomerSendLog']['customer_id']) ) {
			$criteria->compare('customer_id',trim($_POST['CustomerSendLog']['customer_id']));
		}
		if ( isset($_POST['CustomerSendLog']['customer_type']) ) {
			$criteria->compare('customer_type',trim($_POST['CustomerSendLog']['customer_type']));
		}
		if ( isset($_POST['CustomerSendLog']['send_erp_status']) ) {
			$criteria->compare('send_erp_status',trim($_POST['CustomerSendLog']['send_erp_status']));
		}
		if ( isset($_POST['CustomerSendLog']['send_wms_status']) ) {
			$criteria->compare('send_wms_status',trim($_POST['CustomerSendLog']['send_wms_status']));
		}
		$dataProvider = new CActiveDataProvider('CustomerSendLog',array(
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