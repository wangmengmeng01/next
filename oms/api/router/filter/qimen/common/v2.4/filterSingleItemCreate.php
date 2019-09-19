<?php
/**
 * 商品同步接口过滤类
 * @author Renee
 */
class filterSingleItemCreate extends msg
{
	public function create(&$requestData)
	{
	    //连接数据库
		global $db;
		//校验数据是否为空
		if (empty($requestData)) {
			return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
		}

		$item = $requestData['item'];
		//校验操作类型
		if (empty($requestData['actionType'])) {
		    return $this->outputQimen('failure', '操作类型不能为空', 'S003');
		} else {
		    //判断操作类型是否正确
		    if (!in_array($requestData['actionType'], array("add","update"))) {
		        return $this->outputQimen('failure', '操作类型错误或者无效', 'S003');
		    }
		}
	    /*
		//校验仓库
		if (empty($requestData['warehouseCode'])) {
		    return $this->outputQimen('failure', '仓库编码不能为空', 'S003');
		}
		*/
		//校验货主
		if (empty($requestData['ownerCode'])) {
		    return $this->outputQimen('failure', '货主编码不能为空', 'S003');
		} else {
	        $querySql = "SELECT customer_id FROM t_base_customer WHERE customer_id=:customer_id AND active_flag='Y' AND is_valid=1";
	        $model = $db->prepare($querySql);
	        $model->bindParam(':customer_id', $requestData['ownerCode']);
	        $model->execute();
	        $rs = $model->fetch(PDO::FETCH_ASSOC);
	        if (empty($rs)) {
	            return $this->outputQimen('failure', '货主ID不存在或者无效', 'S003');
	        } else {
	            if ($rs['customer_id'] != $requestData['ownerCode']) {
    	            return $this->outputQimen('failure', '货主ID大小写错误', 'S003');
	            }
	        }
		}
		//校验商品编码
		if (empty($item['itemCode'])) {
		    return $this->outputQimen('failure', '商品编码不能为空', 'S003');
		}
		//校验商品名称
		if (empty($item['itemName'])) {
		    return $this->outputQimen('failure', '商品名称不能为空', 'S003');
		}
		/*
		//校验条形码
		if (empty($item['barCode'])) {
		    return $this->outputQimen('failure', '条形码不能为空', 'S003');
		} 
		*/
		//校验商品类型                         
		$types = array('ZC','FX','ZH','ZP','BC','HC','FL','XN','FS','CC','OTHER');
		if (empty($item['itemType'])) {
            return $this->outputQimen('failure', '商品类型不能为空', 'S003');
		} else {
		    if (!in_array($item['itemType'], $types)) {
		        return $this->outputQimen('failure', '该商品类型' . $item['itemType'] . '不存在', 'S003');
		    }
		}
		return $this->outputQimen('success');
	}
}