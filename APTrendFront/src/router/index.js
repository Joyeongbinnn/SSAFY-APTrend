import { createRouter, createWebHistory } from 'vue-router';
import MainRoutes from './MainRoutes';
import PublicRoutes from './PublicRoutes';
import { useAuthStore } from '@/stores/auth';
import { useUIStore } from '@/stores/ui';

export const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/:pathMatch(.*)*',
      component: () => import('@/views/pages/maintenance/error/Error404Page.vue')
    },
    MainRoutes,
    PublicRoutes
  ]
});

router.beforeEach(async (to, from, next) => {
  const uiStore = useUIStore();
  uiStore.isLoading = true;

  const publicPages = ['/login', '/'];
  const auth = useAuthStore();
  const isPublicPage = publicPages.includes(to.path);
  const authRequired = !isPublicPage && to.matched.some((record) => record.meta.requiresAuth);

  // ✅ 로그인 여부 확인을 위해 서버에서 사용자 정보 가져오기
  if (!auth.user) {
    console.log('auth.user 없음, 사용자 정보 가져오기');
    await auth.fetchUser(); // /api/auth/me 요청
  }

  if (authRequired && !auth.user) {
    auth.returnUrl = to.fullPath;
    next('/login');
  } else {
    next();
  }
});


router.afterEach(() => {
  const uiStore = useUIStore();
  uiStore.isLoading = false;
});
