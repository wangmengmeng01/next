var prefix = "/crmkh/market/marketOccupancyTaoxi"
	
	$(function() {
		if(Chrome()){
			alert("各位领导,请使用谷歌浏览器进行操作!");
			return;
		}
		// 发送心跳检测,检测页面开关
		heartCheck();
		
		$("#searchType").on("change",function(){
			var timeType = $(this).val();
			switch(timeType){
				case "1":
					$("#group_time").show().siblings(":not('#group_select,#group-querybtn')").hide().find("input").val("").end().find("select option:first").prop("selected",true);
					break;
				case "2":
					$("#group_time").show().siblings(":not('#group_select,#group-querybtn')").hide().find("input").val("").end().find("select option:first").prop("selected",true);
					break;	
				case "3":
					$("#group_month").show().siblings(":not('#group_select,#group-querybtn')").hide().find("input").val("").end().find("select option:first").prop("selected",true);
					break;
				default:
					$("#group_query").children().not("#group_select,#group-querybtn").hide().find("input").val("").end().find("select option:first").prop("selected",true);
					break;
			}
		});
		
		layui.use(['laydate','laypage','layer','form','upload'], function () {
	    	$=layui.jquery;
	    	var laydate = layui.laydate;
	    	layer = layui.layer;
	    	form = layui.form;
	    	
	    	
	    	laydate.render({
		         	elem: '#searchStartDate', //指定元素 绑定执行日期渲染的元素
	         		max:0,//设置最大范围内的日期时间值
				    showBottom: false
		         });
	    	laydate.render({
		         	elem: '#searchEndDate', //指定元素
	         		max:0,//设置最大范围内的日期时间值
				    showBottom: false
		         });
	    	laydate.render({
	            	elem: '#searchMonth' //指定元素
	            	,type: 'month'
	            	,max:0//设置最大范围内的日期时间值
					//showBottom: false	
	            });
	    	
	    	
			//给input赋值---开始时间默认当前时间，结束时间默认当前时间
		    $('#searchStartDate').val(GetDateStr(-1));
		    $('#searchEndDate').val(GetDateStr(-1));
		    $('#searchMonth').val(GetDateStr(0)); 
		    //$('#qu_date').val(GetDateStr(-1));  
		    var select = document.getElementById('searchType');
		    select.options[1].selected = true;
			$("#group_time").show().siblings(":not('#group_select,#group-querybtn')").hide().find("input").val("").end().find("select option:first").prop("selected",true);
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
						pageSize : 50, // 如果设置了分页，每页数据条数
						pageNumber : 1, // 如果设置了分布，首页页码
						//search : true, // 是否显示搜索框
						showColumns : false, // 是否显示内容下拉框（选择显示的列）
						//showFooter: true, //统计列求和 sum、average等
						sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
						dataField: "data",//这是返回的json数组的key.默认好像是"rows".这里只有前后端约定好就行
						responseHandler:responseHandler,//请求数据成功后，渲染表格前的方法
						
						//grimm 双击触发的事件
	            		/*onDblClickRow: function (row) {
	            			edit(row.id);
	            		},*/
	            		
						queryParams : function(params) {
							return {
								//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
								limit: params.limit,
								offset:params.offset,
					           // name:$('#searchName').val(),
					           // username:$('#searchName').val()
								timeType:$('#searchType').val(),
					           startDate:$('#searchStartDate').val(),
								endDate:$('#searchEndDate').val(),
								month:$('#searchMonth').val()
							};
						},
						// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
						// queryParamsType = 'limit' ,返回参数必须包含
						// limit, offset, search, sort, order 否则, 需要包含:
						// pageSize, pageNumber, searchText, sortName,
						// sortOrder.
						// 返回false将会终止请求
						columns : [[
									{field : 'id', title : '序号', align : 'center', rowspan : 2 },
									{field : 'showQuDate', title : '日期', align : 'center', rowspan : 2 ,width : 100},
									{field : 'totalSum', title : '总计(万)', align : 'center', rowspan : 2}, 
										{field : '',title : '韵达', align : 'center', colspan : 2},
				      		   	        {field : '',title : '中通', align : 'center', colspan : 2},
				      		   	        {field : '',title : '圆通', align : 'center', colspan : 2},
				      		   	        {field : '',title : '申通', align : 'center', colspan : 2},
				      		   	        {field : '',title : '百世', align : 'center', colspan : 2},
									{field : 'id',title : '操作',align : 'center', rowspan : 2,
										formatter : function(value, row, index) {
											var e = '<a class="btn btn-primary btn-sm '+s_edit_h+'" href="#" mce_href="#" title="编辑" onclick="edit(\''
													+ row.id
													+ '\')"><i class="fa fa-edit"></i></a> ';
											if(row.id !=null && row.id!="")
											//if(row.id =="1")
												return e ;
											else return;
									}}
								],[
									{field : 'ydSum',title : '揽件量(万)' , align : 'center', colspan : 1},
									{field : 'ydRatio', title : '占比(%)' , align : 'center', colspan : 1},
									{field : 'ztoSum',title : '揽件量(万)' , align : 'center', colspan : 1},
									{field : 'ztoRatio', title : '占比(%)' , align : 'center', colspan : 1},
									{field : 'ytoSum', title : '揽件量(万)' , align : 'center', colspan : 1},
									{field : 'ytoRatio', title : '占比(%)' , align : 'center', colspan : 1},
									{field : 'stoSum', title : '揽件量(万)' , align : 'center', colspan : 1},
									{field : 'stoRatio', title : '占比(%)' , align : 'center', colspan : 1},
									{field : 'bestexSum', title : '揽件量(万)' , align : 'center', colspan : 1},
									{field : 'bestexRatio', title : '占比(%)' , align : 'center', colspan : 1} 
						]]
					});
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

function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}
/**
 * 查询必须满足以下两种情况：
 * 1.开始时间和结束时间必须是同一年同一个月，不能跨域查询
 * 2.开始时间<结束时间
 */
function query(){
	var startDate = $('#searchStartDate').val(),
		endDate = $('#searchEndDate').val(),
		searchType= $('#searchType').val(),
		searchMonth = $('#searchMonth').val(),
		startYear = startDate.substring(0,5),
		endYear = endDate.substring(0,5),
		startMonth = startDate.substring(5,7),
		endMonth = endDate.substring(5,7);
	var startTime = new Date(startDate.replace(/-/g,"/")).getTime(),
		endTime = new Date(endDate.replace(/-/g,"/")).getTime();
	
	if(searchType=="1"|| searchType=="2"){
		if(!startTime || !endTime){
			layer.msg('查询时间不能为空！'); 
			return;
		}
		if(startTime>endTime){
			layer.msg('开始时间不能大于结束时间！'); 
			return;
		}
	}
	if(searchType=="3"){
		if(!searchMonth){
			layer.msg('查询时间不能为空！'); 
			return;
		}
	}
	$("#exampleTable").bootstrapTable('destroy');
	load();
}


//导出excel
function exportExcel(action) {
	layer.load(1);
	$('#exportExcelbtn').attr("disabled",true);
	$('#exportExcelbtn').attr("readonly","readonly")//将input元素设置为readonly 
	
	  //var url = prefix + action;  //action后面拼接参数   根据情况自行修改
	  var url = prefix + action+"?startDate="+$('#searchStartDate').val()+"&endDate="+$('#searchEndDate').val();
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
	        a.download = 'data.xlsx';//导出表的表名   自行修改
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
function resetForm(){
	document.getElementById("searchExampleData").reset(); 
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