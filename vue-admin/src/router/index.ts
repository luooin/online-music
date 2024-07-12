import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import store from "@/store"
const routes: Array<RouteRecordRaw> = [
  {
    path: '/Home',
    component: () => import('@/views/Home.vue'),
    meta: { title: '自述文件' },
    children: [
      {
        path: '/Info',
        component: () => import('@/views/InfoPage.vue'),
        meta: { title: 'Info',requireAuth: true  }
      },
      {
        path: '/song',
        component: () => import('@/views/SongPage.vue'),
        meta: { title: 'Song',requireAuth: true  }
      },
      {
        path: '/singer',
        component: () => import('@/views/SingerPage.vue'),
        meta: { title: 'Singer',requireAuth: true  }
      },
      {
        path: '/SongList',
        component: () => import('@/views/SongListPage.vue'),
        meta: { title: 'SongList',requireAuth: true   }
      },
      {
        path: '/ListSong',
        component: () => import('@/views/ListSongPage.vue'),
        meta: { title: 'ListSong',requireAuth: true   }
      },
      {
        path: '/Comment',
        component: () => import('@/views/CommentPage.vue'),
        meta: { title: 'Comment',requireAuth: true  }
      },
      {
        path: '/Consumer',
        component: () => import('@/views/ConsumerPage.vue'),
        meta: { title: 'Consumer',requireAuth: true   }
      },
      {
        path: '/Collect',
        component: () => import('@/views/CollectPage.vue'),
        meta: { title: 'Collect',requireAuth: true   }
      }
    ]
  },
  {
    path: '/',
    component: () => import('@/views/Login.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})


router.beforeEach((to, from, next) => {
  const isLoggedIn =store.getters.token;
  console.log('3123132143rt45ghtbn'+isLoggedIn)
  if (to.meta.requireAuth && !isLoggedIn) {
    next({ name: "" });  // 重定向页面
  } else {
    next();
  }
})


export default router
