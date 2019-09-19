var prefix = "/vipkf/sys/loginUser";
$(function() {
	var deptId = '';
    layui.use(['upload','laydate'], function () {
        var upload = layui.upload;
        var laydate = layui.laydate;
        //执行一个laydate实例
        laydate.render({
            elem: '#start_date', //指定元素
            max:0,//设置最大范围内的日期时间值
            showBottom: false
        });
        laydate.render({
            elem: '#end_date', //指定元素
            max:0,
            showBottom: false
        });
        //给input赋值---开始时间默认当前时间的T-1天，结束时间默认当前时间的T-1天
        // $('#start_date').val(GetDateStr(0));
        // $('#end_date').val(GetDateStr(0));
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
    getTreeData();
	load(deptId);
});
//js获取当前日期前后N天的方法:
function GetDateStr(AddDayCount) {
    var date = new Date();
    date.setDate(date.getDate()+AddDayCount);//获取AddDayCount天后的日期
    var y = date.getFullYear();
    var m = changeTime(date.getMonth()+1);//获取当前月份的日期
    var d = changeTime(date.getDate()-1);
    return y+"-"+m+"-"+d;
}
function changeTime(t){
    return t<10 ? "0"+ t : t;
}

function load(deptId) {
	$('#exampleTable')
		.bootstrapTable(
			{
				method : 'get', // 服务器数据的请求方式 get or post
				url : prefix + "/list", // 服务器数据的加载地址
				// showRefresh : true,
				// showToggle : true,
				// showColumns : true,
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
				// search : true, // 是否显示搜索框
				showColumns : true, // 是否显示内容下拉框（选择显示的列）
				sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者
				// "server"
				queryParams : function(params) {
					return {
						// 说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
						limit : params.limit,
						offset : params.offset,
						mobile : $('#mobile').val(),
						role : $('#role').val(),
                        institution : $('#institution').val(),
						username : $('#searchUserName').val(),
                        startupdateTime:$('#start_date').val(),
                        endupdateTime:$('#end_date').val(),
                        status:$('#useType').val(),
						deptId : deptId
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
                        field : 'userId',
                        title : '序号'
                    },
                    {
                        field : 'username',
                        title : '登录人账号'
                    },
					{
						field : 'name',
						title : '登录人姓名'
					},
					{
						field : 'role',
						title : '所属角色'
					},
					{
						field : 'institution',
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
                        field : 'macAdress',
                        title : '有线MAC地址'
                    },
                    {
                        field : 'wirelessMacAdress',
                        title : '无线MAC地址'
                    },
                    {
                        field : 'gmtModified',
                        title : '修改时间'
                    },
                    {
                        field : 'updateName',
                        title : '修改人'
                    },
					/*{
						field : 'email',
						title : '邮箱'
					},*/
					{
						field : 'status',
						title : '使用状态',
						align : 'center',
						formatter : function(value, row, index) {
							if (value == '0') {
								return '<span class="label label-danger">已停用</span>';
							} else if (value == '1') {
								return '<span class="label label-primary">使用中</span>';
							}
						}
					},
					{
						title : '操作',
						field : 'id',
						align : 'center',
						formatter : function(value, row, index) {
							var h = '<a  class="btn btn-primary btn-sm ' + s_editBigarea_h + '" href="#" mce_href="#" title="维护大区" onclick="editBigarea(\''
							+ row.userId
							+ '\')"><i class="fa fa-edit "></i></a> ';
							var g = '<a  class="btn btn-primary btn-sm ' + s_editProvince_h + '" href="#" mce_href="#" title="维护省" onclick="editProvince(\''
								+ row.userId
								+ '\')"><i class="fa fa-edit "></i></a> ';
							var e = '<a  class="btn btn-primary btn-sm ' + s_edit_h + '" href="#" mce_href="#" title="编辑" onclick="edit(\''
								+ row.username
								+ '\')"><i class="fa fa-edit "></i></a> ';
							var d = '<a class="btn btn-warning btn-sm ' + s_remove_h + '" href="#" title="停用/激活"  mce_href="#" onclick="remove(\''
								+ row.userId
								+ '\')"><i class="fa fa-remove"></i></a> ';
							var f = '<a class="btn btn-success btn-sm ' + s_resetPwd_h + '" href="#" title="重置密码"  mce_href="#" onclick="resetPwd(\''
								+ row.userId
								+ '\')"><i class="fa fa-key"></i></a> ';
							return h + g + e + d + f;
						}
					} ]
			});
}
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}
function add() {
	// iframe层
	layer.open({
		type : 2,
		title : '新增账号',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/add',
        btn: ['提交', '取消'],
	    yes: function(index,layero){
	    	$(layero).find("iframe")[0].contentWindow.submitForm();
	    },
	    btn2: function(index){
	      layer.close(index);
	    }
	});
}
function remove(id) {
	layer.confirm('确定要修改选中的记录？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix+"/remove",
			type : "post",
			data : {
				'id' : id,
			},
			success : function(r) {
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
function editProvince(id) {
	layer.open({
		type : 2,
		title : '维护省',
		maxmin : true,
		shadeClose : false,
		area : [ '800px', '520px' ],
		content : prefix + '/editProvince/' + id, // iframe的url
		btn: ['提交', '取消'], 
	    yes: function(index,layero){
	    	$(layero).find("iframe")[0].contentWindow.submitForm();
	    },
	    btn2: function(index){
	      layer.close(index);
	    }
	});
}
function editBigarea(id) {
	layer.open({
		type : 2,
		title : '维护大区',
		maxmin : true,
		shadeClose : false,
		area : [ '800px', '520px' ],
		content : prefix + '/editBigarea/' + id, // iframe的url
		btn: ['提交', '取消'], 
	    yes: function(index,layero){
	    	$(layero).find("iframe")[0].contentWindow.submitForm();
	    },
	    btn2: function(index){
	      layer.close(index);
	    }
	});
}
function edit(id) {
	layer.open({
		type : 2,
		title : '编辑账号',
		maxmin : true,
		shadeClose : false,
		area : [ '800px', '520px' ],
		content : prefix + '/edit/' + id, // iframe的url
		btn: ['提交', '取消'], 
	    yes: function(index,layero){
	    	$(layero).find("iframe")[0].contentWindow.submitForm();
	    },
	    btn2: function(index){
	      layer.close(index);
	    }
	});
}
function resetPwd(id) {
	layer.open({
		type : 2,
		title : '重置密码',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '400px', '260px' ],
		content : prefix + '/resetPwd/' + id // iframe的url
	});
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
		var ids = [];
		// 遍历所有选择的行数据，取每条数据对应的ID
		$.each(rows, function(i, row) {
			ids[i] = row['userId'];
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
	}, function() {});
}
function getTreeData() {
	$.ajax({
		type : "GET",
		url : "/vipkf/system/sysDept/tree",
		success : function(tree) {
			loadTree(tree);
		}
	});
}
function loadTree(tree) {
	$('#jstree').jstree({
		'core' : {
			'data' : tree
		},
		"plugins" : [ "search" ]
	});
	$('#jstree').jstree().open_all();
}
$('#jstree').on("changed.jstree", function(e, data) {
	if (data.selected == -1) {
		var opt = {
			query : {
				deptId : '',
			}
		};
		$('#exampleTable').bootstrapTable('refresh', opt);
	} else {
		var opt = {
			query : {
				deptId : data.selected[0],
			}
		};
		$('#exampleTable').bootstrapTable('refresh',opt);
	}

});

//导出按钮
$('#exportExcelbtn').click(function(){
    var $eleForm = $("<form method='post' enctype='application/x-www-form-urlencoded'>" +
		"<input type='hidden' name='mobile' value="+$('#mobile').val()+">" +
		"<input type='hidden' name='role' value="+$('#role').val()+">" +
		"<input type='hidden' name='institution' value="+$('#institution').val()+">" +
       " <input type='hidden' name='username' value="+$('#searchUserName').val()+">" +
        "<input type='hidden' name='status' value="+ $('#useType').val()+">" +
        "<input type='hidden' name='startupdateTime' value="+ $('#start_date').val()+">" +
		"<input type='hidden' name='endupdateTime' value="+ $('#end_date').val()+"></form>");

    $eleForm.attr("action",prefix+"/exportExcel");

    $(document.body).append($eleForm);

    //提交表单，实现下载
    layer.msg("下载成功");
    $eleForm.submit();
});

function query(){
    var startDate = $('#start_date').val(),
        endDate = $('#end_date').val();
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
