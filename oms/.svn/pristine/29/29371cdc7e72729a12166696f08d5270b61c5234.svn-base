<?php
/**
 * Description: 网易考拉 仓库回调推送理货报告当前处理状态
 * Date: 2018-05-10 16:53
 * Created by XL.
 */

class filterTallyStatusInfo extends msg
{

    public function status($params)
    {

        if (empty($params)) {

            return $this->outputKaola(false, '仓库回调推送理货报告当前处理状态：请求数据不能为空');
        }

        /*# 提运单号
        if (empty($params['bill_no'])) {

            return $this->outputKaola(false, '提运单号不能为空');
        }

        # 理货单号
        if (empty($params['storage_tally_num'])) {

            return $this->outputKaola(false, '理货单号不能为空');
        }

        # 转运单号
        if (empty($params['trick_no'])) {

            return $this->outputKaola(false, '转运单号不能为空');
        }

        # 审批单号
        if (empty($params['approval_no'])) {

            return $this->outputKaola(false, '审批单号不能为空');
        }

        # 审放单号
        if (empty($params['nuclear_no'])) {

            return $this->outputKaola(false, '审放单号不能为空');
        }*/


        return $this->outputKaola(true, '');
    }
}