<?php
/**
 * Notes:
 * User: passerby
 * Date: 2019/9/12
 * Time: 9:51
 */
?>
<style type="text/css">
	.wrapper .title {
		text-align: center;
		width: 100%;
		height: 80px;
		line-height: 80px;
		position: relative;
		padding: 0;
		font-size: 38px;
		font-weight: bold;
	}
	.wrapper .title span {
		font-size: 15px;
		line-height: 15px;
		font-weight: bold;
		position: absolute;
		right: 10px;
		bottom: 5px;
	}
	.box {
		display: flex;
		flex: 1;
	}
	.box div {
		height: 160px;
		line-height: 160px;
		width: 33.33%;
		display: flex;
		justify-content: space-around;
	}
	.box div span {
		font-size: 20px;
		color: #fff;
	}
	.box div span:last-child {
		font-size: 50px;
		color: #fff;
	}
</style>
<div class="wrapper" region="north" border="true" split="true"
	style="background: #fff; padding: 10px">
	<div class="title">韵仓管理部全国云仓数据播报<span class="time">时间</span></div>
	<div class="box">
		<div style="background: #000000;">
			<span>全国云仓个数</span>
			<span>99</span>
		</div>
		<div style="background: #92d050;">
			<span>加盟云仓个数</span>
			<span>99</span>
		</div>
		<div style="background: #82867e;">
			<span>系统云仓个数</span>
			<span>99</span>
		</div>
	</div>
	<div class="box">
		<div style="background: #568fd4;width: 50%;">
			<span>当月累计出库单量(票)</span>
			<span>99</span>
		</div>
		<div style="background: #0d3464;width: 50%;">
			<span>当月累计出库单量(票)</span>
			<span>99</span>
		</div>
	</div>
	<div class="box">
		<div style="background: #ed7d31;">
			<span>全国云仓累计面积(m<sup>2</sup>)</span>
			<span>99</span>
		</div>
		<div style="background: #edd00e;">
			<span>全国云仓累计面积(m<sup>2</sup>)</span>
			<span>99</span>
		</div>
		<div style="background: #870eed
		;">
			<span>全国云仓累计面积(m<sup>2</sup>)</span>
			<span>99</span>
		</div>
	</div>
	<div class="box">
		<div style="width: 50%;display: block;">
			<div style="background: #92d050;width: 100%;">
				<span>加盟云仓累计出库单量(票)</span>
				<span>99</span>
			</div>
			<div style="background: #ed7d31;width: 100%;">
				<span>系统云仓累计单量(票)</span>
				<span>99</span>
			</div>
		</div>
		<div id="echarts" style="width: 50%;height: 320px;">
		</div>
	</div>
</div>
<script type="text/javascript" src="./static/js/plugins/echarts.min.js"></script>
<script type="text/javascript" src="./static/js/jquery/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="./static/js/moudles/outbound/outbound-lookBoardData.js"></script>
<script type="text/javascript">
	let date = new Date();
var year  = date.getFullYear();
  var month  = date.getMonth()+1;
  var day   = date.getDate();
	$('.time').text(year+ '-' +month+ '-' +day)
	// 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('echarts'));

        // 指定图表的配置项和数据
        option = {
			    title : {
			        text: '',
			        subtext: '',
			        x:'center'
			    },
			    tooltip : {
			        trigger: 'item',
			        formatter: "{a} <br/>{b} : {c} ({d}%)"
			    },
			    legend: {
			        orient: 'vertical',
			        left: 'left',
			        data: ['加盟云仓累计出库单量(票)','系统云仓累计单量(票)']
			    },
			    series : [
			        {
			            name: '',
			            type: 'pie',
			            radius : '80%',
			            center: ['50%', '60%'],
			            data:[
			                {value:335, name:'加盟云仓累计出库单量(票)'},
			                {value:310, name:'系统云仓累计单量(票)'},
			            ],
			            itemStyle: {
			                emphasis: {
			                    shadowBlur: 10,
			                    shadowOffsetX: 0,
			                    shadowColor: 'rgba(0, 0, 0, 0.5)'
			                }
			            }
			        }
			    ]
			};

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
</script>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript" src="./static/js/moudles/outbound/outbound-lookBoardData.js"></script>