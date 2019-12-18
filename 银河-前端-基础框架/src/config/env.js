// 配置编译环境和线上环境之间的切换

let baseUrl = '';
let iconfontVersion = ['567566_pwc3oottzol', '1066523_6bvkeuqao36', '1557772_z0sasnvb6c'];
let iconfontUrl = `//at.alicdn.com/t/font_$key.css`;
let codeUrl = `${baseUrl}/code`
const env = process.env
if (env.VUE_APP_ENVIRONMENT == 'development') {
  baseUrl = `http://dev.galaxy.yundasys.com:1888`; // 开发环境地址
} else if (env.VUE_APP_ENVIRONMENT == 'production') {
  baseUrl = `https://yinhe.yundasys.com`; //生产环境地址
} else if (env.VUE_APP_ENVIRONMENT == 'test') {
  baseUrl = `http://uat.galaxy.yundasys.com:1888`; //测试环境地址
}
export {
  baseUrl,
  iconfontUrl,
  iconfontVersion,
  codeUrl,
  env
}
