<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/86');
//获取登陆者是否有操作权限
$operateFlag = util::isHasPri(86.1);
?>
<script type="text/javascript">
    var authUrl = '<?php echo PDD_AUTH_URL; ?>';
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="Seller_formid">
		<lable>商家ID：</lable>
		<input type="text" name="Seller[seller_id]" class="easyui-textbox" style="width: 12%;" id="sellerId" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>有效性：</lable>
		<select name="Seller[is_valid]" id="isValid" class="easyui-combobox" editable="false" style="width: 150px;">
			<option value=''>全部</option>
			<option value='1'>有效</option>
			<option value='0'>无效</option>
		</select> 
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-search'" onClick="searchForm()"
			style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript"
	src="./static/js/moudles/base/base-cskPddAccessToken.js"></script>