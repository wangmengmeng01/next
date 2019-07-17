const koa = require('koa')
const sha1 = require('sha1')
const ejs = require('ejs')
const fs = require('fs')
const path = require('path')
const heredoc = require('heredoc')
const crypto = require('crypto')
const wx = require('./asset/utils.js')
const fun = require('./asset/tpl.js')
const getRawBody = require('raw-body')
let WXconfig = {
    appid: 'wx4efb48893a0813db',
    appsecret: 'd8ce7e16531e2842e63b135d866d35fc',
    token: 'wangmeng',
}
// wx.getAssert()
// wx.getticket()

let tpl = heredoc(() => {
  /*
    <!DOCTYPE html>
    <html lang="en">
      <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width,initial-scale=1.0">
        <title>韵达科技</title>
        <style type="text/css">
          body {
            min-width: 800px;
          }
        </style>
        <script type="text/javascript" src="http://res2.wx.qq.com/open/js/jweixin-1.4.0.js">
        </script>
        <script type="text/javascript" src="https://zeptojs.com/zepto.min.js">
        </script>
      </head>
      <body>
        <div>哈哈</div>
        <script type="text/javascript">
          wx.config({
            debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
            appId: 'wx4efb48893a0813db', // 必填，公众号的唯一标识
            timestamp: '<%= timeStamp %>',//<%= timeStamp %>当‘=’是‘-’可以拼接字符串 像插入a标签 // 必填，生成签名的时间戳
            nonceStr: '<%= nonceStr %>', // 必填，生成签名的随机串
            signature: '<%= signature %>',// 必填，签名
            jsApiList: ['chooseImage'] // 必填，需要使用的JS接口列表
          });
          wx.checkJsApi({
              jsApiList: ['chooseImage'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
              success: function(res) {
              // 以键值对的形式返回，可用的api值true，不可用为false
              // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
                console.log(res)
              }
          });
          wx.ready(function () {   //需在用户可能点击分享按钮前就先调用
            wx.chooseImage({
            count: 1, // 默认9
            sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
            sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
            success: function (res) {
              alert('反反复复')
            var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
            }
            });
          });
        </script>
      </body>
    </html>

  */
})
let createNonce = () => {
  return Math.random().toString(36).substr(2,15);
}
let createTimestamp = () => {
  return parseInt(new Date().getTime()/1000, 10) + ''
}
function _sign(nonceStr,timeStamp,ticket, url) {
  let params = [
    `noncestr=${nonceStr}`,
    `timestamp=${timeStamp}`,
    `jsapi_ticket=${ticket}`,
    `url=${url}`,
  ]
  let str = params.sort().join('&')
  let shasum = crypto.createHash('sha1')
  shasum.update(str)
  // return shasum.digest("hex")
  return sha1(str)
}
function sign(ticket, url) {
  let nonceStr = createNonce()
  let timeStamp = createTimestamp()
  let signature = _sign(nonceStr,timeStamp,ticket, url)
  return {
    nonceStr,
    timeStamp,
    signature,
  }
}
let app = new koa();
app.use(function *(next) {
  if(this.url.indexOf('/wang') > -1)
  {
    // C:\Users\Administrator\Desktop\node-server\assetticket.txt
    let p = path.join(__dirname,'/assetticket.txt')
    // let data = fs.readFileSync(__dirname+"\\ssetticket.txt", "utf8");
    // let data = fs.readFileSync("C:\\Users\\Administrator\\Desktop\\node-serverssetticket.txt", "utf8");
    let data = fs.readFileSync(p, "utf8");
    let url = this.href;//注意URL不能带端口号，否则生成无效签名，真实的域名是不带端口号的
    console.log(data,'data')
    console.log(url,'url')
    let params = sign(JSON.parse(data).ticket,url)
    console.log(params,'ppppppppppp')
    this.body = ejs.render(tpl,params)
    return false
  }
  let token = WXconfig.token;
  // { signature: 'b1e6714bc0f756c8398d5ef0d867c84eaf81630d',
  // echostr: '8535438210835675119',
  // timestamp: '1562826057',
  // nonce: '169236428' }
  let signature = this.query.signature;
  let timestamp = this.query.timestamp;
  let nonce = this.query.nonce;
  let echostr = this.query.echostr;
  let str = [token,timestamp,nonce].sort().join('');
  let sha = sha1(str);
  if(this.method === 'GET') {
    if(sha === signature) {
      this.body = echostr;
    }else {
      this.body = 'wrong';
    }
  }else if(this.method === 'POST') {
    if(sha !== signature) {
      this.body = 'wrong';
      return false
    }
    let data = yield getRawBody(this.req,{
      length: this.length,
      limit: 'lmb',
      // encoding: true,
      encoding: this.charset,
    })
    let content = yield wx.parseXml(data);
    let message = wx.formatMsg(content.xml)
    console.log(message)
    if(message.MsgType === 'text'){
      this.status = 200;
      this.type = 'application/xml';
      let data = fun.tpl('你好！',message)
      // let data = wx.compiled(message)
      console.log(data,'eeeeeeeeeeeeee')
      this.body = data;
    }
  }
}).listen(80,() => {
    console.log('start serve');

})
