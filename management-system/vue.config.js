let path = require('path');
module.exports = {
  lintOnSave: true,
  publicPath:'./',
  outputDir:'dist',
  assetsDir:'static',
  chainWebpack:config => {
    const svgRule = config.module.rule('svg')
    // 清除已有的所有 loader。
    // 如果你不这样做，接下来的 loader 会附加在该规则现有的 loader 之后。
    svgRule.uses.clear()
    svgRule
      .test(/\.svg$/)
      .include.add(path.resolve(__dirname, './src/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
    const fileRule = config.module.rule('file')
    fileRule.uses.clear()
    fileRule
      .test(/\.svg$/)
      .exclude.add(path.resolve(__dirname, './src/icons'))
      .end()
      .use('file-loader')
      .loader('file-loader')
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
