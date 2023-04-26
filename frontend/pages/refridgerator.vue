<template>
    <RefridgeratorWrapper />
</template>


<script setup lang="ts">
definePageMeta({
  "requiresAuth": true,
  middleware: [
    'auth',
  ],
})
</script>