<?php
/**
 * 销售订单内部类
 * User:  wp
 */
require_once API_ROOT . '/router/interface/wms/common/wmsRequestInner.php';
class wmsOrderDeliveryInner extends wmsRequestInner
{

	public function __construct()
	{
		parent::__construct();
	}

	/**
	 * wms货主推送
	 * @param $params
	 * @return mixed
	 */
	public function push($params)
	{
		$method = inner_service::$_methodTo;
		return $this->send($method, array('xmldata' => array('header' => $params)));
	}

}