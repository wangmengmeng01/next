package com.yunda.base.api.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @program: bigCustomer->RoleDTO
 * @author: luzhiwei
 * @email: luzhiwei8794@yundasys.com
 * @create: 2019-09-02 19:15
 * @description:
 */
public class RoleDTO implements Serializable {
    private Long roleId;
    private String roleName;
    private String roleSign;
    private String remark;
    private Long userIdCreate;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
    private List<Long> menuIds;
    //状态
    private String state;
    //上级角色编码
    private String upRoleId;
    //上级角色名称
    private String upRoleName;
    //数据权限
    private String dataPermissions;
    private String orgCode;

    public RoleDTO() {
    }

    public RoleDTO(Long roleId, String roleName, String roleSign, String remark, Long userIdCreate, Timestamp gmtCreate, Timestamp gmtModified, List<Long> menuIds, String state, String upRoleId, String upRoleName, String dataPermissions, String orgCode) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleSign = roleSign;
        this.remark = remark;
        this.userIdCreate = userIdCreate;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
        this.menuIds = menuIds;
        this.state = state;
        this.upRoleId = upRoleId;
        this.upRoleName = upRoleName;
        this.dataPermissions = dataPermissions;
        this.orgCode = orgCode;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleSign() {
        return roleSign;
    }

    public void setRoleSign(String roleSign) {
        this.roleSign = roleSign;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getUserIdCreate() {
        return userIdCreate;
    }

    public void setUserIdCreate(Long userIdCreate) {
        this.userIdCreate = userIdCreate;
    }

    public Timestamp getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Timestamp getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Timestamp gmtModified) {
        this.gmtModified = gmtModified;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Long> menuIds) {
        this.menuIds = menuIds;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUpRoleId() {
        return upRoleId;
    }

    public void setUpRoleId(String upRoleId) {
        this.upRoleId = upRoleId;
    }

    public String getUpRoleName() {
        return upRoleName;
    }

    public void setUpRoleName(String upRoleName) {
        this.upRoleName = upRoleName;
    }

    public String getDataPermissions() {
        return dataPermissions;
    }

    public void setDataPermissions(String dataPermissions) {
        this.dataPermissions = dataPermissions;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    @Override
    public String toString() {
        return "RoleDTO{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", roleSign='" + roleSign + '\'' +
                ", remark='" + remark + '\'' +
                ", userIdCreate=" + userIdCreate +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", menuIds=" + menuIds +
                ", state='" + state + '\'' +
                ", upRoleId='" + upRoleId + '\'' +
                ", upRoleName='" + upRoleName + '\'' +
                ", dataPermissions='" + dataPermissions + '\'' +
                ", orgCode='" + orgCode + '\'' +
                '}';
    }
}
