<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/10');
//获取登陆者是否有导出excel权限
$operateFlag = util::isHasPri(10.1);
?>
<script type="text/javascript">
	var operateFlag = <?php echo $operateFlag; ?>;
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="Product_formid">
		<lable>SKU：</lable>
		<input type="text" class="easyui-textbox" style="width: 12%;" name="Product[sku]" id="Product_sku" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>商品名称：</lable>
		<input type="text" class="easyui-textbox" style="width: 12%;" name="Product[descr_c]" id="Product_descr_c" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>货主ID：</lable>
		<input type="text" name="Product[customer_id]" class="easyui-textbox" style="width: 12%;" id="customer_id" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>是否激活：</lable>
		<select name="Product[active_flag]" id="Product_active_flag" editable="false" class="easyui-combobox" style="width: 16%;">
			<option value=''>全部</option>
			<option value='Y'>是</option>
			<option value='N'>否</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchForm()" style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript" src="./static/js/moudles/base/base-product.js"></script>