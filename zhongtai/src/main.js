import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import http from './assets/js/http.js'
import elementUI from 'element-ui'
import $ from 'jquery'
import 'element-ui/lib/theme-chalk/index.css';
Vue.use(elementUI)
Vue.config.productionTip = false
window.$ = $;
Vue.prototype.$http = http;
router.beforeEach((to,from,next) => {
  eventBus.$emit('loading',true);
  next();
})
router.afterEach((to,from) => {
  eventBus.$emit('loading',false);
})
router.onError(() => {
  eventBus.$emit('loading',false);
})
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
