<?php

class LogisticsController extends Controller
{
	private $_model;
	
	public function actionIndex()
	{
		$this->render('index');
	}
	public function actionData() {
		$dataProvider = Logistics::search($_POST);
		echo '{"total":'.$dataProvider->getTotalItemCount().',"rows":'.CJSON::encode($dataProvider->getData()).'}';
		//记录elk日志
		util::elkLog('基础信息', '物流公司维护查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
		Yii::app()->end();
	}

	public function actionAdd() {
		//校验权限
		util::operatePriContr(7.1);
	    $model = new Logistics();
	    if ( isset($_POST['Logistics']) )
	    {	
	    	//检索物流公司编码是否存在
	        $contents_data = $model->find('logistics_code = :logistics_code', array(':logistics_code'=>$_POST['Logistics']['logistics_code']));
	        if (!empty($contents_data)) {
	            die(json_encode(array('status'=>'error', 'msg'=>'物流编码已存在,保存失败')));
	        } 
	        //检索物流公司名称是否存在
	        $contents_data = $model->find('logistics_name = :logistics_name AND is_valid=1', array(':logistics_name'=>$_POST['Logistics']['logistics_name']));
	        if (!empty($contents_data)) {
	            die(json_encode(array('status'=>'error', 'msg'=>'物流公司名称已存在,保存失败')));
	        }
	        $_POST['Logistics']['operator_id'] = Yii::app()->user->soa_id;
	        $_POST['Logistics']['operator_name'] = Yii::app()->user->user_title;
	        $_POST['Logistics']['create_time'] = date("Y-m-d H:i:s");
	        $model -> attributes = $_POST['Logistics'] ;
	        if ($model->save())
	        {
	            //记录elk日志
	            util::elkLog('基础信息', '物流公司维护新增', 'info', $_POST['Logistics']['logistics_code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'保存成功'), 'Y');
	            die(json_encode(array('status'=>'ok', 'msg'=>'保存成功')));
	            Yii::app()->end();
	        } else {
	            //记录elk日志
	            util::elkLog('基础信息', '物流公司维护新增', 'error', $_POST['Logistics']['logistics_code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'错误操作，请找IT部'), 'N');
	            die(json_encode(array('status'=>'error', 'msg'=>'错误操作，请找IT部')));
	        }
	    }
	    $this->render('_form', array(
	        'model' => $model
	    ));
	}
	
	public function actionUpdate($id)
	{	
		//校验权限
		util::operatePriContr(7.1);
		$model = Logistics::model();
		$logisticsInfo = $model->findByPk($id);
		if ( isset( $_POST['Logistics'] ) )
		{
		 	//检索物流公司名称是否存在
			$contents_data = $model->find('logistics_name = :logistics_name AND logistics_id != :logistics_id', array(':logistics_name'=>$_POST['Logistics']['logistics_name'],':logistics_id'=>$id));
			if (!empty($contents_data)) {
				die(json_encode(array('status'=>'error', 'msg'=>'物流公司名称已存在,保存失败')));
			}
			$_POST['Logistics']['soa_id'] = Yii::app()->user->soa_id;
			$_POST['Logistics']['soa_name'] = Yii::app()->user->user_title;
			$logisticsInfo -> attributes = $_POST['Logistics'];
			if ( $logisticsInfo -> save() ) {
			    //记录elk日志
			    util::elkLog('基础信息', '物流公司维护修改', 'info', $_POST['Logistics']['logistics_code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'更新成功'), 'Y');
				die(json_encode(array('status'=>'ok', 'msg'=>'更新成功')));
				Yii::app()->end();
			} 
		}
		$this->render('_form',array(
			'model'=>$logisticsInfo
		));
	}
	
    //停用物流公司
	public function actionDelete()
	{
		//校验权限
		util::operatePriContr(7.1, 'json');
		if ( !isset( $_POST['id'] ) ) {
		    //记录elk日志
		    util::elkLog('基础信息', '物流公司维护删除', 'error', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'error', 'msg' => '非法操作'), 'N');
			die(json_encode(array('status' => 'error', 'msg' => '非法操作')));
		}
		$delModel = $this -> loadModel( $_POST['id'] );
		$_POST['Logistics']['is_valid'] = 0;
		$delModel -> attributes = $_POST['Logistics'];
		if ($delModel->save(false)) {
		    //记录elk日志
		    util::elkLog('基础信息', '物流公司维护删除', 'info', $_POST['id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'ok', 'msg' => '删除成功'), 'Y');
			die(json_encode(array('status' => 'ok', 'msg' => '删除成功')));
		}
	}
	
	public function loadModel($id)
	{
		if ( $this->_model===null )
		{
			if ( isset( $id ) ) {
				$this->_model = Logistics::model()->findByPk($id);
			}
			if ( $this->_model===null )
				throw new CHttpException(404,'The requested page does not exist.');
		}
		return $this->_model;
	}

}