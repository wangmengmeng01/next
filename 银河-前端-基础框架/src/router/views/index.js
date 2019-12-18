import Layout from "@/page/index/";

export default [
//默认打开页
  {
    path: "/personnel",
    component: Layout,
    redirect: "/personnel/index",
    children: [{
      path: "index",
      name: "人事看板",
      meta: {
        i18n: "dashboard"
      },
      component: () =>
        import( /* webpackChunkName: "views" */ "@/views/personnel")
    }]
  }
];
