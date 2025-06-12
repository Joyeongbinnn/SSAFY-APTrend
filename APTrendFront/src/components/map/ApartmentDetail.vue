<script setup>
import { ref, onMounted } from 'vue';
import ApexChart from 'vue3-apexcharts';
import { StarFilled, StarOutlined } from '@ant-design/icons-vue';

const props = defineProps({
  apartment: Object,
  isFavorite: Function
});

const dealSeries = ref([]);
const isLoadingPrediction = ref(true);
const predictionResult = ref({ nextMonth: null, nextYear: null, nextDecade: null });

const chartOptions = ref({
  chart: {
    type: 'line',
    height: 300,
    zoom: { enabled: false }
  },
  xaxis: {
    categories: [],
    labels: {
      formatter: (val) => {
        if (typeof val === 'string' && val.includes('-')) {
          const [year] = val.split("-");
          return `${year}년`;
        }
        return val;
      },
      rotate: 0,
      showDuplicates: false
    },
    title: { text: '거래 년도' }
  },
  yaxis: {
    labels: {
      formatter: (value) => {
        // 10000으로 나눠서 억 단위로 표시
        return `₩${(value / 10000).toLocaleString()}억`;
      }
    },
    title: {
      text: '가격 (억)'
    }
  },
  tooltip: {
    custom: ({ series, seriesIndex, dataPointIndex, w }) => {
      const rawDeals = w?.config?.series?.[seriesIndex]?.rawDeals || [];
      const deals = rawDeals?.[dataPointIndex] || [];
      const avg = series?.[seriesIndex]?.[dataPointIndex];

      const fullLabel = w?.config?.xaxis?.categories?.[dataPointIndex] ?? '알 수 없음';
      const [year, month] = fullLabel.includes('-') ? fullLabel.split('-') : ['연도', '월'];
      const formattedLabel = `${year}년 ${month}월`;

      return `
        <div style="padding:8px">
          <b>${formattedLabel}</b><br/>
          평균: ₩${(avg / 10000)?.toLocaleString() ?? 'N/A'}억<br/>
          거래가: ${Array.isArray(deals)
            ? deals.map(p => `₩${(p / 10000).toLocaleString()}억`).join(', ')
            : '없음'}
        </div>
      `;
    }
  },
  stroke: {
    curve: 'smooth',
    width: 3
  },
  markers: {
    size: 5,
    colors: ['#1D4ED8'],
    strokeColors: '#fff',
    strokeWidth: 2
  },
  colors: ['#3B82F6', '#EF4444'] // 기본 + 예측 라인 색
});

onMounted(async () => {
  const res = await fetch(`/api/apartments/monthly-deals?roadAddress=${encodeURIComponent(props.apartment.address)}&aptName=${encodeURIComponent(props.apartment.name)}`);
  const data = await res.json();

  chartOptions.value.xaxis.categories = data.map(d => d.month);
  dealSeries.value = [{
    name: '평균 거래가',
    data: data.map(d => d.average),
    rawDeals: data.map(d => d.deals)
  }];

  // 📡 AI 예측 요청
  try {
    const predRes = await fetch(`/api/apartments/predict?roadAddress=${encodeURIComponent(props.apartment.address)}&aptName=${encodeURIComponent(props.apartment.name)}`);
    const predData = await predRes.json();
    predictionResult.value = predData;

    const futureLabels = ['+1달', '+1년', '+10년'];
    const prediction = predData.prediction;
    const futureData = [
      prediction['1_month'],  // 또는 prediction["1_month"]
      prediction['1_year'],
      prediction['10_years']
    ];

    chartOptions.value.xaxis.categories.push(...futureLabels);
    dealSeries.value.push({
      name: 'AI 예측값',
      data: [...Array(data.length).fill(null), ...futureData],
      rawDeals: [],
    });
  } catch (e) {
    console.error('AI 예측 실패:', e);
  } finally {
    isLoadingPrediction.value = false;
  }
});
</script>

<template>
  <v-card class="apartment-card" elevation="2">
    <!-- ⭐ 즐겨찾기 토글 버튼 -->
    <v-btn icon class="favorite-top-button" @click="$emit('toggle-favorite', apartment)">
      <component :is="isFavorite(apartment) ? StarFilled : StarOutlined" />
    </v-btn>

    <v-card-title>{{ apartment.name }}</v-card-title>

    <v-card-text>
      <p>📈 전달 대비: {{ apartment.rate }}%</p>

      <!-- 📊 차트 영역 -->
      <apex-chart
        :options="chartOptions"
        :series="dealSeries"
        :key="chartOptions.xaxis.categories.join(',')"
        type="line"
        height="300"
      />

      <!-- 🔮 AI 예측 결과 or 로딩 -->
      <div v-if="isLoadingPrediction">
        🤖 AI가 거래 데이터를 분석 중입니다...
      </div>
      <div v-else>
        🔮 <b>예측 결과</b><br />
        📅 1달 뒤: ₩{{ (predictionResult.prediction['1_month'] / 10000).toLocaleString() }}억<br />
        📅 1년 뒤: ₩{{ (predictionResult.prediction['1_year'] / 10000).toLocaleString() }}억<br />
        📅 10년 뒤: ₩{{ (predictionResult.prediction['10_years'] / 10000).toLocaleString() }}억
      </div>

      <p>💬 이 아파트에 대한 리뷰를 확인하려면 '리뷰 보기'를 클릭하세요.</p>
    </v-card-text>

    <v-card-actions>
      <v-spacer />
      <v-btn color="primary" @click="$emit('close')">닫기</v-btn>
    </v-card-actions>
  </v-card>
</template>

<style scoped>
.apartment-card {
  position: relative;
}

.favorite-top-button {
  position: absolute;
  top: 10px;
  right: 10px;
  color: #ffc107;
  background-color: white;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
  z-index: 10;
}
</style>
