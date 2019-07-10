import Vue from 'vue'
import Router from 'vue-router'
const home = () => import('./views/Home.vue');
const login = () => import('./views/login.vue');
const user = () => import('./views/user.vue');
const  business = () => import('./views/business.vue');
const  role = () => import('./views/role.vue');
const  authority = () => import('./views/authority.vue');
const  app = () => import('./views/app.vue');
Vue.use(Router)

export default new Router({
  mode: 'hash',
  routes: [
    {
      path: '/',
      redirect:'/home/business',
    },
    {
      path: '/home',
      name: 'home',
      component: home,
			children: [
        {
          path: 'user',
          component: user
        },
				{
          path: 'app',
          component: app
        }, 
				{
          path: 'authority',
          component: authority
        }, 
				{
          path: 'role',
          component: role
        },
				{
          path: 'business',
          component:  business
        },
      ]
    },
    {
      path: '/login',
      name: 'login',
      component:login
    },
    {
      path: '*',
      redirect:'/home',
    },
  ]
})
