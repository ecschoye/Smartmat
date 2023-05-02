<template>
  <div class="flex justify-center mt-10  text-white">
    <h1 class="text-6xl">{{t('your_recipes')}}</h1>
  </div>
  <div>
    <RecipeCatalog/>
  </div>
</template>

<script setup lang="ts">
import RecipeCatalog from "~/components/Recipe/RecipeCatalog.vue";

const { t } = useI18n();

definePageMeta({
  "requiresAuth": true,
  middleware: [
    'auth',
  ],
})
</script>

<style scoped>

</style>