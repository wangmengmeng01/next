<?php
/**
 * 组合商品接口过滤类
 * @author Renee
 */
class filterCombineItemCreate extends msg
{
	public function create(&$requestData)
	{
		//连接数据库
		global $db;
		//校验数据是否为空
		if (empty($requestData)) {
		    return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
		}
		$request = $requestData;
		//校验商品编码
		if (empty($request['itemCode'])) {
		    return $this->outputQimen('failure', '组合商品的ERP编码不能为空', 'S003');
		} 
		//校验货主
		if (empty($request['ownerCode'])) {
		    return $this->outputQimen('failure', '货主编码不能为空', 'S003');
		} else {
	        $sql = "SELECT customer_id FROM t_qimen_customer_bind WHERE customer_id=:customer_id AND is_valid=1";
	        $model = $db->prepare($sql);
	        $model->bindParam(':customer_id', $request['ownerCode']);
	        $model->execute();
	        $rs = $model->fetch(PDO::FETCH_ASSOC);
	        if (empty($rs)) {
	            return $this->outputQimen('failure', '货主ID不存在或者无效', 'S003');
	        } else {
	            if ($rs['customer_id'] != $request['ownerCode']) {
    	            return $this->outputQimen('failure', '货主ID大小写错误', 'S003');
	            }
	        }
		}
		//校验仓库
		if (empty($request['warehouseCode'])) {
		    return $this->outputQimen('failure', '仓库编码不能为空', 'S003');
		}
		//校验组合商品中的商品明细
		if (empty($request['items'])) {
		    return $this->outputQimen('failure', '商品明细不能为空', 'S003');
		} else {
            if (empty($request['items']['item'][0])) {
                $request['items']['item'] = array($request['items']['item']);
            }
		    foreach ($request['items']['item'] as $val) {
		        if (empty($val['itemCode'])) {
		            return $this->outputQimen('failure', '商品明细中商品编码不能为空', 'S003');
		        } else {
		            $sql = "SELECT sku FROM t_base_product WHERE sku=:sku AND customer_id=:customer_id AND warehouse_code=:warehouse_code AND active_flag='Y' AND is_valid=1";
		            $model = $db->prepare($sql);
		            $model->bindParam(':sku', $val['itemCode']);
		            $model->bindParam(':customer_id', $request['ownerCode']);
		            $model->bindParam(':warehouse_code', $request['warehouseCode']);
		            $model->execute();
		            $rs = $model->fetch(PDO::FETCH_ASSOC);
		            if (empty($rs)) {
		                return $this->outputQimen('failure', '商品明细中商品不存在或无效', 'S003');
		            }
		        }
		        if (empty($val['quantity'])) {
		            return $this->outputQimen('failure', '商品明细中商品个数不能为空', 'S003');
		        }
		    }
		}
		return $this->outputQimen('success');
	}	
}