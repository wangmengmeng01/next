var prefix = "/vipkf/bigcustomer/consultConfig"
$().ready(function() {
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		save();
	}
});
function save() {
	//进行判断勾选的和提交的是否一致
	var type = $('input:radio:checked').val();
	if(type==undefined || type==""){
		layer.msg("请选择处理时效!");
		return;
	}
	if(type==1){
		if($('#orderAfterHoursT1').val()==""){
            layer.msg("您勾选的输入框内未填写数据!");
            return;
		}
	}
	if(type==2){
		if($('#orderAfterDayT2').val()=="" || $('#orderAfterTimeT2').val()==""){
            layer.msg("您勾选的输入框内未填写数据!");
            return;
		}
	}
    if(type==3){
        if($('#todayOrderBeforeTimeT31').val()=="" || $('#orderAfterDayT31').val()=="" || $('#orderAfterTimeT31').val()=="" || $('#todayOrderAfterTime32').val()=="" || $('#orderBeforeDayT32').val()=="" || $('#orderBeforeTimeT32').val()==""){
            layer.msg("您勾选的输入框内未填写数据!");
            return;
        }
    }
    if($("#yujingTime").val()==""){
        layer.msg("请填写预警时效	!");
        return;
    }
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
	})
}

function quitAudit(){
    // parent.reLoad(); 不需要重新加载
    var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
    parent.layer.close(index);
}