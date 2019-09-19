/**
 * dp UI 1.0
 */
 
(function($){
	
	function _getCallbacks(options, dgId) {
		var reload = function() {
			$(dgId).datagrid('reload');
		};
		var callbacks = new Array();
		callbacks.push(options.callback);
		if (options.fresh) {
			callbacks.push(reload);
		}
		return callbacks;
	}
	
	function _getParams(options, dgId, methodName) {
		var params, fieldName, formId, index, modifyFields;
		params = options.params;
		fieldName = options.fieldName || 'id';
		formId = options.formId;
		index = options.index;
		
		// 修改单条记录时
		if (!params && formId && methodName == 'ydUpdate') params = $('#' + formId).serializeToJson();
		
		if (methodName == 'ydDelete' && index != undefined) {
			// 根据index获取行主键的值,将该值与fieldName组成的键值对放入params中
			var rowData = $(dgId).datagrid('getRows')[index], obj = {};
			obj[fieldName] = rowData[fieldName];
			params = $.extend(obj, params);
		
		// 批量修改，删除
		} else {
			// 根据用户选择的行记录，获取fieldName集合，用","组装成字符串
			var rowDatas, fieldNameStr = '', obj = {};
			rowDatas = $(dgId).datagrid('getSelections');
			for (var i in rowDatas) {
				fieldNameStr += rowDatas[i][fieldName] + ',';
			}
			if (fieldNameStr) {	// 去除最后一个","
				fieldNameStr = fieldNameStr.substring(0, fieldNameStr.length - 1);
			}
			obj[fieldName] = fieldNameStr;
			params = $.extend(obj, params);
		}
		
		// 批量修改
		modifyFields = options.modifyFields || {};
		params = $.extend(modifyFields, params);
		return params;
	}
	
	/**
	 * 表单提交接口
	 */
	$.ydSubmit = function(options) {
		options = options || {};
		options.paramType = options.paramType || 'string';		// 默认向后台请求的参数类型为string类型
		var params = options.params;
		var formId = options.formId || 'ff';
		if (!options || !options.url) {
			$.alert('参数或url不能为空');
			return;
		}
		if (options.scope) {									// 如果配置scope，那么显示遮盖层，防止重复提交
			$.progress('open');
		}
		
		if (!params) params = $('#' + formId).serializeToJson();	// 如果用户没有提供请求参数，则从表单获取
		
		if (options.paramType == "string") { 			// 如果后台需要字符串格式参数，则拼装
			params = JSON.stringify(params);
			params = "_data_=" + params + "&_time_=" + new Date().getTime() + "&_type_=json";
		}
		if (options.async === undefined ) {
			options.async = true;
		}
		
		$.ajax({
			url: options.url,
			type: options.type || "POST",
			cache : options.cache || false,
			dataType: options.dataType || "json",
			async: options.async,
			data: params,
			beforeSend: options.beforeSend,   			// 可修改XMLHttpRequest 对象的函数，如添加自定义 HTTP 头,
			success: function(data) {
				if (data == null || data == "") {
					return;
				}
				
				var callbacks = options.callback;
				if (typeof options.callback === 'function') {
					callbacks = [options.callback];
				}
				if (!callbacks) {
					if (data.result) {
						$.alert("操作成功");
					} else {
						$.alert("操作失败");
					}
					return;
				}
				var $obj = this;
				for(var i in callbacks) {
					if (callbacks[i] && typeof callbacks[i] === 'function') {
						var callback = callbacks[i];
						callback.call($obj, data);
					}
				}
			},
			complete: function(XHR, TS) {
				if (options.scope) {
					$.progress('close');
				}
				if (XHR.getResponseHeader("sessionStatus") === '400') {
					// 登陆页面根据自己的情况配
					//window.parent.location.href = $GLOBAL.contextPath + '/login.html';
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				XMLHttpRequest.abort();					// 中断请求
				$.alert("服务器繁忙，请稍后再试");
			}
		});
	};
	
	/*
	 * 添加或修改统一调用接口
	 */
	$.fn.ydAddOrUpdate = function(options) {
		var $this = this;
		return this.each(function() {
			options = options || {};
			if (options.isAdd) {
				$this.ydAdd(options);
			} else {
				$this.ydUpdate(options);
			}
		});
	};
	
	/*
	 * 添加行记录
	 */
	$.fn.ydAdd = function(options) {
		var $this = this;
		return this.each(function() {
			options = options || {};
			if (!options) {
				$.alert('参数不能为空');
				return;
			}
			options.callback = _getCallbacks(options, $this);
			if (!$('#' + options.formId).form('validate')) return;
			$.ydSubmit(options);
		});
	};
	
	/*
	 * 修改行指定行记录
	 */
	$.fn.ydUpdate = function(options) {
		var $this = this;
		return this.each(function() {
			$.confirm('您确认执行操作吗?', function(ok) {
				if (ok) {
					options = options || {};
					if (!options) {
						$.alert('参数不能为空');
						return;
					}
					
					options.params = _getParams(options, $this, 'ydUpdate');
					options.callback = _getCallbacks(options, $this);
					if (!$('#' + options.formId).form('validate')) return;
					$.ydSubmit(options);
				}
			});
		});
	};
	
	/*
	 * 批量修改数据
	 */
	$.fn.ydBatchUpdate = function(options) {
		var $this = this;
		return this.each(function() {
			$.confirm('您确认执行操作吗?', function(ok) {
				if (ok) {
					options = options || {};
					if (!options) {
						$.alert('参数不能为空');
						return;
					}
					options.params = _getParams(options, $this, 'ydBatchUpdate');
					options.callback = _getCallbacks(options, $this);
					$.ydSubmit(options);
				}
			});
		});
	};
	
	/**
	 * 表格多条记录，删除一条记录
	 * if (options.index)  删除一行数据，否则删除被选中的所有行数据
	 */
	$.fn.ydDelete = function(options) {
		var $this = this;
		return this.each(function() {
			$.confirm('您确认执行操作吗?', function(ok) {
				if (ok) {
					options = options || {};
					if (!options) {
						$.alert('参数不能为空');
						return;
					}
					options.params = _getParams(options, $this, 'ydDelete');
					options.callback = _getCallbacks(options, $this);
					$.ydSubmit(options);
				}
			});
		});
	};
	
	/**
	 * datagrid 按钮控制功能，
	 * options.setBtn为按钮控制的控制函数
	 */
	$.fn.ydatagrid = function(options) {
		var $this = this;
		return this.each(function() {
			options = options || {};
			if (!options) {
				$.alert('参数不能为空');
				return;
			}
			
			var defaults = {
				onCheck: function(rowIndex, rowData) {
					if (options.setBtn && typeof options.setBtn === 'function') {
						options.setBtn.apply(this, arguments);
					}
		        },
		        onCheckAll: function(rows) {
		        	if (options.setBtn && typeof options.setBtn === 'function') {
						options.setBtn.apply(this, arguments);
					}
		        },
		        onSelect: function() {
		        	if (options.setBtn && typeof options.setBtn === 'function') {
						options.setBtn.apply(this, arguments);
					}
		        },
		        onUnselect: function() {
		        	if (options.setBtn && typeof options.setBtn === 'function') {
						options.setBtn.apply(this, arguments);
					}
		        },
		        onUncheck: function(rowIndex, rowData) {
		        	if (options.setBtn && typeof options.setBtn === 'function') {
						options.setBtn.apply(this, arguments);
					}
		        },
		        onUncheckAll: function(rows) {
		        	if (options.setBtn && typeof options.setBtn === 'function') {
						options.setBtn.apply(this, arguments);
					}
		        },
		        onSelectAll: function() {
		        	if (options.setBtn && typeof options.setBtn === 'function') {
						options.setBtn.apply(this, arguments);
					}
		        },
		        onUnselectAll: function() {
		        	if (options.setBtn && typeof options.setBtn === 'function') {
						options.setBtn.apply(this, arguments);
					}
		        },
				onLoadSuccess: function(data) {
					if (options.setBtn && typeof options.setBtn === 'function') {
						options.setBtn.apply(this, arguments);
					}
					if (data.rows.length == 0) {
						$(this).datagrid('getPanel').find('.datagrid-btable').css({width: '100%'}).append('<tr>'
							+'<td style="text-align:center;"><font size="3px;" color="red" face="微软雅黑">没有可用的数据!</font></td>'
						+'</tr>');
					}
				}	
			};
			options = $.extend(defaults, options);
			$this.datagrid(options);
		});
	};
	
	// 进度条
	$.progress = function(type) {
		if (type == 'open') {
			$.messager.progress();
		} else {
			$.messager.progress('close');
		}
	};
	
	// 提示框
	$.alert = function(message, type, callback) {
		type = type || 'info';
		$.messager.alert('系统提示', message, type, callback);
	};
	
	// 确认框
	$.confirm = function(msg, callback) {
		$.messager.confirm('系统提示', msg, callback);
	};
	
	
	/**
	 * 格式化表单内容为json对象（通常用于获取一个form表单的值）
	 *
	 * @param	notEmptyField	序列化的结果中是否包含值为空的域
	 *
	 * @return	当前选择器所选定的值域标签对应的值json对象
	 */
	$.fn.serializeToJson = function(notEmptyField){
	    var obj = {};
	    $.each( this.serializeArray(), function(i,o){
	        var n = o.name, v = $.trim(o.value);
	        if (!(notEmptyField && "" == v)) {
	        	obj[n] = (obj[n] === undefined) ? v : $.isArray(obj[n]) ? obj[n].concat(v) : [obj[n], v];
	        }
	    });
	    return obj;
	};
	
	/**
	 * 将json中的值填充到页面中（可以按field指定的属性进行赋值）
	 *
	 * @param	jsonObject	用于填充的json数据对象
	 * @param	filed	填充数据jsonObject中的key对应标签的那个属性值，用于定位input域进行赋值，默认为
	 *					不指定则为name($('name=key').val(value))，否则为field指定的标签属性					
	 * @param	type	填充数据的方式，text：将数据填充到text域中，否则填充到value域中
	 */
	$.fn.serializeToForm = function(jsonObject, filed, type){
		if(!jsonObject)return false;
	     for(var key in jsonObject) {
	     	var inputObj = "";
	     	if (filed) {
	     		inputObj = this.find("["+filed+"='"+key+"']");
	     	} else {
	     		inputObj = this.find("[name='"+key+"']");
	     	}
	     	if (inputObj && inputObj.size() > 0 && jsonObject[key] && "null" != jsonObject[key]) {
	     		if (type && 'text'== type) {
	     			inputObj.text(jsonObject[key]);
	     		} else {
	     			inputObj.val(jsonObject[key]);
	     		}
	     	}
	     }
	};
	
	/**
	 *	自适应表格的宽度处理(适用于Jquery Easy Ui中的dataGrid),
	 *	可以实现列表的各列宽度跟着浏览宽度的变化而变化。
	 *
	 *	@param	percent	当前列的列宽所占整个窗口宽度的百分比(以小数形式出现，如0.3代表30%)
	 *  @param	bodyWidth	总宽度，不提供，则默认采用当前页面宽度
	 *
	 *	@return	通过当前窗口和对应的百分比计算出来的具体宽度值
	 */
    $.fillsize = function(percent,bodyWidth) {
    	if(!bodyWidth) {
			bodyWidth = $(document).width();
		}
		return parseInt(bodyWidth*percent);
    };
    
    /**
	 *	获取数据列表的总宽度
	 *
	 *	@param	minWidth	指定最小宽度，确保最后返回的结果一定大于等于该值，不指定的不考虑
	 *
	 *	@return	如果计算出的当前窗口总宽度小于minWidth，则返回minWidth，否则返回当前窗口总宽度
	 */
    $.getDatagridWidth = function(minWidth){
    	if(!minWidth)minWidth = 0;
		var width = ($(document).width()-1);
		return (minWidth > width) ? minWidth:width;
    };
    
    /**
	 *	获取数据列表的高度
	 *
	 *	@return	当前窗口的高度
	 */
    $.getDatagridHeight = function(){
    	return ($(document).height())
    };
    
    /**
	 * 自动调整弹出窗口的长和宽,以适应不同分辨率浏览器下的显示
	 *
	 * @param	dialogId	弹出窗口的id
	 * @param	widthRate	弹出窗口的宽度与浏览器所能提供的宽度(通常为弹出窗口所在子页面对应的Iframe的宽度)的比例,在不同分辨率下使用该比率自动调整
	 * @param	maxHeight	弹出窗口的最大高度
	 * @param	maxWidth	弹出窗口的最大宽度
	 *
	 * @param	自动调节后的窗口宽度
	 */
    var fillDialogWidthAndHeight = function(dialogId, widthRate, maxHeight, maxWidth) {
    	var currentBodyHeight = $(document).height();
		var currentBodyWidth = $(document).width();
		//当前iframe窗口的高宽比
		var heightToWidthRate =  currentBodyHeight/currentBodyWidth;
		var fillWidth = currentBodyWidth*widthRate;
		var fillHeight = fillWidth * heightToWidthRate;
		//如果当前iframe窗口按百分比计算出的宽度大于实际设置的最大值，则以最大值为准
		if (fillWidth >= maxWidth) {
			fillWidth = maxWidth;
		//如果计算出的宽度小于最大值则进一步调整
		} else {
			//如果当前窗口的宽度的95%大于设置的最大值，则自动调整到最大值，否则就取当前窗口的95%作为宽度
			if ((currentBodyWidth * (95/100)) > maxWidth) {
				fillWidth = maxWidth;
			} else {
				fillWidth = currentBodyWidth * (95/100);
			}
		}
		//如果当前iframe窗口按百分比计算出的高度值大于实际设置的最大值，则以最大值为准，否则进一步调整的方法与以上的宽度调整相同
		if (fillHeight >= maxHeight) {
			fillHeight = maxHeight;
		}
		//计算窗口左上角的坐标，使窗口居中
		var leftPos = (currentBodyWidth - fillWidth)/2;
		var topPos = (currentBodyHeight - fillHeight)/2;
		$('#'+dialogId).dialog("options").width=fillWidth;
		$('#'+dialogId).dialog("options").height=fillHeight;
		$('#'+dialogId).dialog("resize",{width:fillWidth,height:fillHeight,left:leftPos,top:topPos});
		return fillWidth;
    };
	    
	/**
	 * 用于格式化json对象到字符串的参数定义
	 */
	var m = {'\b': '\\b','\t': '\\t','\n': '\\n','\f': '\\f','\r': '\\r','"' : '\\"','\\': '\\\\'};
    var s = {'boolean': function (x) {
                return String(x);
            },
            number: function (x) {
                return isFinite(x) ? String(x) : 'null';
            },
            string: function (x) {
                if (/["\\\x00-\x1f"]/.test(x)) {
                    x = x.replace(/(["\x00-\x1f\\"])/g, function(a, b) {
                        var c = m[b];
                        if (c) {
                            return c;
                        }
                        c = b.charCodeAt();
                        return '\\u00' + Math.floor(c / 16).toString(16) + (c % 16).toString(16);
                    });
                }
                return '"' + x + '"';
            },
            object: function (x) {
                if (x) {
                    var a = [], b, f, i, l, v;
                    if (x instanceof Array) {
                        a[0] = '[';
                        l = x.length;
                        for (i = 0; i < l; i += 1) {
                            v = x[i];
                            f = s[typeof v];
                            if (f) {
                                v = f(v);
                                if (typeof v == 'string') {
                                    if (b) {
                                        a[a.length] = ',';
                                    }
                                    a[a.length] = v;
                                    b = true;
                                }
                            }
                        }
                        a[a.length] = ']';
                    } else if (x instanceof Object) {
                        a[0] = '{';
                        for (i in x) {
                            v = x[i];
                            f = s[typeof v];
                            if (f) {
                                v = f(v);
                                if (typeof v == 'string') {
                                    if (b) {
                                        a[a.length] = ',';
                                    }
                                    a.push(s.string(i), ':', v);
                                    b = true;
                                }
                            }
                        }
                        a[a.length] = '}';
                    } else {
                        return;
                    }
                    return a.join('');
                }
                return 'null';
            }
        };
    /**
   	 *	将指定的json对象解析成字符串
   	 *	
   	 *	@param	v	将要被解析的json对象
   	 *
   	 *	@return json解析后的字符串
   	 */
	$.jsonToString = function(jsonObject){
		 var f = s[typeof jsonObject];
         if (f) {
             jsonObject = f(jsonObject);
             if (typeof jsonObject == 'string') {
                 return jsonObject;
             }
         }
         return null;
	};
	
	/**
	 *	日期格式化函数，支持模式：YYYY/yyyy年MM月dd日hh小时mm分ss秒SSS毫秒
	 *
	 *	@param	date	日期，不指定，则取当前时间
	 *	@param	format	格式模式字符串
	 *
	 *	@reurn	格式化后的日期字符串
	 */
    $.dateFormat = function(dateObject, format) {
    	if (!dateObject)dateObject = new Date();
    	var o = { 
			"M+" : dateObject.getMonth()+1, //月 
			"d+" : dateObject.getDate(), //日
			"h+" : dateObject.getHours(), //小时
			"m+" : dateObject.getMinutes(), //分钟
			"s+" : dateObject.getSeconds(), //秒
			"q+" : Math.floor((dateObject.getMonth()+3)/3),//刻 
			"S" : dateObject.getMilliseconds() //毫秒 
		} 
		if(/(y+)/.test(format)) { 
			format = format.replace(RegExp.$1, (dateObject.getFullYear()+"").substr(4 - RegExp.$1.length)); 
		} 
		for(var k in o) { 
			if(new RegExp("("+ k +")").test(format)) { 
				format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
			} 
		} 
		return format; 
    };
	
})(jQuery);

$.fn.extend({
});