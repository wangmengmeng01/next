<?php

class VipPushPickController extends Controller
{

    private $_model;

    public function actionIndex()
    {
        $this->render('index');
    }

    public function actionData()
    {
        $param = $_REQUEST;
        $whInfo = VipPushPick::model()->find("customer_id=:customer_id AND platform_code=:platform_code AND rc_addr=:rc_addr AND shop_name=:shop_name AND is_valid=1", array(
            ':customer_id' => trim($param['vendorId']),
            ':platform_code' => trim($param['platformCode']),
            ':rc_addr' => trim($param['sellSite']),
            ':shop_name' => trim($param['shopName'])
        ));
        if($whInfo) {
            $total = 10;
            for($i=1;$i<=10;$i++) {
                $row[] = [
                    'whNo' => '分仓'.$i,
                    'warehouse_code' => $whInfo['wh'.$i],
                    'descr_c' => $this->getDescr_c($whInfo['wh'.$i]),
                    'vendor_id' => trim($param['vendorId']),
                ];
            }
            $response['total'] = $total;
            $response['rows'] = $row;
        } else {
            $response['total'] = 0;
            $response['rows'] = [];
        }

        //组合返回的数据
        echo CJSON::encode($response) ;
		//记录elk日志
		util::elkLog('出库管理', '唯品会JIT拣货单下发', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
        Yii::app()->end();
    }

    public function loadModel($id)
    {
        if ($this->_model === null) {
            if (isset($id)) {
                $this->_model = VipPushPick::model()->findByPk($id);
            }
            if ($this->_model === null)
                throw new CHttpException(404, 'The requested page does not exist.');
        }
        return $this->_model;
    }

    public function getDescr_c($warehouse_code)
    {
        $warehouse = Warehouse::model()->find(
            array(
                'select' => 'descr_c',
                'condition' => 'warehouse_code=:warehouse_code',
                'params' => array(
                    ':warehouse_code'=>$warehouse_code
                )
            )
        );
        return $warehouse['descr_c'];
    }
}