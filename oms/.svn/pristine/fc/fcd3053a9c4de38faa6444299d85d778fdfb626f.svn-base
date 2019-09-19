<style type="text/css">
#from_erp{
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
$("#ErpIsValid").combobox({
	data : [{ "id":'1', "text":"有效" },{ "id":'0', "text":"无效"}] ,
	valueField : 'id',
	textField : 'text',
	editable : false,
	panelHeight:'auto',
    value: '<?php echo isset($model['is_valid'])?$model['is_valid']:'1';?>'
});
</script>
<div class="ftitle">ERP软件维护界面</div>
<?php $form = $this->beginWidget('CActiveForm', array('id'=>'from_erp')); ?>
<!-- <form id="from_erp">  -->
<table>
	<tr>
		<td>ERP编码：</td>
		<td>
			<input type="text" name="Erp[erp_code]" id="erpCode" style="width:200px;" value="<?php echo $model['erp_code'] ?>" class='easyui-validatebox' missingMessage="ERP编码为必填项"  maxLength="20" data-options="required:true,validType:['code','length[2,20]']" />
		</td>
	</tr>
	<tr>
		<td>ERP名称：</td>
		<td>
			<input type="text" name="Erp[erp_name]"  value="<?php echo $model['erp_name'] ?>" style="width:200px;" class='easyui-validatebox' missingMessage="ERP名称为必填项"  maxLength="50" data-options="required:true,validType:['special','length[2,50]']" />
		</td>
	</tr>
	<tr>
		<td>联系人：</td>
		<td>
			<input type="text" name="Erp[contact]" style="width:200px;"  value="<?php echo $model['contact'] ?>" class='easyui-validatebox' validType="special" maxLength="20"/>
		</td>
	</tr>
	<tr>
		<td>联系人手机号：</td>
		<td>
			<input type="text" name="Erp[contact_phone]"  value="<?php echo $model['contact_phone'] ?>" style="width:200px;" class='easyui-validatebox' validType="mobile" maxLength="24" />
		</td>
	</tr>
	<tr>
		<td>联系人固话：</td>
		<td>
			<input type="text" name="Erp[contact_tel]"  value="<?php echo $model['contact_tel'] ?>" style="width:200px;" class='easyui-validatebox' validType="phone" maxLength="24" />
		</td>
	</tr>	
	<tr>
		<td>联系地址：</td>
		<td>
			<input type="text" name="Erp[address]" style="width:200px;"  value="<?php echo $model['address'] ?>" class='easyui-validatebox' validType="special" maxLength="100"/>
		</td>
	</tr>
	<tr>
		<td>有效性：</td>
		<td>
			<select class="easyui-combobox" id="ErpIsValid" name="Erp[is_valid]" editable="false" style="width:150px;"/></select>
		</td>
	</tr>
	<tr>
		<td>备注：</td>
		<td>
			<textarea name="Erp[remark]" rows=5 cols=40  class="textarea easyui-validatebox" validType="special" maxLength="200"><?php echo $model['remark'] ?></textarea>
		</td>
	</tr>
</table>
<!-- </form> -->
<?php $form = $this->endWidget(); ?>