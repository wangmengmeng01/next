$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '目的地与分仓关联排序表',
		url : './index.php?r=base/DestinationWarehouseBind/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 50,
		pagination : true,
		pageSize : 50,
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'customer_name',
			title : '货主'
		}, {
			field : 'customer_id',
			title : '货主编码'
		},{
			field : 'platform_code',
			title : '平台编码'
		},{
			field : 'shop_name',
			title : '店铺名称'
		},{
			field : 'rc_addr',
			title : '固定收货机构或地址'
		},{
			field : 'wh1',
			title : '分仓1'
		},{
			field : 'wh2',
			title : '分仓2'
		},{
			field : 'wh3',
			title : '分仓3'
		},{
			field : 'wh4',
			title : '分仓4'
		},{
			field : 'wh5',
			title : '分仓5'
		},{
			field : 'wh6',
			title : '分仓6'
		},{
			field : 'wh7',
			title : '分仓7'
		},{
			field : 'wh8',
			title : '分仓8'
		},{
			field : 'wh9',
			title : '分仓9'
		},{
			field : 'wh10',
			title : '分仓10'
		}, {
			field : 'operator_name',
			title : '操作人名称'
		}, {
			field : 'update_time',
			title : '操作时间'
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
		}],
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
		width : 400,
		height : 550,
		closed : false,
		href : './index.php?r=base/DestinationWarehouseBind/add',
		buttons : [ {
			id : 'save',
			text : '保存',
			iconCls : 'icon-save',
			handler : function() {
				$.ydSubmit({
					url : './index.php?r=base/DestinationWarehouseBind/add',
					params : $('#form_DestinationWarehouseBind').serializeToJson(true),
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
						return $("#form_DestinationWarehouseBind").form('validate');
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
			$('#form_DestinationWarehouseBind').form('reset');
		}
	});
}

function openEditDlg() {
	var rowData = $("#dg").datagrid('getSelected');
	$("#dlg").dialog({
		title : '修改',
		width : 400,
		height : 550,
		href : updateUrl.replace('uid', rowData.id),
		buttons : [ {
			id : 'save',
			text : '保存',
			iconCls : 'icon-save',
			handler : function() {
				$.ydSubmit({
					url : updateUrl.replace('uid', rowData.id),
					params : $('#form_DestinationWarehouseBind').serializeToJson(true),
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
						return $("#form_DestinationWarehouseBind").form('validate');
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
			$("#customerId").attr('readonly', 'readonly');
		}
	});
}

function openDelDlg() {
	var row = $("#dg").datagrid("getSelected");
	$.messager.confirm("确认按钮", "确认要删除吗？", function(r) {
		if (r) {
			$.post(deleteUrl, {
				'id' : row.id
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
			'DestinationWarehouseBind[customer_id]' : $("#customerId").val(),
			'DestinationWarehouseBind[customer_name]' : $("#customerName").val(),
		}
	})
}
