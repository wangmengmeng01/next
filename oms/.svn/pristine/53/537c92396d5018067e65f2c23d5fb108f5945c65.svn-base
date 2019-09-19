<style type="text/css">
#logo {
	background: url(./static/image/logo.png) no-repeat;
	width: 95px;
	height: 47px;
	float: left;
}

#sysInfo {
	float: left;
	padding: 2px;
	margin-left: 45px;
}

#sysTitle {
	font-size: 24px;
	font-weight: bold;
	color: #0099FF;
	text-align: center;
}

#menu1 {
	float: left;
	margin-top: 10px;
	margin-left: 17px;
}

#menu1 li {
	float: left;
	list-style: none;
	margin: 1px 8px;
	cursor: pointer;
}

#menu1 li:hover {
	color: #0099FF;
}

.menuSelected {
	color: red;
	font-weight: bold;
}

.selected {
	color: #333;
	font-weight: bold;
	background: #95B8E7;
}

#menu2 {
	padding-top: 5px;
	padding-bottom: 25px;
	background-color: #E0ECFF;
}

.menuItem {
	display: none;
}

.menuItem div {
	padding: 6px 0px;
	/* width:102px; */
	margin-top: 8px;
	padding-left:8px;
	cursor: pointer;
	text-align: left;
}

.menuItem div:hover {
	color: #0099FF;
}
</style>
<div class="easyui-layout" style="width: 100%;" data-options="fit:true">
	<div data-options="region:'north',border:false"
		style="height: 70px; padding: 0px, 10px;" id="header">
		<div id="logo"></div>
		<div id="sysInfo">
			<div id="sysTitle">OMS管理系统</div>
			<div style="margin-top: 2px;">您好，<?php echo Yii::app()->user->user_title?>（<?php echo Yii::app()->user->name;?>）,欢迎您!</div>
		</div>
		<div id="menu1">
			<ul>
				<li onclick="showMenu(this,'m1')" class="menuSelected">基础信息</li>
				<li onclick="showMenu(this,'m3')">入库管理</li>
				<li onclick="showMenu(this,'m4')">出库管理</li>
				<li onclick="showMenu(this,'m5')">库存管理</li>
				<li onclick="showMenu(this,'m6')">日志管理</li>
				<li onclick="showMenu(this,'m9')">应用管理</li>
				<li
					onclick="window.location.href='<?php echo Yii::app()->createUrl('site/Logout'); ?>'">退出系统</li>
			</ul>
		</div>
	</div>
	<div data-options="region:'south'"
		style="height: 50px; overflow: hidden; background: #999;">
		<div id="footer"
			style="width: 100%; height: 100%; vertical-align: middle;">版权所有：上海韵达货运有限公司&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;技术支持：上海东普信息科技有限公司</div>
	</div>
	<div data-options="region:'west',split:true,title:'功能列表'"
		style="width: 150px;">
		<div id="menu2">
			<div class="menuItem" id="m1" style="display: block">
                <!--
				<div class="item"
					href="<?php echo Yii::app()->createUrl('base/erp/index') ?>">ERP软件维护</div>
				<div class="item"
					href="<?php echo Yii::app()->createUrl('base/wms/index') ?>">WMS软件维护</div>
                -->
				<div class="item"
					href="<?php echo Yii::app()->createUrl('base/customer/index'); ?>">货主维护</div>
				<div class="item"
					href="<?php echo Yii::app()->createUrl('base/qimenCustomer/index'); ?>">奇门货主配置维护</div>
				<div class="item"
					href="<?php echo Yii::app()->createUrl('base/qimenCustomerBind/index'); ?>">奇门货主绑定维护</div>
				<div class="item"
					href="<?php echo Yii::app()->createUrl('base/warehouse/index'); ?>">仓库维护</div>		
				<div class="item"
					href="<?php echo Yii::app()->createUrl('base/customerWarehouseBind/index') ?>">货主与仓库关系维护</div>
                <div class="item"
                     href="<?php echo Yii::app()->createUrl('base/DestinationWarehouseBind/index') ?>">目的地与分仓关联排序表</div>
                <!--
				<div class="item"
					href="<?php echo Yii::app()->createUrl('base/CustomerBind/index') ?>">货主与ERP和WMS维护</div>
				-->
				<div class="item"
					href="<?php echo Yii::app()->createUrl('base/qimenCnPlatformRelation/index') ?>">奇门-菜鸟电商平台对应关系维护</div>				
				<div class="item"
					href="<?php echo Yii::app()->createUrl('base/logistics/index') ?>">物流公司维护</div>
                <!--
				<div class="item"
					href="<?php echo Yii::app()->createUrl('base/supplier/index'); ?>">供应商列表</div>				
				<div class="item"
					href="<?php echo Yii::app()->createUrl('base/shop/index'); ?>">店铺列表</div>
				-->
				<div class="item"
					href="<?php echo Yii::app()->createUrl('base/product/index'); ?>">商品列表</div>
                <!--
				<div class="item"
				    href="<?php echo Yii::app()->createUrl('base/combineProduct/index'); ?>">组合商品列表</div>
				<div class="item"
					href="<?php echo Yii::app()->createUrl('base/CustomerSendLog/index') ?>">客商档案推送列表</div>
				-->
				<span><hr/></span>	
				<div class="item"
					href="<?php echo Yii::app()->createUrl('base/cskSellerAccessToken/index') ?>">商家授权列表</div>
                <div class="item"
					href="<?php echo Yii::app()->createUrl('base/cskPddAccessToken/index') ?>">拼多多授权列表</div>
				<div class="item"
					href="<?php echo Yii::app()->createUrl('base/cskSellerWaybillInfo/index') ?>">商家开通电子面单服务信息列表</div>
				<div class="item"
					href="<?php echo Yii::app()->createUrl('base/cskSellerCustomeridRelation/index') ?>">货主与商家关联信息列表</div>
                <div class="item"
                     href="<?php echo Yii::app()->createUrl('base/cskMerchantDeploy/index') ?>">承运商配置</div>
                <!--
				<div class="item"
				    href="<?php echo Yii::app()->createUrl('base/cskShipAddress/index') ?>">发货地址列表</div>
				-->
			</div>
			<div class="menuItem" id="m3">
				<div class="item"
					href="<?php echo Yii::app()->createUrl('inbound/putASNData/index'); ?>">入库单下发报表</div>
				<div class="item"
					href="<?php echo Yii::app()->createUrl('inbound/confirmASNData/index'); ?>">入库单状态明细回传报表</div>
				<div class="item"
					href="<?php echo Yii::app()->createUrl('inbound/cancelASNData/index'); ?>">入库单取消报表</div>
                <!--
				<div class="item" href="<?php echo Yii::app()->createUrl('inbound/storeProcessCreate/index'); ?>">仓内加工单报表</div>
			    <div class="item" href="<?php echo Yii::app()->createUrl('inbound/storeProcessConfirm/index'); ?>">仓内加工单确认报表</div>
			    <div class="item" href="<?php echo Yii::app()->createUrl('inbound/storeProcessCancel/index'); ?>">仓内加工单取消报表</div>
			    -->
			</div>
			<div class="menuItem" id="m4">
                <div class="item"
                     href="<?php echo Yii::app()->createUrl('outbound/lookBoardData/index'); ?>">看板数据</div>
				<div class="item"
					href="<?php echo Yii::app()->createUrl('outbound/putSOData/index'); ?>">出库单下发报表</div>
				<div class="item"
					href="<?php echo Yii::app()->createUrl('outbound/confirmSOData/index'); ?>">出库单明细回传报表</div>
				<div class="item"   
					href="<?php echo Yii::app()->createUrl('outbound/confirmSOStatus/index'); ?>">出库单状态回传报表</div>    
				<div class="item"
					href="<?php echo Yii::app()->createUrl('outbound/cancelSOData/index'); ?>">出库单取消报表</div>
				<div class="item"
					href="<?php echo Yii::app()->createUrl('outbound/putSOEcpt/index'); ?>">出库单异常报表</div>
				<div class="item"
					href="<?php echo Yii::app()->createUrl('outbound/deliveryOrderCreate/index'); ?>">发货单下发报表</div>
				<div class="item"
					href="<?php echo Yii::app()->createUrl('outbound/deliveryOrderRecord/index'); ?>">发货单回传报表</div>
				<div class="item"
					href="<?php echo Yii::app()->createUrl('outbound/deliveryOrderCancel/index'); ?>">发货单取消报表</div>
                <!--
				<div class="item"
					href="<?php echo Yii::app()->createUrl('outbound/deliverySnReport/index'); ?>">发货单SN通知报表</div>
				-->
				<div class="item"
					href="<?php echo Yii::app()->createUrl('outbound/deliveryItemLackReport/index'); ?>">发货单缺货通知报表</div>
				<div class="item"
					href="<?php echo Yii::app()->createUrl('outbound/deliveryOrderShortage/index'); ?>">发货单缺货转仓报表</div>
				<div class="item"
					href="<?php echo Yii::app()->createUrl('outbound/customerShipments/index'); ?>">货主仓库发货量报表</div>
				<div class="item"
					href="<?php echo Yii::app()->createUrl('outbound/warehouseShipments/index'); ?>">仓库数据报表</div>
				<div class="item" href="<?php echo Yii::app()->createUrl('outbound/orderProcessReport/index'); ?>">订单流水通知报表</div>
                <div class="item" href="<?php echo Yii::app()->createUrl('outbound/vipJitPoList/index'); ?>">唯品会JIT采购单列表</div>
                <div class="item" href="<?php echo Yii::app()->createUrl('outbound/vipJitPickList/index'); ?>">唯品会JIT拣货单列表</div>
			</div>
			
			<div class="menuItem" id="m5">   
				<div class="item" href="<?php echo Yii::app()->createUrl('inventory/queryINVData/index'); ?>">库存查询</div>
				<div class="item" href="<?php echo Yii::app()->createUrl('inventory/stockChangeReport/index'); ?>">库存异动通知</div>
				<div class="item" href="<?php echo Yii::app()->createUrl('inventory/inventoryReport/index'); ?>">库存盘点通知</div>
				<div class="item" href="<?php echo Yii::app()->createUrl('inventory/inventorySumAvailable/index'); ?>">实时库存查询</div>
				<!-- <div class="item">库存同步</div> -->
			</div>
			<div class="menuItem" id="m6">
                <!--
			    <div class="item" href="<?php echo Yii::app()->createUrl('interfaceLog/customerLog/index'); ?>">客商档案日志</div>
			    -->
			    <div class="item" href="<?php echo Yii::app()->createUrl('interfaceLog/productLog/index'); ?>">商品日志</div>	
				<div class="item" href="<?php echo Yii::app()->createUrl('interfaceLog/orderLog/index'); ?>">订单日志</div>
				<div class="item" href="<?php echo Yii::app()->createUrl('interfaceLog/waybillLog/index'); ?>">菜鸟电子面单接口日志</div>
                <div class="item" href="<?php echo Yii::app()->createUrl('interfaceLog/pddLog/index'); ?>">拼多多电子面单接口日志</div>
            </div>
			<div class="menuItem" id="m9">
				<div class="item"
					href="<?php echo Yii::app()->createUrl('base/manage/index'); ?>">接口联调</div>
			</div>
		</div>
	</div>
	<div data-options="region:'center'" style="border-width: 0px;">
		<div id="tt" class="easyui-tabs" data-options="fit:true">
			<div title="欢迎信息" style="padding: 25px 40px;">
				<p style="font-size: 13px; font-weight: bold;">您好：</p>
				<p style="font-size: 13px; font-weight: bold;">&nbsp;&nbsp;&nbsp;&nbsp;欢迎登录OMS管理系统!</p>
			</div>
		</div>
	</div>
</div>
<div id="window-dialog">
	<iframe scrolling="no" id='window-dialog-Iframe' frameborder="0" src=""
		style="width: 100%; height: 100%; overflow: hidden;"></iframe>
</div>
<script type="text/javascript"
	src="./static/js/controllers/site-home.js"></script>