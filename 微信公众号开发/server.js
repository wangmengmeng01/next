var http = require("http");
// var app = http()

// respond with "hello world" when a GET request is made to the homepage
let WXconfig = {
    appid: 'wx4efb48893a0813db',
    appsecret: 'd8ce7e16531e2842e63b135d866d35fc',
    token: 'wangmeng',
}
http.createServer(function(request,response){
    // res.writeHead(200,{"Content-Type":"application/json"});
    // res.writeHead(200,{"Content-Type":"text/plain"});
    // res.write("hello my first demo");
    // res.write(request);
    // res.json("{token:'wangmeng'}");
    // res.end();
    response.get('/', function (req, res) {
        res.send('wangmeng')
      })
    console.log(request.url,'req------------------')
    // console.log(res,'res')
}).listen("8070");
console.log("server start");

// signature=688b27f3059c3ba2a26cf7f2451783dad97f7bb7&echostr=4322852707758758199&timestamp=1548226854&nonce=589239839