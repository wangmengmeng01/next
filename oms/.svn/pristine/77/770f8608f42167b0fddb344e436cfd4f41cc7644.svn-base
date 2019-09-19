<?php
/**
 * 奇门数据字典更新接口
 * @author Renee
 *
 */
class filterMetaDataUpdate extends msg {
    public function update(&$requestData) {
        if (empty($requestData)) {
            $this->outputQimen('failure', 'body中数据不能为空', 'S003');
        }
        if (empty($requestData['metaDataCategory'])) {
            $this->outputQimen('failure', '获取的数据类型不能为空', 'S003');
        }
        return $this->outputQimen('success');
    }
}