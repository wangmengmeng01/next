<?php
/**
 * Created by PhpStorm.
 * User: zhaoyan
 * Date: 2019/6/25
 * Time: 7:40 PM
 */

require dirname(__FILE__) . '/init.php';

$act = isset($_POST['act']) ? $_POST['act'] : 'index';

if ($act == 'index') {

    $app_name = isset($_POST['app_name']) ? $_POST['app_name'] : '';
    $app_code = isset($_POST['app_code']) ? $_POST['app_code'] : '';
    $app_status = isset($_POST['app_status']) ? $_POST['app_status'] : '';

    yd_json(200, 'success', [
        ['id' => 1, 'app_name' => 'sdafsdf', 'app_code' => 'asdfasdfasdf', 'remark' => 'asdfasdfasdf', 'app_status' => mt_rand(0,1), 'updatetime' => date('Y-m-d H:i:s')],
        ['id' => 2, 'app_name' => '12313', 'app_code' => 'asdfasdfasdf', 'remark' => 'asdfasdfasdf', 'app_status' => mt_rand(0,1), 'updatetime' => date('Y-m-d H:i:s')],
        ['id' => 4, 'app_name' => '123123', 'app_code' => 'asdfasdfasdf', 'remark' => 'asdfasdfasdf', 'app_status' => mt_rand(0,1), 'updatetime' => date('Y-m-d H:i:s')],
        ['id' => 5, 'app_name' => '123123', 'app_code' => 'asdfasdfasdf', 'remark' => 'asdfasdfasdf', 'app_status' => mt_rand(0,1), 'updatetime' => date('Y-m-d H:i:s')],
        ['id' => 6, 'app_name' => '123123', 'app_code' => 'asdfasdfasdf', 'remark' => 'asdfasdfasdf', 'app_status' => mt_rand(0,1), 'updatetime' => date('Y-m-d H:i:s')]
    ]);

} else if ($act == 'status') {
    yd_json(200, 'success');
} else if ($act == 'edit') {
    yd_json(200, 'success');
} else if ($act == 'create') {
    yd_json(200, 'success');
} else if ($act == 'info') {
    yd_json(200, 'success', [
        'app_name' => 'asdfasdf',
        'app_code' => 'asdfasdf',
        'remark' => 'asdfasdfasdf',
        'app_status' => mt_rand(0,1)
    ]);
}