<?php

class WarehouseController extends Controller
{
    private $_model;

    public function actionIndex()
    {
        $this->render('index');
    }

    public function actionData()
    {
        $dataProvider = Warehouse::search($_POST);
        if (!empty($dataProvider)) {
            echo '{"total":' . $dataProvider->getTotalItemCount() . ',"rows":' . CJSON::encode($dataProvider->getData()) . '}';
        }
		//记录elk日志
		util::elkLog('基础信息', '仓库维护查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
        Yii::app()->end();
    }

    public function actionAdd()
    {
    	//校验权限
    	util::operatePriContr(4.1);
    	$model = new Warehouse();
    	if (isset($_POST['Warehouse'])) {
    		//校验仓库编码是否存在
    		$contents_data = $model->find('warehouse_code=:warehouse_code AND is_valid=1', array(
    				':warehouse_code' => $_POST['Warehouse']['warehouse_code']
    	    ));
    		if (!empty($contents_data)) {
				//记录elk日志
				util::elkLog('基础信息', '仓库维护新增', 'error', $_POST['Warehouse']['warehouse_code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'仓库编码已存在,保存失败'), 'N');
    			die(json_encode(array(
    					'status' => 'error',
    					'msg' => '仓库编码已存在,保存失败'
    			)));
    		}
    		//校验仓库名称是否存在
    		$contents_data = $model->find('descr_c = :descr_c AND is_valid=1', array(
    				':descr_c' => $_POST['Warehouse']['descr_c']
    		));
    		if (! empty($contents_data)) {
				//记录elk日志
				util::elkLog('基础信息', '仓库维护新增', 'error', $_POST['Warehouse']['descr_c'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'仓库名称已存在,保存失败'), 'N');
    			die(json_encode(array(
    					'status' => 'error',
    					'msg' => '仓库名称已存在,保存失败'
    			)));
    		}
    		//校验网点编码是否存在
    		$db = Yii::app()->db;
    		$sql = 'SELECT bm FROM ydserver.gs WHERE bm=:bm';
    		$dbModel = $db->createCommand($sql);
    		$dbModel->bindValue(':bm', $_POST['Warehouse']['branch_code']);
    		$row = $dbModel->queryRow();
    		if (empty($row)) {
				//记录elk日志
				util::elkLog('基础信息', '仓库维护新增', 'error', $_POST['Warehouse']['branch_code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'所属网点编码不存在，保存失败'), 'N');
    			die(json_encode(array(
    					'status' => 'error',
    					'msg' => '所属网点编码不存在，保存失败'
    			)));
    		}
    		$_POST['Warehouse']['operator_id'] = Yii::app()->user->soa_id;
    		$_POST['Warehouse']['operator_name'] = Yii::app()->user->user_title;
    		$_POST['Warehouse']['create_time'] = date("Y-m-d H:i:s");
    		$model->attributes = $_POST['Warehouse'];
    		if ($model->save()) {
				//记录elk日志
				util::elkLog('基础信息', '仓库维护新增', 'info', $_POST['Warehouse']['warehouse_code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'保存成功'), 'Y');
    			die(json_encode(array(
    					'status' => 'ok',
    					'msg' => '保存成功'
    			)));
    			Yii::app()->end();
    		} else {
				//记录elk日志
				util::elkLog('基础信息', '仓库维护新增', 'error', $_POST['Warehouse']['warehouse_code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'错误操作，请找IT部'), 'N');
    			die(json_encode(array(
    					'status' => 'error',
    					'msg' => '错误操作，请找IT部'
    			)));
    		}
    	}
    	$this->renderPartial('_form', array(
    			'model' => $model
    	));
    }
    
    public function actionUpdate($id)
    {
    	//校验权限
    	util::operatePriContr(4.1);
    	$model = Warehouse::model();
    	$warehouseInfo = $model->findByPk($id);
    	if (isset($_POST['Warehouse'])) {
    		//校验仓库名称是否存在
    		$contents_data = $model->find('descr_c = :descr_c AND warehouse_id != :warehouse_id', array(
    				':descr_c' => $_POST['Warehouse']['descr_c'],
    				':warehouse_id' => $id
    		));
    		if (! empty($contents_data)) {
				//记录elk日志
				util::elkLog('基础信息', '仓库维护修改', 'error', $id, Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'仓库名称已存在,保存失败'), 'N');
    			die(json_encode(array(
    					'status' => 'error',
    					'msg' => '仓库名称已存在,保存失败'
    			)));
    		}
    		$_POST['Warehouse']['operator_id'] = Yii::app()->user->soa_id;
    		$_POST['Warehouse']['operator_name'] = Yii::app()->user->user_title;

    		if (!isset($_POST['Warehouse']['wms_url'])) {
                $_POST['Warehouse']['wms_url'] = '';
            }

    		$warehouseInfo->attributes = $_POST['Warehouse'];
    		if ($warehouseInfo->save()) {
				//记录elk日志
				util::elkLog('基础信息', '仓库维护修改', 'info', $id, Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'更新成功'), 'Y');
    			die(json_encode(array(
    					'status' => 'ok',
    					'msg' => '更新成功'
    			)));
    			Yii::app()->end();
    		}else{
				//记录elk日志
				util::elkLog('基础信息', '仓库维护修改', 'error', $id, Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'错误操作，请找IT部'), 'N');
    			die(json_encode(array(
    					'status' => 'error',
    					'msg' => '错误操作，请找IT部'
    			)));
			}
    	}
    	$this->render('_form', array(
    			'model' => $warehouseInfo
    	));
    }
    
    //作废仓库
    public function actionDelete()
    {
    	//校验权限
    	util::operatePriContr(4.1, 'json');
    	if (! isset($_POST['id'])) {
			//记录elk日志
			util::elkLog('基础信息', '仓库维护删除', 'error', $_POST['id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'非法操作'), 'N');
    		die(json_encode(array(
    				'status' => 'error',
    				'msg' => '非法操作'
    		)));
    	}
    	$delModel = $this->loadModel($_POST['id']);
    	$_POST['Warehouse']['is_valid'] = 0;
    	$delModel->attributes = $_POST['Warehouse'];
    	if ($delModel->save(false)) {
			//记录elk日志
			util::elkLog('基础信息', '仓库维护删除', 'info', $_POST['id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'删除成功'), 'Y');
    		die(json_encode(array(
    				'status' => 'ok',
    				'msg' => '删除成功'
    		)));
    	}
    }
    
    public function loadModel($id)
    {
        if ($this->_model === null) {
            if (isset($id)) {
                $this->_model = Warehouse::model()->findByPk($id);
            }
            if ($this->_model === null)
                throw new CHttpException(404, 'The requested page does not exist.');
        }
        return $this->_model;
    }
}