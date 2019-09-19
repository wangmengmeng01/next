<?php
/**
 * 货主与仓库编码绑定
 * @author wp
 *
 */
class DestinationWarehouseBindController extends Controller
{
    private $_model;

    public function actionIndex()
    {
        $this->render('index');
    }

    public function actionData()
    {
        $dataProvider = DestinationWarehouseBind::search($_POST);
        if (!empty($dataProvider)) {
            echo '{"total":' . $dataProvider->getTotalItemCount() . ',"rows":' . CJSON::encode($dataProvider->getData()) . '}';
        }
        //记录elk日志
        util::elkLog('基础信息', '目的地与分仓关联排序表查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
        Yii::app()->end();
    }


    //新增目的地与分仓关联排序
    public function actionAdd()
    {
        //校验权限
        util::operatePriContr(85.1);
        $model = new DestinationWarehouseBind();
        if (isset($_POST['DestinationWarehouseBind'])) {
            //校验货主ID是否存在
            $rsCustomer = Customer::model()->findByPk($_POST['DestinationWarehouseBind']['customer_id']);
            if (empty($rsCustomer)) {
                //记录elk日志
                util::elkLog('基础信息', '目的地与分仓关联排序表新增', 'error', $_POST['DestinationWarehouseBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'货主ID不存在,保存失败'), 'N');
                die(json_encode(array(
                    'status' => 'error',
                    'msg' => '货主ID不存在,保存失败'
                )));
            } elseif ($rsCustomer['customer_id'] != $_POST['DestinationWarehouseBind']['customer_id']) {
                //记录elk日志
                util::elkLog('基础信息', '目的地与分仓关联排序表新增', 'error', $_POST['DestinationWarehouseBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'货主ID填写不规范，请注意字母的大小写,保存失败'), 'N');
                die(json_encode(array(
                    'status' => 'error',
                    'msg' => '货主ID填写不规范，请注意字母的大小写,保存失败'
                )));
            }


            //校验数据是否已经存在
            $rs = $model->find("customer_id=:customer_id AND platform_code=:platform_code AND rc_addr=:rc_addr AND shop_name=:shop_name AND is_valid=1", array(
                ':customer_id' => $_POST['DestinationWarehouseBind']['customer_id'],
                ':platform_code' => $_POST['DestinationWarehouseBind']['platform_code'],
                ':rc_addr' => $_POST['DestinationWarehouseBind']['rc_addr'],
                ':shop_name' => $_POST['DestinationWarehouseBind']['shop_name']
            ));
            if (!empty($rs)) {
                //记录elk日志
                util::elkLog('基础信息', '目的地与分仓关联排序表新增', 'error', $_POST['DestinationWarehouseBind']['rc_addr'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'货主：' . $_POST['DestinationWarehouseBind']['customer_id'] . '与收货地址：' . $_POST['DestinationWarehouseBind']['rc_addr'] .'和平台：'. $_POST['DestinationWarehouseBind']['platform_code'] .'以及店铺：'. $_POST['DestinationWarehouseBind']['shop_name']. '的绑定关系已经存在,保存失败'), 'N');
                die(json_encode(array(
                    'status' => 'error',
                    'msg' => '货主：' . $_POST['DestinationWarehouseBind']['customer_id'] . '与收货地址：' . $_POST['DestinationWarehouseBind']['rc_addr'] .'和平台：'. $_POST['DestinationWarehouseBind']['platform_code'].'以及店铺：'. $_POST['DestinationWarehouseBind']['shop_name'] . '的绑定关系已经存在,保存失败'
                )));
            }

            $_POST['DestinationWarehouseBind']['operator_id'] = Yii::app()->user->soa_id;
            $_POST['DestinationWarehouseBind']['operator_name'] = Yii::app()->user->user_title;
            $_POST['DestinationWarehouseBind']['create_time'] = date("Y-m-d H:i:s");
            $model->attributes = $_POST['DestinationWarehouseBind'];
            if ($model->save()) {
                //记录elk日志
                util::elkLog('基础信息', '目的地与分仓关联排序表新增', 'info', $_POST['DestinationWarehouseBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'保存成功'), 'Y');
                die(json_encode(array(
                    'status' => 'ok',
                    'msg' => '保存成功'
                )));
                Yii::app()->end();
            } else {
                //记录elk日志
                util::elkLog('基础信息', '目的地与分仓关联排序表新增', 'error', $_POST['DestinationWarehouseBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'错误操作，请找IT部'), 'N');
                die(json_encode(array(
                    'status' => 'error',
                    'msg' => '错误操作，请找IT部'
                )));
            }
        }

        $this->renderPartial('_form', array(
            'model' => $model,
            'warehouse' => json_encode($this->getWarehouseArr())
        ));
    }

    //修改目的地与分仓关联排序
    public function actionUpdate($id)
    {
        //校验权限
        util::operatePriContr(85.1);
        $model = DestinationWarehouseBind::model();
        $DestinationWarehouseBindInfo = $model->findByPk($id);
        if (isset($_POST['DestinationWarehouseBind'])) {

            //校验数据是否已经存在
            $rs = $model->find("customer_id=:customer_id AND platform_code=:platform_code AND rc_addr=:rc_addr AND shop_name=:shop_name AND id!=$id AND is_valid=1", array(
                ':customer_id' => $_POST['DestinationWarehouseBind']['customer_id'],
                ':platform_code' => $_POST['DestinationWarehouseBind']['platform_code'],
                ':rc_addr' => $_POST['DestinationWarehouseBind']['rc_addr'],
                ':shop_name' => $_POST['DestinationWarehouseBind']['shop_name']
            ));
            if (!empty($rs)) {
                //记录elk日志
                util::elkLog('基础信息', '目的地与分仓关联排序表修改', 'error', $_POST['DestinationWarehouseBind']['rc_addr'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'货主：' . $_POST['DestinationWarehouseBind']['customer_id'] .'与收货地址：' . $_POST['DestinationWarehouseBind']['rc_addr'] .'和平台：'. $_POST['DestinationWarehouseBind']['platform_code'].'以及店铺：'. $_POST['DestinationWarehouseBind']['shop_name'] . '的绑定关系已经存在,保存失败'), 'N');
                die(json_encode(array(
                    'status' => 'error',
                    'msg' => '货主：' . $_POST['DestinationWarehouseBind']['customer_id'] . '与收货地址：' . $_POST['DestinationWarehouseBind']['rc_addr'] .'和平台：'. $_POST['DestinationWarehouseBind']['platform_code'].'以及店铺：'. $_POST['DestinationWarehouseBind']['shop_name'] . '的绑定关系已经存在,保存失败'
                )));
            }

            $_POST['DestinationWarehouseBind']['operator_id'] = Yii::app()->user->soa_id;
            $_POST['DestinationWarehouseBind']['operator_name'] = Yii::app()->user->user_title;
            $_POST['DestinationWarehouseBind']['update_time'] = date("Y-m-d H:i:s");
            $DestinationWarehouseBindInfo->attributes = $_POST['DestinationWarehouseBind'];
            if ($DestinationWarehouseBindInfo->save()) {
                //记录elk日志
                util::elkLog('基础信息', '目的地与分仓关联排序表修改', 'info', $_POST['DestinationWarehouseBind']['rc_addr'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'更新成功'), 'Y');
                die(json_encode(array(
                    'status' => 'ok',
                    'msg' => '更新成功'
                )));
                Yii::app()->end();
            }else{
                //记录elk日志
                util::elkLog('基础信息', '目的地与分仓关联排序表修改', 'error', $_POST['DestinationWarehouseBind']['rc_addr'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'错误操作，请找IT部'), 'N');
                die(json_encode(array(
                    'status' => 'error',
                    'msg' => '错误操作，请找IT部'
                )));
            }
        }

        $this->render('_form', array(
            'model' => $DestinationWarehouseBindInfo,
            'warehouse' => json_encode($this->getWarehouseArr())
        ));
    }

    //作废目的地与分仓关联排序
    public function actionDelete()
    {
        //校验权限
        util::operatePriContr(85.1, 'json');
        if (! isset($_POST['id'])) {
            //记录elk日志
            util::elkLog('基础信息', '目的地与分仓关联排序表删除', 'error', $_POST['id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'非法操作'), 'N');
            die(json_encode(array(
                'status' => 'error',
                'msg' => '非法操作'
            )));
        }
        $delModel = $this->loadModel($_POST['id']);
        $_POST['DestinationWarehouseBind']['is_valid'] = 0;
        $delModel->attributes = $_POST['DestinationWarehouseBind'];
        if ($delModel->save(false)) {
            //记录elk日志
            util::elkLog('基础信息', '目的地与分仓关联排序表删除', 'info', $_POST['id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'删除成功'), 'Y');
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
                $this->_model = DestinationWarehouseBind::model()->findByPk($id);
            }
            if ($this->_model === null)
                throw new CHttpException(404, 'The requested page does not exist.');
        }
        return $this->_model;
    }

    public function getWarehouseArr()
    {
        $warehouse = Warehouse::model()->findAll(
            array(
                'select'=>'warehouse_code,descr_c',
                'condition'=>'is_valid=1'
            )
        );
        $whArr[] = array('descr_c'=>'--','warehouse_code'=>'');
        foreach ($warehouse as $wh) {
            $whArr[] = $wh->attributes;
        }
        return $whArr;
    }
}