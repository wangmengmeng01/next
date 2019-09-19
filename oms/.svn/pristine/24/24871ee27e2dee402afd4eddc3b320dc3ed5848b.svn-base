<?php
/**
 * Created by PhpStorm.
 * User: 20171012
 * Date: 2018/4/24
 * Time: 10:28
 */
class filterKjUpdateStatus extends msg{
    public function update(&$requestData){
        //校验数据是否为空
        if (empty($requestData)) {
            return $this->outputCustom(false,'请求报文不能为空！');
        }
        //校验海关审核状态
        if (!isset($requestData['status'])) {
            return $this->outputCustom(false,'海关审核状态不能为空！');
        }
        //校验仓库ID
        if (empty($requestData['whCode'])) {
            return $this->outputCustom(false,'仓库ID不能为空！');
        }
        //校验时间
        if (empty($requestData['time'])) {
            return $this->outputCustom(false,'时间不能为空！');
        }
        //校验申报单号
        if (empty($requestData['declareNo'])) {
            return $this->outputCustom(false,'申报单号不能为空！');
        }
        //校验通过
        return $this->outputCustom(true,'成功');
    }
}