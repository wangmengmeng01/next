<?php
/**
 * Created by PhpStorm.
 * User: zhaoyan
 * Date: 2019/6/25
 * Time: 7:40 PM
 */

function yd_json($code, $message, $data) {

    $arr = [
        'code' => $code,
        'message' => $message,
        'data' => $data
    ];

    echo json_encode($arr);
    exit(0);
}