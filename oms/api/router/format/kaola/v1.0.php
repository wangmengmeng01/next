<?php
/**
 * Created by PhpStorm.
 * User: 20171012
 * Date: 2018/5/4
 * Time: 15:20
 */

class format
{
    /**
     * 请求参数转换
     * @param $request
     * @return SimpleXMLElement
     */
    public function request($requestData)
    {
        return json_decode($requestData,true);
    }

}