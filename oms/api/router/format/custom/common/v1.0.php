<?php
/**
 * 标准参数格式转换
 * Created by PhpStorm.
 * User: Renee
 * Date: 2018/1/3
 * Time: 11:03
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
        $requestData = trim($requestData,"\n\r");
        $xmlObj = new xml();
        //将xml转换为数组
        $requestData = $xmlObj->xmlStr2array($requestData);
        //过滤数组中的空数组
        $utilObj = new util();
        $requestData = $utilObj->filter_null($requestData);
        return $requestData;
    }

}