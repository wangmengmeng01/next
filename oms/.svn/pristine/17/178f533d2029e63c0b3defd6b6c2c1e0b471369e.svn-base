<?php
/**
 * 仓库回调推送理货报告详情信息接口过滤类
 * User: Renee
 * Date: 2018/5/10
 * Time: 13:20
 */
class filterTallyConfirm extends msg{
    public function confirm(&$requestData)
    {
        if (empty($requestData)) {
            return $this->outputKaola(false,'请求数据为空！');
        }

        return $this->outputKaola(true,'校验成功！');
    }
}