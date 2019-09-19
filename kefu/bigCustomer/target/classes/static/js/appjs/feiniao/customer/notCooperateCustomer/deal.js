var prefix = "/vipkf/customer/notCooperateCustomer"
$().ready(function() {
    validateRule();
});

$.validator.setDefaults({
    submitHandler : function() {
        save();
    }
});
function save() {
    $.ajax({
		cache : true,
		type : "POST",
		url : prefix +"/dealSave",
		data : $('#signupForm').serialize(),// 你的formid
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

