$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '商家授权信息维护界面',
		url : './index.php?r=base/cskSellerAccessToken/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 50,
		pagination : true,
		pageSize : 50,
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'seller_id',
			title : '商家ID'
		}, {
			field : 'access_token',
			title : '授权口令'
		},{
			field : 'platform_elec',
			title : '电子面单平台'
		},{
			field : 'product_type',
			title : '物流商产品类型'
		}, {
			field : 'is_valid',
			title : '有效性',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
					'1' : '有效',
					'0' : '无效'
				};
				return jsondata[value];
			}
		}, {
			field : 'time',
			title : '授权时间'
		} ] ],
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
		},
		onBeforeLoad : function() {
			if (operateFlag == 0 ) {
			    $('div.datagrid-toolbar').hide();
			}
		}
	})
})

function openAddDlg() {
	$("#dlg").dialog({
		title : '新增',
		width : 600,
		height : 550,
		closed : false,
		href : './index.php?r=base/cskSellerAccessToken/add',
		buttons : [ {
			id : 'save',
			text : '保存',
			iconCls : 'icon-save',
			handler : function() {
				$.ydSubmit({
					url : './index.php?r=base/cskSellerAccessToken/add',
					params : $('#form_seller').serializeToJson(true),
					callback : function(data) {
						$.messager.show({
							title : '友情提示',
							msg : data.msg
						});
						if (data.status == 'ok') {
							$("#dlg").dialog("close");
							$("#dg").datagrid("reload");
						}
					},
					paramType : 'json',
					beforeSend : function() {
						return $("#form_seller").form('validate');
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
			$('#form_seller').form('reset');
		}
	});
}
function openEditDlg() {
	var rowData = $("#dg").datagrid('getSelected');
	$("#dlg").dialog({
		title : '修改',
		width : 600,
		height : 550,
		href : updateUrl.replace('uid', rowData.seller_id),
		buttons : [ {
			id : 'save',
			text : '保存',
			iconCls : 'icon-save',
			handler : function() {
				$.ydSubmit({
					url : updateUrl.replace('uid', rowData.seller_id),
					params : $('#form_seller').serializeToJson(true),
					callback : function(data) {
						$.messager.show({
							title : '友情提示',
							msg : data.msg
						});
						if (data.status == 'ok') {
							$("#dlg").dialog("close");
							$("#dg").datagrid("reload");
						}
					},
					paramType : 'json',
					beforeSend : function() {
						return $("#form_seller").form('validate');
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
			$("#seller_id").attr('readonly', 'readonly');
		}
	});
}
function openDelDlg() {
	var row = $("#dg").datagrid("getSelected");
	$.messager.confirm("确认按钮", "确认要删除吗？", function(r) {
		if (r) {
			$.post(deleteUrl, {
				'id' : row.seller_id
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
		if (row[0].is_valid == 1) {
			$("#del").linkbutton("enable");
		} else {
			$("#del").linkbutton("disable");
		}
		$("#edit").linkbutton("enable");
	} else if (row.length > 1) {
		$("#edit").linkbutton("disable");
		$("#del").linkbutton("disable");
	} else if (row.length == 0) {
		$("#edit").linkbutton("disable");
		$("#del").linkbutton("disable");
	}

}
function searchForm() {
	$("#dg").datagrid({
		queryParams : {
			'Seller[seller_id]' : $("#sellerId").val(),
			'Seller[access_token]' : $("#accessToken").val(),
			'Seller[platform_elec]' : $("#platformElec").val(),
			'Seller[product_type]' : $("#product_type").val(),
			'Seller[is_valid]' : $("#isValid").combobox('getValue')
		}
	})
}
