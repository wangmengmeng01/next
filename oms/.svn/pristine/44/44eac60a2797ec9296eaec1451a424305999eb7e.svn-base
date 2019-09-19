<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/13');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(13.1);
?>
<script type="text/javascript">
    var orderId = '<?php echo $_GET['id'] ?>';
	var recordId = '<?php echo $_GET['rid'] ?>';
	var orderNo = '<?php echo $_GET['name'] ?>';
	var createTime = '<?php echo $_GET['ctime'] ?>';
	var deatilViewUrl = '<?php echo Yii::app()->createUrl('inbound/inboundRecordBatchDetail/index', array('id'=>'uid')); ?>';
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
</script>
<div id="dg"></div>
<div id="remark_div"></div>
<script type="text/javascript" src="./static/js/moudles/inbound/inbound-inboundRecodeDetail.js"></script>