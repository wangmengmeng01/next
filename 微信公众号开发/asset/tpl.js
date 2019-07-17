let ejs = require('./utils.js')
let tpl = function (content,message) {
	let type = 'text'
	let info = {}
	if(Array.isArray(content)){
		type = 'news'
	}
	info.MsgType = message.MsgType;
	info.ToUserName = message.ToUserName;
	info.FromUserName = message.FromUserName;
	info.Content = content;
	info.type = message.type || 'text';
	info.CreateTime = new Date().getTime();
	return ejs.compiled(info)
}
module.exports = {
	tpl,
}