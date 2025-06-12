<script setup lang="ts">
import { shallowRef } from 'vue';

// icons
import { RiseOutlined, FallOutlined } from '@ant-design/icons-vue';

const fivecards = shallowRef([
  {
    name: '가장 많이 상승한 아파트',
    earn: '래미안 강남힐스 (+4.2%)',
    percent: '4.2%',
    color: 'primary',
    icon: RiseOutlined,
    text: '금월 최고 상승률'
  },
  {
    name: '이번 달 총 매매량',
    earn: '53,460건',
    percent: '33.5%',
    color: 'success',
    icon: RiseOutlined,
    text: '전월 대비 +13,535건'
  },
  {
    name: '이번 달 부동산 이슈',
    earn: '22건',
    percent: '12.0%',
    color: 'warning',
    icon: RiseOutlined,
    text: '이슈 증가'
  },
  {
    name: '가장 많이 하락한 아파트',
    earn: '수성자이 (-3.1%)',
    percent: '3.1%',
    color: 'error',
    icon: FallOutlined,
    text: '금월 최저 변동률'
  }
]);
</script>

<template>
  <v-row class="my-0">
    <v-col cols="12" sm="6" md="3" v-for="(card5, i) in fivecards" :key="i" :value="card5">
      <v-card elevation="0">
        <v-card variant="outlined">
          <v-card-text>
            <div class="d-flex align-items-center justify-space-between">
              <div>
                <h6 class="text-h6 text-lightText mb-1">{{ card5.name }}</h6>
                <h4 class="text-h4 d-flex align-center mb-0">
                  {{ card5.earn }}
                  <v-chip :color="card5.color" :border="`${card5.color} solid thin opacity-50`" class="ml-2"
                    size="small" label>
                    <template v-slot:prepend>
                      <component :is="card5.icon" :style="{ fontSize: '12px' }" :class="'mr-1 text-' + card5.color" />
                    </template>
                    {{ card5.percent }}
                  </v-chip>
                </h4>
                <span class="text-lightText text-caption pt-5 d-block">
                  <span :class="'text-' + card5.color">{{ card5.text }}</span>
                </span>
              </div>
            </div>
          </v-card-text>
        </v-card>
      </v-card>
    </v-col>
  </v-row>
</template>
