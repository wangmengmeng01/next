<?php
class ErpController extends Controller
{
	private $_model;
	
	public function actionIndex()
	{
		$this->render('index');
	}
	
	//获取ERP表中的数据
	public function actionData() {
		$dataProvider = Erp::search($_POST);
		echo '{"total":'.$dataProvider->getTotalItemCount().',"rows":'.CJSON::encode($dataProvider->getData()).'}';
		//记录elk日志
		util::elkLog('基础信息', 'ERP软件维护查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
		Yii::app()->end();
	}

	//添加ERP
	public function actionAdd() {
		//校验权限
        util::operatePriContr(1.1);
	    $model = new Erp();
	    if ( isset($_POST['Erp']) )
	    {	//校验ERP编码是否存在
	        $contents_data = $model->find('erp_code = :erp_code', array(':erp_code'=>$_POST['Erp']['erp_code']));
	        if (!empty($contents_data)) {
				//记录elk日志
	        	util::elkLog('基础信息', 'ERP软件维护新增', 'error', $_POST['Erp']['erp_code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'ERP编码已存在,保存失败'), 'N');
	            die(json_encode(array('status'=>'error', 'msg'=>'ERP编码已存在,保存失败')));
	        } 
	        //校验ERP名称是否存在
	        $contents_data = $model->find('erp_name = :erp_name AND is_valid=1', array(':erp_name'=>$_POST['Erp']['erp_name']));
	        if (!empty($contents_data)) {
				//记录elk日志
	        	util::elkLog('基础信息', 'ERP软件维护新增', 'error', $_POST['Erp']['erp_name'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'ERP名称已存在,保存失败'), 'N');
	            die(json_encode(array('status'=>'error', 'msg'=>'ERP名称已存在,保存失败')));
	        }
	        $_POST['Erp']['operator_id'] = Yii::app()->user->soa_id;
	        $_POST['Erp']['operator_name'] = Yii::app()->user->user_title;
	        $_POST['Erp']['create_time'] = date("Y-m-d H:i:s");
	        $model -> attributes = $_POST['Erp'] ;
	        if ($model -> save()) {
	        	//记录elk日志
	        	util::elkLog('基础信息', 'ERP软件维护新增', 'info', $_POST['Erp']['erp_code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'保存成功'), 'Y');
	            die(json_encode(array('status'=>'ok', 'msg'=>'保存成功')));
	            Yii::app()->end();
	        } else {
	        	//记录elk日志
	        	util::elkLog('基础信息', 'ERP软件维护新增', 'error', $_POST['Erp']['erp_code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'错误操作，请找IT部'), 'N');
	            die(json_encode(array('status'=>'error', 'msg'=>'错误操作，请找IT部')));
	        }
	    }
	    $this->render('_form', array(
	        'model' => $model
	    ));
	}
	
	//修改ERP
	public function actionUpdate($id)
	{	
		//校验权限
        util::operatePriContr(1.1);
		$model = Erp::model();
		$erpInfo = $model->findByPk($id);		
		if (isset( $_POST['Erp'] )) {
		 	//检索ERP名称是否存在
			$contents_data = $model->find('erp_name = :erp_name AND erp_id != :erp_id', array(':erp_name'=>$_POST['Erp']['erp_name'],':erp_id'=>$id));
			if (!empty($contents_data)) {
				//记录elk日志
				util::elkLog('基础信息', 'ERP软件维护修改', 'error', $id, Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'ERP名称已存在,保存失败'), 'N');
				die(json_encode(array('status'=>'error', 'msg'=>'ERP名称已存在,保存失败')));
			}
			$_POST['Erp']['operator_id'] = Yii::app()->user->soa_id;
			$_POST['Erp']['operator_name'] = Yii::app()->user->user_title;
			$erpInfo->attributes = $_POST['Erp'];
			if ($erpInfo->save()) {
				//记录elk日志
				util::elkLog('基础信息', 'ERP软件维护修改', 'info', $id, Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'更新成功'), 'Y');
				die(json_encode(array('status'=>'ok', 'msg'=>'更新成功')));
				Yii::app()->end();
			} 
		}
		$this->render('_form',array(
			'model'=>$erpInfo
		));
	}
	
    //删除ERP，有效性置为无效
	public function actionDelete()
	{
		//校验权限
        util::operatePriContr(1.1, 'json');
		if ( !isset( $_POST['id'] ) ) {
			//记录elk日志
			util::elkLog('基础信息', 'ERP软件维护删除', 'error', $_POST['id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'error', 'msg' => '非法操作'), 'N');
			die(json_encode(array('status' => 'error', 'msg' => '非法操作')));
		}
		$delModel = $this -> loadModel( $_POST['id'] );
		$_POST['Erp']['is_valid'] = 0;
		$delModel -> attributes = $_POST['Erp'];
		if ( $delModel -> save(false) ) {
			//记录elk日志
			util::elkLog('基础信息', 'ERP软件维护删除', 'info', $_POST['id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'ok', 'msg' => '删除成功'), 'Y');
			die(json_encode(array('status' => 'ok', 'msg' => '删除成功')));
		}
	}
	
	public function loadModel($id)
	{
		if ( $this->_model===null )
		{
			if ( isset( $id ) ) {
				$this->_model = Erp::model()->findByPk($id);
			}
			if ( $this->_model===null )
				throw new CHttpException(404,'The requested page does not exist.');
		}
		return $this->_model;
	}
}