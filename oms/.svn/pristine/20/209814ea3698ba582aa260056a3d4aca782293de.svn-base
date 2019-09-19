<style type="text/css">
#form_qimenCustomerBind{
	margin:0;
	padding:10px 30px;
}
.ftitle{
	font-size:12px;
	font-weight:bold;
	padding:5px 0;
	margin-bottom:10px;
	border-bottom:1px solid #ccc;
}
.fitem{
	margin-bottom:10px;
}
.fitem label{
	display:inline-block;
	width:80px;
}
</style>
<script type="text/javascript">
$("#CustomerBindIsValid").combobox({
	data : [{ "id":'1', "text":"有效" },{ "id":'0', "text":"无效"}] ,
	valueField : 'id',
	textField : 'text',
	editable : false,
	panelHeight:'auto',
    value: '<?php echo isset($model['is_valid'])?$model['is_valid']:'1';?>'
});
$("#QimenCustomerAutoFlag").combobox({
    data : [{ "id":'1', "text":"是" },{ "id":'0', "text":"否"}] ,
    valueField : 'id',
    textField : 'text',
    editable : false,
    panelHeight:'auto',
    value: '<?php echo isset($model['auto_flag'])?$model['auto_flag']:'0';?>'
});
</script>
<div class="ftitle">奇门货主绑定维护</div>
<form id="form_qimenCustomerBind"> 
<table>
    <tr>
		<td>奇门客户ID/菜鸟仓编码：</td>
		<td>
			<input type="text" name="QimenCustomerBind[qimen_customer_id]" id="QimenCustomerId" style="width:200px;" value="<?php echo $model['qimen_customer_id'] ?>" class='easyui-validatebox' missingMessage="奇门货主ID为必填项"  maxLength="50" data-options="required:true,validType:['code','length[2,50]']"/>
		</td>
	</tr>
	<tr>
		<td>货主ID：</td>
		<td>
			<input type="text" name="QimenCustomerBind[customer_id]" id="customerId" style="width:200px;" value="<?php echo $model['customer_id'] ?>" class='easyui-validatebox' missingMessage="货主ID为必填项"  maxLength="30" data-options="required:true,validType:['code','length[2,30]']"/>
		</td>
	</tr>
	<tr>
		<td>仓库编码：</td>
		<td>
			<input type="text" name="QimenCustomerBind[warehouse_code]" id="warehouseCode" style="width:200px;" value="<?php echo $model['warehouse_code'] ?>" class='easyui-validatebox' maxLength="30" data-options="validType:['code','length[2,30]']"/>
		</td>
	</tr>
    <tr>
        <td>智能仓标识：</td>
        <td>
            <select class="easyui-combobox" name="QimenCustomerBind[auto_flag]" id="QimenCustomerAutoFlag" style="width:150px;"/></select>
        </td>
    </tr>
	<tr>
		<td>有效性：</td>
		<td>
			<select class="easyui-combobox" id="CustomerBindIsValid" name="QimenCustomerBind[is_valid]" style="width:150px;"/></select>
		</td>
	</tr>
</table>
</form>
