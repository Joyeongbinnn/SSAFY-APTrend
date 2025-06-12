<script setup>
import AptTrendSummary from '@/components/dashboard/AptTrendSummary.vue';
import PriceTrendChart from '@/components/chart/PriceTrendChart.vue';
import VolumeTrendChart from '@/components/chart/VolumeTrendChart.vue';
import TopRiseApartTable from '@/components/table/TopRiseApartTable.vue';
import AnalyticsSummary from '@/components/report/AnalyticsSummary.vue';
import RegionComparisonChart from '@/components/chart/RegionComparisonChart.vue';
import RecentIssues from '@/components/report/RecentIssues.vue';
import FeedbackChat from '@/components/common/FeedbackChat.vue';
</script>

<template>
  <!-- 상단 요약 카드 -->
  <AptTrendSummary />

  <v-row class="mb-0">
    <v-col cols="12" md="8">
      <PriceTrendChart />
    </v-col>
    <v-col cols="12" md="4">
      <VolumeTrendChart />
    </v-col>
  </v-row>

  <v-row class="mb-0">
    <v-col cols="12" md="8">
      <TopRiseApartTable />
    </v-col>
    <v-col cols="12" md="4">
      <AnalyticsSummary />
    </v-col>
  </v-row>

  <v-row>
    <v-col cols="12" md="7">
      <RegionComparisonChart />
    </v-col>

    <v-col cols="12" md="5">
      <v-row>
        <v-col cols="12">
          <RecentIssues />
        </v-col>
        <v-col cols="12">
          <FeedbackChat />
        </v-col>
      </v-row>
    </v-col>
  </v-row>
</template>
