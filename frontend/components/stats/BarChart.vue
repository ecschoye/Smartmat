<template>
    <div class="w-4/5">
        <canvas ref="chart"></canvas>
        <div class="flex flex-row justify-center p-5 font-bold">
            <div class="w-80 bg-zinc-200 dark:bg-zinc-700 dark:text-white border-2 border-black dark:border-white rounded-3xl p-10 m-5">
               <h1 class="flex justify-center text-xl mb-3"> Total Food Eaten </h1>
               <h3 class="flex justify-center text-green-custom text-4xl"> {{ totalFoodEaten }} KG </h3> 
               <h3 class="flex justify-center text-green-custom text-xl"> {{ getTotalFoodEatenPercentage() }} % </h3> 
            </div>
            <div class="w-80 bg-zinc-200 dark:bg-zinc-700 dark:text-white border-2 border-black dark:border-white rounded-3xl p-10 m-5">
                <h1 class="flex justify-center text-xl mb-3"> Total Food Wasted </h1>
               <h3 class="flex justify-center text-red-400 text-4xl"> {{ totalFoodWaste }} KG </h3> 
               <h3 class="flex justify-center text-red-400 text-xl"> {{ getTotalFoodWastePercentage() }} % </h3> 
            </div>
        </div>
    </div>    
  </template>
  
  <script lang="ts">
  import Chart from 'chart.js/auto';
  import StatsService from '~/service/httputils/StatsService';
  
  export default {
    data() {
      return {
        statMonths: [
          {
            name: "January, 2023",
            foodEaten: 3,
            foodWaste: 2
          },
          {
            name: "February, 2023",
            foodEaten: 7,
            foodWaste: 5
          },
          {
            name: "March, 2023",
            foodEaten: 5,
            foodWaste: 4
          },
          {
            name: "April, 2023",
            foodEaten: 6,
            foodWaste: 4
          },
          {
            name: "May, 2023",
            foodEaten: 4,
            foodWaste: 3
          },
          {
            name: "June, 2023",
            foodEaten: 5,
            foodWaste: 4
          },
          {
            name: "July, 2023",
            foodEaten: 3,
            foodWaste: 2
          },
          {
            name: "August, 2023",
            foodEaten: 6,
            foodWaste: 3
          },
          {
            name: "September, 2023",
            foodEaten: 5,
            foodWaste: 3
          },
          {
            name: "October, 2023",
            foodEaten: 5,
            foodWaste: 2
          },
          {
            name: "November, 2023",
            foodEaten: 4,
            foodWaste: 3
          },
          {
            name: "December, 2023",
            foodEaten: 5,
            foodWaste: 2
          }
        ] as StatMonth[],
        totalFoodEaten: 0,
        totalFoodWaste: 0,
        totalFood: 0,
        chart: null as any,
      };
    },
    methods: {
      createChart() {
        if (this.chart) {
          this.chart.destroy();
        }
        this.chart = new Chart(this.$refs.chart, {
          type: 'bar',
          data: {
            labels: this.statMonths.map((month) => String(month.name)),
            datasets: [
              {
                label: 'Food Eaten',
                backgroundColor: '#31C48D',
                data: this.statMonths.map((month) => Number(month.foodEaten)),
                borderColor: 'black',
                borderWidth: 1,
                borderRadius: 15,
              },
              {
                label: 'Food Wasted',
                backgroundColor: '#cc1244',
                data: this.statMonths.map((month) => Number(month.foodWaste)),
                borderColor: 'black',
                borderWidth: 1,
                borderRadius: 15,
              },
            ],
          },
          options: {
            scales: {
              yAxes: [
                {
                  ticks: {
                    beginAtZero: true,
                  },
                },
              ],
            },
          },
        });
      },
      async fetchData() {
        //const response = await StatsService.getStatsData()
        //this.statMonths = response.data;
        this.createChart();
      },
      calculateTotalFoodStats() {
            this.statMonths.forEach((month) => {
                this.totalFoodEaten += month.foodEaten;
                this.totalFoodWaste += month.foodWaste;
            });
            this.totalFood = this.totalFoodEaten + this.totalFoodWaste
        },
      getTotalFoodEatenPercentage() {
        return (this.totalFoodEaten / this.totalFood * 100).toFixed(2)
      },
      getTotalFoodWastePercentage() {
        return (this.totalFoodWaste / this.totalFood * 100).toFixed(2)
      }
    },
    mounted() {
      this.fetchData();
      this.calculateTotalFoodStats();
    },
    destroyed() {
      if (this.chart) {
        this.chart.destroy();
      }
    },
  };
  </script>
  