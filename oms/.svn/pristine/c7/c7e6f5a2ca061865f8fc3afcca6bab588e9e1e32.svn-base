<?php
/**
 * 奇门数据字典获取接口过滤类
 * @author Renee
 *
 */
class filterMetadataQuery extends msg {
    public function query(&$requestData) {
        if (empty($requestData)) {
            return $this->outputQimen('failure', 'body中数据不能为空', 'S003');
        }
        if (empty($requestData['metaDataCategory'])) {
            return  $this->outputQimen('failure', '获取的数据类型不能为空', 'S003');
        }
        return $this->outputQimen('success');
    }
}
?>