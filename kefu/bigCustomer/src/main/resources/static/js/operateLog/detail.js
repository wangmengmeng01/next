var prefix = "/vipkf/system/operateLog"
$(function() {
    layui.use('laydate', function(){
        var laydate = layui.laydate;
        //执行一个laydate实例
        laydate.render({
            elem: '#startTime', //指定元素
            max:0,//设置最大范围内的日期时间值
            showBottom: false
        });
        laydate.render({
            elem: '#endTime', //指定元素
            max:0,
            showBottom: false
        });
        //给input赋值---开始时间默认T-1天，结束时间默认T-1天
        $('#startTime').val(GetDateStr(0));
        $('#endTime').val(GetDateStr(0));

        load();
    });
});

function load() {
	$('#detailTable')
			.bootstrapTable(
					{
						method : 'get', // 服务器数据的请求方式 get or post
						url : prefix + "/detailList", // 服务器数据的加载地址
						showRefresh : true,
					//	showToggle : true,
					//	showColumns : true,
						iconSize : 'outline',
						toolbar : '#exampleToolbar',
						striped : true, // 设置为true会有隔行变色效果
						dataType : "json", // 服务器返回的数据类型
						pagination : true, // 设置为true会在底部显示分页条
						// queryParamsType : "limit",
						// //设置为limit则会发送符合RESTFull格式的参数
						singleSelect : false, // 设置为true将禁止多选
						// contentType : "application/x-www-form-urlencoded",
						// //发送到服务器的数据编码类型
						pageSize : 10, // 如果设置了分页，每页数据条数
						pageNumber : 1, // 如果设置了分布，首页页码
						//search : true, // 是否显示搜索框
						showColumns : false, // 是否显示内容下拉框（选择显示的列）
						sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
						
					    //grimm
					    //双击触发的事件
	            		/*onDblClickRow: function (row) {
	            			edit(row.sessionId);
	            		},*/
	            		
						queryParams : function(params) {
							return {
								//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
								limit: params.limit,
								offset: params.offset,
                                sessionId:$('#sessionId').val(),
                                userId:$('#userId').val(),
								operation:$('#operation').val().trim(),
                                startTime:$('#startTime').val(),
                                endTime:$('#endTime').val()
								/*orderId: $('#searchOrderId').val(),
								status: $('#searchOrderStatus').val()*/
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
						columns : [
								{
									checkbox : true
								},
                                {
                                    field : 'id',
                                    title : '序号'
                                },
								{
									field : 'sessionId',
									title : 'sessionId',
                                    visible: false
								},
								{
									field : 'userName',
									title : '登录人账号'
								},
                                {
                                    field : 'name',
                                    title : '登录人姓名'
                                },
								{
									field : 'operation',
									title : '访问页面'
								},
								{
									field : 'operationTyp',
									title : '操作类型'
								},
                                {
                                    field : 'createTime',
                                    title : '操作时间'
                                },
								{
									field : 'params',
									title : '查询条件'
								}]
					});
}
function search() {
	$('#detailTable').bootstrapTable('refresh');
}

function batchRemove() {
    var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
    if (rows.length == 0) {
        layer.msg("请选择要删除的数据");
        return;
    }
    layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
        btn : [ '确定', '取消' ]
        // 按钮
    }, function() {
        var ids = new Array();
        // 遍历所有选择的行数据，取每条数据对应的ID
        $.each(rows, function(i, row) {
            ids[i] = row['id'];
        });
        $.ajax({
            type : 'POST',
            data : {
                "ids" : ids
            },
            url : prefix + '/batchRemove',
            success : function(r) {
                if (r.code == 0) {
                    layer.msg(r.msg);
                    reLoad();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    }, function() {
    });
}

function resetForm(){
    document.getElementById("searchLog").reset();
}

//js获取当前日期前后N天的方法:
function GetDateStr(AddDayCount) {
    var date = new Date();
    date.setDate(date.getDate()+AddDayCount);//获取AddDayCount天后的日期
    var y = date.getFullYear();
    var m = changeTime(date.getMonth()+1);//获取当前月份的日期
    var d = changeTime(date.getDate());
    return y+"-"+m+"-"+d;
}
//如果月份和日期为一位数，需要在前面补0
function changeTime(t){
    return t<10 ? "0"+ t : t;
}

//导出按钮
$('#exportExcelbtn').click(function(){
    var $eleForm = $("<form method='post' enctype='application/x-www-form-urlencoded'><input type='hidden' name='operation' value="+$('#operation').val().trim()+"><input type='hidden' name='sessionId' value="+$('#sessionId').val().trim()+"><input type='hidden' name='startTime' value="+ $('#startTime').val()+"><input type='hidden' name='endTime' value="+ $('#endTime').val()+"></form>");

    $eleForm.attr("action",prefix+"/exportExcel");

    $(document.body).append($eleForm);

    //提交表单，实现下载
    layer.msg("下载成功");
    $eleForm.submit();
});