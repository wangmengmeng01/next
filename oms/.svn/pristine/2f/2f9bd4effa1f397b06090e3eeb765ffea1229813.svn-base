<?php

/**
 * ERP软件维护model
 * base_erp
 */
class Erp extends CActiveRecord
{
	/**
	 * Returns the static model of the specified AR class.
	 * @param string $className active record class name.
	 * @return Erp the static model class
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
		return '{{base_erp}}';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('erp_code,erp_name', 'required'),
			array('is_valid', 'numerical', 'integerOnly'=>true),
			array('contact,contact_phone,contact_tel,address,remark,operator_id,operator_name,create_time', 'safe'),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('erp_code, erp_name, is_valid', 'safe', 'on'=>'search')
		);
	}


	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
				'erp_code' => 'ERP编码',
				'erp_name' => 'ERP名称',
				'contact' => '联系人',
				'contact_phone' => '联系人手机号',
				'contact_tel' => '联系人固话',				
				'address' => '联系人地址',
				'remark' => '备注',
				'is_valid' => '有效性'
		);
	} 


    /**
     * @return array relational rules.
     */	
    public function relations()  
    {  
        return array(  
            'province'=>array(self::BELONGS_TO, 'Province', 'province')
        );  
    }
    	
	/**
	 * Retrieves a list of models based on the current search/filter conditions.
	 * @return CActiveDataProvider the data provider that can return the models based on the search/filter conditions.
	 */
	public function search()
	{
		$criteria = new CDbCriteria();
		//校验是否有查看所有ERP的权限
		$viewFlag = util::hasViewAll();
		if ($viewFlag == 0) {
			$existsArr = util::getCustomer('ERP');
			if (!empty($existsArr)) {
				$criteria->addInCondition('erp_code', $existsArr);
			} else {
				return ;
			}
		}		
		if ( isset($_POST['Erp']['erp_name']) ) {
		      $criteria->compare('erp_name',trim($_POST['Erp']['erp_name']),true);
		}
		if ( isset($_POST['Erp']['erp_code']) ) {
		      $criteria->compare('erp_code',trim($_POST['Erp']['erp_code']));
		}
		if ( isset($_POST['Erp']['is_valid']) ) {
		      $criteria->compare('is_valid',trim($_POST['Erp']['is_valid']));
		}
		$_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
		$_POST['rows'] = empty($_POST['rows']) ? '50' : $_POST['rows'];
		$dataProvider = new CActiveDataProvider('Erp',array(
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