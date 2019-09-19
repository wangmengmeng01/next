var prefix = "/crmkh/report/reportTotaldata";

$(function() {
	load();
	//上传excel按钮
    layui.use(['laydate','laypage','layer','form','upload'], function () {
    	$=layui.jquery;
    	var laydate = layui.laydate;
    	layer = layui.layer;
    	form = layui.form;
    	
    	laydate.render({
             	elem: '#qu_date' //指定元素
               ,showBottom: false

             });
    	laydate.render({
	         	elem: '#start_date' //指定元素
	           ,showBottom: false

	         });
    	laydate.render({
	         	elem: '#end_date' //指定元素
	           ,showBottom: false

	         });
    	laydate.render({
            	elem: '#month_year' //指定元素
            	,type: 'month'
         	    ,showBottom: false

            });
    	laydate.render({
                elem: '#jidu_year' //指定元素
                ,type: 'year'
         	    ,showBottom: false

            });
    	laydate.render({
            elem: '#year' //指定元素
            ,type: 'year'
     	    ,showBottom: false

        });
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


//function child(cBigArea,cProvinceId,cCityId){
//	bigArea=cBigArea;
//	provinceId=cProvinceId;
//	cityId=cCityId;
//alert(bigArea+provinceId+cityId);
//}
//function child(cBigArea,cProvinceId,cCityId){
//bigArea=cBigArea;
//provinceId=cProvinceId;
//cityId=cCityId;
//alert(cProvinceId);
//}

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
//						pagination : true, // 设置为true会在底部显示分页条
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
								start_date:($('#startDate').val()==null)?'':$('#startDate').val(),
								end_date:($('#endDate').val()==null)?'':$('#endDate').val(),
								province_id :($('#provinceid').val()==null)?'':$('#provinceid').val(),
							    region_id : ($('#bigarea').val()==null)?'':$('#bigarea').val(),
							    showType : "city"
							};
						},
						// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
						// queryParamsType = 'limit' ,返回参数必须包含
						// limit, offset, search, sort, order 否则, 需要包含:
						// pageSize, pageNumber, searchText, sortName,
						// sortOrder.
						// 返回false将会终止请求
					      frozenColumns : [ [ 
               				               //    {field : 'ck', checkbox : true, align : 'center'},
               				                   //{field : 'id', title : 'id', hidden : true},
                                                     {field : 'bigarea', width :100, title : '大区', hidden : true},
                                                  {field : 'provinceID', width :100, title : '省', hidden : true},
                                                  {field : 'cityID', width :100, title : '市', hidden : true}

               				        		   ]
               				                ] ,
               				        		columns : [ [
               				        		   		  {field : 'customerName', title :  '按地级市', align : 'center', rowspan : 2, width : 180,
              				        		  			formatter:function(value,row, index){
              				        		  				if(row.customerName!=undefined){
              												if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
              							                	  return "--";
              							                	  else if(row.customerName.indexOf("合计")!=-1)
              							                	  return value;
             							                		  else
             							                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openCityTable(\""+row.bigarea+","+row.provinceid+","+row.cityid+","+row.startDate+","+row.endDate+"\");'>"+value+'</a>';  
              				        		  			
              							                }else{
              							                	return value;
              							                }
              				        		  			}
              				        				  },
              				        				  
              				        		   		  {field : 'orderSum', title :  '总单量', align : 'center', rowspan : 2, width : 180,
            					        		  			formatter:function(value,row, index){
            													if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
            								                	  return "--";
            								                	  else
            								                      return value;  
            												}
            					        				  },
            					        		   		  {field : 'orderAvg', title :  '日均单量', align : 'center', rowspan : 2, width : 180,
            						        		  			formatter:function(value,row, index){
            														if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
            									                	  return "--";
            									                	  else
            									                      return value;  
            													}
            						        				  },
            						        		  {field : 'yewuliang',title : '业务量类型', align : 'center', colspan : 3},
            				        		   	      {field : 'dianzimiandan',title : '电子面单总量', align : 'center', colspan : 4},

               				        		   	      {field : '',title : 'A类', align : 'center', colspan : 6},
               				        		   	      {field : '',title : 'B类', align : 'center', colspan : 6},

               				        		   	      {field : '',title : 'C类', align : 'center', colspan : 6},
               				        		   	      {field : '',title : 'D类', align : 'center', colspan : 6},

               				        		   	      {field : '',title : 'E类', align : 'center', colspan : 6},
               				        			      {field : '',title : 'F类', align : 'center', colspan : 6},
               				        			      {field : '',title : 'G类', align : 'center', colspan : 6}				        		             
               				        		             
               				        		             ],[
           								                   {field : 'dianziOrderSum', width : 100, title : '电子面单',align : 'center',
       						     		                	  formatter:function(value,row, index){
       																if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
       											                	  return "--";
       											                	  else
       											                      return value;  
       															}
       						     		                   },
       									                   {field : 'ordinaryOrderSum', width : 100, title : '普通面单',align : 'center',
       							     		                	  formatter:function(value,row, index){
       																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
       												                	  return "--";
       												                	  else
       												                      return value;  
       																}
       							     		                   },
       										                   {field : 'dianziPercent', width : 100, title : '电子面单占比',align : 'center',
       								     		                	  formatter:function(value,row, index){
       																		if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
       													                	  return "--";
       													                	  else
       													                      return value;  
       																	}
       								     		                   },
       											                   {field : 'customerSum', width : 100, title : '客户数',align : 'center',
       									     		                	  formatter:function(value,row, index){
       																			if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
       														                	  return "--";
       														                	  else
       														                      return value;  
       																		}
       									     		                   },
       									     		                   {field : 'dianziOrderSumAvg', width : 100, title : '日均单量',align : 'center',
       										     		                	  formatter:function(value,row, index){
       																				if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
       															                	  return "--";
       															                	  else
       															                      return value;  
       																			}
       										     		                   },
       										     		                   {field : 'dianziPriceSumAvg', width : 100, title : '日均奖励金额(/元)',align : 'center',
       											     		                	  formatter:function(value,row, index){
       																					if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
       																                	  return "--";
       																                	  else
       																                      return value;  
       																				}
       											     		                   },
       											     		                   {field : 'dianziPriceSum', width : 100, title : '总奖励金额(/元)',align : 'center',
       												     		                	  formatter:function(value,row, index){
       																						if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
       																	                	  return "--";
       																	                	  else
       																	                      return value;  
       																					}
       												     		                   },
               							                   {field : 'acustomerSum', width : 100, title : '客户数',align : 'center'},
               							                   {field : 'aorderAvg', width : 100, title : '日均单量', align : 'center'},
               							                   {field : 'aorderSum', width : 100, title : '总单量', align : 'center'},
               							                   {field : 'apricePercent', width : 100, title : '单量占比', align : 'center'},
               							                   {field : 'apriceSum', width : 100, title : '日均奖励金额(/元)',align : 'center'},
               							                   {field : 'aallPriceSum', width : 100, title : '总奖励金额(/元)',align : 'center'},

               							                   {field : 'bcustomerSum', width : 100, title : '客户数', align : 'center'},
               							                   {field : 'borderAvg', width : 100, title : '日均单量',align : 'center'},
               							                   {field : 'borderSum', width : 100, title : '总单量',align : 'center'},
               							                   {field : 'bpricePercent', width : 100, title : '单量占比', align : 'center'},
               							                   {field : 'bpriceSum', width : 100, title : '日均奖励金额(/元)', align : 'center'},
               							                   {field : 'ballPriceSum', width : 100, title : '总奖励金额(/元)',align : 'center'},								        		  
               							        		
               							                   {field : 'ccustomerSum', width : 100, title : '客户数',align : 'center'},
               							                   {field : 'corderAvg', width : 100, title : '日均单量', align : 'center'},
               							                   {field : 'corderSum', width : 100, title : '总单量', align : 'center'},
               							                   {field : 'cpricePercent', width : 100, title : '单量占比',align : 'center'},
               							                   {field : 'cpriceSum', width : 100, title : '日均奖励金额(/元)', align : 'center'},
               							                   {field : 'callPriceSum', width : 100, title : '总奖励金额(/元)', align : 'center'},
               								   
               							                   {field : 'dcustomerSum', width : 100, title : '客户数', align : 'center'},
               							                   {field : 'dorderAvg', width : 100, title : '日均单量', align : 'center'},
               							                   {field : 'dorderSum', width : 100, title : '总单量', align : 'center'},
               							                   {field : 'dpricePercent', width : 100, title : '单量占比',align : 'center'},
               							                   {field : 'dpriceSum', width : 100, title : '日均奖励金额(/元)', align : 'center'},
               							                   {field : 'dallPriceSum', width : 100, title : '总奖励金额(/元)', align : 'center'},

               							                   {field : 'ecustomerSum', width : 100, title : '客户数', align : 'center'},
               							                   {field : 'eorderAvg', width : 100, title : '日均单量',align : 'center'},
               							                   {field : 'eorderSum', width : 100, title : '总单量', align : 'center'},
               							                   {field : 'epricePercent', width : 100, title : '单量占比', align : 'center'},
               							                   {field : 'epriceSum', width : 100, title : '日均奖励金额(/元)', align : 'center'},
               							                   {field : 'eallPriceSum', width : 100, title : '总奖励金额(/元)',align : 'center'},

               							                   {field : 'fcustomerSum', width : 100, title : '客户数', align : 'center'},
               							                   {field : 'forderAvg', width : 100, title : '日均单量',align : 'center'},
               							                   {field : 'forderSum', width : 100, title : '总单量',align : 'center'},
               							                   {field : 'fpricePercent', width : 100, title : '单量占比',align : 'center'},
               							                   {field : 'fpriceSum', width : 100, title : '日均奖励金额(/元)',align : 'center'},
               							                   {field : 'fallPriceSum', width : 100, title : '总奖励金额(/元)',align : 'center'},

               							                   {field : 'gcustomerSum', width : 100, title : '客户数', align : 'center'},
               							                   {field : 'gorderAvg', width : 100, title : '日均单量', align : 'center'},
               							                   {field : 'gorderSum', width : 100, title : '总单量', align : 'center'},
               							                   {field : 'gpricePercent', width : 100, title : '单量占比', align : 'center'},
               							                   {field : 'gpriceSum', width : 100, title : '日均奖励金额(/元)', align : 'center'},
               							                   {field : 'gallPriceSum', width : 100, title : '总奖励金额(/元)', align : 'center'}

               								                   ]]
					});
}

function openCityTable(param){
		/*if(s_cityData_h=='hidden'){
			return;
		};*/	
//		var date_style  = $("#dateStyle").val();
//		var qu_date  = $('#quDate').val();
//		var month_year  = $('#monthYear').val();
//		var quarter_year  = $('#quarterYear').val();
//		var quarter_date  = $('#quarterDate').val();
//		var year  = $('#year').val();
		var start_date  = param.split(',')[3];
		var end_date  = param.split(',')[4];
		var bigarea  = param.split(',')[0];
		var provinceid  = param.split(',')[1];
		var cityid  = param.split(',')[2];
		var index = layer.open({
			type : 2,
			title : '公司表',
			maxmin : true,
			shadeClose : false, // 点击遮罩关闭层
			area : [ '800px', '520px' ],
			content : prefix  + '/reportTotalBranch/'+start_date+"/"+end_date+"/"+bigarea+"/"+provinceid+"/"+cityid// iframe的url
		});
		layer.full(index);

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
}
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
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
				'bigarea' : id
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
			ids[i] = row['bigarea'];
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
