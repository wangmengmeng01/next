<?php
/**
 * 奇门货主绑定model
 * table: t_qimen_customer_bind
 * @author Renee
 *
 */

class QimenCustomerBind extends CActiveRecord
{
    public static function model($className=__CLASS__)
	{
		return parent::model($className);
	}
	
	public function tableName()
	{
		return '{{qimen_customer_bind}}';
	}
	
	public function rules()
	{
		return array(
				array('qimen_customer_id,customer_id', 'required'),
				array('auto_flag,is_valid', 'numerical', 'integerOnly'=>true),
				array('warehouse_code,operator_id,operator_name,create_time', 'safe'),
				// The following rule is used by search().
				// Please remove those attributes that should not be searched.
				array('qimen_customer_id,customer_id,warehouse_code,auto_flag,is_valid', 'safe', 'on'=>'search')
		);
	}
	
	public function attributeLabels()
	{
		return array(
		        'qimen_customer_id' => '奇门货主ID',
				'customer_id' => '货主ID',
		        'warehouse_code' => '仓库编码',
				'operator_name' => '操作人姓名',
		        'operator_id' => '操作人ID',
				'auto_flag'   => '智能仓标识',
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
		if ( isset($_POST['QIMEN_CustomerBind']['qimen_customer_id']) ) {
			$criteria->compare('qimen_customer_id',trim($_POST['QIMEN_CustomerBind']['qimen_customer_id']));
		}
		if ( isset($_POST['QIMEN_CustomerBind']['customer_id']) ) {
			$criteria->compare('customer_id',trim($_POST['QIMEN_CustomerBind']['customer_id']));
		}
		if ( isset($_POST['QIMEN_CustomerBind']['warehouse_code']) ) {
		    $criteria->compare('warehouse_code',trim($_POST['QIMEN_CustomerBind']['warehouse_code']));
		}
        if ( isset($_POST['QIMEN_CustomerBind']['auto_flag']) ) {
            $criteria->compare('auto_flag',trim($_POST['QIMEN_CustomerBind']['auto_flag']));
        }
		if ( isset($_POST['QIMEN_CustomerBind']['is_valid']) ) {
			$criteria->compare('is_valid',trim($_POST['QIMEN_CustomerBind']['is_valid']));
		}
		$_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
		$_POST['rows'] = empty($_POST['rows']) ? '50' : $_POST['rows'];
		$dataProvider = new CActiveDataProvider('QimenCustomerBind',array(
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