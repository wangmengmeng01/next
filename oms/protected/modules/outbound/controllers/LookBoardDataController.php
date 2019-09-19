<?php
/**
 * Notes: 看板数据
 * User: passerby
 * Date: 2019/9/12
 * Time: 9:45
 */


class LookBoardDataController extends Controller
{
    private $_model;

    public function actionIndex()
    {
        $this->render('index');
    }

}