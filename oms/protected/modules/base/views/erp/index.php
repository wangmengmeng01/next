<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/1');
//获取登陆者是否有操作权限
$operateFlag = util::isHasPri(1.1);
?>
<script type="text/javascript">
	var updateUrl = '<?php echo $this->createUrl('erp/update', array('id'=>'uid'));?>';
	var deleteUrl = '<?php echo $this->createUrl('erp/delete');?>';
	var operateFlag = <?php echo $operateFlag; ?>;
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="Erp_formid">
		<lable>ERP软件编码：</lable>
		<input type="text" name="Erp[erp_code]" class="easyui-textbox"
			style="width: 12%;" id="Erp_erp_code" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>ERP软件名称：</lable>
		<input type="text" name="Erp[erp_name]" class="easyui-textbox"
			style="width: 12%;" id="Erp_erp_name" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>有效性：</lable>
		<select id="isValid" class="easyui-combobox" editable="false"
			style="width: 16%;">
			<option value=''>全部</option>
			<option value='1'>有效</option>
			<option value='0'>无效</option>
		</select> <a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-search'" onClick="searchForm()"
			style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript" src="./static/js/moudles/base/base-erp.js"></script>