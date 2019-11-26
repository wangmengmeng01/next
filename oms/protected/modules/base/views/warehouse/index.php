<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/4');
//获取登陆者是否有操作权限
$operateFlag = util::isHasPri(4.1);
?>
<script type="text/javascript">
	var updateUrl = '<?php echo $this->createUrl('warehouse/update', array('id'=>'uid'));?>';
	var deleteUrl = '<?php echo $this->createUrl('warehouse/delete');?>';
	var operateFlag = <?php echo $operateFlag; ?>;
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="Warehouse_formid">
		<lable>仓库编码：</lable>
		<input type="text" name="Warehouse[warehouse_code]" class="easyui-textbox" style="width: 12%;" id="Warehouse_warehouse_code" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>仓库名称：</lable>
		<input type="text" name="Warehouse[descr_c]" class="easyui-textbox" style="width: 12%;" id="Warehouse_descr_c" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>网点编码：</lable>
		<input type="text" name="Warehouse[branch_code]" class="easyui-textbox" style="width: 100px;" id="branchCode" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>是否激活：</lable>
		<select name="Warehouse[active_flag]" id="activeFlag" class="easyui-combobox" editable="false" style="width: 150px;">
			<option value=''>全部</option>
			<option value='Y'>是</option>
			<option value='N'>否</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>有效性：</lable>
		<select name="Warehouse[is_valid]" id="isValid" class="easyui-combobox" style="width: 150px;">
			<option value=''>全部</option>
			<option value='1'>有效</option>
			<option value='0'>无效</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchForm()" style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript" src="./static/js/moudles/base/base-warehouse.js"></script>