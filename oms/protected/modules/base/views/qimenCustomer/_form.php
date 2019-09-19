<style type="text/css">
#form_qimenCustomer{
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
$("#CustomerIsValid").combobox({
	data : [{ "id":'1', "text":"有效" },{ "id":'0', "text":"无效"}] ,
	valueField : 'id',
	textField : 'text',
	editable : false,
	panelHeight:'auto',
    value: '<?php echo isset($model['is_valid'])?$model['is_valid']:'1';?>'
});
</script>
<div class="ftitle">奇门客户配置维护界面</div>
<form id="form_qimenCustomer"> 
<table>
	<tr>
		<td>客户ID：</td>
		<td>
			<input type="text" name="Qimen_Customer[customer_id]" id="customer_id" style="width:200px;" value="<?php echo $model['customer_id'] ?>" class='easyui-validatebox' missingMessage="客户ID为必填项"  maxLength="30" data-options="required:true,validType:['code','length[2,30]']"/>
		</td>
	</tr>
	<tr>
		<td>客户名称：</td>
		<td>
			<input type="text" name="Qimen_Customer[customer_name]"  value="<?php echo $model['customer_name'] ?>" style="width:200px;" class='easyui-validatebox' missingMessage="客户名称为必填项"  maxLength="50" data-options="required:true,validType:['special','length[2,50]']"/>
		</td>
	</tr>
	<tr>
		<td>奇门WMS的app_key：</td>
		<td>
			<input type="text" name="Qimen_Customer[wms_app_key]"  value="<?php echo $model['wms_app_key'] ?>" style="width:200px;" class='easyui-validatebox' missingMessage="wms的app_key为必填项"  maxLength="64" data-options="required:true,validType:['code']"/>
		</td>
	</tr>
	<tr>
		<td>奇门WMS密码：</td>
		<td>
			<input type="text" name="Qimen_Customer[wms_secret]"  value="<?php echo $model['wms_secret'] ?>" style="width:200px;" class='easyui-validatebox' missingMessage="wms的秘钥为必填项"  maxLength="64" data-options="required:true,validType:['code']"/>
		</td>
	</tr>
	<tr>
		<td>中文描述：</td>
		<td>
			<input type="text" name="Qimen_Customer[descr_c]"  value="<?php echo $model['descr_c'] ?>" style="width:200px;" class='easyui-validatebox' maxLength="20" validType="special"/>
		</td>
	</tr>
	<tr>
		<td>英文描述：</td>
		<td>
			<input type="text" name="Qimen_Customer[descr_e]"  value="<?php echo $model['descr_e'] ?>" style="width:200px;" class='easyui-validatebox' maxLength="20" validType="special"/>
		</td>
	</tr>
	<tr>
		<td>联系人：</td>
		<td>
			<input type="text" name="Qimen_Customer[contact]"  value="<?php echo $model['contact'] ?>" style="width:200px;" class='easyui-validatebox' maxLength="20" validType="special"/>
		</td>
	</tr>
	<tr>
		<td>联系人手机号：</td>
		<td>
			<input type="text" name="Qimen_Customer[contact_mobile]"  value="<?php echo $model['contact_mobile'] ?>" style="width:200px;" class='easyui-validatebox' maxLength="100" validType="mobile"/>
		</td>
	</tr>
	<tr>
		<td>联系人固话：</td>
		<td>
			<input type="text" name="Qimen_Customer[contact_phone]"  value="<?php echo $model['contact_phone'] ?>" style="width:200px;" class='easyui-validatebox' maxLength="100" validType="phone"/>
		</td>
	</tr>
	<tr>
		<td>ERP编码：</td>
		<td>
			<input type="text" name="Qimen_Customer[erp_code]"  value="<?php echo $model['erp_code'] ?>" style="width:200px;" class='easyui-validatebox' missingMessage="ERP编码为必填项"  maxLength="30" data-options="required:true,validType:['code','length[2,30]']"/>
		</td>
	</tr>
	
	<tr>
		<td>ERP接口版本：</td>
		<td>
			<input type="text" name="Qimen_Customer[erp_api_ver]"  value="<?php echo $model['erp_api_ver']; ?>" style="width:200px;" class='easyui-validatebox' missingMessage="ERP接口版本为必填项"  maxLength="10" data-options="required:true,validType:['code','length[1,10]']"/>
		</td>
	</tr>
	<tr>
		<td>ERP接口地址：</td>
		<td>
			<input type="text" name="Qimen_Customer[erp_api_url]"  value="<?php echo $model['erp_api_url']; ?>" style="width:350px;" class='easyui-validatebox' missingMessage="ERP接口地址为必填项"  maxLength="255" data-options="required:true,validType:['interfaceUrl','length[2,255]']"/>
		</td>
	</tr>
	<tr>
		<td>WMS编码：</td>
		<td>
			<input type="text" name="Qimen_Customer[wms_code]" style="width:200px;"  value="<?php echo $model['wms_code'] ?>" class='easyui-validatebox' maxLength="30" missingMessage="WMS编码为必填项"  maxLength="30" data-options="required:true,validType:['code','length[2,50]']"/>
		</td>
	</tr>
	<tr>
		<td>WMS接口版本：</td>
		<td>
			<input type="text" name="Qimen_Customer[wms_api_ver]"  value="<?php echo $model['wms_api_ver'] ?>" style="width:200px;" class='easyui-validatebox' maxLength="24" missingMessage="WMS接口版本为必填项"  maxLength="10" data-options="required:true,validType:['code']"/>
		</td>
	</tr>
	<tr>
		<td>WMS接口地址：</td>
		<td>
			<input type="text" name="Qimen_Customer[wms_api_url]"  value="<?php echo $model['wms_api_url'] ?>" style="width:350px;" class='easyui-validatebox' maxLength="255" missingMessage="WMS接口地址为必填项"  maxLength="255" data-options="required:true,validType:['interfaceUrl','length[2,255]']"/>
		</td>
	</tr>
	<tr>
		<td>备注：</td>
		<td>
			<textarea name="Qimen_Customer[remark]" rows=5 cols=40  class="textarea easyui-validatebox" maxLength="255" validType="special"><?php echo $model['remark']; ?></textarea>
		</td>
	</tr>
	<tr>
		<td>有效性：</td>
		<td>
			<select class="easyui-combobox" id="CustomerIsValid" name="Qimen_Customer[is_valid]" style="width:150px;"/></select>
		</td>
	</tr>
</table>
</form>
