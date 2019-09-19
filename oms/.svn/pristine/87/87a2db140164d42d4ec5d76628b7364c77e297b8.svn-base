<?php

/**
 * Notes:接口相关配置
 * Date: 2019/6/13
 * Time: 9:59
 */
class api_config
{
    public function getApiConfig($apiName)
    {
        $data = array();
        switch ($apiName) {
            case 'beibei':
                $data = array(
                    # 入库单创建
                    'beibei.outer.entryorder.create' => array('class' => 'entryOrderCreate', 'fct' => 'create', 'to' => 'wms'),
                    # 入库单回传
                    'beibei.outer.entryorder.confirm' => array('class' => 'entryOrderConfirm', 'fct' => 'confirm', 'to' => 'erp'),
                    # 发货单创建
                    'beibei.outer.deliveryorder.create' => array('class' => 'deliveryOrderCreate', 'fct' => 'create', 'to' => 'wms'),
                    # 是否允许发货
                    'beibei.outer.so.deliver' => array('class' => 'soDeliver', 'fct' => 'judge', 'to' => 'erp'),
                    # 查询第三方仓库库存
                    'beibei.outer.inventory.query' => array('class' => 'inventoryQuery', 'fct' => 'search', 'to' => 'wms'),
                    # 销售退货下传
                    'beibei.outer.rma.create' => array('class' => 'rmaCreate', 'fct' => 'create', 'to' => 'wms'),
                    # 商品同步
                    'beibei.outer.product.sync' => ['class' => 'ProductSync', 'fct' => 'create', 'to' => 'wms'],
                    # 出库单创建
                    'beibei.outer.stockout.create' => ['class' => 'StockoutCreate', 'fct' => 'create', 'to' => 'wms'],
                    # 出库单回传
                    'beibei.outer.stockout.confirm' => ['class' => 'StockoutConfirm', 'fct' => 'confirm', 'to' => 'erp'],
                    # 单据取消
                    'beibei.outer.bill.cancel' => ['class' => 'BillCancel', 'fct' => 'cancel', 'to' => 'wms'],
                    # 库存异动
                    'beibei.outer.stockchange.report' => ['class' => 'StockChangeReport', 'fct' => 'report', 'to' => 'erp'],
                    # 入库单查询
                    'beibei.outer.entryorder.query' => ['class' => 'EntryorderQuery', 'fct' => 'query', 'to' => 'wms'],
                    # 入库单更新
                    'beibei.outer.entryorder.update' => ['class' => 'EntryorderUpdate', 'fct' => 'update', 'to' => 'wms'],
                    # 发货单查询
                    'beibei.outer.deliveryorder.query' => ['class' => 'DeliveryorderQuery', 'fct' => 'query', 'to' => 'wms'],
                    # 分页查询第三方库存
                    'beibei.outer.inventory.pagequery' => ['class' => 'InventoryPagequery', 'fct' => 'query', 'to' => 'wms'],
                );
                break;
        }

        return $data;
    }
}