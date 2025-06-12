<template>
  <div class="right-sidebar">
    <h3>📈 급상승 아파트</h3>
    <ul>
      <li v-for="apt in topRisingApts" :key="apt.id">
        <strong>{{ apt.name }}</strong> (+{{ (apt.rate/100).toFixed(1) }}%)<br />
        <small>{{ apt.price/10000 }}억</small>
      </li>
    </ul>
    <hr />
    <h4>📊 통계</h4>
    <p>전체 평균 변동률: {{ avgRate }}%</p>
    <p>상승 아파트 수: {{ positiveCount }}개</p>
    <p>하락 아파트 수: {{ negativeCount }}개</p>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  apartments: {
    type: Array,
    default: () => []
  }
});

console.log('🔥 props.apartments:', props.apartments);


// ✅ 상위 5개 상승 아파트 정렬
const topRisingApts = computed(() =>
  [...props.apartments]
    .filter(a => typeof a.rate === 'number' && a.rate > 0)
    .sort((a, b) => b.rate - a.rate)
    .slice(0, 5)
);

// ✅ 평균 변동률 계산
const avgRate = computed(() => {
  const valid = props.apartments.filter(a => typeof a.rate === 'number');
  if (valid.length === 0) return '0.00';
  const total = valid.reduce((sum, a) => sum + a.rate, 0);
  return (total / valid.length).toFixed(2);
});

// ✅ 상승/하락 개수 계산
const positiveCount = computed(() =>
  props.apartments.filter(a => a.rate > 0).length
);

const negativeCount = computed(() =>
  props.apartments.filter(a => a.rate < 0).length
);
</script>

<style scoped>
.right-sidebar {
  position: absolute;
  top: 60px;
  right: 10px;
  width: 260px;
  background-color: #f9f9f9;
  padding: 16px;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  z-index: 1002;
  font-size: 14px;
  transition: all 0.3s ease-in-out;
}

.right-sidebar h3,
.right-sidebar h4 {
  font-size: 16px;
  margin-bottom: 12px;
  color: #333;
}

.right-sidebar ul {
  list-style: none;
  padding: 0;
  margin: 0 0 16px 0;
}

.right-sidebar li {
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #ddd;
  font-size: 14px;
  color: #444;
}

.right-sidebar li strong {
  display: inline-block;
  margin-bottom: 2px;
  font-weight: 600;
  color: #000;
}

.right-sidebar li small {
  color: #777;
  font-size: 13px;
}

.right-sidebar p {
  margin: 6px 0;
  font-size: 13px;
  color: #555;
}
</style>
