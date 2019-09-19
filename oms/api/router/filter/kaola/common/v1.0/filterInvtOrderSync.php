<?php
/**
 * 网易考拉清单信息同步接口
 * User: Renee
 * Date: 2018/8/20
 * Time: 15:17
 */
class filterInvtOrderSync extends msg{
    public function sync(&$requestData)
    {
        //判断请求数据不能为空
        if (empty($requestData)) {
            return $this->outputKaola(false,'请求数据为空！');
        }

        return $this->outputKaola(true,'校验成功！');
    }
}