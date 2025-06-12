<template>
  <div class="logo">
    <RouterLink to="/" class="logo-link" aria-label="logo">
      <img src="/assets/APTrendIcon.png" alt="APTTrend Logo" class="logo-image" />
      <span class="logo-text">APTrend</span>
    </RouterLink>
  </div>
</template>

<script setup>
import { RouterLink } from 'vue-router';
</script>

<style scoped>
.logo {
  padding-left: 0px;
  padding-bottom: 0px;
  margin-left: -4px;
  margin-bottom: -10px;
}

.logo-link {
  display: flex;
  align-items: center;
  text-decoration: none;
}

.logo-image {
  height: 80px; /* 더 크게 조정 */
  margin-right: 0px;
}

.logo-text {
  font-size: 20px;
  font-weight: 700;
  margin-left: -4px;
  color: #000;
}
</style>
