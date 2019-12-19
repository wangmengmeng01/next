package com.yundagalaxy.management.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class IdCommonVO {
    /**
     *
     */
    @ApiModelProperty(name="dpmentIds", value="1,2")
    private String dpmentIds;
    /**
     *
     */
    @ApiModelProperty(name="jobIds", value="1,2")
    private String jobIds;
    /**
     *
     */
    @ApiModelProperty(name="tmpEmpIds", value="1,2")
    private String tmpEmpIds;
}
