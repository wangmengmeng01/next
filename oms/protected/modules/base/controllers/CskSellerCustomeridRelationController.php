<?php

class CskSellerCustomeridRelationController extends Controller
{

    private $_model;

    public function actionIndex()
    {
        $this->render('index');
    }

    public function actionData()
    {
        $dataProvider = CskSellerCustomeridRelation::search($_POST);
        if (!empty($dataProvider)) {
            echo '{"total":' . $dataProvider->getTotalItemCount() . ',"rows":' . CJSON::encode($dataProvider->getData()) . '}';
        }
        //记录elk日志
        util::elkLog('基础信息', '货主与商家关联信息列表查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
        Yii::app()->end();
    }

    public function actionAdd()
    {
    	//校验权限
    	util::operatePriContr(40.1);
        $model = new CskSellerCustomeridRelation();
        if (isset($_POST['Relation'])) {
        	$_POST['Relation']['oper_id'] = Yii::app()->user->soa_id;
        	$_POST['Relation']['oper_name'] = Yii::app()->user->user_title;
            $_POST['Relation']['oper_time'] = date("Y-m-d H:i:s");
            $model->attributes = $_POST['Relation'];
            if ($model->save()) {
                //记录elk日志
                util::elkLog('基础信息', '货主与商家关联信息列表新增', 'info', $_POST['Relation']['seller_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'ok','msg' => '保存成功'), 'Y');
                die(json_encode(array(
                    'status' => 'ok',
                    'msg' => '保存成功'
                )));
                Yii::app()->end();
            } else {
                //记录elk日志
                util::elkLog('基础信息', '货主与商家关联信息列表新增', 'error', $_POST['Relation']['seller_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'error','msg' => '错误操作，请找IT部'), 'N');
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
    	util::operatePriContr(40.1);
        $model = CskSellerCustomeridRelation::model();
        $relationInfo = $model->findByPk($id);
        if (isset($_POST['Relation'])) {
            $_POST['Relation']['oper_id'] = Yii::app()->user->soa_id;
            $_POST['Relation']['oper_name'] = Yii::app()->user->user_title;
            $_POST['Relation']['oper_time'] = date("Y-m-d H:i:s");
            $model->attributes = $_POST['Relation'];
            if ($model->save()) {
                //记录elk日志
                util::elkLog('基础信息', '货主与商家关联信息列表更新', 'info', $_POST['Relation']['seller_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'ok','msg' => '更新成功'), 'Y');
                die(json_encode(array(
                    'status' => 'ok',
                    'msg' => '更新成功'
                )));
                Yii::app()->end();
            }
        }
        $this->render('_form', array(
            'model' => $relationInfo
        ));
    }

    //作废货主
    public function actionDelete()
    {
    	//校验权限
    	util::operatePriContr(40.1, 'json');
        if (! isset($_POST['id'])) {
            die(json_encode(array(
                'status' => 'error',
                'msg' => '非法操作'
            )));
        }
        $delModel = $this->loadModel($_POST['id']);
        $_POST['Relation']['is_valid'] = 0;
        $delModel->attributes = $_POST['Relation'];
        if ($delModel->save(false)) {
            //记录elk日志
            util::elkLog('基础信息', '货主与商家关联信息删除', 'info', $_POST['id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'ok','msg' => '删除成功'), 'Y');
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
                $this->_model = CskSellerCustomeridRelation::model()->findByPk($id);
            }
            if ($this->_model === null)
                throw new CHttpException(404, 'The requested page does not exist.');
        }
        return $this->_model;
    }
}
?>