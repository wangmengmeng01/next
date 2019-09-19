<?php

class OutboundRecordPackageController extends Controller
{

    private $_model;

    public function actionIndex()
    {
        $this->render('index');
    }

    public function actionData()
    {
        $dataProvider = OutboundRecordPackage::search($_REQUEST);
        if (!empty($dataProvider)) {
            echo '{"total":' . $dataProvider->getTotalItemCount() . ',"rows":' . CJSON::encode($dataProvider->getData()) . '}';
        }
        Yii::app()->end();
    }

    public function loadModel($id)
    {
        if ($this->_model === null) {
            if (isset($id)) {
                $this->_model = OutboundRecordPackage::model()->findByPk($id);
            }
            if ($this->_model === null)
                throw new CHttpException(404, 'The requested page does not exist.');
        }
        return $this->_model;
    }
}