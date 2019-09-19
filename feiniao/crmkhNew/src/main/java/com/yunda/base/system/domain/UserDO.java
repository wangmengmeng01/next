package com.yunda.base.system.domain;

import com.github.crab2died.annotation.ExcelField;
import com.yunda.ydmbspringbootstarter.common.annotation.ValidCertifId;
import com.yunda.ydmbspringbootstarter.common.annotation.ValidMac;
import com.yunda.ydmbspringbootstarter.common.annotation.ValidMobile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;


@ApiModel(value = "用户维护表")
public class UserDO implements Serializable,UserDetails {

    private static final long serialVersionUID = 1L;
    
    // 用户名
    //@ExcelField(title = "账号", order = 1)
    @ApiModelProperty(value = "账号")
    @NotBlank(message = "账号不能为空")
    private String username;
    // 密码
   // @ExcelField(title = "密码", order = 2)
    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;
    //序号
    private Long userId;
 // 用户真实姓名
    @ApiModelProperty(value = "姓名")
    @NotBlank(message = "姓名不能为空")
    private String name;
    // 部门
    private Long deptId;
    private String deptName;
    // 邮箱
    private String email;
    // 手机号
    @ApiModelProperty(value = "手机号")
    @ValidMobile
    @NotBlank(message = "手机号不能为空")
    private String mobile;
    // 状态 0:禁用，1:正常
    @ApiModelProperty(value = "使用状态")
    private Integer status;
    // 创建用户id
    private Long userIdCreate;
    // 创建时间
    private Date gmtCreate;
    // 修改时间
    @ApiModelProperty(value = "修改时间")
    private Date gmtModified;
    //身份证
    @ValidCertifId
    @ApiModelProperty(value = "身份证",required = true)
    @NotBlank(message = "身份证不能为空")
    private String idcdNo;
    //mac地址
    @ApiModelProperty(value = "mac地址",required = true)
    @NotBlank(message = "mac地址不能为空")
    @ValidMac
    private String macAdress;
    //修改人
    @ApiModelProperty(value = "修改人",required = true)
    private String updateName;



    //性别
    private Long sex;
    //出身日期
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;
    //图片ID
    private Long picId;
    //现居住地
    private String liveAddress;
    //爱好
    private String hobby;
    //省份
    private String province;
    //所在城市
    private String city;
    //所在地区
    private String district;
    //用户面容编码
    private Object base64;
    
    
  //机构(网点)编码,名称,类型
    private String orgCode;
	private String orgName;
	private String orgType;
	
	/////////////////////////域字段
	public Set<String> perms;
	//管辖省份的id
    private List<Long> provinceIds;
    //管辖大区
    private List<String> bigareaNames;
    //角色
    private List<Long> roleIds;
    //当前用户的安全等级
    private String safeLevel;
    
	
	public String getSafeLevel() {
		return safeLevel;
	}

	public void setSafeLevel(String safeLevel) {
		this.safeLevel = safeLevel;
	}

	//用户是否拥有指定的权限
	public boolean hasPerms(String perm){
		if(perms!=null && perms.size()>0){
			return perms.contains(perm);
		}
		return false;
	}
	
	public boolean isProvinceqx(){
        return provinceIds != null && provinceIds.size() > 0;
    }
	public boolean isBigareaqx(){
        return bigareaNames != null && bigareaNames.size() > 0;
    }
	
	public Set<String> getPerms() {
		return perms;
	}

	public void setPerms(Set<String> perms) {
		this.perms = perms;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public List<String> getBigareaNames() {
		return bigareaNames;
	}

	public void setBigareaNames(List<String> bigareaNames) {
		this.bigareaNames = bigareaNames;
	}

	public List<Long> getProvinceIds() {
		return provinceIds;
	}

	public void setProvinceIds(List<Long> provinceIds) {
		this.provinceIds = provinceIds;
	}

	public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getUserIdCreate() {
        return userIdCreate;
    }

    public void setUserIdCreate(Long userIdCreate) {
        this.userIdCreate = userIdCreate;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public Long getSex() {
        return sex;
    }

    public void setSex(Long sex) {
        this.sex = sex;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Long getPicId() {
        return picId;
    }

    public void setPicId(Long picId) {
        this.picId = picId;
    }

    public String getLiveAddress() {
        return liveAddress;
    }

    public void setLiveAddress(String liveAddress) {
        this.liveAddress = liveAddress;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
    


	public Object getBase64() {
		return base64;
	}

	public void setBase64(Object base64) {
		this.base64 = base64;
	}


    public String getIdcdNo() {
        return idcdNo;
    }


    public void setIdcdNo(String idcdNo) {
        this.idcdNo = idcdNo;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

    public String getMacAdress() {
        return macAdress;
    }

    public void setMacAdress(String macAdress) {
        this.macAdress = macAdress;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((base64 == null) ? 0 : base64.hashCode());
		result = prime * result + ((bigareaNames == null) ? 0 : bigareaNames.hashCode());
		result = prime * result + ((birth == null) ? 0 : birth.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((deptId == null) ? 0 : deptId.hashCode());
		result = prime * result + ((deptName == null) ? 0 : deptName.hashCode());
		result = prime * result + ((district == null) ? 0 : district.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((gmtCreate == null) ? 0 : gmtCreate.hashCode());
		result = prime * result + ((gmtModified == null) ? 0 : gmtModified.hashCode());
		result = prime * result + ((hobby == null) ? 0 : hobby.hashCode());
		result = prime * result + ((idcdNo == null) ? 0 : idcdNo.hashCode());
		result = prime * result + ((liveAddress == null) ? 0 : liveAddress.hashCode());
		result = prime * result + ((macAdress == null) ? 0 : macAdress.hashCode());
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((orgCode == null) ? 0 : orgCode.hashCode());
		result = prime * result + ((orgName == null) ? 0 : orgName.hashCode());
		result = prime * result + ((orgType == null) ? 0 : orgType.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((perms == null) ? 0 : perms.hashCode());
		result = prime * result + ((picId == null) ? 0 : picId.hashCode());
		result = prime * result + ((province == null) ? 0 : province.hashCode());
		result = prime * result + ((provinceIds == null) ? 0 : provinceIds.hashCode());
		result = prime * result + ((roleIds == null) ? 0 : roleIds.hashCode());
		result = prime * result + ((safeLevel == null) ? 0 : safeLevel.hashCode());
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((updateName == null) ? 0 : updateName.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((userIdCreate == null) ? 0 : userIdCreate.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDO other = (UserDO) obj;
		if (base64 == null) {
			if (other.base64 != null)
				return false;
		} else if (!base64.equals(other.base64))
			return false;
		if (bigareaNames == null) {
			if (other.bigareaNames != null)
				return false;
		} else if (!bigareaNames.equals(other.bigareaNames))
			return false;
		if (birth == null) {
			if (other.birth != null)
				return false;
		} else if (!birth.equals(other.birth))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (deptId == null) {
			if (other.deptId != null)
				return false;
		} else if (!deptId.equals(other.deptId))
			return false;
		if (deptName == null) {
			if (other.deptName != null)
				return false;
		} else if (!deptName.equals(other.deptName))
			return false;
		if (district == null) {
			if (other.district != null)
				return false;
		} else if (!district.equals(other.district))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (gmtCreate == null) {
			if (other.gmtCreate != null)
				return false;
		} else if (!gmtCreate.equals(other.gmtCreate))
			return false;
		if (gmtModified == null) {
			if (other.gmtModified != null)
				return false;
		} else if (!gmtModified.equals(other.gmtModified))
			return false;
		if (hobby == null) {
			if (other.hobby != null)
				return false;
		} else if (!hobby.equals(other.hobby))
			return false;
		if (idcdNo == null) {
			if (other.idcdNo != null)
				return false;
		} else if (!idcdNo.equals(other.idcdNo))
			return false;
		if (liveAddress == null) {
			if (other.liveAddress != null)
				return false;
		} else if (!liveAddress.equals(other.liveAddress))
			return false;
		if (macAdress == null) {
			if (other.macAdress != null)
				return false;
		} else if (!macAdress.equals(other.macAdress))
			return false;
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (orgCode == null) {
			if (other.orgCode != null)
				return false;
		} else if (!orgCode.equals(other.orgCode))
			return false;
		if (orgName == null) {
			if (other.orgName != null)
				return false;
		} else if (!orgName.equals(other.orgName))
			return false;
		if (orgType == null) {
			if (other.orgType != null)
				return false;
		} else if (!orgType.equals(other.orgType))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (perms == null) {
			if (other.perms != null)
				return false;
		} else if (!perms.equals(other.perms))
			return false;
		if (picId == null) {
			if (other.picId != null)
				return false;
		} else if (!picId.equals(other.picId))
			return false;
		if (province == null) {
			if (other.province != null)
				return false;
		} else if (!province.equals(other.province))
			return false;
		if (provinceIds == null) {
			if (other.provinceIds != null)
				return false;
		} else if (!provinceIds.equals(other.provinceIds))
			return false;
		if (roleIds == null) {
			if (other.roleIds != null)
				return false;
		} else if (!roleIds.equals(other.roleIds))
			return false;
		if (safeLevel == null) {
			if (other.safeLevel != null)
				return false;
		} else if (!safeLevel.equals(other.safeLevel))
			return false;
		if (sex == null) {
			if (other.sex != null)
				return false;
		} else if (!sex.equals(other.sex))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (updateName == null) {
			if (other.updateName != null)
				return false;
		} else if (!updateName.equals(other.updateName))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (userIdCreate == null) {
			if (other.userIdCreate != null)
				return false;
		} else if (!userIdCreate.equals(other.userIdCreate))
			return false;
		if (username == null) {
            return other.username == null;
		} else return username.equals(other.username);
    }

	@Override
	public String toString() {
		return "UserDO [userId=" + userId + ", username=" + username + ", name=" + name + ", password=" + password + ", deptId=" + deptId + ", deptName=" + deptName + ", email=" + email + ", mobile=" + mobile + ", status=" + status + ", userIdCreate=" + userIdCreate + ", gmtCreate=" + gmtCreate + ", gmtModified=" + gmtModified + ", idcdNo=" + idcdNo + ", macAdress=" + macAdress + ", updateName=" + updateName + ", sex=" + sex + ", birth=" + birth + ", picId=" + picId + ", liveAddress=" + liveAddress + ", hobby=" + hobby + ", province=" + province + ", city=" + city + ", district=" + district + ", base64=" + base64 + ", orgCode=" + orgCode + ", orgName=" + orgName + ", orgType=" + orgType + ", perms=" + perms + ", provinceIds=" + provinceIds + ", bigareaNames=" + bigareaNames + ", roleIds=" + roleIds + ", safeLevel=" + safeLevel + "]";
	}

   

}
