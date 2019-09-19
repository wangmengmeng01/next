var prefix = "/vipkf/market/marketOccupancyReport";
$(function() {
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
	if($('#reportStatus').val()=="0"||$('#reportStatus').val()=="2"||$('#reportStatus').val()=="4"){
		$('#formAudit').hide();
		$('#formRemark').hide();	
		$('#group-submitbtn').hide();
	
	}else if($('#reportStatus').val()=="1"){
		$('#formAudit').show();
		$('#formRemark').show();	
		$('#group-submitbtn').show();
		$('#group-querybtn').hide();
	}
	var type = $('#reportStatus').val();
	if(type=="0"||type=="4"){
	$('#exampleTable')
			.bootstrapTable(
					{
						method : 'get', // 服务器数据的请求方式 get or post
						url : prefix + "/listSearch", // 服务器数据的加载地址
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
						cache : false,
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
								offset: params.offset,
								provinceID: $('#provinceid').val(),
								report: $('#reportDate').val(),
//								type: $('#reportStatus').val()
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
								/*{
									checkbox : true
								},*/
																{
									field : 'recordId', 
									title : '数据记录主键ID',
									visible:false
								},								{
									field : 'provinceid', 
									title : '省ID',
									visible:false
								},	{
									field : 'reportNian', 
									title : '上报年份',
									visible:false

								},
																{
									field : 'reportYue', 
									title : '上报月份',
									visible:false

								},
																{
									field : 'cityname', 
									title : '地区/公司' 
								},
																{
									field : 'proportionYd', 
									title : '韵达(单位:%)',
									validate: function(value) { //字段验证
										return toDecimal2(value);
									},
									editable:{
										type: 'text',
										mode: "inline"
//										disabled: true
				                    }
								},
								{
									field : 'proportionYt', 
									title : '圆通(单位:%)',
									valida11te: function(value) { //字段验证
										return toDecimal2(value);
									},
									editable:{
										type: 'text',
										mode: "inline"
//										disabled: true
				                    }
								},
								{
									field : 'proportionZt', 
									title : '中通(单位:%)',
									validate: function(value) { //字段验证
										return toDecimal2(value);
									},
									editable:{
										type: 'text',
										mode: "inline"
//										disabled: true
				                    }
								},
								{
									field : 'proportionSt', 
									title : '申通(单位:%)',
									validate: function(value) { //字段验证
										return toDecimal2(value);
									},
									editable:{
										type: 'text',
										mode: "inline"
//										disabled: true
				                    }
								},
								{
									field : 'proportionBs', 
									title : '百世(单位:%)' ,
									validate: function(value) { //字段验证
										return toDecimal2(value);
									},
									editable:{
										type: 'text',
										mode: "inline"
//										disabled: true
				                    }
								}],
								   onEditableSave: function (field, row, oldValue, $el) {
									   var json = {};
									   json.cityid = row.cityid;
									   json.reportDate = row.reportNian+"-"+ row.reportYue;
									   json.proportionYd = row.proportionYd;
									   json.proportionZt = row.proportionZt;
									   json.proportionBs = row.proportionBs;
									   json.proportionYt = row.proportionYt;
									   json.proportionSt = row.proportionSt;
									   json.recordId = row.recordId;
                                       json.provinceid =row.provinceid;
                                       //对修改的数据进行验证
                                       if(!checkproportion(row.proportionYd)||!checkproportion(row.proportionZt)
                                    		   ||!checkproportion(row.proportionBs)||!checkproportion(row.proportionYt)
                                    		   ||!checkproportion(row.proportionSt)){
                                    	 // alert('zzzzz');
                                    	  layer.msg("修改数据大小应是1-100，包含两位小数,不符合要求的数据点击确定也不会保存！谢谢!");
                                       }else{
                                    	   $.ajax({
   						                    type: "GET",
   						                    url: prefix + '/cacheSave/',
   						                    data: json,
   						                    dataType: 'JSON',
   						                    async : false,
   						                    success: function (r) { 
   						                        if (r.code==0) { 
//   						                        	$("#allocation").removeClass("btn-warning").attr("disabled","disabled");
//   						        	        		$("#split").removeClass("btn-info").attr("disabled","disabled");
//   						        	        		$("#modify").removeClass("btn-primary").attr("disabled","disabled");
   						                        }
   						                    }

   						                })                                    	   
                                       }
				            }
					});
	}else{		
		$('#exampleTable')
		.bootstrapTable(
				{
					method : 'get', // 服务器数据的请求方式 get or post
					url : prefix + "/listSearch", // 服务器数据的加载地址
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
//            		onDblClickRow: function (row) {
//            			edit(row.id);
//            		},
            		
					queryParams : function(params) {
						return {
							//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
							limit: params.limit,
							offset: params.offset,
							provinceID: $('#provinceid').val(),
							report: $('#reportDate').val(),
//							type: $('#reportStatus').val()
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
							/*{
								checkbox : true
							},*/
															{
								field : 'recordId', 
								title : '数据记录主键ID',
								visible:false

							},
															{
								field : 'cityname', 
								title : '地区/公司' 
							},
															{
								field : 'proportionYd', 
								title : '韵达(单位:%)'
							},
							{
								field : 'proportionYt', 
								title : '圆通(单位:%)'
							},
							{
								field : 'proportionZt', 
								title : '中通(单位:%)'
							},
							{
								field : 'proportionSt', 
								title : '申通(单位:%)'
							},
							{
								field : 'proportionBs', 
								title : '百世(单位:%)'
							}]
				});
	}
}
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}

function toDecimal2(x){
	 //var reg = /^(([1-9]{1}\d*)|(0{1}))(\.\d{2})$/;
	//修改框所有验证参数必须是0-100，不包含0和100，且必须保留两位小数
	var reg = /^[1-9][0-9]?(\.\d{2})$/;
	 //console.log(reg5.test(x));
	 /*if(!$.trim(x)) {
			return '不能为空';
	 }*/
	return reg.test(x);
 }

//数值必须是惺惺惜惺惺x
function checkproportion(x){
	if(toDecimal2(x)){
		//大小边界检测
	//	if(x >= 1 &&  <=100){
			return  true;
		//}
	}
	return false;
 }

 function submit(){
	 if($('#reportStatus').val()=="2"){ 
			var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
			parent.layer.close(index);	 
	 }
	 
    var json={};
	var provinceid = $('#provinceid').val();
	var report_date = $('#reportDate').val();
	json.provinceid = provinceid;
	json.reportDate = report_date;
	
	layer.confirm('修改后，审核会以修改后的数据为准，是否确认？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			   type: "POST",
			   url:prefix+ "/upData",
			   dataType:"json",
		       data: json,
			   success: function(data){
					if (data && data.code == 0) {
						parent.layer.msg("占比上报成功");				
					} else {
						parent.layer.msg("占比上报失败");				
					}
					parent.reLoad();
					var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
					parent.layer.close(index);
			   }
			});
	})
	
 }
 
 
 function submitAudit(){
       var select = $('#auditResult option:selected').val();
	   var audit_remarks = document.getElementById("auditRemarks");
	   var aujson = {};
	   var provinceid = $('#provinceid').val();
	   var report_date = $('#reportDate').val();
		if(select=="" || select==null){
		    layer.msg("请选中审核结果");
			return false;
		}
		if(audit_remarks.value.length>100){
		    layer.msg("审核备注小于100字,谢谢!");
			return false;
		}
	   aujson.provinceid = provinceid;
	   aujson.reportDate = report_date;
	   aujson.auditResult = select;
	   aujson.auditRemarks = audit_remarks.value;
	   $.ajax({
		   type: "POST",
		   url: prefix+"/auditData",
		   dataType:"json",
	       data: aujson,
		   success: function(data){
			  // alert(data.code);
				if (data && data.code == 0) {
//					$("#reportTable").dialog('close');
					parent.layer.msg("审核成功");
					$('#exampleTable').bootstrapTable('refresh');
				} else {
					parent.layer.msg('审核失败，已审核过');
				}
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);
		   }
		}); 
	 
	 }
 function quitAudit(){
	parent.reLoad();
	var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
	parent.layer.close(index);
 }

//导出excel
function exportExcel(action) {
	layer.load(1);
	$('#exportExcelbtn').attr("disabled",true);
	$('#exportExcelbtn').attr("readonly","readonly");//将input元素设置为readonly
	
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
