<?php
/**
 * 出库单报检确认接口过滤类
 * User: Renee
 * Date: 2018/1/18
 * Time: 15:44
 */
class filterKjSoDecl extends msg{
    public function decl(&$requestData){
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputCustom(false,'请求报文不能为空！');
        }
        //校验货主编码
        if (empty($requestData['storer'])) {
            return $this->outputCustom(false,'货主编码不能为空！');
        }
        //校验仓库代码
        if (empty($requestData['wmwhseid'])) {
            return $this->outputCustom(false,'仓库代码不能为空！');
        }
        //校验跨境平台申报单号
        if (empty($requestData['externalNo'])) {
            return $this->outputCustom(false,'跨境平台申报单号不能为空！');
        }
        //校验明细行项总数
        if (empty($requestData['tdq'])) {
            return $this->outputCustom(false,'明细行项总数不能为空！');
        }

        //校验通过
        return $this->outputCustom(true,'成功');
    }
}