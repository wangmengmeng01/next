<?php

class CustomerController extends Controller
{

    private $_model;

    public function actionIndex()
    {
        $this->render('index');
    }

    public function actionData()
    {
        $dataProvider = Customer::search($_POST);
        if (!empty($dataProvider)) {
            echo '{"total":' . $dataProvider->getTotalItemCount() . ',"rows":' . CJSON::encode($dataProvider->getData()) . '}';
        }
		//记录elk日志
		util::elkLog('基础信息', '货主维护查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
        Yii::app()->end();
    }

    public function actionAdd()
    {
    	//校验权限
    	util::operatePriContr(3.1);
        $model = new Customer();
        if (isset($_POST['Customer'])) {
        	//校验货主ID是否存在
        	$contents_data = $model->findByPk($_POST['Customer']['customer_id']);
        	if (!empty($contents_data)) {
				//记录elk日志
				util::elkLog('基础信息', '货主维护新增', 'error', $_POST['Customer']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'货主ID已存在,保存失败'), 'N');
        		die(json_encode(array(
        				'status' => 'error',
        				'msg' => '货主ID已存在,保存失败'
        		)));
        	}
            //校验货主名称是否存在
            $contents_data = $model->find('customer_name = :customer_name AND is_valid=1', array(
                ':customer_name' => $_POST['Customer']['customer_name']
            ));
            if (! empty($contents_data)) {
				//记录elk日志
				util::elkLog('基础信息', '货主维护新增', 'error', $_POST['Customer']['customer_name'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'货主名称已存在,保存失败'), 'N');
                die(json_encode(array(
                    'status' => 'error',
                    'msg' => '货主名称已存在,保存失败'
                )));
            }
            //校验网点编码是否存在
            $db = Yii::app()->db;
            $sql = 'SELECT bm FROM ydserver.gs WHERE bm=:bm';
            $dbModel = $db->createCommand($sql);
            $dbModel->bindValue(':bm', $_POST['Customer']['branch_code']);
            $row = $dbModel->queryRow();
            if (empty($row)) {
				//记录elk日志
				util::elkLog('基础信息', '货主维护新增', 'error', $_POST['Customer']['branch_code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'所属网点编码不存在，保存失败'), 'N');
            	die(json_encode(array(
            			'status' => 'error',
            			'msg' => '所属网点编码不存在，保存失败'
            	)));
            }
            $_POST['Customer']['operator_id'] = Yii::app()->user->soa_id;
            $_POST['Customer']['operator_name'] = Yii::app()->user->user_title;
            $_POST['Customer']['create_time'] = date("Y-m-d H:i:s");
            $model->attributes = $_POST['Customer'];
            if ($model->save()) {
				//记录elk日志
				util::elkLog('基础信息', '货主维护新增', 'info', $_POST['Customer']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'保存成功'), 'Y');
                die(json_encode(array(
                    'status' => 'ok',
                    'msg' => '保存成功'
                )));
                Yii::app()->end();
            } else {
				//记录elk日志
				util::elkLog('基础信息', '货主维护新增', 'error', $_POST['Customer']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'错误操作，请找IT部'), 'N');
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
    	util::operatePriContr(3.1);
        $model = Customer::model();
        $customerInfo = $model->findByPk($id);
        if (isset($_POST['Customer'])) {
            //校验货主名称是否存在
            $contents_data = $model->find('customer_name = :customer_name AND customer_id != :customer_id', array(
                ':customer_name' => $_POST['Customer']['customer_name'],
                ':customer_id' => $id
            ));
            if (! empty($contents_data)) {
				//记录elk日志
				util::elkLog('基础信息', '货主维护修改', 'error', $id, Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'货主名称已存在,保存失败'), 'N');
                die(json_encode(array(
                    'status' => 'error',
                    'msg' => '货主名称已存在,保存失败'
                )));
            }
            $_POST['Customer']['operator_id'] = Yii::app()->user->soa_id;
            $_POST['Customer']['operator_name'] = Yii::app()->user->user_title;
            $model->attributes = $_POST['Customer'];
            if ($model->save()) {
				//记录elk日志
				util::elkLog('基础信息', '货主维护修改', 'info', $id, Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'更新成功'), 'Y');
                die(json_encode(array(
                    'status' => 'ok',
                    'msg' => '更新成功'
                )));
                Yii::app()->end();
            }else{
				//记录elk日志
				util::elkLog('基础信息', '货主维护修改', 'error', $id, Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'错误操作，请找IT部'), 'N');
				die(json_encode(array('status'=>'error', 'msg'=>'错误操作，请找IT部')));
			}
        }
        $this->render('_form', array(
            'model' => $customerInfo
        ));
    }

    //作废货主
    public function actionDelete()
    {
    	//校验权限
    	util::operatePriContr(3.1, 'json');
        if (! isset($_POST['id'])) {
			//记录elk日志
			util::elkLog('基础信息', '货主维护删除', 'error', $_POST['id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'非法操作'), 'N');
            die(json_encode(array(
                'status' => 'error',
                'msg' => '非法操作'
            )));
        }
        $delModel = $this->loadModel($_POST['id']);
        $_POST['Customer']['is_valid'] = 0;
        $delModel->attributes = $_POST['Customer'];
        if ($delModel->save(false)) {
			//记录elk日志
			util::elkLog('基础信息', '货主维护删除', 'info', $_POST['id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'删除成功'), 'Y');
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
                $this->_model = Customer::model()->findByPk($id);
            }
            if ($this->_model === null)
                throw new CHttpException(404, 'The requested page does not exist.');
        }
        return $this->_model;
    }
}