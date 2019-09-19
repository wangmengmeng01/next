<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/25');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(25.1);
?>
<script type="text/javascript">
	var materialViewUrl = '<?php echo Yii::app()->createUrl('inbound/storeProcessMaterialInfo/index', array('id'=>'uid','name'=>'uname')); ?>';
	var productViewUrl = '<?php echo Yii::app()->createUrl('inbound/storeProcessProductInfo/index', array('id'=>'uid','name'=>'uname')); ?>';
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="STPC_formid">
	    <lable>加工单编码</lable>
	    <input type="text" name="StoreProcessCreate[process_order_code]" class="easyui-textbox" style="width:12%" id="processOrderCode"/>&nbsp;&nbsp;&nbsp;&nbsp;
	    <lable>货主编码：</lable>
		<input type="text" name="StoreProcessCreate[customer_id]" class="easyui-textbox" style="width: 12%;" id="customerId" />
		<lable>仓库编码：</lable>
		<input type="text" name="StoreProcessCreate[warehouse_code]" class="easyui-textbox" style="width: 12%;" id="warehouseCode" /><p></p>
		<lable>开始时间：</lable>
	    <input class="easyui-datetimebox" name="StoreProcessCreate[start_time]" style="width:200px" id="startTime" value="<?php echo date('Y-m-d', time()).' 00:00:00';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>结束时间：</lable>
	    <input class="easyui-datetimebox" name="StoreProcessCreate[end_time]" style="width:200px" id="endTime" value="<?php echo date('Y-m-d', time()).' 23:59:59';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchForm()" style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="exportDg"></div>
<script type="text/javascript" src="./static/js/moudles/inbound/inbound-storeProcessCreate.js"></script>