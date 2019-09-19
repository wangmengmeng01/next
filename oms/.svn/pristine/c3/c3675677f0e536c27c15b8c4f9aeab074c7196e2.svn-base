<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/85');
//获取登陆者是否有操作权限
$operateFlag = util::isHasPri(85.1);
?>
<script type="text/javascript">
	var updateUrl = '<?php echo $this->createUrl('DestinationWarehouseBind/update', array('id'=>'uid'));?>';
	var deleteUrl = '<?php echo $this->createUrl('DestinationWarehouseBind/delete');?>';
	var operateFlag = <?php echo $operateFlag; ?>;
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="DestinationWarehouseBind_formid">
		<lable>货主：</lable>
		<input type="text" name="DestinationWarehouseBind[customer_name]" class="easyui-textbox" style="width: 12%;" id="customerName" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>货主编码：</lable>
		<input type="text" name="DestinationWarehouseBind[customer_id]" class="easyui-textbox" style="width: 12%;" id="customerId" />&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-search'" onClick="searchForm()"
			style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript"
	src="./static/js/moudles/base/base-DestinationWarehouseBind.js"></script>