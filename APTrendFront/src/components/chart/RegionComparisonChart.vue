<script setup lang="ts">
import { computed, ref } from 'vue';

const chartOptions1 = computed(() => {
  return {
    yaxis: [
      {
        labels: {
          formatter: (val: number) => `${val.toLocaleString()} 건`
        }
      },
      {
        opposite: true,
        labels: {
          formatter: (val: number) => `${val.toFixed(1)} 억`
        }
      }
    ],


    chart: {
      type: 'bar',
      height: 360,
      fontFamily: `inherit`,
      foreColor: 'rgba(var(--v-theme-secondary), var(--v-high-opacity))',
      offsetY: 20,
      toolbar: {
        show: false
      }
    },
    dataLabels: {
      enabled: false
    },
    plotOptions: {
      bar: {
        columnWidth: '30%',
        borderRadius: 4
      }
    },
    labels: ['서울', '경기', '인천', '부산', '대구', '광주'],
    colors: [
      'rgba(var(--v-theme-warning), var(--v-high-opacity))',
      'rgba(var(--v-theme-primary), var(--v-high-opacity))'
    ],
    stroke: {
      curve: 'smooth',
      width: 6,
      colors: ['transparent']
    },
    fill: {
      opacity: 1
    },
    legend: {
      position: 'top',
      horizontalAlign: 'right',
      markers: {
        width: 15,
        height: 15
      }
    },
    grid: {
      borderColor: 'rgba(var(--v-theme-borderLight), var(--v-high-opacity))'
    },
    tooltip: {
      fixed: {
        enabled: false
      }
    },
    responsive: [
      {
        breakpoint: 400,
        options: {
          plotOptions: {
            bar: {
              columnWidth: '65%'
            }
          }
        }
      },
      {
        breakpoint: 450,
        options: {
          plotOptions: {
            bar: {
              columnWidth: '55%'
            }
          }
        }
      },
      {
        breakpoint: 550,
        options: {
          plotOptions: {
            bar: {
              columnWidth: '45%'
            }
          }
        }
      },
      {
        breakpoint: 650,
        options: {
          plotOptions: {
            bar: {
              columnWidth: '35%'
            }
          }
        }
      },
      {
        breakpoint: 768,
        options: {
          plotOptions: {
            bar: {
              columnWidth: '30%'
            }
          }
        }
      },
      {
        breakpoint: 960,
        options: {
          plotOptions: {
            bar: {
              columnWidth: '30%'
            }
          }
        }
      },
      {
        breakpoint: 1150,
        options: {
          plotOptions: {
            bar: {
              columnWidth: '35%'
            }
          }
        }
      },
      {
        breakpoint: 1280,
        options: {
          plotOptions: {
            bar: {
              columnWidth: '30%'
            }
          }
        }
      },
      {
        breakpoint: 1360,
        options: {
          plotOptions: {
            bar: {
              columnWidth: '35%'
            }
          }
        }
      }
    ]
  };
});

// chart 1
const barChart1 = {
  series: [
    {
      name: '거래량',
      data: [8200, 7400, 5100, 4600, 3900, 3100],
      type: 'column',
      yAxisIndex: 0
    },
    {
      name: '평균 매매가(억원)',
      data: [12.3, 10.1, 7.9, 6.4, 5.8, 4.3],
      type: 'column',
      yAxisIndex: 1
    }
  ]
};


const items = ref(['Today', 'This month', 'This year']);
</script>

<template>
  <v-card class="title-card" variant="text" rounded="md">
    <v-card-item class="pb-2 px-0 pt-0">
      <div class="d-flex justify-space-between">
        <v-card-title class="text-h5">Sales Report</v-card-title>
        <div style="min-width: 160px">
          <v-autocomplete id="autocomplete-input" model-value="Today" :items="items" color="primary" variant="outlined"
            hide-details density="compact"></v-autocomplete>
        </div>
      </div>
    </v-card-item>
    <v-card-text variant="outlined" class="rounded-md">
      <div class="pt-5">
        <h6 class="text-h6 text-lightText mb-3">2025년 4월 기준</h6>
        <h4 class="text-h4 mb-0">거래량 Top 6 지역</h4>
      </div>
      <apexchart type="bar" height="360" class="salesReport" :options="chartOptions1" :series="barChart1.series">
      </apexchart>
    </v-card-text>
  </v-card>
</template>
<style lang="scss">
.salesReport {
  margin-top: -50px;

  .apexcharts-legend {
    .apexcharts-legend-series {
      display: flex !important;
      align-items: center;
    }
  }
}
</style>
