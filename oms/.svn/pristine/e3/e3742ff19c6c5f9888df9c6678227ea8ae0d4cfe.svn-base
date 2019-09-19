<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/23');
//获取登陆者是否有操作权限
$operateFlag = util::isHasPri(23.1);
?>
<script type="text/javascript">
	var updateUrl = '<?php echo $this->createUrl('qimenCustomerBind/update', array('id'=>'uid'));?>';
	var deleteUrl = '<?php echo $this->createUrl('qimenCustomerBind/delete');?>';
	var pushWmsUrl = '<?php echo $this->createUrl('qimenCustomerBind/pushWms');?>';
	var operateFlag = <?php echo $operateFlag; ?>;
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="QIMEN_CustomerBindBind_formid">
		<lable>奇门货主ID/菜鸟仓编码：</lable>
		<input type="text" name="QIMEN_CustomerBind[qimen_customer_id]" class="easyui-textbox" style="width: 12%;" id="qimenCustomerId" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>奇门货主编码：</lable>
		<input type="text" name="QIMEN_CustomerBind[customer_id]" class="easyui-textbox" style="width: 12%;" id="customerId" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>仓库编码（菜鸟仓储接口需要维护）：</lable>
        <input type="text" name="QIMEN_CustomerBind[warehouse_code]" class="easyui-textbox" style="width: 12%;" id="warehouseCode" />
        <br/><br/>
        <lable style="padding-left: 65px;">智能仓标识：</lable>
        <select name="QIMEN_CustomerBind[auto_flag]" id="autoFlag" class="easyui-combobox" editable="false" style="width: 150px;">
            <option value=''>全部</option>
            <option value='1'>是</option>
            <option value='0'>否</option>
        </select>
        <lable style="padding-left: 110px;">有效性：</lable>
		<select name="QIMEN_CustomerBind[is_valid]" id="isValid" class="easyui-combobox" editable="false" style="width: 150px;">
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
	src="./static/js/moudles/base/base-qimenCustomerBind.js"></script>