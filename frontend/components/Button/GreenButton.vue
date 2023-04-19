<template>
  <button class="text-white rounded hover:bg-green-700" :style="{ width: width, height: height }">
    {{ label }}
  </button>
</template>

<script setup>
const { label, width = '', height = '' } = defineProps({
  label: String,
  width: String,
  height: String
});
</script>

<style scoped>
button {
  background-color: #31C48D;
  border: none;
  outline: none;
  cursor: pointer;
  transition: all 0.2s;
}
</style>
