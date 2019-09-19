var prefix = "/vipkf/bigcustomer/customerManage"
$().ready(function() {
   /* $.ajax({
        type: 'GET',
        url: prefix + '/getOrganizationInfo',
        success: function (r) {
            //成功后把数据添加到下拉框中
            for(var i=0;i<r.length;i++){
                //获取第1个class对象
                $(".es-list").eq(0).append("<li class value="+r[i]+">"+r[i]+"</li>")
            }
        }
    });*/
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		update();
	}
});
function update() {
	$.ajax({
		cache : true,
		type : "POST",
		url : prefix + "/update",
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
				required : icon + "请输入名字"
			}
		}
	})
}

function searchItem(that) {
//        console.log($('.main').val())
}
//    function test() {
//        console.log($('.main').val())
//    }

$('#organization').editableSelect({
//        effects: 'slide',
    onSelect:function (element) {
        $('.organization').val(element.attr('value'));
        console.log(element)
    },
    onHide:function(){
        console.log($('.organization').val())
    }
});

function getVipNameByVipNum(vipNum) {
    if(vipNum==""){
        layer.msg("vip账号不能为空!")
        return;
    }
    //发起ajax请求获取vipName
    $.ajax({
        type: 'GET',
        url: prefix + '/getVipNameByVipNum/'+vipNum,
        success: function (r) {
            //成功后把数据回显到vip名称中
            if(r.code==500){
                layer.msg(r.msg)
                return;
            }
            $("#vipName").val(r.msg)
        }
    });
}


function quitAudit(){
    // parent.reLoad(); 不需要重新加载
    var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
    parent.layer.close(index);
}