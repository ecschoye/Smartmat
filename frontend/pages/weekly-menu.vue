<template>
    <div class="weekly-menu">
        <RecipePool/>
    </div>

</template>

<script>
import RecipePool from '~/components/WeeklyMenu/RecipePool.vue';
export default {
    components: { RecipePool }
}

definePageMeta({
  "requiresAuth": true,
  middleware: [
    'auth',
  ],
})
</script>

<style>
.weekly-menu {
    margin: 20px;
}
</style>