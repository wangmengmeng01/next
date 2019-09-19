$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '供应商列表',
		url : './index.php?r=base/supplier/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 50,
		pagination : true,
		pageSize : 20,
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'supplier_code',
			title : '供应商编码'
		}, {
			field : 'descr_c',
			title : '中文描述'
		}, {
			field : 'descr_e',
			title : '英文描述'
		}, {
			field : 'country',
			title : '国家代码'
		},{
			field : 'province',
			title : '省'
		},{
			field : 'city',
			title : '市'
		},{
			field : 'zip',
			title : '邮编'
		},{
			field : 'address1',
			title : '地址1'
		},{
			field : 'address2',
			title : '地址2'
		},{
			field : 'address3',
			title : '地址3'
		}, {
			field : 'contact1',
			title : '联系人1'
		}, {
			field : 'contact1_tel1',
			title : '联系人1手机号码'
		}, {
			field : 'contact1_tel2',
			title : '联系人1电话号码'
		}, {
			field : 'contact1_fax',
			title : '联系人1传真号'
		}, {
			field : 'contact1_title',
			title : '联系人1职位'
		}, {
			field : 'contact1_email',
			title : '联系人1邮箱'
		}, {
			field : 'contact2',
			title : '联系人2'
		}, {
			field : 'contact2_tel1',
			title : '联系人2手机号码'
		}, {
			field : 'contact2_tel2',
			title : '联系人2电话号码'
		}, {
			field : 'contact2_fax',
			title : '联系人2传真号'
		}, {
			field : 'contact2_title',
			title : '联系人2职位'
		}, {
			field : 'contact2_email',
			title : '联系人2邮箱'
		}, {
			field : 'contact3',
			title : '联系人3'
		}, {
			field : 'contact3_tel1',
			title : '联系人3手机号码'
		}, {
			field : 'contact3_tel2',
			title : '联系人3电话号码'
		}, {
			field : 'contact3_fax',
			title : '联系人3传真号'
		}, {
			field : 'contact3_title',
			title : '联系人3职位'
		}, {
			field : 'contact3_email',
			title : '联系人3邮箱'
		},{
			field : 'currency',
			title : '币种'
		},{
			field : 'bank_account',
			title : '银行账号'
		},{
			field : 'easy_code',
			title : '助记码'
		}, {
			field : 'remark',
			title : '备注'
		}, {
			field : 'active_flag',
			title : '是否激活',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
					'Y' : '是',
					'N' : '否',
					''  : '否'
				};
				return jsondata[value];
			}
		}, {
			field : 'create_time',
			title : '入库时间'
		} ] ],
		/*
		toolbar : [ {
			id : 'add',
			title : '添加',
			text : '添加',
			iconCls : 'icon-add',
			disabled : false,
			handler : function() {
				openAddDlg();
			}
		}, {
			id : 'edit',
			title : '编辑',
			text : '编辑',
			iconCls : 'icon-edit',
			disabled : true,
			handler : function() {
				openEditDlg();
			}
		}, {
			id : 'del',
			title : '删除',
			text : '删除',
			iconCls : 'icon-cut',
			disabled : true,
			handler : function() {
				openDelDlg();
				setBtn();
			}
		} ],
		onCheck : function() {
			setBtn();
		},
		onCheckAll : function() {
			setBtn();
		},
		onSelect : function() {
			setBtn();
		},
		onUnselect : function() {
			setBtn();
		},
		onUncheck : function() {
			setBtn();
		},
		onUncheckAll : function() {
			setBtn();
		},
		onSelectAll : function() {
			setBtn();
		},
		onUnselectAll : function() {
			setBtn();
		},
		onLoadSuccess : function() {
			setBtn();
		}
		*/
	})
})

/*
function openAddDlg() {
	$("#dlg").dialog({
		title : '新增',
		width : 600,
		height : 500,
		closed : false,
		href : './index.php?r=base/supplier/add',
		buttons : [ {
			id : 'save',
			text : '保存',
			iconCls : 'icon-save',
			handler : function() {
				$.ydSubmit({
					url : './index.php?r=base/supplier/add',
					params : $('#yw0').serializeToJson(true),
					callback : function(data) {
						$.messager.show('友情提示', data.msg);
						if (data.status == 'ok') {
							$("#dlg").dialog("close");
							$("#dg").datagrid("reload");
						}
					},
					paramType : 'json',
					beforeSend : function() {
						return $("#yw0").form('validate');
					} // 默认，一般不配置
				});
			}
		}, {
			id : 'cancel',
			text : '关闭',
			iconCls : 'icon-cancel',
			handler : function() {
				$('#dlg').dialog('close');
			}
		} ],
		onLoad : function() {
			$('#yw0').form('reset');
		}
	});
}
function openEditDlg() {
	var rowData = $("#dg").datagrid('getSelected');
	$("#dlg").dialog({
		title : '修改',
		width : 600,
		height : 500,
		href : updateUrl.replace('uid', rowData.supplier_id),
		buttons : [ {
			id : 'save',
			text : '保存',
			iconCls : 'icon-save',
			handler : function() {
				$.ydSubmit({
					url : updateUrl.replace('uid', rowData.supplier_id),
					params : $('#yw0').serializeToJson(true),
					callback : function(data) {
						$.messager.show('友情提示', data.msg);
						if (data.status == 'ok') {
							$("#dlg").dialog("close");
							$("#dg").datagrid("reload");
						}
					},
					paramType : 'json',
					beforeSend : function() {
						return $("#yw0").form('validate');
					}// 默认，一般不配置
				});
			}
		}, {
			id : 'cancel',
			text : '关闭',
			iconCls : 'icon-cancel',
			handler : function() {
				$('#dlg').dialog('close');
			}
		} ],
		onLoad : function() {
		}
	});
}
function openDelDlg() {
	var row = $("#dg").datagrid("getSelected");
	$.messager.confirm("确认按钮", "确认要删除吗？", function(r) {
		if (r) {
			$.post(deleteUrl, {
				'delId' : row.supplier_id
			}, function(data) {
				$.messager.show({
					title : '友情提示',
					msg : data.msg
				});
				if (data.status == 'ok') {
					$("#dg").datagrid("reload");
				}
			}, 'json');
		}
	});
	return false;

}
function setBtn() {
	var row = $("#dg").datagrid('getSelections');
	if (row.length == 1) {
		$("#edit").linkbutton("enable");
		$("#del").linkbutton("enable");
	} else if (row.length > 1) {
		$("#edit").linkbutton("disable");
		$("#del").linkbutton("disable");
	} else if (row.length == 0) {
		$("#edit").linkbutton("disable");
		$("#del").linkbutton("disable");
	}

}
*/

function searchForm() {
	$("#dg").datagrid({
		queryParams : {
			'Supplier[descr_c]' : $("#Supplier_descr_c").val(),
			'Supplier[supplier_code]' : $("#Supplier_supplier_code").val(),
			'Supplier[active_flag]' : $("#Supplier_active_flag").combobox('getValue')
		}
	})
}
