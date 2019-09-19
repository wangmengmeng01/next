<?php

class BaseController extends Controller
{

    /*
     * 功能：通过省编码，获取市级数据
     */
    public function actionGetCity()
    {
        $upModel = array();
        if ($_POST['ProvinceID']) {
            if (isset($_POST['type']) && $_POST['type'] == 'array') {
                $provinceID = $_POST['ProvinceID'];
                $connection = Yii::app()->db;
                $datas = $connection->createCommand("SELECT CityID,CityName FROM ydserver.`city` WHERE provinceid =:provinceid");
                // 替换占位符 ":id"
                $datas->bindParam(":provinceid", $provinceID);
                $upModel = $datas->queryAll();
                array_push($upModel, array(
                    'CityID' => 0,
                    'CityName' => '请选择城市',
                    'selected' => true
                ));
                die(json_encode(array(
                    'data' => $upModel,
                    'status' => 'ok'
                )));
            } else {
                $connection = Yii::app()->db;
                $datas = $connection->createCommand("SELECT CityID,CityName FROM ydserver.`city` WHERE provinceid =:provinceid ");
                // 替换占位符 ":id"
                $datas->bindParam(":provinceid", $provinceID);
                $upModel = $datas->queryAll();
                echo CHtml::tag('option', array(
                    'value' => ''
                ), CHtml::encode('请选择城市'), true);
                foreach ($upModel as $v) {
                    echo CHtml::tag('option', array(
                        'value' => $v['CityID']
                    ), CHtml::encode($v['CityName']), true);
                }
            }
        } else {
            echo CHtml::tag('option', array(
                'value' => ''
            ), CHtml::encode('请选择城市'), true);
        }
    }

    /*
     * 功能：通过市编码，获取区县级数据
     */
    public function actionGetCounty()
    {
        $upModel = array();
        if ($_POST['CityID']) {
            if (isset($_POST['type']) && $_POST['type'] == 'array') {
                $cityID = $_POST['CityID'];
                $connection = Yii::app()->db;
                $datas = $connection->createCommand("SELECT CountyID,CountyName FROM ydserver.`county` WHERE cityid =:cityid");
                // 替换占位符 ":id"
                $datas->bindParam(":cityid", $cityID);
                $upModel = $datas->queryAll();
                array_push($upModel, array(
                    'CountyID' => 0,
                    'CountyName' => '请选择县区',
                    'selected' => true
                ));
                die(json_encode(array(
                    'data' => $upModel,
                    'status' => 'ok'
                )));
            } else {
                $connection = Yii::app()->db;
                $datas = $connection->createCommand("SELECT CityID,CityName FROM ydserver.`city` WHERE provinceid =: provinceid ");
                // 替换占位符 ":id"
                $datas->bindParam(":provinceid", $provinceID);
                $upModel = $datas->queryAll();
                echo CHtml::tag('option', array(
                    'value' => ''
                ), CHtml::encode('请选择县区'), true);
                foreach ($upModel as $v) {
                    echo CHtml::tag('option', array(
                        'value' => $v['CityID']
                    ), CHtml::encode($v['CityName']), true);
                }
            }
        } else {
            echo CHtml::tag('option', array(
                'value' => ''
            ), CHtml::encode('请选择县区'), true);
        }
    }
}