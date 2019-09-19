var prefix = "/crmkh/system/loginLog"
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
								userId:$('#userId').val().trim(),
                                mobile:$('#mobile').val().trim(),
                                police:$('#police').val().trim(),
								startTime:$('#startTime').val(),
								endTime:$('#endTime').val()
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
									field : 'userId',
									title : '登录人账号'
								},
																{
									field : 'name',
									title : '登录人姓名'
								},
																{
									field : 'roleName',
									title : '角色'
								},
																{
									field : 'orgName',
									title : '所属机构'
								},
																{
									field : 'idcdNo',
									title : '身份证号'
								},
																{
									field : 'mobile',
									title : '手机号'
								},
																{
									field : 'createTime',
									title : '登陆时间'
								},
																{
									field : 'outTime',
									title : '退出时间'
								},
																{
									field : 'time',
									title : '登陆时长'
								},
																{
									field : 'police',
									title : '是否报警'
								},
																{
									field : 'policeTyp',
									title : '报警类型'
								},
																{
									field : 'userIp',
									title : '用户ip地址'
								},
																{
									field : 'macAdress',
									title : 'mac有线地址'
								},
																{
									field : 'wMacAdress',
									title : 'mac无线地址'
								},
																{
									field : 'operation',
									title : '操作信息',
									visible: false
								},
																{
									title : '操作',
									field : 'id',
									align : 'center',
									class : 'W166',
									formatter : function(value, row, index) {
										var e = '<a href="#" style="color:blue;" mce_href="#" title="查看详情" onclick="detail(\''
											+ row.sessionId
											+ '\')"><font style="text-decoration:underline">查看详情</font></a> ';
                                        var f = '<a style="color:blue;" href="#" mce_href="#" title="查看报警图片" onclick="checkPicture(\''
                                            + row.sessionId
                                            + '\')"><font style="text-decoration:underline">查看报警图片</font></a> ';
										return e+f ;
									}

								} ]
					});
}
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}

//导出excel
function exportExcel(action) {
	layer.load(1);
	$('#exportExcelbtn').attr("disabled",true);
	$('#exportExcelbtn').attr("readonly","readonly")//将input元素设置为readonly

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
	    $('#exportExcelbtn').attr("disabled",false);
	    $('#exportExcelbtn').removeAttr("readonly");//去除input元素的readonly属性
	  };
	  // 发送ajax请求
	  xhr.send()
	}

function add() {
	layer.open({
		type : 2,
		title : '添加',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/add' // iframe的url
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
		area : [ '800px', '520px' ],
		content : prefix + '/edit/' + id // iframe的url
	});
}
function remove(id) {
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

function detail(sessionId) {
    layer.open({
        type : 2,
        title : '详情',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '1100px', '500px' ],
        content : prefix +'/detail/'+ sessionId // iframe的url
    });
}

//查看报警图片(第二种直接给地址,第一个是要求json)
function checkPicture(sessionId) {
	/*$.getJSON(prefix+"/checkPicture/"+ sessionId , function(json){
		layer.photos({
			photos: json //格式见API文档手册页
			,anim: 5 //0-6的选择，指定弹出图片动画类型，默认随机
		});
	});*/
    var im;
    $.ajax({
        type: "GET",
        url: prefix+"/viewDirect/"+ sessionId,
        success: function (data) {
            layer.open({
                type : 1,
                title : "图片",
                maxmin : true,
                offset: '100px',
                area : [ '520px', '400px' ],
                content : "<img style='width: 100%;height: 100%' src="+data+" />"
            });
        }
    });
    /*layer.open({
        type : 2,
        title : "图片",
        maxmin : true,
        offset: '100px',
        area : [ '500px', '400px' ],
        content : "tupian"
    });*/
}

function search() {
    var startTime = $('#startTime').val(),
        endTime = $('#endTime').val();
    if(startTime>endTime){
        layer.msg('开始时间不能大于结束时间！');
        return;
    }
    $('#exampleTable').bootstrapTable('refresh');
}

//重置
function resetForm(){
    document.getElementById("searchLog").reset();
    //给input赋值---开始时间默认T-1天，结束时间默认T-1天
    $('#startTime').val(GetDateStr(0));
    $('#endTime').val(GetDateStr(0));
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
    var $eleForm = $("<form method='post' enctype='application/x-www-form-urlencoded'><input type='hidden' name='userId' value="+$('#userId').val()+"><input type='hidden' name='mobile' value="+$('#mobile').val()+"><input type='hidden' name='startTime' value="+ $('#startTime').val()+"><input type='hidden' name='endTime' value="+ $('#endTime').val()+"></form>");

    $eleForm.attr("action",prefix+"/exportExcel");

    $(document.body).append($eleForm);

    //提交表单，实现下载
    layer.msg("下载成功");
    $eleForm.submit();
});