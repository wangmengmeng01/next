import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/about',
    name: 'about',
    component: () => import('../views/about.vue'),
    // redirect: '/about/s',
    meta: { title: 'a', icon: 'keep' },
    children: [
      {
        path: 's',
        name: 's',
        meta: { title: 'e', icon: 'keep' },
        component: () => import('../views/Home.vue'),
        children: [
          {
            path: 'ff',
            name: 'ff',
            meta: { title: '恶', icon: 'keep' },
            component: () => import('../views/Home.vue')
          }
        ]
      },
      {
        path: '4041',
        name: '4041',
        // hidden: true,
        meta: { title: 'b', icon: 'keep' },
        component: () => import('../views/404.vue')
      },
    ]
  },
  {
    path: '/404',
    name: '404',
    hidden: true,
    meta: { title: 'b', icon: 'keep' },
    component: () => import('../views/404.vue')
  },
  {
    path: '/aa',
    name: 'aa',
    meta: { title: 'c',icon: 'keep' },
    component: () => import('../views/about.vue')
  }
]

const router = new VueRouter({
  mode: 'hash',
  base: process.env.BASE_URL,
  routes
})

export default router
