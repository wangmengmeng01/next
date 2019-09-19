<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/34');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(34.1);

$db = Yii::app()->db;

$sqlProvince = 'SELECT provinceid,provincename FROM ydserver.province';
$model = $db->createCommand($sqlProvince);
$province = $model->query();

$sqlWms = 'SELECT wms_code,wms_name FROM t_base_wms';
$model = $db->createCommand($sqlWms);
$wmsInfo = $model->query();
?>
<script type="text/javascript">
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
</script>
<style type="text/css">
    .combo-panel {
      height:228px;
      overflow: auto;
    }
</style>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="CstmSdLog_formid">
	    <lable>仓库名称：</lable>
		<input type="text" name="WarehouseShipments[warehouse_name]" class="easyui-textbox" style="width: 12%;" id="warehouseName" />&nbsp;&nbsp;&nbsp;&nbsp;
	    <lable>仓库编码：</lable>
		<input type="text" name="WarehouseShipments[warehouse_code]" class="easyui-textbox" style="width: 12%;" id="warehouseCode" />&nbsp;&nbsp;&nbsp;&nbsp;
	    <lable>网点名称：</lable>
		<input type="text" name="WarehouseShipments[gs_name]" class="easyui-textbox" style="width: 12%;" id="gsName" />&nbsp;&nbsp;&nbsp;&nbsp;
	    <lable>网点编码：</lable>
		<input type="text" name="WarehouseShipments[gs_code]" class="easyui-textbox" style="width: 12%;" id="gsCode" />&nbsp;&nbsp;&nbsp;&nbsp;
	    <p/>
	    <lable>省份：</lable>
	    <select id="province" name="WarehouseShipments[province]" editable="false" class="easyui-combobox" style="width: 8%;">
	        <option value=''>-请选择-</option>
	        <?php foreach ($province as $sheng) {?>
		    <option value="<?php echo $sheng['provinceid'];?>"><?php echo $sheng['provincename'];?></option>
		    <?php } ?>			
	    </select>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>使用系统：</lable>
		<select id="wmsName" name="WarehouseShipments[wms_name]" editable="false" class="easyui-combobox" style="width: 8%;">
	        <option value=''>-请选择-</option>
	        <?php foreach ($wmsInfo as $wms) {?>
			<option value="<?php echo $wms['wms_code'];?>"><?php echo $wms['wms_name'];?></option>
			<?php }?>
	    </select>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>开始时间：</lable>
		<input name="WarehouseShipments[start_time]" class="easyui-datebox" style="width: 150px" id="startTime" value="<?php echo date('Y-m-d', strtotime("-1 day"));?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>结束时间：</lable>
		<input name="WarehouseShipments[end_time]" class="easyui-datebox" style="width: 150px" id="endTime" value="<?php echo date('Y-m-d', strtotime("-1 day"));?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-search'" onClick="searchForm()"
			style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript" src="./static/js/moudles/outbound/outbound-warehouseShipments.js"></script>
