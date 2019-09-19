<?php
class DestinationWarehouseBind extends CActiveRecord
{
    public static function model($className=__CLASS__)
	{
		return parent::model($className);
	}
	
	public function tableName()
	{
		return '{{vip_wh_info}}';
	}
	
	public function rules()
	{
		return array(
				array('customer_id,customer_name,rc_addr,wh1', 'required'),
				array('operator_id,operator_name,create_time,update_time,platform_code,shop_name,wh2,wh3,wh4,wh5,wh6,wh7,wh8,wh9,wh10,is_valid', 'safe'),
				// The following rule is used by search().
				// Please remove those attributes that should not be searched.
				array('customer_id,customer_name', 'safe', 'on'=>'search'),
		);
	}
	
	public function attributeLabels()
	{
		return array(
				'customer_id' => '货主编码',
				'customer_name' => '货主名称',
				'platform_code' => '平台编码',
				'shop_name' => '店铺名称',
				'rc_addr' => '固定收货机构或地址',
				'wh1' => '分仓1',
				'wh2' => '分仓2',
				'wh3' => '分仓3',
				'wh4' => '分仓4',
				'wh5' => '分仓5',
				'wh6' => '分仓6',
				'wh7' => '分仓7',
				'wh8' => '分仓8',
				'wh9' => '分仓9',
				'wh10' => '分仓10',
		);
	}
	
	public static function search()
	{
		$criteria = new CDbCriteria();
		//校验是否有查看所有货主的权限
//		$viewFlag = util::hasViewAll();
//		if ($viewFlag == 0) {
//			$existsArr = util::getCustomer();
//			if (!empty($existsArr)) {
//				$criteria->addInCondition('customer_id', $existsArr);
//			} else {
//				return ;
//			}
//		}
		if ( isset($_POST['DestinationWarehouseBind']['customer_id']) ) {
			$criteria->compare('customer_id',trim($_POST['DestinationWarehouseBind']['customer_id']));
		}
		if ( isset($_POST['DestinationWarehouseBind']['customer_name']) ) {
			$criteria->compare('customer_name',trim($_POST['DestinationWarehouseBind']['customer_name']));
		}
        $criteria->compare('is_valid',1);
		$_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
		$_POST['rows'] = empty($_POST['rows']) ? '50' : $_POST['rows'];

		$dataProvider = new CActiveDataProvider('DestinationWarehouseBind',array(
				'sort'=>array(
						'defaultOrder'=>'id Desc',
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