<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/20');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(20.1);
?>
<script type="text/javascript">
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
</script>
<div region="north" border="true" split="true" style="background: #B3DFDA; padding: 10px">
	<form id="QueryINVData_formid">
		<lable>货主ID：</lable>
		<input type="text" name="QueryINVData[customer_id]" class="easyui-textbox" style="width: 12%;" id="customerId" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>仓库编码：</lable>
		<input type="text" name="QueryINVData[warehouse_code]" name="QueryINVData[warehouse_code]" class="easyui-textbox" style="width: 12%;" id="warehouseCode" />&nbsp;&nbsp;&nbsp;&nbsp;	
		<lable>SKU：</lable>
		<input type="text" name="QueryINVData[sku]" class="easyui-textbox" style="width: 12%;" id="sku" />&nbsp;&nbsp;&nbsp;&nbsp;	
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchForm()" style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript" src="./static/js/moudles/inventory/inventory-queryINVData.js"></script>