<script setup lang="ts">
import { computed } from 'vue';
import { useTheme } from 'vuetify';
import UiTitleCard from '@/components/shared/UiTitleCard.vue';

const theme = useTheme();
const InfoColor = theme.current.value.colors.info;

// chart 1: 월별 거래량 데이터로 교체
const barChart1 = {
  series: [
    {
      name: '월별 거래량',
      data: [39362, 42914, 36399, 34355, 29784, 39925, 53460] // 예시값
    }
  ]
};

const chartOptions1 = computed(() => {
  return {
    chart: {
      type: 'bar',
      height: 100,
      fontFamily: `inherit`,
      foreColor: '#a1aab2',
      toolbar: {
        show: false
      }
    },
    dataLabels: {
      enabled: false
    },
    plotOptions: {
      bar: {
        columnWidth: '45%',
        borderRadius: 4
      }
    },
    labels: ['9월', '10월', '11월', '12월', '1월', '2월', '3월'],
    colors: [InfoColor],
    stroke: {
      curve: 'smooth'
    },
    fill: {
      opacity: 0.6
    },
    xaxis: {
      axisBorder: {
        show: false
      },
      axisTicks: {
        show: false
      }
    },
    yaxis: {
      show: false
    },
    grid: {
      show: false
    },
    tooltip: {
      fixed: {
        enabled: false
      }
    }
  };
});

</script>

<template>
  <UiTitleCard title="아파트 거래량" class-name="pt-5 px-0 rounded-md overflow-hidden">
    <div class="px-5">
      <h6 class="text-h6 text-lightText mb-4">2025년</h6>
      <h3 class="text-h3 mb-0">총 123,169건</h3>
    </div>
    <apexchart type="bar" height="365" :options="chartOptions1" :series="barChart1.series"> </apexchart>
  </UiTitleCard>
</template>
