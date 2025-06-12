<script setup lang="ts">
import { computed, shallowRef } from 'vue';
import UiTitleCard from '@/components/shared/UiTitleCard.vue';

const chartOptions1 = computed(() => {
  return {
    chart: {
      type: 'line',
      height: 340,
      fontFamily: `inherit`,
      foreColor: '#a1aab2',
      toolbar: {
        show: false
      }
    },
    colors: ['rgba(var(--v-theme-warning), var(--v-medium-opacity))'],
    dataLabels: {
      enabled: false
    },
    labels: ['8월', '9월', '10월', '11월', '12월', '1월', '2월', '3월', '4월'],
    xaxis: {
      crosshairs: {
        width: 1
      },
      labels: {
        offsetX: 8
      },
      axisTicks: {
        show: false
      },
      axisBorder: {
        show: false
      }
    },
    yaxis: {
      show: false
    },
    stroke: {
      curve: 'smooth',
      width: 1.5
    },
    grid: {
      strokeDashArray: 4,
      borderColor: 'rgba(var(--v-theme-borderLight), var(--v-high-opacity))'
    },
    tooltip: {
      fixed: {
        enabled: false
      },
      x: {
        show: false
      },
      marker: {
        show: false
      }
    }
  };
});

const lineChart1 = {
  series: [
    {
      name: '서울특별시 강남구 평균 아파트 가격',
      data: [22.77, 23.20, 23.54, 23.84, 24.04, 24.13, 24.15, 24.42, 25.27, 25.26] // 단위: 억원
    }
  ]
};


const reports = shallowRef([
  {
    name: '최고가 지역',
    percent: '서울특별시 강남구'
  },
  {
    name: '최저가 지역',
    percent: '강원특별자치도 태백시'
  },
  {
    name: '전국평균',
    percent: ''
  }
]);

</script>

<template>
  <UiTitleCard title="Analytics Report" class-name="px-0 rounded-md overflow-hidden">
    <v-list class="py-0" aria-busy="true" aria-label="Report list" border>
      <v-list-item :value="item.name" v-for="(item, i) in reports" :key="i">
        <div class="d-inline-flex align-center justify-space-between w-100 ga-2">
          <h6 class="text-h6 mb-0">{{ item.name }}</h6>
          <h5 class="ml-auto text-h5 mb-0">{{ item.percent }}</h5>
        </div>
      </v-list-item>
    </v-list>
    <v-divider>최고가지역 가격변화</v-divider>
    <apexchart type="line" height="340" :options="chartOptions1" :series="lineChart1.series"> </apexchart>
  </UiTitleCard>
</template>
