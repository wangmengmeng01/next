<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/18');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(18.1);
?>
<script type="text/javascript">
	var excelExportFlag = <?php echo $excelExportFlag; ?>;	
	$.extend($.fn.textbox.methods, {
		setText : function(jq, value) {
			return jq.each(function() {
				var opts = $(this).textbox('options');
				var input = $(this).textbox('textbox');
				value = value == undefined ? '' : String(value);

				if ($(this).textbox('getText') != value) {
				input.val(value);
			}
			opts.value = value;
			if (!input.is(':focus')) {
				if (value) {
					input.removeClass('textbox-prompt');
				} else {
					input.val(opts.prompt).addClass('textbox-prompt');
				}
			}
			$(this).textbox('validate');
			});
		}
	});
</script>
<style type="text/css">
	#sku,#product_name{white-space: pre-wrap;};
</style>
<div region="north" border="true" split="true" style="background: #B3DFDA; padding: 10px">
	<form id="SCR_formid">
		<lable>货主ID：</lable>
		<input type="text" name="inventorySumAvailable[customer_id]" class="easyui-textbox" style="width: 12%;" id="customer_id" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>仓库编码：</lable>
		<input type="text" name="inventorySumAvailable[warehouse_code]" class="easyui-textbox" style="width: 12%;" id="warehouse_code" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>SKU：</lable>
		<textarea name="inventorySumAvailable[sku]" style="width: 12%;height: 35px;" class="easyui-textbox" data-options="multiline:true" id="sku"></textarea><span style="color: red;">*使用Enter键分割</span><p></p>
	    <!-- <input type="text" name="inventorySumAvailable[sku]" class="easyui-textbox" style="width: 12%;" id="sku" /><p></p> -->
		<lable>货主名称：</lable>
	    <input type="text" name="inventorySumAvailable[customer_name]" class="easyui-textbox" style="width: 12%;" id="customer_name" />&nbsp;&nbsp;&nbsp;&nbsp;
	    <lable>仓库名称：</lable>
		<input type="text" name="inventorySumAvailable[warehouse_name]" class="easyui-textbox" style="width: 12%;" id="warehouse_name" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>商品名称：</lable>
		<textarea name="inventorySumAvailable[product_name]" style="width: 12%;height: 35px;" class="easyui-textbox" data-options="multiline:true" id="product_name"></textarea><span style="color: red;">*使用Enter键分割</span>
<!-- 		<input type="text" name="inventorySumAvailable[product_name]" class="easyui-textbox" style="width: 12%;" id="product_name" /> -->
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchForm()" style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript" src="./static/js/moudles/inventory/inventory-inventorySumAvailable.js"></script>
                   