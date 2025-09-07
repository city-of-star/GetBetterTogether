import { createRouter, createWebHistory } from 'vue-router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

NProgress.configure({
  showSpinner: false,  // 隐藏加载小圆圈
  easing: 'ease',
  speed: 500
})

const routes = [
  {
    path: '/',
    name: 'home',
    component: () => import('@/views/HomeView.vue'),
    redirect: '/calendar', // 默认重定向到日历页
    children: [
      {
        path: '/calendar',
        name: 'calendar',
        component: () => import('@/views/home/CalendarView.vue')
      },
      {
        path: '/stats',
        name: 'stats',
        component: () => import('@/views/home/Stats.vue')
      },
      {
        path: '/goals',
        name: 'goals',
        component: () => import('@/views/home/Goals.vue')
      },
      {
        path: '/basic-info',
        name: 'basic-info',
        component: () => import('@/views/home/BasicInfo.vue')
      }
    ]
  },
  {
    path: '/404',
    name: '404',
    component: () => import('@/views/404.vue')
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(async () => {
  NProgress.start() // 开始加载进度条
})

router.afterEach(() => {
  NProgress.done()  // 结束加载进度条
})

router.onError((error) => {
  NProgress.done()  // 出现错误时也结束进度条
  console.error('路由错误:', error)
})

export default router