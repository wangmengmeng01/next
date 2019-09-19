<?php

class StockChangeReportController extends Controller
{

    private $_model;

    public function actionIndex()
    {
        $this->render('index');
    }

    public function actionData()
    {
        $dataProvider = StockChangeReport::search($_REQUEST);
        if (!empty($dataProvider)) {
            echo '{"total":' . $dataProvider->getTotalItemCount() . ',"rows":' . CJSON::encode($dataProvider->getData()) . '}';
        }
		//记录elk日志
		util::elkLog('库存管理', '库存异动通知查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
        Yii::app()->end();
    }

    public function loadModel($id)
    {
        if ($this->_model === null) {
            if (isset($id)) {
                $this->_model = StockChangeReport::model()->findByPk($id);
            }
            if ($this->_model === null)
                throw new CHttpException(404, 'The requested page does not exist.');
        }
        return $this->_model;
    }
}