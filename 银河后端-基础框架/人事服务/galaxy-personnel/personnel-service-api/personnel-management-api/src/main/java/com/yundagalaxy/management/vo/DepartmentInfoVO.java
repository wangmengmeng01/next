package com.yundagalaxy.management.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.yundagalaxy.management.entity.DepartmentInfo;
import com.yundagalaxy.management.node.INode;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门岗位表视图实体类
 *
 * @author dongfeng
 * @since 2019-10-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "DepartmentInfoVO对象", description = "部门岗位表")
public class DepartmentInfoVO extends DepartmentInfo implements INode {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long dpmentId;

	/**
	 * 示例：YH00000001
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private String dpmentCode;
	/**
	 * 父节点-部门层级为一级时，该值为0；部门层级为二级，三级时为对应的上级部门编码
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private String parentDpmentCode;

	/**
	 * 子孙节点
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<INode> children;

	@Override
	public List<INode> getChildren() {
		if (this.children == null) {
			this.children = new ArrayList<>();
		}
		return this.children;
	}
	/**
	 * 上级机构
	 */
	private String parentDpmentName;
	/**
	 * 岗位在职人数
	 */
	private Long empNum;
	/**
	 * 等级名称
	 */
	private String dpmentLevelName;
	/**
	 * 经营模式名称
	 */
	private String businessModelName;



}
