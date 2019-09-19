<?php

class WarehouseShipmentsController extends Controller
{

private $_model;

    public function actionIndex()
    {
        $this->render('index');
    }

    public function actionData()
    {
    	$dataStr = WarehouseShipments::search($_REQUEST);
    	echo $dataStr;
		//记录elk日志
		util::elkLog('出库管理', '仓库数据报表查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
    }

    public function loadModel($id)
    {
        if ($this->_model === null) {
            if (isset($id)) {
                $this->_model = WarehouseShipments::model()->findByPk($id);
            }
            if ($this->_model === null)
                throw new CHttpException(404, 'The requested page does not exist.');
        }
        return $this->_model;
    }
}