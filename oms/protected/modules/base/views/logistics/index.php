<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/7');
//获取登陆者是否有操作权限
$operateFlag = util::isHasPri(7.1);
?>
<script type="text/javascript">
	var updateUrl = '<?php echo $this->createUrl('logistics/update', array('id'=>'uid'));?>';
	var deleteUrl = '<?php echo $this->createUrl('logistics/delete');?>';
	var operateFlag = <?php echo $operateFlag; ?>;
</script>
<div region="north" border="true" split="true"	style="background: #B3DFDA; padding: 10px">
	<form id="Logistics_formid">
		<lable>物流公司编码：</lable>
		<input type="text" name="Logistics[logistics_code]" class="easyui-textbox" style="width: 12%;"  id="Logistics_logistics_code" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>物流公司名称：</lable>
		<input type="text" name="Logistics[logistics_name]" class="easyui-textbox" style="width: 12%;"  id="Logistics_logistics_name" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>有效性：</lable>
		<select id="Logistics_isValid" name="Logistics[is_valid]" class="easyui-combobox" editable="false" style="width: 16%;">
			<option value=''>全部</option>
			<option value='1'>有效</option>
			<option value='0'>无效</option>
		</select>
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchForm()" style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript" src="./static/js/moudles/base/base-logistics.js"></script>