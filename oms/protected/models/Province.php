<?php

/**
 * This is the model class for table "ydserver.province".
 *
 * The followings are the available columns in table 'ydserver.province':
 * @property string $ProvinceID
 * @property string $ProvinceName
 * @property string $miniName
 * @property string $bigarea
 */
class Province extends CActiveRecord
{
	/**
	 * Returns the static model of the specified AR class.
	 * @param string $className active record class name.
	 * @return Province the static model class
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
		return 'ydserver.province';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('ProvinceID', 'required'),
			array('ProvinceID', 'length', 'max'=>6),
			array('ProvinceName', 'length', 'max'=>100),
			array('miniName', 'length', 'max'=>20),
			array('bigarea', 'length', 'max'=>4),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('ProvinceID, ProvinceName, miniName, bigarea', 'safe', 'on'=>'search'),
		);
	}

	/**
	 * @return array relational rules.
	 */
	public function relations()
	{
		// NOTE: you may need to adjust the relation name and the related
		// class name for the relations automatically generated below.
		return array(
		);
	}

	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
			'ProvinceID' => 'Province',
			'ProvinceName' => 'Province Name',
			'miniName' => 'Mini Name',
			'bigarea' => 'Bigarea',
		);
	}

	/**
	 * Retrieves a list of models based on the current search/filter conditions.
	 * @return CActiveDataProvider the data provider that can return the models based on the search/filter conditions.
	 */
	public function search()
	{
		// Warning: Please modify the following code to remove attributes that
		// should not be searched.

		$criteria=new CDbCriteria;

		$criteria->compare('ProvinceID',$this->ProvinceID,true);
		$criteria->compare('ProvinceName',$this->ProvinceName,true);
		$criteria->compare('miniName',$this->miniName,true);
		$criteria->compare('bigarea',$this->bigarea,true);

		return new CActiveDataProvider($this, array(
			'criteria'=>$criteria,
		));
	}

	public static function getPrvoice($proviceId='')
	{
		//获取省表中所有数据
		$provices = self::model()->findAll();
		$arr = array();
		array_push($arr,array('ProvinceID'=>0,'ProvinceName'=>'请选择省份'));
		//判断获取数据，是否为空。非空，将省编码为键，省名为值。
		if ( $provices == null ) {
			return '';
		} else {
			foreach( $provices as $item ) {
				if(!empty($proviceId) && $item->ProvinceID == $proviceId){
					array_push($arr,array('ProvinceID'=>$item->ProvinceID,'ProvinceName'=>$item->ProvinceName,'selected'=>true));
				}else{
					array_push($arr,array('ProvinceID'=>$item->ProvinceID,'ProvinceName'=>$item->ProvinceName));
				}
			}
			return $arr;
		}
	
	}
}