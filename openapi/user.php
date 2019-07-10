<?php
/**
 * Created by PhpStorm.
 * User: zhaoyan
 * Date: 2019/6/25
 * Time: 7:41 PM
 */
require dirname(__FILE__) . '/init.php';

$act = isset($_POST['act']) ? $_POST['act'] : 'index';
// echo json_encode($_POST);
if ($act == 'index') {
    $real_name = isset($_POST['real_name']) ? $_POST['real_name'] : '';
    $job_num = isset($_POST['job_num']) ? $_POST['job_num'] : '';
    $status = isset($_POST['status']) ? $_POST['status'] : '';

    yd_json(200, 'success', [
        ['id' => '1','real_name' => 'asdfsdaf','job_num' => 'sadfasfd','updatetime' => date('Y-m-d H:i:s'),'status' => mt_rand(0,1),'role_id' => mt_rand(0,1),'mobile' => '1231234123','email' => 'asdfafd@mail.com'],
        ['id' => '2','real_name' => '1111','job_num' => 'sadfasfd','updatetime' => date('Y-m-d H:i:s'),'status' => mt_rand(0,1),'role_id' => mt_rand(0,1),'mobile' => '1231234123','email' => 'asdfafd@mail.com'],
        ['id' => '3','real_name' => '2222','job_num' => 'sadfasfd','updatetime' => date('Y-m-d H:i:s'),'status' => mt_rand(0,1),'role_id' => mt_rand(0,1),'mobile' => '1231234123','email' => 'asdfafd@mail.com'],
        ['id' => '4','real_name' => '333','job_num' => 'sadfasfd','updatetime' => date('Y-m-d H:i:s'),'status' => mt_rand(0,1),'role_id' => mt_rand(0,1),'mobile' => '1231234123','email' => 'asdfafd@mail.com'],
        ['id' => '5','real_name' => '4444','job_num' => 'sadfasfd','updatetime' => date('Y-m-d H:i:s'),'status' => mt_rand(0,1),'role_id' => mt_rand(0,1),'mobile' => '1231234123','email' => 'asdfafd@mail.com']
    ]);
} else if ($act == 'status') {
    yd_json(200, 'success');
} else if ($act == 'edit') {
    yd_json(200, 'success');
} else if ($act == 'create') {
    yd_json(200, 'success');
} else if ($act == 'delete') {
    yd_json(200, 'success');
} else if ($act == 'update_password') {
    yd_json(200, 'success');
} else if ($act == 'info') {
    yd_json(200, 'success', [
        'real_name' => 'afsdfasdf',
        'job_num' => 'asdfasdf',
        'updatetime' => 'asdfasdf',
        'status' => 'asdfsdf',
        'role_id' => 'asdfasdf',
        'mobile' => 'asdfasdf',
        'email' => 'asdfasdf'
    ]);
}