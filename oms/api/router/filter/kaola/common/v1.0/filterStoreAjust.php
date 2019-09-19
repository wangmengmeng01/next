<?php
/**
 * Created by PhpStorm.
 * User: Renee
 * Date: 2018/6/15
 * Time: 14:51
 */

class filterStoreAjust extends msg{
    public function ajust(&$requestData)
    {
        //判断请求数据不能为空
        if (empty($requestData)) {
            return $this->outputKaola(false,'请求数据为空！');
        }

        //校验盘点单号不能为空
        if (empty($requestData['check_id'])) {
            return $this->outputKaola(false,'盘点单号不能为空！');
        }

        return $this->outputKaola(true,'校验成功！');
    }
}