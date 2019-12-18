/**
 * 全站权限配置
 *
 */
import router from './router/router'
import store from './store'
import { validatenull } from '@/util/validate'
import { getToken } from '@/util/auth'
import { baseUrl } from '@/config/env';
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
NProgress.configure({ showSpinner: false });
const lockPage = store.getters.website.lockPage; //锁屏页
router.beforeEach((to, from, next) => {
  //缓冲设置
  if (store.state.common.meta) {
    to.meta.$keepAlive = true;
  } else {
    to.meta.$keepAlive = false;
  }
  const meta = to.meta || {};
  if (getToken()) {
    if (store.getters.isLock && to.path != lockPage) { //如果系统激活锁屏，全部跳转到锁屏页
      next({ path: lockPage })
    }
    else if (to.path === '/login') { //如果登录成功访问登录页跳转到主页
      next({ path: '/personnel/index' })
    }
    else {
      // const value = to.query.src || to.fullPath;
      const value = to.query.src || to.path;
      const label = to.query.name || to.name;
      const meta = to.meta || router.$avueRouter.meta || {};
      const i18n = to.query.i18n;
      if (meta.isTab !== false && !validatenull(value) && !validatenull(label)) {
        store.commit('ADD_TAG', {
          label: label,
          value: value,
          params: to.params,
          query: to.query,
          meta: (() => {
            if (!i18n) {
              return meta
            }
            return {
              i18n: i18n
            }
          })(),
          group: router.$avueRouter.group || []
        });
      }
      next()
    }
  } else {
    //判断是否需要认证，没有登录访问去登录页
    if (meta.isAuth === false) {
      next()
    } else {
      if (baseUrl) {
        window.location.href = `${baseUrl}/galaxy-login/index.html#/login`
      } else {
		  //开发环境时 跳转到本地
        window.location.href = 'http://10.18.61.39:1888/#/login';
      }
    }
  }
})

router.afterEach(() => {
  NProgress.done();
});
