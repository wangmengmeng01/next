<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/41');
//获取登陆者是否有操作权限
$operateFlag = util::isHasPri(41.1);
?>
<script type="text/javascript">
	var updateUrl = '<?php echo $this->createUrl('cskShipAddress/update', array('id'=>'uid'));?>';
	var deleteUrl = '<?php echo $this->createUrl('cskShipAddress/delete');?>';
	var operateFlag = <?php echo $operateFlag; ?>;
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="Addr_formid">
		<lable>仓库地址编码：</lable>
		<input type="text" name="Addr[ship_addr_code]" class="easyui-textbox" style="width: 12%;" id="shipAddrCode" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>发件地址：</lable>
		<input type="text" name="Addr[ship_address]" class="easyui-textbox" style="width: 18%;" id="shipAddress" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>有效性：</lable>
		<select name="Addr[is_valid]" id="isValid" class="easyui-combobox" editable="false" style="width: 150px;">
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
	src="./static/js/moudles/base/base-cskShipAddress.js"></script>