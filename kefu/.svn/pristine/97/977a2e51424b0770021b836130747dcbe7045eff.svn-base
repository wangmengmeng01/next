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
    obj = document.getElementsByName("customerPlatform");
    check_val = [];
    for(k in obj){
        if(obj[k].checked)
            check_val.push(obj[k].value);
    }
    if(check_val.length < 1){
        alert("客户所属平台最少选择一个复选框!")
        return false;
    }
    var arr  = [];
    for (var i = 0; i < 6; i++) {
       if ($('.cooperateRatio')[i].value !== '' || $('.cooperatePrice')[i].value !== '' || $('.remark')[i].value !== '') {
           arr.push({
               branchCode:$('#branchCode').val(),
               productType:$('#productType').val(),
               cooperatePeerName:$('.cooperateRatio')[i].parentNode.getAttribute('data-name'),
               cooperateRatio:$('.cooperateRatio')[i].value,
               cooperatePrice:$('.cooperatePrice')[i].value,
               remark:$('.remark')[i].value
           })
       }

    }
    var result = JSON.stringify(arr);
    $('#cooperatePeerCase').val(result);
    $.ajax({
		cache : true,
		type : "POST",
		url : prefix +"/save",
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
    $.ajax({
        cache : true,
        type : "GET",
        url : prefix + "/getBranchInfo",
        async : false,
        error : function(request) {
            $('#branchName').text("获取失败");
            $('#branchCode').text("获取失败");
        },
        success : function(data) {
            $('#branchName_span').text(data.branchName);
            $('#branchName').val(data.branchName);
            $('#branchCode_span').text(data.branchCode);
            $('#branchCode').val(data.branchCode);
        }
    });

}


function quitAudit(){
    parent.reLoad();
    var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
    parent.layer.close(index);
}

function checkboxOnclick(checkbox){
    if (checkbox.checked == true){
        $('#otherPlatForm').attr("required","true")
    }else {
        $('#otherPlatForm').removeAttr("required")
    }
}
