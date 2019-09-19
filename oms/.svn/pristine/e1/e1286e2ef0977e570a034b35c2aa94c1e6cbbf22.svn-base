<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/83');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(83.1);
?>
<script type="text/javascript">
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
	var selfreq = '<?php echo VIP_OMS_SELF_REQ_SECRET;?>';
	var vendorId = '<?php echo VIP_VENDOR_ID;?>';
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="CstmSdLog_formid">
	    <lable>采购单号</lable>
	    <input type="text" name="VipJitPoList[po_no]" class="easyui-textbox" style="width:12%" id="poNo"/>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>供应商：</lable>
		<input type="text" name="VipJitPoList[vendor_name]" class="easyui-textbox" style="width: 12%;" id="vendorId" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>品牌：</lable>
		<input type="text" name="VipJitPoList[brand_name]" class="easyui-textbox" style="width: 12%;" id="brandName" />&nbsp;&nbsp;&nbsp;&nbsp;<p></p>
        <lable>档期名称：</lable>
        <input type="text" name="VipJitPoList[schedule_name]" class="easyui-textbox" style="width: 12%;" id="scheduleName" />&nbsp;&nbsp;&nbsp;&nbsp;
        <lable>唯品会仓库：</lable>
        <input type="text" name="VipJitPoList[sell_site]" class="easyui-textbox" style="width: 12%;" id="sellSite" />&nbsp;&nbsp;&nbsp;&nbsp;
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchForm()" style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="exportDg"></div>
<script type="text/javascript" src="./static/js/moudles/outbound/outbound-vipJitPoList.js"></script>