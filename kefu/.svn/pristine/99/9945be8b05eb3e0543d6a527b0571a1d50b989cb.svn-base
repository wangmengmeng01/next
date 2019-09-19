var prefix = "/vipkf/bigcustomer/orderChi"
$(function () {
    let text = $('#detail_state').text();
    if(text=='已结单'){
        $('#btnDiv').hide();
    }
    load();

});

function load() {
    $('#exampleTable1')
        .bootstrapTable(
            {
                method: 'get', // 服务器数据的请求方式 get or post
                url: prefix + "/caoZuo", // 服务器数据的加载地址
                //	showRefresh : true,
                //	showToggle : true,
                //	showColumns : true,
                iconSize: 'outline',
                toolbar: '#exampleToolbar',
                striped: true, // 设置为true会有隔行变色效果
                dataType: "json", // 服务器返回的数据类型
                pagination: true, // 设置为true会在底部显示分页条
                // queryParamsType : "limit",
                // //设置为limit则会发送符合RESTFull格式的参数
                singleSelect: false, // 设置为true将禁止多选
                // contentType : "application/x-www-form-urlencoded",
                // //发送到服务器的数据编码类型
                pageSize: 10, // 如果设置了分页，每页数据条数
                pageNumber: 1, // 如果设置了分布，首页页码
                //search : true, // 是否显示搜索框
                showColumns: false, // 是否显示内容下拉框（选择显示的列）
                //showFooter: true, //统计列求和 sum、average等
                sidePagination: "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"

                //grimm 双击触发的事件
                onDblClickRow: function (row) {
                    edit(row.id);
                },

                queryParams: function (params) {
                    return {
                        //说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                        limit: params.limit,
                        offset: params.offset,
                        orderId: $('#orderId').val()
                        // name:$('#searchName').val(),
                        // username:$('#searchName').val()
                    };
                },
                // //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
                // queryParamsType = 'limit' ,返回参数必须包含
                // limit, offset, search, sort, order 否则, 需要包含:
                // pageSize, pageNumber, searchText, sortName,
                // sortOrder.
                // 返回false将会终止请求
                columns: [
                    {
                        field: 'operateName',
                        title: '操作人'
                    },
                    {
                        field: 'operateOrganization',
                        title: '操作机构'
                    },
                    {
                        field: 'time',
                        title: '操作时间'
                    },
                    {
                        field: 'type',
                        title: '操作类型'
                    },
                    {
                        field: 'dealContent',
                        title: '处理内容'
                    },
                    {
                        field: 'fileName',
                        title: '附件',
                        formatter: function (value, row, index) {
                            if(row.fileName!='' && row.fileName!=null){
                                var e = '<a style="color:blue;" href="#" mce_href="#" title="查看详情" onclick="downFile(\''
                                    + row.fileName+","+row.uploadPath
                                    + '\')">'+' <font style="text-decoration:underline">'+row.fileName+'</font> '+'</a> ';
                                return e;
                            }
                        }
                    },
                    {
                        field: 'zeRenFang',
                        title: '责任方'
                    }]
            });
    $('#exampleTable2')
        .bootstrapTable(
            {
                method: 'get', // 服务器数据的请求方式 get or post
                url: prefix + "/qiTaWenTi", // 服务器数据的加载地址
                //	showRefresh : true,
                //	showToggle : true,
                //	showColumns : true,
                iconSize: 'outline',
                toolbar: '#exampleToolbar',
                striped: true, // 设置为true会有隔行变色效果
                dataType: "json", // 服务器返回的数据类型
                pagination: true, // 设置为true会在底部显示分页条
                // queryParamsType : "limit",
                // //设置为limit则会发送符合RESTFull格式的参数
                singleSelect: false, // 设置为true将禁止多选
                // contentType : "application/x-www-form-urlencoded",
                // //发送到服务器的数据编码类型
                pageSize: 10, // 如果设置了分页，每页数据条数
                pageNumber: 1, // 如果设置了分布，首页页码
                //search : true, // 是否显示搜索框
                showColumns: false, // 是否显示内容下拉框（选择显示的列）
                //showFooter: true, //统计列求和 sum、average等
                sidePagination: "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"

                //grimm 双击触发的事件
                onDblClickRow: function (row) {
                    edit(row.id);
                },

                queryParams: function (params) {
                    return {
                        //说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                        limit: params.limit,
                        offset: params.offset,
                        waybillNum: $('#waybillNum').val()
                        // name:$('#searchName').val(),
                        // username:$('#searchName').val()
                    };
                },
                // //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
                // queryParamsType = 'limit' ,返回参数必须包含
                // limit, offset, search, sort, order 否则, 需要包含:
                // pageSize, pageNumber, searchText, sortName,
                // sortOrder.
                // 返回false将会终止请求
                columns: [
                    {
                        field: 'id',
                        title: '序号'
                    },
                    {
                        field: 'orderId',
                        title: '咨询单号'
                    },
                    {
                        field: 'logisticOrderNum',
                        title: '物流订单号'
                    },
                    {
                        field: 'waybillNum',
                        title: '运单号'
                    },
                    {
                        field: 'consultTime',
                        title: '咨询时间'
                    },
                    {
                        field: 'consultType',
                        title: '咨询类型'
                    },
                    {
                        field: 'priority',
                        title: '优先级'
                    },
                    {
                        field: 'state',
                        title: '咨询单状态'
                    }]
            });
}

function reLoad() {
    $('#exampleTable1').bootstrapTable('refresh');
    $('#exampleTable2').bootstrapTable('refresh');

}

//导出excel
function exportExcel(action) {
    layer.load(1);
    $('#exportExcelbtn').attr("disabled", true);
    $('#exportExcelbtn').attr("readonly", "readonly")//将input元素设置为readonly

    var url = prefix + action;
    var xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);    // 也可以使用POST方式，根据接口
    xhr.responseType = "blob";  // 返回类型blob
    // 定义请求完成的处理函数，请求前也可以增加加载框/禁用下载按钮逻辑
    xhr.onload = function () {
        // 请求完成
        if (this.status === 200) {
            // 返回200
            var blob = this.response;
            var reader = new FileReader();
            reader.readAsDataURL(blob);  // 转换为base64，可以直接放入a表情href
            reader.onload = function (e) {
                // 转换完成，创建一个a标签用于下载
                var a = document.createElement('a');
                a.download = 'data.xlsx';
                a.href = e.target.result;
                $("body").append(a);  // 修复firefox中无法触发click
                a.click();
                $(a).remove();
            }

            layer.msg("下载成功");
        }

        layer.closeAll('loading');
        $('#exportExcelbtn').attr("disabled", false);
        $('#exportExcelbtn').removeAttr("readonly");//去除input元素的readonly属性
    };
    // 发送ajax请求
    xhr.send()
}

function add() {
    layer.open({
        type: 2,
        title: '添加',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '520px'],
        content: prefix + '/add' // iframe的url
    });
}

function edit(id) {
    if (s_edit_h == 'hidden') {
        return;
    };

    layer.open({
        type: 2,
        title: '编辑',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '520px'],
        content: prefix + '/edit/' + id // iframe的url
    });
}

function remove(id) {
    layer.confirm('确定要删除选中的记录？', {
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: prefix + "/remove",
            type: "post",
            data: {
                'id': id
            },
            success: function (r) {
                if (r.code == 0) {
                    layer.msg(r.msg);
                    reLoad();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    })
}

function resetPwd(id) {
}

function batchRemove() {
    var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
    if (rows.length == 0) {
        layer.msg("请选择要删除的数据");
        return;
    }
    layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
        btn: ['确定', '取消']
        // 按钮
    }, function () {
        var ids = new Array();
        // 遍历所有选择的行数据，取每条数据对应的ID
        $.each(rows, function (i, row) {
            ids[i] = row['id'];
        });
        $.ajax({
            type: 'POST',
            data: {
                "ids": ids
            },
            url: prefix + '/batchRemove',
            success: function (r) {
                if (r.code == 0) {
                    layer.msg(r.msg);
                    reLoad();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    }, function () {

    });
}

function detail(orderId) {
    //发起ajax请求查看详情
    layer.open({
        type : 2,
        title : '咨询单详情页面',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '100%', '100%' ],
        content : prefix + '/detail/' + orderId // iframe的url
    });
}

function caoZuo(that) {
    $(that).addClass('active').siblings().removeClass('active')
    $('.table1').show()
    $('.table2').hide()
}
function qiTaWenTi(that) {
    $(that).addClass('active').siblings().removeClass('active')
    $('.table2').show()
    $('.table1').hide()
}
function wuLiu() {
    //弹框(优化,发起ajax从后台获取地址)
    layer.open({
        type: 2,
        title: '物流记录',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area : [ '100%', '100%' ],
        content: "http://n2cx.yundasys.com:18090/wsd/kjcx/cxend.jsp?wen="+$("#waybillNum").val()+"&jmm="+$("#waybillNum").val() // iframe的url
    });
}

function jieDan() {
    //判断是否可以处理或者结单
    if($('#detail_state').text()=='待申领' || $('#detail_state').text()=='已结单'){
        layer.msg("该状态下不能执行此操作!");
        return;
    }
    var orderIds=$('#orderId').val()
    var consultType=$("#consultType").val();
    layer.open({
        type: 2,
        title: '结单',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area : [ '580px', '430px' ],
        content: prefix + '/piLiangJieDan/'+orderIds // iframe的url
    });
}

function deal() {
    //判断是否可以处理或者结单
    if($('#detail_state').text()=='待申领' || $('#detail_state').text()=='已结单'){
        layer.msg("该状态下不能执行此操作!");
        return;
    }
    var orderId=$('#orderId').val()
    layer.open({
        type: 2,
        title: '处理',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area : [ '580px', '430px' ],
        content: prefix + '/deal/'+orderId // iframe的url
    });
}



function downFile(filePath) {
    var file = filePath.split(",");
    fileName = file[0];
    filePath = file[1];

    var url = prefix +'/downFile';
    var form=$("<form>");//定义一个form表单
    form.attr("style","display:none");
    form.attr("target","");
    form.attr("method","post");//请求类型
    form.attr("action",url);//请求地址
    $("body").append(form);//将表单放置在web中
    /**
     *1.tt是参数
     tt1相当于关键字。tt2相当于关键字的值
     *
     */
    var input1=$("<input>");
    input1.attr("type","hidden");
    input1.attr("name","fileName");
    input1.attr("value",fileName);
    form.append(input1);

    var input2=$("<input>");
    input2.attr("type","hidden");
    input2.attr("name","filePath");
    input2.attr("value",filePath);
    form.append(input2);
    form.submit();//表单提交

}