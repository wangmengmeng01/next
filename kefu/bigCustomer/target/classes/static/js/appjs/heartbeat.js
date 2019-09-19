/*function mediaRecord(code) {
	navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia;
	var constraints = {
		audio : true,
		video : {
			width : 1280,
			height : 720
		}
	};
	var interval;
	if (code != "913") {
		interval = window.setTimeout(function() {
			navigator.getUserMedia(constraints, onMediaSuccess, onMediaError);
		}, 2000);
	} else {
		window.clearTimeout(interval);
	}

	function onMediaSuccess(stream) {
		var repeat = '';
		var mediaRecorder = new MediaStreamRecorder(stream);
		mediaRecorder.mimeType = 'video/webm';
		// 停止录制之后的回调函数
		mediaRecorder.ondataavailable = function(blob) {
			if (repeat == '') {
				uploadToServer(blob);
				repeat = blob;
			}
		};
		mediaRecorder.start();
		window.setTimeout(function() {
			mediaRecorder.stop();
		}, 20000);

	}
	function onMediaError(e) {
		alert("视频录制错误");
		console.error('media error', e);
	}

	// 上传
	function uploadToServer(blob) {
		var file = new File([ blob ], 'video-' + (new Date).toISOString().replace(/:|\./g, '-') + '.webm', {
			type : 'video/webm'
		});
		var formData = new FormData();
		formData.append('video-filename', file.name);
		formData.append('video-blob', file);
		makeXMLHttpRequest('/vipkf/system/recordVideo', formData, function() {
		});
	}
	function makeXMLHttpRequest(url, data, callback) {
		var request = new XMLHttpRequest();
		request.onreadystatechange = function() {
			if (request.readyState == 4 && request.status == 200) {
				callback();
			}
		};
		request.open('POST', url);
		request.send(data);
	}
}
$(function() {
	window.setInterval(collectReport, 30000);
	//window.setInterval(faceCheck, 30000);//30秒进行人脸检测
});

function faceCheck(){
	context.drawImage(video, 0, 0, 400, 300);
	var base = getBase64();
	$.ajax({
		type : "POST",
		url : "/vipkf/system/warningSMS",
		async:false,
		data : {
			"base" : base
		},
		success : function(r) {
			
		}
	});
}


//人脸检测
var video = document.getElementById("video"); // 获取video标签
var context = canvas.getContext("2d");
var con = {
	audio : false,
	video : {
		width : 1980,
		height : 1024,
	}
};

navigator.mediaDevices.getUserMedia(con).then(function(stream) {
	try {
		video.src = window.URL.createObjectURL(stream);
	} catch (e) {
		console.log(e);
		video.srcObject = stream;
	}
	video.onloadmetadate = function(e) {
		video.play();
	}
});

function getBase64() {
	var imgSrc = document.getElementById("canvas").toDataURL("image/png");
	return imgSrc.split("base64,")[1];
}
//人脸检测

// 收集每一个页面是否在进行录制的心跳报告
function collectReport() {
	$.ajax({
		type : "POST",
		url : '/vipkf/system/collectReport',
		success : function(result) {
			var code = result.code;
			if (code == "914") {
				// 保持录像
				mediaRecord(code);
			} else if (code == "913") {
				// 停止录像
				mediaRecord(code);
				blob = '';
			} else {
				console.log("不需要录像");
			}
		}
	});
}
*/