var prefix = "/crmkh/customer/customerDealPlan"
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
            elem: '#tongjiStartDate', //指定元素
            max:0,//设置最大范围内的日期时间值
            showBottom: false
        });
        laydate.render({
            elem: '#tongjiEndDate', //指定元素
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
                                startDate:$('#startDate').val(),
                                endDate:$('#endDate').val(),
                                tongjiStartDate:$('#tongjiStartDate').val(),
                                tongjiEndDate:$('#tongjiEndDate').val(),
								organization:$('#organization').val(),
                                organizationH:$('#organizationH').val()
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
									field : 'organization', 
									title : '组织',
									formatter:function(value,row, index){
										if(row.organization!=undefined && row.mcCode==null){
											return "<a href='javascript:void(0);' style='color:blue;' onclick='openBranchTable(\""+row.organization+","+$('#startDate').val()+","+$('#endDate').val()+"\");'>"+value+'</a>';
										}else{
											return value+"("+row.mcCode+")";
										}
									}
								},
																{
									field : 'customerNum', 
									title : '上报客户数' 
								},
																{
									field : 'averageAmount', 
									title : '上报客户量(日均)' 
								},
																{
									field : 'dealCustomerNum', 
									title : '处理客户数' 
								},
																{
									field : 'dealCustomerAmount', 
									title : '处理客户单量' 
								},
																{
									field : 'customerDealRatio', 
									title : '客户处理率' 
								},
																{
									field : 'customerDealAmountRatio', 
									title : '客户处理单量占比' 
								},
																{
									field : 'waitDealCustomerNum', 
									title : '待处理客户数' 
								},
																{
									field : 'changeCooperationAmount', 
									title : '转化合作单量' 
								},
																{
									field : 'customerChangeRatio', 
									title : '客户转化率' 
								},
																{
									title : '操作',
									field : 'id',
									align : 'center',
									formatter : function(value, row, index) {
										var e = '<a class="btn btn-primary btn-sm '+s_edit_h+'" href="#" mce_href="#" title="编辑" onclick="edit(\''
												+ row.id
												+ '\')"><i class="fa fa-edit"></i></a> ';
										var d = '<a class="btn btn-warning btn-sm '+s_remove_h+'" href="#" title="删除"  mce_href="#" onclick="remove(\''
												+ row.id
												+ '\')"><i class="fa fa-remove"></i></a> ';
										var f = '<a class="btn btn-success btn-sm" href="#" title="备用"  mce_href="#" onclick="resetPwd(\''
												+ row.id
												+ '\')"><i class="fa fa-key"></i></a> ';
										return e + d ;
									}
								} ]
					});
}
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}

//导出按钮
$('#exportExcelbtn').click(function(){
    var $eleForm = $("<form method='post' enctype='application/x-www-form-urlencoded'>" +
        "<input type='hidden' name='organization' value="+$('#organization').val()+">" +
        "<input type='hidden' name='startDate' value="+$('#startDate').val()+">" +
        "<input type='hidden' name='endDate' value="+$('#endDate').val()+"></form>");

    $eleForm.attr("action",prefix+"/exportExcel");

    $(document.body).append($eleForm);

    //提交表单，实现下载
    layer.msg("下载成功");
    $eleForm.submit();
});
	
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

function query(){
    var startDate = $('#startDate').val(),
        endDate = $('#endDate').val();
    var tongjiStartDate = $('#tongjiStartDate').val(),
        tongjiEndDate = $('#tongjiEndDate').val();
    var startTime = new Date(startDate.replace(/-/g,"/")).getTime(),
        endTime = new Date(endDate.replace(/-/g,"/")).getTime();
    var tongjiStartTime = new Date(tongjiStartDate.replace(/-/g,"/")).getTime(),
        tongjiEndTime = new Date(tongjiEndDate.replace(/-/g,"/")).getTime();
    if(startTime>endTime){
        layer.msg('开始时间不能大于结束时间！');
        return;
    }
    if(tongjiStartTime>tongjiEndTime){
        layer.msg('开始时间不能大于结束时间！');
        return;
    }
    if(tongjiStartTime<startTime || tongjiEndTime>endTime){
        layer.msg('统计时间范围不能超过上传时间范围！');
        return;
    }
    $("#exampleTable").bootstrapTable('destroy');
    load();
}

function resetForm(){
    document.getElementById("searchUser").reset();
}

function openBranchTable(Sheng) {
    var organizationH = Sheng.split(',')[0];
    var startDateH = Sheng.split(',')[1];
    var endDateH = Sheng.split(',')[2];
    var index = layer.open({
        type : 2,
        title : '客户处理进度表-网点',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '800px', '520px' ],
        content : prefix + '/customerDealPlanBranch/'+organizationH+"/"+startDateH+"/"+endDateH,// iframe的url

        yes: function(index,layero){
            $(layero).find("iframe")[0].contentWindow.submitForm();
        },
        btn2: function(index){
            layer.close(index);
        }
    });
    layer.full(index);
}