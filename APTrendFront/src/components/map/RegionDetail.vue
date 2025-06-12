<!-- src/components/map/RegionDetail.vue -->
<template>
  <v-card elevation="6" class="pa-4">
    <v-card-title class="headline text-h6">
      🏙️ {{ regionName }}
    </v-card-title>

    <v-divider class="my-2" />

    <v-card-text>
      <p>
        <strong>📈 지난달 대비 변동률:</strong>
        <span :class="rateClass">{{ formattedRate }}</span>
      </p>
      <p>
        <strong> 🆔 지역 코드:</strong> {{ regionCode }}
      </p>
    </v-card-text>

    <v-card-actions class="justify-end">
      <v-btn color="primary" variant="outlined" @click="$emit('close')">닫기</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  regionName: String,
  regionRate: Number,
  regionCode: String
});

const formattedRate = computed(() => {
  if (props.regionRate == null) return '-';
  return props.regionRate.toFixed(2) + '%';
});

const rateClass = computed(() => {
  if (props.regionRate > 0) return 'text-positive';
  if (props.regionRate < 0) return 'text-negative';
  return 'text-neutral';
});

</script>

<style scoped>
.text-positive {
  color: #e53935;
  font-weight: bold;
}
.text-negative {
  color: #1e88e5;
  font-weight: bold;
}
.text-neutral {
  color: #555;
}
</style>
