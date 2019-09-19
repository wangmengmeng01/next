<?php
/**
 * 承运商接口 (接收申报系统发送的承运商资料)
 * Created by PhpStorm.
 * User: xl
 * Date: 2018-01-08
 * Time: 10:00
 */

require_once API_ROOT . '/router/interface/wms/custom/common/wmsRequest.php';

class wmsKjCarrierInterface extends wmsRequest
{

    public function create($param)
    {

        try {
            if (empty($param)) {
                return $this->msgObj->outputCustom(false, '失败:请求数据为空');
            }


            # 转发承运商资料数据给WMS
            $response = $this->send();

            # 检测数据转发是否成功
            if (!$response['success']) {

                return $this->msgObj->outputCustom(false, '承运商资料接口：' . $response['reasons'], $response['addon']);
            }


            # 检测承运商资料是否已存在，（存在更新，不存在新增）
            global $db;
            $sql = 'SELECT carrier FROM t_kj_carrier WHERE carrier = \'' . $param['carrier'] . '\' LIMIT 1;';
            $model = $db->prepare($sql);
            $model->execute();
            $carrier = $model->fetch(PDO::FETCH_ASSOC);

            # 插入最新承运商资料
            if (empty($carrier)) {

                $sql = 'INSERT INTO t_kj_carrier
                                   (`carrier`,`wmwhseid`,`company`,`desce`,`address1`,
                                    `address2`,`city`,`province`,`postcode`,`contact`,
                                    `contactPhone`,`contactFax`,`notes`,`create_time`
                                   )
                                    VALUE (
                                    :carrier,:wmwhseid,:company,:desce,:address1,
                                    :address2,:city,:province,:postcode,:contact,
                                    :contactPhone,:contactFax,:notes,now()
                                  )';

                # 语句准备
                $model = $db->prepare($sql);

                # 预处理数据绑定并执行保存
                if ($model->execute($param)) {

                    return $this->msgObj->outputCustom(true, '承运商资料转发并保存成功', $response['addon']);
                }

                return $this->msgObj->outputCustom(false, '承运商资料转发成功，但保存失败', $response['addon']);

            } else {

                # 更新承运商信息
                $sql = 'UPDATE t_kj_carrier set wmwhseid = :wmwhseid, company = :company, desce  = :desce,
                                                address1 = :address1, address2 = :address2, city = :city,
                                                province = :province, postcode = :postcode, contact = :contact,
                                                contactPhone = :contactPhone, contactFax = :contactFax, notes = :notes
                                            WHERE carrier = :carrier';

                # 语句准备
                $model = $db->prepare($sql);

                #预处理数据并执行更新
                if ($model->execute($param)) {

                    return $this->msgObj->outputCustom(true, '承运商资料转发并更新成功', $response['addon']);
                }

                return $this->msgObj->outputCustom(false, '承运商资料转发成功，但更新失败', $response['addon']);

            }

        } catch (Exception $e) {

            return $this->msgObj->outputCustom(false, $e->getMessage());
        }

    }
}

