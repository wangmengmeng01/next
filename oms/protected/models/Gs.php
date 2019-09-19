<?php

/**
 * This is the model class for table "ydserver.gs".
 *
 * The followings are the available columns in table 'ydserver.gs':
 * @property integer $bm
 * @property string $shi
 * @property string $mc
 * @property string $dz
 * @property string $yb
 * @property integer $sjdw
 * @property string $frdb
 * @property string $fzr
 * @property string $lxdh
 * @property string $khyh
 * @property string $yhzh
 * @property integer $lb
 * @property double $yzjj
 * @property integer $dqlsdm
 * @property integer $sjgs
 * @property string $kl
 * @property integer $qx
 * @property string $sheng
 * @property integer $jgf
 * @property string $fcz
 * @property integer $mdz
 * @property integer $wsqx
 * @property string $gsqx
 * @property string $szd
 * @property string $spd
 * @property integer $hklb
 * @property integer $jyfs
 */
class Gs extends CActiveRecord
{
	/**
	 * Returns the static model of the specified AR class.
	 * @param string $className active record class name.
	 * @return Gs the static model class
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
		return 'ydserver.gs';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('bm, sjdw, lb, dqlsdm, sjgs, qx, jgf, mdz, wsqx, hklb, jyfs', 'numerical', 'integerOnly'=>true),
			array('yzjj', 'numerical'),
			array('shi, yb, sheng, szd', 'length', 'max'=>6),
			array('mc, khyh', 'length', 'max'=>40),
			array('dz', 'length', 'max'=>100),
			array('frdb, fzr, kl', 'length', 'max'=>20),
			array('lxdh, spd', 'length', 'max'=>200),
			array('yhzh', 'length', 'max'=>30),
			array('fcz', 'length', 'max'=>11),
			array('gsqx', 'length', 'max'=>1000),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('bm, shi, mc, dz, yb, sjdw, frdb, fzr, lxdh, khyh, yhzh, lb, yzjj, dqlsdm, sjgs, kl, qx, sheng, jgf, fcz, mdz, wsqx, gsqx, szd, spd, hklb, jyfs', 'safe', 'on'=>'search'),
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
			'bm' => 'Bm',
			'shi' => 'Shi',
			'mc' => 'Mc',
			'dz' => 'Dz',
			'yb' => 'Yb',
			'sjdw' => 'Sjdw',
			'frdb' => 'Frdb',
			'fzr' => 'Fzr',
			'lxdh' => 'Lxdh',
			'khyh' => 'Khyh',
			'yhzh' => 'Yhzh',
			'lb' => 'Lb',
			'yzjj' => 'Yzjj',
			'dqlsdm' => 'Dqlsdm',
			'sjgs' => 'Sjgs',
			'kl' => 'Kl',
			'qx' => 'Qx',
			'sheng' => 'Sheng',
			'jgf' => 'Jgf',
			'fcz' => 'Fcz',
			'mdz' => 'Mdz',
			'wsqx' => 'Wsqx',
			'gsqx' => 'Gsqx',
			'szd' => 'Szd',
			'spd' => 'Spd',
			'hklb' => 'Hklb',
			'jyfs' => 'Jyfs',
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

		$criteria->compare('bm',$this->bm);
		$criteria->compare('shi',$this->shi,true);
		$criteria->compare('mc',$this->mc,true);
		$criteria->compare('dz',$this->dz,true);
		$criteria->compare('yb',$this->yb,true);
		$criteria->compare('sjdw',$this->sjdw);
		$criteria->compare('frdb',$this->frdb,true);
		$criteria->compare('fzr',$this->fzr,true);
		$criteria->compare('lxdh',$this->lxdh,true);
		$criteria->compare('khyh',$this->khyh,true);
		$criteria->compare('yhzh',$this->yhzh,true);
		$criteria->compare('lb',$this->lb);
		$criteria->compare('yzjj',$this->yzjj);
		$criteria->compare('dqlsdm',$this->dqlsdm);
		$criteria->compare('sjgs',$this->sjgs);
		$criteria->compare('kl',$this->kl,true);
		$criteria->compare('qx',$this->qx);
		$criteria->compare('sheng',$this->sheng,true);
		$criteria->compare('jgf',$this->jgf);
		$criteria->compare('fcz',$this->fcz,true);
		$criteria->compare('mdz',$this->mdz);
		$criteria->compare('wsqx',$this->wsqx);
		$criteria->compare('gsqx',$this->gsqx,true);
		$criteria->compare('szd',$this->szd,true);
		$criteria->compare('spd',$this->spd,true);
		$criteria->compare('hklb',$this->hklb);
		$criteria->compare('jyfs',$this->jyfs);

		return new CActiveDataProvider($this, array(
			'criteria'=>$criteria,
		));
	}
	
	/**
	 * 通过公司编码取得公司信息
	 * @param int $gsbm
	 * @return boolean
	 */
	public function getGsByBm($gsbm){
		if(is_numeric($gsbm)&&strlen($gsbm)==6){
			$cache = Yii::app()->cache;
			$data = $cache['gs:'.$gsbm];
			if(!$data){
				$data = self::model()->findByPk($gsbm,array('select'=>'bm,mc,lxdh,fzr,lb,szd'));
				$data = util::objToArray($data,array('bm','mc','lxdh','fzr','lb','szd'));
				$cache -> set('gs:'.$gsbm,$data,Yii::app()->params['cacheExpires']);
			}
			return $data;
		}
		return false;
		
	}
}