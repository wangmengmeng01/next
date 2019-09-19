<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/39');
//获取登陆者是否有操作权限
$operateFlag = util::isHasPri(39.1);
?>
<script type="text/javascript">
	var updateUrl = '<?php echo $this->createUrl('cskSellerWaybillInfo/update', array('id'=>'uid'));?>';
	var operateFlag = <?php echo $operateFlag; ?>;
	var reloadUrl = '<?php echo $this->createUrl('cskSellerWaybillInfo/reload', array('id'=>'sellerId')); ?>';
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="WaybillInfo_formid">
		<label>商家ID：</label>
		<input type="text" name="WaybillInfo[seller_id]" class="easyui-textbox" style="width: 8%;" id="sellerId" />&nbsp;&nbsp;&nbsp;&nbsp;
		<label>仓库地址编码：</label>
		<input type="text" name="WaybillInfo[ship_addr_code]" class="easyui-textbox" style="width: 8%;" id="shipAddrCode" />&nbsp;&nbsp;&nbsp;&nbsp;
		<label>详细地址：</label>
		<input type="text" name="WaybillInfo[ship_detail_address]" class="easyui-textbox" style="width: 12%;" id="shipAddressDetail" />&nbsp;&nbsp;<br><br>
		<label>快递公司编码：</label>
		<input type="text" name="WaybillInfo[cp_code]" class="easyui-textbox" style="width: 100px;" id="cpCode" />&nbsp;&nbsp;&nbsp;&nbsp;
		<label>网点编码：</label>
		<input type="text" name="WaybillInfo[branch_code]" class="easyui-textbox" style="width: 100px;" id="branchCode" />&nbsp;&nbsp;&nbsp;&nbsp;
		<label>网点名称：</label>
		<input type="text" name="WaybillInfo[branch_name]" class="easyui-textbox" style="width: 12%;" id="branchName" />&nbsp;&nbsp;&nbsp;&nbsp;
        <label>电子面单类型:</label>
        <select name="WaybillInfo[is_jd]" class="easyui-combobox" id="is_jd" style="width: 90px">
            <option value="all">全部</option>
            <option value="1">京东</option>
            <option value="">菜鸟</option>
            <option value="2">拼多多</option>
        </select>
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-search'" onClick="searchForm()"
			style="font-weight: bold;margin-left: 20px;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript"
	src="./static/js/moudles/base/base-cskSellerWaybillInfo.js"></script>