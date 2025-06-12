import DashboardLayout from '@/layouts/dashboard/DashboardLayout.vue';

import HomeView from '@/views/HomeView.vue';
import ApartmentMap from '@/components/map/ApartmentMap.vue';
import ReviewPage from '@/views/ReviewPage.vue';
import PricePredictPage from '@/views/PricePredictPage.vue';
import IssuePage from '@/views/IssuePage.vue';
import ReviewWrite from '@/views/review/ReviewWrite.vue';  // ReviewWrite 컴포넌트를 임포트
import ReviewFix from '@/views/review/ReviewFix.vue';  // ReviewFix 컴포넌트를 임포트
import ApartReview from '@/views/review/ApartReview.vue';  // ApartReview 컴포넌트를 임포트
import IssueWritePage from '@/views/issue/IssueWritePage.vue';  // IssueWritePage 컴포넌트를 임포트
import IssueDetailPage from '@/views/issue/IssueDetailPage.vue';  // IssueDetailPage 컴포넌트를 임포트


const MainRoutes = {
  path: '/',
  component: DashboardLayout,
  children: [
    {
      path: '',
      name: 'Home',
      component: HomeView,
      meta: { requiresAuth: false } // 홈은 예외
    },
    {
      path: 'map',
      name: 'ApartmentMap',
      component: ApartmentMap,
      meta: { requiresAuth: false }
    },
    {
      path: 'review',
      name: 'Review',
      component: ReviewPage,
      meta: { requiresAuth: true }
    },
    {
      path: 'predict',
      name: 'PricePredict',
      component: PricePredictPage,
      meta: { requiresAuth: true }
    },
    {
      path: 'issues',
      name: 'IssuePage',
      component: IssuePage,
      meta: { requiresAuth: true }
    },
    {
      path: '/issues/write',
      name: 'IssueWritePage',
      component: IssueWritePage,
      meta: { requiresAuth: true }
    },
    {
      path: '/issues/detail/:issueId',
      name: 'IssueDetailPage',
      component: IssueDetailPage,
      meta: { requiresAuth: true }
    },
    {
      path: '/review/write',  // ReviewWrite 페이지로 가는 경로 설정
      name: 'ReviewWrite',
      component: ReviewWrite,  // ReviewWrite 컴포넌트와 연결
      meta: { requiresAuth: true }
    },
    {
      path: '/review/fix/:apartId',
      name: 'ReviewFix',
      component: ReviewFix,
      meta: { requiresAuth: true },
    },
    {
      path: '/review/apart/:apartId',
      name: 'ApartReview',
      component: ApartReview,
      meta: { requiresAuth: true },
    },
  ]
};

export default MainRoutes;
