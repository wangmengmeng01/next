var prefix = "/vipkf/costreport/costreportOrderCostFinish";
var month; 
$(function() {
	if(Chrome()){
		alert("各位领导,请使用谷歌浏览器进行操作!");
		return;
	}
	// 发送心跳检测,检测页面开关
	heartCheck();
/* $('#customerCode').magicSuggest({
        width:100,
        allowFreeEntries: false,
        maxSelection:1,
        autoSelect:false,
        valueField:"id",
        displayField:"name",
        placeholder:"",
        resultAsString:true,
        selectionStacked: true,
        queryParam:'queryString',
        //下拉框数据的获得：
        data: prefix+'/searchCustomerData'
});*/
	
	
/* $('#branchCode').magicSuggest({
        width:100,
        allowFreeEntries: false,
        maxSelection:1,
        autoSelect:false,
        valueField:"bm",
        displayField:"mc",
        placeholder:"",
        resultAsString:true,
        selectionStacked: true,
        queryParam:'queryString',
        //下拉框数据的获得：
        data: prefix+'/searchCustBraData'
});*/

	//上传excel按钮
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
    
	$('#month_year').val(GetDateStr(0));
	
	load();

	//上传excel按钮
 /*   layui.use('upload', function () {
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
    */
    //刷新按钮
     $("#refresh").click(function(){ $('#exampleTable').bootstrapTable('refresh'); }); 
});

//js获取当前日期前后N天的方法:
function GetDateStr(AddDayCount) { 
    var date = new Date(); 
    date.setDate(date.getDate()+AddDayCount);//获取AddDayCount天后的日期
    var y = date.getFullYear(); 
    var m = changeTime(date.getMonth());//获取当前月份的日期
//    var d = changeTime(date.getDate()); 
    return y+"-"+m; 
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
							$('#limitvalue').val(params.limit);
							$('#offsetvalue').val(params.offset);
							   month = $('#month_year').val();
							   if(month=="" || month==null){
								    layer.msg("请输入查询日期");
									return false;
								}else{
							    	month = month.replace("-","");
								}
							return {
								//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
								limit: params.limit,
								offset:params.offset,
								//accountDt:$('#month_year').val(),
								accountDt:  month,
								//customerId:$('#customerCode').magicSuggest().getValue()[0],
								customerId:$('#customerCode').val(),
								branchCode:$('#branchCode').val()
								//branchCode:$('#branchCode').magicSuggest().getValue()[0]
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
									checkbox : false
								},*/
								//{field:'recordId',title:'序号',align:'center'},
								{field:'customerId',title:'客户编码',align:'center'},
								{field:'accountDt',title:'日期',align:'center',visible:false},
								{field:'customerName',title:'客户名称',align:'center'},
								{field:'orderSum',title:'票件量(件)',align:'center'},
								{field:'weightAll',title:'重量(kg)',align:'center'},
								{field:'income',title:'收入(元)',align:'center',
									formatter:function(value,row, index){
										if(row.customerName!='首行合计')
										return "<a href='javascript:void(0);' style='color:blue;' onclick='openOrderIncome(\""+row.customerId+","+row.accountDt+"\");'>"+value+'</a>';    
					                	  else
					                      return value;
								}},
								{field:'expenditure',title:'支出(元)',align:'center',
									formatter:function(value,row, index){	
										if(row.customerName!='首行合计')
							                 return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceExpenditureWindow(\""+row.customerId+","+row.accountDt+"\");'>"+value+'</a>';    
					                	  else
						                  return value;
									}},
								{field:'profit',title:'利润',align:'center'},
								{field:'incomeEach',title:'单票收入',align:'center'},
								{field:'kilogramEach',title:'单公斤收入',align:'center'},
								{field:'profitEach',title:'单票收益',align:'center'},
								{field:'kilogramProfitEach',title:'单公斤收益',align:'center'}
							]
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
function getMouldList() {
    $.ajax({
        type: 'GET',
        url: prefix + "/searchCustomerData"+"?q="+$('#customerCode').getValue()==null?'':$('#customerCode').getValue(),
		dataType:"json",
        success: function(data) {
            var code3;
            var mouldCode=$('#customerCode').val();
            for (var a=0;a<data.length;a++) {

                var dataContent = data[a];
                if (dataContent == mouldCode) {

                    code3 = code3 + '<option selected="selected" value='+data[a].code+'>' + data[a].name + '</option>'
                }
                if (dataContent != mouldCode) {

                    code3 = code3 + '<option  value='+data[a].code+'>' + data[a].name + '</option>'
                }
            }
            $('#customerCode').empty().append(code3);
        }

    });
}


function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}

function query() {
	   month = $('#month_year').val();
	   if(month=="" || month==null){
		    layer.msg("请输入查询日期");
			return false;
		}else{
	    	month = month.replace("-","");
		}
	   
	//$('#exampleTable').bootstrapTable('refresh');
	$("#exampleTable").bootstrapTable('destroy');
	load();
}

//点击收入，获取收入报表
function openOrderIncome(income){
	var customerId = income.split(',')[0];
	var accountDt = income.split(',')[1];
	if(customerId !=''&& customerId!=null && accountDt !='' && accountDt!=null){
		var index = layer.open({
			type : 2,
			title : '收入报表',
			maxmin : true,
			shadeClose : false, // 点击遮罩关闭层
			area : [ '800px', '520px' ],
			content : '/vipkf/costreport/costreportCustRouteIncome/getIncomeHtml/'+accountDt+"/"+customerId // iframe的url
		});
		layer.full(index);
	}
}

function openProvinceExpenditureWindow(param){
	/*if(s_branchData_h=='hidden'){
		return;
	};*/	
//	var date_style  = $("#dateStyle").val();
//	var qu_date  = $('#quDate').val();
//	var month_year  = $('#monthYear').val();
//	var quarter_year  = $('#quarterYear').val();
//	var quarter_date  = $('#quarterDate').val();
//	var year  = $('#year').val();
	var customerId  =param.split(',')[0];
	var accountDt  = param.split(',')[1];
	if(customerId !=''&& customerId!=null && accountDt !='' && accountDt!=null){
	var index =layer.open({
		type : 2,
		title : '支出报表',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix  + '/reportProvinceExpenditure/'+customerId+"/"+accountDt // iframe的url
	});
	layer.full(index);
	}
}
/*$('#exportExcelbtn').click(function(){
	var accountDt = $('#month_year').val()==undefined?'': $('#month_year').val();
	var url = prefix + "/exportExcel"+"?accountDt="+accountDt;
	window.open(url);
}); */

//导出excel
function exportExcel(action) {
	var limit =  $('#limitvalue').val();
	var offset = $('#offsetvalue').val();
	var accountDt = $('#month_year').val()==undefined?'': $('#month_year').val();
	//var customerId1 = $('#customerCode').magicSuggest().getValue()[0];
	var customerId1 = $('#customerCode').val();
	//var branchCode1 = $('#branchCode').magicSuggest().getValue()[0];
	var branchCode1 = $('#branchCode').val();
	if(customerId1 == undefined){
		customerId1 = "";
	}
	if(branchCode1 == undefined){
		branchCode1 = "";
	}

	
	layer.load(1);
	$('#exportExcelbtn').attr("disabled",true);
	$('#exportExcelbtn').attr("readonly","readonly");//将input元素设置为readonly
	
	  var url = prefix + action+"?accountDt="+accountDt+"&customerId="+customerId1+"&branchCode="+branchCode1+"&limit="+limit+"&offset="+offset;
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

	        a.download = '量本利汇总报表-'+nowTime+'.xlsx';
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

function resetForm(){
	document.getElementById("signupForm").reset(); 
}