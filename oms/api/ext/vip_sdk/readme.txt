唯品会开放平台（vop.vip.com）SDK使用说明书

1）内容说明

┬ src/Osp 	底层通讯协议源码
└ src/* 	服务帮助类源码

2）使用说明

直接将src目录下所有内容引入到项目中即可。

3）关键字段说明

appkey:创建应用时生成，为接口调用凭证之一
appsecret：创建应用时生成，为接口调用凭证之一
accessToken：通过Oauth认证授权时生成，参考：http://vop.vip.com/doccenter/viewdoc/33
sign:调用签名，建议在异常捕获中记录该值，可以提高开放平台定位异常的效率，具体生成规则参考：http://vop.vip.com/doccenter/viewdoc/8#A4

4）调用示例

将sdk引入到项目中后，可根据以下demo测试应用访问权限和连通性。

<?php

require_once 'vipapis/address/AddressServiceClient.php';

$ctx = null;
try {
	//1、获取服务客户端
	$service = \vipapis\address\AddressServiceClient::getService();
	
	//2、设置系统级调用参数，只需在程序开始调用前设置一次即可
	$ctx = \Osp\Context\InvocationContextFactory::getInstance ();
	$ctx->setAppKey("appKey");//替换为你的appKey
	$ctx->setAppSecret("appSecret");//替换为你的appSecret
	//$ctx->setAccessToken("accessToken");//替换为你的accessToken，通过Oauth认证时必填
	$ctx->setAppURL("http://sandbox.vipapis.com/");//沙箱环境
	//$ctx->setAppURL("https://gw.vipapis.com/");//正式环境
	//$ctx->setTimeOut(30)//超时时间，可选，默认30秒
	
	//3、调用API及返回
	$rtn = $service->getProvinceWarehouse(\vipapis\address\Is_Show_GAT::SHOW_ALL);
	var_dump($rtn);
} catch (\Osp\Exception\OspException $e) {
	//4、捕获异常
	var_dump($ctx->getSign());//获取最近一次调用的sign值
}

5)FAQ

a.调用失败？

i.对于有明确返回错误信息的失败调用，请根据错误信息提示操作；
ii.对于原因不明的失败，请通过开放平台的支持中心搜索解决方案或提交问题进行报障：https://vop.vip.com/support/index

b.网络环境不稳定或超时？

i.如果是正式环境，请确认调用参数AppUrl是否为https://gw.vipapis.com
ii.若业务参数包含分页数据，请按照API在线文档（https://vop.vip.com/apicenter/index）的建议大小设置每页数据大小，一般建议每页数据在100以内
iii.其他疑难问题，请通过开放平台的支持中心提交问题进行报障：https://vop.vip.com/support/index

c.sdk如何升级？

可到开放平台下载sdk最新版本，：https://vop.vip.com/doccenter/viewdoc/20
建议下载新的sdk以后，替换src目录下所有内容，以保证sdk正常运行。