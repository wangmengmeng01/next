<?php
/**
 * Controller is the customized base controller class.
 * All controller classes for this application should extend from this base class.
 */
class Controller extends CController
{
	public $layouts = '//layouts/main';
	/**
	控制器启动权限控制
	*/
	public function filters()
    {
        return array(
            'accessControl',
        );
    }

	/**
	设定访问规则
	*/
    public function accessRules()
    {
        return array(
            array('allow',
                'users'=>array('@'),
            ),
            array('deny',
                'users'=>array('*'),
            ),
        );
    }

}