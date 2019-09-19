<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/9');
?>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="Shop_formid">
		<lable>店铺编码：</lable>
		<input type="text" name="Shop[shop_code]" class="easyui-textbox" style="width: 12%;" id="Shop_shop_code" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>店铺名称：</lable>
		<input type="text" name="Shop[descr_c]" class="easyui-textbox" style="width: 12%;" id="Shop_descr_c" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>是否激活：</lable>
		<select name="name="Shop[active_flag]" id="Shop_active_flag" class="easyui-combobox" editable="false" style="width: 16%;">
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
<script type="text/javascript" src="./static/js/moudles/base/base-shop.js"></script>