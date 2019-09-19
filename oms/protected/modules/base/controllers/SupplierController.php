<?php

class SupplierController extends Controller
{

	public function actionIndex()
	{
		$this->render('index');
	}
	
	public function actionData() {
		$dataProvider = Supplier::search($_POST);
		echo '{"total":'.$dataProvider->getTotalItemCount().',"rows":'.CJSON::encode($dataProvider->getData()).'}';
		//记录elk日志
		util::elkLog('基础信息', '供应商列表查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
		Yii::app()->end();
	}
		
}