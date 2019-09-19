<?php
/**
 * 网易报关系统核放单接口
 * User: Renee
 * Date: 2018/10/30
 * Time: 13:36
 */
class ReviewRelease {
    public function postData(&$request)
    {
        $data = $request['msg'];
        $apiParams= array(
            'userid' => REVIEWRELEASE_USERID  ,
            'msg'    => $data,
            'sign'   => REVIEWRELEASE_SIGN,
        );

        try {
            $utilObj = new util();
            $resp = $utilObj->postForm(REVIEWRELEASE_URL,$apiParams);

            if (empty($resp)) {
                $resp = '<response><result>F</result><returnNo></returnNo><resultMsg>请求超时</resultMsg></response>';
            }

            return $resp;
        } catch (Exception $e) {
            return '<response><result>F</result><returnNo></returnNo><resultMsg>Exception:'.$e->getMessage().'</resultMsg></response>';
        }
    }
}