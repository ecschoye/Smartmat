<template>
  <div>
    <RecipeCatalog/>
  </div>
</template>

<script setup lang="ts">
import RecipeCatalog from "~/components/Recipe/RecipeCatalog.vue";

definePageMeta({
  "requiresAuth": true,
  middleware: [
    'auth',
  ],
})
</script>

<style scoped>

</style>