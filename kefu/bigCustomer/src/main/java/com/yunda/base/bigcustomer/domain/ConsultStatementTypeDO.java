package com.yunda.base.bigcustomer.domain;

import java.io.Serializable;

/**
 * @program: bigCustomer->ConsultStatementTypeDO
 * @author: luzhiwei
 * @email: luzhiwei8794@yundasys.com
 * @create: 2019-07-25 11:03
 * @description: 咨询类型和结单结果关联表
 */
public class ConsultStatementTypeDO implements Serializable {

    private static final long serialVersionUID = -3869358618316869015L;

    //序号
    private Integer id;

    //咨询类型
    private String consulType;

    //结单结果
    private String statementResult;

    //可用 0-不可用 1-可用
    private String status;

    public ConsultStatementTypeDO() {
    }

    public ConsultStatementTypeDO(Integer id, String consulType, String statementResult, String status) {
        this.id = id;
        this.consulType = consulType;
        this.statementResult = statementResult;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConsulType() {
        return consulType;
    }

    public void setConsulType(String consulType) {
        this.consulType = consulType;
    }

    public String getStatementResult() {
        return statementResult;
    }

    public void setStatementResult(String statementResult) {
        this.statementResult = statementResult;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ConsultStatementTypeDO{" +
                "id=" + id +
                ", consulType='" + consulType + '\'' +
                ", statementResult='" + statementResult + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
