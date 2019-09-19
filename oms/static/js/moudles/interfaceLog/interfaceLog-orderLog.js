$(function () {
    $("#dg").datagrid({
        loadMsg: '正在加载中',
        title: '订单接口日志',
        url: './index.php?r=interfaceLog/orderLog/data',
        method: 'POST',
        rownumbers: true,
        height: $(window).height() - 80,
        pagination: true,
        pageSize: 50,
        columns: [[{
            field: 'order_type',
            title: '订单类型',
            formatter: function (value, rowData, rowIndex) {
                var jsondata = {
                    'PO': '采购入库',
                    'TR': '调拨入库',
                    'RS': '销售退货入库',
                    'IP': '盘盈入库',
                    'SO': '销售出库',
                    'TO': '调拨出库',
                    'RP': '采购退货出库',
                    'IL': '盘亏出库',
                    'OO': '线下出库',
                    'B2B': '批量销售出库',
                    'SCRK': '奇门生产入库',
                    'LYRK': '奇门领用入库',
                    'CCRK': '奇门残次品入库',
                    'CGRK': '奇门采购入库',
                    'DBRK': '奇门调拨入库',
                    'QTRK': '奇门其他入库',
                    'B2BRK': '奇门B2B入库',
                    'THRK': '奇门退货入库',
                    'HHRK': '奇门换货入库',
                    'PTCK': '奇门普通出库单（退仓）',
                    'DBCK': '奇门调拨出库',
                    'B2BCK': '奇门B2B出库',
                    'QTCK': '奇门其他出库',
                    'JYCK': '奇门一般交易出库',
                    'HHCK': '奇门换货出库',
                    'BFCK': '奇门补发出库',
                    'CNJG': '奇门仓内加工',
                    'JQRK': '拒签入库',
                    'ASN': '采购入库',
                    'STI': '调拨入库',
                    'RBR': '领用还回',
                    'STO': '调拨出库',
                    'RTS': '采购退货',
                    'RB': '领用出库',
                    'ESO': '线下销售',
                    'PY': '盘盈',
                    'PK': '盘亏',
                    'FROF': '冻结',
                    'FROG': '解冻',
                    'DEFD': '正转残',
                    'DEFG': '残转正',
                    'RMA': '销售退货'
                };
                return jsondata[value];
            }
        }, {
            field: 'customer_id',
            title: '货主ID'
        }, {
            field: 'warehouse_code',
            title: '仓库编码'
        }, {
            field: 'method',
            title: '接口方法',
            formatter: function (value, rowData, rowIndex) {
                var jsondata = {
                    'putASNData': '入库单下发',
                    'cancelASNData': '入库单取消',
                    'confirmASNData': '入库单状态明细回传',
                    'putSOData': '出库单下发',
                    'cancelSOData': '出库单取消',
                    'confirmSOStatus': '出库单状态回传',
                    'confirmSOData': '出库单明细回传',
                    'inventoryReport': '库存盘点通知',
                    'entryorder.create': '奇门入库单创建',
                    'taobao.qimen.entryorder.confirm': '奇门入库单确认',
                    'returnorder.create': '奇门退货入库单创建',
                    'taobao.qimen.returnorder.confirm': '奇门退货入库单确认',
                    'stockout.create': '奇门出库单创建',
                    'taobao.qimen.stockout.confirm': '奇门出库单确认',
                    'deliveryorder.create': '奇门发货单创建',
                    'deliveryorder.batchcreate': '发货单创建接口 （批量）',
                    'taobao.qimen.deliveryorder.confirm': '奇门发货单确认',
                    'taobao.qimen.deliveryorder.batchconfirm': '发货单确认接口  (批量)',
                    'taobao.qimen.sn.report': '奇门发货单SN通知',
                    'taobao.qimen.orderprocess.report': '奇门订单流水通知',
                    'taobao.qimen.itemlack.report': '奇门发货单缺货通知',
                    'order.cancel': '奇门单据取消',
                    'taobao.qimen.inventory.report': '奇门库存盘点通知',
                    'storeprocess.create': '奇门仓内加工单创建',
                    'taobao.qimen.storeprocess.confirm': '仓内加工单确认接口',
                    'taobao.qimen.stockchange.report': '奇门库存异动通知',
                    'taobao.qimen.deliveryorder.shortage' : '奇门直连发货单缺货通知接口',
                    '10' : '网易考拉采购单推送接口',
                    '100' : '网易考拉出库单推送接口',
                    '101' : '网易考拉出库单取消接口',
                    '102' : '网易考拉出库单回传接口',
                    '103' : '网易考拉出库单出库确认接口',
                    '104' : '网易考拉入库单推送接口',
                    '105' : '网易考拉入库单回传接口',
                    '106' : '网易考拉入库单入库确认接口',
                    '107' : '网易考拉仓库回调推送理货报告状态信息接口',
                    '108' : '网易考拉仓库回调推送理货报告详情信息接口',
                    '109' : '网易考拉推送理货单审核状态给仓库',
                    '11' : '网易考拉取消采购单',
                    '119' : '网易考拉商品资料下发推送接口',
                    '150' : '网易考拉SKU效期信息回调接口',
                    '20' : '网易考拉订单推送接口',
                    '21' : '网易考拉取消用户订单',
                    '30' : '网易考拉采购单入库回调接口',
                    '31' : '网易考拉用户订单出库回调',
                    '50' : '网易考拉盘点情况回调接口',
                    '60' : '网易考拉库存查询接口',
                    'kaola_getBillNo' : '网易考拉子母件获取运单号接口',
                    '115' : '网易考拉库存调整回调接口',
                    'beibei.outer.entryorder.create': '贝贝入库单创建接口',
                    'beibei.outer.entryorder.confirm': '贝贝入库单回传接口',
                    'beibei.outer.stockout.create': '贝贝出库单创建接口',
                    'beibei.outer.stockout.confirm': '贝贝出库单回传接口',
                    'beibei.outer.deliveryorder.create': '贝贝发货单创建接口',
                    'beibei.outer.bill.cancel': '贝贝单据取消接口',
                    'beibei.outer.stockchange.report': '贝贝库存异动接口'
                };
                return jsondata[value];
            }
        }, {
            field: 'msg_id',
            title: '接口日志主id'
        }, {
            field: 'api_url',
            title: '调用接口地址'
        }, {
            field: 'return_status',
            title: '推送状态',
            formatter: function (value, rowData, rowIndex) {
                var jsondata = {
                    '1': '成功',
                    '0': '失败'
                };
                return jsondata[value];
            }
        }, {
            field: 'request_param',
            title: '请求值',
            width: 500,
            formatter: function (value, rowData, rowIndex) {
                if (value == null || value == undefined) {
                    value = '';
                } else {
                    value = value.replace(/</g, '&lt;');
                    value = value.replace(/>/g, '&gt;');
                }
                return value;
            }
        }, {
            field: 'filter_result',
            title: '过滤错误',
            width: 200,
            formatter: function (value, rowData, rowIndex) {
                if (value == null || value == undefined) {
                    value = '';
                } else {
                    value = value.replace(/</g, '&lt;');
                    value = value.replace(/>/g, '&gt;');
                }
                return value;
            }
        }, {
            field: 'response_param',
            title: '响应值',
            width: 400,
            formatter: function (value, rowData, rowIndex) {
                if (value == null || value == undefined) {
                    value = '';
                } else {
                    value = value.replace(/</g, '&lt;');
                    value = value.replace(/>/g, '&gt;');
                }
                return value;
            }
        }, {
            field: 'create_time',
            title: '入库时间'
        }]],
        frozenColumns: [[
            {
                field: "xx",
                checkbox: true
            },
            {
                field: 'order_no',
                title: '订单号'
            }
        ]],
        toolbar: [
            {
                id: 'pushData',
                title: '推送数据到ERP',
                text: '推送数据到ERP',
                iconCls: 'icon-redo',
                handler: function () {
                    entryOrderConfirm();
                }
            }
        ],
        onBeforeLoad: function () {
            if (operateFlag == 0) {
                $('div.datagrid-toolbar').hide();
            }
        }
    })
})

function searchForm() {
    $("#dg").datagrid({
        queryParams: {
            'OrderLog[order_no]': $("#orderNo").val(),
            'OrderLog[order_type]': $("#orderType").combobox('getValue'),
            'OrderLog[customer_id]': $("#customerId").val(),
            'OrderLog[warehouse_code]': $("#warehouseCode").val(),
            'OrderLog[method]': $("#method").combobox('getValue'),
            'OrderLog[return_status]': $("#returnStatus").combobox('getValue'),
            'OrderLog[start_time]': $("#startTime").datetimebox("getValue"),
            'OrderLog[end_time]': $("#endTime").datetimebox("getValue")
        }
    })
}

//获取选中数据进行推送
function entryOrderConfirm() {
    //选中元素
    var data = $("#dg").datagrid('getSelections');
    var push = 0;

    var url = [
        'taobao.qimen.entryorder.confirm',
        'taobao.qimen.returnorder.confirm',
        'taobao.qimen.deliveryorder.confirm'
    ];

    var pushData = new Array();

    if (data.length < 0) {
        alert("至少选择一条推送数据");
        return false;
    }

    //检测是否有已推送成功的订单,并获取需要传输数据
    for (var i = 0; i < data.length; i++) {

        if ($.inArray(data[i].method, url) < 0) {
            alert("仅支持 奇门入库单确认，奇门退货入库单确认，奇门发货单确认接口手工推送,请检查手工推送数据是否在此范围内");
            return false;
        }
        pushData.push({
            'method': data[i].method,
            'order_no': data[i].order_no,
            'api_url': data[i].api_url,
            'order_type': data[i].order_type,
            'customer_id': data[i].customer_id,
            'request_param': data[i].request_param,
            'warehouse_code': data[i].warehouse_code
        });

        if (data[i].return_status) {
            if (!push) {
                var sure = confirm("选中推送订单有已推送成功的，确定要再次推送？");
                if (sure) {
                    push = 1;
                } else {
                    break;
                }
            }
        }
    }

    //确定再次推送
    if (push) {
        $.ajax({
            url: './index.php?r=interfaceLog/orderLog/Push',
            type: "post",
            data: {value: pushData},
            success: function (res) {

                var res = eval('(' + res + ')');

                alert(res.msg);

                return;
            }
        });
    } else {
        //终止操作
        return false;
    }
}