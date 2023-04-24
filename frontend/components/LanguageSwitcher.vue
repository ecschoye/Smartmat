<template>
  <select v-model="language" class="bg-white dark:bg-zinc-400 px-4 py-2 rounded-md border dark:border-zinc-600 focus:outline-none focus:ring-2 focus:ring-blue-500 appearance-none" style="-webkit-appearance: none;">
    <option v-for="locale in locales"
            :key="locale.code"
            :value="locale.code"
            class="py-1 hover:bg-red-500">
      {{ locale.name }}
    </option>
  </select>
</template>




<script setup lang="ts">
const { locales, locale, setLocale} = useI18n();

const switchLocalePath = useSwitchLocalePath()

const language = computed({
  get: () => locale.value,
  set: (value) => {
    setLocale(value)
  },
});

</script>

<style scoped>

</style>