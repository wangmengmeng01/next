<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/18');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(18.1);
?>
<script type="text/javascript">
    var detailViewUrl = '<?php echo Yii::app()->createUrl('inventory/inventoryProductReport/index', array('id'=>'uid','name'=>'uname')); ?>';
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
</script>
<style type="text/css">
    .combo-panel {
      height:228px;
      overflow: auto;
    }
</style>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="SCR_formid">
	    <lable>盘点单编码</lable>
	    <input type="text" name="InventoryReport[check_order_code]" class="easyui-textbox" style="width:12%" id="checkOrderCode"/>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>货主ID：</lable>
		<input type="text" name="InventoryReport[customer_id]" class="easyui-textbox" style="width: 12%;" id="customerId" /><p></p>
		<lable>仓库编码：</lable>
		<input type="text" name="InventoryReport[warehouse_code]" class="easyui-textbox" style="width: 12%;" id="warehouseCode" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>开始时间：</lable>
	    <input class="easyui-datetimebox" name="InventoryReport[start_time]" style="width:200px" id="startTime" value="<?php echo date('Y-m-d', time()).' 00:00:00';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>结束时间：</lable>
	    <input class="easyui-datetimebox" name="InventoryReport[end_time]" style="width:200px" id="endTime" value="<?php echo date('Y-m-d', time()).' 23:59:59';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchForm()" style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript" src="./static/js/moudles/inventory/inventory-inventoryReport.js"></script>
