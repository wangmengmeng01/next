'use strict'
let xml = require('xml2js')
let promise = require('bluebird')
let ejs = require('ejs')
let request = require('request')
let path = require('path')
let fs = require('fs')

let heredoc = require('heredoc')
let WXconfig = {
    appid: 'wx4efb48893a0813db',
    appsecret: 'd8ce7e16531e2842e63b135d866d35fc',
    token: 'wangmeng',
}
let url = {
	img:'https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE'
}
let wx = {
	access_token:'',
	expires_in:'',
}
function sendImg(argument) {
	request({
	    url: `https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=${WXconfig.appid}&secret=${WXconfig.appsecret}
	`,
	    method: "POST",
	    json: true,
	    headers: {
	      "content-type": "application/json",
	    },
	    body: {

	    }
		}, function(error, response, body) {
	    if (!error && response.statusCode == 200) {
	      console.log(body,'body') // 请求成功的处理逻辑
	      fs.writeFile(__dirname+"assert.txt", JSON.stringify(body), err => {
			    if (!err) {

			    }
				})
	    }
		})
}
function getAssert() {
	request({
    url: `https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=${WXconfig.appid}&secret=${WXconfig.appsecret}
`,
    method: "GET",
    json: true,
    headers: {
      "content-type": "application/json",
    },
    body: {

    }
	}, function(error, response, body) {
    if (!error && response.statusCode == 200) {
      console.log(body,'body') // 请求成功的处理逻辑
      wx.access_token = body.access_token;
      fs.writeFile(__dirname+"assert.txt", JSON.stringify(body), err => {
		    if (!err) {
		        fs.readFile(__dirname+"assert.txt", "utf8", (err, data) => {
		            console.log(data,'data'); // Hello world
		        });
		    }
			})
    }
	})
}
function getticket() {
	let token;
let data = fs.readFileSync(__dirname+"assert.txt", "utf8");
  console.log(JSON.parse(data),'wx.access_token')

	request({
    url: `https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=${JSON.parse(data).access_token}&type=jsapi`,
    method: "GET",
    json: true,
    headers: {
      "content-type": "application/json",
    },
    body: {

    }
	}, function(error, response, body) {
    if (!error && response.statusCode == 200) {
      console.log(body,'getticket') // 请求成功的处理逻辑
      fs.writeFile(__dirname+"ticket.txt", JSON.stringify(body), err => {
		    if (!err) {
		        fs.readFile(__dirname+"ticket.txt", "utf8", (err, data) => {
		            console.log(data,'ticket'); // Hello world
		        });
		    }
			})
    }
	})
}
function parseXml(xmlData) {
	return new promise((resolve,reject) => {
		xml.parseString(xmlData,{trim: true},(err,result) => {
			if(err) reject(err)
			else {
				resolve(result)
			}
		})
	})
}
function formatMsg(result) {
	let msg = {};
	if(typeof result === 'object') {
		let keys = Object.keys(result)
		for(var i = 0;i < keys.length; i++){
			let item = result[keys[i]];
			let key = keys[i];
			if(!(item instanceof Array) || item.length === 0){
				continue;
			}
			if(item.length === 1){
				var value = item[0];

				if(typeof value === 'object'){
					msg[key] = formatMsg(value)
				}else {
					msg[key] = (value || '').trim()
				}
			}else {
				msg[key] = []
				for (var j = 0; j < item.length; j++) {
					msg[key].push(formatMsg(item[j]))
				}
			}
		}
	}
	return msg;
}
// module.exports.formatMsg = formatMsg;
let tpl = heredoc((content) => {
/*
<xml>
  <ToUserName><![CDATA[<%= FromUserName %>]]></ToUserName>
  <FromUserName><![CDATA[<%= ToUserName %>]]></FromUserName>
  <CreateTime><%= CreateTime %></CreateTime>
  <MsgType><![CDATA[<%= MsgType %>]]></MsgType>
  <% if(MsgType === 'text') { %>
  	<Content><![CDATA[<%= Content %>]]></Content>
  <% } else if(MsgType === 'image') { %>
  	<Image>
	    <MediaId><![CDATA[<%= content.MediaId %>]]></MediaId>
	  </Image>
  <% } else if(MsgType === 'voice') { %>
		<Voice>
	    <MediaId><![CDATA[<%= content.MediaId %>]]></MediaId>
	  </Voice>
  <% } else if(MsgType === 'video') { %>
		<Video>
	    <MediaId><![CDATA[<%= content.MediaId %>]]></MediaId>
	    <Title><![CDATA[<%= content.Title %>]]></Title>
	    <Description><![CDATA[<%= content.Description %>]]></Description>
	  </Video>
  <% } else if(MsgType === 'music') { %>
		<Music>
	    <Title><![CDATA[<%= content.Title %>]]></Title>
	    <Description><![CDATA[<%= content.Description %>]]></Description>
	    <MusicUrl><![CDATA[<%= content.MusicUrl %>]]></MusicUrl>
	    <HQMusicUrl><![CDATA[<%= content.HQMusicUrl %>]]></HQMusicUrl>
	    <ThumbMediaId><![CDATA[<%= content.ThumbMediaId %>]]></ThumbMediaId>
	  </Music>
  <% } else if(MsgType === 'news') { %>
		<ArticleCount><%= content.length %></ArticleCount>
	  <Articles>
	  <% content.forEach((item) => { %>
	    <item>
	      <Title><![CDATA[<%= item.Title %>]]></Title>
	      <Description><![ CDATA[<%=item.Description %>]]></Description>
	      <PicUrl><![CDATA[<%= item.PicUrl %>]]></PicUrl>
	      <Url><![CDATA[<%= item.Url %>]]></Url>
	    </item>
	  <% }) %>
	  </Articles>
  <% } %>
</xml>
*/
})

let compiled = ejs.compile(tpl)
exports = module.exports = {
	getAssert,
	getticket,
	parseXml,
	formatMsg,
	compiled,
}