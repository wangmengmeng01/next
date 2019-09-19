<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/16');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(16.1);
?>
<script type="text/javascript">
	var orderId = '<?php echo $_GET['id'] ?>';
	var orderNo = '<?php echo $_GET['name'] ?>';
	var recordId = '<?php echo $_GET['rid'] ?>';
	var createTime = '<?php echo $_GET['ctime'] ?>';
	var materialViewUrl = '<?php echo Yii::app()->createUrl('outbound/outboundRecordMaterialPackage/index', array('id'=>'uid')); ?>';
	var productViewUrl = '<?php echo Yii::app()->createUrl('outbound/outboundRecordProductPackage/index', array('id'=>'uid')); ?>';
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
</script>
<div id="dg"></div>
<div id="remark_div"></div>
<script type="text/javascript" src="./static/js/moudles/outbound/outbound-outboundRecordPackage.js"></script>