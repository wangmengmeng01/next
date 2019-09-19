<?php
/**
 * 库存汇总可用量查询Controller
 */
class InventorySumAvailableController extends Controller
{
	private $_model;
	
	public function actionIndex()
	{
		$this->render('index');
	}
	
	// public function actionData()
	// {
	// 	$dataProvider = InventorySumAvailable::search($_POST);
	// 	print_r(CJSON::encode($dataProvider->getData()));die;
	// 	echo '{"total":' . $dataProvider->getTotalItemCount() . ',"rows":' . CJSON::encode($dataProvider->getData()) . '}';
	// 	Yii::app()->end();
	// }
	  

	public function actionData()
	{
	    //校验权限
	    util::operatePriContr(43, 'json');
	    echo InventorySumAvailable::search($_POST,1);
		//记录elk日志
		util::elkLog('库存管理', '实时库存查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
	}
	
	public function loadModel($id)
	{
		if ($this->_model === null) {
            if (isset($id)) {
                $this->_model = InventorySumAvailable::model()->findByPk($id);
            }
            if ($this->_model === null)
                throw new CHttpException(404, 'The requested page does not exist.');
        }
        return $this->_model;
	}
	
}