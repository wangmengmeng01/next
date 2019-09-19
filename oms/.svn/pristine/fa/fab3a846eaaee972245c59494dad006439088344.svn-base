<style type="text/css">
#form_warehouse{
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
$("#WarehouseActiveFlag").combobox({
	data : [{ "id":'Y', "text":"是" },{ "id":'N', "text":"否"}] ,
	valueField : 'id',
	textField : 'text',
	editable : false,
	panelHeight:'auto',
    value: '<?php echo isset($model['active_flag'])?$model['active_flag']:'Y';?>'
});
$("#WarehouseIsValid").combobox({
	data : [{ "id":'1', "text":"有效" },{ "id":'0', "text":"无效"}] ,
	valueField : 'id',
	textField : 'text',
	editable : false,
	panelHeight:'auto',
    value: '<?php echo isset($model['is_valid'])?$model['is_valid']:'1';?>'
});
</script>
<div class="ftitle">客户维护界面</div>
<form id="form_warehouse">
<table>
	<tr>
		<td>仓库编码：</td>
		<td>
			<input type="text" name="Warehouse[warehouse_code]" id="warehouseCode" style="width:200px;" value="<?php echo $model['warehouse_code'] ?>" class='easyui-validatebox' missingMessage="仓库编码为必填项"  maxLength="20" data-options="required:true,validType:['code','length[2,20]']"/>
		</td>
	</tr>
	<tr>
		<td>仓库名称：</td>
		<td>
			<input type="text" name="Warehouse[descr_c]"  value="<?php echo $model['descr_c'] ?>" style="width:200px;" class='easyui-validatebox' missingMessage="仓库名称为必填项"  maxLength="50" data-options="required:true,validType:['special','length[2,50]']"/>
		</td>
	</tr>
    <tr>
        <td>接口url：</td>
        <td>
            <input type="text" name="Warehouse[wms_url]"  value="<?php echo $model['wms_url'] ?>" style="width:350px;" class='easyui-validatebox' missingMessage="仓库接口地址"  maxLength="255" data-options="validType:['interfaceUrl','length[2,255]']"/>
        </td>
    </tr>
	<tr>
		<td>所属网点编码：</td>
		<td>
			<input type="text" name="Warehouse[branch_code]"  value="<?php echo $model['branch_code']; ?>" style="width:200px;" class='easyui-validatebox' missingMessage="所属网点编码为必填项"  maxLength="8" data-options="required:true,validType:['gsbm','length[6,8]']"/>
		</td>
	</tr>
	<tr>
		<td>联系人：</td>
		<td>
			<input type="text" name="Warehouse[contact1]" style="width:200px;"  value="<?php echo $model['contact1'] ?>" class='easyui-validatebox' maxLength="20" validType="special"/>
		</td>
	</tr>
	<tr>
		<td>联系人手机号码：</td>
		<td>
			<input type="text" name="Warehouse[contact1_tel1]"  value="<?php echo $model['contact1_tel1'] ?>" style="width:200px;" class='easyui-validatebox' maxLength="24" validType="mobile"/>
		</td>
	</tr>
	<tr>
		<td>联系人电话号码：</td>
		<td>
			<input type="text" name="Warehouse[contact1_tel2]"  value="<?php echo $model['contact1_tel2'] ?>" style="width:200px;" class='easyui-validatebox' maxLength="24" validType="phone"/>
		</td>
	</tr>
	<tr>
		<td>联系地址：</td>
		<td>
			<input type="text" name="Warehouse[address1]" style="width:350px;"  value="<?php echo $model['address1'] ?>" class='easyui-validatebox' maxLength="100" validType="special"/>
		</td>
	</tr>
	<tr>
		<td>激活标志：</td>
		<td>
			<select class="easyui-combobox" id="WarehouseActiveFlag" name="Warehouse[active_flag]" style="width:150px;"/></select>
		</td>
	</tr>
	<tr>
		<td>有效性：</td>
		<td>
			<select class="easyui-combobox" id="WarehouseIsValid" name="Warehouse[is_valid]" style="width:150px;"/></select>
		</td>
	</tr>
	<tr>
		<td>备注：</td>
		<td>
			<textarea name="Warehouse[remark]" rows=5 cols=40  class="textarea easyui-validatebox" maxLength="200" validType="special"><?php echo $model['remark']; ?></textarea>
		</td>
	</tr>
</table>
</form>
