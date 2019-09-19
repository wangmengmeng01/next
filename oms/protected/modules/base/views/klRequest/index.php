<script type='text/javascript'>
var osType = getOs(); 
var queryJson='[{\n' +
    '\t"advent_prd_sold_days": 0,\n' +
    '\t"brand_name": "迪奥-auto-2016071210192729哈哈~~~",\n' +
    '\t"code_ts": "09010100",\n' +
    '\t"composite_tax_rate": "0.11900",\n' +
    '\t"consumption_tax": "0001",\n' +
    '\t"customs_alias": "",\n' +
    '\t"expiration_days": 0,\n' +
    '\t"first_unit": "座",\n' +
    '\t"first_unit_count": 1.000000,\n' +
    '\t"goods_declare_count": 1.000000,\n' +
    '\t"goods_declare_unit": "座",\n' +
    '\t"goods_en_name": "",\n' +
    '\t"goods_id": 58722933,\n' +
    '\t"goods_name": "普通商品",\n' +
    '\t"goods_no": "ADAS57257257",\n' +
    '\t"hscode10": "6111200050",\n' +
    '\t"hscode10_des": "针织婴儿套装",\n' +
    '\t"is_bill": 0,\n' +
    '\t"kaola_factory_shop": false,\n' +
    '\t"last_category_name": "香水",\n' +
    '\t"need_advent_manage": 0,\n' +
    '\t"need_exp_manage": 0,\n' +
    '\t"origin_country": "100",\n' +
    '\t"picture_url_list": [],\n' +
    '\t"product_sku_code": "7000000857691",\n' +
    '\t"sale_unit": "头",\n' +
    '\t"second_unit": "",\n' +
    '\t"second_unit_count": 0.000000,\n' +
    '\t"shelf_limit_days": 0,\n' +
    '\t"sku_barcode": "20170323101614396,newcodedafd",\n' +
    '\t"sku_desc": "默认",\n' +
    '\t"sku_id": "N412810-72bcf4e0ae1947bba73418bc1abd7af4",\n' +
    '\t"sku_type": 0,\n' +
    '\t"storage_id": 412810,\n' +
    '\t"storage_limits_days": 0,\n' +
    '\t"storage_name": "极智嘉郑州保税测试仓",\n' +
    '\t"suggest_price": 100,\n' +
    '\t"update_time": 1526887951860,\n' +
    '\t"value_added_tax": "001",\n' +
    '\t"value_added_tax_rate": "0.17",\n' +
    '\t"warehouse_limit_days": 0,\n' +
    '\t"weight": "0.500"\n' +
    '}]';

function subApi(){
	var method = $('#ApiListMethod').combobox('getValue');
	if(method == '请选择接口'){
		$.messager.show({title:'友情提示', msg:'选择接口'});
		return false;
	};
	var formData  = $('#ff').serializeToJson(true);
	$.post('./index.php?r=base/klRequest/sub', formData, function(data) {
			if(data.status == 'ok'){
				$('#ApiListResult').val(osType=='MSIE'?formatJson(data.msg):data.msg);
			}else{
				$.messager.show({title:'友情提示', msg: data.msg});
			}
	}, 'json');
}

window.onload = function(){  
    $("#ApiListMethod").combobox({  
        //相当于html >> select >> onChange事件  
        onChange:function(){ 
            if($(this).combobox('getValue')=='119'){
            	$('#ApiListJson').val(osType=='MSIE'?formatJson(queryJson):queryJson);
            }
			$('#ApiListResult').val('');
        }
    })  
}

//格式化json
function formatJson(json, options) {
    var reg = null,
        formatted = '',
        pad = 0,
        PADDING = '    ';
    options = options || {};
    options.newlineAfterColonIfBeforeBraceOrBracket = (options.newlineAfterColonIfBeforeBraceOrBracket === true) ? true : false;
    options.spaceAfterColon = (options.spaceAfterColon === false) ? false : true;
    if (typeof json !== 'string') {
        json = JSON.stringify(json);
    } else {
        json = JSON.parse(json);
        json = JSON.stringify(json);
    }
    reg = /([\{\}])/g;
    json = json.replace(reg, '\r\n$1\r\n');
    reg = /([\[\]])/g;
    json = json.replace(reg, '\r\n$1\r\n');
    reg = /(\,)/g;
    json = json.replace(reg, '$1\r\n');
    reg = /(\r\n\r\n)/g;
    json = json.replace(reg, '\r\n');
    reg = /\r\n\,/g;
    json = json.replace(reg, ',');
    if (!options.newlineAfterColonIfBeforeBraceOrBracket) {
        reg = /\:\r\n\{/g;
        json = json.replace(reg, ':{');
        reg = /\:\r\n\[/g;
        json = json.replace(reg, ':[');
    }
    if (options.spaceAfterColon) {
        reg = /\:/g;
        json = json.replace(reg, ':');
    }
    (json.split('\r\n')).forEach(function (node, index) {
            var i = 0,
                indent = 0,
                padding = '';

            if (node.match(/\{$/) || node.match(/\[$/)) {
                indent = 1;
            } else if (node.match(/\}/) || node.match(/\]/)) {
                if (pad !== 0) {
                    pad -= 1;
                }
            } else {
                indent = 0;
            }

            for (i = 0; i < pad; i++) {
                padding += PADDING;
            }

            formatted += padding + node + '\r\n';
            pad += indent;
        }
    );
    return formatted;
}

//格式化xml  
function formatXml(str){      
     //去除输入框中xmll两端的空格。   
       str = str.replace(/^\s+|\s+$/g,"");   
       var source = new ActiveXObject("Msxml2.DOMDocument");   
      //装载数据   
       source.async = false;   
       source.loadXML(str);      
       // 装载样式单   
       var stylesheet = new ActiveXObject("Msxml2.DOMDocument");   
       stylesheet.async = false;   
       stylesheet.resolveExternals = false;   
       stylesheet.load("./static/js/moudles/base/format.xsl");   
         
       // 创建结果对象   
       var result = new ActiveXObject("Msxml2.DOMDocument");   
       result.async = false;   
         
       // 把解析结果放到结果对象中方法1   
       source.transformNodeToObject(stylesheet, result);   
       //alert(result.xml);  
       if(result.xml==''||result.xml==null){  
            alert('xml报文格式错误，请检查');  
            return false;  
           }  
       var finalStr = result.xml;  
       return finalStr;
} 

function getOs() 
{
   if(navigator.userAgent.indexOf("MSIE")>0) { 
        return "MSIE"; 
   } 
   else if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){ 
        return "Firefox"; 
   } 
   else if(isMozilla=navigator.userAgent.indexOf("Opera")>0){ //这个也被判断为chrome
        return "Opera"; 
   } 
   else if(isFirefox=navigator.userAgent.indexOf("Chrome")>0){ 
        return "Chrome"; 
   } 
   else if(isSafari=navigator.userAgent.indexOf("Safari")>0) { 
        return "Safari"; 
   }  
   else if(isCamino=navigator.userAgent.indexOf("Camino")>0){ 
        return "Camino"; 
   } 
   else if(isMozilla=navigator.userAgent.indexOf("Gecko/")>0){ 
        return "Gecko"; 
   } 
}
</script>

<div class="easyui-layout" fit="true" id="subLayout">
	<form id="ff">
		<div region="north" title="接口类型" border="true" split="true"
			style="height: 100px; background: #B3DFDA; padding: 10px">
			<table>
				<tr>
					<td>
						<h2>选择接口：</h2>
					</td>
					<td><select class="easyui-combobox" id="ApiListMethod" onChange="change()" name="ApiList[method]" style="width: 455px;" />
						<option value='请选择接口'>请选择接口</option>
						<option value='119'>商品同步接口</option>
				        </select>
				    </td>
					<td>
						<div id="dlg-buttons">
							<a class="easyui-linkbutton" iconCls="icon-ok"
								onclick="subApi()">Submit</a>
						</div>
					</td>
				</tr>
			</table>
		</div>
		<div region="west" split="true" title="原始XML格式数据"
			style="width: 26%;">
			<textarea id="ApiListJson" name="ApiList[json]"
				style="overflow: auto; border: 0; width: 98%; height: 99%;">Hi,I am easyui.</textarea>
		</div>
		<div region="center" title="返回的结果" border="true" split="true">
			<textarea id="ApiListResult" name="ApiList[result]"
				style="overflow: auto; border: 0; width: 98%; height: 99%;">Hi,I am easyui.</textarea>
		</div>
	</form>
</div>
