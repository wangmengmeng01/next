$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '店铺列表',
		url : './index.php?r=base/shop/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 50,
		pagination : true,
		pageSize : 20,
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'shop_code',
			title : '店铺编码'
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

function searchForm() {
	$("#dg").datagrid({
		queryParams : {
			'Shop[descr_c]' : $("#Shop_descr_c").val(),
			'Shop[shop_code]' : $("#Shop_shop_code").val(),
			'Shop[active_flag]' : $("#Shop_active_flag").combobox('getValue')
		}
	})
}

/*
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

function addTab(subtitle,url){
	if(!$('#tabs').tabs('exists',subtitle)){
	$('#tabs').tabs('add',{
	title:subtitle,
	content:createFrame(url),
	closable:true
	});
	}else{
	$('#tabs').tabs('select',subtitle);
	// $('#mm-tabupdate').click();
	}
}
*/
