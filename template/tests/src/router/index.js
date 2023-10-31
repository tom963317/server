import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/handler',
    component: () => import('@/views/phone/PhoneHandler'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: '控制台',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '控制台', icon: 'dashboard' }
    }]
  },
  {
    path: '/rawfish',
    component: Layout,
    name: '生鱼',
    meta: { title: '生鱼', icon: 'el-icon-s-help' },
    children: [
      {
        path: '/rawfish',
        name: 'Rawfish',
        component: () => import('@/views/card/rawfish/index.vue'),
        meta: { title: '生鱼', icon: 'table' }
      },
    ]
  },
  {
    path: '/example',
    component: Layout,
    redirect: '/example/fish',
    name: '系统管理',
    meta: { title: '系统管理', icon: 'el-icon-s-help' },
    children: [
      {
        path: 'user',
        name: 'userInfo',
        component: () => import('@/views/card/user/index.vue'),
        meta: { title: '用户管理', icon: 'table' }
      },
      {
        path: 'fish',
        name: 'Fish',
        component: () => import('@/views/card/fish/index.vue'),
        meta: { title: '熟鱼', icon: 'table' }
      },

      {
        path: 'cardInfo',
        name: 'cardInfo',
        component: () => import('@/views/card/card/index.vue'),
        meta: { title: '卡管理', icon: 'table' }
      }
    ]
  },

  {
    path: '/white',
    component: Layout,
    children: [
      {
        path: 'white',
        name: '白名单',
        component: () => import('@/views/white/White'),
        meta: { title: '白名单设置', icon: 'form' }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
