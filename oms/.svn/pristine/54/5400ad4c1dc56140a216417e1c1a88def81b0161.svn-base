<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/36');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(36.1);
?>
<script type="text/javascript">
    var batchViewUrl = '<?php echo Yii::app()->createUrl('inventory/stockChangeBatchReport/index', array('id'=>'uid')); ?>';
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
	    <lable>引起异动的单据编码</lable>
	    <input type="text" name="StockChangeReport[order_code]" class="easyui-textbox" style="width:12%" id="orderCode"/>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>订单类型：</lable>
		<select name="StockChangeReport[order_type]" id="orderType" class="easyui-combobox" editable="false" style="width: 16%;">
			<option value=''>全部</option>
			<option value='JYCK'>一般交易出库</option>
			<option value='HHCK'>换货出库</option>
			<option value='BFCK'>补发出库</option>
			<option value='PTCK'>普通出库</option>
			<option value='DBCK'>调拨出库</option>
			<option value='QTCK'>其他出库</option>
			<option value='SCRK'>生产入库</option>
			<option value='LYRK'>领用入库</option>
			<option value='CCRK'>残次品入库</option>
			<option value='CGRK'>采购入库</option>
			<option value='DBRK'>调拨入库</option>
			<option value='QTRK'>其他入库</option>
			<option value='XTRK'>销退入库</option>
			<option value='HHRK'>换货入库</option>
			<option value='JQRK'>拒签入库</option>
			<option value='CNJG'>仓内加工单</option>
			<option value='ZTTZ'>状态调整单</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>货主ID：</lable>
		<input type="text" name="StockChangeReport[customer_id]" class="easyui-textbox" style="width: 12%;" id="customerId" /><p></p>
		<lable>仓库编码：</lable>
		<input type="text" name="StockChangeReport[warehouse_code]" class="easyui-textbox" style="width: 12%;" id="warehouseCode" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>开始时间：</lable>
	    <input class="easyui-datetimebox" name="StockChangeReport[start_time]" style="width:200px" id="startTime" value="<?php echo date('Y-m-d', time()).' 00:00:00';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>结束时间：</lable>
	    <input class="easyui-datetimebox" name="StockChangeReport[end_time]" style="width:200px" id="endTime" value="<?php echo date('Y-m-d', time()).' 23:59:59';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchForm()" style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript" src="./static/js/moudles/inventory/inventory-stockChangeReport.js"></script>
