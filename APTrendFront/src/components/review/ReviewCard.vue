<template>
  <v-card outlined>
    <v-card-title class="text-subtitle-1 font-weight-bold">
      {{ review.aptName }}
    </v-card-title>
    <v-card-subtitle>
      ⭐ {{ review.rate }}점
    </v-card-subtitle>
    <v-card-text>{{ review.summary }}</v-card-text>
  </v-card>
</template>

<script setup>
defineProps({
  review: Object
});
</script>
