<?php
/**
 * 商品资料下发推送接口过滤类
 * User: Renee
 * Date: 2018/5/7
 * Time: 17:26
 */
class filterSkuCreate extends msg{
    //过滤方法
    public function create(&$requestData)
    {
        //判断转换后的数据是否为空
        if (empty($requestData)) {
            return $this->outputKaola(false,'请求数据为空！');
        }

        //循环校验
        foreach ($requestData as $data) {
            //校验商品编码是否为空
            if (empty($data['sku_id'])) {
                return $this->outputKaola(false,'skuid不能为空！');
            }
        }

        return $this->outputKaola(true,'校验成功！');
    }
}