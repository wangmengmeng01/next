<?php

class CskSellerAccessTokenController extends Controller
{

    private $_model;

    public function actionIndex()
    {
        $this->render('index');
    }

    public function actionData()
    {
        $dataProvider = CskSellerAccessToken::search($_POST);
        if (!empty($dataProvider)) {
            echo '{"total":' . $dataProvider->getTotalItemCount() . ',"rows":' . CJSON::encode($dataProvider->getData()) . '}';
        }
        //记录elk日志
        util::elkLog('基础信息', '商家授权列表查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
        Yii::app()->end();
    }

    public function actionAdd()
    {
    	//校验权限
    	util::operatePriContr(38.1);
        $model = new CskSellerAccessToken();
        if (isset($_POST['Seller'])) {
        	//校验货主ID是否存在
        	$contents_data = $model->findByPk($_POST['Seller']['seller_id']);
        	if (!empty($contents_data)) {
        		die(json_encode(array(
        				'status' => 'error',
        				'msg' => '商家ID已存在,保存失败'
        		)));
        	}
            //校验货主名称是否存在
            $contents_data = $model->find('access_token = :access_token AND is_valid=1', array(
                ':access_token' => $_POST['Seller']['access_token']
            ));
            if (! empty($contents_data)) {
                die(json_encode(array(
                    'status' => 'error',
                    'msg' => '授权口令已存在,保存失败'
                )));
            }
            $_POST['Seller']['create_time'] = date("Y-m-d H:i:s");
            $model->attributes = $_POST['Seller'];
            if ($model->save()) {
                //记录elk日志
                util::elkLog('基础信息', '商家授权列表新增', 'info', $_POST['Seller']['seller_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'ok','msg' => '保存成功'), 'Y');
                die(json_encode(array(
                    'status' => 'ok',
                    'msg' => '保存成功'
                )));
                Yii::app()->end();
            } else {
                //记录elk日志
                util::elkLog('基础信息', '商家授权列表新增', 'error', $_POST['Seller']['seller_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'error','msg' => '错误操作，请找IT部'), 'N');
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
    	util::operatePriContr(38.1);
        $model = CskSellerAccessToken::model();
        $sellerInfo = $model->findByPk($id);
        if (isset($_POST['Seller'])) {
            //校验商家是否存在
            $contents_data = $model->find('access_token = :access_token AND seller_id != :seller_id', array(
                ':access_token' => $_POST['Seller']['access_token'],
                ':seller_id' => $id
            ));
            if (! empty($contents_data)) {
                die(json_encode(array(
                    'status' => 'error',
                    'msg' => '该授权口令已存在,保存失败'
                )));
            }
            $model->attributes = $_POST['Seller'];
            if ($model->save()) {
                //记录elk日志
                util::elkLog('基础信息', '商家授权列表修改', 'info', $id, Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'ok','msg' => '更新成功'), 'Y');
                die(json_encode(array(
                    'status' => 'ok',
                    'msg' => '更新成功'
                )));
                Yii::app()->end();
            }
        }
        $this->render('_form', array(
            'model' => $sellerInfo
        ));
    }

    //作废货主
    public function actionDelete()
    {
    	//校验权限
    	util::operatePriContr(38.1, 'json');
        if (! isset($_POST['id'])) {
            //记录elk日志
            util::elkLog('基础信息', '商家授权列表删除', 'error', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'error','msg' => '非法操作'), 'N');
            die(json_encode(array(
                'status' => 'error',
                'msg' => '非法操作'
            ))); 
        }
        $delModel = $this->loadModel($_POST['id']);
        $_POST['Seller']['is_valid'] = 0;
        $delModel->attributes = $_POST['Seller'];
        if ($delModel->save(false)) {
            //记录elk日志
            util::elkLog('基础信息', '商家授权列表删除', 'info', $_POST['id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'ok','msg' => '删除成功'), 'Y');
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
                $this->_model = CskSellerAccessToken::model()->findByPk($id);
            }
            if ($this->_model === null)
                throw new CHttpException(404, 'The requested page does not exist.');
        }
        return $this->_model;
    }
}