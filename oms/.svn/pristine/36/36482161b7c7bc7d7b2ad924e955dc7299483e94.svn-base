<?php

class CskShipAddressController extends Controller
{

    private $_model;

    public function actionIndex()
    {
        $this->render('index');
    }

    public function actionData()
    {
        $dataProvider = CskShipAddress::search($_POST);
        if (!empty($dataProvider)) {
            echo '{"total":' . $dataProvider->getTotalItemCount() . ',"rows":' . CJSON::encode($dataProvider->getData()) . '}';
        }
        //记录elk日志
        util::elkLog('基础信息', '发货地址列表查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
        Yii::app()->end();
    }

    public function actionAdd()
    {
    	//校验权限
    	util::operatePriContr(41.1);
        $model = new CskShipAddress();
        if (isset($_POST['Addr'])) {
        	//校验仓库地址编码是否存在
        	$contents_data = $model->findByPk($_POST['Addr']['ship_addr_code']);
        	if (!empty($contents_data)) {
        		die(json_encode(array(
        				'status' => 'error',
        				'msg' => '仓库地址编码已存在,保存失败'
        		)));
        	} 
        	$_POST['Addr']['oper_id'] = Yii::app()->user->soa_id;
        	$_POST['Addr']['oper_name'] = Yii::app()->user->user_title;
            $_POST['Addr']['oper_time'] = date("Y-m-d H:i:s");
            $model->attributes = $_POST['Addr'];
            if ($model->save()) {
                //记录elk日志
                util::elkLog('基础信息', '发货地址列表新增', 'info', $_POST['Addr']['ship_addr_code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'ok','msg' => '保存成功'), 'Y');
                die(json_encode(array(
                    'status' => 'ok',
                    'msg' => '保存成功'
                )));
                Yii::app()->end();
            } else {
                //记录elk日志
                util::elkLog('基础信息', '发货地址列表新增', 'error', $_POST['Addr']['ship_addr_code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'error','msg' => '错误操作，请找IT部'), 'N');
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
    	util::operatePriContr(41.1);
        $model = CskShipAddress::model();
        $addrInfo = $model->findByPk($id);
        if (isset($_POST['Addr'])) {
            $_POST['Addr']['oper_id'] = Yii::app()->user->soa_id;
            $_POST['Addr']['oper_name'] = Yii::app()->user->user_title;
            $_POST['Addr']['oper_time'] = date("Y-m-d H:i:s");
            $model->attributes = $_POST['Addr'];
            if ($model->save()) {
                //记录elk日志
                util::elkLog('基础信息', '发货地址列表更新', 'info', $_POST['Addr']['ship_addr_code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'ok','msg' => '更新成功'), 'Y');
                die(json_encode(array(
                    'status' => 'ok',
                    'msg' => '更新成功'
                )));
            }
        }
        $this->render('_form', array(
            'model' => $addrInfo
        ));
    }

    //作废货主
    public function actionDelete()
    {
    	//校验权限
    	util::operatePriContr(41.1, 'json');
        if (! isset($_POST['id'])) {
            //记录elk日志
            util::elkLog('基础信息', '发货地址列表删除', 'error', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'error','msg' => '非法操作'), 'N');
            die(json_encode(array(
                'status' => 'error',
                'msg' => '非法操作'
            )));
        }
        $delModel = $this->loadModel($_POST['id']);
        $_POST['Addr']['is_valid'] = 0;
        $delModel->attributes = $_POST['Addr'];
        if ($delModel->save()) {
            //记录elk日志
            util::elkLog('基础信息', '发货地址列表删除', 'info', $_POST['id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'ok','msg' => '删除成功'), 'Y');
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
                $this->_model = CskShipAddress::model()->findByPk($id);
            }
            if ($this->_model === null)
                throw new CHttpException(404, 'The requested page does not exist.');
        }
        return $this->_model;
    }
}
?>