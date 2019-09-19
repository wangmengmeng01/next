<style type="text/css">
#form_waybill{
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
<div class="ftitle">商家开通电子面单服务信息维护界面</div>
<form id="form_waybill"> 
<table>
    <tr style="display: none">
		<td>ID：</td>
		<td>
			<input type="text" name="WaybillInfo[info_id]" id="info_id" style="width:200px;" value="<?php echo $model['info_id'] ?>" class='easyui-validatebox' maxLength="20" data-options="required:true,validType:['code','length[2,20]']"/>
		</td>
	</tr>
	<tr>
		<td>商家ID：</td>
		<td>
			<input type="text" name="WaybillInfo[seller_id]" id="seller_id" style="width:200px;" value="<?php echo $model['seller_id'] ?>" class='easyui-validatebox' maxLength="20" data-options="required:true,validType:['code','length[2,20]']"/>
		</td>
	</tr>
	<tr>
		<td >仓库地址编码：</td>
		<td>
			<input type="text" name="WaybillInfo[ship_addr_code]" id="ship_addr_code" value="<?php echo $model['ship_addr_code'] ?>" style="width:200px;" class='easyui-validatebox' maxLength="50" data-options="validType:['code','length[1,30]']"/>
		</td>
	</tr>
	<tr>
		<td>详细地址：</td>
		<td>
			<input type="text" name="WaybillInfo[ship_detail_address]"  id="ship_detail_address" value="<?php echo $model['ship_detail_address']; ?>" style="width:300px;" class='easyui-validatebox' maxLength="8" data-options="required:true,validType:['addr','length[2,200]']"/>
		</td>
	</tr>
	
	<tr>
		<td>发件省：</td>
		<td>
			<input type="text" name="WaybillInfo[ship_prov]" id="ship_prov" value="<?php echo $model['ship_prov']; ?>" style="width:200px;" class='easyui-validatebox' maxLength="32" data-options="required:true,validType:['addr','length[2,50]']"/>
		</td>
	</tr>
	<tr>
		<td>发件城市：</td>
		<td>
			<input type="text" name="WaybillInfo[ship_city]" id="ship_city" value="<?php echo $model['ship_city']; ?>" style="width:200px;" class='easyui-validatebox' maxLength="32" data-options="validType:['addr','length[2,50]']"/>
		</td>
	</tr>
	<tr>
		<td>发件县区：</td>
		<td>
			<input type="text" name="WaybillInfo[ship_county]" id="ship_county" value="<?php echo $model['ship_county']; ?>" style="width:200px;" class='easyui-validatebox' maxLength="32" data-options="validType:['addr','length[2,50]']"/>
		</td>
	</tr>
	<tr>
		<td>发件乡镇：</td>
		<td>
			<input type="text" name="WaybillInfo[ship_town]" id="ship_town" value="<?php echo $model['ship_town']; ?>" style="width:200px;" class='easyui-validatebox' maxLength="32" data-options="validType:['addr','length[2,50]']"/>
		</td>
	</tr>
	<tr>
		<td>快递公司编码：</td>
		<td>
			<input type="text" name="WaybillInfo[cp_code]" id="cp_code" value="<?php echo $model['cp_code']; ?>" style="width:200px;" class='easyui-validatebox' maxLength="32" data-options="validType:['code','length[0,50]']"/>
		</td>
	</tr>
	<tr>
		<td>快递公司类型：</td>
		<td>
			<input type="text" name="WaybillInfo[cp_type]" id="cp_type" value="<?php echo $model['cp_type']; ?>" style="width:200px;" class='easyui-validatebox' maxLength="1" data-options="validType:['type','length[0,1]']"/>
		</td>
	</tr>
	<tr>
		<td>当前余量：</td>
		<td>
			<input type="text" name="WaybillInfo[quantity]" id="quantity" value="<?php echo $model['quantity']; ?>" style="width:200px;" class='easyui-validatebox' maxLength="32" data-options="validType:['code','length[0,11]']"/>
		</td>
	</tr>
	<tr>
		<td>总分配单号数：</td>
		<td>
			<input type="text" name="WaybillInfo[allocated_quantity]" id="allocated_quantity" value="<?php echo $model['allocated_quantity']; ?>" style="width:200px;" class='easyui-validatebox' maxLength="32" data-options="validType:['code','length[0,11]']"/>
		</td>
	</tr>
	<tr>
		<td>单号取消数：</td>
		<td>
			<input type="text" name="WaybillInfo[cancel_quantity]" id="cancel_quantity" value="<?php echo $model['cancel_quantity']; ?>" style="width:200px;" class='easyui-validatebox' maxLength="32" data-options="validType:['code','length[0,11]']"/>
		</td>
	</tr>
	<tr>
		<td>网点编码：</td>
		<td>
			<input type="text" name="WaybillInfo[branch_code]" id="branch_code" value="<?php echo $model['branch_code']; ?>" style="width:200px;" class='easyui-validatebox'  maxLength="32" data-options="validType:['code','length[2,8]']"/>
		</td>
	</tr>
	<tr>
		<td>网点名称：</td>
		<td>
			<input type="text" name="WaybillInfo[branch_name]" id="branch_name" value="<?php echo $model['branch_name']; ?>" style="width:200px;" class='easyui-validatebox' maxLength="32" data-options="validType:['name','length[2,40]']"/>
		</td>
	</tr>
</table>
</form>
