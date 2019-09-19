var prefix = "/vipkf/bigcustomer/order"
$(function () {
    //日期默认为当前日期
    layui.use(['upload', 'laydate'], function () {
        var laydate = layui.laydate;
        //执行一个laydate实例
        laydate.render({
            elem: '#startDate', //指定元素
            max: 0,//设置最大范围内的日期时间值
            type: 'datetime'
           /* showBottom: false*/
        });
        laydate.render({
            elem: '#endDate', //指定元素
            max: 0,
            type: 'datetime'
           /* showBottom: false*/
        });
        laydate.render({
            elem: '#jieDanStartDate', //指定元素
            max: 0,//设置最大范围内的日期时间值
            type: 'datetime'
            /* showBottom: false*/
        });
        laydate.render({
            elem: '#jieDanEndDate', //指定元素
            max: 0,//设置最大范围内的日期时间值
            type: 'datetime'
            /* showBottom: false*/
        });
        //给input赋值---开始时间默认T-1天，结束时间默认T-1天
        //判断如果时间框中没有赋值的话就默认当天(因为有可能在系统头上有输入运单号默认是当月)
        if($('#startDate').val()=="" || $('#endDate').val()==""){
            $('#startDate').val(GetDateStr(-15," 00:00:00"));
            $('#endDate').val(GetDateStr(0," 23:59:59"));
        }

    });

    //初始化咨询类型
    $.ajax({
        type: 'GET',
        url: prefix + '/getAllConsultype',
        success: function (r) {
         //成功后把数据添加到下拉框中
            for(var i=0;i<r.length;i++){
                $("#consultType").append("<option text="+r[i]+" value="+r[i]+">"+r[i]+"</option>")
            }
        }
    });


    load();

    //刷新按钮
    $("#refresh").click(function () {
        $('#exampleTable').bootstrapTable('refresh');
    });
});

function load() {
    $('#exampleTable')
        .bootstrapTable(
            {
                method: 'get', // 服务器数据的请求方式 get or post
                url: prefix + "/list", // 服务器数据的加载地址
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
                clickToSelect: true,  //是否启用点击选中行
                // contentType : "application/x-www-form-urlencoded",
                // //发送到服务器的数据编码类型
                pageSize: 10, // 如果设置了分页，每页数据条数
                pageNumber: 1, // 如果设置了分布，首页页码
                //search : true, // 是否显示搜索框
                showColumns: false, // 是否显示内容下拉框（选择显示的列）
                //showFooter: true, //统计列求和 sum、average等
                sidePagination: "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
                dataField: "data",//这是返回的json数组的key.默认好像是"rows".这里只有前后端约定好就行
                responseHandler:responseHandler,//请求数据成功后，渲染表格前的方法

                //grimm 双击触发的事件
                /*onDblClickRow: function (row) {
                    edit(row.id);
                },*/

                queryParams: function (params) {
                    //判断批量查询中是否包含中文逗号,如果包含要提示
                    var waybillNum = $("#waybillNum").val();
                    if(waybillNum.indexOf("，") != -1){
                        waybillNum = waybillNum.replace(/，/g,',');
                    }
                    return {
                        //说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                        limit: params.limit,
                        offset: params.offset,
                        sort : 'consult_time',
                        order : params.order,
                        startDate : $("#startDate").val(),
                        endDate : $("#endDate").val(),
                        consultType : $("#consultType").val(),
                        waybillNum : waybillNum,
                        logisticOrderNum : $("#logisticOrderNum").val(),
                        orderId : $("#orderId").val(),
                        priority : $("#priority").val(),
                        shiXiaoState : $("#shiXiaoState").val(),
                        state : $("#state").val(),
                        shenLingI : document.getElementById("shenLingCheckBox").checked,
                        faQiI : document.getElementById("faQiCheckBox").checked,
                        merchant:$("#merchant").val(),
                        organizationNum:$("#organizationNum").val(),
                        dealCode:$("#dealCode").val(),
                        jieDanStartDate:$("#jieDanStartDate").val(),
                        jieDanEndDate:$("#jieDanEndDate").val()
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
                        checkbox: true
                    },
                    {
                        field: 'id',
                        title: '序号'
                    },
                    {
                        field: 'orderId',
                        title: '咨询单号',
                        formatter: function (value, row, index) {
                            var e = '<a style="color:blue;" href="#" mce_href="#" title="查看详情" onclick="detail(\''
                                + JSON.stringify(row).replace(/"/g, '&quot;')
                                + '\')">'+' <font style="text-decoration:underline">'+row.orderId+'</font> '+'</a> ';
                            return e;
                        }
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
                        field: 'merchant',
                        title: '客户'
                    },
                    {
                        field: 'consultTime',
                        title: '咨询时间',
                        sortable: true
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
                        field: 'organizationName',
                        title: '责任机构'
                    },
                    {
                        field: 'shiXiaoState',
                        title: '时效状态',
                        formatter: function (value, row, index) {
                            if(row.shiXiaoState=="超时"){
                                return '<i style="display:inline-block;width: 20px;height: 20px;position: relative;top:4px;background: red"></i>'
                            }else if(row.shiXiaoState=="预警"){
                                return '<i style="display:inline-block;width: 20px;height: 20px;position: relative;top:4px;background: yellow"></i>'
                            }else if(row.shiXiaoState=="正常"){
                                return '<i style="display:inline-block;width: 20px;height: 20px;position: relative;top:4px;background: green"></i>'
                            }
                        }
                    },
                    {
                        field: 'shengYuTime',
                        title: '剩余时间'
                    },
                    {
                        field: 'state',
                        title: '状态'
                    },
                    {
                        field: 'dealCode',
                        title: '处理人'
                    },
                    {
                        field: 'dealPersion',
                        title: '操作人'
                    },
                    {
                        field: 'jieDanResult',
                        title: '结单结果'
                    },
                    {
                        field: 'problemDescription',
                        title: '问题描述',
                        cellStyle:formatTableUnit,
                        formatter :paramsMatter
                    },
                    {
                        field: 'dealTime',
                        title: '最近处理时间'
                    },
                    {
                        field: 'dealContent',
                        title: '最近处理描述',
                        cellStyle:formatTableUnit,
                        formatter :paramsMatter
                    },
                    {
                        field: 'faqiCode',
                        title: '发起人编码'
                    }]
            });
}

function reLoad() {
    $('#exampleTable').bootstrapTable('refresh');
}

//导出excel
function exportExcel(action) {
    layer.load(1);
    $('#exportExcelbtn').attr("disabled", true);
    $('#exportExcelbtn').attr("readonly", "readonly")//将input元素设置为readonly

    var url = prefix + action+"?startDate="+$("#startDate").val()+"&endDate="+$("#endDate").val()+"&consultType="+$("#consultType").val()
    +"&waybillNum="+$("#waybillNum").val()+"&logisticOrderNum="+$("#logisticOrderNum").val()+"&orderId="+$("#orderId").val()+"&priority="+$("#priority").val()+"&shiXiaoState="+$("#shiXiaoState").val()
    +"&state="+$("#state").val()+"&shenLingI="+document.getElementById("shenLingCheckBox").checked+"&faQiI="+document.getElementById("faQiCheckBox").checked+"&merchant="+$("#merchant").val()+"&organizationNum="+$("#organizationNum").val();
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

function shenLing() {
    var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
    if (rows.length == 0) {
        layer.msg("请选择要申领的数据");
        return;
    }
    for (let i = 0; i < rows.length; i++) {
        if(rows[i].state!="待申领"){
            layer.msg("您要申领的数据状态不符合申领要求!");
            return;
        }
    }  
    layer.confirm("确认要申领选中的'" + rows.length + "'条数据吗?", {
        btn: ['确定', '取消']
        // 按钮
    }, function () {
        var orderIds = new Array();
        // 遍历所有选择的行数据，取每条数据对应的ID
        $.each(rows, function (i, row) {
            orderIds[i] = row['orderId'];
        });
        $.ajax({
            type: 'POST',
            data: {
                "orderIds": orderIds
            },
            url: prefix + '/shenLing',
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

function zhiPai() {
    var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
    if (rows.length == 0) {
        layer.msg("请选择要指派的数据");
        return;
    }
    for (let i = 0; i < rows.length; i++) {
        if(rows[i].state!="待申领"){
            layer.msg("您要申领的数据状态不符合指派要求!");
            return;
        }
    }
    var orderIdArr = new Array();
    // 遍历所有选择的行数据，取每条数据对应的ID
    $.each(rows, function (i, row) {
        orderIdArr[i] = row['orderId'];
    });
    var orderIds = orderIdArr.toString();
    layer.open({
        type: 2,
        title: '指派',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area : [ '550px', '325px' ],
        content: prefix + '/zhiPai/'+orderIds, // iframe的url
       /* btn: ['提交', '取消'],
        yes: function (index, layero) {
            console.log($("#dealMan").val())
           //window[layero.find('iframe')[0]['form']].submitForm();
           //$(layero).find("iframe")[0]['form'].submitForm();
        },
        btn2: function (index) {
            layer.close(index);
        }*/
    });
}

function piLiangJieDan() {
    var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
    if (rows.length == 0) {
        layer.msg("请选择要结单数据");
        return;
    }
    var orderIdArr = new Array();
    var consultTypeArr = new Array();
    // 遍历所有选择的行数据，取每条数据对应的ID
    var consultType = rows[0]['consultType'];
    $.each(rows, function (i, row) {
        var state = row['state'];
        if (row['state']=='待处理' || row['state']=='处理中'){
            orderIdArr[i] = row['orderId'];
        }else {
            layer.msg("请检查要结单的数据状态是否正确");
            return false;
        }
        if(consultType!=row['consultType']){
            layer.msg("您勾选的咨询类型不一致,无法批量结单!");
            return false;
        }else {
            consultTypeArr[i] = row['consultType'];
        }
    });
    if(orderIdArr.length<rows.length){
        return;
    }
    if(consultTypeArr.length<rows.length){
        return;
    }
    var orderIds = orderIdArr.toString();
    var endcodeConsultType = encodeURI(consultType);
    layer.open({
        type: 2,
        title: '批量结单',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area : [ '650px', '500px' ],
        content: prefix + '/piLiangJieDan/'+orderIds// iframe的url
    });
}

function detail(orderStr) {
    orderStr = orderStr.replace(/"/g,"&^");
    orderStr = orderStr.replace(/;/g,"&_");
    //发起ajax请求查看详情
    layer.open({
        type : 2,
        title : '咨询单详情页面',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '100%', '100%' ],
        content : prefix + '/detail/' + orderStr // iframe的url
    });
}

//js获取当前日期前后N天的方法:
function GetDateStr(AddDayCount,time) {
    var date = new Date();
    date.setDate(date.getDate()+AddDayCount);//获取AddDayCount天后的日期
    var y = date.getFullYear();
    var m = changeTime(date.getMonth()+1);//获取当前月份的日期
    var d = changeTime(date.getDate());
    return y+"-"+m+"-"+d+time;
}
//如果月份和日期为一位数，需要在前面补0
function changeTime(t){
    return t<10 ? "0"+ t : t;
}

//重置
function resetForm(){
    document.getElementById("searchUser").reset();
    //给input赋值---开始时间默认T-1天，结束时间默认T-1天
    $('#startDate').val(GetDateStr(-15," 00:00:00"));
    $('#endDate').val(GetDateStr(0," 23:59:59"));
}

//请求成功方法
function responseHandler(result){
    var errcode = result.code;//在此做了错误代码的判断
    if(errcode != 200){
        //alert("错误代码" + errcode);
        layer.msg('请勿重复请求');
        return;
    }
    //如果没有错误则返回数据，渲染表格
    return {
        total : result.data.total, //总页数,前面的key必须为"total"
        data :  result.data.rows //行数据，前面的key要与之前设置的dataField的值一致.
    };
};


//表格超出宽度鼠标悬停显示td内容
function paramsMatter(value,row,index, field) {
    var span=document.createElement('span');
    span.setAttribute('title',value);
    span.innerHTML = value;
    return span.outerHTML;
}
//td宽度以及内容超过宽度隐藏
function formatTableUnit(value, row, index) {
    return {
        css: {
        "white-space": 'nowrap',
        "text-overflow": 'ellipsis',
        "overflow": 'hidden',
        "max-width":"100px"
        }
    }
}

function zhuanFa() {
    var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
    if (rows.length == 0) {
        layer.msg("请选择要转发的数据");
        return;
    }
    var orderIdArr = new Array();
    // 遍历所有选择的行数据，取每条数据对应的ID
    $.each(rows, function (i, row) {
        var state = row['state'];
        if (row['state']!='已结单'){
            orderIdArr[i] = row['orderId'];
        }else {
            layer.msg("请检查要转发的数据状态是否正确");
            return false;
        }
    });
    if(orderIdArr.length<rows.length){
        return;
    }
    var orderIds = orderIdArr.toString();
    layer.open({
        type: 2,
        title: '转发',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area : [ '580px', '430px' ],
        content: prefix + '/zhuanFa/'+orderIds// iframe的url
    });
}

function changeCheckFaQiCheckBox() {
    var faQiCheckBoxFlag = document.getElementById("faQiCheckBox").checked;
    if(faQiCheckBoxFlag==true){
        $("#shenLingCheckBox").removeAttr("checked")
        $("#leftBtnDiv").attr("hidden","true")
    }else{
        $("#leftBtnDiv").removeAttr("hidden")
    }
}

function changeCheckShenLingCheckBox() {
    var shenLingCheckBox = document.getElementById("shenLingCheckBox").checked;
    if(shenLingCheckBox==true){
        $("#faQiCheckBox").removeAttr("checked")
        $("#leftBtnDiv").removeAttr("hidden")
    }
}
