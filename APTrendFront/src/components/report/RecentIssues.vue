<script setup lang="ts">
import { shallowRef } from 'vue';
import UiTitleCard from '@/components/shared/UiTitleCard.vue';
import { FileTextOutlined, FileSearchOutlined, FileProtectOutlined } from '@ant-design/icons-vue';

const reports = shallowRef([
  {
    icon: FileTextOutlined,
    subtitle: '정책 / 2024.06.01',
    title: '청약 제도 개편 발표',
    color: 'primary',
    price: '조회수 1,320회',
    pricepercent: '★ 4.5'
  },
  {
    icon: FileSearchOutlined,
    subtitle: '시장분석 / 2024.05.30',
    title: '서울 아파트 매매지수 반등',
    color: 'success',
    price: '조회수 980회',
    pricepercent: '★ 4.2'
  },
  {
    icon: FileProtectOutlined,
    subtitle: '정책 / 2024.05.28',
    title: '실거주 요건 강화 관련 논의',
    color: 'warning',
    price: '조회수 712회',
    pricepercent: '★ 4.0'
  }
]);

</script>
<template>
  <UiTitleCard title="최신 리포트 요약" class-name="px-0 pb-0 rounded-md overflow-hidden">
  <v-list lines="two" class="py-0" aria-busy="true" aria-label="Report list">
    <v-list-item v-for="report in reports" :key="report.title" :value="report.title" border>
      <template v-slot:prepend>
        <v-avatar size="36" :color="report.color" :class="`text-${report.color}`" variant="tonal">
          <component :is="report.icon" :style="{ fontSize: '16px' }" />
        </v-avatar>
      </template>
      <div class="d-flex align-center justify-space-between w-100 ga-2">
        <div>
          <h6 class="text-subtitle-1 mb-0">{{ report.title }}</h6>
          <span class="text-subtitle-2 text-lightText font-weight-regular">{{ report.subtitle }}</span>
        </div>
        <div class="text-right">
          <h6 class="text-subtitle-1 mb-0">{{ report.price }}</h6>
          <span class="text-h6 text-lightText">{{ report.pricepercent }}</span>
        </div>
      </div>
    </v-list-item>
  </v-list>
</UiTitleCard>

</template>
