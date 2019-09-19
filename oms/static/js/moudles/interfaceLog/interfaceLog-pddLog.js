$(function() {
    $("#dg").datagrid({
        loadMsg : '正在加载中',
        title : '菜鸟电子面单接口日志',
        url : './index.php?r=interfaceLog/pddLog/data',
        method : 'POST',
        rownumbers : true,
        height : $(window).height() - 80,
        pagination : true,
        pageSize : 50,
        columns : [ [{
            field : 'id',
            hidden: true,
            title : '主键ID'
        }, {
            field : 'customer_id',
            title : '货主ID'
        }, {
            field : 'method',
            title : '接口方法',
            formatter : function(value, rowData, rowIndex) {
                var jsondata = {
                    'pdd.waybill.authorization' : '商家授权信息同步接口',
                    'pdd.waybill.get' : '电子面单云打印接口',
                    'pdd.waybill.search' : '查询面单服务订购及面单使用情况',
                    'pdd.cloudprint.stdtemplates.get' : '获取所有的标准电子面单模板',
                    'pdd.waybill.cancel' : '商家取消获取的电子面单号',
                };
                return jsondata[value];
            }
        }, {
            field : 'app_code',
            title : '应用编码'
        }, {
            field : 'msg_id',
            title : '接口日志主id'
        }, {
            field : 'parent_log_id',
            title : '父日志id'
        }, {
            field : 'api_url',
            title : '调用接口地址'
        }, {
            field : 'return_status',
            title : '推送状态',
            formatter : function(value, rowData, rowIndex) {
                var jsondata = {
                    '1' : '成功',
                    '0' : '失败'
                };
                return jsondata[value];
            }
        },{
            field : 'request_param',
            title : '请求值',
            width : 500,
            formatter : function(value, rowData, rowIndex) {
                if (value == null || value == undefined) {
                    value = '';
                } else {
                    value = value.replace(/</g, '&lt;');
                    value = value.replace(/>/g, '&gt;');
                }
                return value;
            }
        }, {
            field : 'response_param',
            title : '响应值',
            width : 400,
            formatter : function(value, rowData, rowIndex) {
                if (value == null || value == undefined) {
                    value = '';
                } else {
                    value = value.replace(/</g, '&lt;');
                    value = value.replace(/>/g, '&gt;');
                }
                return value;
            }
        }, {
            field : 'request_time',
            title : '请求时间'
        }, {
            field : 'response_time',
            title : '响应时间'
        }, {
            field : 'create_time',
            title : '入库时间'
        } ] ],
        frozenColumns : [[{
            field : 'order_list',
            title : '交易订单号'
        }]]
    })
})

function searchForm() {
    $("#dg").datagrid({
        queryParams : {
            'WaybillLog[order_list]' : $("#orderList").val(),
            'WaybillLog[customer_id]' : $("#customerId").val(),
            'WaybillLog[app_code]' : $("#appCode").val(),
            'WaybillLog[method]' : $("#method").combobox('getValue'),
            'WaybillLog[return_status]' : $("#returnStatus").combobox('getValue'),
            'WaybillLog[start_time]' : $("#startTime").datetimebox("getValue"),
            'WaybillLog[end_time]' : $("#endTime").datetimebox("getValue")
        }
    })
}
