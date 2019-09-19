<?php
/**
 * 考拉子母件获取运单号处理类
 * User: 20171012
 * Date: 2018/5/23
 * Time: 14:28
 */

class KlGetBillNo
{
    /**
     * 请求并根据返回处理数据
     * @param $params 报文
     * @return array  返回网易报文格式
     */
    public function get($params)
    {
        $rsArr = array(
            'code'    => 400,
        );
        if (empty($params)) {
            $rsArr['message'] = '执行错误：请求数据为空！';
            return json_encode($rsArr);
        } else {
            try {
                $reqParam = json_decode($params,true);
                $ownerId = $reqParam['ownerId'];
                $stockId = $reqParam['stockId'];

                $partnerInfo = OmsDatabase::$oms_db->fetchOne('notes','t_kj_storer','storer=:storer AND wmwhseid=:wmwhseid AND is_auto=0',array(':storer'=>$ownerId,':wmwhseid'=>$stockId));

                if (empty($partnerInfo['notes'])) {
                    $rsArr['message'] = '未找到仓库对应的partnerCode！';
                    return json_encode($rsArr);
                }

                $reqParams = array(
                    'partnerCode'=>$partnerInfo['notes'],
                    'sign'=>base64_encode(md5($params.KAOLA_GETBILLNO_KEY)),
                    'data'=>$params,
                );

                $httpObj = new httpclient();
                $rsp = $httpObj->post(KAOLA_GETBILLNO_URL, $reqParams);

                if (empty($rsp) || $rsp == '-3' || $rsp == NULL) {
                    $rsArr['message'] = '请求超时！';
                    $rsp = json_encode($rsArr);
                }
                kaola_service::$_rsMsg = $rsp;

                return $rsp ;
            } catch (Exception $e) {
                $rsArr['message'] = $e->getMessage();
                return json_encode($rsArr);
            }
        }
    }
}