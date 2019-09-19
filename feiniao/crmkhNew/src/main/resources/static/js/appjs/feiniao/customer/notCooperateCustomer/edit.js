var prefix = "/crmkh/customer/notCooperateCustomer"
$().ready(function() {
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		update();
	}
});
function update() {
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
	var branchCode=$('#branchCodeH').val();
	var productType=$('#productType').val();
	//发送ajax
    $.ajax({
        cache : true,
        type : "GET",
        url : prefix +'/editHuiXianCooperatePeer/'+branchCode+"/"+productType,
        async : false,
        error : function(request) {
            parent.layer.alert("Connection error");
        },
        success : function(rsp) {
            var tableContainerTr = $('.table-container tr');
        	var customerPlatformH = $('#customerPlatformH').val();
        	var arr = customerPlatformH.split("、")
			console.log(arr)
			for (var i=0;i<arr.length;i++){

                if(arr[i]=="淘宝"){
        			$('#taobao').attr("checked","true");
					continue;
                }else if(arr[i]=="京东"){
                    $('#jingdong').attr("checked","true");
                    continue;
                }else if(arr[i]=="拼多多"){
                    $('#pinduoduo').attr("checked","true");
                    continue;
                }else{
                    $('#other').attr("checked","true");
                    $('#otherPlatForm').val(arr[i])
                }
			}
			console.log(tableContainerTr.length)
            for(var i=0;i<tableContainerTr.length;i++){
        		var tableName = tableContainerTr.eq(i).find('td').eq(1).attr('data-name')
				for(var j=0;j<rsp.data.length;j++){
        			var resArrName = rsp.data[j];
                    if(tableName == resArrName.cooperatePeerName){
                        tableContainerTr.eq(i).find('.cooperateRatio').val(resArrName.cooperateRatio)
                        tableContainerTr.eq(i).find('.cooperatePrice').val(resArrName.cooperatePrice)
                        tableContainerTr.eq(i).find('.remark').val(resArrName.remark)
                        // tableContainerTr.eq(i).find('.cooperateRatio').val(resArrName.cooperateRatio)
                    }
				}

            	/*alert(cooperatePeer);*/
            }
        }
    });

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

function quitAudit(){
    parent.reLoad();
    var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
    parent.layer.close(index);
}

function checkboxOnclick(checkbox){
    if (checkbox.checked == true){
		$('#otherPlatForm').attr("required","true")
    }
}