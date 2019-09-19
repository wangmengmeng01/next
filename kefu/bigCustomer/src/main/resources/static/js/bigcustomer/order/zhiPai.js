var prefix = "/vipkf/bigcustomer/order"
$().ready(function() {
    //初始所有指派人员:姓名(账号)
   /* $.ajax({
        type: 'GET',
        url: prefix + '/getAllUserInfo',
        success: function (r) {
            //成功后把数据添加到下拉框中
            for(var i=0;i<r.length;i++){
                $(".es-list").append("<li class value="+r[i].username+">"+r[i].name+"</li>")
            }
        }
    });*/

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
        url : prefix +"/zhiPaiSave",
        data : $('#signupForm').serialize(),// 你的formid
        async : false,
        error : function(request) {
            parent.layer.alert(request.get());
        },
        success : function(data) {
            if (data.code == 0) {
                parent.layer.msg("操作成功");
                parent.reLoad();
                //改变父页面的数据
                parent.$('#detail_state').val("待处理");
                parent.$('#detail_state').text("待处理");
                parent.$('#btnDiv').show();
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

}

function quitAudit(){
    // parent.reLoad(); 不需要重新加载
    var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
    parent.layer.close(index);
}

//    filter：过滤，即当输入内容时下拉选项会匹配输入的字符，支持中文，true/false，默认true。
//
//    duration：下拉选项框展示的过渡动画速度，有fast，slow，以及数字（毫秒），默认是fast。
//
//    onCreate：当输入时触发。
//
//    onShow：当下拉时触发。
//
//    onHide：当下拉框隐藏时触发。
$('#dealMan').editableSelect({
//        effects: 'slide',
//     bg_iframe: true, //是否加iframe
    onSelect:function (element) {
        $('.dealMan').val(element.attr('value'));
        console.log(element)
    },
    onHide:function(){
        console.log($('.dealMan').val())
    }
  /*  case_sensitive: false, // 是否匹配
    items_then_scroll: 10 ,// 设置下拉选项的数目
    isFilter:true //是否根据条件过滤下拉选项*/
}).prop('placeholder','请选择');
function searchItem(that) {
//        console.log($('.main').val())
}
//    function test() {
//        console.log($('.main').val())
//    }