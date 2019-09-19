<?php
/**
 * Notes:考拉授权码信息同步接口字段信息校验
 * Date: 2019/2/12
 * Time: 13:36
 */

class filterGetKaolaStoreInfo extends msg
{

    public function update($param)
    {
        # 请求字段信息校验
        if (empty($param)) {
            return $this->outputCustom(false, '请求数据内容不能为空');
        }

        # 店铺海关备案ID
        if (empty($param['StoreIDForKeepOn'])) {
            return $this->outputCustom(false, '店铺海关备案ID不能为空');
        }

        # access_token
        if (empty($param['StoreInfo']['access_token'])) {
            return $this->outputCustom(false, 'access_token不能为空');
        }

        # Kaola_key
        if (empty($param['StoreInfo']['Kaola_key'])) {
            return $this->outputCustom(false, 'Kaola_key不能为空');
        }

        # Kaola_secert
        if (empty($param['StoreInfo']['Kaola_secert'])) {
            return $this->outputCustom(false, 'Kaola_secert不能为空');
        }

        # 校验通过
        return $this->outputCustom(true, '成功');
    }
}