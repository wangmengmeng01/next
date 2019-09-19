<?php
/**
 * Notes:承运商配置
 * Date: 2019/4/29
 * Time: 15:47
 */

//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/87');
//获取登陆者是否有操作权限
$operateFlag = util::isHasPri(87.1);
?>
<script type="text/javascript">
    var updateUrl = '<?php echo $this->createUrl('cskMerchantDeploy/update', array('id'=>'uid'));?>';
    var operateFlag = <?php echo $operateFlag; ?>;
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="Seller_formid">
		<lable>商家编码：</lable>
		<input type="text" name="vendor_code" class="easyui-textbox" style="width: 12%;" id="vendor_code" />&nbsp;&nbsp;&nbsp;&nbsp;
        <lable>承运商编码：</lable>
        <input type="text" name="provider_code" class="easyui-textbox" style="width: 12%;" id="provider_code" />&nbsp;&nbsp;&nbsp;&nbsp;
        <lable>WMS承运商编码：</lable>
        <input type="text" name="wms_provider_code" class="easyui-textbox" style="width: 12%;" id="wms_provider_code" />&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-search'" onClick="searchForm()"
			style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript"
	src="./static/js/moudles/base/base-cskMerchantDeploy.js"></script>