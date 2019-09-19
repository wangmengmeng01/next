$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '商家开通电子面单服务信息展示界面',
		url : './index.php?r=base/cskSellerWaybillInfo/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 50,
		pagination : true,
		pageSize : 50,
        fitColumns: true,
		columns : [ [ {
			field : 'info_id',
			hidden: true,
			title : '信息ID'
		}, {
			field : 'ship_addr_code',
			title : '仓库地址编码'
		}, {
			field : 'is_jd',
			title : '电子面单平台',
			align: 'center',
			formatter : function (value, rowData, rowIndex) {
				var data = {
					'1' : '京东',
					'2' : '拼多多',
					''	: '菜鸟'
				};

				return data[value];
			}
		}, {
			field : 'cp_type',
			title : '快递公司类型',
			formatter : function (value, rowData, rowIndex) {
				var jsondata = {
					'1' : '直营',
					'2' : '加盟'
				};
				return jsondata[value];
			}
		}, {
			field : 'cp_code',
			title : '快递公司编码'
		}, {
			field : 'ship_detail_address',
			title : '发件详细地址'
		}, {
			field : 'ship_prov',
			title : '发件省'
		}, {
			field : 'ship_city',
			title : '发件城市'
		}, {
			field : 'ship_county',
			title : '发件县区'
		}, {
			field : 'ship_town',
			title : '发件乡镇'
		}, {
			field : 'waybill_address_id',
			title : '地址记录ID(非地址库ID)'
		}, {
			field : 'branch_code',
			title : '发件网点编码'
		}, {
			field : 'branch_name',
			title : '发件网点名称'
		}, {
			field : 'quantity',
			title : '当前余量'
		}, {
			field : 'print_quantity',
			title : '面单打印数'
		}, {
			field : 'cancel_quantity',
			title : '单号取消数'
		}, {
			field : 'allocated_quantity',
			title : '总分配单号数'
		}, {
			field : 'create_time',
			title : '更新时间'
		} ] ],
		frozenColumns : [[{
			field : 'ck',
			checkbox : true
		}, {
			field : 'seller_id',
			title : '商家ID'
		}]],
		toolbar : [ {
			id : 'edit',
			title : '编辑',
			text : '编辑',
			iconCls : 'icon-edit',
			disabled : true,
			handler : function() {
				openEditDlg();
			}
		}, {
			id : 'refresh',
			text: '刷新',
			iconCls : 'icon-reload',
			disabled : true,
			handler : function () {
                reloadSheet();
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

function openEditDlg() {
	var rowData = $("#dg").datagrid('getSelected');
	$("#dlg").dialog({
		title : '修改',
		width : 600,
		height : 550,
		href : updateUrl.replace('uid', rowData.info_id),
		buttons : [ {
			id : 'save',
			text : '保存',
			iconCls : 'icon-save',
			handler : function() {
				$.ydSubmit({
					url : updateUrl.replace('uid', rowData.info_id),
					params : $('#form_waybill').serializeToJson(true),
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
						return $("#form_waybill").form('validate');
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
			$("#ship_detail_address").attr('readonly', 'readonly');
			$("#cp_code").attr('readonly', 'readonly');
			$("#branch_code").attr('readonly', 'readonly');
			$("#branch_name").attr('readonly', 'readonly');
			$("#ship_prov").attr('readonly', 'readonly');
			$("#ship_city").attr('readonly', 'readonly');
			$("#ship_county").attr('readonly', 'readonly');
			$("#ship_town").attr('readonly', 'readonly');
			$("#print_quantity").attr('readonly', 'readonly');
			$("#cancel_quantity").attr('readonly', 'readonly');
			$("#allocated_quantity").attr('readonly', 'readonly');
			$("#quantity").attr('readonly', 'readonly');
			$("#cp_type").attr('readonly', 'readonly');
			$("#cp_code").attr('readonly', 'readonly');
		}
	});
}

// 刷新商家面单订购详情
function reloadSheet() {

	var rowData = $("#dg").datagrid('getSelected');

	$.ajax({
        method : 'get',
        dataType : 'json',
        url : reloadUrl.replace('sellerId', rowData.seller_id),
        success : function (res) {

			$.messager.show({
				title: '提示',
				msg: res.msg
			});

            if (res.status) {

                $('#dg').datagrid('reload');
            }
        }
    });
}

function setBtn() {
	var row = $("#dg").datagrid('getSelections');
	if (row.length == 1) {
		$("#edit").linkbutton("enable");
		$("#refresh").linkbutton("enable");
	} else if (row.length > 1 || row.length == 0) {
		$("#edit").linkbutton("disable");
		$("#refresh").linkbutton("disable");
	}

}

function searchForm() {
	$("#dg").datagrid({
		queryParams : {
			'WaybillInfo[seller_id]' : $("#sellerId").val(),
			'WaybillInfo[ship_addr_code]' : $("#shipAddrCode").val(),
			'WaybillInfo[address_detail]' : $("#addressDetail").val(),
			'WaybillInfo[cp_code]' : $("#cpCode").val(),
			'WaybillInfo[branch_code]' : $("#branchCode").val(),
			'WaybillInfo[branch_name]' : $("#branchName").val(),
			'WaybillInfo[is_jd]' : $("#is_jd").combobox('getValue'),
		}
	})
}
