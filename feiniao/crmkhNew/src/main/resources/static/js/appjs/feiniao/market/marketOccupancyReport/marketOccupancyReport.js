var prefix = "/crmkh/market/marketOccupancyReport";
$(function() {
	
	
   layui.use(['laydate','laypage','layer','form','upload'], function () {
    	$=layui.jquery;
    	var laydate = layui.laydate;
    	layer = layui.layer;
    	form = layui.form;
    	laydate.render({
            	elem: '#month_year' //指定元素
            	,type: 'month'
            });
    });
	 //给input赋值---开始时间默认当前时间，结束时间默认当前时间
	$('#month_year').val(GetDateStr(0));
	
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
    
    load();
    
    //刷新按钮
     $("#refresh").click(function(){ $('#exampleTable').bootstrapTable('refresh'); }); 

});

//js获取当前日期前后N天的方法:
function GetDateStr(AddDayCount) { 
    var date = new Date(); 
    date.setDate(date.getDate()+AddDayCount);
    var y = date.getFullYear(); 
    var m = changeTime(date.getMonth());
    return y+"-"+m; 
 } 
//如果月份和日期为一位数，需要在前面补0
function changeTime(t){
	return t<10 ? "0"+ t : t;
}

/*$('#exportExcelbtn').click(function(){  
	var reportStatus = $("#report_status").find("option:selected").attr("value");
	var data = $('#month_year').val();
	var auditResult = $("#audit_result").find("option:selected").attr("value");
	var provinceid = $('#provinceID').magicSuggest().getValue()[0]==undefined?'':$('#provinceID').magicSuggest().getValue()[0];
	var url = prefix + "/exportExcel"+"?reportStatus="+reportStatus+"&monthYear="+data+"&auditResult="+auditResult+"&provinceid="+provinceid;
	window.open(url);
}); */

function load() {
	var addressIdMagicSuggest = $('#provinceID').magicSuggest({
        width:100,
        allowFreeEntries: false,
        maxSelection:1,
        autoSelect:false,
        valueField:"provinceid",
        displayField:"provincename",
        placeholder:"",
        resultAsString:true,
        selectionStacked: true,
        queryParam:'queryString',
        //下拉框数据的获得：
        data: prefix+'/searchProvinceData'
});
	
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
//	            		onDblClickRow: function (row) {
//	            			edit(row.id);
//	            		},
	            		
						queryParams : function(params) {
							return {
								//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
								limit: params.limit,
								offset:params.offset,
								reportStatus:$("#report_status").find("option:selected").attr("value"),
					          	provinceid:$('#provinceID').magicSuggest().getValue()[0],
					          	auditResult:$("#audit_result").find("option:selected").attr("value"),
					          	monthYear:$('#month_year').val()
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
									field : 'recordId', 
									title : '序号'
									//visible:false
								},
																{
									field : 'bigarea', 
									title : '大区' 
								},
																{
									field : 'provinceid', 
									title : '省ID' ,
									visible:false

								},
																{
									field : 'provincename', 
									title : '省名称' 
								},
																{
									field : 'cityid', 
									title : '城市ID' ,
									visible:false
								},
																{
									field : 'cityname', 
									title : '城市名称' ,
									visible:false
								},
																{
									field : 'responsiblePeople', 
									title : '负责人' 
								},
																{
									field : 'containCity', 
									title : '所含重点城市' ,
									visible:false
								},
								{
									field : 'reportNian', 
									title : '上报年份' 
								},
																{
									field : 'reportYue', 
									title : '上报月份' 
								},
																{
									field : 'reportDate', 
									title : '上报月份' ,
									visible:false
								},
																{
									field : 'reportStatus', 
									title : '上报状态' ,formatter:function(val,row){
										if(row.reportStatus == "2"){
											return "待审核";
										}else if(row.reportStatus == "3"){
											return "未上报";
										}else if(row.reportStatus == "1"){
											return "已审核";
										}
									}
								},
															   {
									field : 'auditResult', 
									title : '审核结果' ,formatter:function(val,row){
										if(row.auditResult == "2"){
											return "虚假上报";
										}else if(row.auditResult == "1"){
											return "如实上报";
										}else if(row.auditResult == "3"){
											return "未上报";
										}else{
											return "";
										}
									}
								},
																{
									field : 'auditRemarks', 
									title : '审核备注' 
								},
																{
									field : 'regionGs', 
									title : '地区/公司' ,
									visible:false
								},
																{
									field : 'monthScore', 
									title : '本月得分' 
								},
							    {
									title : '操作',
									field : 'id',
									align : 'center',
									formatter : function(value, row, index) {
										
										 var e = "<a href='javascript:void(0);' style='color:blue;' onclick='doEdit(\""+row.provinceid+","+row.reportNian+","+row.reportYue+","+row.reportStatus+"\");'>"+' 修改 '+'</a>';  

										 var d = "<a href='javascript:void(0);' style='color:blue;' onclick='seeReport(\""+row.provinceid+","+row.reportNian+","+row.reportYue+"\");'>"+' 查看 '+'</a>';  

										 var f = "<a href='javascript:void(0);' style='color:blue;' onclick='auditReport(\""+row.provinceid+","+row.reportNian+","+row.reportYue+","+row.reportStatus+"\");'>"+' 审核 '+'</a>';  

										 var g = "<a href='javascript:void(0);' style='color:blue;' onclick='doReport(\""+row.provinceid+","+row.reportNian+","+row.reportYue+","+row.reportStatus+"\");'>"+' 上报 '+'</a>';  
										
//											if(row.type=="1"){
//												return g+d+e;
//											}else{
//												return f+d;
//											}
										return e + d + f + g;
									}
								} ]
					});
}
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}

//上报
function doReport(data){
	//权限控制，只有网点有上报权限
	if(s_doReport_h=='hidden'){
		return;
    }
    var result = data.split(',');
	if(result[3]=='1'){
      layer.msg("已审核，不可重复上报");
      return false;	
	}
	var provinceid = result[0];
	var report_date = result[1]+"-"+result[2];
	var index = layer.open({
		type : 2,
		title : '上报占比',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix  + '/openShangBao/'+provinceid+"/"+report_date+"/"+"0"// iframe的url
	});
}

//查看
function seeReport(data){
	//权限控制，只有网点有上报权限
	if(s_seeReport_h=='hidden'){
		return;
    }
    var result = data.split(',');
	var provinceid = result[0];
	var report_date = result[1]+"-"+result[2];
	var index = layer.open({
		type : 2,
		title : '查看',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix  + '/openShangBaoSee/'+provinceid+"/"+report_date+"/"+"2"// iframe的url
	});
}
	
//修改
function doEdit(data){	
	//权限控制，只有网点有修改权限
	if(s_doEdit_h=='hidden'){
		return;
    }
    var result = data.split(',');
	var provinceid = result[0];
	var report_date = result[1]+"-"+result[2];
	if(result[3]=="1"){
	    layer.msg("该条记录已经审核过，不可修改，谢谢！");	
	    return false;
	}
/*	if(result[3]=="3"){
	    layer.msg("该条记录未上报，请先上报，谢谢！");	
	    return false;
	}*/
		var index = layer.open({
			type : 2,
			title : '修改',
			maxmin : true,
			shadeClose : false, // 点击遮罩关闭层
			area : [ '800px', '520px' ],
			content : prefix  + '/openShangBaoXiuGai/'+provinceid+"/"+report_date+"/"+"4"// iframe的url
		});
}

//审核
function auditReport(data){	
	//权限控制，只有网点有审核权限
	if(s_auditReport_h=='hidden'){
		return;
    }
    var result = data.split(',');
	var provinceid = result[0];
	var report_date = result[1]+"-"+result[2];
	if(result[3]=="1"){
	    layer.msg("该条记录已经审核过，不可重复审核，谢谢！");	
	    return false;
	}
	if(result[3] == "3"){
	    layer.msg("该条记录未上报，请先上报，谢谢！");	
	    return false;
	}

	var index = layer.open({
		type : 2,
		title : '审核',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix  + '/openShangBaoShengHe/'+provinceid+"/"+report_date+"/"+"1"// iframe的url
	});
}
function query() {	
	$('#exampleTable').bootstrapTable('refresh');
}

 function resetval(){
	$('#provinceID').magicSuggest().clear(true);
    var select = document.getElementById('report_status');
    select.options[0].selected = true;
    var result = document.getElementById('audit_result');
    result.options[0].selected = true;    
	$("#month_year").val("");
 }
 
 

//导出excel
 function exportExcel(action) {	
 	layer.load(1);
 	$('#exportExcelbtn').attr("disabled",true);
 	$('#exportExcelbtn').attr("readonly","readonly");//将input元素设置为readonly
 	
	var reportStatus = $("#report_status").find("option:selected").attr("value");
	var data = $('#month_year').val();
	var auditResult = $("#audit_result").find("option:selected").attr("value");
	var provinceid = $('#provinceID').magicSuggest().getValue()[0]==undefined?'':$('#provinceID').magicSuggest().getValue()[0];
	var url = prefix + "/exportExcel"+"?reportStatus="+reportStatus+"&monthYear="+data+"&auditResult="+auditResult+"&provinceid="+provinceid;
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
 	        var date = new Date();
 	        var nowTime = date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate()+' '+date.getHours()+date.getMinutes()+date.getSeconds();
 	        a.download = '重点省份对比表-'+nowTime+'.xlsx';
 	        a.href = e.target.result;
 	        $("body").append(a);  // 修复firefox中无法触发click
 	        a.click();
 	        $(a).remove();
 	      };
 	      
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
    }
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
				'recordId' : id
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
		var ids = [];
		// 遍历所有选择的行数据，取每条数据对应的ID
		$.each(rows, function(i, row) {
			ids[i] = row['recordId'];
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
