<template>
  <div class="bg" ref='pdf'>
		<div class="box">
		  <div>业务中台-用户中心</div>
		  <div class="text">
		    <span>用户中心业务接口白皮书</span>
		    <span>作者：赵严</span>
		    <span>联系人：赵严</span>
		    <span>版本：V1.0.0</span>
		    <span>发布日期：2019-06-13</span>
		  </div>
		  <div class="text" style="margin-top:10px;">
		    <span>1 概述</span>
		    <span>1.1 文档目的1 概述</span>
		    <span>1.1 文档目的</span>
		    <span>版本：V1.0.0</span>
		    <span>发布日期：2019-06-13</span>
		  </div>
		  <div class="text" style="margin-top:10px;">
		    <span>本文档主要讲述CP系统如何对接PAC。 如通讯模型，基础参数约定, 数据安全策略,及统一错误编码等</span>
		  </div>
		  <div class="text" style="margin-top:10px;">
		    <span>1.2 名词说明</span>
		    <span>名称      说明</span>
		    <span>PAC      全称为partner access center，即菜鸟合作伙伴接入中心，是菜鸟与合作伙伴数据交换的统一对接平台</span>
		    <span>CP      CP: 全称为cainiao partner,即菜鸟合作伙伴</span>
		    <span>网关类型      PAC支持以不同的params组合通过HTTP协议请求CP系统和被CP系统请求，不同的params组合构成了不同的网关</span>
		  </div>
		  <div>
		    <span>2 PAC请求CP</span>
		  </div>
		  <div class="text" style="margin-top:10px;">
		    <span>PAC支持通过HTTP(GET、POST)、webservice协议请求CP系统。</span>
		    <span>PAC支持以不同的params组合（网关类型）通过HTTP协议请求CP，具体的网关类型如下：</span>
		  </div>
		  <img @click="getPdf()" src="../assets/imgs/xiazai.png">
		</div>
		<img src="" alt="" id="pdf1" crossorigin>

  </div>
</template>

<script>
// @ is an alias to /src
import html2canvas from "html2canvas";
export default {
  name: 'home',
  data() {
    return {
    }
  },
  components: {
  },
  created() {
  },
  methods: { 
    get(i) {
    },
		getPdf(){
      const cntElem = this.$refs.pdf;

      var shareContent = cntElem; //需要截图的包裹的（原生的）DOM 对象` 
      var width = shareContent.offsetWidth; //获取dom 宽度
      var height = shareContent.offsetHeight; //获取dom 高度
      var canvas = document.createElement("canvas"); //创建一个canvas节点
      var scale = 2; //定义任意放大倍数 支持小数
      canvas.width = width * scale
      canvas.height = height * scale
      canvas.getContext("2d").scale(scale, scale); //获取context,设置scale
      var context = canvas.getContext('2d')
      context.translate('-'+(shareContent.offsetLeft), '-'+(shareContent.offsetTop))
      var opts = {
        dpi: window.devicePixelRatio * scale,
        scale: scale, // 添加的scale 参数
        canvas, //自定义 canvas
        logging: true, //日志开关，便于查看html2canvas的内部执行流程
        width: width, //dom 原始宽度
        height: height,
        useCORS: true, // 【重要】开启跨域配置
      };
      html2canvas(shareContent, opts).then(canvas => {
        var context = canvas.getContext('2d');
        // 【重要】关闭抗锯齿
        context.mozImageSmoothingEnabled = false;
        context.webkitImageSmoothingEnabled = false;
        context.msImageSmoothingEnabled = false;
        context.imageSmoothingEnabled = false;
        var dataUrl = canvas.toDataURL()
				//最优化版A4
				 let contentWidth = canvas.width
        let contentHeight = canvas.height
        let pageHeight = contentWidth / 592.28 * 841.89
        let leftHeight = contentHeight
        let position = 0
        let imgWidth = 595.28
        let imgHeight = 592.28 / contentWidth * contentHeight
        let pageData = canvas.toDataURL('image/JPEG', 1.0)  //压缩的base64，PDF文件
        let PDF = new jsPDF('', 'pt', 'a4')
        if (leftHeight < pageHeight) {
          PDF.addImage(pageData, 'JPEG', 0, 0, imgWidth, imgHeight)
        } else {
          while (leftHeight > 0) {
            PDF.addImage(pageData, 'JPEG', 0, position, imgWidth, imgHeight)
            leftHeight -= pageHeight
            position -= 841.89
            if (leftHeight > 0) {
              PDF.addPage()
            }
          }
        }
        // console.log(pageData)  //（获取压缩的PDF文件图片数据）
        PDF.save('tt' + '.pdf') //导出PDF文件（导出PDF需要用这个）

				 // 592.28 * 841.89 A4纸宽高
				// var doc = new jsPDF("l", "pt", [width,height]);
  
// doc.setFontSize(40);
// doc.text(35, 25, "Octonyan loves jsPDF");
// doc.addImage(dataUrl, 'PNG', 0, 0, width, height);
//  doc.save('AA.pdf')


// 				 let aLink = document.createElement('a');
//          aLink.href = dataUrl;
//          aLink.download = 'test.png';
//          document.body.appendChild(aLink);
//          aLink.click();
//          document.body.removeChild(aLink)
      });
			
			// 		 var pdf = new jsPDF('p','pt','a4');
			// // 设置打印比例 越大打印越小
			//    pdf.internal.scaleFactor = 2;
			//    var options = {
			//        pagesplit: true, //设置是否自动分页
			//       "background": '#FFFFFF'   //如果导出的pdf为黑色背景，需要将导出的html模块内容背景 设置成白色。
			//   };
			//    // var printHtml = $('#pdfDom').get(0);   // 页面某一个div里面的内容，通过id获取div内容
			//    pdf.addHTML($('#pdfDom').get(0),15, 15, options,function() {
			//       pdf.save('目标.pdf');
			//   });
				
    },
  }
}
</script>
<style lang="scss" scoped>
.box {
	padding: 10px 20px;
	font-size: 13px;
	color: #333333;
	&>div:first-child {
		font-size: 20px;
		font-weight: bold;
	}
	.text {
		margin-top: 20px;
		span {
			line-height: 20px;
			display: block;
		}
	}
	&>img {
		position: absolute;
		right: 80px;
		top: 30px;
		width: 33px;
		height: 29px;
	}
}
</style>