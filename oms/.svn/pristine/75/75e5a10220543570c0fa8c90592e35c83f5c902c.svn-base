<?php

class WmsController extends Controller
{
	private $_model;
	
	public function actionIndex()
	{
		$this->render('index');
	}
	
	public function actionData() {
		$dataProvider = Wms::search($_POST);
		echo '{"total":'.$dataProvider->getTotalItemCount().',"rows":'.CJSON::encode($dataProvider->getData()).'}';
		//记录elk日志
		util::elkLog('基础信息', 'WMS软件维护查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
		Yii::app()->end();
	}

	//新增wms
	public function actionAdd() {
		//校验权限
		util::operatePriContr(2.1);
	    $model = new Wms();
	    if ( isset($_POST['Wms']) )
	    {	
	    	//校验wms编码是否存在
	        $contents_data = $model->find('wms_code = :wms_code', array(':wms_code'=>$_POST['Wms']['wms_code']));
	        if (!empty($contents_data)) {
				//记录elk日志
				util::elkLog('基础信息', 'WMS软件维护新增', 'error', $_POST['Wms']['wms_code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'WMS编码已存在,保存失败'), 'N');
	            die(json_encode(array('status'=>'error', 'msg'=>'WMS编码已存在,保存失败')));
	        } 
	        //校验wms名称是否存在
	        $contents_data = $model->find('wms_name = :wms_name AND is_valid=1', array(':wms_name'=>$_POST['Wms']['wms_name']));
	        if (!empty($contents_data)) {
				//记录elk日志
				util::elkLog('基础信息', 'WMS软件维护新增', 'error', $_POST['Wms']['wms_name'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'WMS名称已存在,保存失败'), 'N');
	            die(json_encode(array('status'=>'error', 'msg'=>'WMS名称已存在,保存失败')));
	        }
	        $_POST['Wms']['operator_id'] = Yii::app()->user->soa_id;
	        $_POST['Wms']['operator_name'] = Yii::app()->user->user_title;
	        $_POST['Wms']['create_time'] = date("Y-m-d H:i:s");
	        $model -> attributes = $_POST['Wms'] ;
	        if ($model->save())
	        {
				//记录elk日志
				util::elkLog('基础信息', 'WMS软件维护新增', 'info', $_POST['Wms']['wms_code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'保存成功'), 'Y');
	            die(json_encode(array('status'=>'ok', 'msg'=>'保存成功')));
	            Yii::app()->end();
	        } else {
				//记录elk日志
				util::elkLog('基础信息', 'WMS软件维护新增', 'error', $_POST['Wms']['wms_code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'错误操作，请找IT部'), 'N');
	            die(json_encode(array('status'=>'error', 'msg'=>'错误操作，请找IT部')));
	        }
	    }
	    $this->render('_form', array(
	        'model' => $model
	    ));
	}
	
	//修改wms信息
	public function actionUpdate($id)
	{	
		//校验权限
		util::operatePriContr(2.1);
		$model = Wms::model();
		$wmsInfo = $model->findByPk($id);
		if ( isset( $_POST['Wms'] ) )
		{
		 	//检索WMS名称是否存在
			$contents_data = $model->find('wms_name = :wms_name AND wms_id != :wms_id AND is_valid = 1', array(':wms_name'=>$_POST['Wms']['wms_name'],':wms_id'=>$id));
			if (!empty($contents_data)) {
				//记录elk日志
				util::elkLog('基础信息', 'WMS软件维护修改', 'error', $id, Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'WMS名称已存在,保存失败'), 'N');
				die(json_encode(array('status'=>'error', 'msg'=>'WMS名称已存在,保存失败')));
			}
			$_POST['Wms']['soa_id'] = Yii::app()->user->soa_id;
			$_POST['Wms']['soa_name'] = Yii::app()->user->user_title;
			$wmsInfo -> attributes = $_POST['Wms'];
			if ( $wmsInfo -> save() ) {
				//记录elk日志
				util::elkLog('基础信息', 'WMS软件维护修改', 'info', $id, Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'更新成功'), 'Y');
				die(json_encode(array('status'=>'ok', 'msg'=>'更新成功')));
				Yii::app()->end();
			} 
		}
		$this->render('_form',array(
			'model'=>$wmsInfo
		));
	}

	//删除wms，把wms有效性置为0
	public function actionDelete()
	{
		//校验权限
		util::operatePriContr(2.1, 'json');
		if ( !isset( $_POST['id'] ) ) {
			//记录elk日志
			util::elkLog('基础信息', 'WMS软件维护删除', 'error', $_POST['id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'非法操作'), 'N');
			die(json_encode(array('status' => 'error', 'msg' => '非法操作')));
		}
		$delModel = $this -> loadModel( $_POST['id'] );
		$_POST['Wms']['is_valid'] = 0;
		$delModel -> attributes = $_POST['Wms'];
		if ($delModel -> save(false)) {
			//记录elk日志
			util::elkLog('基础信息', 'WMS软件维护删除', 'info', $_POST['id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'删除成功'), 'Y');
			die(json_encode(array('status' => 'ok', 'msg' => '删除成功')));
		}
	}
	
	public function loadModel($id)
	{
		if ( $this->_model===null )
		{
			if ( isset( $id ) ) {
				$this->_model = Wms::model()->findByPk($id);
			}
			if ( $this->_model===null )
				throw new CHttpException(404,'The requested page does not exist.');
		}
		return $this->_model;
	}
}