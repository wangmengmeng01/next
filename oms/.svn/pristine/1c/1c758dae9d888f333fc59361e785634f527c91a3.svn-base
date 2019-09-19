<?php

/**
 * 唯品会JIT拣货单列表model
 * table: t_vip_pick
 */
class VipJitPickList extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *            active record class name.
     * @return StoreProcessCreate the static model class
     */
    public static function model($className = __CLASS__)
    {
        return parent::model($className);
    }

    /**
     *
     * @return string the associated database table name
     */
    public function tableName()
    {
        return '{{vip_pick_list}}';
    }

    public function relations()
    {
        // NOTE: you may need to adjust the relation name and the related
        // class name for the relations automatically generated below.
        return array(
            'vip_delivery_info'=>array(
                self::HAS_ONE,
                'VipDeliveryInfo',
                ['po_no' => 'po_no','vendor_id'=>'vendor_id','sell_site'=>'sell_site'],
                'alias' => 'd',
                'select'=>'delivery_no,arrival_time,carrier_name,storage_no,confirm_time'
            ),
            'vip_po_list'=>array(
                self::HAS_ONE,
                'VipJitPoList',
                ['id' => 'po_id'],
                'alias' => 'po',
                'select'=>'brand_name'
            ),
        );
    }

    /**
     * Retrieves a list of models based on the current search/filter conditions.
     * 原生sql联表查询
     * @return CActiveDataProvider the data provider that can return the models based on the search/filter conditions.
     */
    public static function search($param)
    {
        //校验是否有查看所有货主的权限
//        $viewFlag = util::hasViewAll();
//        if ($viewFlag == 0) {
//            $existsArr = util::getCustomer();
//            if (!empty($existsArr)) {
//                $where = "t.customer_id in($existsArr)";
//            } else {
//                return ;
//            }
//        }
        //组合查询条件
        $select = 'SELECT p.id,p.po_no,p.pick_no,p.co_mode,p.sell_site,p.order_cate,p.pick_num,p.create_time,p.first_export_time,p.export_num,p.store_sn,p.delivery_num,p.status,p.vendor_id,p.warehouse,p.send_time,po.brand_name,d.delivery_no,d.arrival_time,d.carrier_name,d.storage_no,d.confirm_time ';
        $selectNum = ' SELECT p.pick_no';
        $from = ' FROM t_vip_pick_list as p 
                  LEFT JOIN t_vip_delivery_info as d ON d.po_no=p.po_no AND d.vendor_id=p.vendor_id AND d.sell_site=p.sell_site 
                  LEFT JOIN t_vip_po_list as po on po.id=p.po_id ';

        $where = " WHERE 1=1";
        if (!empty($param['VipJitPickList']['po_no'])) {
            $poNo = trim($param['VipJitPickList']['po_no']);
            $where .= " AND p.po_no='{$poNo}'";
        }
        if (!empty($param['VipJitPickList']['pick_no'])) {
            $pickNo = trim($param['VipJitPickList']['pick_no']);
            $where .= " AND p.pick_no='{$pickNo}'";
        }
        if (!empty($param['VipJitPickList']['warehouse_code'])) {
            $warehouseCode = trim($param['VipJitPickList']['warehouse_code']);
            $where .= " AND p.warehouse='{$warehouseCode}'";
        }
        if (!empty($param['VipJitPickList']['delivery_no'])) {
            $deliveryNo = trim($param['VipJitPickList']['delivery_no']);
            $where .= " AND d.delivery_no='{$deliveryNo}'";
        }
        if (!empty($param['VipJitPickList']['storage_no'])) {
            $storageNo = trim($param['VipJitPickList']['storage_no']);
            $where .= " AND d.storage_no='{$storageNo}'";
        }
        if (!empty($param['VipJitPickList']['status'])) {
            $status = trim($param['VipJitPickList']['status']);
            $where .= " AND p.status='{$status}'";
        }
        if (!empty($param['VipJitPickList']['vendor_id'])) {
            $vendorId = trim($param['VipJitPickList']['vendor_id']);
            $where .= " AND p.vendor_id='{$vendorId}'";
        }
        if (!empty($param['VipJitPickList']['brand_name'])) {
            $brandName = trim($param['VipJitPickList']['brand_name']);
            $where .= " AND po.brand_name='{$brandName}'";
        }
        if (!empty($param['VipJitPickList']['sell_site'])) {
            $sellSite = trim($param['VipJitPickList']['sell_site']);
            $where .= " AND p.sell_site='{$sellSite}'";
        }


        $groupBy = '';
        $order = ' ORDER BY p.id desc';
        $page = empty($param['page']) ? '1' : $param['page'];
        $pageSize = empty($param['rows']) ? '20' : $param['rows'];
        $offset = ($page - 1) * $pageSize;
        $limit = " LIMIT $offset, $pageSize";

        //excel导出时limit参数做特殊处理
        if (isset($param['VipJitPickList']['operate_type']) && $param['VipJitPickList']['operate_type'] == 'excel_export') {
            $limit = '';
        }



        //连接数据库
        $db = Yii::app()->db;
        //获取总记录条数
        $sqlNum = 'SELECT COUNT(*) AS num FROM (' . $selectNum . $from . $where . $groupBy . ') a';
        $model = $db->createCommand($sqlNum);
        $rs = $model->queryRow();
        if (empty($rs['num'])) {
            $total = 0;
        } else {
            $total = $rs['num'];
        }

        //总页数
//        $totalPage = ceil($total/$pageSize);

        //获取每页数据
        $sql = $select . $from . $where . $groupBy . $order . $limit;
        $model = $db->createCommand($sql);
        $row = $model->queryAll();
        //组合返回的数据
        return '{"total":' . $total . ',"rows":' . CJSON::encode($row) . '}';
    }

    /**
     * @param $param
     * @return string
     * CActiveDataProvider 联表查询
     */
    public static function search_c($param)
    {
        $criteria = new CDbCriteria();

        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '20' : $param['rows'];
        $criteria->with = array(
            'vip_delivery_info',
            'vip_po_list'
        );

        //excel导出时pagination参数做特殊处理
        if (isset($param['VipJitPickList']['operate_type']) && $param['VipJitPickList']['operate_type'] == 'excel_export') {
            $pagination = false;
        } else {
            $pagination = array(
                'pageSize' => $param['rows'],
                'currentPage' => $param['page'] - 1
            );
        }
        $dataProvider = new CActiveDataProvider('VipJitPickList', array(
            'sort' => array(
                'defaultOrder' => 't.id desc'
            ),
            'pagination' => $pagination,
            'criteria' => $criteria,
        ));
//        print_r($dataProvider->getData()[0]->vip_delivery_info->attributes);die;
        $data = $dataProvider->getData();
        $mergeData = self::model()->mergeData($data,$criteria->with);
        return '{"total":' . $dataProvider->getTotalItemCount() . ',"rows":' . CJSON::encode($mergeData) . '}';
    }
    
    /**
     * 唯品会JIT拣货单列表导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
			'po编号' => 'po_no',
			'拣货单编号' => 'pick_no',
			'合作模式' => 'co_mode',
			'唯品会仓库' => 'sell_site',
			'订单类别' => 'order_cate',
			'拣货数量' => 'pick_num',
			'拣货单创建时间' => 'create_time',
			'首次导出时间' => 'first_export_time',
			'导出次数' => 'export_num',
			'门店编码' => 'store_sn',
			'发货数' => 'delivery_num',
			'运单号' => 'delivery_no',
			'要求到货时间' => 'arrival_time',
			'承运商' => 'carrier_name',
			'入库单号' => 'storage_no',
			'发货时间' => 'confirm_time',
			'状态' => 'status',
			'供应商' => 'vendor_id',
			'拣货仓' => 'warehouse',
			'下发时间' => 'send_time',
    	);
    	return $columns;
    }

    /**
     * @param $data
     * @param $with
     * @return array
     * 联表查询，组合主表及关联表的字段值
     */
    public function mergeData($data,$with)
    {
        $mergeData = array();
        foreach ($data as $k=>$item) {
            $tmp = array();
            foreach ($with as $relation) {
                $relation_select = explode(',',self::model()->relations()[$relation]['select']);
                if(!empty($item->$relation->attributes)) {
                    foreach ($item->$relation->attributes as $key=>$value) {
                        if(in_array($key,$relation_select)) {
                            $tmp[$key] = $value;
                        }
                    }
                } else {
                    foreach ($relation_select as $select) {
                        $tmp[$select] = null;
                    }
                }
            }
            $mergeData[] = array_merge($data[$k]->attributes,$tmp);
        }
        return $mergeData;
    }

    /***
     * Notes: 获取指定拣货仓信息
     * Date: 2019/3/8
     * Time: 17:14
     * @param $vendor_id 供应商id
     * @param $pick_no  拣货单编号
     * @param $warehouse  拣货仓
     * @return mixed array
     */
    public static function checkOne($vendor_id,$pick_no,$warehouse)
    {
        $where = " vendor_id = '{$vendor_id}' and pick_no = '{$pick_no}' and warehouse = '{$warehouse}' limit 1";
        $sql = "select * from t_vip_pick_list where {$where}";
        $model = Yii::app()->db->createCommand($sql);
        $rs = $model->queryRow();
        return $rs;
    }

}
