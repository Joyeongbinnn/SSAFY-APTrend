<script setup lang="ts">
import { computed, ref, onMounted } from 'vue';
import { useTheme } from 'vuetify';

const theme = useTheme();

const Color1 = theme.current.value.colors.darkprimary;
const Color2 = theme.current.value.colors.primary;
const Color3 = theme.current.value.colors.success;
const Color4 = theme.current.value.colors.secondary;

// 차트 데이터 타입 지정
interface SeriesData {
  name: string;
  data: number[];
}

// 타입을 명시적으로 줌
const areaChart1 = ref<{ series: SeriesData[] }>({ series: [] });
const areaChart2 = ref<{ series: SeriesData[] }>({ series: [] });

const chartLabels = ref<string[]>([]);

const chartOptions1 = computed(() => {
  return {
    chart: {
      type: 'area',
      height: 450,
      fontFamily: `inherit`,
      foreColor: '#a1aab2',
      toolbar: false
    },
    colors: [Color1, Color2, Color3, Color4],
    dataLabels: { enabled: false },
    stroke: { curve: 'smooth', width: 2 },
    fill: {
      type: 'gradient',
      gradient: { shadeIntensity: 1, opacityFrom: 0.7, opacityTo: 0.4, stops: [0, 100] }
    },
    grid: { borderColor: 'rgba(var(--v-theme-borderLight), var(--v-high-opacity))' },
    xaxis: {
      categories: chartLabels.value,
      labels: {
        rotate: 0,
        formatter: (val: string): string =>
          val?.toString().includes('1월') ? val.substring(0, 4) : ''
      },
      axisBorder: {
        show: true,
        color: 'rgba(var(--v-theme-borderLight), var(--v-high-opacity))'
      },
      axisTicks: {
        color: 'rgba(var(--v-theme-borderLight), var(--v-high-opacity))'
      }
    },
    yaxis: {
      labels: {
        formatter: (val: number): string =>
          val != null ? `${val.toLocaleString()} 건` : '0 건'
      }
    },
    legend: { show: true },
    tooltip: {
      theme: 'dark',
      y: {
        formatter: (val: number): string =>
          val != null ? `${val.toLocaleString()} 건` : '0 건'
      }
    }

  };
});

const chartOptions2 = computed(() => {
  return {
    chart: {
      type: 'area',
      height: 450,
      fontFamily: `inherit`,
      foreColor: '#a1aab2',
      toolbar: false
    },
    colors: [Color1, Color2, Color3, Color4],
    dataLabels: { enabled: false },
    stroke: { curve: 'smooth', width: 2 },
    fill: {
      type: 'gradient',
      gradient: { shadeIntensity: 1, opacityFrom: 0.7, opacityTo: 0.4, stops: [0, 100] }
    },
    grid: { borderColor: 'rgba(var(--v-theme-borderLight), var(--v-high-opacity))' },
    xaxis: {
      categories: chartLabels.value,
      labels: {
        rotate: 0,
        formatter: (val: string): string =>
          val?.toString().includes('1월') ? val.substring(0, 4) : ''
      },
      axisBorder: {
        show: true,
        color: 'rgba(var(--v-theme-borderLight), var(--v-high-opacity))'
      },
      axisTicks: {
        color: 'rgba(var(--v-theme-borderLight), var(--v-high-opacity))'
      }
    },
    yaxis: {
      labels: {
        formatter: (val: number) => val != null ? `${(val / 100).toFixed(0)} 억` : '0 억'
      }
    },
    tooltip: {
      y: {
        formatter: (val: number) => val != null ? `${(val / 100).toFixed(2)} 억` : '0 억'
      }
    }


  };
});

const tab = ref(1);

onMounted(async () => {
  const res = await fetch('/apt_avg_price.json');
  const raw = await res.json();
  const sheetData = raw.sheet["1"].data;

  const labelMap: Record<string, string> = {};
  for (const col in sheetData["0"]) {
    const label = sheetData["0"][col];
    if (label.includes("년") && label.includes("월")) {
      labelMap[col] = label;
    }
  }

  const labels = Object.values(labelMap);
  chartLabels.value = labels;

  const regions = ["전국", "서울", "경기", "인천", "강원", "충북", "충남", "세종", "대전", "전북", "전남", "광주", "경북", "경남", "대구", "부산", "울산", "제주"];
  const regionData: Record<string, number[]> = {};
  for (const region of regions) regionData[region] = [];

  for (const key in sheetData) {
    const row = sheetData[key];
    const name = row["1"];
    if (regions.includes(name)) {
      const data: number[] = [];
      for (const col in labelMap) {
        const val = row[col]?.replace(/,/g, '');
        if (val && !isNaN(+val)) data.push(parseFloat(val));
      }

      regionData[name] = data;
    }
  }

  const sudogwon = labels.map((_, i) => (
    (regionData["서울"][i] + regionData["경기"][i] + regionData["인천"][i]) / 3).toFixed(1)
  ).map(Number);

  const nonSeoulGyeonggi = regions.filter(r => !["서울", "경기"].includes(r));
  const jibang = labels.map((_, i) => (
    nonSeoulGyeonggi.reduce((sum, r) => sum + regionData[r][i], 0) / nonSeoulGyeonggi.length
  ).toFixed(1)).map(Number);





  // 1. fetch 거래량 데이터
  const dealRes = await fetch('/area_deal.json');
  const dealRaw = await dealRes.json();
  const dealData = dealRaw.sheet["1"].data

  // 2. 거래량 데이터 파싱
  const dealRegionData: Record<string, number[]> = {};
  for (const region of regions) dealRegionData[region] = [];

  for (const key in dealData) {
    if (key === "0") continue;
    const row = dealData[key];
    const name = row["1"];
    if (regions.includes(name)) {
      const data: number[] = [];
      for (const col in labelMap) {
        const val = row[col];
        data.push(val && !isNaN(+val) ? parseInt(val) : 0);
      }
      dealRegionData[name] = data;
    }
  }


  const nationwide = regionData["전국"];



  areaChart2.value.series = [
    { name: "전국", data: nationwide },
    { name: "서울", data: regionData["서울"] },
    { name: "수도권", data: sudogwon },
    { name: "지방", data: jibang }
  ];


  for (const key in dealData) {
    if (key === "0") continue;
    const row = dealData[key];
    const name = row["1"];
    if (regions.includes(name)) {
      const data: number[] = [];
      for (const col in labelMap) {
        const val = row[col];
        if (val && !isNaN(+val)) {
          data.push(parseInt(val));
        } else {
          data.push(0);
        }
      }
      dealRegionData[name] = data;
    }
  }


  const sudogwonVol = labels.map((_, i) => (
    Math.round((dealRegionData["서울"][i] + dealRegionData["경기"][i] + dealRegionData["인천"][i]) / 3)
  ));

  const jibangVol = labels.map((_, i) => (
    Math.round(nonSeoulGyeonggi.reduce((sum, r) => sum + dealRegionData[r][i], 0) / nonSeoulGyeonggi.length)
  ));

  const nationwideVol = labels.map((_, i) => (
    Math.round(regions.reduce((sum, r) => sum + dealRegionData[r][i], 0) / regions.length)
  ));

  areaChart1.value.series = [
    { name: "전국", data: nationwideVol },
    { name: "서울", data: dealRegionData["서울"] },
    { name: "수도권", data: sudogwonVol },
    { name: "지방", data: jibangVol }
  ];
});
</script>




<template>
  <v-card class="title-card" variant="text">
    <v-card-item class="pb-2 px-0 pt-0">
      <div class="d-flex justify-space-between">
        <v-card-title class="text-h5">연도별 동향</v-card-title>
        <div class="d-flex flex-wrap">
          <v-tabs v-model="tab" color="primary" class="tabBtn" density="compact" hide-slider>
            <v-tab value="one" class="mr-1" variant="outlined" rounded="md"> 평균 거래량 </v-tab>
            <v-tab value="two" variant="outlined" rounded="md"> 평균 시세 </v-tab>
          </v-tabs>
        </div>
      </div>
    </v-card-item>
    <v-card-text class="rounded-md overflow-hidden">
      <v-window v-model="tab">

        <v-window-item value="one">
          <apexchart type="area" height="450" :options="chartOptions1" :series="areaChart1.series"> </apexchart>
        </v-window-item>

        <v-window-item value="two">
          <apexchart type="area" height="450" :options="chartOptions2" :series="areaChart2.series"> </apexchart>
        </v-window-item>
      </v-window>
    </v-card-text>
  </v-card>
</template>
