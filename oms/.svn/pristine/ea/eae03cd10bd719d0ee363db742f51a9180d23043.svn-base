<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/24');
//获取登陆者是否有导出excel权限
$excelExportFlag = util::isHasPri(24.1);
?>
<script type="text/javascript">
    var deatilViewUrl = '<?php echo Yii::app()->createUrl('base/combineProductDetail/index', array('id'=>'uid','name'=>'uname')); ?>';
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="Product_formid">
	    <lable>组合商品的ERP编码：</lable>
	    <input type="text" name="CombineProduct[item_code]" class="easyui-textbox" style="width:12%" id="itemCode"/>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>货主ID：</lable>
		<input type="text" name="CombineProduct[customer_id]" class="easyui-textbox" style="width: 12%;" id="customerId" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>仓库编码：</lable>
		<input type="text" name="CombineProduct[warehouse_code]" class="easyui-textbox" style="width: 12%;" id="warehouseCode" />&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchForm()" style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="exportDg"></div>
<script type="text/javascript" src="./static/js/moudles/base/base-combineProduct.js"></script>