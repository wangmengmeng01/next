$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '商品列表',
		url : './index.php?r=base/product/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 50,
		pagination : true,
		pageSize : 20,
		fitColumns : false,
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'sku',
			title : 'SKU'
		}, {
			field : 'descr_c',
			title : '中文名称'
		},{
			field : 'descr_e',
			title : '英文名称'
		},{
			field : 'sku_short',
			title : '商品简称'
		}, {
			field : 'customer_id',
			title : '货主ID'
		},{
			field : 'gross_weight',
			title : '毛重量'
		},{
			field : 'net_weight',
			title : '净重量'
		},{
			field : 'cube',
			title : '体积'
		},{
			field : 'price',
			title : '价格',
//			formatter : function(value, rowData, rowIndex) {
//				    var test = '/([0-9]+\.[0-9]{2})[0-9]*/';
//				    var newValue = value.replace(test,"$1");
//					return newValue;
//			}
		},{
			field : 'sku_Length',
			title : '长'
		},{
			field : 'sku_width',
			title : '宽'
		},{
			field : 'sku_height',
			title : '高'
		},{
			field : 'cycle_class',
			title : '循环级别'
		},{
			field : 'shelfLife_flag',
			title : '货架生命周期'
		},{
			field : 'shelfLife_type',
			title : '周期类型'
		},{
			field : 'inbound_life_days',
			title : '入库有效期'
		},{
			field : 'outbound_life_days',
			title : '出库有效期'
		},{
			field : 'shelf_life',
			title : '有效期'
		},{
			field : 'sku_group1',
			title : '货号或款号'
		},{
			field : 'sku_group2',
			title : '颜色'
		},{
			field : 'sku_group3',
			title : '尺码'
		},{
			field : 'sku_group4',
			title : '品牌'
		},{
			field : 'sku_group5',
			title : '年份'
		},{
			field : 'sku_group6',
			title : '季节'
		},{
			field : 'sku_group7',
			title : '大类'
		},{
			field : 'sku_group8',
			title : '中类'
		},{
			field : 'sku_group9',
			title : '小类'
		},{
			field : 'qty_min',
			title : '库存下限'
		},{
			field : 'qty_max',
			title : '库存上限'
		},{
			field : 'freight_class',
			title : '货物类型'
		},{
			field : 'kit_flag',
			title : '组合件标识'
		}, {
			field : 'active_flag',
			title : '激活标志',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
					'Y' : '是',
					'N' : '否'
				};
				return jsondata[value];
			}
		}, {
			field : 'create_time',
			title : '入库时间'
		} ] ],
		
		toolbar : [ {
			id : 'exportExcel',
			title : '导出Excel',
			text : '导出Excel',
			iconCls : 'icon-export',
			handler : function() {
				openExportExcel();
			}
		} ],	
		onBeforeLoad : function() {
			if (operateFlag == 0 ) {
			    $('div.datagrid-toolbar').hide();
			}
		}
		/*
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
			'Product[descr_c]' : $("#Product_descr_c").val(),
			'Product[sku]' : $("#Product_sku").val(),
			'Product[customer_id]' : $("#customer_id").val(),
			'Product[active_flag]' : $("#Product_active_flag").combobox('getValue')
		}
	})
}

/**
 * 导出excel
 * 
 */
function openExportExcel() {
	//校验是否有导出的权限
	if (operateFlag == 0 ) {
		$.messager.show({
			title : '友情提示',
			msg : '您没有导出excel的权限！'
		});
		return false;
	}
	var rows = $("#dg").datagrid("getRows");
	if(rows[0]===undefined){
		$.messager.show({
			title : '友情提示',
			msg : '导出数据为空，请查询后导出'
		});
		return false;
	}
	var sku = $.trim($("#Product_sku").val());
	var descr_c = $.trim($("#Product_descr_c").val());
	var customerId = $.trim($("#customer_id").val());
	var activeFlag = $.trim($("#Product_active_flag").combobox('getValue'));
	if (sku == '' && descr_c == '' && customerId == '') {
		$.messager.show({
			title : '友情提示',
			msg : '商品SKU、商品名称和货主ID查询条件不能都为空'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=product&sku='
			+ sku
			+ '&descr_c='
			+ descr_c
			+ '&customerId='
			+ customerId
			+ '&activeFlag='
			+ activeFlag;
}