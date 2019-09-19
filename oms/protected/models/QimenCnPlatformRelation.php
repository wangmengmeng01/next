<?php

/**
 * 奇门-菜鸟电商平台对应表model
 * table: t_qimen_cn_platform_relation
 */
class QimenCnPlatformRelation extends CActiveRecord
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
		return '{{qimen_cn_platform_relation}}';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('qimen_platform_code', 'required'),
			array('is_valid', 'numerical', 'integerOnly'=>true),
		    array('id,qimen_platform_code,qimen_platform_name,cn_platform_code,cn_platform_name,oper_name,oper_id,oper_time','safe'),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('id,qimen_platform_code,qimen_platform_name,cn_platform_code,cn_platform_name,oper_name,oper_time', 'safe', 'on'=>'search'),
		);
	}


	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
				'qimen_platform_code' => '奇门电商平台编码',
		        'qimen_platform_name' => '奇门电商平台名称',
		        'cn_platform_code' => '菜鸟电商平台编码',
				'cn_platform_name' => '菜鸟电商平台名称',
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
		if ( isset($_POST['Relation']['qimen_platform_code']) ) {
			$criteria->compare('qimen_platform_code',trim($_POST['Relation']['qimen_platform_code']));
		}
		if ( isset($_POST['Relation']['cn_platform_code']) ) {
		      $criteria->compare('cn_platform_code',trim($_POST['Relation']['cn_platform_code']),true);
		}	
		if ( isset($_POST['Relation']['is_valid']) ) {
		      $criteria->compare('is_valid',trim($_POST['Relation']['is_valid']));
		}	
		$_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
		$_POST['rows'] = empty($_POST['rows']) ? '50' : $_POST['rows'];
		$dataProvider = new CActiveDataProvider('QimenCnPlatformRelation',array(
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