var prefix = "/vipkf/bigcustomer/order"
$(function() {
    validateRule();

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
                layer.alert(r.msg);
                /*var msgString = r.msg;
                layer.open({
                    type: 2,
                    title: '请修改错误行的数据,再重新导入所有数据!',
                    area : [ '100%', '100%' ],
                    content: msgString
                });*/
                //reLoad();
            },
            error: function (r) {
                layer.msg(r.msg);
                //var msgString = r.msg;
                //parent.layer.alert(msgString);

/*
                layer.open({
                    type: 1
                    ,offset: type //具体配置参考：http://www.layui.com/doc/modules/layer.html#offset
                    ,id: 'layerDemo'+type //防止重复弹出
                    ,content: '<div style="padding: 20px 100px;">'+ msgString +'</div>'
                    ,btn: '关闭全部'
                    ,btnAlign: 'c' //按钮居中
                    ,shade: 0 //不显示遮罩
                    ,yes: function(){
                        layer.closeAll();
                    }
                });*/
            },
            before: function(obj){
                layer.load(1);
                $('#uploadbtn').attr("disabled",true);
                $('#uploadbtn').attr("readonly","readonly")//将input元素设置为readonly
            }
        });
    });

    //初始化咨询类型
    $.ajax({
        type: 'GET',
        url: prefix + '/getAllConsultype',
        success: function (r) {
            //成功后把数据添加到下拉框中
            for(var i=0;i<r.length;i++){
                $("#consultType").append("<option text="+r[i]+" value="+r[i]+">"+r[i]+"</option>")
            }
        }
    });

});

$.validator.setDefaults({
	submitHandler : function() {
		save();
	}
});
function save() {
    var data = $('#signupForm').serialize();
    $.ajax({
        cache : true,
        type : "POST",
        url : prefix +"/checkOrder",
        data : data,// 你的formid
        async : false,
        error : function(request) {
            parent.layer.alert("Connection error");
        },
        success : function(res) {
            if (res.code == 200) {
                var msg = "此件于"+res.data.consultTime+"已发起过相同类型的咨询单记录,咨询单号("+res.data.orderId+"),是否继续发起?"
                layer.confirm(msg, {
                    btn: ['是', '否', '查看'] //可以无限个按钮
                    ,btn3: function(index){
                        var orderStr = JSON.stringify(res.data);
                        orderStr = orderStr.replace(/"/g,"&^");
                        orderStr = orderStr.replace(/;/g,"&_");
                        //按钮【按钮三】的回调
                        //发起ajax请求查看详情
                        layer.open({
                            type : 2,
                            title : '咨询单详情页面',
                            maxmin : true,
                            shadeClose : false, // 点击遮罩关闭层
                            area : [ '100%', '100%' ],
                            content : prefix + '/detail/' + orderStr // iframe的url
                        });
                    }
                }, function(index, layero){
                    //按钮【按钮一】的回调
                    $.ajax({
                        cache : true,
                        type : "POST",
                        url : prefix +"/save",
                        data : data,// 你的formid
                        async : false,
                        error : function(request) {
                            parent.layer.alert("Connection error");
                        },
                        success : function(data) {
                            if (data.code == 0) {
                               layer.close(index);
                                parent.layer.msg("操作成功");
                            } else if(data.code==-1){
                                parent.layer.alert(data.msg)
                            }else {
                                parent.layer.alert(data.msg)
                            }

                        }
                    });
                }, function(index){
                    //按钮【按钮二】的回调
                });
            }else{
                $.ajax({
                    cache : true,
                    type : "POST",
                    url : prefix +"/save",
                    data : data,// 你的formid
                    async : false,
                    error : function(request) {
                        parent.layer.alert("Connection error");
                    },
                    success : function(data) {
                        if (data.code == 0) {
                            parent.layer.msg("操作成功");
                        } else if(data.code==-1){
                            parent.layer.alert(data.msg)
                        }else {
                            parent.layer.alert(data.msg)
                        }

                    }
                });
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

function resetForm(){
    document.getElementById("signupForm").reset();
}


//导出excel
function exportExcel(action) {
    layer.load(1);
    $('#exportExcelbtn').attr("disabled",true);
    $('#exportExcelbtn').attr("readonly","readonly")//将input元素设置为readonly

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
            }

            layer.msg("下载成功");
        }

        layer.closeAll('loading');
        $('#exportExcelbtn').attr("disabled",false);
        $('#exportExcelbtn').removeAttr("readonly");//去除input元素的readonly属性
    };
    // 发送ajax请求
    xhr.send()
}


$('#merchant').editableSelect({
//        effects: 'slide',
    onSelect:function (element) {
        $('.merchant').val(element.attr('value'));
    },
    onHide:function(){
        //console.log($('.merchant').val())
    }
}).prop('placeholder','请选择');

