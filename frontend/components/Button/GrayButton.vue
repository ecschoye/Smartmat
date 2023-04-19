<template>
  <button class="text-black bg-gray-400 rounded hover:bg-gray-600" :style="{ width: width, height: height }">
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
  border: none;
  outline: none;
  cursor: pointer;
}
</style>
