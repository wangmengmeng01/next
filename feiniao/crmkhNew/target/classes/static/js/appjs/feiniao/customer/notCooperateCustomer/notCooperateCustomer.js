var prefix = "/crmkh/customer/notCooperateCustomer"
$(function() {
    layui.use(['upload','laydate'], function () {
        var upload = layui.upload;
        var laydate = layui.laydate;
        //执行一个laydate实例
        laydate.render({
            elem: '#startDate', //指定元素
            max:0,//设置最大范围内的日期时间值
            showBottom: false
        });
        laydate.render({
            elem: '#endDate', //指定元素
            max:0,
            showBottom: false
        });
        laydate.render({
            elem: '#startVisitDate', //指定元素
            max:0,//设置最大范围内的日期时间值
            showBottom: false
        });
        laydate.render({
            elem: '#endVisitDate', //指定元素
            max:0,
            showBottom: false
        });
    });
	load();
	
	//上传excel按钮
    layui.use('upload', function () {
        var upload = layui.upload;
        //执行实例
        var uploadInst = upload.render({
            elem: '#uploadbtn', //绑定元素
            url: prefix+'/importExcel', //上传接口
            size: 1000,
            accept: 'file',
            exts: 'xls|xlsx',
            done: function (r) {
            	layer.closeAll('loading');
            	$('#uploadbtn').attr("disabled",false);
            	$('#uploadbtn').removeAttr("readonly");//去除input元素的readonly属性
                layer.msg(r.msg);
                reLoad();
            },
            error: function (r) {
                layer.msg(r.msg);
            },
            before: function(obj){ 
            	layer.load(1);
            	$('#uploadbtn').attr("disabled",true);
            	$('#uploadbtn').attr("readonly","readonly")//将input元素设置为readonly  
            }
        });
    });	
    
    //刷新按钮
     $("#refresh").click(function(){ $('#exampleTable').bootstrapTable('refresh'); }); 
});

function load() {
    $('#exampleTable')
			.bootstrapTable(
					{
						method : 'get', // 服务器数据的请求方式 get or post
						url : prefix + "/list", // 服务器数据的加载地址
					//	showRefresh : true,
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
						//showFooter: true, //统计列求和 sum、average等
						sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
						
						//grimm 双击触发的事件
	            		/*onDblClickRow: function (row) {
	            			edit(row.id);
	            		},*/
	            		
						queryParams : function(params) {
							return {
								//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
								limit: params.limit,
								offset:params.offset,
                                customerName:$('#customerName').val(),
                                startDate:$('#startDate').val(),
								endDate:$('#endDate').val(),
								state:$('#state').val(),
                                customerPhone:$('#customerPhone').val(),
                                branchCode:$('#branchCode').val(),
                                
                                provinceName:$('#provinceName').val(),
                                city:$('#city').val(),
                                provinceVisit:$('#provinceVisit').val(),
                                startVisitDate:$('#startVisitDate').val(),
                                endVisitDate:$('#endVisitDate').val()
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
								// 								{
								// 	field : 'id',
								// 	title : '序号',
								// 	visible:false
								// },
																{
									field : 'province', 
									title : '省' 
								},
																{
									field : 'city', 
									title : '市' 
								},
																{
									field : 'customerName', 
									title : '客户名称' 
								},
																{
									field : 'provinceVisit', 
									title : '是否拜访' 
								},
																{
									field : 'provinceVisitTime', 
									title : '拜访时间' 
								},
																{
									field : 'contactName', 
									title : '联系人' 
								},
																{
									field : 'productType', 
									title : '产品类目' 
								},
																{
									field : 'dayAverageAmount', 
									title : '日均单量' 
								},
																{
									field : 'branchCode', 
									title : '网点编码' 
								},
																{
									field : 'branchName', 
									title : '网点名称' 
								},
																{
									field : 'time', 
									title : '上传日期' 
								},
																{
									field : 'state', 
									title : '状态' 
								},
																{
									field : 'remark', 
									title : '备注' 
								},
																{
									title : '操作',
									field : 'id',
									align : 'center',
									formatter : function(value, row, index) {
                                        var e = "<a class="+s_edit_h+" href='javascript:void(0);' style='color:blue;' onclick='edit(\""+row.id+"\");'>"+' <font style="text-decoration:underline">编辑</font> '+'</a>';

                                        var d = "<a class="+s_seeReport_h+" href='javascript:void(0);' style='color:blue;' onclick='seeReport(\""+row.id+"\");'>"+' <font style="text-decoration:underline">查看</font> '+'</a>';

                                        var f = "<a class="+s_deal_h+" href='javascript:void(0);' style='color:blue;' onclick='deal(\""+row.id+"\");'>"+' <font style="text-decoration:underline">处理</font> '+'</a>';

                                        var g = "<a class="+s_remove_h+" href='javascript:void(0);' style='color:blue;' onclick='remove(\""+row.id+","+row.state+"\");'>"+' <font style="text-decoration:underline">删除</font> '+'</a>';

                                        var h = "<a class="+s_boundVip_h+" href='javascript:void(0);' style='color:blue;' onclick='boundVip(\""+row.id+","+row.state+"\");'>"+' <font style="text-decoration:underline">绑定VIP账号</font> '+'</a>';

//											if(row.type=="1"){
//												return g+d+e;
//											}else{
//												return f+d;
//											}
                                        return e + d + f + g + h;
									}
								} ]
					});
    
    //发起ajax获取红色字的数据信息,在后端拼接好了展示出来返回String
    $.ajax({
        cache : true,
        type : "GET",
        url : prefix + "/getSummary",
        async : false,
        error : function(request) {
            $('#summary').text("获取失败");
        },
        success : function(data) { //data是返回的hash,key之类的值，key是定义的文件名
			$('#summary').text(data.summary);
        }
    });

}
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}

//导出按钮
$('#exportExcelbtn').click(function(){
    var $eleForm = $("<form method='post' enctype='application/x-www-form-urlencoded'>" +
        "<input type='hidden' name='customerName' value="+$('#customerName').val()+">" +
        "<input type='hidden' name='startDate' value="+$('#startDate').val()+">" +
        "<input type='hidden' name='endDate' value="+$('#endDate').val()+">" +
        "<input type='hidden' name='customerPhone' value="+$('#customerPhone').val()+">" +
        "<input type='hidden' name='state' value="+ $('#state').val()+">" +
        "<input type='hidden' name='branchCode' value="+ $('#branchCode').val()+"></form>");

    $eleForm.attr("action",prefix+"/exportExcel");

    $(document.body).append($eleForm);

    //提交表单，实现下载
    layer.msg("下载成功");
    $eleForm.submit();
});
	
function add() {
	layer.open({
		type : 2,
		title : '新建',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '830px', '520px' ],
		content : prefix + '/add' // iframe的url
        /*btn: ['提交', '取消'],
        yes: function(index,layero){
            $(layero).find("iframe")[0].contentWindow.submitForm();
        },
        btn2: function(index){
            layer.close(index);
        }*/
	});
}

function edit(id) {
	if(s_edit_h=='hidden'){
		return;
	};
	
	layer.open({
		type : 2,
		title : '编辑',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '830px', '520px' ],
		content : prefix + '/edit/' + id // iframe的url
	});
}

//查看按钮
function seeReport(id) {
    if(s_seeReport_h=='hidden'){
        return;
    };
    layer.open({
        type : 2,
        title : '查看',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '830px', '520px' ],
        content : prefix + '/seeReport/'+ id  // iframe的url
    });
}

//处理按钮
function deal(id) {
    if(s_deal_h=='hidden'){
        return;
    };
    $.ajax({
        cache : true,
        type : "GET",
        url : prefix + '/dealBefore/'+ id,
        async : false,
        error : function(request) {
            $('#summary').text("请求失败");
        },
        success : function(data) { //data是返回的hash,key之类的值，key是定义的文件名
            if(data==200){
                layer.open({
                    type : 2,
                    title : '处理',
                    maxmin : true,
                    shadeClose : false, // 点击遮罩关闭层
                    area : [ '830px', '520px' ],
                    content : prefix + '/deal/'+ id  // iframe的url
                });
			}else {
                layer.alert('无法处理');
                return;
			}

        }
    });

}

//绑定vip账号按钮
function boundVip(data) {
	var result = data.split(",");
	var id = result[0];
	var state=result[1];
	if(state=="未达成合作"){
		alert("该状态下不能绑定vip账号")
		return false;
	}
    if(s_boundVip_h=='hidden'){
        return;
    };
    layer.open({
        type : 2,
        title : '绑定VIP账号',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '550px', '325px' ],
        content : prefix + '/boundVip/'+ id  // iframe的url
    });
}

function remove(data) {
	//未合作状态不允许删除数据
    var result = data.split(",");
    var id = result[0];
    var state=result[1];
    if(state=="未达成合作"){
        alert("该状态下不能执行删除操作")
        return false;
    }

	layer.confirm('确定要删除选中的记录？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix+"/remove",
			type : "post",
			data : {
				'id' : id
			},
			success : function(r) {
				if (r.code==0) {
					layer.msg(r.msg);
					reLoad();
				}else{
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

function query(){
    var startDate = $('#startDate').val(),
        endDate = $('#endDate').val();
    var startTime = new Date(startDate.replace(/-/g,"/")).getTime(),
        endTime = new Date(endDate.replace(/-/g,"/")).getTime();
    if(startTime>endTime){
        layer.msg('开始时间不能大于结束时间！');
        return;
    }
    $("#exampleTable").bootstrapTable('destroy');
    load();
}

function resetForm(){
    document.getElementById("searchUser").reset();
}

//查看
/*
function seeReport(data){
    var result = data.split(',');
    var index = layer.open({
        type : 2,
        title : '查看',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '830px', '520px' ],
        content : prefix  + '/openShangBaoSee/'+provinceid+"/"+report_date+"/"+"2"// iframe的url
    });
}*/
