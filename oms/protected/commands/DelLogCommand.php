<?php
    class DelLogCommand extends CConsoleCommand {
        
        /**
         * 每天执行一次，删除15天前日志
         */
        public function actionDelete (){
        	
        	//只有每天早上4点之后执行一次
        	$searchDate = date("Y-m-d");
        	if (date('H:i') < '04:00') {
        		die('time error');
        	} else {
            	$logFileName = ROOT_DIR . 'protected/runtime/uploadFiles/delLog.log';
            	if (file_exists($logFileName)) {
            		$lastTime = file_get_contents($logFileName);
            		if ($lastTime != $searchDate) {
            			file_put_contents($logFileName, $searchDate);
            		} else {
            			die('date is exists');
            		}
            	} else {
            		file_put_contents($logFileName, $searchDate);
            	}
            }
        	
            $bTime = date("Y-m-d 00:00:00",strtotime("-15 day"));
            
            $delSql = "DELETE FROM t_api_log WHERE create_time<'$bTime';
                       DELETE FROM t_customer_interface_log WHERE create_time<'$bTime';
                       DELETE FROM t_product_interface_log WHERE create_time<'$bTime';
                       DELETE FROM t_order_interface_log WHERE create_time<'$bTime';
                       DELETE FROM csk_interface_log WHERE create_time<'$bTime';
                       DELETE FROM pdd_interface_log WHERE create_time<'$bTime';";
            
            $db = Yii::app()->db;
            $model = $db->createCommand($delSql);
            $model->execute();
            echo 'OK';
        }
        
    }
?>