import Vue from 'vue'
import SvgIcon from '@/components/svgIcon'// svg组件

// 注册到全局
Vue.component('svg-icon', SvgIcon)

const requireAll = requireContext => requireContext.keys().map(requireContext)
// eslint-disable-next-line
const req = require.context('./svg', false, /\.svg$/)
console.log(req.keys(),'444444')
console.log(req.keys().map(req),'444444')
requireAll(req)
console.log(requireAll(req),'444444')