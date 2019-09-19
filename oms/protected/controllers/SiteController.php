<?php
class SiteController extends CController
{
	
	public function actionVcode()
	{
		Vcode::getVcode()->renderImage();
	}
	
	public function actionError()
	{
		$error=Yii::app()->errorHandler->error;
		if($error)
		{
		  $this->render('error', $error);
		}
	}

    /**
     * 功能：检查菜单权限
     *
     */
    public function actionCheckPri($pos)
    {
         //获取登陆者所有的权限
         $allPriArr = util::getUserAllPriArr();
         if (in_array($pos, $allPriArr)) {
         	return true;
         } else {
         	die('<h3>无此权限，请让IT部门开通权限</h3>');
         } 
    }
    
	/**
	 * 登录
	 */
	public function actionLogin(){
		if($_SERVER['REQUEST_METHOD']=='POST'){
			$result =  Ry::model()->login();
			echo json_encode($result);
			Yii::app()->end();
		}
		$this->render('login');
	}

	/**
	 * 退出
	 */
	public function actionLogout(){
	    //记录elk日志
	    util::elkLog('退出系统', '', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), '', array(), 'Y');
		Yii::app()->user->logout();
		$this->redirect(Yii::app()->user->loginUrl);
	}
}