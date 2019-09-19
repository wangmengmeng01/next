<?php
define('ROOT_DIR', dirname(realpath(__FILE__)) . '/../');
require ROOT_DIR . 'config.php';

//输出队列进程数
$threadList = array();
for($i=1; $i<=STORAGE_THREAD_NUM; $i++) {
    $threadList[] = $i;
}

echo implode(' ', $threadList);