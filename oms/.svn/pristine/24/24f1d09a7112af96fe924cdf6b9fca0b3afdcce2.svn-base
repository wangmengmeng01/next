<?php
class CustomerWarehouseBind extends CActiveRecord
{
    public static function model($className=__CLASS__)
	{
		return parent::model($className);
	}
	
	public function tableName()
	{
		return '{{customer_relation}}';
	}
	
	public function rules()
	{
		return array(
				array('customer_id,code', 'required'),
				array('is_valid', 'numerical', 'integerOnly'=>true),
				array('operator_id,operator_name,type,create_time', 'safe'),
				// The following rule is used by search().
				// Please remove those attributes that should not be searched.
				array('customer_id,code,is_valid', 'safe', 'on'=>'search'),
		);
	}
	
	public function attributeLabels()
	{
		return array(
				'customer_id' => '货主ID',
				'code' => '仓库编码',	
				'type' => '类型',			
				'is_valid' => '有效性'
		);
	}
	
	public static function search()
	{
		$criteria = new CDbCriteria();
		//校验是否有查看所有货主的权限
		$viewFlag = util::hasViewAll();
		if ($viewFlag == 0) {
			$existsArr = util::getCustomer();
			if (!empty($existsArr)) {
				$criteria->addInCondition('customer_id', $existsArr);
			} else {
				return ;
			}
		}		
		if ( isset($_POST['CustomerWarehouseBind']['customer_id']) ) {
			$criteria->compare('customer_id',trim($_POST['CustomerWarehouseBind']['customer_id']));
		}
		if ( isset($_POST['CustomerWarehouseBind']['code']) ) {
			$criteria->compare('code',trim($_POST['CustomerWarehouseBind']['code']));
		}
		if ( isset($_POST['CustomerWarehouseBind']['is_valid']) ) {
			$criteria->compare('is_valid',trim($_POST['CustomerWarehouseBind']['is_valid']));
		}
		$criteria->compare('type','WH');
		$_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
		$_POST['rows'] = empty($_POST['rows']) ? '50' : $_POST['rows'];
		$dataProvider = new CActiveDataProvider('CustomerWarehouseBind',array(
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