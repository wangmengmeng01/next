<?php
/**
 * Created by PhpStorm.
 * User: zhaoyan
 * Date: 2019/6/25
 * Time: 7:41 PM
 */
require dirname(__FILE__) . '/init.php';
$act = isset($_POST['act']) ? $_POST['act'] : 'index';

if ($act == 'index') {
    $real_name = isset($_POST['real_name']) ? $_POST['real_name'] : '';
    $job_num = isset($_POST['job_num']) ? $_POST['job_num'] : '';
    $status = isset($_POST['status']) ? $_POST['status'] : '';

    yd_json(200, 'success', [
        ['id' => '123', 'role_name' => '123', 'remark' => '123123', 'updatetime' => '123123', 'status' => mt_rand(0,1)],
        ['id' => '2', 'role_name' => '123', 'remark' => '123123', 'updatetime' => '123123', 'status' => mt_rand(0,1)],
        ['id' => '3', 'role_name' => '123', 'remark' => '123123', 'updatetime' => '123123', 'status' => mt_rand(0,1)],
        ['id' => '4', 'role_name' => '123', 'remark' => '123123', 'updatetime' => '123123', 'status' => mt_rand(0,1)],
        ['id' => '5', 'role_name' => '123', 'remark' => '123123', 'updatetime' => '123123', 'status' => mt_rand(0,1)]
    ]);
} else if ($act == 'status') {
    yd_json(200, 'success');
} else if ($act == 'edit') {
    yd_json(200, 'success');
} else if ($act == 'create') {
    yd_json(200, 'success');
} else if ($act == 'delete') {
    yd_json(200, 'success');
} else if ($act == 'powers') {
    yd_json(200, 'success', [
        ['power_id' => '1', 'power_name' => '第一个'],
        ['power_id' => '2', 'power_name' => '第二个'],
        ['power_id' => '3', 'power_name' => '第三个'],
        ['power_id' => '4', 'power_name' => '第四个'],
        ['power_id' => '5', 'power_name' => '第五个']
    ]);
} else if ($act == 'info') {
    yd_json(200, 'success', [
        'role_name' => 'afsdfasdf',
        'remark' => 'asdfasdf',
        'status' => 'asdfsdf'
    ]);
}