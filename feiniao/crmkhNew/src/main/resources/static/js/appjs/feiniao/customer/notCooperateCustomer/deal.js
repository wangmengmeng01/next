var prefix = "/crmkh/customer/notCooperateCustomer"
$(function() {
	layui.use(['upload','laydate'], function () {
	    var upload = layui.upload;
	    var laydate = layui.laydate;
	    //执行一个laydate实例
	    laydate.render({
	        elem: '#provinceVisitTime', //指定元素
	        max:0,//设置最大范围内的日期时间值
	        showBottom: false
	    });
	    
	});	 
});

$().ready(function() {
    validateRule();
});

function provinceVisitChange(){
    if($("#provinceVisit").val()=="是"){
    	$("#provinceVisitTime").removeAttr("disabled");
    }else {
    	$("#provinceVisitTime").attr("disabled",true);
        $("#provinceVisitTime").val("");
	}
}

function feedBackCheckChange(){
    //当网点反馈审核选中“不属实”时，处理状态为“省公司待处理”,前端去掉按钮  后端控制
    if($("#feedBackCheck").val()=="属实"){
    	$("#state").removeAttr("disabled");   
        $("#cooperateBranch").removeAttr("disabled");
        $("#boundVipAccount").removeAttr("disabled");
    }else {
    	$("#state").attr("disabled",true);   
        $("#cooperateBranch").attr("disabled",true);
        $("#boundVipAccount").attr("disabled",true);
        $("#cooperateBranch").val("");
        $("#boundVipAccount").val("");
        $("#state").val("省公司待处理");
	}
}

function stateChange(){
    //如果处理状态不为已达成合作,那么合作网点编码和绑定vip账号不作为必填项
    if($("#state").val()!="已达成合作"){
        $("#cooperateBranch").removeAttr("required");
        $("#boundVipAccount").removeAttr("required");
    }else {
        $("#cooperateBranch").attr("required",true);
        $("#boundVipAccount").attr("required",true);
	}
}

$.validator.setDefaults({
    submitHandler : function() {
        save();
    }
});
function save() {
	//var visitTime =  $('#provinceVisitTime').val();
	var params=$('#signupForm').serialize()//+"&provinceVisitTime="+visitTime;
    $.ajax({
		cache : true,
		type : "POST",
		url : prefix +"/dealSave",
		data : params,//$('#signupForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code == 0) {
				parent.layer.msg("操作成功");
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

			} else {
				parent.layer.alert(data.msg)
			}

		}
	});

}
function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
			name : {
				required : true
			}
		},
		messages : {
			name : {
				required : icon + "请输入姓名"
			}
		}
	});

	//为了把后台所属网点和网点编码数据展示到页面山
   /* $.ajax({
        cache : true,
        type : "GET",
        url : prefix + "/getBranchInfo",
        async : false,
        error : function(request) {
            $('#branchName_span').text("获取失败");
            $('#zongBuDealName_span').text("获取失败");
            $('#zongBuDealTime_span').text("获取失败");
            $('#provinceDealTime_span').text("获取失败");
            $('#provinceDealName_span').text("获取失败");
        },
        success : function(data) {
            $('#branchName_span').text(data.branchName+"("+data.branchCode+")");
            $('#branchName').val(data.branchName+"("+data.branchCode+")");
            $('#zongBuDealName_span').text(data.zongBuDealName);
            $('#zongBuDealName').text(data.zongBuDealName);
            $('#zongBuDealTime_span').text(data.zongBuDealTime);
            $('#zongBuDealTime').text(data.zongBuDealTime);
            $('#provinceDealName_span').text(data.provinceDealName);
            $('#provinceDealName').text(data.provinceDealName);
            $('#provinceDealTime_span').text(data.provinceDealTime);
            $('#provinceDealTime').text(data.provinceDealTime);
        }
    });*/

}


function quitAudit(){
   // parent.reLoad(); 不需要重新加载
    var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
    parent.layer.close(index);
}

