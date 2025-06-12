<template>
  <div>
    <h3 class="text-h6 mb-4">리뷰 목록</h3>

    <div v-if="!isLoggedIn">
      <v-alert type="warning" icon="mdi-login">
        리뷰를 보려면 로그인이 필요합니다.
      </v-alert>
    </div>

    <div v-else-if="isLoggedIn && !isVerified">
      <v-alert type="info" icon="mdi-shield-account">
        상단 1개의 리뷰만 볼 수 있습니다. 전체 리뷰 열람을 위해 실거주 인증이 필요합니다.
      </v-alert>
      <ReviewCard :review="reviews[0]" />
    </div>

    <div v-else>
      <v-row>
        <v-col cols="12" sm="6" md="4" v-for="review in reviews" :key="review.id">
          <ReviewCard :review="review" />
        </v-col>
      </v-row>
    </div>
  </div>
</template>

<script setup>
import ReviewCard from './ReviewCard.vue';

// 샘플 props 또는 추후 store/API 연동
const isLoggedIn = true;     // TODO: 로그인 상태 확인
const isVerified = false;    // TODO: 실거주 인증 여부 확인

const reviews = [
  { id: 1, aptName: '래미안 강남힐스', rate: 4, summary: '교통 최고, 층간소음 약간 있음' },
  { id: 2, aptName: '자이 반포', rate: 5, summary: '모든 게 완벽한 단지입니다.' },
  { id: 3, aptName: '대전 둔산 힐스', rate: 3, summary: '가격대비 만족도 좋음.' }
];
</script>
