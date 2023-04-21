<template>
  <div class="box">
    <h1 class="text-center"> {{ title }}</h1>
  </div>
</template>

<script setup lang="ts">

import { defineProps } from "vue";

const props = defineProps({
  title: {
    type: String,
  }
});
</script>


<style scoped>
h1 {
  font-size: 30px;
  margin: 0;
}

.box {
  width: 350px !important;
  height: 150px !important;
  background-color: white;
  border-radius: 20px;
  margin-right: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.inner {
  margin: 20px;
  height: calc(100% - 40px);
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.icon {
  margin-right: 10px;
  font-size: 3rem;
  padding-top: 5px;
}

.box:hover {
  background-color: #efefef;
  cursor: pointer;
  transform: scale(1.05);
  transition: transform .5s ease-out;
}
</style>