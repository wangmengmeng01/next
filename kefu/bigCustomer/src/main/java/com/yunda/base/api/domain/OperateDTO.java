package com.yunda.base.api.domain;

import java.io.Serializable;
import java.util.List;

import com.yunda.base.bigcustomer.domain.ConsultFileDO;
import com.yunda.base.bigcustomer.domain.OperateDO;

/**
 * @program: bigCustomer->OperateDTO
 * @author: luzhiwei
 * @email: luzhiwei8794@yundasys.com
 * @create: 2019-07-26 19:21
 * @description: 操作记录详细封装类
 */
public class OperateDTO implements Serializable {

    private static final long serialVersionUID = -2367447509461949696L;

    /**
     * 操作记录封装类
     */
    private OperateDO operateDO;
    /**
     * 文件封装类
     */
    private List<ConsultFileDO> consultFileDOList;

    public OperateDTO() {
    }

    public OperateDTO(OperateDO operateDO, List<ConsultFileDO> consultFileDOList) {
        this.operateDO = operateDO;
        this.consultFileDOList = consultFileDOList;
    }

    public OperateDO getOperateDO() {
        return operateDO;
    }

    public void setOperateDO(OperateDO operateDO) {
        this.operateDO = operateDO;
    }

    public List<ConsultFileDO> getConsultFileDOList() {
        return consultFileDOList;
    }

    public void setConsultFileDOList(List<ConsultFileDO> consultFileDOList) {
        this.consultFileDOList = consultFileDOList;
    }

    @Override
    public String toString() {
        return "OperateDTO{" +
                "operateDO=" + operateDO +
                ", consultFileDOList=" + consultFileDOList +
                '}';
    }
}
