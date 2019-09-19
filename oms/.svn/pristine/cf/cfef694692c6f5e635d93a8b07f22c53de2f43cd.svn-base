<?php
/**
 * Description: 推送理货单审核状态给仓库
 * Date: 2018-05-10 17:19
 * Created by XL.
 */

class filterPushAuditStatus extends msg
{

    public function status($params)
    {

        if (empty($params)) {
            return $this->outputKaola(false,'请求数据为空！');
        }

        # 仓库理货单id
        if (empty($params['storage_tally_num'])) {

            return $this->outputKaola(false, '仓库理货单id不能为空');
        }

        # 理货审核状态结果
        if (empty($params['status'])) {

            return $this->outputKaola(false, '理货审核状态结果不能为空');
        }


        return $this->outputKaola(true,'成功');
    }
}