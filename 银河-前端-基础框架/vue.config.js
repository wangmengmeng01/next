
module.exports = {
  // publicPath 配置可根据具体项目配置即可
  publicPath: process.env.NODE_ENV === "production" ? "/personel-center" : "/",
  outputDir: process.env.outputDir,
  assetsDir: 'static',
  lintOnSave: true,
  productionSourceMap: false,
  chainWebpack: config => {
    //忽略的打包文件
    config.externals({
      vue: "Vue",
      "vue-router": "VueRouter",
      vuex: "Vuex",
      axios: "axios",
      "element-ui": "ELEMENT"
    });
    config.resolve.symlinks(true)
    const entry = config.entry("app");
    entry.add("babel-polyfill").end();
    entry.add("classlist-polyfill").end();
  },
  devServer: {
    port: 1889,
    proxy: {
      "/api": {
        //本地服务接口地址
        target: 'http://10.19.160.197:8068',
        ws: true,
        pathRewrite: {
          "^/api": "/"
        }
      }
    }
  }
};
