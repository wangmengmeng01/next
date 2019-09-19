<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/6');
//获取登陆者是否有操作权限
$operateFlag = util::isHasPri(6.1);

?>
<script type="text/javascript">
	var updateUrl = '<?php echo $this->createUrl('customerBind/update', array('id'=>'uid'));?>';
	var deleteUrl = '<?php echo $this->createUrl('customerBind/delete');?>';
	var pushErpUrl = '<?php echo $this->createUrl('customerBind/pushErp');?>';
	var pushWmsUrl = '<?php echo $this->createUrl('customerBind/pushWms');?>';
	var operateFlag = <?php echo $operateFlag; ?>;
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="CustomerBind_formid">
		<lable>货主ID：</lable>
		<input type="text" name="CustomerBind[customer_id]" class="easyui-textbox" style="width: 12%;" id="customerId" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>ERP编码：</lable>
		<input type="text" name="CustomerBind[erp_code]" class="easyui-textbox" style="width: 12%;" id="erpCode" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>WMS编码：</lable>
		<input type="text" name="CustomerBind[wms_code]" class="easyui-textbox" style="width: 12%;" id="wmsCode" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>有效性：</lable>
		<select name="CustomerBind[is_valid]" id="isValid" class="easyui-combobox" editable="false" style="width: 150px;">
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
	src="./static/js/moudles/base/base-customerBind.js"></script>