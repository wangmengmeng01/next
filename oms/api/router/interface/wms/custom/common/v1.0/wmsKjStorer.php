<?php
/**
 * 跨境仓储货主接口
 * User: Renee
 * Date: 2018/1/18
 * Time: 15:49
 */
require API_ROOT . 'router/interface/wms/custom/common/wmsRequest.php';
class wmsKjStorer extends wmsRequest {
    /**
     * 货主接口数据处理
     * @param $params
     * @return array
     */
    public function create($params){
        if (empty($params)) {
            return $this->msgObj->outputCustom('false', '失败：请求的数据为空!');
        } else {
            try {
                $response = $this->send();
                if (!empty($response)) {
                    if ($response['success'] == 'true') {
                        if ($this->checkInfo($params['storer'],$params['wmwhseid'])) {
                            $this->updateInfo($params);
                        } else {
                            $this->insertInfo($params);
                        }
                        return $this->msgObj->outputCustom('true', $response['reasons'],$response['addon']);
                    } else {
                        return $this->msgObj->outputCustom('false', $response['reasons'],$response['addon']);
                    }
                } else {
                    return $this->msgObj->outputCustom('false', $response['reasons'],$response['addon']);
                }
            } catch (Exception $e){
                return $this->msgObj->outputCustom('false', $e->getMessage());
            }
        }
    }

    /**
     * 判断货主仓库信息是否存在
     * @param $storer
     * @param $whId
     * @return bool
     */
    public function checkInfo($storer,$whId){
        global $db;

        $sql = "SELECT id FROM t_kj_storer WHERE storer=:storer AND wmwhseid=:wmwhseid;";
        $model = $db->prepare($sql);
        $model->bindParam(':storer',$storer);
        $model->bindParam(':wmwhseid',$whId);
        $model->execute();
        $rs = $model->fetch(PDO::FETCH_ASSOC);
        if (empty($rs)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 插入货主仓库信息
     * @param $params
     * @return bool
     */
    public function insertInfo($params){
        global $db;

        $storerDbRelation = $this->getDbRelation('cnec_wh_1');
        $storer_key_info = implode(',', array_values($storerDbRelation)) . ',create_time';
        $storer_value_info = ":" . implode(",:", array_values($storerDbRelation)). ',now()';

        $sql = 'INSERT INTO t_kj_storer('.$storer_key_info.') VALUES('.$storer_value_info.');';
        $model = $db->prepare($sql);
        $values = array();
        foreach ($storerDbRelation as $s_k=>$s_v) {
            $values[':'.$s_v] = empty($params[$s_k]) ? "" : $params[$s_k];
        }
        $model->execute($values);
        return true;
    }

    /**
     * 更新货主仓库信息
     * @param $params
     * @return bool
     */
    public function updateInfo($params){
        global $db;

        $sql = "UPDATE t_kj_storer SET company=:company WHERE storer=:storer AND wmwhseid=:wmwhseid";
        $model = $db->prepare($sql);
        $model->bindParam(':company',$params['company']);
        $model->bindParam(':storer',$params['storer']);
        $model->bindParam(':wmwhseid',$params['wmwhseid']);
        $model->execute();
        return true;
    }
}