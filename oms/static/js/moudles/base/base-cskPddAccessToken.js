$(function () {
    $("#dg").datagrid({
        loadMsg: '正在加载中',
        title: '拼多多授权信息维护界面',
        url: './index.php?r=base/cskPddAccessToken/data',
        method: 'POST',
        rownumbers: true,
        height: $(window).height() - 50,
        pagination: true,
        pageSize: 50,
        columns: [[{
            field: 'ck',
            checkbox: true
        }, {
            field: 'shop_id',
            title: '店铺ID',
            align: 'center'
        }, {
            field: 'seller_id',
            title: '商家ID',
            align: 'center'
        }, {
            field: 'seller_name',
            title: '商家名称',
            align: 'center'
        }, {
            field: 'platform',
            title: '电子面单平台',
            align: 'center'
        }, {
            field: 'creater',
            title: '创建人',
            align: 'center'
        }, {
            field: 'is_valid',
            title: '有效性',
            align: 'center',
            formatter: function (value, rowData, rowIndex) {
                var jsondata = {
                    '1': '有效',
                    '0': '无效'
                };
                return jsondata[value];
            }
        }, {
            field: 'auth_time',
            title: '授权时间',
            align: 'center',
            width: '140'
        }, {
            field: 'auth',
            title: '授权',
            width: '80',
            align: 'center',
            formatter: function (value, rowData, rowIndex) {

                var href = '';

                if (rowData.is_valid == 1) {

                    href = authUrl + 'oms_' + rowData.shop_id
                }

                return '<a href="'+ href +'" target="_blank">授权</a>';
            }
        }, {
            field: 'operate',
            title: '操作',
            align: 'center',
            width: '160',
            formatter: function (value, rowData, rowIndex) {

                var shopId = rowData.shop_id;
                var disabledOk = '';
                var disabledNo = 'disabled';

                if (rowData.is_valid == 1) {

                    disabledOk = 'disabled';
                    disabledNo = '';
                }

                return '<input type="button" ' + disabledOk + ' value="启用" onclick="setCustomer(' + shopId + ', 1)">&nbsp;&nbsp;' +
                    '<input ' + disabledNo + ' type="button" value="禁用" onclick="setCustomer(' + shopId + ', 0)">';
            }
        }]],
        toolbar: [{
            id: 'add',
            title: '添加',
            text: '添加',
            iconCls: 'icon-add',
            disabled: false,
            handler: function () {
                addCustomer();
            }
        }]
    })
})

// 添加拼多多商家
function addCustomer() {

    $.ajax({
        url: './index.php?r=base/CskPddAccessToken/add',
        data: {operate: 'add'},
        method: 'post',
        dataType: 'json',
        success: function (res) {

            $.messager.show({
                title: '提示',
                msg: res.msg
            });

            if (res.status) {
                $("#dg").datagrid('reload');
            }
        }
    });
}

// 设置商家有效性
function setCustomer(shop_id, is_valid) {

    $.ajax({
        method: 'post',
        dataType: 'json',
        url: './index.php?r=base/CskPddAccessToken/setCustomer',
        data: {shop_id: shop_id, is_valid: is_valid},
        success: function (res) {

            $.messager.show({
                title: '提示',
                msg: res.msg
            })
            if (res.status) {
                $("#dg").datagrid('reload');
            }
        }
    })

}

function searchForm() {
    $("#dg").datagrid({
        queryParams: {
            'Seller[seller_id]': $("#sellerId").val(),
            'Seller[is_valid]': $("#isValid").combobox('getValue')
        }
    })
}
