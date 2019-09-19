<style type="text/css">
#form_DestinationWarehouseBind{
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
    var wh = new Array(
        '<?php echo $model['wh1']; ?>',
        '<?php echo $model['wh2']; ?>',
        '<?php echo $model['wh3']; ?>',
        '<?php echo $model['wh4']; ?>',
        '<?php echo $model['wh5']; ?>',
        '<?php echo $model['wh6']; ?>',
        '<?php echo $model['wh7']; ?>',
        '<?php echo $model['wh8']; ?>',
        '<?php echo $model['wh9']; ?>',
        '<?php echo $model['wh10']; ?>'
    );
    for(var i=1;i<=10;i++) {
        $("#wh"+i).combobox({
            data : <?php echo $warehouse; ?> ,
            valueField : 'warehouse_code',
            textField : 'descr_c',
            editable : false,
            panelHeight:'auto',
            value: wh[i-1]
        });
    }

</script>
<div class="ftitle">目的地与分仓关联排序表</div>
<form id="form_DestinationWarehouseBind"> 
<table>
	<tr>
		<td>货主名称：</td>
		<td>
			<input type="text" name="DestinationWarehouseBind[customer_name]" id="customerName" style="width:200px;" value="<?php echo $model['customer_name'] ?>" class='easyui-validatebox' missingMessage="货主名称为必填项"  maxLength="50" data-options="required:true,validType:['code','length[2,50]']"/>
		</td>
	</tr>
	<tr>
		<td>货主编码：</td>
		<td>
            <input type="text" name="DestinationWarehouseBind[customer_id]" id="customerId" style="width:200px;" value="<?php echo $model['customer_id'] ?>" class='easyui-validatebox' missingMessage="货主编码为必填项"  maxLength="30" data-options="required:true,validType:['code','length[2,30]']"/>
		</td>
	</tr>
    <tr>
        <td>平台编码：</td>
        <td>
            <input type="text" name="DestinationWarehouseBind[platform_code]" id="platformCode" style="width:200px;" value="<?php echo $model['platform_code'] ?>" class='easyui-validatebox' />
        </td>
    </tr>
    <tr>
        <td>店铺名称：</td>
        <td>
            <input type="text" name="DestinationWarehouseBind[shop_name]" id="shopName" style="width:200px;" value="<?php echo $model['shop_name'] ?>" class='easyui-validatebox' />
        </td>
    </tr>
    <tr>
        <td>固定收货机构或地址：</td>
        <td>
            <input type="text" name="DestinationWarehouseBind[rc_addr]" id="rcAddr" style="width:200px;" value="<?php echo $model['rc_addr'] ?>" class='easyui-validatebox' missingMessage="固定收货机构或地址为必填项" data-options="required:true"/>
        </td>
    </tr>
    <tr>
        <td>分仓1：</td>
        <td>
            <select name="DestinationWarehouseBind[wh1]" id="wh1" style="width:150px;"  class="easyui-combobox" missingMessage="分仓1为必填项" data-options="required:true"/>
        </td>
    </tr>
    <tr>
        <td>分仓2：</td>
        <td>
            <select name="DestinationWarehouseBind[wh2]" id="wh2" style="width:150px;"  class="easyui-combobox" />
        </td>
    </tr>
    <tr>
        <td>分仓3：</td>
        <td>
            <select name="DestinationWarehouseBind[wh3]" id="wh3" style="width:150px;"  class="easyui-combobox" />
        </td>
    </tr>
    <tr>
        <td>分仓4：</td>
        <td>
            <select name="DestinationWarehouseBind[wh4]" id="wh4" style="width:150px;"  class="easyui-combobox" />
        </td>
    </tr>
    <tr>
        <td>分仓5：</td>
        <td>
            <select name="DestinationWarehouseBind[wh5]" id="wh5" style="width:150px;"  class="easyui-combobox" />
        </td>
    </tr>
    <tr>
        <td>分仓6：</td>
        <td>
            <select name="DestinationWarehouseBind[wh6]" id="wh6" style="width:150px;"  class="easyui-combobox" />
        </td>
    </tr>
    <tr>
        <td>分仓7：</td>
        <td>
            <select name="DestinationWarehouseBind[wh7]" id="wh7" style="width:150px;"  class="easyui-combobox" />
        </td>
    </tr>
    <tr>
        <td>分仓8：</td>
        <td>
            <select name="DestinationWarehouseBind[wh8]" id="wh8" style="width:150px;"  class="easyui-combobox" />
        </td>
    </tr>
    <tr>
        <td>分仓9：</td>
        <td>
            <select name="DestinationWarehouseBind[wh9]" id="wh9" style="width:150px;"  class="easyui-combobox" />
        </td>
    </tr>
    <tr>
        <td>分仓10：</td>
        <td>
            <select name="DestinationWarehouseBind[wh10]" id="wh10" style="width:150px;"  class="easyui-combobox" />
        </td>
    </tr>
</table>
</form>
