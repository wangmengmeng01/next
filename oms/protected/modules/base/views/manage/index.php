<script type='text/javascript'>
var osType = getOs(); 
var queryINVData_xml='<xmldata><data><header><CustomerID>5180001001</CustomerID><WarehouseID>WH01</WarehouseID><SKU>A201</SKU><PageSize>10</PageSize><PageNo>1</PageNo></header></data></xmldata>';
var confirmINVData_xml='<xmldata><data><header><CustomerID>5180001001</CustomerID><WarehouseID>WH01</WarehouseID><SKU/><PageSize>10</PageSize><PageNo>1</PageNo></header></data></xmldata>';
var putCustData_xml='<xmldata><header><CustomerID>VE_103</CustomerID><Customer_Type>VE</Customer_Type><Descr_C>供应商VE_103</Descr_C><Contact1>李一</Contact1><Contact1_Tel1>13612300001</Contact1_Tel1><Active_Flag>Y</Active_Flag></header><header><CustomerID>VE_104</CustomerID><Customer_Type>VE</Customer_Type><Descr_C>供应商104</Descr_C><Contact1>张七</Contact1><Contact1_Tel1>13612300001</Contact1_Tel1><Active_Flag>Y</Active_Flag></header><header><CustomerID>OT_1002</CustomerID><Customer_Type>OT</Customer_Type><Descr_C>店铺1002</Descr_C><Contact1>李四</Contact1><Contact1_Tel1>13612300001</Contact1_Tel1><Active_Flag>Y</Active_Flag></header></xmldata>';
var putSKUData_xml='<xmldata><header><CustomerID>5180001001</CustomerID><SKU>A001</SKU><Active_Flag>Y</Active_Flag><Descr_C>衬衫</Descr_C><Price>399.99</Price><CycleClass>A</CycleClass><ShelfLife>10</ShelfLife><ReorderQty>199.99</ReorderQty><SKU_Group2>红色</SKU_Group2> </header></xmldata>';
var putASNData_xml='<xmldata><header><OrderNo>ERP.3000025</OrderNo><OrderType>PO</OrderType><CustomerID>5180001001</CustomerID><WarehouseID>WH01</WarehouseID><UserDefine4>ERP</UserDefine4><detailsItem><LineNo>1</LineNo><CustomerID>5180001001</CustomerID><SKU>A201</SKU><ExpectedQty>2000</ExpectedQty></detailsItem><detailsItem><LineNo>2</LineNo><CustomerID>5180001001</CustomerID><SKU>A002</SKU><ExpectedQty>5000</ExpectedQty></detailsItem></header></xmldata>';
var cancelASNData_xml='<xmldata><header><OrderNo>ERP.2000023</OrderNo><OrderType>PO</OrderType><CustomerID>5180001001</CustomerID><WarehouseID>WH_001</WarehouseID><Reason>取消原因</Reason></header></xmldata>';
var putSOData_xml='<xmldata><header><OrderNo>NO.000076</OrderNo><OrderType>TO</OrderType><CustomerID>5180001001</CustomerID><WarehouseID>WH01</WarehouseID><OrderTime>2015-06-04 18:20:00</OrderTime><SOReference2></SOReference2><ConsigneeName>张三丰</ConsigneeName><C_Country>CN</C_Country><C_Province>江苏省</C_Province><C_City>南京市</C_City><C_Tel1>13800000001</C_Tel1><C_Tel2>33333332</C_Tel2><C_Mail>wwww0001</C_Mail><C_Address1>上海市青浦区崧复路1253号</C_Address1><UserDefine4>OMS</UserDefine4><Notes>请发韵达快递</Notes><H_EDI_01>现金</H_EDI_01><H_EDI_02>105.8</H_EDI_02><H_EDI_10>8</H_EDI_10><CarrierId>YUNDA</CarrierId><CarrierName>韵达快递</CarrierName><InvoicePrintFlag>1</InvoicePrintFlag><ConsigneeID>10001</ConsigneeID><detailsItem><CustomerID>5180001001</CustomerID><SKU>A001</SKU><QtyOrdered>350</QtyOrdered></detailsItem><invoiceItem><OrderNo>200002</OrderNo><LineNumber>1</LineNumber><Title></Title><Reference1></Reference1><SKU>A002</SKU><UOM>个</UOM><QTY>2</QTY><UnitPrice>12</UnitPrice><Amount></Amount></invoiceItem></header></xmldata>';
var cancelSOData_xml='<xmldata><header><OrderNo>NO.000020</OrderNo><OrderType>SO</OrderType><CustomerID>5180001001</CustomerID><WarehouseID>WH01</WarehouseID><Reason>取消原因1</Reason></header></xmldata>';
var confirmSOData_xml='<xmldata><data><orderinfo><OMSOrderNo>NO.000079</OMSOrderNo><WMSOrderNo>X15060035</WMSOrderNo><OrderType>TO</OrderType><CustomerID>5180001001</CustomerID><WarehouseID>WH01</WarehouseID><OrderTime>2015-06-04 18:20:00</OrderTime><DeliveryNo /><Weight>0</Weight><CarrierId>YUNDA</CarrierId><CarrierName>韵达快递</CarrierName><ExpectedShipmentTime1>2015-06-10 22:12:16</ExpectedShipmentTime1><RequiredDeliveryTime /><SOReference2 /><SOReference3 /><SOReference4 /><SOReference5 /><ConsigneeID>10001</ConsigneeID><ConsigneeName>张三丰</ConsigneeName><C_Country /><C_Province>江苏省</C_Province><C_City>南京市</C_City><C_Tel1>13800000001</C_Tel1><C_Tel2>33333332</C_Tel2><C_ZIP /><C_Mail /><C_Address1>上海市青浦区崧复路1253号</C_Address1><C_Address2 /><C_Address3 /><UserDefine2 /><UserDefine3 /><UserDefine4>ERP</UserDefine4><UserDefine5 /><InvoicePrintFlag /><Notes>请发韵达快递</Notes><H_EDI_01>现金</H_EDI_01><H_EDI_02>105.8</H_EDI_02><H_EDI_03 /><H_EDI_04 /><H_EDI_05 /><H_EDI_06 /><H_EDI_07 /><H_EDI_08 /><H_EDI_09 /><H_EDI_10>8</H_EDI_10><UserDefine6 /><RouteCode /><Stop /><CarrierMail /><CarrierFax /><Channel>*</Channel><item><OrderNo>X15060028</OrderNo><LineNo>1</LineNo><SKU>A001</SKU><QtyOrdered>350</QtyOrdered><QtyShipped>350</QtyShipped><ShippedTime>2015-06-11 11:18:41</ShippedTime><DeliveryNo /><Weight /><Lotatt01 /><Lotatt02 /><Lotatt03 /><Lotatt04 /><Lotatt05 /><Lotatt06 /><Lotatt07 /><Lotatt08 /><Lotatt09 /><Lotatt10 /><Lotatt11 /><Lotatt12 /><UserDefine1 /><UserDefine2 /><UserDefine3 /><UserDefine4 /><UserDefine5 /><UserDefine6 /><Notes /><Price /><D_EDI_03 /><D_EDI_04 /><D_EDI_05 /><D_EDI_06 /><D_EDI_07 /><D_EDI_08 /><D_EDI_09 /><D_EDI_10 /><D_EDI_11 /><D_EDI_12 /><D_EDI_13 /><D_EDI_14 /><D_EDI_15 /><D_EDI_16 /></item></orderinfo></data></xmldata>';
var confirmSOStatus_xml='<xmldata><data><orderinfo><OrderNo>NO.000079</OrderNo><OrderType>TO</OrderType><CustomerID>5180001001</CustomerID><WarehouseID>WH01</WarehouseID><Status>40</Status><Desc>分配完成</Desc><Time>2015-06-03 15:02:10</Time><Udf1/><Udf2/><Udf3/><Udf4/><Udf5/><Udf6/><Udf7/><Udf8/><Udf9/><Udf10/></orderinfo></data></xmldata>';
var confirmASNData_xml='<xmldata><data><orderinfo><OMSOrderNo>ERP0000001</OMSOrderNo><WMSOrderno>WMS0000001</WMSOrderno><OrderType>PO</OrderType><CustomerID>5180001001</CustomerID><WarehouseID>WH_001</WarehouseID><Status>30</Status><Desc>部分收货</Desc><ASNCreationTime>2015-05-10 03:03:03</ASNCreationTime><UserDefine4>ERP</UserDefine4><item><CustomerID>5180001001</CustomerID><SKU>A001</SKU><LineStatus>40</LineStatus><LineDesc>完全收货</LineDesc><ExpectedQty>3000</ExpectedQty><ReceivedQty>3000</ReceivedQty><ReceivedTime>2015-05-10 03:03:03</ReceivedTime><LotAtt01></LotAtt01></item><item><CustomerID>5180001001</CustomerID><SKU>A002</SKU><LineStatus>30</LineStatus><LineDesc>部分收货</LineDesc><ExpectedQty>5000</ExpectedQty><ReceivedQty>4998</ReceivedQty><ReceivedTime>2015-05-10 03:03:03</ReceivedTime><LotAtt01></LotAtt01></item></orderinfo></data></xmldata>';
function subApi(){
	var method = $('#ApiListMethod').combobox('getValue');
	if(method == '请选择接口'){
		$.messager.show({title:'友情提示', msg:'选择接口'});
		return false;
	};
	var formData  = $('#ff').serializeToJson(true);
	$.post('./index.php?r=base/manage/sub', formData, function(data) {
			if(data.status == 'ok'){
				$('#ApiListCompile').val(osType=='MSIE'?formatXml(data.msg.compile):data.msg.compile);
				$('#ApiListResult').val(osType=='MSIE'?formatXml(data.msg.result):data.msg.result);
			}else{
				$.messager.show({title:'友情提示', msg: data.msg});
			}
	}, 'json');
}

window.onload = function(){  
    $("#ApiListMethod").combobox({  
        //相当于html >> select >> onChange事件  
        onChange:function(){ 
            if($(this).combobox('getValue')=='queryINVData'){
            	$('#ApiListXml').val(osType=='MSIE'?formatXml(queryINVData_xml):queryINVData_xml);
            }else if($(this).combobox('getValue')=='confirmINVData'){
            	$('#ApiListXml').val(osType=='MSIE'?formatXml(confirmINVData_xml):confirmINVData_xml);
            }else if($(this).combobox('getValue')=='putCustData'){
            	$('#ApiListXml').val(osType=='MSIE'?formatXml(putCustData_xml):putCustData_xml);
            }else if($(this).combobox('getValue')=='putSKUData'){
            	$('#ApiListXml').val(osType=='MSIE'?formatXml(putSKUData_xml):putSKUData_xml);
            }else if($(this).combobox('getValue')=='putASNData'){
            	$('#ApiListXml').val(osType=='MSIE'?formatXml(putASNData_xml):putASNData_xml);
            }else if($(this).combobox('getValue')=='cancelASNData'){
            	$('#ApiListXml').val(osType=='MSIE'?formatXml(cancelASNData_xml):cancelASNData_xml);
            }else if($(this).combobox('getValue')=='putSOData'){
            	$('#ApiListXml').val(osType=='MSIE'?formatXml(putSOData_xml):putSOData_xml);
            }else if($(this).combobox('getValue')=='cancelSOData'){
            	$('#ApiListXml').val(osType=='MSIE'?formatXml(cancelSOData_xml):cancelSOData_xml);
            }else if($(this).combobox('getValue')=='confirmSOData'){
            	$('#ApiListXml').val(osType=='MSIE'?formatXml(confirmSOData_xml):confirmSOData_xml);
            }else if($(this).combobox('getValue')=='confirmSOStatus'){
            	$('#ApiListXml').val(osType=='MSIE'?formatXml(confirmSOStatus_xml):confirmSOStatus_xml);
            }else if($(this).combobox('getValue')=='confirmASNData'){
            	$('#ApiListXml').val(osType=='MSIE'?formatXml(confirmASNData_xml):confirmASNData_xml);
            }
			$('#ApiListCompile').val('');
			$('#ApiListResult').val('');
        }
    })  
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
    var OsObject = ""; 
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

function return_xml(xml){
	var xmls=new XMLSerializer(); 
	var result=xmls.serializeToString(xml); 
	return result;
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
						<option value='putCustData'>客商档案接口（供应商、店铺）</option>
						<option value='putSKUData'>商品资料接口</option>
						<option value='putASNData'>入库单下发接口</option>
						<option value='cancelASNData'>入库单取消</option>
						<option value='putSOData'>出库单下发（销售出库、调拨出库、采购退货出库、盘亏出库）</option>
						<option value='cancelSOData'>出库单取消</option>
						<option value='queryINVData'>库存查询</option>
						<option value='confirmINVData'>库存推送</option>
						<option value='confirmASNData'>入库单状态明细回传</option> 
						<option value='confirmSOData'>出库单明细回传</option>
						<option value='confirmSOStatus'>出库单状态回传</option>  
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
			<textarea id="ApiListXml" name="ApiList[xml]"
				style="overflow: auto; border: 0; width: 98%; height: 99%;">Hi,I am easyui.</textarea>
		</div>
		<div region="east" split="true" title="经过编码过的数据" style="width: 33%;">
			<textarea id="ApiListCompile" name="ApiList[compile]"
				style="overflow: auto; border: 0; width: 98%; height: 99%;">Hi,I am easyui.</textarea>
		</div>
		<div region="center" title="返回的结果" border="true" split="true">
			<textarea id="ApiListResult" name="ApiList[result]"
				style="overflow: auto; border: 0; width: 98%; height: 99%;">Hi,I am easyui.</textarea>
		</div>
	</form>
</div>


















<!-- <div class="easyui-layout" fit="true" id="subLayout">
	<div region="north" title="north" border="true" split="true"
		style="height: 100px; background: #B3DFDA; padding: 10px"></div>
	<div region="west" split="true" title="sub West"
		style="width: 150px; padding: 10px;">sub west content</div>
	<div region="east" split="true" title="sub East"
		style="width: 100px; padding: 10px;">sub east region</div>
	<div region="south" border="true" split="true" title="sub south"
		style="height: 100px; background: #A9FACD; padding: 10px;">sub south
		region</div>
	<div region="center" title="sub Main  Title" border="true" split="true">
	</div>
</div> -->