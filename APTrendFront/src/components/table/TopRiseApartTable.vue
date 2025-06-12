<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';
import UiTitleCard from '@/components/shared/UiTitleCard.vue';

interface HotApt {
  region: string;
  name: string;
  order: number;
  amount: string;
  rate: number;
}

const projects = ref<HotApt[]>([]);

onMounted(async () => {
  try {
    const response = await axios.get(`${import.meta.env.VITE_APP_DEVELOP_BACKEND_URL}/api/apartments/hot`);
    projects.value = response.data;
  } catch (error) {
    console.error('🔥 급상승 아파트 데이터를 불러오는 데 실패했습니다:', error);
  }
});
</script>


<template>
  <UiTitleCard title="급상승 아파트" class-name="px-0 pb-0 rounded-md">
    <v-table class="bordered-table" hover density="comfortable">
      <thead class="bg-containerBg">
        <tr>
          <th class="text-left text-caption font-weight-bold text-uppercase">지역</th>
          <th class="text-left text-caption font-weight-bold text-uppercase">아파트 이름</th>
          <th class="text-right text-caption font-weight-bold text-uppercase" style="min-width: 100px">거래 수</th>
          <th class="text-left text-caption font-weight-bold text-uppercase"> 비율</th>
          <th class="text-right text-caption font-weight-bold text-uppercase">거래 가격</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in projects" :key="item.name">
          <td class="py-3">
            <router-link to="/dashboard/default" class="text-secondary link-hover">{{ item.region }}</router-link>
          </td>
          <td class="py-3">{{ item.name }}</td>
          <td class="py-3 text-right" style="min-width: 100px">{{ item.order }}</td>
          <td class="py-3">
            <v-chip variant="text" size="small" class="px-0" v-if="item.rate > 0">
              <v-avatar size="8" color="error" variant="flat" class="mr-2"></v-avatar>
              <p class="text-h6 mb-0">상승 ({{ item.rate }}%)</p>
            </v-chip>
            <v-chip variant="text" size="small" class="px-0" v-else-if="item.rate < 0">
              <v-avatar size="8" color="info" variant="flat" class="mr-2"></v-avatar>
              <p class="text-h6 mb-0">하락 ({{ item.rate }}%)</p>
            </v-chip>
            <v-chip variant="text" size="small" class="px-0" v-else>
              <v-avatar size="8" color="warning" variant="flat" class="mr-2"></v-avatar>
              <p class="text-h6 mb-0">보합 (0%)</p>
            </v-chip>
          </td>
          <td class="py-3 text-right" style="min-width: 100px">{{ item.amount }}</td>
        </tr>
      </tbody>

    </v-table>
  </UiTitleCard>
</template>
