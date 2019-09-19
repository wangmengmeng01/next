var prefix = "/vipkf/bigcustomer/order"
$().ready(function() {
    validateRule();
    //上传附件
    layui.use("upload", function () {
        var upload = layui.upload;
        //执行实例
        var uploadInst = upload.render({
            elem: '#upload', //绑定元素
            url: prefix+'/upload', //上传接口
            size: 5000,
            accept: 'file',
            auto: true,    //关闭自动提交
            // bindAction: '#queRen',  //绑定表单按钮
            exts: 'xls|xlsx|zip|docx|pdf|rar|jpeg|jpg|png|bmp',    //传输完成的回调
            done: function (r) {
                layer.closeAll('loading');
                $('#upload').attr("disabled",false);
                $('#upload').removeAttr("readonly");//去除input元素的readonly属性
                //将文件名回显到页面以及将文件地址存放到input框中
                $('#uploadSpan').text(r.fileName);
                $('#fileName').val(r.fileName);
                $('#uploadPath').val(r.filePath);
                layer.msg("文件上传成功");
                //reLoad();
            },
            error: function (r) {           //传输失败的回调
                layer.msg(r.msg);
            },
            before: function(obj){
                layer.load(1);
                $('#upload').attr("disabled",true);
                $('#upload').attr("readonly","readonly");//将input元素设置为readonly
            }
        });
    });
});

$.validator.setDefaults({
    submitHandler : function() {
        save();
    }
});

function save() {
    if($("#organizationName").text()==""){
        parent.layer.alert("该机构编码不存在无法执行保存操作!")
        return
    }
    $.ajax({
        cache : true,
        type : "POST",
        url : prefix +"/zhuanFaSave",
        data : $('#signupForm').serialize(),// 你的formid
        async : false,
        error : function(request) {
            parent.layer.alert("操作失败");
        },
        success : function(data) {
            if (data.code == 0) {
                parent.layer.msg("操作成功");
                parent.reLoad();
                //改变父页面的数据
                parent.$('#detail_state').val("待申领");
                parent.$('#detail_state').text("待申领");
                parent.$('#btnDiv').hide();
                var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                parent.layer.close(index);
                window.parent.close();
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

/*参数校验*/
function checkLengthT(that,count) {
    if ($(that).val().length >= count) {
        $(that).parent().append("<span class='textarea-check'>限定输入"+count+"个字符</span>")
    } else {
        $(that).parent().find('.textarea-check').remove()
    }
}

function getOrganizationNameByCode() {
    var organizationNum =  $("#organizationNum").val();
    if(organizationNum==""){
        $("#organizationName").text("");
        return;
    }

    $.ajax({
        cache : true,
        type : "GET",
        url : prefix +"/getOrganizationNameByCode/"+organizationNum,
        async : false,
        error : function(request) {
            parent.layer.alert("操作失败");
        },
        success : function(r) {
            if(r.code!="200"){
                parent.layer.alert("该机构编码不存在");
                $("#organizationName").text("");
            }else {
                $("#organizationName").text(r.data)
            }

        }
    });

}


