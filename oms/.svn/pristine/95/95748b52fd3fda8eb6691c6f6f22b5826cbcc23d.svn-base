<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/84');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(84.1);
?>
<script type="text/javascript">
    var deatilViewUrl = '<?php echo Yii::app()->createUrl('outbound/vipJitPickDetail/index', array('pickNo'=>'uid')); ?>';
    var pushPickUrl = '<?php echo Yii::app()->createUrl('outbound/vipPushPick/index',array('vendorId'=>'vendor_id','sellSite'=>'sell_site','platformCode'=>'platform_code','shopName'=>'shop_name'));?>';
    var manualDownloadUrl = '<?php echo Yii::app()->createUrl('outbound/vipJitPickList/manualDownload',array('status'=>'Status'));?>';
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
    var selfreq = '<?php echo VIP_OMS_SELF_REQ_SECRET;?>';
    var vendorId = '<?php echo VIP_VENDOR_ID;?>';
    var status = <?php echo $status; ?>
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="CstmSdLog_formid">
	    <lable>采购单号</lable>
	    <input type="text" name="VipJitPickList[po_no]" class="easyui-textbox" style="width:12%" id="poNo"/>&nbsp;&nbsp;&nbsp;&nbsp;
        <lable>拣货单号</lable>
        <input type="text" name="VipJitPickList[pick_no]" class="easyui-textbox" style="width:12%" id="pickNo"/>&nbsp;&nbsp;&nbsp;&nbsp;
        <lable>拣货仓</lable>
        <input type="text" name="VipJitPickList[warehouse_code]" class="easyui-textbox" style="width:12%" id="warehouseCode"/>&nbsp;&nbsp;&nbsp;&nbsp;<p></p>
        <lable>运单号</lable>
        <input type="text" name="VipJitPickList[delivery_no]" class="easyui-textbox" style="width:12%" id="deliveryNo"/>&nbsp;&nbsp;&nbsp;&nbsp;
        <lable>入库单号</lable>
        <input type="text" name="VipJitPickList[storage_no]" class="easyui-textbox" style="width:12%" id="storageNo"/>&nbsp;&nbsp;&nbsp;&nbsp;
        <lable>状态：</lable>
		<select name="VipJitPickList[status]" id="status" class="easyui-combobox" editable="false" style="width: 12%;">
			<option value=''>全部</option>
			<option value='已下发'>已下发</option>
			<option value='已拣货'>已拣货</option>
			<option value='已发货'>已发货</option>
			<option value='已送货'>已送货</option>
		</select><p></p>
		<lable>供应商：</lable>
		<input type="text" name="VipJitPickList[vendor_id]" class="easyui-textbox" style="width: 12%;" id="vendorId" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>品牌：</lable>
		<input type="text" name="VipJitPickList[brand_name]" class="easyui-textbox" style="width: 12%;" id="brandName" />&nbsp;&nbsp;&nbsp;&nbsp;
        <lable>唯品会仓库：</lable>
        <input type="text" name="VipJitPickList[sell_site]" class="easyui-textbox" style="width: 12%;" id="sellSite" />&nbsp;&nbsp;&nbsp;&nbsp;
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchForm()" style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="exportDg"></div>
<script type="text/javascript" src="./static/js/moudles/outbound/outbound-vipJitPickList.js"></script>