<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/33');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(33.1);

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
	
	    <label>货主名称：</label>
		<input type="text" name="CustomerShipments[customer_name]" class="easyui-textbox" style="width: 10%;" id="customerName" />&nbsp;&nbsp;&nbsp;&nbsp;
	    <label>货主编码：</label>
		<input type="text" name="CustomerShipment
		s[customer_id]" class="easyui-textbox" style="width: 10%;" id="customerId" />&nbsp;&nbsp;&nbsp;&nbsp;
	    <label>网点名称：</label>
		<input type="text" name="CustomerShipments[gs_name]" class="easyui-textbox" style="width: 10%;" id="gsName" />&nbsp;&nbsp;&nbsp;&nbsp;
	    <label>网点编码：</label>
		<input type="text" name="CustomerShipments[gs_code]" class="easyui-textbox" style="width: 10%;" id="gsCode" />&nbsp;&nbsp;&nbsp;&nbsp;
	    <p/>
	    <label>仓库名称：</label>
		<input type="text" name="CustomerShipments[warehouse_name]" class="easyui-textbox" style="width: 10%;" id="warehouseName" />&nbsp;&nbsp;&nbsp;&nbsp;
	    <label>仓库编码：</label>
		<input type="text" name="CustomerShipments[warehouse_code]" class="easyui-textbox" style="width: 10%;" id="warehouseCode" />&nbsp;&nbsp;&nbsp;&nbsp;
	    <label>省份：</label>
	    <select id="province" name="CustomerShipments[province]" editable="false" class="easyui-combobox" style="width:8%;">
	        <option value=''>-请选择-</option>
	        <?php foreach ($province as $sheng) {?>
		    <option value="<?php echo $sheng['provinceid'];?>"><?php echo $sheng['provincename'];?></option>
		    <?php } ?>			
	    </select>&nbsp;&nbsp;&nbsp;&nbsp;
		<label>使用系统：</label>
		<select id="wmsName" name="CustomerShipments[wms_name]" editable="false" class="easyui-combobox" style="width: 8%;">
	        <option value=''>-请选择-</option>
	        <?php foreach ($wmsInfo as $wms) {?>
			<option value="<?php echo $wms['wms_code'];?>"><?php echo $wms['wms_name'];?></option>
			<?php }?>
	    </select>&nbsp;&nbsp;&nbsp;&nbsp;
		<label>开始时间：</label>
		<input name="CustomerShipments[start_time]" class="easyui-datebox" style="width: 150px" id="startTime" value="<?php echo date('Y-m-d', strtotime("-1 day"));?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<label>结束时间：</label>
		<input name="CustomerShipments[end_time]" class="easyui-datebox" style="width: 150px" id="endTime" value="<?php echo date('Y-m-d', strtotime("-1 day"));?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-search'" onClick="searchForm()"
			style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript" src="./static/js/moudles/outbound/outbound-customerShipments.js"></script>
