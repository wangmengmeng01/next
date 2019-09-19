<h1>你的访问出错了!</h1>
<?php

//if(defined('YII_DEBUG')&&YII_DEBUG==true){
	$err = Yii::app()->errorHandler->error;
	$str = '<div>信息如下：</div>';
	$str .= '<p>URL：'.Yii::app()->request->requestUri.'</p>';
	$str .= '<p>代码(code):'.$err['code'].'</p>';
	$str .= '<p>类型(type):'.$err['type'].'</p>';
	$str .= '<p>错误消息:'.$err['message'].'</p>';
	$str .= '<p>错误代码:'.$err['errorCode'].'</p>';
	$str .= '<p>文件:'.$err['file'].'</p>';
	$str .= '<p>行数:'.$err['line'].'</p>';
	$str .= '<p>trace信息:'.$err['trace'].'</p>';
	$str .= '<p>traces:<pre>'.print_r($err['traces'],true).'</pre></p>';
	echo $str;
//}
Yii::app()->end();
