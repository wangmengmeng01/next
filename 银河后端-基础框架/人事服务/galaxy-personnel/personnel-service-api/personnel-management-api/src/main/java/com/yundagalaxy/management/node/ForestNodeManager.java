/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package com.yundagalaxy.management.node;


import java.util.ArrayList;
import java.util.List;

/**
 * 森林管理类
 *
 * @author smallchill
 */
public class ForestNodeManager<T extends INode> {

	/**
	 * 森林的所有节点
	 */
	private List<T> list;

	/**
	 * 森林的父节点ID
	 */
	private List<String> parentDpmentCodes = new ArrayList<>();

	public ForestNodeManager(List<T> items) {
		list = items;
	}

	/**
	 * 根据节点ID获取一个节点
	 *
	 * @param dpmentCode 节点ID
	 * @return 对应的节点对象
	 */
	public INode getTreeNodeAT(String dpmentCode) {
		for (INode forestNode : list) {
			if (forestNode.getDpmentCode().equals(dpmentCode)) {
				return forestNode;
			}
		}
		return null;
	}

	/**
	 * 增加父节点ID
	 *
	 * @param parentDpmentCode
	 */
	public void addParentDpmentCode(String parentDpmentCode) {

		parentDpmentCodes.add(parentDpmentCode);
	}

	/**
	 * 获取树的根节点(一个森林对应多颗树)
	 *
	 * @return 树的根节点集合
	 */
	public List<T> getRoot() {
		List<T> roots = new ArrayList<>();
		for (T forestNode : list) {
			if ("0".equals(forestNode.getParentDpmentCode()) || parentDpmentCodes.contains(forestNode.getDpmentCode())) {
				roots.add(forestNode);
			}
		}
		return roots;
	}

}
