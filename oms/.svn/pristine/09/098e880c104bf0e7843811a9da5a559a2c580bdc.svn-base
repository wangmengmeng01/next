<script type="text/jscript">
	function submit() {
		$("#importFormId").submit();
	}
</script>
<?php 
   //校验是否有查看权限
   Yii::app()->runController('site/CheckPri/pos/15.2');
   $resultInfo = '';
   if (isset($successNum) || isset($errorInfo)) {
       $errorNum = 0;
       $errorStr = '';
       if (!empty($errorInfo)) {
       	   $errorNum = count($errorInfo);
       	   foreach ($errorInfo as $key => $val)
       	   {
       	   	   $errorStr .= "第" . ($key+2) . "行：" . $val . "\n";      	   	  
       	   }
       }
       $resultInfo = "成功导入" . $successNum . "个订单，失败" . $errorNum . "个订单\n";
       if ($errorNum > 0) {
       	   $resultInfo .=  "失败原因：\n" . $errorStr;
       }
       
   }
?>
<div style="padding-left: 10px;">
	<form method="post"	action="<?php echo Yii::app()->createUrl('base/import/import')?>" enctype="multipart/form-data" id="importFormId">
		<h3>1、下载Excel导入模板：</h3>
		请点击&nbsp;<b><a class='ddl' href="<?php echo APP_URL.'/template/oms-wms.xls';?>" title="点击下载导入模板">下载订单导入模板</b></a>
		<p></p>
		<p></p>
		<h3>2、导入Excel文件：</h3>
		<input class="easyui-filebox" name="importFile" id="importFile" data-options="prompt:'请选择需要导入的excel文件',buttonText:'浏览文件'" style="width: 250px;">&nbsp;&nbsp;&nbsp;&nbsp;
		<a class="easyui-linkbutton" iconCls="icon-ok" onclick="submit()">提交</a>
		<p></p>
		<h3>导入结果：</h3>
		<textarea id="result_Outbound" rows=15 cols=40  class="textarea easyui-validatebox"><?php echo $resultInfo; ?></textarea>
	</form>
</div>