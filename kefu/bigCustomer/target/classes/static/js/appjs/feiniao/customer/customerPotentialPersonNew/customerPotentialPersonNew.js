var prefix = "/vipkf/customer/customerPotentialPersonNew";
$(function() {
	if(Chrome()){
		alert("各位领导,请使用谷歌浏览器进行操作!");
		return;
	}
	// 发送心跳检测,检测页面开关
	heartCheck();
	load();
	
	//上传excel按钮
    layui.use(['upload','laydate'], function () {
        var upload = layui.upload;
        var laydate = layui.laydate;
        //执行一个laydate实例
		laydate.render({
		    elem: '#startsearchUpdateTime',//指定元素
		    max:0,//设置最大范围内的日期时间值
		    showBottom: false
		});
		laydate.render({
		    elem: '#endsearchUpdateTime',//指定元素
		    max:0,
		    showBottom: false
		});
		//给input赋值---开始时间默认当前时间，结束时间默认当前时间
	   // $('#searchUpdateTime').val(GetDateStr(-1));
	    //$('#searchEndDate').val(GetDateStr(-1));
	    
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
            	var errcode = r.code;//在此做了错误代码的判断
                if(errcode = 200){
                	reLoad();
                }
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
	//模糊查询
	var addressIdMagicSuggest = $('#searchCustomerName').magicSuggest({
        width:100,
        allowFreeEntries: false,
        maxSelection:1,
        autoSelect:false,
        valueField:"customerName",
        displayField:"customerName",
        placeholder:"",
        resultAsString:true,
        selectionStacked: true,
        queryParam:'queryString',
        //下拉框数据的获得：
        data: prefix+'/fuzzySearchCustomerName'
	});
	var addressIdMagicSuggest = $('#searchShopName').magicSuggest({
        width:100,
        allowFreeEntries: false,
        maxSelection:1,
        autoSelect:false,
        valueField:"shopName",
        displayField:"shopName",
        placeholder:"",
        resultAsString:true,
        selectionStacked: true,
        queryParam:'queryString',
        //下拉框数据的获得：
        data: prefix+'/fuzzySearchCustomerName'
	});
	var addressIdMagicSuggest = $('#searchProvinceName').magicSuggest({
        width:100,
        allowFreeEntries: false,
        maxSelection:1,
        autoSelect:false,
        valueField:"provinceName",
        displayField:"provinceName",
        placeholder:"",
        resultAsString:true,
        selectionStacked: true,
        queryParam:'queryString',
        //下拉框数据的获得：
        data: prefix+'/fuzzySearchCustomerName'
	});
	var addressIdMagicSuggest = $('#searchBigarea').magicSuggest({
        width:100,
        allowFreeEntries: false,
        maxSelection:1,
        autoSelect:false,
        valueField:"bigarea",
        displayField:"bigarea",
        placeholder:"",
        resultAsString:true,
        selectionStacked: true,
        queryParam:'queryString',
        //下拉框数据的获得：
        data: prefix+'/fuzzySearchCustomerName'
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
						dataField: "data",//这是返回的json数组的key.默认好像是"rows".这里只有前后端约定好就行
						responseHandler:responseHandler,//请求数据成功后，渲染表格前的方法
						
						//grimm 双击触发的事件
	            		onDblClickRow: function (row) {
	            			edit(row.id);
	            		},
	            		
						queryParams : function(params) {
							return {
								//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
								limit: params.limit,
								offset:params.offset,
								customerName:$('#searchCustomerName').magicSuggest().getValue()[0],
								shopName:$('#searchShopName').magicSuggest().getValue()[0],
								provinceName:$('#searchProvinceName').magicSuggest().getValue()[0],
								bigarea:$('#searchBigarea').magicSuggest().getValue()[0],
					           //customerName:$('#searchCustomerName').val(),
					           //provinceid:$('#searchBigarea').magicSuggest().getValue()[0],
					           //shopName:$('#searchShopName').val(),
					           startDailyOrderAvg:$('#searchStartDailyOrderAvg').val(),
					           endDailyOrderAvg:$('#searchEndDailyOrderAvg').val(),
					           //provinceName:$('#searchProvinceName').val(),
					           //bigarea:$('#searchBigarea').val(),
					           //handlerId:$('#searchHandlerId').val(),
					           handlerName:$('#searchHandlerName').val(),
					           startupdateTime:$('#startsearchUpdateTime').val(),
					           endupdateTime:$('#endsearchUpdateTime').val(),
					           
					           
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
									field : 'recordId', 
									title : '序号' 
								},
																{
									field : 'customerName', 
									title : '客户名称' 
								},
																{
									field : 'sendNumber', 
									title : '联系方式' 
								},
																{
									field : 'shopName', 
									title : '店铺名称' 
								},
																{
									field : 'product', 
									title : '产品结构' 
								},
																{
									field : 'bulkyCargo', 
									title : '是否泡货' 
								},
																{
									field : 'weight', 
									title : '三公斤内均重（kg）' 
								},
																{
									field : 'dailyOrderAvg', 
									title : '日均件量(票)' 
								},
																{
									field : 'expressCompany', 
									title : '合作快递公司' 
								},
																{
									field : 'unitPrice', 
									title : '价格' 
								},
																{
									field : 'sendAddress', 
									title : '客户具体发货地址' 
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
									field : 'cityName', 
									title : '城市' 
								},
								/*								{
									field : 'provinceId', 
									title : '省份id' 
								},*/
																{
									field : 'provinceName', 
									title : '省份' 
								},
																{
									field : 'bigarea', 
									title : '大区' 
								},
								/*								{
									field : 'handlerId', 
									title : '上传人员id' 
								},*/
																{
									field : 'handlerName', 
									title : '上传人员姓名' 
								},
																{
									field : 'updateTime', 
									title : '维护时间' 
								},
																{
									title : '操作',
									field : 'id',
									align : 'center',
									formatter : function(value, row, index) {
										var e = '<a class="btn btn-primary btn-sm '+s_edit_h+'" href="#" mce_href="#" title="编辑" onclick="edit(\''
												+ row.recordId
												+ '\')"><i class="fa fa-edit"></i></a> ';
										var d = '<a class="btn btn-warning btn-sm '+s_remove_h+'" href="#" title="删除"  mce_href="#" onclick="remove(\''
												+ row.recordId
												+ '\')"><i class="fa fa-remove"></i></a> ';
										var f = '<a class="btn btn-success btn-sm" href="#" title="备用"  mce_href="#" onclick="resetPwd(\''
												+ row.recordId
												+ '\')"><i class="fa fa-key"></i></a> ';
										return e + d ;
									}
								} ]
					});
}

//请求成功方法
function responseHandler(result){
    var errcode = result.code;//在此做了错误代码的判断
    var data = result.data;
    if(errcode == 490){
        popSafepage(data);
        return { total : 0  };
    }else if(errcode != 200){
        layer.alert(data);
        return { total : 0  };
    }
    //如果没有错误则返回数据，渲染表格
    return {
    	total : result.data.total, //总页数,前面的key必须为"total"
        data :  result.data.rows //行数据，前面的key要与之前设置的dataField的值一致.
    };
}
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}
/**
 * 查询必须满足以下两种情况：
 * 1.开始时间和结束时间必须是同一年同一个月，不能跨域查询
 * 2.开始时间<结束时间
 */
function query(){
	var customerName=$('#searchCustomerName').magicSuggest().getValue()[0],
	shopName=$('#searchShopName').magicSuggest().getValue()[0],
	provinceName=$('#searchProvinceName').magicSuggest().getValue()[0],
	bigarea=$('#searchBigarea').magicSuggest().getValue()[0],
	//customerName =$('searchCustomerName').val(),
    //shopName=$('#searchShopName').val(),
    startDailyOrderAvg=$('#searchStartDailyOrderAvg').val(),
    endDailyOrderAvg=$('#searchEndDailyOrderAvg').val(),
    //provinceId=$('#searchProvinceId').val(),
    //provinceName=$('#searchProvinceName').val(),
    //bigarea=$('#searchBigarea').val(),
    handlerName=$('#searchHandlerName').val(),
    startupdateTime=$('#startsearchUpdateTime').val(),
    endupdateTime=$('#endsearchUpdateTime').val();//,
    //customerName=$('#searchCustomerName').val();
	if(!startDailyOrderAvg ){
		if(endDailyOrderAvg){
			layer.msg('日均票件量全输入或者全不输入！'); 
			return;
		}
	}
	if(!endDailyOrderAvg){
		if(startDailyOrderAvg){
			layer.msg('日均票件量全输入或者全不输入！'); 
			return;
		}
	}
	if(startDailyOrderAvg>endDailyOrderAvg){
		layer.msg('左边日均票件量不能大于右边日均票件量！'); 
		return;
	}
	var startTime = new Date(startupdateTime.replace(/-/g,"/")).getTime(),
		endTime = new Date(endupdateTime.replace(/-/g,"/")).getTime();
	if(startTime>endTime){
		layer.msg('开始时间不能大于结束时间！'); 
		return;
	}
	/*var startDate = $('#searchStartDate').val(),
		endDate = $('#searchEndDate').val(),
		startYear = startDate.substring(0,5),
		endYear = endDate.substring(0,5),
		startMonth = startDate.substring(5,7),
		endMonth = endDate.substring(5,7);
	var startTime = new Date(startDate.replace(/-/g,"/")).getTime(),
		endTime = new Date(endDate.replace(/-/g,"/")).getTime();
	if(startTime>endTime){
		layer.msg('开始时间不能大于结束时间！'); 
		return;
	}
	if( (startYear == endYear && startMonth !== endMonth) || startYear !== endYear){
		layer.msg('不能跨月查询！'); 
		return;
	}*/
	$("#exampleTable").bootstrapTable('destroy');
	load();
}


//导出excel
function exportExcel(action) {
	layer.load(1);
	$('#exportExcelbtn').attr("disabled",true);
	$('#exportExcelbtn').attr("readonly","readonly");//将input元素设置为readonly
	//$('#searchCustomerName').magicSuggest().getValue()[0]是模糊搜索的value		           
	  //var url = prefix + action;  //action后面拼接参数   根据情况自行修改
	
	var customerName="",
	shopName="",
	provinceName="",
	bigarea="";
		if($('#searchCustomerName').magicSuggest().getValue()[0] !=null){// && !"".equals($('#searchCustomerName').magicSuggest().getValue()[0])){
			customerName = $('#searchCustomerName').magicSuggest().getValue()[0];
		}
		if($('#searchShopName').magicSuggest().getValue()[0]!=null){// && !"".equals($('#searchShopName').magicSuggest().getValue()[0])){
			shopName = $('#searchShopName').magicSuggest().getValue()[0];
		}
		if($('#searchProvinceName').magicSuggest().getValue()[0]!=null){// && !"".equals($('#searchProvinceName').magicSuggest().getValue()[0])){
			provinceName = $('#searchProvinceName').magicSuggest().getValue()[0];
		}
		if($('#searchBigarea').magicSuggest().getValue()[0]!=null){// && !"".equals($('#searchBigarea').magicSuggest().getValue()[0])){
			bigarea = $('#searchBigarea').magicSuggest().getValue()[0];
		}

	  var url = prefix + action+"?customerName="+customerName+"&shopName="+shopName
	  			+"&startDailyOrderAvg="+$('#searchStartDailyOrderAvg').val()+"&endDailyOrderAvg="+$('#searchEndDailyOrderAvg').val()
	  			+"&provinceName="+provinceName+"&bigarea="+bigarea
	  			+"&handlerName="+$('#searchHandlerName').val()
	  			+"&startupdateTime="+$('#startsearchUpdateTime').val()+"&endupdateTime="+$('#endsearchUpdateTime').val();
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
		    a.download = '潜在客户新表-'+nowTime+'.xlsx';//导出表的表名   自行修改    	  
	        
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
