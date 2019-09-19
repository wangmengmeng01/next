<?php
/**
 * OMS
 * 基于Yii framework 1.1.15 开发
 * @version 1.0
 * @copyright 2015.03
 */

header('Content-Type:text/html;charset=utf-8');
header("Cache-Control: no-cache, must-revalidate"); # HTTP/1.1
header("Expires: Mon, 26 Jul 1997 05:00:00 GMT"); #过去的时间
ini_set('session.cookie_httponly', '1');
date_default_timezone_set('Asia/Shanghai');
define('YII_FRAMEWORK_PATH', dirname(__DIR__) . DIRECTORY_SEPARATOR . 'framework');
define('APP_ROOT', __DIR__);
define('APP_URL', 'http://' . $_SERVER['HTTP_HOST'] . substr($_SERVER['PHP_SELF'], 0, strrpos($_SERVER['PHP_SELF'], '/') + 1));

define('YII_DEBUG', false);
define('YII_TRACE_LEVEL', 4);

require 'config.php';
require APP_ROOT . '/protected/ext/Crypt/RC4.php';

$config = APP_ROOT . '/protected/config/main.php';


require_once(YII_FRAMEWORK_PATH . DIRECTORY_SEPARATOR . 'yii.php');

$yii = Yii::createWebApplication($config);


//rc4解密get参数
$rc4 = new RC4();
$rc4->setKey('dXx29EOcQ8DHma1o');
$data = json_decode($rc4->decrypt(base64_decode($_GET['sid'])), 1);
// print_r($data);die;

// $data = base64_encode($rc4->encrypt('deliveryOrderShortag'));


Yii::app()->session->clear();
//客户
if ($data['userType'] == 'customer') {
    //获取角色列表
    $role_query = Yii::app()->db->createCommand();
    $role_query->select('user_domain,user_role_list,user_type');
    $role_query->from('oms.t_domain_user');
    $role_query->where('user_id=:user_id and user_status=:user_status', array(':user_id' => $data['userId'], ':user_status' => 1));
    $role_query->limit(1);
    $role = $role_query->queryRow();


    //超级管理员拥有所有权限
    if ($role['user_type'] == '1') {
        //查询符合角色列表的权限以及用户信息
        $user_query = Yii::app()->db->createCommand();
        $user_query->select('A.user_id,A.user_name,A.user_domain,A.user_role_list,A.user_permission,A.user_type');
        $user_query->from('oms.t_domain_user A');
        // $user_query->leftjoin('oms.t_domain_role B',"B.domain_code = A.user_domain and B.id in(".$role['user_role_list'].")");
        $user_query->where('A.user_id=:user_id and A.user_type=:user_type', array(':user_id' => $data['userId'], ':user_type' => $role['user_type']));
        $user = $user_query->queryAll();
        //获取老OMS对应权限
        $pri_query = Yii::app()->db->createCommand();
        $pri_query->select('pri_id');
        $pri_query->from('oms.t_domain_menu');
        $pri = $pri_query->queryAll();

        $pri_id = '';
        foreach ($pri as $k => $v) {
            if ($v['pri_id'] > 0) {
                $pri_id .= $v['pri_id'] . ',';
            }
        }
        $pri_id = substr($pri_id, 0, -1);

        $identity = new CUserIdentity($user[0]['user_id'], date('Y-m-d H:i:s'));
        Yii::app()->user->login($identity, 0);
        Yii::app()->user->setState('soa_id', $user[0]['user_id']);
        Yii::app()->user->setState('user_title', $user[0]['user_name']);

        Yii::app()->user->setState('user_role', 'customer');
        Yii::app()->user->setState('user_all_pri', $pri_id);
        //登录方式
        Yii::app()->user->setState('login_type', 'ADMIN');
    } else {
        //查询符合角色列表的权限以及用户信息
        $user_query = Yii::app()->db->createCommand();
        $user_query->select('A.user_id,A.user_name,A.user_domain,A.user_role_list,A.user_permission,A.user_type,B.menu_permission,B.customer_permission,B.ware_permission');
        $user_query->from('oms.t_domain_user A');
        $user_query->leftjoin('oms.t_domain_role B', "B.domain_code = A.user_domain and B.id in(" . $role['user_role_list'] . ")");
        $user_query->where('A.user_id=:user_id and A.user_type=:user_type', array(':user_id' => $data['userId'], ':user_type' => $role['user_type']));
        $user = $user_query->queryAll();
        //菜单权限
        $menu_permission = array();
        //角色数据 货主权限
        $customer_permission = array();
        //仓库权限
        $ware_permission = array();
        //权限合并去重
        foreach ($user as $k => $v) {
            $menu_permission = array_merge(explode(',', $v['menu_permission']), $menu_permission);
            $customer_permission = array_merge(explode(',', $v['customer_permission']), $customer_permission);
            $ware_permission = array_merge(explode(',', $v['ware_permission']), $ware_permission);
        }

        $menu_permission = implode(',', array_unique($menu_permission));
        $customer_permission = implode(',', array_unique($customer_permission));
        $ware_permission = implode(',', array_unique($ware_permission));
        //获取老OMS对应权限
        $pri_query = Yii::app()->db->createCommand();
        $pri_query->select('pri_id');
        $pri_query->from('oms.t_domain_menu');
        $pri_query->where("id in ($menu_permission)");
        $pri = $pri_query->queryAll();

        $pri_id = '';
        foreach ($pri as $k => $v) {
            if ($v['pri_id'] > 0) {
                $pri_id .= $v['pri_id'] . ',';
            }
        }
        $pri_id = substr($pri_id, 0, -1);
        //用户信息 权限 存session
        $identity = new CUserIdentity($user[0]['user_id'], date('Y-m-d H:i:s'));
        Yii::app()->user->login($identity, 0);
        Yii::app()->user->setState('soa_id', $user[0]['user_id']);
        Yii::app()->user->setState('user_title', $user[0]['user_name']);
        // Yii::app()->user->setState('menu_permission',$menu_permission);
        Yii::app()->user->setState('customer_permission', $customer_permission);
        Yii::app()->user->setState('ware_permission', $ware_permission);

        Yii::app()->user->setState('user_role', 'customer');
        Yii::app()->user->setState('user_all_pri', $pri_id);
        //登录方式
        Yii::app()->user->setState('login_type', 'OMS');
        // print_r(str_replace(",", "','", Yii::app()->user->getState('customer_permission')));die;
        // print_r($_SESSION);die;
    }
}

//人员
if ($data['userType'] == 'user') {
    $query = Yii::app()->db->createCommand();
    $query->select('A.USERPASS,A.EMPID,B.gh_oa,B.name,C.bm,C.mc,C.lb');
    $query->from('ydserver.yd_cas_emp A');
    $query->leftjoin('ydserver.ry B', 'A.EMPID=B.gh_oa');
    $query->leftjoin('ydserver.gs C', 'B.gs=C.bm');
    $query->where('A.EMPID=:id', array(':id' => $data['userId']));
    $query->limit(1);
    $rows = $query->queryRow();

    $identity = new CUserIdentity($rows['gh_oa'], date('Y-m-d H:i:s'));
    Yii::app()->user->login($identity, 0);
    Yii::app()->user->setState('soa_id', $rows['EMPID']);
    Yii::app()->user->setState('user_title', $rows['name']);
    Yii::app()->user->setState('gsbm', $rows['bm']);
    Yii::app()->user->setState('gsmc', $rows['mc']);
    Yii::app()->user->setState('gslb', $rows['lb']);
    //获取登陆者的角色
    $ry = new Ry;
    Yii::app()->user->setState('user_role', $ry->getUserRole());
    //获取登陆者的权限位
    Yii::app()->user->setState('user_all_pri', $ry->getUserAllPri());
}
//页面跳转
$url = "./index.php?r=" . $data['module'] . "/" . $data['controller'] . "/" . $data['action'];
// print_r($url);die;
header("Location: $url");
// $yii->run();


?>