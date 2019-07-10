let path = require('path');
module.exports = {
  lintOnSave: true,
  publicPath:'./',
  outputDir:'dist',
  assetsDir:'static',
  chainWebpack:config => {
  },
  configureWebpack: {
    resolve: {
      alias: {
        wang: path.resolve(__dirname, 'src/')
      }
    },
    module: {

    }
  },
  devServer: {
    open: true,
    // host: '10.20.26.26',
    host: '10.18.61.66',
    port: 8077,
    https: false,
    hotOnly: false,
    proxy: {
      '/api': {
        target: 'http://localhost:80/openapi',
				changeOrigin: true,
        pathRewrite: {
          '^/api': ''    //代理的路径
        }
      },

    }
  },
}
