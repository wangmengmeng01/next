<?php

/**
 * 商家维护model
 * table: csk_seller_access_token
 */
class CskSellerCustomeridRelation extends CActiveRecord
{
	/**
	 * Returns the static model of the specified AR class.
	 * @param string $className active record class name.
	 * @return Customer the static model class
	 */
	public static function model($className=__CLASS__)
	{
		return parent::model($className);
	}

	/**
	 * @return string the associated database table name
	 */
	public function tableName()
	{
		return 'csk_seller_customerid_relation';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('seller_id', 'required'),
			array('is_valid', 'numerical', 'integerOnly'=>true),
		    array('relation_id,ship_addr_code,customer_code,platform_elec,platform_mall,shop_name,oper_name,oper_id,oper_time','safe'),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('seller_id, ship_addr_code, customer_code, platform_elec, platform_mall,is_valid, shop_name, oper_time', 'safe', 'on'=>'search'),
		);
	}


	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
				'seller_id' => '商家id',
		        'ship_addr_code' => '仓库地址编码',
		        'customer_code' => '货主代码',
				'platform_elec' => '电子面单平台',
		        'platform_mall' => '电商平台',
				'shop_name' => '店铺名称',
				'is_valid' => '有效性'
		);
	}
	
	/**
	 * Retrieves a list of models based on the current search/filter conditions.
	 * @return CActiveDataProvider the data provider that can return the models based on the search/filter conditions.
	 */
	public static function search()
	{		
		$criteria = new CDbCriteria();
		
        //$criteria->addCondition("is_valid=1"); //查询条件，即where is_valid = 1  		
		if ( isset($_POST['Relation']['seller_id']) ) {
			$criteria->compare('seller_id',trim($_POST['Relation']['seller_id']));
		}
		if ( isset($_POST['Relation']['customer_code']) ) {
		      $criteria->compare('customer_code',trim($_POST['Relation']['customer_code']),true);
		}	
		if ( isset($_POST['Relation']['ship_addr_code']) ) {
			$criteria->compare('ship_addr_code',trim($_POST['Relation']['ship_addr_code']),true);
		}
		if ( isset($_POST['Relation']['shop_name']) ) {
		    $criteria->compare('shop_name',trim($_POST['Relation']['shop_name']),true);
		}
		if ( isset($_POST['Relation']['is_valid']) ) {
		      $criteria->compare('is_valid',trim($_POST['Relation']['is_valid']));
		}	
		$_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
		$_POST['rows'] = empty($_POST['rows']) ? '50' : $_POST['rows'];
		$dataProvider = new CActiveDataProvider('CskSellerCustomeridRelation',array(
    		    'sort'=>array(
    		        'defaultOrder'=>'oper_time Desc',
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