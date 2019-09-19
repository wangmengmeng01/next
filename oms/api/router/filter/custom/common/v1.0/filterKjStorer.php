<?php
/**
 * 货主接口过滤类
 * User: Renee
 * Date: 2018/1/18
 * Time: 15:34
 */
class filterKjStorer extends msg{
    public function create(&$requestData){
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputCustom(false,'请求报文不能为空！');
        }
        //校验货主代码
        if (empty($requestData['storer'])) {
            return $this->outputCustom(false,'货主代码不能为空！');
        }
        //校验仓库代码
        if (empty($requestData['wmwhseid'])) {
            return $this->outputCustom(false,'仓库代码不能为空！');
        }
        //校验通过
        return $this->outputCustom(true,'成功');
    }
}